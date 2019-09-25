package com.tidc.match.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassNmae DownProperties
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tidc.tao.file")
public class DownProperties {
	String photoFile = "/www/server/tomcat/webapps/photo/";
	String file = "/www/server/tomcat/webapps/file/";

}
