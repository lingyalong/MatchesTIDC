package com.tidc.match.Properties;

import lombok.Data;

/**
 * @ClassNmae LoginProperties
 * @Description TODO
 * @Author 14631
 **/
@Data
public class LoginProperties {
	//如果用户进行了配置就用用户的没有就用这个
	private String loginUrl = "/tidc/login"; //这个是表示登录接口url
	private LoginType loginType = LoginType.JSSON;//登陆成功或失败时的处理方式
	private int rememberMeSeconds = 3600*48; //记住我的秒数设置
}
