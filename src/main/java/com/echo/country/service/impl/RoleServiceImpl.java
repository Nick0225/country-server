package com.echo.country.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.RoleMapper;
import com.echo.country.pojo.Role;
import com.echo.country.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author Paradise
 * @description 针对表【role(权限表)】的数据库操作Service实现
 * @createDate 2022-02-25 21:49:06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
		implements RoleService {

}




