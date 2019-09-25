package com.tidc.match.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @ClassNmae ValidateCodeException
 * @Description TODO
 * @Author 14631
 **/
public class ValidateCodeException extends AuthenticationException {
	//这个异常是Security所有的身份认证异常的基类
	public ValidateCodeException(String msg) {
		super(msg);
	}
}
