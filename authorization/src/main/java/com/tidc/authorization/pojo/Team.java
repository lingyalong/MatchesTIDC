package com.tidc.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassNmae Team
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
@Scope(value = "prototype")
@Accessors(chain = true) //这个注解可以使这个类的set方法返回当前对象
public class Team implements Serializable {
	private int id;
	private String name;
	private int team_student_id;
	private int team_works_id;
	private Student team_student;
	private List<Integer> list;
	private List<Student> students;

}
