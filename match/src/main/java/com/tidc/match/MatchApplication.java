package com.tidc.match;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@MapperScan("com.tidc.match.mapper")
@EnableSwagger2
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class MatchApplication {
	//加入一个加密器 可以使用Autowired进行注入 注册的时候要调用.encode()进行加密
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(MatchApplication.class, args);

	}


}
