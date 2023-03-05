package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName v_report
 */
@TableName(value = "v_report")
@Data
public class Vreport {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 图片
	 */
	private String ico;

	/**
	 * 内容
	 */
	private String descb;

	/**
	 * 上报人
	 */
	private String name;

	/**
	 * 手机号
	 */
	private String tel;

	/**
	 * 村编号
	 */
	private String villageName;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

}