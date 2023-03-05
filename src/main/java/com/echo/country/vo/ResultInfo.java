package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/14 4:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("统一返回结果实体类")
public class ResultInfo {
	@ApiModelProperty("响应消息")
	private String message;
	@ApiModelProperty("状态码")
	private Integer code;
	@ApiModelProperty("返回实体类")
	private Object object;


	/**
	 * 成功返回结果
	 *
	 * @param message
	 * @param object
	 * @return
	 */
	public static ResultInfo success(String message, Object object) {

		return new ResultInfo(message, 200, object);

	}

	/**
	 * 失败返回结果
	 *
	 * @param message
	 * @param code
	 * @return
	 */
	public static ResultInfo failure(String message, int code) {
		return new ResultInfo(message, code, null);
	}


}
