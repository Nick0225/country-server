package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/26 17:33
 */
@Data
@ApiModel
public class PictureVo {

	@ApiModelProperty(value = "图片链接", required = false)
	private String href;

	@ApiModelProperty(value = "图片地址", required = false)
	private String img;

}
