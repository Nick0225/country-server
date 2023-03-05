package com.echo.country.vo;

import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/8 20:31
 */
@Data
public class ShopUser {
	/**
	 * 编号
	 */
	private Long id;

	/**
	 * 头像
	 */
	private String image;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 民族
	 */
	private String nation;

	/**
	 * 身份证号
	 */
	private String personId;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 联系方式
	 */
	private String tel;

	/**
	 * 权限
	 */
	private String role;

	/**
	 * 积分大小
	 */
	private Integer integralSize;

	/**
	 * 县名称
	 */
	private String countyName;

	/**
	 * 乡名称（不存在为 无）
	 */
	private String townshipName;

	/**
	 * 村名称（不存在为 无）
	 */
	private String villageName;

	/**
	 * 组名称（不存在为 无）
	 */
	private String groupName;

	/**
	 * 店名
	 */
	private String shopName;

	/**
	 * 店地址
	 */
	private String shopAddress;

	private Integer isMerchant;


}
