package com.echo.country.config.security;

import com.echo.country.vo.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/19 14:19
 */
@Slf4j
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setContentType("application/json; charset=UTF-8");

		log.error("访问接口:" + request.getRequestURI() + "-ip:" + request.getRemoteAddr() + "-主机信息：" + request.getRemoteHost() + "-退出成功");

		ResultInfo info = ResultInfo.failure("退出成功", 200);

		response.getWriter().println(new ObjectMapper().writeValueAsString(info));
	}
}
