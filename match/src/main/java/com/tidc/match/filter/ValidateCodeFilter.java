package com.tidc.match.filter;

import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.code.ImgCode;
import com.tidc.match.controller.ValidateCodeController;
import com.tidc.match.exception.ValidateCodeException;
import com.tidc.match.handler.TidcAuthenticationFailureHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassNmae ValidateCodeFilter
 * @Description TODO
 * @Author 14631
 **/
//这个过滤器会在登录的时候会执行两次是因为登录成功控制器会做一个跳转条到用户第一次请求的地址
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
	TidcAuthenticationFailureHandler tidcAuthenticationFailureHandler;  //登录失败处理器
	SessionStrategy strategy = new HttpSessionSessionStrategy();
	//继承OncePerRequestFilter这个类是为了保证该过滤器在一次请求中只调用一次
	//这个过滤器将会在UserNamePasswordAuthenticationFilter之前调用
	//如果验证码验证失败就抛出异常
	//实现InitializingBean这个接口是为了在所有数据封装完毕之后去封装url这个值
	Set<String> url = new HashSet<>();
	@Autowired
	private SecurityProperties securityProperties;
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
//InitializingBean接口的方法 也就是在所有数据封装完毕之后去封住url
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		//将配置里的url以逗号分开
		String test = "";
		String[] configUrl = StringUtils.split(" /login/teacher,/login/student ",",");
		for (String s : configUrl) {
			//将所有人需要校验验证码的请求加入这个set容器
			url.add(s);
		}
	}
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		boolean flag = false;
		System.out.println("这个验证码过滤器通过了");
		 SecurityContext securityContext = SecurityContextHolder.getContext();




//		BufferedReader br;
//		try {
//			br = httpServletRequest.getReader();
//			String str, wholeStr = "";
//			while((str = br.readLine()) != null){
//				wholeStr += str;
//			}
//			System.out.println(wholeStr);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		httpServletRequest.setAttribute("loginUserName",httpServletRequest.getParameter("username"));
//		for(String url:url){
//			if(antPathMatcher.match(url,httpServletRequest.getRequestURI())){
//				flag = true;
//			}
//		}
		if(flag){
//			必须是登录的请求以及必须是post请求该过滤器才起效
			//下面就是认证逻辑  如果认证失败或者认证过程中出现异常就会调用我们自定义的登录失败处理器
			try{
				validated(new ServletWebRequest(httpServletRequest));
			}catch (ValidateCodeException e){
				tidcAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
				//验证码校验失败 请求返回
				return;
			}
		}
			//验证码校验成功执行后面的过滤器
			filterChain.doFilter(httpServletRequest,httpServletResponse);

	}

	private void validated(ServletWebRequest request) throws ServletRequestBindingException {
		ImgCode codeInSession = (ImgCode) strategy.getAttribute(request, ValidateCodeController.SESSION_KEY);
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"这个是从请求里拿到imagecode参数也就是用户输入的验证码");

		if (org.apache.commons.lang.StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException( "验证码的值不能为空");
		}

		if (codeInSession == null) {
			throw new ValidateCodeException("验证码不存在");
		}

		if (codeInSession.isExpried()) {
			strategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
			throw new ValidateCodeException( "验证码已过期");
		}

		if (!org.apache.commons.lang.StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException("验证码不匹配");
		}

		strategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
	}


}
