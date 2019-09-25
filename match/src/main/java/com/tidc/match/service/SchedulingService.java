package com.tidc.match.service;

import com.tidc.match.mapper.MatchsMapper;
import com.tidc.match.pojo.Matchs;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassNmae SchedulingService
 * @Description TODO
 * @Author 14631
 **/
@Service
public class SchedulingService {
	@Autowired
	MatchsMapper matchsMapper;

//	秒 分 时 日 月 周几
	@Scheduled(cron = "0 59 23 * * *")
	public void check() throws ParseException {
		//
		List<Matchs> list = matchsMapper.checkMatch();
//		获取当前时间
		Date date=new Date();
		//时间格式转换器
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (Matchs matchs : list) {
			Date time = formatter.parse(matchs.getTime());
			if(time.compareTo(date)!=1){
				//这就是超时了 完成
				matchsMapper.setFlag(matchs.getId());
			}

		}
	}
}
