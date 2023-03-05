package com.echo.country.config;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.echo.country.config.security.*;
import com.echo.country.pojo.Role;
import com.echo.country.pojo.User;
import com.echo.country.pojo.UserInfo;
import com.echo.country.service.RoleService;
import com.echo.country.service.UserInfoService;
import com.echo.country.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;


/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/19 14:06
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserService userService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private RoleService roleService;

	@Bean
	public UserDetailsService userDetails() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

				QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
				userQueryWrapper.eq("username", username);
				User user = userService.getOne(userQueryWrapper);
				if (null == user) {
					throw new UsernameNotFoundException("该用户不存在");
				}
				QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
				userInfoQueryWrapper.eq("tel", username);
				UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);

				QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
				roleQueryWrapper.eq("name_zh", userInfo.getRole());
				Role role = roleService.getOne(roleQueryWrapper);

				ArrayList<Role> roles = new ArrayList<>();
				roles.add(role);
				if (userInfo.getIsMerchant() == 1) {
					Role role1 = new Role();
					role1.setId(6L);
					role1.setName("ROLE_SHOP");
					role.setNameZh("商家");
					roles.add(role1);
				}

				user.setRoles(roles);
				return user;
			}
		};
	}


	/**
	 * 无凭证处理类：
	 *
	 * @return
	 */
	@Bean
	public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	/**
	 * 自定义权限不住处理器
	 *
	 * @return
	 */
	@Bean
	public AccessDeniedHandler jwtAccessDeniedHandler() {
		return new JwtAccessDeniedHandler();
	}

	/**
	 * 自定义认证成功处理器
	 *
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}

	/**
	 * 自定义认证失败处理器
	 *
	 * @return
	 */
	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		return new LoginFailureHandler();
	}

	/**
	 * 自定义的Jwt Token过滤器
	 *
	 * @return
	 * @throws Exception
	 */
	@Bean
	public OncePerRequestFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationTokenFilter();
	}

	/**
	 * 自定义的密码编码器
	 *
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 验证逻辑
	 *
	 * @return
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				UserDetailsService userDetails = userDetails();
				PasswordEncoder passwordEncoder = passwordEncoder();
				//获取输入的用户名
				String username = authentication.getName();
				//获取输入的明文
				String rawPassword = (String) authentication.getCredentials();

				//查询用户是否存在
				User user = (User) userDetails.loadUserByUsername(username);


				if (!user.isEnabled()) {
					throw new DisabledException("该账户已被禁用，请联系管理员");

				} else if (!user.isAccountNonLocked()) {
					throw new LockedException("该账号已被锁定");

				} else if (!user.isAccountNonExpired()) {
					throw new AccountExpiredException("该账号已过期，请联系管理员");

				} else if (!user.isCredentialsNonExpired()) {
					throw new CredentialsExpiredException("该账户的登录凭证已过期，请重新登录");
				}
				//验证密码
				if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
					throw new BadCredentialsException("输入密码错误!");
				}

				return new UsernamePasswordAuthenticationToken(user, rawPassword, user.getAuthorities());

			}

			@Override
			public boolean supports(Class<?> authentication) {
				//确保authentication能转成该类
				return authentication.equals(UsernamePasswordAuthenticationToken.class);
			}
		};
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.formLogin()
				// 自定义登录拦截URI
				.loginProcessingUrl("/login")
				//自定义认证成功处理器
				.successHandler(jwtAuthenticationSuccessHandler())
				// 自定义失败拦截器
				.failureHandler(loginFailureHandler())
				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(new LogoutSuccessHandler())//退出成功处理器
				.and()
				//token的验证方式不需要开启csrf的防护
				.csrf().disable()
				// 自定义认证失败类
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint())
				// 自定义权限不足处理类
				.accessDeniedHandler(jwtAccessDeniedHandler())
				.and()
				//设置无状态的连接,即不创建session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.mvcMatchers(
						"/user/updatePasswordAPP/**",
						"/user/adminLogin"
						, "/groupDec/getNode/**",
						"/noCon/addUser"
						, "/user/updatePassword/**",
						"/user/getTokenIsExprie",
						"/user/getViliCode/**",
						"/upload/**",
						"/websocket/**",
						"/login",
						"/logout",
						"/user/addUserInfo",
						"/css/**",
						"/js/**",
						"/swagger-ui.html",
						"/fonts/**",
						"favicon.ico",
						"/doc.html",                    // 放行 swagger 资源
						"/webjars/**",                  // 放行 swagger 资源
						"/swagger-resources/**",
						// 放行 swagger 资源
						"/v2/api-docs/**",              // 放行 swagger 资源
						"/ws/**").permitAll()

				//配置允许匿名访问的路径
				.anyRequest().authenticated();

		// 解决跨域问题（重要）  只有在前端请求接口时才发现需要这个
		http.cors().and().csrf().disable();

		//配置自己的jwt验证过滤器
		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

		// 禁用页面缓存
		http.headers().cacheControl();
	}
}

