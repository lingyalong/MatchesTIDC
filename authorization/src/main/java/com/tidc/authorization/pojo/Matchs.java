package com.tidc.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ClassNmae Match
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
@Scope(value = "prototype")
@Accessors(chain = true) //这个注解可以使这个类的set方法返回当前对象
public class Matchs implements Serializable {
	private int id;
	private String name;
	private String brief;
	private String url;
	private String logo;
	private String school;
	private String domain;
	private Integer teacher_id;
	private String time;
	private Integer number;
	private int flag ;

}
