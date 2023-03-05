package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 通讯录
 *
 * @TableName contact
 */
@Data
public class Contact {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private String id;

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
	 * 职务
	 */
	private String role;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 联系方式
	 */
	private String tel;

	/***
	 * 身份证号
	 */
	private String personId;

	/**
	 * 积分大小
	 */
	private Integer integralSize;

	/**
	 * 县名称
	 */
	private String countyName;

	/**
	 * 乡名称
	 */
	private String townshipName;

	/**
	 * 村名称
	 */
	private String villageName;

	/**
	 * 组名称（不存在为 无）
	 */
	private String groupName;

	/**
	 * 短号
	 */
	private String tail;
}