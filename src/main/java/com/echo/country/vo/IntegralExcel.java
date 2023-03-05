package com.echo.country.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/14 20:24
 */
@Data
public class IntegralExcel {

	@ExcelIgnore
	private String id;

	@ExcelProperty(value = "姓名", index = 1)
	private String name;

	@ExcelProperty(value = "联系方式", index = 2)
	private String tel;

	@ExcelProperty(value = "积分大小", index = 3)
	private Integer number;

}
