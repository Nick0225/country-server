package com.echo.country.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Echo-9527
 * @Describe mybatsiplus配置类
 * @Version 1.0
 * @date 2022/2/19 13:40
 */
@MapperScan("com.echo.country.mapper")
@Configuration
public class MybatisPlusConfig {
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		//分页插件
		PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
		mybatisPlusInterceptor.addInnerInterceptor(pageInterceptor);

		return mybatisPlusInterceptor;
	}
}
