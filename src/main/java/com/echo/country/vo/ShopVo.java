package com.echo.country.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/8 19:10
 */
@Data
public class ShopVo {

	/**
	 * 店名
	 */
	@ApiModelProperty("店名")
	private String shopName;

	/**
	 * 店地址
	 */
	@ApiModelProperty("店地址")
	private String shopAddress;


}
