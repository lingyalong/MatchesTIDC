package com.tidc.match.utiles;

import com.tidc.match.mapper.UserMapper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Student;
import com.tidc.match.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @ClassNmae Register
 * @Description TODO
 * @Author 14631
 **/
@Component
public class RegisterUtiles {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	public UserMapper userMapper;
	public  UserOV register(Student student, Teacher teacher, UserOV userOV){
		//查重
		Student student1 = null;
		Teacher teacher1 = null;
		if(student!=null){
			student1 = userMapper.selectStudentUser(student.getEmail());
			//进行密码加密
			student.setPassword(passwordEncoder.encode(student.getPassword()));
			userOV.setCode(200).setMessage("注册成功");
		}else{
			teacher1 = userMapper.selectTeacher(teacher.getEmail());
			teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
			userOV.setCode(200).setMessage("注册成功").setData(teacher);
		}
		if (student1!=null||teacher1!=null){
			userOV.setMessage("email重复").setCode(500).setData(null);
			return userOV;
		}
		if(student!=null){
			String s = (String) student.getCard().subSequence(16,17);
			int i = Integer.parseInt(s);
			if(i%2==0){
				student.setGender("女");
			}else {
				student.setGender("男");
			}
			userOV.setData(student);
		}

		return userOV;


	}
}
