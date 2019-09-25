package com.tidc.match.config;

import com.tidc.match.mapper.UserMapper;
import com.tidc.match.pojo.Student;
import com.tidc.match.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * @ClassNmae MyUserDeatisService
 * @Description TODO
 * @Author 14631
 **/
@Component
@CrossOrigin //解决跨域问题
public class MyUserDeatisService implements UserDetailsService , SocialUserDetailsService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) {
		//实现这个接口方法 从数据库里获取到一个完整的用户信息封装进UserDetails里
		//因为UserDetails是个接口不能被实例化所以我们返回一个接口的实现类也就是SpringSecurity里的User
		//这个user构造方法是username,password,四个bool值代表UserDetails的那四个方法的返回值一个为false登录就会失败
		// 以及权限集合 下面那个方法的把一个字符串以逗号隔开分割成权限集合对象
		//密码的对比他会自己对比
		System.out.println(username+"登录了!!!!!");
		Student student = userMapper.selectStudent(username.trim());
		Teacher teacher = userMapper.selectTeacher(username);
		String power = "ROLE_STUDENT,ROLE_User";
		if(student!=null) {
			System.out.println(student);
			return returnUser(student.getEmail(),student.getPassword(),power);
		}
		if(teacher!=null){
			//这个return的user是spring的
			System.out.println(teacher);
			power = "ROLE_TEACHER,ROLE_STUDENT,ROLE_User";
			return returnUser(teacher.getEmail(),teacher.getPassword(),power);
		}

		return returnUser(student.getEmail(),student.getPassword(),power);
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		Integer i = Integer.parseInt(userId);
		Student student = userMapper.selectStudent_id(i);
		String power = "ROLE_STUDENT";
		return (SocialUserDetails) returnUser(student.getEmail(),student.getPassword(),power);
	}
	public User returnUser(String email,String password,String power){
		return new SocialUser(email,password,
				true,true,true,true,
				AuthorityUtils.commaSeparatedStringToAuthorityList(power));
	}
}
