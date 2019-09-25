package com.tidc.match.service;

import com.tidc.match.mapper.UserMapper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Student;
import com.tidc.match.pojo.Teacher;
import com.tidc.match.utiles.ApplicationContextProvider;
import com.tidc.match.utiles.EmailUtiles;
import com.tidc.match.utiles.RegisterUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassNmae PasswordService
 * @Description TODO
 * @Author 14631
 **/
@Service
public class PasswordService {
	@Autowired
	public UserMapper userMapper;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	JavaMailSenderImpl javaMailSender;
	@Autowired
	EmailUtiles emailUtiles;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	RegisterUtiles registerUtiles;
	@Autowired
	ProviderSignInUtils providerSignInUtils;
	public UserOV email(String email, HttpServletRequest req,UserOV userOV){
		int number = (int) (Math.random()*10000);
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setSubject("博海报名系统验证码");
		simpleMailMessage.setText(number+"");
		simpleMailMessage.setTo(email);
		simpleMailMessage.setFrom("1463158635@qq.com");
		javaMailSender.send(simpleMailMessage);
		req.getSession().setAttribute(email+"Properties",number);
		userOV.setMessage("邮件已发送").setCode(200);
		return userOV;
	}
	public UserOV findStudentPassord(Student student, String code, HttpServletRequest req,UserOV userOV){

		return emailUtiles.email(student,code,req,userOV,"student");
	}
	public UserOV findteacherPassord(Teacher teacher, String code, HttpServletRequest req, UserOV userOV){
		return emailUtiles.email(teacher,code,req,userOV,"teacher");
	}
	public UserOV registerStudent(Student student, UserOV userOV){
		userOV = registerUtiles.register(student,null,userOV);
		if(userOV.getData()!=null){
			userMapper.addStudent((Student) userOV.getData());
		}
		return userOV;
	}
	public UserOV registerTeacher(Teacher teacher, UserOV userOV){
		userOV = registerUtiles.register(null,teacher,userOV);
		if(userOV.getData()!=null){
			userMapper.addTeacher((Teacher) userOV.getData());
		}
		return userOV;
	}

	public UserOV register(Student student,UserOV userOV,HttpServletRequest req) {
		//		这里先根据第三方的用户信息来注册拿到用户的id
		Student student2 = userMapper.selectStudent(student.getEmail().trim());
		if(passwordEncoder.matches(student.getPassword(),student2.getPassword())){
			//		接下来就是把用户的id传给SpringSocial让它把用户的业务系统id和第三方的用户信息绑定到一起
			providerSignInUtils.doPostSignUp(String.valueOf(student2.getId()),new ServletWebRequest(req));
			return userOV.setCode(200);
		}
		return userOV.setCode(500).setMessage("密码错误或者用户名不正确");
	}
}
