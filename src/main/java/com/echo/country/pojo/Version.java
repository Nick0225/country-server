package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName version
 */
@Data
public class Version {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 版本号
	 */
	private String appVersion;

	/**
	 * 下载链接
	 */
	private String href;

	private String descb;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createDate;

	@TableField(fill = FieldFill.INSERT)
	@TableLogic
	private Integer isdeleted;

}