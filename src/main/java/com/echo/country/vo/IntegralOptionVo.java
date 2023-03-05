package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/3 9:35
 */
@Data
@ApiModel("积分操作Vo")
public class IntegralOptionVo {

	@ApiModelProperty(name = "options", value = "积分操作（1代表加，0代表减）", required = true)
	Integer options;
	@ApiModelProperty(name = "num", value = "加减数值", required = true)
	Integer num;
	@ApiModelProperty(name = "descb", value = "操作描述", required = true)
	String descb;
	@ApiModelProperty(name = "name", value = "核销店名", required = true)
	String name;

}
