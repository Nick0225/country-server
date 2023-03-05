package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户信息表
 *
 * @TableName user_info
 */
@Data
public class UserInfo {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private String id;

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
	 * 短号
	 */
	private String tail;

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
	 * 是否为商家
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer isMerchant;

}