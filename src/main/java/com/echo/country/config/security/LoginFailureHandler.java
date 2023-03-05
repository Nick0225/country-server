package com.echo.country.config.security;

import com.echo.country.vo.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Echo-9527
 * @Describe 自定义认证失败类
 * 当用户输入错误的账号或者密码时，
 * 就会进入这个处理类，同样要在配置类中指明
 * @Version 1.0
 * @date 2022/2/14 4:13
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
	/**
	 * 认证失败处理逻辑
	 *
	 * @param request
	 * @param response
	 * @param exception
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

		response.setContentType("application/json; charset=UTF-8");

		ResultInfo info = new ResultInfo();

		if (exception.getMessage().startsWith("Bad")) {

			log.error("访问接口:" + request.getRequestURI() + "-ip:" + request.getRemoteAddr() + "-主机信息：" + request.getRemoteHost() + "-用户名和密码不匹配");

			info = ResultInfo.failure("用户名和密码不匹配", 500);
		} else {

			log.error("访问接口:" + request.getRequestURI() + "-ip:" + request.getRemoteAddr() + "-主机信息：" + request.getRemoteHost() + "-" + exception.getMessage());

			info = ResultInfo.failure(exception.getMessage(), 500);

		}
		response.getWriter().println(new ObjectMapper().writeValueAsString(info));


	}
}
