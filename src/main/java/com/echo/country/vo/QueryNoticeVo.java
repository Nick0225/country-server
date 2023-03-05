package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/3 18:01
 */
@Data
@ApiModel
public class QueryNoticeVo {
	@ApiModelProperty(value = "通知标题", required = false)
	private String title;

	@ApiModelProperty(value = "通知机构", required = false)
	private String institutions;

	@ApiModelProperty(value = "内容", required = false)
	private String descb;
}
