package com.tidc.match.mapper;

import com.tidc.match.pojo.Score;
import org.springframework.stereotype.Repository;

/**
 * @ClassNmae ScoreMapper
 * @Description TODO
 * @Author 14631
 **/
@Repository
public interface ScoreMapper {
	public void putScore(Score score);
	public int selectStudent_id(int value);
	public Score selectScore(Score score);
}
