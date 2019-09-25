package com.tidc.match.config;

import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.Properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @ClassNmae SwaggerConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)//这是让SecurityProperties这个类生效
public class SwaggerConfig {
	@Autowired
	private SwaggerProperties swaggerProperties;
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.enable(swaggerProperties.isFlag())
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tidc.match.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("博海报名系统")
				.description("简单优雅的restfun风格")
				.termsOfServiceUrl("http://101.200.121.105:5461/login/stydent")
				.version("1.0")
				.build();
	}
}
