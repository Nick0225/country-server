package com.echo.country.config.security;

import com.echo.country.vo.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Echo-9527
 * @Describe 自定义权限不住处理器
 * @Version 1.0
 * @date 2022/2/14 4:19
 */
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	/**
	 * 具体逻辑
	 *
	 * @param request
	 * @param response
	 * @param accessDeniedException
	 * @throws IOException
	 * @throws ServletException
	 */

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.setContentType("application/json; charset=UTF-8");

		ResultInfo info = ResultInfo.failure("权限不足", HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);

		log.error("访问接口:" + request.getRequestURL() + "-ip:" + request.getRemoteAddr() + "-主机信息：" + request.getRemoteHost() + "-权限不住,禁止访问接口");

		response.getWriter().println(new ObjectMapper().writeValueAsString(info));

	}
}
