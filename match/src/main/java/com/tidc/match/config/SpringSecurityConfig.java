//package com.tidc.match.config;
//
//
//
//import com.tidc.match.Properties.SecurityProperties;
//import com.tidc.match.filter.ValidateCodeFilter;
//import com.tidc.match.handler.TidcAuthenticationFailureHandler;
//import com.tidc.match.handler.TidcAuthenticationSuccessHandler;
//import com.tidc.match.handler.TidcLogoutSuccessHandler;
//
//
//
//import javax.sql.DataSource;
//
///**
// * @ClassNmae SpringSecurityConfig
// * @Description TODO
// * @Author 14631
// **/
//@Configuration
//public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
//	@Autowired
//	private MyUserDeatisService myUserDeatisService;
//	@Autowired
//	private SecurityProperties securityProperties;
//	@Autowired
//	private TidcAuthenticationSuccessHandler tidcAuthenticationSuccessHandler;
//	@Autowired
//	private TidcAuthenticationFailureHandler tidcAuthenticationFailureHandler;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	@Autowired
//	private DataSource dataSource;
//	@Autowired
//	LogoutSuccessHandler logoutSuccessHandler;
////	这个是qq登录的配置
//	@Autowired
//	SpringSocialConfigurer tidcSocialConfigurer;
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(myUserDeatisService).passwordEncoder(passwordEncoder);
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//
//				.authorizeRequests()//这句话的意思是下面这些都是授权的配置
//				.antMatchers("/student/check/match","/photo/**/**","ftt/**/**","register/**/**","/login",securityProperties.getLogin().getLoginUrl(),"/code/img","/swagger-ui.html#/**","/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/ui","/swagge‌​r-ui.html").permitAll() //这句话是这个url不需要用户验证即可访问
//				.antMatchers("/teacher/**/**").hasRole("TEACHER")
//				.antMatchers("/student/**/**").hasRole("STUDENT")
//
//				.and()
//				.formLogin()//这里的意思是用表单登录来进行身份认证
////				.loginPage("/html.html")//指定自定义的登录页面
//				.loginPage(securityProperties.getLogin().getLoginUrl())//指定一个登录页面的路径
//				.loginProcessingUrl("/login")//这个是指定哪个url是登录请求需要进行用户密码校验的url 也就是登录页面的表单里发起的请求路径
//				.usernameParameter("email").passwordParameter("password")//绑定登录时使用的参数名
//
//				.successHandler(tidcAuthenticationSuccessHandler)//加入自定义的登录成功的处理器 用户登录成功只会就会执行这个处理器类里的方法
//				.failureHandler(tidcAuthenticationFailureHandler)//加入自定义的登录失败的处理器 用户登录失败只会就会执行这个处理器类里的方法
//				//如果使用了上面那两个类就会覆盖掉SpringSecurity的默认处理方式 也就是成功后跳转到用户本来请求的页面
//				//不过也可以调成使用默认的处理方式
//				.and()
////				用户退出登录的配置
//				.logout()
////				退出的请求的url
//					.logoutUrl(securityProperties.getLogoutProperties().getLogoutUrl())
////				退出成功之后跳转的页面 和handler互斥
////					.logoutSuccessUrl("/logout/success")
//					.logoutSuccessHandler(logoutSuccessHandler)
//
//				.and()
//				//记住我的配置
//				.rememberMe()
//				.tokenRepository(persistentTokenRepository())//获取我写的类
//				.tokenValiditySeconds(securityProperties.getLogin().getRememberMeSeconds())//设置记住我时间
//				.userDetailsService(myUserDeatisService)//这个从数据库取出token之后进行的用户密码校验的过滤器
//				.and()
////				QQ登录的过滤器
//				.apply(tidcSocialConfigurer)
//				.and()
//				.cors()
//				.and()
//				.csrf().disable();//关闭csrf的跨站防护功能
//		//以上的代码的结果就是除了某些url之外的任何请求都需要是身份认证以后才能访问
//		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
//		validateCodeFilter.afterPropertiesSet();
//		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
//
//	}
//	//这个是为了实现记住我
//	@Bean
//	public PersistentTokenRepository persistentTokenRepository(){
//		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(false);//这个是设置让系统启动自己去数据库生成一个表 true就是自动创建
//		return tokenRepository;
//	}
//	@Bean
//	@ConditionalOnMissingBean(LogoutSuccessHandler.class)
//	public LogoutSuccessHandler logoutSuccessHandler(){
//		return new TidcLogoutSuccessHandler();
//	}
//
//}
