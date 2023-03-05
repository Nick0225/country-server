package com.echo.country.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.echo.country.pojo.Contact;
import com.echo.country.pojo.Integral;
import com.echo.country.pojo.IntegralOption;
import com.echo.country.pojo.UserInfo;
import com.echo.country.service.ContactService;
import com.echo.country.service.IntegralOptionService;
import com.echo.country.service.IntegralService;
import com.echo.country.service.UserInfoService;
import com.echo.country.vo.IntegralExcel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/14 20:29
 */
public class IntegralExcelListener extends AnalysisEventListener<IntegralExcel> {

	@Autowired
	IntegralService integralService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	ContactService contactService;

	@Autowired
	IntegralOptionService integralOptionService;


	//总条数
	private int count;

	//失败条数
	private int errorCount;

	//成功条数
	private int successCount;

	public int getCount() {
		return count;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public int getSuccessCount() {
		return successCount;
	}


	public IntegralExcelListener(IntegralService integralService, UserInfoService userInfoService, ContactService contactService, IntegralOptionService integralOptionService) {
		this.count = 0;
		this.errorCount = 0;
		this.successCount = 0;
		this.integralService = integralService;
		this.userInfoService = userInfoService;
		this.contactService = contactService;
		this.integralOptionService = integralOptionService;
	}

	@Override
	public void invoke(IntegralExcel integralExcel, AnalysisContext analysisContext) {

		if (null == integralExcel) {
			return;
		}
		count++;
		String name = integralExcel.getName();
		String tel = integralExcel.getTel();
		Integer number = integralExcel.getNumber();

		//更新通讯鹿积分
		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();
		contactQueryWrapper.eq("tel", tel);
		contactQueryWrapper.eq("name", name);
		Contact one = contactService.getOne(contactQueryWrapper);
		if (one == null) {
			errorCount++;
			return;
		}
		one.setIntegralSize(one.getIntegralSize() + number);

		Integer integralSize = one.getIntegralSize();

		contactService.update(one, contactQueryWrapper);

		successCount++;


		//更新用户表
		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
		userInfoQueryWrapper.eq("tel", tel);
		userInfoQueryWrapper.eq("name", name);
		UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
		if (userInfo == null) {
			return;
		}
		userInfo.setIntegralSize(integralSize);

		userInfoService.update(userInfo, userInfoQueryWrapper);

		//更新积分表
		Long id = Long.parseLong(userInfo.getId());
		QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();
		integralQueryWrapper.eq("user_id", id);
		Integral integral = integralService.getOne(integralQueryWrapper);
		integral.setSize(integralSize);
		integralService.update(integral, integralQueryWrapper);

		//添加积分操作
		Long id1 = integral.getId();
		IntegralOption integralOption = new IntegralOption();
		integralOption.setOptions(1);
		integralOption.setDescb("积分补贴");
		integralOption.setName("乡政府");
		integralOption.setNum(number);
		integralOption.setIntegralId(id1);
		integralOptionService.save(integralOption);

	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {

	}
}
