package com.echo.country.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.echo.country.pojo.Contact;
import com.echo.country.pojo.ContactExcel;
import com.echo.country.service.ContactService;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/22 22:05
 */
public class ContactExcelListener extends AnalysisEventListener<ContactExcel> {

	private ContactService contactService;
	private Integer sum;
	private Integer successCount;
	private Integer failedCount;
	/**
	 * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
	 */
	private static final int BATCH_COUNT = 1;
	List<Contact> list = new ArrayList<>();


	public ContactExcelListener(ContactService contactService) {
		sum = 0;
		successCount = 0;
		failedCount = 0;
		this.contactService = contactService;
	}

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}

	@Override
	public void invoke(ContactExcel contactExcel, AnalysisContext analysisContext) {

		if (contactExcel == null) {
			return;
		}

		sum++;

		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();
		contactQueryWrapper.eq("tel", contactExcel.getTel());
		contactQueryWrapper.eq("person_id", contactExcel.getPersonId());
		Contact one = contactService.getOne(contactQueryWrapper);

		//判断该导入是更新还是导入
		if (one == null) {

			//导入操作
			Contact contact = new Contact();

			BeanUtils.copyProperties(contactExcel, contact);

			boolean isSave = contactService.save(contact);

			if (isSave) {
				successCount++;
			} else {
				failedCount++;
			}
		} else {

			//更新操作
			BeanUtils.copyProperties(contactExcel, one);
			boolean update = contactService.updateById(one);
			if (update) {
				successCount++;
			} else {
				failedCount++;
			}
		}

	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
	}
}
