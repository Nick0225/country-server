package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 公益宣传表
 *
 * @TableName welfare
 */
@Data
public class Welfare {
	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	private String title;

	/**
	 * 宣传标题
	 */
	private String descb;

	/**
	 * 宣传机构
	 */
	private String institutions;

	/**
	 * 浏览量
	 */
	private Integer views;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;

	/**
	 * 逻辑删除
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private Integer isdeleted;

}