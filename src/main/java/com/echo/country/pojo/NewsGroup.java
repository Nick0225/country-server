package com.echo.country.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @TableName news_group
 */
@Data
public class NewsGroup {
	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 组编号
	 */
	private Long groupId;

	/**
	 * 消息编号
	 */
	private Long newsId;

}