package com.tidc.match.controller.passwordController;

import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Student;
import com.tidc.match.pojo.Teacher;
import com.tidc.match.service.PasswordService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassNmae StudentPassswordController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin //解决跨域问题
@Api(value = "找回密码")
@RequestMapping("/register")
@RestController()
public class StudentPassswordController {
	@Autowired
	JavaMailSenderImpl javaMailSender;
	@Autowired
	PasswordService passwordServicer;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	@ApiOperation("发送邮件需要一个email")
	@GetMapping("/password/email")
	public UserOV getEmail(String email, HttpServletRequest req){
		UserOV userOV = new UserOV();
		return passwordServicer.email(email,req,userOV);
	}

	@ApiOperation("找回学生密码,需要一个name和email和code就行了")
	@GetMapping("/find/student")
	public UserOV findStudentPassword(Student student, String code, HttpServletRequest req){
		UserOV userOV = new UserOV();
		return passwordServicer.findStudentPassord(student,code,req,userOV);
	}

	@ApiOperation("找回老师密码 ,需要一个userName和email和code就行了")
	@GetMapping("/find/teacher")
	public UserOV findTeacherPassword(Teacher teacher, String code, HttpServletRequest req){
		UserOV userOV = new UserOV();
		return passwordServicer.findteacherPassord(teacher,code,req,userOV);
	}

	@ApiOperation("注册学生账号")
	@PostMapping("/password/student")
	public UserOV registerStudent(@Validated @RequestBody Student student){
		UserOV  userOV = ac.getBean(UserOV.class);
		userOV = passwordServicer.registerStudent(student,userOV);
		return userOV;
	}
	@ApiOperation("注册老师账号")
	@PostMapping("/password/teacher")
	public UserOV registerTeacher(@Validated @RequestBody Teacher teacher){
		UserOV  userOV = ac.getBean(UserOV.class);
		userOV = passwordServicer.registerTeacher(teacher,userOV);
		return userOV;
	}
	@PostMapping("/regist")
	public UserOV regist(Student student,HttpServletRequest req){
		UserOV  userOV = ac.getBean(UserOV.class);
		userOV = passwordServicer.register(student,userOV,req);
		return userOV;
	}
}
