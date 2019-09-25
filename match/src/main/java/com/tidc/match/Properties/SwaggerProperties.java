package com.tidc.match.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassNmae SwaggerProperties
 * @Description TODO
 * @Author 14631
 **/
@Data
@ConfigurationProperties(prefix = "com.tidc.tao.swagger")
public class SwaggerProperties {
	boolean flag = true;


}
