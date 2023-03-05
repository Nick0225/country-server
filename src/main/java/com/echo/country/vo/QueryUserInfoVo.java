package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/3 8:51
 */
@ApiModel
@Data
public class QueryUserInfoVo {
	@ApiModelProperty(value = "条件查询姓名", required = false)
	String name;
	@ApiModelProperty(value = "条件查询村名", required = false)
	String townshipName;
	@ApiModelProperty(value = "条件查询乡名", required = false)
	String villageName;
	@ApiModelProperty(value = "条件查询组名", required = false)
	String groupName;
}
