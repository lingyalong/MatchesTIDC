package com.tidc.match.utiles;

import com.tidc.match.mapper.UserMapper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Student;
import com.tidc.match.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassNmae EmailUtiles
 * @Description TODO
 * @Author 14631
 **/
@Component
public class EmailUtiles {
	@Autowired
	public UserMapper userMapper;

	public UserOV email(Object user, String code, HttpServletRequest req, UserOV userOV,String role){
		String bool = null;
		if(role.equals("teacher")){
			user = (Teacher)userMapper.selectTeacherEmail((Teacher)user);
			if(user==null){
				userOV.setCode(404).setMessage("没有用户名或者邮箱不对");
				return userOV;
			}
			bool =  req.getSession().getAttribute(((Teacher)user).getEmail() + "Properties")+"";
		}else{
			user = (Student)userMapper.selectStudentEmail((Student) user);
			if(user==null){
				userOV.setCode(404).setMessage("没有用户名或者邮箱不对");
				return userOV;
			}
			bool =  req.getSession().getAttribute(((Student)user).getEmail() + "Properties")+"";
		}
		if(bool.equals(code)){
			userOV.setMessage("验证码正确").setCode(200).setData(user);
			return userOV;
		}
		userOV.setCode(404).setMessage("验证码错误");
		return userOV;
	}
}
