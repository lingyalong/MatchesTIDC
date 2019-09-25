package com.tidc.match.mapper;

import com.tidc.match.pojo.Team;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassNmae TeamMapper
 * @Description TODO
 * @Author 14631
 **/
@Repository
public interface TeamMapper {
	public void foundTeam(Team team);
	public void upMatchId(Team team);
	public List<Integer> getTeamMemberStudentId(int id);
}
