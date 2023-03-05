package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/3 9:43
 */
@Data
@ApiModel
public class QueryWelfareVo {
	@ApiModelProperty(value = "通知标题", required = false)
	private String title;

	@ApiModelProperty(value = "通知机构", required = false)
	private String institutions;

	@ApiModelProperty(value = "内容", required = false)
	private String descb;
}
