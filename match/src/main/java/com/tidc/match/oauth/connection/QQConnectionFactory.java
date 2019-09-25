package com.tidc.match.oauth.connection;

import com.tidc.match.oauth.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @ClassNmae QQConnectionFactory
 * @Description TODO
 * @Author 14631
 **/
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

	public QQConnectionFactory(String providerId,String appid, String appSecret) {
		super(providerId,new QQServiceProvider(appid,appSecret), new QQAdapter());
	}
}
