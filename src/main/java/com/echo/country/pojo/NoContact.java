package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 通讯录
 *
 * @TableName no_contact
 */
@TableName(value = "no_contact")
@Data
@ApiModel
public class NoContact implements Serializable {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "编号（不传）", required = false)
	private String id;

	/**
	 * 姓名
	 */
	@ApiModelProperty(name = "name", value = "姓名（不传）", required = true)
	private String name;

	/**
	 * 性别
	 */
	@ApiModelProperty(name = "sex", value = "性别", required = true)
	private String sex;

	/**
	 * 地址
	 */
	@ApiModelProperty(name = "address", value = "地址", required = false)
	private String address;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(name = "tel", value = "联系方式", required = true)
	private String tel;

	/**
	 * 民族
	 */
	@ApiModelProperty(name = "nation", value = "民族", required = true)
	private String nation;

	/**
	 * 权限
	 */
	@ApiModelProperty(name = "role", value = "权限", required = false)
	private String role;

	/**
	 * 积分大小
	 */
	@ApiModelProperty(name = "integralSize", value = "积分大小", required = false)
	private Integer integralSize;

	/**
	 * 县名称（不存在为 无）
	 */
	@ApiModelProperty(name = "countyName", value = "县名", required = true)
	private String countyName;

	/**
	 * 乡名称（不存在为 无）
	 */
	@ApiModelProperty(name = "townshipName", value = "乡名", required = true)
	private String townshipName;

	/**
	 * 村名称（不存在为 无）
	 */
	@ApiModelProperty(name = "villageName", value = "村名", required = true)
	private String villageName;

	/**
	 * 组名称（不存在为 无）
	 */
	@ApiModelProperty(name = "groupName", value = "组名", required = true)
	private String groupName;

	/**
	 * 身份证号
	 */
	@ApiModelProperty(name = "personId", value = "身份证号", required = true)
	private String personId;

	/**
	 * 短号
	 */
	@ApiModelProperty(name = "tail", value = "短号", required = false)
	private String tail;

	/**
	 * 是否允许注册
	 */
	@ApiModelProperty(name = "isAccess", value = "是否通过注册", required = false)
	private Integer isAccess;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

}