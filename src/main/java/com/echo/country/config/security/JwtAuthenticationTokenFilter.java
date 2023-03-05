package com.echo.country.config.security;


import com.echo.country.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Echo-9527
 * @Describe 自定义Token过滤器
 * @Version 1.0
 * @date 2022/2/14 4:44
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Value("${jwt.tokenHeader}")
	private String tokenHeader;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//		System.out.println("请求路径:"+request.getRequestURI());
		//获取请求请求头中的token信息
		String authHeader = request.getHeader(tokenHeader);
		//存在token
		if (null != authHeader && authHeader.startsWith(tokenHead)) {
			String authToken = authHeader.substring(tokenHead.length());
			String userName = jwtUtil.getUserNameFromToken(authToken);
			//token存在但是未登录
			if (null != userName && null == SecurityContextHolder.getContext().getAuthentication()) {
				//登录
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
				if (null != userDetails && userDetails.isEnabled()) {
					if (jwtUtil.validateToken(authToken, userDetails)) {//如username不为空，并且能够在数据库中查到
						/**
						 * UsernamePasswordAuthenticationToken继承AbstractAuthenticationToken实现Authentication
						 * 所以当在页面中输入用户名和密码之后首先会进入到UsernamePasswordAuthenticationToken验证(Authentication)，
						 * 然后生成的Authentication会被交由AuthenticationManager来进行管理
						 * 而AuthenticationManager管理一系列的AuthenticationProvider，
						 * 而每一个Provider都会通UserDetailsService和UserDetail来返回一个
						 * 以UsernamePasswordAuthenticationToken实现的带用户名和密码以及权限的Authentication
						 */
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}
				}
			}
		}
		filterChain.doFilter(request, response);

	}
}
