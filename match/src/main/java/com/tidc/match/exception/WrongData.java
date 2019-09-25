package com.tidc.match.exception;

/**
 * @ClassNmae WrongData
 * @Description TODO
 * @Author 14631
 **/
public class WrongData extends Exception{
	private String name;
	private Object data;
	public WrongData(String name,Object data){
		this.name = name;
		this.data = data;
	}

	@Override
	public String getMessage() {
		return name+"出现异常"+ "内容是"+data;
	}
}
