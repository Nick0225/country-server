package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/26 0:01
 */
@Data
@ApiModel("注册Vo")
public class LoginUser {
	@ApiModelProperty(name = "用户名", required = true)
	private String username;
	@ApiModelProperty(name = "密码", required = true)
	private String password;
	@ApiModelProperty(name = "验证消息", required = false)
	private String msg;
}
