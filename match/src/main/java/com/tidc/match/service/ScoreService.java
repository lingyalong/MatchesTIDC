package com.tidc.match.service;

import com.tidc.match.exception.RepetitionException;
import com.tidc.match.exception.WrongData;
import com.tidc.match.mapper.ScoreMapper;
import com.tidc.match.mapper.UserMapper;
import com.tidc.match.mapper.WorksMaper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Score;
import com.tidc.match.pojo.Teacher;
import com.tidc.match.utiles.ApplicationContextProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

/**
 * @ClassNmae ScoreService
 * @Description TODO
 * @Author 14631
 **/
@Service
public class ScoreService {
	@Autowired
	ScoreMapper scoreMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	WorksMaper worksMaper;
	public UserOV postScore(UserDetails userDetails,Score score, UserOV userOV) throws RepetitionException, WrongData {
		Teacher teacher = userMapper.selectTeacher(userDetails.getUsername());
		//先进行查询这个老师有没有进行评分
		if(score.getScore()>100||score.getScore()<0){
   			throw new WrongData("score",score.getScore());
		}
		Score score2 = ac.getBean(Score.class); //这个是为了测试是否重复评分
		score2.setScore_works_id(score.getScore_works_id());
		score2.setTeacher_id(teacher.getId());
		score2 = scoreMapper.selectScore(score2);
		if(score2!=null){
			throw new RepetitionException(teacher.getUserName());
		}
		score.setStudent_id(scoreMapper.selectStudent_id(score.getScore_works_id()));
		score.setTeacher_id(teacher.getId());
		Map map = new HashMap();
		scoreMapper.putScore(score);
		map.put("id",score.getScore_works_id());
		map.put("score",score.getScore());
		worksMaper.addScore(map);
		userOV.setCode(200).setMessage("评分成功");
		return userOV;
	}
	public UserOV getScore(UserDetails userDetails, UserOV userOV, Score score){
		Teacher teacher = (userMapper.selectTeacher(userDetails.getUsername()));
		score.setTeacher_id(teacher.getId());
		score = scoreMapper.selectScore(score);
		if(score==null){
			userOV.setCode(404).setMessage("该老师没有进行评分");
			return userOV;
		}
		userOV.setCode(200).setMessage("该老师以及进行过了评分").setData(score);
		return userOV;
	}
}
