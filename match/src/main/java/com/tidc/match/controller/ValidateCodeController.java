package com.tidc.match.controller;

import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.code.ImgCode;
import com.tidc.match.generator.ImageCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * @ClassNmae ValidateCodeController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin //解决跨域问题
@RequestMapping("/code")
@RestController()
public class ValidateCodeController {
	public static String SESSION_KEY = "SESSION_KEY_IMG_CODE";
	private SessionStrategy strategy = new HttpSessionSessionStrategy();
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	ImageCodeGenerator imageGenerator;
	@GetMapping("/img")
	public void createCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ImgCode imgCode = imageGenerator.createImageCode(new ServletWebRequest(req));
		//第一个参数会把req传进去SessionStrategy会从里面取出Session
		//第二个参数是放到Session里面的key
		resp.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
		strategy.setAttribute(new ServletWebRequest(req),SESSION_KEY,imgCode);
		//输出图片
		ImageIO.write(imgCode.getBufferedImage(),"JPEG",resp.getOutputStream());




	}


	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}
}
