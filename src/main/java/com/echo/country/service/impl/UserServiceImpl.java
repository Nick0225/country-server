package com.echo.country.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.UserMapper;
import com.echo.country.pojo.User;
import com.echo.country.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Paradise
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-02-25 20:08:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
		implements UserService {

}




