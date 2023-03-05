package com.echo.country.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/8 19:29
 */
@Data
public class UpdateNews {
	@ApiModelProperty(name = "图标路径")
	private String ico;

	/**
	 * 标题
	 */
	@ApiModelProperty(name = "标题")
	private String title;

	/**
	 * 通知内容
	 */
	@ApiModelProperty("通知内容")
	private String descb;

	/**
	 * 发布组织
	 */
	@ApiModelProperty("发布组织")
	private String institutions;

}
