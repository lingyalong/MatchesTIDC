package com.tidc.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ClassNmae Works
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
@Scope(value = "prototype")
@Accessors(chain = true) //这个注解可以使这个类的set方法返回当前对象
public class Works implements Serializable {
	private int id;
	private int works_student_id;
	private String name;
	private String logo;
	private String brief;
	private String url;
	private int works_matchs_id;
	private int works_team_id;
	private int score;
}
