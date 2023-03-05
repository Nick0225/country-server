package com.echo.country.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/25 19:47
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
	@Bean
	public Docket createAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				//指定哪个包下面生成接口文档
				.apis(RequestHandlerSelectors.basePackage("com.echo.country"))
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(securitySchemes()); // 配置请求头信息
	}

	/**
	 * 文档基本信息
	 *
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.version("2.0")
				.title("数字乡村接口文档")
				.description("数字乡村接口文档")
				.contact(new Contact("Echo-9527", "localhost:8081/doc.html", "825457022@qq.com"
				))
				.build();

	}

	// 1. 解决访问接口登录问题
	private List<SecurityScheme> securitySchemes() {
		// 设置请求头信息
		List<SecurityScheme> result = new ArrayList<>();
		// 参数：api key 名字 { 准备的 key 名字，value 请求头 }
		result.add(new ApiKey("Authorization", "Authorization", "header"));
		return result;
	}


}
