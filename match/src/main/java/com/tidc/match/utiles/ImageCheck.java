package com.tidc.match.utiles;

import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

/**
 * @ClassNmae ImageCheck
 * @Description TODO
 * @Author 14631
 **/
@Component
public class  ImageCheck {
	private  MimetypesFileTypeMap mtftp;
	public ImageCheck(){
		mtftp = new MimetypesFileTypeMap();
		/* 不添加下面的类型会造成误判 详见：http://stackoverflow.com/questions/4855627/java-mimetypesfiletypemap-always-returning-application-octet-stream-on-android-e*/
		mtftp.addMimeTypes("image png tif jpg jpeg bmp JPG");
	}
	public  boolean isImage(File file){
		String mimetype= mtftp.getContentType(file);
		String type = mimetype.split("/")[0];
		return type.equals("image");
	}

}
