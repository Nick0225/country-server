package com.echo.country.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/3 19:38
 */
@Data
@Api(tags = "版本Vo")
public class VersionVo {

	/**
	 * 版本号
	 */
	@ApiModelProperty(value = "版本号", required = true)
	private String appVersion;

	/**
	 * 下载链接
	 */
	@ApiModelProperty(value = "下载链接", required = true)
	private String href;

	@ApiModelProperty(value = "版本描述", required = true)
	private String descb;

}
