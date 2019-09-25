package com.tidc.authorization.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @ClassNmae RedisConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
public class RedisConfig {
	@Bean
	public RedisTemplate<Object, Object> userOVRedisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<Object, Object> template = new RedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//		修改这个Template的序列化策略为我们新写的
		template.setDefaultSerializer(serializer);
		return template;
	}
	@Bean
	public RedisCacheManager UserOVRedisCacheManager(RedisConnectionFactory redisConnectionFactory){
		//初始化一个RedisCacheWriter
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
		//设置CacheManager的值序列化方式为json序列化
		RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
				.fromSerializer(jsonSerializer);
		RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(pair);
		//设置默认超过期时间是30秒
		defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
		//初始化RedisCacheManager
		return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
	}
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append("&");
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

}
