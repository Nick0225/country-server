package com.echo.country.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.handler.IntegralExcelListener;
import com.echo.country.pojo.*;
import com.echo.country.service.*;
import com.echo.country.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/25 22:41
 */
@Api(tags = "积分管理接口")
@RestController
@Transactional
@RequestMapping("/integral")
public class IntegralController {

	@Autowired
	private IntegralService integralService;

	@Autowired
	private ShopService shopService;


	@Autowired
	private ContactService contactService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private IntegralOptionService integralOptionService;

	@ApiOperation("获取指定用户积分")
	@GetMapping("/getIntegralNum/{userId}")
	public ResultInfo getIntegralNum(@ApiParam(name = "userId", value = "用户编号", required = true)
	                                 @PathVariable Long userId) {
		Integral integral = getIntegral(userId);
		HashMap<String, Object> map = new HashMap<>();
		map.put("integralNum", integral);
		return ResultInfo.success("获取当前积分成功", map);
	}


	@ApiOperation(value = "修改指定用户积分大小", notes = "管理员请求接口")
	@PutMapping("/updateIntegral/{userId}")
	@Secured("ROLE_ADMIN")
	public ResultInfo updateIntegral(@PathVariable Long userId,
	                                 @RequestBody Integer integralNum) throws Exception {

		Integral integral = getIntegral(userId);

		if (integralNum < 0) {
			throw new Exception("设置失败,用户积分至少为零");
		}
		integral.setSize(integralNum);
		QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();
		integralQueryWrapper.eq("user_id", userId);
		integralService.update(integral, integralQueryWrapper);

		QueryWrapper<UserInfo> userInfoQueryWrapper1 = new QueryWrapper<>();
		userInfoQueryWrapper1.eq("id", userId);
		UserInfo userInfo2 = new UserInfo();
		userInfo2.setIntegralSize(integralNum);
		userInfoService.update(userInfo2, userInfoQueryWrapper1);

		//更新联系人表积分大小
		UserInfo info = userInfoService.getOne(userInfoQueryWrapper1);
		QueryWrapper<Contact> contactQueryWrapper1 = new QueryWrapper<>();
		contactQueryWrapper1.eq("tel", info.getTel());
		Contact contact1 = new Contact();
		contact1.setIntegralSize(integralNum);
		contactService.update(contact1, contactQueryWrapper1);


		return ResultInfo.success("设置积分大小成功", null);
	}

	@ApiOperation(value = "积分核销操作")
	@PutMapping("/optionsIntegral/{userId}")
	public ResultInfo updateIntegral(@ApiParam(name = "userId", value = "被操作积分的用户Id", required = true)
	                                 @PathVariable Long userId,
	                                 @RequestBody(required = true) IntegralOptionVo integralOptionVo) throws Exception {

		Integral integral = getIntegral(userId);

		if (null == integral) {

			throw new Exception("该用户暂未开通积分操作");

		}
		Integer size = integral.getSize();

		if (integralOptionVo.getOptions() == 1) {

			//更新积分
			integral.setSize(size + integralOptionVo.getNum());
			QueryWrapper<Integral> integralQueryWrapper1 = new QueryWrapper<>();
			integralQueryWrapper1.eq("user_id", userId);
			integralService.update(integral, integralQueryWrapper1);

			//更新用户积分
			QueryWrapper<UserInfo> userInfoQueryWrapper1 = new QueryWrapper<>();
			userInfoQueryWrapper1.eq("id", userId);
			UserInfo userInfo2 = new UserInfo();
			userInfo2.setIntegralSize(integral.getSize());
			userInfoService.update(userInfo2, userInfoQueryWrapper1);

			//更新联系人表积分大小
			UserInfo info = userInfoService.getOne(userInfoQueryWrapper1);
			QueryWrapper<Contact> contactQueryWrapper1 = new QueryWrapper<>();
			contactQueryWrapper1.eq("tel", info.getTel());
			Contact contact1 = new Contact();
			contact1.setIntegralSize(integral.getSize());
			contactService.update(contact1, contactQueryWrapper1);

			//添加积分操作
			IntegralOption integralOption = new IntegralOption();
			integralOption.setOptions(integralOptionVo.getOptions());
			integralOption.setDescb(integralOptionVo.getDescb());
			integralOption.setName(integralOptionVo.getName());
			integralOption.setNum(integralOptionVo.getNum());
			integralOption.setIntegralId(integral.getId());
			integralOptionService.save(integralOption);

			return ResultInfo.success("操作成功", "");

		}

		if (size < integralOptionVo.getNum()) {
			return ResultInfo.failure("更新失败,积分不足", 500);
		}
		integral.setSize(size - integralOptionVo.getNum());
		QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();
		integralQueryWrapper.eq("user_id", userId);
		integralService.update(integral, integralQueryWrapper);

		//更新用户积分
		QueryWrapper<UserInfo> userInfoQueryWrapper1 = new QueryWrapper<>();
		userInfoQueryWrapper1.eq("id", userId);
		UserInfo userInfo2 = new UserInfo();
		userInfo2.setIntegralSize(integral.getSize());
		userInfoService.update(userInfo2, userInfoQueryWrapper1);

		//更新联系人表积分大小
		UserInfo info = userInfoService.getOne(userInfoQueryWrapper1);
		QueryWrapper<Contact> contactQueryWrapper1 = new QueryWrapper<>();
		contactQueryWrapper1.eq("tel", info.getTel());
		Contact contact1 = new Contact();
		contact1.setIntegralSize(integral.getSize());
		contactService.update(contact1, contactQueryWrapper1);

		IntegralOption integralOption = new IntegralOption();
		integralOption.setOptions(integralOptionVo.getOptions());
		integralOption.setDescb(integralOptionVo.getDescb());
		integralOption.setName(integralOptionVo.getName());
		integralOption.setNum(integralOptionVo.getNum());
		integralOption.setIntegralId(integral.getId());
		integralOptionService.save(integralOption);
		return ResultInfo.success("操作成功", null);
	}

	@ApiOperation("分页获取指定账号的所有核销操作（按日期排序）")
	@GetMapping("/getIntegralOfOptions/{currentPage}/{pageSize}")
	public ResultInfo getIntegralOfoptions(
			@ApiParam(name = "tel", value = "用户联系方式", required = false) String tel,
			@PathVariable
			@ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
			@PathVariable
			@ApiParam(name = "pageSize", value = "页条数", required = true) Integer pageSize) {
		Page<IntegralOption> integralOptionPage = new Page<>(currentPage, pageSize);
		if (tel != null && tel.trim() != "") {
			QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
			userInfoQueryWrapper.eq("tel", tel);
			UserInfo user = userInfoService.getOne(userInfoQueryWrapper);
			if (user == null) {
				return ResultInfo.success("无该用户信息", "");
			}
			QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();

			integralQueryWrapper.eq("user_id", user.getId());

			Integral integral = integralService.getOne(integralQueryWrapper);

			QueryWrapper<IntegralOption> integralOptionQueryWrapper = new QueryWrapper<>();

			integralOptionQueryWrapper.eq("integral_id", integral.getId());

			integralOptionQueryWrapper.orderByDesc("create_time");

			Page<IntegralOption> page = integralOptionService.page(integralOptionPage, integralOptionQueryWrapper);

			if (page.getTotal() == 0) {
				return ResultInfo.success("暂无核销记录", page);
			}
			return ResultInfo.success("获取分页成功", page);

		}
		QueryWrapper<IntegralOption> integralOptionQueryWrapper = new QueryWrapper<>();

		integralOptionQueryWrapper.orderByDesc("create_time");

		Page<IntegralOption> page = integralOptionService.page(integralOptionPage, integralOptionQueryWrapper);

		if (page.getTotal() == 0) {
			return ResultInfo.success("暂无核销记录", page);
		}
		return ResultInfo.success("获取分页成功", page);
	}

	@ApiOperation("查看所有核销记录")
	@GetMapping("/getIntegralOptions")
	public ResultInfo getIntegralOptions() {

		List<IntegralOption> list = integralOptionService.list();
		List<IntegralOprationR> optionsList = new ArrayList<>();

		for (IntegralOption integralOption : list) {
			IntegralOprationR integralOprationR = new IntegralOprationR();

			BeanUtils.copyProperties(integralOption, integralOprationR);

			QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();

			integralQueryWrapper.eq("id", integralOption.getIntegralId());

			Integral integral = integralService.getOne(integralQueryWrapper);

			QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

			userInfoQueryWrapper.eq("id", integral.getUserId());

			UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);

			integralOprationR.setIntegralSize(userInfo.getIntegralSize());
			integralOprationR.setUserName(userInfo.getName());

			optionsList.add(integralOprationR);

			userInfoQueryWrapper.clear();
			integralQueryWrapper.clear();
		}

		return ResultInfo.success("成功", optionsList);

	}


	@ApiOperation("获取指定商家核销记录")
	@GetMapping("/getOpration/{tel}")
	public ResultInfo getOpration(@PathVariable String tel) {

		QueryWrapper<Shop> wrapper = new QueryWrapper<>();

		wrapper.eq("tel", tel);

		Shop shop = shopService.getOne(wrapper);

		if (null == shop) {
			return ResultInfo.failure("无该店铺信息", 500);
		}

		String shopName = shop.getShopName();

		QueryWrapper<IntegralOption> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("name", shopName);
		queryWrapper.orderByDesc("create_time");

		List<IntegralOption> list = integralOptionService.list(queryWrapper);

		if (list != null && list.size() != 0) {
			QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();

			ArrayList<IntegralOprationVo> integralOptionVos = new ArrayList<>();

			for (IntegralOption integralOption : list) {

				Long id = integralOption.getIntegralId();

				integralQueryWrapper.eq("id", id);

				Integral integral = integralService.getOne(integralQueryWrapper);

				Long userId = integral.getUserId();

				UserInfo userInfo = userInfoService.getById(userId);

				IntegralOprationVo integralOprationVo = new IntegralOprationVo();
				integralOprationVo.setIntegralOption(integralOption);
				integralOprationVo.setUserInfo(userInfo);

				integralOptionVos.add(integralOprationVo);
				integralQueryWrapper.clear();

			}
			return ResultInfo.success("成功", integralOptionVos);

		}


		return ResultInfo.success("该店铺暂无核销记录", "");

	}

	@ApiOperation("获取商家核销总积分")
	@GetMapping("/getShopNum/{tel}")
	public ResultInfo getsShopNum(@ApiParam(name = "tel", value = "店家联系方式", required = true)
	                              @PathVariable String tel) {

		QueryWrapper<Shop> shopWrapper = new QueryWrapper<>();
		shopWrapper.eq("tel", tel);
		Shop shop = shopService.getOne(shopWrapper);

		if (shop == null) {
			return ResultInfo.success("无该店铺信息", "");
		}
		String shopName = shop.getShopName();

		QueryWrapper<IntegralOption> integralOptionQueryWrapper = new QueryWrapper<>();

		integralOptionQueryWrapper.eq("name", shopName);

		List<IntegralOption> integralOptions = integralOptionService.list(integralOptionQueryWrapper);
		int sum = 0;
		for (IntegralOption integralOption : integralOptions) {

			sum += integralOption.getNum();
		}
		return ResultInfo.success("获取商家核销总积分成功", sum);
	}


	/**
	 * 导入积分
	 *
	 * @param file
	 * @return
	 */
	@ApiOperation("导入积分")
	@PostMapping("/insertIntegralByExcel")
	public ResultInfo insertIntegralExcel(@ApiParam(name = "file") @RequestBody MultipartFile file) throws IOException {

		IntegralExcelListener integralExcelListener = new IntegralExcelListener(integralService, userInfoService, contactService, integralOptionService);

		EasyExcel.read(file.getInputStream(), IntegralExcel.class, integralExcelListener).sheet().doRead();

		HashMap<String, Object> map = new HashMap<>();

		int count = integralExcelListener.getCount();

		int errorCount = integralExcelListener.getErrorCount();

		int successCount = integralExcelListener.getSuccessCount();

		map.put("count", count);
		map.put("errorCount", errorCount);
		map.put("successCount", successCount);

		return ResultInfo.success("成功", map);
	}


	/**
	 * 获取当前用户积分
	 *
	 * @param integerId
	 * @return
	 */
	private Integral getIntegral(Long integerId) {
		QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();
		integralQueryWrapper.eq("user_id", integerId);
		return integralService.getOne(integralQueryWrapper);
	}


}
