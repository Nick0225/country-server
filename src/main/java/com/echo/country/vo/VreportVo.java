package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/4/23 14:56
 */
@Data
@ApiModel
public class VreportVo {

	/**
	 * 图片
	 */
	@ApiModelProperty(value = "图片", required = false)
	private String ico;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容", required = true)
	private String descb;

	/**
	 * 上报人
	 */
	@ApiModelProperty(value = "上报人", required = true)
	private String name;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "上报人联系方式", required = true)
	private String tel;

	/**
	 * 村名
	 */
	@ApiModelProperty(value = "上报人所在村", required = true)
	private String villageName;

}
