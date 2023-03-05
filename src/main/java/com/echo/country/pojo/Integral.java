package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 积分表
 *
 * @TableName integral
 */
@Data
public class Integral {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 积分大小
	 */
	private Integer size;

	/**
	 * 用户编号
	 */
	private Long userId;

}