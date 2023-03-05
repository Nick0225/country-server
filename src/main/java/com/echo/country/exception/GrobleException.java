package com.echo.country.exception;

import com.echo.country.vo.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Echo-9527
 * @Describe 全局异常处理
 * @Version 1.0
 * @date 2022/2/20 14:48
 */
@Slf4j
@ControllerAdvice
public class GrobleException {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResultInfo exception(Exception e) {

//		log.error(e.getMessage());
		e.printStackTrace();
		return ResultInfo.failure("服务器异常...", 500);
	}

}
