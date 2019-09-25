package com.tidc.match.controller.foundController;

import com.tidc.match.exception.PeyondPowerException;
import com.tidc.match.exception.RepetitionException;
import com.tidc.match.mapper.WorksMaper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.Team;
import com.tidc.match.pojo.Works;
import com.tidc.match.service.FoundService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassNmae StudentFoundController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin
@RequestMapping("/student")
@RestController
public class StudentFoundController {
	@Autowired
	private ApplicationContextProvider ac;
	@Autowired
	private WorksMaper worksMaper;
	@Autowired
	private FoundService foundService;
	//重复报名没有解决 要改

	@ApiOperation("学生报名比赛的接口需要比赛的所有信息包含name brief和works_matchs_id")
	@PostMapping("/found/match")
	public UserOV foundWorks(Team team, Works works, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest req) throws RepetitionException {
		UserOV userOV = ac.getBean(UserOV.class);
		try {
			userOV = foundService.foundWorks(userDetails,works,userOV,req);
		} catch (IOException e) {
			userOV.setCode(500).setMessage("创建失败请重新再试");
		}
		return userOV;
	}
//	要改
	@ApiOperation("学生修改自己的项目") //需要将文件上传之后将之前的文件删除
	@PutMapping("/found/match")
	public UserOV putWorks(Works works,@AuthenticationPrincipal UserDetails userDetails) throws PeyondPowerException {
		UserOV userOV = ac.getBean(UserOV.class);
		try {
			userOV = foundService.putWorks(userOV,works,userDetails);
		} catch (IOException e) {
			e.printStackTrace();
			userOV.setMessage("服务器繁忙").setCode(500);
		}
		return userOV;
	}

}
