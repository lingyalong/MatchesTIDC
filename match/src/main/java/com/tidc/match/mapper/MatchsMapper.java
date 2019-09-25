package com.tidc.match.mapper;

import com.tidc.match.pojo.Matchs;
import com.tidc.match.pojo.Power;
import com.tidc.match.pojo.Team;
import com.tidc.match.pojo.Works;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassNmae MatchsMapper
 * @Description TODO
 * @Author 14631
 **/
@Repository
public interface MatchsMapper {
	public List<Integer> selectTeacherMatchs(int value);
	public int insertMatchs(Matchs matchs);
	public Matchs selectMatchs(int value);
	public List<Matchs> selectMyMatchs(int value);
	public void addPower(Power power);
	public List<Works> selectMatchsWorks(int value);
	public Team selectTeam_member(int value);
	public Works selectWorks(int value);
	public void numberUP(int id);
	public void updateMatch(Matchs matchs);
	public List<Matchs> checkMatch();
	public void setFlag(int id);
	public List<Matchs> checkMyMatch(String value);

}
