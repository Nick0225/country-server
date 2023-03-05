package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 积分操作表
 *
 * @TableName integral_option
 */
@Data
public class IntegralOption {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 具体操作（1代表加，0代表减）
	 */
	private Integer options;

	/**
	 * 操作描述
	 */
	private String descb;

	/**
	 * 操作人
	 */
	private String name;

	/**
	 * 数值
	 */
	private Integer num;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	/**
	 * 操作积分编号
	 */
	private Long integralId;

}