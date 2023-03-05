package com.echo.country.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/24 18:17
 */
@Data
public class ContactExcel {

	@ExcelProperty(value = "名字", index = 1)
	private String name;

	@ExcelProperty(value = "性别", index = 2)
	private String sex;

	@ExcelProperty(value = "民族", index = 3)
	private String nation;

	@ExcelProperty(value = "职务", index = 4)
	private String role;

	@ExcelProperty(value = "地址", index = 5)
	private String address;

	@ExcelProperty(value = "手机号码", index = 6)
	private String tel;

	@ExcelProperty(value = "身份证号", index = 7)
	private String personId;

	@ExcelProperty(value = "积分", index = 8)
	private Integer integralSize;

	@ExcelProperty(value = "县", index = 9)
	private String countyName;

	@ExcelProperty(value = "乡镇", index = 10)
	private String townshipName;

	@ExcelProperty(value = "村", index = 11)
	private String villageName;

	@ExcelProperty(value = "小组", index = 12)
	private String groupName;

	@ExcelProperty(value = "短号", index = 13)
	private String tail;


}
