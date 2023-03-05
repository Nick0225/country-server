package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 轮播图
 *
 * @TableName picture
 */
@Data
public class Picture implements Serializable {
	/**
	 * 图片编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 图片地址
	 */
	private String img;

	/**
	 * 图片链接
	 */
	private String href;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;

	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private Integer isdeleted;

}