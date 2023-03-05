package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限表
 *
 * @TableName role
 */
@Data
public class Role implements Serializable {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 *
	 */
	private String nameZh;

	private static final long serialVersionUID = 1L;
}