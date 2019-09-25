package com.tidc.match.service;

import com.tidc.match.exception.PeyondPowerException;
import com.tidc.match.mapper.MatchsMapper;
import com.tidc.match.mapper.UserMapper;
import com.tidc.match.mapper.WorksMaper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Matchs;
import com.tidc.match.pojo.Student;
import com.tidc.match.pojo.Teacher;
import com.tidc.match.pojo.Works;
import com.tidc.match.utiles.ApplicationContextProvider;
import com.tidc.match.utiles.FileHandel;
import com.tidc.match.utiles.ImageCheck;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassNmae DownService
 * @Description TODO
 * @Author 14631
 **/
@Service
public class DownService {
	@Autowired
	private FileHandel fileHandel;
	@Autowired
	private WorksMaper worksMaper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private MatchsMapper matchsMapper;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	ImageCheck imageCheck;
	public UserOV teacherDownFile(HttpServletResponse resp,int worksId,UserOV userOV) throws Exception {
		Works works = null;
		works = matchsMapper.selectWorks(worksId);
		fileHandel.downloadFile(resp,works.getUrl(),works.getName()+".zip");
		userOV.setCode(200).setMessage("下载开始");
		return userOV;
	}
	public UserOV studentDownFile(HttpServletResponse resp,int worksId, UserOV userOV, UserDetails userDetails) throws Exception {

		Student student = userMapper.selectStudent(userDetails.getUsername());
		Map map = new HashMap();
		map.put("studentId",student.getId());
		map.put("worksId",worksId);
		Works works = null;
		works = worksMaper.affirmUrl(map);
		if (works!=null){
			fileHandel.downloadFile(resp,works.getUrl(),works.getName()+".zip");
			userOV.setCode(200).setMessage("下载开始");
		}else {
			throw new PeyondPowerException(student.getName(),student.getId());
		}
		return userOV;
	}
	public UserOV downMatchFile(UserOV userOV,int id,HttpServletResponse resp) throws Exception {
		Matchs matchs = null;
		matchs = matchsMapper.selectMatchs(id);
		if(matchs!=null){
			fileHandel.downloadFile(resp,matchs.getUrl(),matchs.getName()+".zip");
			userOV.setCode(200).setMessage("查询成功");
		}else{
			throw new NotFoundException("这个比赛不存在 id:"+id);
		}
		return userOV;

	}
	public UserOV uploadFile(MultipartFile file,String name,int id,UserOV userOV,UserDetails userDetails) throws IOException {
		String fileName = null;
		String UploadName = file.getOriginalFilename();
		String fileType = UploadName.substring(UploadName.indexOf("."));
		System.out.println(fileType+"     dasd");
		if(fileType.equals(".jpg")){
			userOV.setCode(200);
		}else{
			userOV.setCode(201);
		}
		if(id==1){
			Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
			FileHandel fileHandel = ac.getBean(FileHandel.class);
			fileName = fileHandel.upfile(file,teacher.getId(),name);
		}else{
			Student student = userMapper.selectStudent(userDetails.getUsername());
			FileHandel fileHandel = ac.getBean(FileHandel.class);
			fileName = fileHandel.upfile(file,student.getId(),name);

			System.out.println(fileName);

		}

		return userOV.setMessage("上传成功").setData(fileName);
	}
}
