package com.echo.country;

import com.echo.country.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootTest
class ApplicationTests {


	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	JwtUtil jwtUtil;


	void contextLoads() throws InterruptedException {


		/**
		 &param=手机号1|参数1|参数2@手机号2|参数1|参数2
		 &rece=json&timestamp=636949832321055780
		 &sign=96E79218965EB72C92A54
		 */

	}


	private String radomCode() {

		Random random = new Random();
		String code = "";
		for (int i = 0; i < 6; i++) {
			code += String.valueOf(random.nextInt(10));
		}

		return code;

	}

	@Test
	public void testSign() {
		String HeadeToken = "Country eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzM1NjgwMzcyMSIsImNyZWF0ZWQiOjE2NTExNTAyMTc0ODksImV4cCI6MTY1MTc1NTAxN30.9YwlgDHiNt0oTA7TzMZrZu74DBY4BUGg9YOyZDmY5VR-7ef02f2PQj6FsZznOeBGPeA8_xANBUNa4P28wDk7uQ";
		String substring = HeadeToken.substring(7);
		System.out.println(substring.startsWith(" "));
	}


}
