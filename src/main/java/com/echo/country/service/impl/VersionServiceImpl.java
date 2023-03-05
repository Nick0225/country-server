package com.echo.country.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.VersionMapper;
import com.echo.country.pojo.Version;
import com.echo.country.service.VersionService;
import org.springframework.stereotype.Service;

/**
 * @author Paradise
 * @description 针对表【version】的数据库操作Service实现
 * @createDate 2022-03-03 19:27:33
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version>
		implements VersionService {

}




