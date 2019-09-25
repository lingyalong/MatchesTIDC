package com.tidc.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ClassNmae Teacher
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
@Scope(value = "prototype")
@Accessors(chain = true) //这个注解可以使这个类的set方法返回当前对象
public class Teacher implements Serializable {
	private int id;
	private String userName;
	private String password;
	private String telephone;
	private String email;
	private String school;
//放一个他创建的比赛的list
	//放一个他有评分权限的list


}
