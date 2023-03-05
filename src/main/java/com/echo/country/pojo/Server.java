package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName server
 */
@Data
public class Server {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 客服名称
	 */
	private String name;

	/**
	 * 客服手机号
	 */
	private String tel;

	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private Integer isdeleted;
}