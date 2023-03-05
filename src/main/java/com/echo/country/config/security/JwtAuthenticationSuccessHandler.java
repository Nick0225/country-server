package com.echo.country.config.security;


import com.echo.country.pojo.User;
import com.echo.country.utils.JwtUtil;
import com.echo.country.vo.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Echo-9527
 * @Describe 自定义认证成功处理器
 * @Version 1.0
 * @date 2022/2/14 4:22
 */
@Slf4j
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Value("${jwt.tokenHeader}")
	String tokenHeader;

	@Value("${jwt.tokenHead}")
	String tokenHead;

	@Autowired
	JwtUtil jwtUtil;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		response.setContentType("application/json; charset=UTF-8");

//		User user = (User) userDetailsService.loadUserByUsername(authentication.getName());

		User user = (User) authentication.getPrincipal();

		String token = jwtUtil.generateToken(user);

		Map<String, Object> map = new HashMap<>();

		map.put("tokenHeader", tokenHeader);

		map.put("token", tokenHead + " " + token);

		//将生成的authentication放入容器中，生成安全的上下文
		SecurityContextHolder.getContext().setAuthentication(authentication);


		ResultInfo info = ResultInfo.success("登录成功", map);

		response.getWriter().println(new ObjectMapper().writeValueAsString(info));
	}
}
