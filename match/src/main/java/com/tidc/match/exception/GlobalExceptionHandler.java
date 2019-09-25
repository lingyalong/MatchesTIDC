package com.tidc.match.exception;

import com.tidc.match.ov.RestfulUserVOResultBuilder;
import com.tidc.match.ov.UserOV;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassNmae GlobalExceptionHandler
 * @Description TODO
 * @Author 14631
 **/
@ControllerAdvice
public class GlobalExceptionHandler implements RestfulUserVOResultBuilder {
	/**
	 * 处理，实际为捕捉全局异常
	 *
	 * @param exception 全局异常
	 * @return 具体异常信息 后面那个是500报错内容是服务器内部异常
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<UserOV> handlerException(Exception exception) {
		//当未知异常发生时，将信息弹出堆栈
		exception.printStackTrace();
		return new ResponseEntity<>(faled(500,exception.getMessage()+"这个是全局异常难搞"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	/**
	 * 处理 http消息不可读异常
	 *
	 * @param httpMessageNotReadableException http消息不可读异常
	 * @return 具体异常信息
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<UserOV> handlerHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
		return new ResponseEntity<>(faled(1003,
				httpMessageNotReadableException.getMessage()+"  发送过来的http我读取不了"),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	/**
	 * 处理 找不到数据的异常或者数据不存在
	 * @param notFoundException notFoundException
	 * @return 具体异常信息
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<UserOV> handlerNotFoundException(NotFoundException notFoundException) {
		return new ResponseEntity<>(faled(1004,
				notFoundException.getMessage() + " is not found."+"   你要找的数据我没有"),
				HttpStatus.NOT_FOUND);
	}
	/**
	 * 处理 空指针异常
	 * @param nullPointerException 空指针异常
	 * @return 具体异常信息
	 */
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<UserOV> handlerNullPointerException(NullPointerException nullPointerException){
		nullPointerException.getMessage();
		return new ResponseEntity<>(faled(1013,
				nullPointerException.getMessage()+"  空指针异常"),
				HttpStatus.BAD_REQUEST);
	}
	/**
	 * 处理 超越权限去访问的异常
	 * @param peyondPowerException 权限异常
	 * @return 具体异常信息
	 */
	@ExceptionHandler(PeyondPowerException.class)
	public ResponseEntity<UserOV> PeyondPowerException(PeyondPowerException peyondPowerException){
		return new ResponseEntity<>(faled(500,
				peyondPowerException.getMessage()),
				HttpStatus.BAD_REQUEST);
	}
	/**
	 * 处理 重复报名或者创建或者评分`
	 * @param RepetitionException 重复
	 * @return 具体异常信息
	 */
	@ExceptionHandler(RepetitionException.class)
	public ResponseEntity<UserOV> RepetitionException(RepetitionException RepetitionException){
		return new ResponseEntity<>(faled(500,
				RepetitionException.getMessage()),
				HttpStatus.BAD_REQUEST);
	}


}
