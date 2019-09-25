package com.tidc.match.generator;

import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.code.ImgCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @ClassNmae ValidateCodeGenerator
 * @Description TODO
 * @Author 14631
 **/
public interface ValidateCodeGenerator {

	public ImgCode createImageCode(ServletWebRequest req);

}
