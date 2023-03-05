package com.echo.country.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/21 21:16
 */
@Data
public class WelfareVo {

	@ApiModelProperty(value = "通知标题", required = true)
	private String title;

	@ApiModelProperty(value = "通知内容", required = true)
	private String descb;

	@ApiModelProperty(value = "通知机构", required = true)
	private String institutions;

	@ApiModelProperty(value = "通知的观看量", required = true)
	private Integer views;


}
