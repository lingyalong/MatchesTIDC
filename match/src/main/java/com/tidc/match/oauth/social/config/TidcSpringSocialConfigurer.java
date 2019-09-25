package com.tidc.match.oauth.social.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @ClassNmae TidcSpringSocialConfiguarer
 * @Description TODO
 * @Author 14631
 **/
public class TidcSpringSocialConfigurer extends SpringSocialConfigurer {
//	这个属性设置成可配置的形式
	String filterProcessesUrl ;
	public TidcSpringSocialConfigurer(String filterProcessesUrl){
		this.filterProcessesUrl = filterProcessesUrl;
	}
	@Override
//	这个方法里的参数就是SocialAuthenticationFilter
//	 这个类就是为了改变qq登录的url的/auth部分
	protected <T> T postProcess(T object) {
		SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
		filter.setFilterProcessesUrl(filterProcessesUrl);
		return (T) filter;
	}
}
