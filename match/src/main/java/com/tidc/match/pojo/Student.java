package com.tidc.match.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @ClassNmae User
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
@Scope(value = "prototype")
@Accessors(chain = true) //这个注解可以使这个类的set方法返回当前对象
public class Student implements Serializable {
//	@Pattern(regexp = "/^$/", message = "didsaf")
	private int id;
	@Pattern(regexp = "^[\\u4e00-\\u9fa5]{0,}$",message = "只能输入汉字")
	private String name; //名字
	@Pattern(regexp = "^[a-zA-Z]\\w{5,17}$",message = "以字母开头，长度在6~18之间，只能包含字符、数字和下划线。")
	private String password; //密码
	@Pattern(regexp = "^\\d{11}$",message = "手机号格式错误")
	private String telephone; //手机
	@Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "邮箱格式错误")
	private String email; //邮箱
	private String gender; //性别
	private String major; //专业
	private String classGrade; //班级
//	@Pattern(regexp = "\\d{15}(\\d{2}[0-9xX])?",message = "身份证号格式错误")
	private String card;
	private String studentNum;
	private String school;
	//放一个他创建的比赛的List
	//放一个他参加的比赛的List
}
