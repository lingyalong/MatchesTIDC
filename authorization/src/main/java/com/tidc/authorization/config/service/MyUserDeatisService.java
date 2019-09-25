package com.tidc.authorization.config.service;


import com.tidc.authorization.mapper.UserMapper;
import com.tidc.authorization.pojo.Student;
import com.tidc.authorization.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @ClassNmae MyUserDeatisService
 * @Description TODO
 * @Author 14631
 **/
@Component
@CrossOrigin //解决跨域问题
public class MyUserDeatisService implements UserDetailsService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) {
		System.out.println(username+"登录了!!!!!");
		Student student = userMapper.selectStudent(username.trim());
		Teacher teacher = userMapper.selectTeacher(username.trim());
		String power = "ROLE_STUDENT,ROLE_User";
		if(student!=null) {
			System.out.println(student);
			return returnUser(student.getEmail(),student.getPassword(),power);
		}
		if(teacher!=null){
			System.out.println(teacher);
			power = "ROLE_TEACHER,ROLE_STUDENT,ROLE_User";
			return returnUser(teacher.getEmail(),teacher.getPassword(),power);
		}

		return returnUser(student.getEmail(),student.getPassword(),power);
	}


	public User returnUser(String email, String password, String power){
		return new User(email,password,
				true,true,true,true,
				AuthorityUtils.commaSeparatedStringToAuthorityList(power));
	}
}
