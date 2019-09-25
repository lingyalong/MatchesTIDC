package com.tidc.match.utiles;

import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Student;
import com.tidc.match.pojo.Teacher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassNmae UserSession
 * @Description TODO
 * @Author 14631
 **/
@Component
public class UserSession {
	public UserOV Session(String userName, UserOV userOV, HttpServletRequest req){
		Object user = null;
		if(userName.equals("student")){
			user = (Student) req.getSession().getAttribute(userName);
		}else{
			user = (Teacher) req.getSession().getAttribute(userName);
		}

		if(user!=null){
			userOV.setData(user).setMessage("success").setCode(200);
			return userOV;
		}
		userOV.setCode(404).setMessage("没有登录").setData(null);
		return userOV;
	}
}
