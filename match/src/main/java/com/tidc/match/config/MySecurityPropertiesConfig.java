package com.tidc.match.config;

import com.tidc.match.Properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassNmae MySecurityConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)//这是让SecurityProperties这个类生效
public class MySecurityPropertiesConfig {
}
