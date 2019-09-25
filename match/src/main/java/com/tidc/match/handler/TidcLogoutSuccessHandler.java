package com.tidc.match.handler;

import com.tidc.match.Properties.LoginType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassNmae LogoutSuccessHandler
 * @Description TODO
 * @Author 14631
 **/
@Component
public class TidcLogoutSuccessHandler implements LogoutSuccessHandler {
	private Logger logger = LoggerFactory.getLogger(TidcLogoutSuccessHandler.class);
	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<>();
		map.put("code","200");
		map.put("message","退出成功");
//		如果这个登录的配置返回方式是Json就使用我们自己写的返回Json的方式
			httpServletResponse.setContentType("application/json;charset=UTF-8");
		}
}
