package com.tidc.match.controller.scoreController;

import com.tidc.match.exception.RepetitionException;
import com.tidc.match.exception.WrongData;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Score;
import com.tidc.match.service.ScoreService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassNmae TeacherScoreController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin //解决跨域问题
@RequestMapping("/teacher")
@RestController()

public class TeacherScoreController {
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	ScoreService scoreService;
	@ApiOperation("根据当前登录的老师id以及作品id来给作品评分，只需要分数以及比赛的id和作品id 如果返回500则是提醒该老师已经进行过评分了")
	@PostMapping("/score")
	public UserOV postScore(@AuthenticationPrincipal UserDetails userDetails, Score score, HttpServletRequest req) throws RepetitionException, WrongData {
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = scoreService.postScore(userDetails,score,userOV);
		return userOV;
	}
	@ApiOperation("根据当前登录的老师id以及作品id来查看这个作品老师是否有进行过评分以及评了多少分,需要作品id")
	@GetMapping("/score/{id}")
	public UserOV getScore(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int id,HttpServletRequest req){
		UserOV userOV = ac.getBean(UserOV.class);
		Score score = ac.getBean(Score.class);
		score.setScore_works_id(id);
		userOV = scoreService.getScore(userDetails,userOV,score);
		return userOV;
	}
}
