package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName href
 */
@Data
public class Href {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private String id;

	/**
	 * 图标
	 */
	private String ico;

	/**
	 * 外链名称
	 */
	private String name;

	/**
	 * 链接
	 */
	private String href;

	@TableField(fill = FieldFill.INSERT)
	private String create_time;

	@TableField(fill = FieldFill.INSERT)
	@TableLogic
	private Integer isdeleted;

}