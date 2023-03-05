package com.echo.country.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.UserInfoMapper;
import com.echo.country.pojo.UserInfo;
import com.echo.country.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author Paradise
 * @description 针对表【user_info(用户信息表)】的数据库操作Service实现
 * @createDate 2022-02-25 20:08:13
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
		implements UserInfoService {

}




