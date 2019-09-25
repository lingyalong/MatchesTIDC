//package com.tidc.match.oauth.social.config;
//
//import com.tidc.match.Properties.QQProperties;
//import com.tidc.match.Properties.SecurityProperties;
//import com.tidc.match.oauth.connection.QQConnectionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.social.connect.ConnectionFactory;
//
///**
// * @ClassNmae SocialConfig
// * @Description TODO
// * @Author 14631
// **/
//@Configuration
//@ConditionalOnProperty(prefix = "tidc.tao.security.social.qq",name="appId") //只有tidc.tao.security.social.qq.appId被配置了值这个配置类才生效
//public class QQAutoConfig extends SocialAutoConfigurerAdapter {
//	@Autowired
//	SecurityProperties securityProperties ;
////	自动创建一个ConnectionFactory
//	@Override
//	protected ConnectionFactory<?> createConnectionFactory() {
//		QQProperties qqProperties = securityProperties.getSocial().getQq();
//		return new QQConnectionFactory(qqProperties.getProviderId(),qqProperties.getAppId(),qqProperties.getAppSecret());
//	}
//}
