package com.tidc.authorization.config;

import com.tidc.authorization.config.service.MyUserDeatisService;
import com.tidc.authorization.handler.TidcAuthenticationFailureHandler;
import com.tidc.authorization.handler.TidcAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassNmae SecurityConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	@Bean
//	public PasswordEncoder passwordEncoder(){
//		return new BCryptPasswordEncoder();
//	}
	@Autowired
	private MyUserDeatisService myUserDeatisService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TidcAuthenticationSuccessHandler tidcAuthenticationSuccessHandler;
	@Autowired
	private TidcAuthenticationFailureHandler tidcAuthenticationFailureHandler;



//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.requestMatchers().anyRequest()
//				.and()
//				.authorizeRequests()
//				.antMatchers("/oauth/*").permitAll()
//				.and()
//				.authorizeRequests().antMatchers("/alter/hello").permitAll();
//		http
//				.authorizeRequests()
//				// 自定义页面或处理url是，如果不配置全局允许，浏览器会提示服务器将页面转发多次
//				.antMatchers("/teacher/**/**").hasRole("TEACHER")
//				.antMatchers("/login/**","/oauth/**")
//				.permitAll()
//				.anyRequest()
//				.authenticated()
//				.and()
//				.formLogin()//这里的意思是用表单登录来进行身份认证
////
//				.loginProcessingUrl("/login")//这个是指定哪个url是登录请求需要进行用户密码校验的url 也就是登录页面的表单里发起的请求路径
//				.usernameParameter("email").passwordParameter("password")//绑定登录时使用的参数名
//
//				.successHandler(tidcAuthenticationSuccessHandler)//加入自定义的登录成功的处理器 用户登录成功只会就会执行这个处理器类里的方法
//				.failureHandler(tidcAuthenticationFailureHandler)
//				.and()
//				.cors()
//				.and()
//				.csrf().disable();//关闭csrf的跨站防护功能
//
//	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDeatisService)
				.passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http

				.authorizeRequests()//这句话的意思是下面这些都是授权的配置
				.antMatchers("/photo/**/**","register/**/**","/swagger-ui.html#/**","/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/ui","/swagge‌​r-ui.html").permitAll() //这句话是这个url不需要用户验证即可访问
				.and()
				.authorizeRequests().anyRequest().authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.and()
				.httpBasic()
				.and()
				.cors()
				.and()
				.csrf().disable();//关闭csrf的跨站防护功能
		http
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.formLogin().and()
				.csrf().disable()
				.httpBasic();
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favor.ioc");
	}
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Bean
	@Override
	protected UserDetailsService userDetailsService(){
		return myUserDeatisService;
	}
}
