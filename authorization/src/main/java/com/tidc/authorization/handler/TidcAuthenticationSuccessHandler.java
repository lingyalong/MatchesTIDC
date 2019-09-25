package com.tidc.authorization.handler;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.apache.commons.collections.MapUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassNmae TidcAuthenticationSuccessHandler
 * @Description TODO
 * @Author 14631
 **/
@Component
public class TidcAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private ClientDetailsService clientDetailsService;
	@Autowired
	AuthorizationServerTokenServices authorizationServerTokenServices;
	protected String getCredentialsCharset(HttpServletRequest httpRequest) {
		return "UTF-8";
	}

	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
		byte[] base64Token = header.substring(6).getBytes("UTF-8");

		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		} catch (IllegalArgumentException var7) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, this.getCredentialsCharset(request));
		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		} else {
			return new String[]{token.substring(0, delim), token.substring(delim + 1)};
		}
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		//authentication接口 封装了用户的认证信息 比如ip session UserDetails 如果登录的方式不一样里面的内容也会变化
		//这个接口如果传给前端里面的密码等内容会被隐藏
//		if(securityProperties.getLogin().getLoginType().equals(LoginType.JSSON)){
//			//这里可以使用自己的处理方法
//		}else{
//			//调用父类的处理方法也就是调用默认的处理方法 也就是跳转到用户本身请求的页面
//			super.onAuthenticationSuccess(httpServletRequest,httpServletResponse,authentication);
//		}

		Map<String, Object> map = new HashMap<>();
		map.put("code", "201");
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		if (authorities.size() == 3) {
			map.put("code", "200");
		}
		map.put("message", "登录成功");
//		如果这个登录的配置返回方式是Json就使用我们自己写的返回Json的方式
//			httpServletResponse.setContentType("application/json;charset=UTF-8");
//			httpServletResponse.getWriter().write(objectMapper.writeValueAsString(map));

		System.out.println("成功处理器");

		String header = httpServletRequest.getHeader("Authorization");
		if (header != null && header.toLowerCase().startsWith("basic ")) {
			String[] tokens = this.extractAndDecodeHeader(header, httpServletRequest);

			assert tokens.length == 2;
//获取到ClientId和Secret
			String clientId = tokens[0];
			String clientSecret = tokens[1];
//			获取ClientDetails

			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

			if(clientDetails==null){
				throw new UnapprovedClientAuthenticationException("ClientId不存在"+clientId);
			}else if (!StringUtils.equals(clientDetails.getClientSecret(),clientSecret)){
				throw new UnapprovedClientAuthenticationException("clientSecret不匹配"+clientSecret);
			}
//			获取TokenRequest 第一个属性是为了构架authentication的但是我们有了就不用了
//			第四个参数是填四种授权模式中的一种这里我们写一个自定义的
			TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP,clientId, clientDetails.getScope() ,"custom");
//			构建OAuth2Request
			OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
//			构建
			OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request,authentication);
//			获取Token
			OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
//			将Token作为Session返回去
			map.put("access_token",oAuth2AccessToken);
			map.put("token_type","passsword");
			map.put("expires_in",3600);
			map.put("scope","all");
			httpServletResponse.setContentType("application/json;charset=UTF-8");
			httpServletResponse.getWriter().write(objectMapper.writeValueAsString(map));
		}else{
			throw new UnapprovedClientAuthenticationException("请求头中无Client信息");
		}
	}
}