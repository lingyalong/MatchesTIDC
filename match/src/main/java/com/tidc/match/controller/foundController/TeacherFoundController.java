package com.tidc.match.controller.foundController;

import com.tidc.match.exception.PeyondPowerException;
import com.tidc.match.mapper.MatchsMapper;
import com.tidc.match.mapper.UserMapper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Matchs;
import com.tidc.match.service.FoundService;
import com.tidc.match.utiles.ApplicationContextProvider;
import com.tidc.match.utiles.FileHandel;
import com.tidc.match.utiles.ImageCheck;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @ClassNmae TeacherFoundController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin
@RequestMapping("/teacher")
@RestController
public class TeacherFoundController {
	@Autowired
	MatchsMapper matchsMapper;
	@Autowired
	public UserMapper userMapper;
	@Autowired
	ImageCheck imageCheck;
	@Autowired
	ApplicationContextProvider ac;
	@Autowired
	FoundService foundService;
	@ApiOperation("创建比赛 需要:name(比赛名称) brief(比赛简介) files(一个比赛封面和一个比赛详情压缩包需要验证后缀名格式) domain(比赛的领域范围) time(比赛报名结束时间)")
	@PostMapping("/found/match")
	public UserOV foundMatch(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest req, Matchs matchs){

		UserOV userOV = ac.getBean(UserOV.class);
		System.out.println(matchs);
		try {
			userOV = foundService.postMatch(userDetails,matchs,userOV);
		} catch (IOException e) {
			e.printStackTrace();
			userOV.setData(null).setCode(500).setMessage("比赛创建失败");
		}
		return userOV;
	}
//	@ApiOperation("创建好项目之后增加自己为评分老师的")
//	@PostMapping("/add/mypower")
//	public UserOV addmypower(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest req){
//		UserOV userOV = ac.getBean(UserOV.class);
//		userOV = foundService.addmypower(userDetails,req,userOV);
//		return userOV;
//	}
	//没写好
	@ApiOperation("增加评分老师的,需要被添加的老师的email和比赛的id")
	@PostMapping("/add/power")
	public UserOV addTeacher(@AuthenticationPrincipal UserDetails userDetails,@RequestParam("email")String email,@RequestParam("id") int id, HttpServletRequest req) throws NotFoundException, PeyondPowerException {
		UserOV userOV = ac.getBean(UserOV.class);
		userOV = foundService.addTeacher(userDetails,email,id,userOV,req);
		return userOV;
	}

//	@ApiOperation("判断老师是否有修改当前的项目的权利并返回项目的原数据,需要项目")
//	@GetMapping("/if/match")
//	public UserOV ifMatchs(@AuthenticationPrincipal UserDetails userDetails,int id,HttpServletRequest req){
//		UserOV userOV = ac.getBean(UserOV.class);
//		userOV = foundService.ifMatchs(userDetails,id,req,userOV);
//		return userOV;
//	}
	@ApiOperation("修改比赛项目,")
	@PutMapping("/alter/match")
	public UserOV putMatch( @AuthenticationPrincipal UserDetails userDetails,Matchs matchs,HttpServletRequest req) throws PeyondPowerException, NotFoundException {
		UserOV userOV = ac.getBean(UserOV.class);
		try {
			userOV = foundService.putMatchs(userDetails,matchs,req,userOV);
		} catch (IOException e) {
			e.printStackTrace();
			userOV.setData(null).setCode(500).setMessage("修改失败");
		}
		return userOV;
	}
}
