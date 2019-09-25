package com.tidc.authorization.config;

import com.tidc.authorization.config.service.MyUserAuthenticationConverter;
import com.tidc.authorization.config.service.MyUserDeatisService;
import com.tidc.authorization.message.CustomWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @ClassNmae AuthorizationConfig
 * @Description TODO
 * @Author 14631
 **/



@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends  AuthorizationServerConfigurerAdapter  {
//	@Autowired
//	private AuthenticationManager authenticationManager;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;
	@Autowired
	private MyUserDeatisService myUserDeatisService;
	@Autowired
	private TokenStore redisTokenStore;
	@Autowired
	private ClientDetailsService jdbcClientDetails;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	MyUserAuthenticationConverter myUserAuthenticationConverter;
	@Bean
	public TokenStore tokenStore(DataSource dataSource) {
		// 基于 JDBC 实现，令牌保存到数据
		return new JdbcTokenStore(dataSource);
	}

	@Bean
	public ClientDetailsService jdbcClientDetails(DataSource dataSource) {
		// 基于 JDBC 实现，需要事先在数据库配置客户端信息
		return new JdbcClientDetailsService(dataSource);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 读取客户端配置
		clients.withClientDetails(jdbcClientDetails);
//		clients.inMemory()
//				.withClient("TIDC")
//				.authorizedGrantTypes("password", "refresh_token")
//				.scopes("all")
//				.secret("computer");
////				令牌有效期
	}
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints.authenticationManager(authenticationManager);
		//		持久化策略 默认存储是内存中的实现 这里是使用JDBCToken持久化策略
		endpoints.tokenStore(redisTokenStore);
//        注入UserDetails
		endpoints.userDetailsService(myUserDeatisService);
//        Token校验失败的处理器
		endpoints.exceptionTranslator(customWebResponseExceptionTranslator);
//		Token有两个属性refreshToken和accessToken 请求访问的时候如果accessToken过期了就会看refreshToken如果
//		没过期就去刷新accessToken
//		设置成false的话只要用户在refresh Token(用于刷新的token)有效期时进行过操作那么refreshToken和accessToken就都会刷新永不过期
//		true的话refreshToken是不会被刷新的有效期默认是7天 7天之后用户就需要重新登录刷refreshToeken
		endpoints.reuseRefreshTokens(false);
//		以下是配置/oauth/check_token获取用户信息的
		DefaultAccessTokenConverter defaultAccessTokenConverter=new DefaultAccessTokenConverter();
		defaultAccessTokenConverter.setUserTokenConverter(myUserAuthenticationConverter);
		endpoints.accessTokenConverter(defaultAccessTokenConverter);
	}
	/**
	 *@Author feng
	 *@Description 描述 不知道是干什么的
	 *@Param  * @param null
	 *@return */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()");
		security .checkTokenAccess("isAuthenticated()");
//		 允许client使用form的方式进行authentication的授权
		security.allowFormAuthenticationForClients();

	}
//这一段不能注释
	@Primary
	@Bean
	public ClientDetailsService clientDetails() {
		return new JdbcClientDetailsService(dataSource);
	}
	/**
	 * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
	 * 默认的配置
	 * @return
	 */
	@Primary
	@Bean
	public DefaultTokenServices defaultTokenServices(){
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(redisTokenStore);
		tokenServices.setSupportRefreshToken(false);
		tokenServices.setClientDetailsService(jdbcClientDetails);
		tokenServices.setAccessTokenValiditySeconds(60*60*12); // token有效期自定义设置，默认12小时
		tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);//默认30天，这里修改
		return tokenServices;
	}


}
