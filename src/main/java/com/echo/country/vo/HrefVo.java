package com.echo.country.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/5 14:28
 */
@Data
public class HrefVo {

	@ApiModelProperty(name = "ico", value = "外链图标", required = false)
	private String ico;

	@ApiModelProperty(name = "name", value = "外链名称", required = false)
	private String name;

	@ApiModelProperty(name = "href", value = "外链链接", required = false)
	private String href;


}
