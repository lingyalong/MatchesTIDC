package com.tidc.match.config;

import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.handler.TidcAuthenticationFailureHandler;
import com.tidc.match.handler.TidcAuthenticationSuccessHandler;
import com.tidc.match.handler.TidcLogoutSuccessHandler;
import com.tidc.match.message.AuthExceptionEntryPoint;
import com.tidc.match.message.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @ClassNmae TidcResoureceConfig
 * @Description TODO
 * @Author 14631
 **/
//app模块里没有session没有记住我没有退出
@Configuration
@EnableResourceServer
//这个配置相当于securityConfig
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TidcResoureceConfig extends ResourceServerConfigurerAdapter {
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	private TidcLogoutSuccessHandler tidcLogoutSuccessHandler;
	@Autowired
	private TidcAuthenticationFailureHandler tidcAuthenticationFailureHandler;
	@Autowired
	private TidcAuthenticationSuccessHandler tidcAuthenticationSuccessHandler;
	@Autowired
	SpringSocialConfigurer tidcSocialConfigurer;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Autowired
	public TidcResoureceConfig(CustomAccessDeniedHandler customAccessDeniedHandler) {
		this.customAccessDeniedHandler = customAccessDeniedHandler;
	}
	/**
	 * 验证token失败
	 * 设置一个token校验失败的处理器
	 * @param resources resources
	 * @throws Exception resources
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.authenticationEntryPoint(new AuthExceptionEntryPoint())
//				 用来解决认证过的用户访问无权限资源时的异常
				.accessDeniedHandler(customAccessDeniedHandler);
	}
	@Primary
	@Bean
	public RemoteTokenServices tokenServices() {
		final RemoteTokenServices tokenService = new RemoteTokenServices();
		tokenService.setCheckTokenEndpointUrl("http://localhost:5461/oauth/check_token");
		tokenService.setClientId("TIDC");
		tokenService.setClientSecret("computer");
		return tokenService;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().permitAll();
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.and().cors()
				.and().csrf().disable();
	}
}
