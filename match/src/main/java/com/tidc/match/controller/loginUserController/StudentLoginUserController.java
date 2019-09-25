package com.tidc.match.controller.loginUserController;

import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Student;
import com.tidc.match.service.LoginService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @ClassNmae StudentLoginUserController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin //解决跨域问题
@Api(value = "登录用户信息的获取")
@RequestMapping("/student")
@RestController()
public class StudentLoginUserController {
	@Autowired
	private LoginService service;
	@Autowired
	ApplicationContextProvider ac;
	@ApiOperation("获取当前登录账号的个人信息如果没有登录就返回一个404状态码和,message:用户未登录 一个null,成功就是200和获取成功和用户数据")
	@GetMapping("/login/user")
	public UserOV getStudent(String access_token){
		System.out.println("获取信息");
		UserOV userOV = ac.getBean(UserOV.class);
		return service.getStudent(userOV,access_token);
	}
	@ApiOperation(value = "用户登录")
	@PostMapping("/login")
	public UserOV login(Student student) throws IOException{
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = service.login(userOV, student);;
		return userOV;
	}
}
