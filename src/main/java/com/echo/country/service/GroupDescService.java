package com.echo.country.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.echo.country.pojo.GroupDesc;

import java.util.List;

/**
 * @author Paradise
 * @description 针对表【group_desc(分组描述表)】的数据库操作Service
 * @createDate 2022-02-26 18:18:57
 */
public interface GroupDescService extends IService<GroupDesc> {

	List<GroupDesc> findDictById(Long id);
}
