package com.tidc.match.Properties;

import com.tidc.match.Properties.img.ValidateCodeProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @ClassNmae SecurityProperties
 * @Description TODO
 * @Author 14631
 **/
@Data
@ConfigurationProperties(prefix = "com.tidc.tao.security")
public class SecurityProperties {
	LoginProperties login = new LoginProperties();
	ValidateCodeProperties code = new ValidateCodeProperties();
	SocialProperties social = new SocialProperties();
	LogoutProperties logoutProperties = new LogoutProperties();

}
