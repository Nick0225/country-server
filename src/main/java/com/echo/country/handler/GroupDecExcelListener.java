package com.echo.country.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.echo.country.pojo.GroupDesc;
import com.echo.country.service.GroupDescService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/26 18:40
 */
public class GroupDecExcelListener extends AnalysisEventListener<GroupDesc> {

	private GroupDescService groupDescService;

	public GroupDecExcelListener(GroupDescService groupDescService) {
		this.groupDescService = groupDescService;
	}

	/**
	 * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
	 */
	private static final int BATCH_COUNT = 1;
	List<GroupDesc> list = new ArrayList<>();

	@Override
	public void invoke(GroupDesc groupDesc, AnalysisContext analysisContext) {
		list.add(groupDesc);
		if (list.size() >= BATCH_COUNT) {
			saveData();
			list.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {

	}

	/**
	 * 加上存储数据库
	 */
	private void saveData() {
		if (!CollectionUtils.isEmpty(list)) {
			groupDescService.saveBatch(list);
		}
	}
}
