package com.echo.country.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/26 12:26
 */
@ApiModel("修改用户接受实体类")
@Data
public class UpdateUserinfoVo {

	@ApiModelProperty(value = "用户姓名", required = false)
	private String name;

	@ApiModelProperty(value = "用户头像连接", required = false)
	private String image;

	@ApiModelProperty(value = "性别", required = false)
	private String sex;

	@ApiModelProperty(value = "民族", required = false)
	private String nation;

	@ApiModelProperty(value = "县名", required = false)
	private String countyName;

	@ApiModelProperty(value = "乡名", required = false)
	private String townshipName;

	@ApiModelProperty(value = "村名", required = false)
	private String villageName;

	@ApiModelProperty(value = "组名", required = false)
	private String groupName;

	@ApiModelProperty(value = "地址（有就上面四个拼接）", required = false)
	private String address;
}
