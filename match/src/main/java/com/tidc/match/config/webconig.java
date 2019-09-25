package com.tidc.match.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassNmae webconig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
public class webconig {


		/**
		 * 跨域请求支持
		 */
		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurerAdapter() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**").allowedOrigins("*")
							.allowedMethods("*").allowedHeaders("*")
							.allowCredentials(true)
							.exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
				}
			};
		}


}
