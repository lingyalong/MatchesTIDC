package com.tidc.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ClassNmae Power
 * @Description TODO
 * @Author 14631
 **/
@Data
@Component
@Scope(value = "prototype")
@Accessors(chain = true) //这个注解可以使这个类的set方法返回当前对象

public class Power implements Serializable {

	private int id;
	private int power_teacher_id;
	private int power_matchs_id;
}
