package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息表
 *
 * @TableName news
 */
@Data
public class News implements Serializable {
	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 图标
	 */
	private String ico;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 通知内容
	 */
	private String descb;

	/**
	 * 发布组织
	 */
	private String institutions;

	/**
	 * 是否全部接收
	 */
	private Integer isAllr;

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