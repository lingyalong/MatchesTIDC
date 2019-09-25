package com.tidc.match.config;

import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.generator.ImageCodeGenerator;
import com.tidc.match.generator.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassNmae ValidateCodeBeanConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
public class ValidateCodeBeanConfig {

	@Autowired
	private SecurityProperties securityProperties;
	@Bean
	@ConditionalOnMissingBean(name = "imageGenerator")//这个注解就是运行程序之前会在spring容器中找imageGenerator这个bean如果有就代替没有就用原来的
	public ValidateCodeGenerator imageGenerator(){
		ValidateCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
		((ImageCodeGenerator) imageCodeGenerator).setSecurityProperties(securityProperties);
		return imageCodeGenerator;
	}
}
