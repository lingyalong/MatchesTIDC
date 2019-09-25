package com.tidc.match.service;

import com.tidc.match.mapper.UserMapper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.ov.UserTokenDTO;

import com.tidc.match.pojo.Student;

import com.tidc.match.utiles.CheckUserInfo;
import cucumber.api.java.bs.A;
import gherkin.deps.com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import org.springframework.http.*;
import com.tidc.match.utiles.ApplicationContextProvider;
import com.tidc.match.utiles.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;




import org.springframework.http.client.support.BasicAuthorizationInterceptor;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassNmae LoginService
 * @Description TODO
 * @Author 14631
 **/
@Service
public class LoginService {
	@Autowired
	public UserMapper userMapper;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	UserSession userSession;
	@Autowired
	RedisTemplate redisTemplate;

	/**
	 * spring对web请求的封装类 (所以说，约定大于配置!)
	 */
	private RestTemplate restTemplate = new RestTemplate();
//	public UserOV studentLogin(Student student, HttpServletRequest req,UserOV userOV){
//		student = userMapper.selectStudent(student);
//		if(student !=null){
//			//如果登录成功就存入Session
//			req.getSession().setAttribute("student", student);
//			userOV.setMessage("success").setCode(200);
//		}else {
//			userOV.setMessage("邮箱或密码错误").setCode(404);
//		}
//
//		return userOV;
//	}
//	public UserOV teacherLogin(Teacher teacher,HttpServletRequest req,UserOV userOV){
//		teacher = userMapper.selectTeacher(teacher);
//		if(teacher!=null){
//			//如果登录成功就存入Session
//			req.getSession().setAttribute("teacher",teacher);
//			userOV.setMessage("success").setCode(200);
//		}else{
//			userOV.setMessage("用户名或密码错误").setCode(404);
//		}
//
//
//		return userOV;
//	}
	public UserOV getStudent(UserOV userOV,String access_token){
		userOV.setData(CheckUserInfo.getUserInfo(access_token));
		userOV.setCode(200);
		return userOV;
	}
	public UserOV login(UserOV userOV,Student student){
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("username", student.getEmail());
		params.add("password", student.getPassword());
		params.add("scope", "all");
		params.add("client_id", "TIDC");
		params.add("client_secret", "computer");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccessControlRequestMethod(HttpMethod.POST);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
		UserTokenDTO userTokenDTO = null;
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("TIDC", "computer"));
		try {
//            向/oauth/token获取Token
			userTokenDTO = restTemplate.postForObject("http://localhost:5461/oauth/token", requestEntity, UserTokenDTO.class);
		} catch (RestClientException rce) {
			rce.printStackTrace();
		}
		//将token存入redis
		Assert.notNull(userTokenDTO, "userTokenDTO is null!");
		redisTemplate.opsForValue().set("USER_TOKEN_DTO" + student.getId(), userTokenDTO, userTokenDTO.getExpires_in(), TimeUnit.SECONDS);
		student.setPassword(null);
		return userOV.setData(userTokenDTO).setCode(200).setMessage("cg");
	}
//	public UserOV getTeacher(UserOV userOV,HttpServletRequest req){
//		userOV = userSession.Session("teacher",userOV,req);
//		return userOV;
//	}
}
