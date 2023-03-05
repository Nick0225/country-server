package com.echo.country.vo;

import lombok.Data;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/4/2 19:02
 */
@Data
public class MessageCode {
	private String returnstatus;
	private String code;
	private TaskID[] taskID;
	private String remark;

}

@Data
class TaskID {
	String tel;
}
