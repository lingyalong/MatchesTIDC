package com.tidc.match.service;

import com.tidc.match.exception.PeyondPowerException;
import com.tidc.match.exception.WrongData;
import com.tidc.match.mapper.MatchsMapper;

import com.tidc.match.mapper.TeamMapper;
import com.tidc.match.mapper.UserMapper;
import com.tidc.match.mapper.WorksMaper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.*;



import com.tidc.match.utiles.ApplicationContextProvider;
import com.tidc.match.utiles.ImageCheck;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassNmae CheckService
 * @Description TODO
 * @Author 14631
 **/
@CacheConfig(cacheNames={"check"},cacheManager = "UserOVRedisCacheManager",keyGenerator = "keyGenerator")
@Service
public class CheckService {
	@Autowired
	public MatchsMapper matchsMapper;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	ImageCheck imageCheck;
	@Autowired
	UserMapper userMapper;
	@Autowired
	WorksMaper worksMaper;
	@Autowired
	TeamMapper teamMapper ;
	Logger l = LoggerFactory.getLogger(CheckService.class);
	@Cacheable
	public UserOV getMatch(UserDetails userDetails, UserOV userOV) throws InterruptedException {
		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
		List<Matchs> matchs = new ArrayList<>();
		List<Integer> list = matchsMapper.selectTeacherMatchs(teacher.getId());
		for (int i:list) {
			matchs.add(matchsMapper.selectMatchs(i));
		}
		if (matchs.size()==0){
			return userOV.setCode(404).setMessage("该老师没有任何可以评分的比赛").setData(null);
		}
		userOV.setCode(200).setMessage("查询成功").setData(matchs);
		return userOV;
	}
	@Cacheable
	public UserOV myMatchs(UserDetails userDetails, UserOV userOV){
		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
		List<Matchs> matchs = matchsMapper.selectMyMatchs(teacher.getId());
		if(matchs.size()==0){
			return userOV.setCode(404).setMessage("该老师没有创建任何比赛").setData(null);
		}
		userOV.setCode(200).setMessage("查询成功").setData(matchs);
		return userOV;
	}
	@Cacheable
	public UserOV getMatchs(int id,UserOV userOV) throws NotFoundException {
		Matchs matchs = matchsMapper.selectMatchs(id);
		if(matchs==null){
			throw new NotFoundException("没有这个id的比赛信息Id:"+id);
		}
		userOV.setCode(200).setMessage("成功").setData(matchs);
		return userOV;
	}
	@Cacheable
	public UserOV getMatchsWorks(int id,UserOV userOV){
		List<Works> list = matchsMapper.selectMatchsWorks(id);
		userOV.setCode(200).setData(list).setMessage("查询成功");
		return userOV;
	}
	@Cacheable
	public UserOV getTeam(int id,UserOV userOV){
		return null;
	}
	@Cacheable
	public UserOV getWorks(int id,UserOV userOV,UserDetails userDetails) throws NotFoundException, PeyondPowerException {
		Works works = null;
		boolean flage = true;
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_TEACHER")) {
				works = matchsMapper.selectWorks(id);
				userOV.setCode(200).setData(works).setMessage("查询成功");
				flage = false;

			}
		}
		if(flage){
			Student student = userMapper.selectStudent(userDetails.getUsername());
			Map map = new HashMap();
			map.put("works_student_id",student.getId());
			map.put("id",id);
			//走到这里的时候就证明这是个学生在查就需要看这个作品是否是这个学生的了
			works = worksMaper.selectStudentWorks(map);
			if(works==null){
				throw new PeyondPowerException(student.getName(),student.getId());
			}
		}
		Team team = matchsMapper.selectTeam_member(id);
		if(team==null){
			throw new NotFoundException("这个项目不存在"+"worksId: "+id);
		}
		team.setStudents(new ArrayList<>());
		team.setTeam_student(userMapper.selectStudent_id(team.getTeam_student_id()));
		for (int i:team.getList()) {
			team.getStudents().add(userMapper.selectStudent_id(i));
		}
		Map map = new HashMap();
		map.put("work",works);
		map.put("team",team);
		userOV.setCode(200).setData(map).setMessage("查询成功");
		return userOV;
	}
	@Cacheable
	public UserOV getWorkAll(UserOV userOV,UserDetails userDetails){
		Student student = userMapper.selectStudent(userDetails.getUsername());
		List<Works> list = worksMaper.getStudentWorksAll(student.getId());
		userOV.setCode(200).setMessage("查询成功").setData(list);
		return userOV;
	}
	@Cacheable
	public UserOV getScoreTop(int id,UserOV userOV){
		Map<String,Integer> map = new HashMap<>();
		List<Works> list = worksMaper.getMatchIdAll(id);
		userOV.setCode(200).setMessage("查询成功").setData(list);

		return userOV;
	}
	@Cacheable
	public UserOV getTeamMember(int student_id,int team_id,UserOV userOV) throws NotFoundException {
		List<Integer> list = teamMapper.getTeamMemberStudentId(team_id);
		list.add(student_id);
		List<String> listName = userMapper.get_team_member_student_all(list);
		if(listName.size()==0){
			throw new NotFoundException("你要找的队伍不存在");
		}
		System.out.println(listName);
		return userOV.setCode(200).setData(listName).setMessage("成功");

	}
	public UserOV getMatches(UserOV userOV){
		List<Matchs> list = matchsMapper.checkMatch();
		return userOV.setMessage("获取成功").setCode(200).setData(list);
	}
	public UserOV getMyMatches(UserOV userOV,UserDetails userDetails) throws WrongData {
		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
		Student student = userMapper.selectStudent(userDetails.getUsername());
		String school = null;
		if(student!=null){
			school = student.getSchool();
		}else{
			school=teacher.getSchool();
		}
		if(school==null){
			throw new WrongData("school",school);
		}
		List<Matchs> list = matchsMapper.checkMyMatch(school);
		return userOV.setMessage("获取成功").setCode(200).setData(list);
	}
}
