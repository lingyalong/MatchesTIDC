package com.tidc.match.Properties;

import lombok.Data;
/**
 * @ClassNmae QQProperties
 * @Description TODO
 * @Author 14631
 **/
@Data
public class QQProperties {
	private String appId = "101786883";
	private String appSecret = "562eb22e5b3c29be59a7cd7e2d4c9e4f";
	private String providerId = "qq";
	private String filterProcessesUrl = "/auth"; //登录前半段
	private String signUpUrl = "/sign"; //注册页面
}
