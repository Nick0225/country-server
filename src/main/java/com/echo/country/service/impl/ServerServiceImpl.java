package com.echo.country.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.ServerMapper;
import com.echo.country.pojo.Server;
import com.echo.country.service.ServerService;
import org.springframework.stereotype.Service;

/**
 * @author Paradise
 * @description 针对表【server】的数据库操作Service实现
 * @createDate 2022-03-09 19:29:04
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server>
		implements ServerService {

}




