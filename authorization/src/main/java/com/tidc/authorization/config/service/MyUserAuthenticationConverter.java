package com.tidc.authorization.config.service;

import com.tidc.authorization.mapper.UserMapper;
import com.tidc.authorization.pojo.Student;
import com.tidc.authorization.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassNmae MyUserAuthenticationConverter
 * @Description TODO
 * @Author 14631
 **/
//替换掉/oauth/check_token里的  Map<String, Object> response = this.accessTokenConverter.convertAccessToken(token, authentication);
//	convertAccessToken里的UserAuthenticationConverter对象 重写它获取用户信息的方法 在认证服务器里配置进去替换掉原来DefaultAccessTokenConverter的
//	DefaultUserAuthenticationConverter对象
// 返回用户信息的
//	这里面大多数都是源码没必要看我从源码复制的
//	convertUserAuthentication
@Service
public class MyUserAuthenticationConverter extends DefaultUserAuthenticationConverter   {
	@Autowired
	private UserMapper userMapper;

	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		Map<String, Object> response = new LinkedHashMap();
		String username = authentication.getName();
		response.put("user_name",username);
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
//		上面都是源码
		Student student = userMapper.selectStudent(username);
		Teacher teacher = userMapper.selectTeacher(username);
		if(student!=null){
			student.setPassword("");
			response.put("student",student);
		}else if(teacher!=null){
			teacher.setPassword("");

			response.put("teacher",teacher);

		}
		return response;
	}
}
