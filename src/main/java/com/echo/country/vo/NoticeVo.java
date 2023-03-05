package com.echo.country.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/19 23:21
 */
@Data
public class NoticeVo {

	@ApiModelProperty("通知标题")
	private String title;

	@ApiModelProperty("通知内容")
	private String descb;

	@ApiModelProperty("通知图片")
	private String img;

	@ApiModelProperty("通知机构")
	private String institutions;

	@ApiModelProperty("通知的观看量")
	private Integer views;

//	@ApiModelProperty("是否是乡长发布（是:传所在乡的id，不是传零）")
//	private Integer isSteward;
}
