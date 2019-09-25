package com.tidc.match.controller.checkController;


import com.tidc.match.ov.UserOV;
import com.tidc.match.service.CheckService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassNmae TeacherCheckController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin
@RequestMapping("/teacher")
@RestController
public class TeacherCheckController {
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	CheckService checkService;
	//目前没用问题
	@ApiOperation("返回当前老师所拥有可评分的比赛如果该老师没有创建任何比赛会返回404") //需要修改
	@GetMapping("/check/power")
	public UserOV getMatch(@AuthenticationPrincipal UserDetails userDetails) throws InterruptedException {
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getMatch(userDetails,userOV);
		return userOV;
	}
	//这个目前来看没有问题
	@ApiOperation("返回当前老师创建的比赛,如果该老师没有创建任何比赛会返回404")
	@GetMapping("/check/match")
	public UserOV mymatchs(@AuthenticationPrincipal UserDetails userDetails){
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.myMatchs(userDetails,userOV);
		return userOV;
	}
	//需要添加一个权限控制 当前老师只能查看他拥有的评分权限的比赛
	@ApiOperation("根据比赛的id来查询所有的参赛作品 需要传入一个比赛id")
	@GetMapping("/check/work/{id}")
	public UserOV getMatchsWorks(@PathVariable int id){
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = checkService.getMatchsWorks(id,userOV);
		return userOV;
	}

}
