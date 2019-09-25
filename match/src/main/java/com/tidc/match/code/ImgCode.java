package com.tidc.match.code;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassNmae ImgCode
 * @Description TODO
 * @Author 14631
 **/
@Data
public class ImgCode {
	private BufferedImage bufferedImage; //图片
	private String code; //验证码内容
	private LocalDateTime localDateTime; //验证码过期时间 这个时间一般是一个秒数 60秒之类的

	public ImgCode(BufferedImage bufferedImage, String code, int time) {
		this.bufferedImage = bufferedImage;
		this.code = code;
		this.localDateTime = LocalDateTime.now().plusSeconds(time);//这个是在当前时间上加上一个秒数
	}





	@Override
	public int hashCode() {
		return Objects.hash(bufferedImage, code, localDateTime);
	}

	public boolean isExpried() {
		return LocalDateTime.now().isAfter(localDateTime);
	}
}
