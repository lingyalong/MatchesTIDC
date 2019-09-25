package com.tidc.match.exception;

/**
 * @ClassNmae PeyondPowerException
 * @Description TODO
 * @Author 14631
 **/
public class PeyondPowerException extends Exception{
	private String studentName;
	private int id;
	public PeyondPowerException(String studentName, int id){
		super();
		this.studentName = studentName;
		this.id = id;
	}
	@Override
	public String getMessage(){
		return String.join( "id:"+id+"name"+studentName) +"试图访问一些它不该访问的内容";
	}
}
