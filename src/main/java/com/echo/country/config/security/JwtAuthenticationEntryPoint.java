package com.echo.country.config.security;

import com.echo.country.vo.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Echo-9527
 * @Describe 无凭证处理类：
 * 当用户没有携带有效凭证时，就会转到这里来，
 * 当然，我们还需要在Spring Security的配置类中指定我们自定义的处理类才可以
 * @Version 1.0
 * @date 2022/2/14 4:00
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	/**
	 * 无登录凭证处理逻辑
	 *
	 * @param request
	 * @param response
	 * @param authException
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


		response.setContentType("application/json; charset=UTF-8");

		ResultInfo info = ResultInfo.failure("请先登录授权", HttpServletResponse.SC_UNAUTHORIZED);

		log.error("访问接口:" + request.getRequestURI() + "-ip:" + request.getRemoteAddr() + "-主机信息：" + request.getRemoteHost() + "-未授权,禁止访问接口");

		response.getWriter().println(new ObjectMapper().writeValueAsString(info));

	}
}
