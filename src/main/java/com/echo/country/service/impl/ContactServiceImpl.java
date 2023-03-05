package com.echo.country.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.ContactMapper;
import com.echo.country.pojo.Contact;
import com.echo.country.service.ContactService;
import org.springframework.stereotype.Service;

/**
 * @author Paradise
 * @description 针对表【contact(通讯录)】的数据库操作Service实现
 * @createDate 2022-02-25 20:07:49
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
		implements ContactService {


}




