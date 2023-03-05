package com.echo.country.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/9 19:51
 */
@Data
public class AddServer {

	@ApiModelProperty("客服姓名")
	private String name;

	@ApiModelProperty("客服联系方式")
	private String tel;
}
