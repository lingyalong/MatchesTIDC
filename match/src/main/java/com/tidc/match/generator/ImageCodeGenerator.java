package com.tidc.match.generator;

import com.tidc.match.Properties.SecurityProperties;
import com.tidc.match.code.ImgCode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @ClassNmae ImageCodeGenerator
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
public class ImageCodeGenerator implements ValidateCodeGenerator {
	@Autowired
	private SecurityProperties securityProperties;
	@Override
	public ImgCode createImageCode(ServletWebRequest req) {
		int width = securityProperties.getCode().getImage().getWidth();
		int height = securityProperties.getCode().getImage().getHeight();
		System.out.println(securityProperties.getCode().getImage());
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("宋体", Font.BOLD, 18));

		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < securityProperties.getCode().getImage().getLen(); i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		g.dispose();

		return new ImgCode(image, sRand, securityProperties.getCode().getImage().getExpireIn()); //有效期
	}

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
