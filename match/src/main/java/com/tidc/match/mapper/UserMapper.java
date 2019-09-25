package com.tidc.match.mapper;

import com.tidc.match.pojo.Matchs;
import com.tidc.match.pojo.Teacher;
import com.tidc.match.pojo.Student;
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
	public Student selectStudentEmail(Student student);
	public Teacher selectTeacherEmail(Teacher teacher);
	public Teacher findTeacher(String email);
	public void addStudent(Student student);
	public void addTeacher(Teacher teacher);
	public Student selectStudentUser(String value);
	public Student selectStudent_id(int value);
	public List<String> get_team_member_student_all(List<Integer> list);
}
