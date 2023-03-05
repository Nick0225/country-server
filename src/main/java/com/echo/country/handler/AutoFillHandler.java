package com.echo.country.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/19 21:30
 */
@Component
public class AutoFillHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(date);
		this.setFieldValByName("createTime", format, metaObject);
		this.setFieldValByName("updateTime", format, metaObject);
		this.setFieldValByName("isdeleted", 0, metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(date);
		this.setFieldValByName("updateTime", format, metaObject);
	}
}
