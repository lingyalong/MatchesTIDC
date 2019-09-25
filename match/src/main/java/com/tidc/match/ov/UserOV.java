package com.tidc.match.ov;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ClassNmae userov
 * @Description TODO
 * @Author 14631
 **/
@Scope(value = "prototype")
@Component
@Accessors(chain = true) //这个注解可以使这个类的set方法返回当前对象
@Data
public class UserOV implements Serializable {
//	状态码
	private int code;
//	信息 message   (成功或失败的原因)
	private String message;
	//	Data (Json数据)
	private Object data ;

	public UserOV() {
	}

	public UserOV(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public UserOV(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
}
