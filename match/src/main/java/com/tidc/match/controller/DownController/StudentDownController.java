package com.tidc.match.controller.DownController;

import com.tidc.match.ov.UserOV;
import com.tidc.match.service.DownService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassNmae StudentDownController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin //解决跨域问题
@Api(value = "文件下载")
@RequestMapping("/student")
@RestController
public class StudentDownController {
	@Autowired
	private ApplicationContextProvider ac;
	@Autowired
	private DownService downService;
	Logger logger = LoggerFactory.getLogger(StudentDownController.class);

	@GetMapping("/down/work/file/{worksId}")
	@ApiOperation("学生下载学生项目文件需要下载的项目的id")
	//需要一个权限判断是否是自己上传的文件
	public UserOV downFile(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("worksId") int worksId, HttpServletResponse resp){
		UserOV userOV = ac.getBean(UserOV.class);
		try {
			downService.studentDownFile(resp,worksId,userOV,userDetails);
		} catch (Exception e) {
			e.printStackTrace();
			userOV.setCode(500).setMessage("下载出错");
		}
		return userOV;
	}
	@ApiOperation("下载比赛的详细信息文件需要比赛的id")
	@GetMapping("/down/match/file/{id}")
	public UserOV downMatchFile(@PathVariable("id") int id,HttpServletResponse resp){
		UserOV userOV = ac.getBean(UserOV.class);
		try {
			userOV = downService.downMatchFile(userOV, id,resp);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("比赛文件下载失败比赛id为"+id);
		}
		return userOV;
	}
	@ApiOperation("上传文件,需要一个文件 一个比赛名字一个 数字老师为1 学生为2")
//	图片200 其他201
	@PostMapping("/upload/file")
	public UserOV uploadFile(MultipartFile file,String name,int id,@AuthenticationPrincipal UserDetails userDetails){
		UserOV userOV = ac.getBean(UserOV.class);
		try {
			System.out.println(file.getName());
			userOV = downService.uploadFile(file,name,id,userOV,userDetails);
			System.out.println(userOV.getData());
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("文件上传失败");
		}
		return userOV;
	}
}
