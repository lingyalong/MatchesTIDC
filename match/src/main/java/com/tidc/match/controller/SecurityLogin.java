package com.tidc.match.controller;


import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.ov.UserOV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @ClassNmae SecurityLogin
 * @Description TODO
 * @Author 14631
 **/
@RestController("/security")
public class SecurityLogin {
	@Autowired
	private SecurityProperties properties ;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private Logger logger = LoggerFactory.getLogger(getClass());
	//这个类是用来拿到引发跳转的请求  requestCache(请求的缓存`)
	private RequestCache requestCache = new HttpSessionRequestCache();
//	@PostMapping("/login")
//	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)//返回一个401
//	public UserOV studentLogin(HttpServletResponse resp, HttpServletRequest req) throws IOException {
//		//一个请求过来security判断是否需要用户权限进行登录 如果需要
//		//就会根据配置来到这个Controller
//		//requestCache会把当前的请求缓存到session里
//		//这句话就是从session取回请求 也就是拿到引发跳转的请求
//		SavedRequest savedRequest = requestCache.getRequest(req,resp);
//		if(savedRequest!=null){
//			//拿到引发跳转的请求的url 也就是他想访问的地址
//			String target = savedRequest.getRedirectUrl();
//			logger.info("引发跳转的url是"+target);
//			//判断url是否以.html结尾
//			if (StringUtils.endsWithIgnoreCase(target,".html")){
//				//访问跳转到html 如果需要实现可重用就需要将跳转的url封装到配置属性中
//				redirectStrategy.sendRedirect(req,resp,properties.getLogin().getLoginUrl());
//			}
//		}
//		return null;
//	}
	@GetMapping("/user")
	public UserOV getUser(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails){
		return null;
	}
}
