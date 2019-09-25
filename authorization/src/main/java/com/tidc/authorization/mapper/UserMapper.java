package com.tidc.authorization.mapper;


import com.tidc.authorization.pojo.Student;
import com.tidc.authorization.pojo.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassNmae UserMapper
 * @Description TODO
 * @Author 14631
 **/

@Repository
public interface UserMapper {
	public Student selectStudent(String value);
	public Teacher selectTeacher(String value);


}
