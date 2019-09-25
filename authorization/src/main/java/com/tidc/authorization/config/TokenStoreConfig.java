package com.tidc.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @ClassNmae TokenStoreConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
public class TokenStoreConfig {
	@Autowired
	RedisConnectionFactory redisConnectionFactory;
	@Bean
	public TokenStore redisTokenStore(){
		return new RedisTokenStore(redisConnectionFactory);
	}
}
