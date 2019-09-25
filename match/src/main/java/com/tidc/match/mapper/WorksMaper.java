package com.tidc.match.mapper;

import com.tidc.match.pojo.Works;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassNmae WorksMaper
 * @Description TODO
 * @Author 14631
 **/
@Repository
public interface WorksMaper {
	public void foundWorks(Works works);
	public Works affirmUrl(Map map);
	public Works selectStudentWorks(Map map);
	public void addScore(Map map);
	public List<Works> getStudentWorksAll(int id);
	public List<Works> getMatchIdAll(int id);
	public Works repetition(Map map);
	public void putWorks(Works works);

}
