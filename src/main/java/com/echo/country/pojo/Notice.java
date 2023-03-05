package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 通知表
 *
 * @TableName notice
 */
@Data
public class Notice implements Serializable {
	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private String id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 配图
	 */
	private String img;

	/**
	 * 通知内容
	 */
	private String descb;

	/**
	 * 机构名称
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
	@TableField(fill = FieldFill.INSERT)
	@TableLogic
	private Integer isdeleted;

}