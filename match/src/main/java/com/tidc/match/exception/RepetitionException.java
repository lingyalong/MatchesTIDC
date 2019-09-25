package com.tidc.match.exception;

/**
 * @ClassNmae RepetitionException
 * @Description TODO
 * @Author 14631
 **/
public class RepetitionException extends Exception{
	private String name;
	public RepetitionException(String name){
		this.name = name;
	}

	@Override
	public String getMessage() {
		return name+"在重复创建或者报名比赛或者在重复评分";
	}
}
