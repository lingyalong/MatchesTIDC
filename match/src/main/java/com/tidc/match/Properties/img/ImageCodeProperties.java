package com.tidc.match.Properties.img;

import lombok.Data;

/**
 * @ClassNmae ImageCodeProperties
 * @Description TODO
 * @Author 14631
 **/
@Data
public class ImageCodeProperties {
	private int width = 67; //验证码图片宽度
	private int height = 23; //验证码图片长度
	private int len = 4; //验证码位数
	private int expireIn = 60; //验证码的有效时间秒
	private String url = "/login/teacher"; //需要校验验证码的接口

}
