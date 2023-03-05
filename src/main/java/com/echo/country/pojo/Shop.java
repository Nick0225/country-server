package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @TableName shop
 */
@Data
public class Shop {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 店名
	 */
	private String shopName;

	/**
	 * 店地址
	 */
	private String shopAddress;

	/**
	 * 店主手机号
	 */
	private String tel;

}