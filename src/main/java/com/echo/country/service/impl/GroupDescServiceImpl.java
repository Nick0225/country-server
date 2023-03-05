package com.echo.country.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.country.mapper.GroupDescMapper;
import com.echo.country.pojo.GroupDesc;
import com.echo.country.service.GroupDescService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Paradise
 * @description 针对表【group_desc(分组描述表)】的数据库操作Service实现
 * @createDate 2022-02-26 18:18:57
 */
@Service
public class GroupDescServiceImpl extends ServiceImpl<GroupDescMapper, GroupDesc>
		implements GroupDescService {

	/**
	 * @param id
	 * @return
	 */
	@Override
	public List<GroupDesc> findDictById(Long id) {
		QueryWrapper<GroupDesc> groupDescQueryWrapper = new QueryWrapper<>();
		groupDescQueryWrapper.eq("parent_id", id);
		List<GroupDesc> GroupDescs = baseMapper.selectList(groupDescQueryWrapper);
		for (GroupDesc groupDesc : GroupDescs) {
			Long id1 = groupDesc.getId();
			groupDesc.setHasChildren(this.isHasChildNodes(id1));
		}
		return GroupDescs;
	}

	/**
	 * 判断当前节点是否有数据
	 *
	 * @param id1
	 * @return
	 */
	private Boolean isHasChildNodes(Long id1) {
		QueryWrapper<GroupDesc> dictQueryWrapper = new QueryWrapper<>();
		dictQueryWrapper.eq("parent_id", id1);
		Long aLong = baseMapper.selectCount(dictQueryWrapper);
		return aLong > 0 ? true : false;
	}


}




