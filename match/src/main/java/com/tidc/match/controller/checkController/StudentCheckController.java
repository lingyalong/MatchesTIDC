package com.tidc.match.controller.checkController;


import com.tidc.match.exception.PeyondPowerException;
import com.tidc.match.exception.WrongData;
import com.tidc.match.ov.UserOV;
import com.tidc.match.service.CheckService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassNmae StudentCheckController
 * @Description TODO
 * @Author 14631
 **/


@CrossOrigin
@RequestMapping("/student")
@RestController
public class StudentCheckController {
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	CheckService checkService;

	@ApiOperation("使用teamID和队长id获取整个team")
	@GetMapping("/check/team/{student_id}/{team_id}")
	public UserOV teamId(@PathVariable("student_id")int student_id,@PathVariable("team_id") int team_id) throws NotFoundException {
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getTeamMember(student_id,team_id, userOV);
		return userOV;
	}
	@ApiOperation("根据比赛id来返回比赛的详细信息 具体是/check/match/id(这里是数字,动态的)")
	@GetMapping("/check/match/{id}")
	public UserOV getmatches(@PathVariable("id") int id) throws NotFoundException {
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getMatchs(id, userOV);
		return userOV;
	}


	@ApiOperation("当前登录的学生查看自己创建的所有比赛 但是如果没用创建过比赛就会返回一个空数组")
	@GetMapping("/check/works")
	public UserOV myWorks(@AuthenticationPrincipal UserDetails userDetails){
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getWorkAll(userOV,userDetails);
		return userOV;
	}

	@ApiOperation("根据参赛作品的id来查看该作品的详细信息包括队伍人员查询 具体是/check/work/id(这里是数字,动态的),如果是老师着可以无条件查询学生只能查看自己的")
	@GetMapping("/check/work/{id}")
	public UserOV getWorks(@PathVariable("id") int id,@AuthenticationPrincipal UserDetails userDetails) throws NotFoundException, PeyondPowerException {
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getWorks(id,userOV,userDetails);
		return userOV;
	}
	@ApiOperation(value = "根据比赛id查看分数排行榜 返回所有人的比赛名字属性和分数总分和平均分")
	@GetMapping("/check/score/top/{id}")
	public UserOV getScoreTop(@PathVariable int id){
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getScoreTop(id,userOV);
		return userOV;
	}
	@PreAuthorize(value = "hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_STUDENT')")
	@ApiOperation(value = "查询所有的比赛")
	@GetMapping("/check/match")
	public UserOV getMatches(@AuthenticationPrincipal UserDetails userDetails){

		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getMatches(userOV);
		return userOV;
	}
	@ApiOperation(value = "查询当前学生的学校的比赛")
	@GetMapping("/check/school/match")
	public UserOV getMyMatches(@AuthenticationPrincipal UserDetails userDetails) throws WrongData {
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getMyMatches(userOV,userDetails);
		return userOV;
	}

}
