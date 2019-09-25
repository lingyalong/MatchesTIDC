package com.tidc.match.ov;

/**
 * @ClassNmae RestfulUserVOResultBuilder
 * @Description TODO
 * @Author 14631
 **/
public interface RestfulUserVOResultBuilder {
	/**
	 * 成功Status标识
	 */
	String STATUS_SUCCESS = "success";
	/**
	 * 失败Status标识
	 */
	String STATUS_FAILED = "failed";
	default UserOV success(Object object,int code,String message){
		return new UserOV(code,message,object);
	}
	default UserOV faled(int code,String message){
		return new UserOV(code,message);
	}

}
