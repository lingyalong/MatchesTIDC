package com.tidc.match.oauth.social.config;


import com.tidc.match.Properties.QQProperties;
import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.oauth.connection.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @ClassNmae SocialConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
//这个注解是为了启动EnableSocial
@EnableSocial
@ConditionalOnProperty(name="tidc.tao.security.social.qq.appId") //只有tidc.tao.security.social.qq.appId被配置了值这个配置类才生效
public class SocialConfig extends SocialConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private SecurityProperties securityProperties;
//	@Autowired(required = false)
//	TidcConnctionSignUp tidcConnctionSignUp;
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
//		数据源 第二个是查找ConnectionFactory 第三个是加密解密
		JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
//		如果你在这个表的名字的前面加一些前缀就需要这个配置
//		jdbcUsersConnectionRepository.setTablePrefix("这里写前缀");
//		if(tidcConnctionSignUp!=null){
//			jdbcUsersConnectionRepository.setConnectionSignUp(tidcConnctionSignUp);
//		}
		return jdbcUsersConnectionRepository;
	}
	//	加入一个过滤器到Ioc容器中从securityConfig里取出来并加入到security的过滤链里
	@Bean
	public SpringSocialConfigurer springSocialConfigurer(){
		//传入传入自定义的登录url的前半段
		TidcSpringSocialConfigurer tidcSpringSocialConfigurer = new TidcSpringSocialConfigurer(securityProperties.getSocial().getQq().getFilterProcessesUrl());
//		传入注册页面在获取不到用户信息的时候跳转到注册页面

		tidcSpringSocialConfigurer.signupUrl(securityProperties.getSocial().getQq().getSignUpUrl());
		return tidcSpringSocialConfigurer;
	}
	//	这个工具类帮助我们在注册页面获得用户的社交信息和将用户的业务系统的id传给Social
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator){
		return new ProviderSignInUtils(connectionFactoryLocator,getUsersConnectionRepository(connectionFactoryLocator));
	}

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {

		super.addConnectionFactories(connectionFactoryConfigurer, environment);
		QQProperties qqProperties = securityProperties.getSocial().getQq();

		connectionFactoryConfigurer.addConnectionFactory(new QQConnectionFactory(qqProperties.getProviderId(),qqProperties.getAppId(),qqProperties.getAppSecret()));
	}
	@Override
	public UserIdSource getUserIdSource() {
		// TODO Auto-generated method stub
		return new AuthenticationNameUserIdSource();
	}
}
