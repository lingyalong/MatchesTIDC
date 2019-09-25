package com.tidc.match.service;

import com.tidc.match.exception.PeyondPowerException;
import com.tidc.match.exception.RepetitionException;
import com.tidc.match.mapper.MatchsMapper;
import com.tidc.match.mapper.TeamMapper;
import com.tidc.match.mapper.UserMapper;
import com.tidc.match.mapper.WorksMaper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.*;
import com.tidc.match.utiles.ApplicationContextProvider;
import com.tidc.match.utiles.FileHandel;
import com.tidc.match.utiles.ImageCheck;
import org.apache.catalina.User;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ClassNmae MatchService
 * @Description TODO
 * @Author 14631
 **/
@Service
public class FoundService {
	@Autowired
	MatchsMapper matchsMapper;
	@Autowired
	public UserMapper userMapper;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	ImageCheck imageCheck;
	@Autowired
	FileHandel fileHandel;
	@Autowired
	TeamMapper teamMapper;
	@Autowired
	WorksMaper worksMaper;
	@CacheEvict(cacheNames = {"check"},cacheManager = "UserOVRedisCacheManager",allEntries = true)
	public UserOV postMatch(UserDetails userDetails, Matchs matchs, UserOV userOV) throws IOException {

		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
		matchs.setNumber(0);
		matchs.setTeacher_id(teacher.getId());
		matchs.setSchool(teacher.getSchool());
		matchs.setFlag(1);
		matchsMapper.insertMatchs(matchs);
		Power power = ac.getBean(Power.class);
		power.setPower_matchs_id(matchs.getId());
		power.setPower_teacher_id(teacher.getId());
		matchsMapper.addPower(power);
		userOV.setCode(200).setMessage("创建成功").setData(matchs);
		return userOV;
	}
//	public UserOV addmypower(UserDetails userDetails,HttpServletRequest req,UserOV userOV){
//		HttpSession hs = req.getSession();
//		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
//		int matchsid = (int) hs.getAttribute("matchsId");
//		Power power = ac.getBean(Power.class);
//		power.setPower_matchs_id(matchsid);
//		power.setPower_teacher_id(teacher.getId());
//		matchsMapper.addPower(power);
// 		userOV.setCode(200).setMessage("添加成功");
//		return userOV;
//	}
	public UserOV addTeacher(UserDetails userDetails,String email,int matchsId,UserOV userOV,HttpServletRequest req) throws NotFoundException, PeyondPowerException {
		Teacher teacher2 = userMapper.selectTeacher(userDetails.getUsername());
		userOV = this.ifMatchs(userDetails,matchsId,req,userOV);
		if(userOV.getCode()==500){
			throw new PeyondPowerException(teacher2.getUserName(),teacher2.getId());
		}
		Teacher teacher = userMapper.findTeacher(email);
		if(teacher==null){
			return userOV.setCode(404).setMessage("添加的用户对象不存在");
		}
		Power power = ac.getBean(Power.class);
		power.setPower_teacher_id(teacher.getId());
		power.setPower_matchs_id(matchsId);
		matchsMapper.addPower(power);
		userOV.setCode(200).setMessage("添加成功");
		return userOV;
	}
	public UserOV ifMatchs(UserDetails userDetails,int matchsId,HttpServletRequest req,UserOV userOV) throws NotFoundException {
		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
		CheckService checkService= ac.getBean(CheckService.class);
		userOV = checkService.getMatchs(matchsId,userOV);
		Matchs matchs = (Matchs) userOV.getData();
		if(matchs.getTeacher_id()!=teacher.getId()){
			//判断这个老师是不是这个比赛的创始人
			return userOV.setCode(500).setMessage("当前用户对象没有权限");
		}
		return userOV.setCode(200).setMessage("可以修改该项目").setData(matchs);
	}
	@CacheEvict(cacheNames = {"check"},cacheManager = "UserOVRedisCacheManager",allEntries = true)
	public UserOV putMatchs(UserDetails userDetails,Matchs matchs, HttpServletRequest req, UserOV userOV) throws IOException, NotFoundException, PeyondPowerException {
		//权限查询
		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
		userOV = this.ifMatchs(userDetails,matchs.getId(),req,userOV);
		if(userOV.getCode()==500){
			throw new PeyondPowerException(teacher.getUserName(),teacher.getId());
		}
		//		//这里要获取之前的match数据然后删除文件
		Matchs matchs1 = matchsMapper.selectMatchs(matchs.getId());
		//根据matchid获得match
		boolean flag=false;
		if(matchs1.getUrl()!=null){
			flag = fileHandel.deleteFile(matchs1.getUrl());
		}
		if(flag==false){
			userOV.setCode(500).setMessage("文件修改失败").setData(null);
			return userOV;
		}
		if(matchs1.getLogo()!=null&&flag){
			flag = fileHandel.deleteFile(matchs1.getLogo());
		}
		if(flag==false){
			userOV.setCode(500).setMessage("文件修改失败").setData(null);
			return userOV;
		}
		matchsMapper.updateMatch(matchs);
		userOV.setMessage("ok").setCode(200).setData(matchs);
		return userOV;
	}
	public UserOV foundWorks(UserDetails userDetails, Works works, UserOV userOV, HttpServletRequest req) throws IOException, RepetitionException {
		Student student = userMapper.selectStudent(userDetails.getUsername());
		//查重
		Map map = new HashMap();
		map.put("student",student.getId());
		map.put("match",works.getWorks_matchs_id());
		Works works2 = null;
		works2 = worksMaper.repetition(map);
		if (works2!=null){
			throw new RepetitionException(student.getName());
		}
		//创建比赛队伍
		Team team = ac.getBean(Team.class);
		team.setName(works.getName()+"队");
		team.setTeam_student_id(student.getId());
		teamMapper.foundTeam(team);
		works.setWorks_team_id(team.getId());
		works.setScore(0);
		works.setWorks_student_id(student.getId());
		worksMaper.foundWorks(works);
		team.setTeam_works_id(works.getId());
		teamMapper.upMatchId(team);
		userOV.setCode(200).setMessage("创建成功");
		//这里是match的number++
		matchsMapper.numberUP(works.getWorks_matchs_id());
		return userOV;
	}
	@CacheEvict(cacheNames = {"check"},cacheManager = "UserOVRedisCacheManager",allEntries = true)
	public UserOV putWorks(UserOV userOV,Works works,UserDetails userDetails) throws IOException, PeyondPowerException {
//		权限管理
		Student student = userMapper.selectStudent(userDetails.getUsername());
		Map map = new HashMap();
		map.put("studentId", student.getId());
		map.put("worksId", works.getId());
		Works works2 = null;
		works2 = worksMaper.affirmUrl(map);
		if (works2 == null) {
			throw new PeyondPowerException(student.getName(),student.getId());
		}
		works.setWorks_student_id(student.getId());
		//这里需要删除旧文件
		boolean flag=false;
		if(works2.getUrl()!=null){
			flag = fileHandel.deleteFile(works2.getUrl());
		}
		if(flag==false){
			userOV.setCode(500).setMessage("文件修改失败").setData(null);
			return userOV;
		}
		if(works2.getLogo()!=null&&flag){
			flag = fileHandel.deleteFile(works2.getLogo());
		}
		if(flag==false){
			userOV.setCode(500).setMessage("文件修改失败").setData(null);
			return userOV;
		}

		worksMaper.putWorks(works);
		userOV.setCode(200).setMessage("修改成功").setData(works);
		return userOV;
	}
}
