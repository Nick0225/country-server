package com.echo.country.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 分组描述表
 *
 * @TableName group_desc
 */
@Data
public class GroupDesc {
	/**
	 * ID
	 */
	@ExcelProperty(value = "编号", index = 0)
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 上级ID
	 */
	@ExcelProperty(value = "上级编号", index = 1)
	private Integer parentId;

	/**
	 * 分组名称
	 */
	@ExcelProperty(value = "分组名称", index = 2)
	private String name;

	@ExcelIgnore
	@TableField(exist = false)
	private Boolean hasChildren;

}