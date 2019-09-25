package com.tidc.match.controller.DownController;

import com.tidc.match.ov.UserOV;
import com.tidc.match.service.DownService;
import com.tidc.match.utiles.ApplicationContextProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassNmae TeacherDownController
 * @Description TODO
 * @Author 14631
 **/
@CrossOrigin //解决跨域问题
@Api(value = "文件下载")
@RequestMapping("/teacher")
@RestController()
public class TeacherDownController {
	@Autowired
	private ApplicationContextProvider ac;
	@Autowired
	private DownService downService;
	@GetMapping("/down/file/{id}")
	@ApiOperation("老师下载文件需要下载的项目的id" )
	public UserOV downFile(@PathVariable("id") int worksId,HttpServletResponse resp){
		UserOV userOV = ac.getBean(UserOV.class);
		try {
			downService.teacherDownFile(resp,worksId,userOV);
		} catch (Exception e) {
			e.printStackTrace();
			userOV.setCode(500).setMessage("下载出错");
		}
		return userOV;
	}
}
