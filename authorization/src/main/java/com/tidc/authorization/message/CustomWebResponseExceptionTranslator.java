package com.tidc.authorization.message;

import com.tidc.authorization.constant.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author innoyiya
 * @date 2018/9/24.
 */
@Component("customWebResponseExceptionTranslator")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {


        //为了安全不管用户名错误还是密码错误皆返回同样错误
        try {
            //密码错误
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new CustomOauthException(Message.LOGIN_WRONG));
        }catch (Exception e1){
            //账户错误
            return ResponseEntity
                    .status(401)
                    .body(new CustomOauthException(Message.LOGIN_WRONG));
        }
    }
}
