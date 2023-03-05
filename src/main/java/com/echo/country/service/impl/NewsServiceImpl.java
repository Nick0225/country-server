package com.echo.country.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.NewsMapper;
import com.echo.country.pojo.News;
import com.echo.country.service.NewsService;
import org.springframework.stereotype.Service;

/**
 * @author Paradise
 * @description 针对表【news(消息表)】的数据库操作Service实现
 * @createDate 2022-03-07 21:27:10
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
		implements NewsService {

}




