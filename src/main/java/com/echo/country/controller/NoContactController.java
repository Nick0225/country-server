package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.Contact;
import com.echo.country.pojo.Integral;
import com.echo.country.pojo.NoContact;
import com.echo.country.pojo.UserInfo;
import com.echo.country.service.impl.ContactServiceImpl;
import com.echo.country.service.impl.IntegralServiceImpl;
import com.echo.country.service.impl.NoContactServiceImpl;
import com.echo.country.service.impl.UserInfoServiceImpl;
import com.echo.country.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/4/10 15:16
 */
@Api(tags = "不在通讯录的人员接口")
@RestController
@RequestMapping("/noCon")
public class NoContactController {

	@Autowired
	NoContactServiceImpl noContactService;

	@Autowired
	ContactServiceImpl contactService;

	@Autowired
	UserInfoServiceImpl userInfoService;

	@Autowired
	IntegralServiceImpl integralService;

	@ApiOperation(value = "通讯录中无信息的注册功能", notes = "姓名、手机号、性别、民族、手机号、身份证号、县名称、乡名称、村名称、组名称")
	@PostMapping("/addUser")
	public ResultInfo addUser(@RequestBody NoContact noContact) {

		QueryWrapper<NoContact> contactQueryWrapper = new QueryWrapper<>();
		contactQueryWrapper.eq("tel", noContact.getTel());
		contactQueryWrapper.eq("name", noContact.getName());

		NoContact one = noContactService.getOne(contactQueryWrapper);
		if (one != null) {
			return ResultInfo.failure("该用户已提交注册，请等待审核", 500);
		}
		noContact.setRole("村民");
		noContact.setIntegralSize(0);
		noContact.setAddress("江西省赣州市会昌县" + noContact.getTownshipName() + noContact.getVillageName() + noContact.getGroupName());
		noContact.setIsAccess(0);
		noContactService.save(noContact);
		return ResultInfo.success("添加成功，请联系管理员进行审核", "");

	}

	@ApiOperation("通过审核")
	@GetMapping("/changeAcess/{id}")
	public ResultInfo changeAcess(@PathVariable String id) {

		NoContact noContact = noContactService.getById(id);

		noContact.setIsAccess(1);

		noContactService.updateById(noContact);

		noContact.setId(null);

		Contact contact = new Contact();

		BeanUtils.copyProperties(noContact, contact);

		contactService.save(contact);

		UserInfo userInfo = new UserInfo();

		BeanUtils.copyProperties(noContact, userInfo);

		userInfo.setIsMerchant(0);

		userInfoService.save(userInfo);

		Integral integral = new Integral();
		integral.setSize(userInfo.getIntegralSize());
		integral.setUserId(Long.parseLong(userInfo.getId()));
		integralService.save(integral);

		return ResultInfo.success("成功", "");
	}

	@ApiOperation("获取所有需要审核的人员")
	@GetMapping("/getNoconList/{curPage}/{pageSize}")
	public ResultInfo changeAcess(@PathVariable Long curPage,
	                              @PathVariable Long pageSize) {


		QueryWrapper<NoContact> noContactQueryWrapper = new QueryWrapper<>();
		noContactQueryWrapper.orderByDesc("create_time");

		Page<NoContact> noContactPage = new Page<>(curPage, pageSize);

		Page<NoContact> page = noContactService.page(noContactPage, noContactQueryWrapper);

		return ResultInfo.success("成功", page);
	}


}
