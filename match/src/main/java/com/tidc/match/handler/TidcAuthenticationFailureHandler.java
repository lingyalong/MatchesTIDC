package com.tidc.match.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.tidc.match.Properties.LoginType;
import com.tidc.match.Properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassNmae TidecAuthenticationss
 * @Description TODO
 * @Author 14631
 **/
@Component
public class TidcAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	ObjectMapper objectMapper;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//		e是一个异常包含登录过程中产生的错误比如用户名没找到或者密码不对
		System.out.println("登录失败处理器执行了");
			Map<String, Object> map = new HashMap<>();
			map.put("code","404");
			map.put("message","登录失败没有接收到用户名");
		if(request.getAttribute("loginUserName")!=null){
			map.put("code","500");
			map.put("message","登录失败但是接收到用户名"+request.getAttribute("loginUserName"));
		}
			map.put("data",exception.getMessage());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(map));

//		}
	}
}
