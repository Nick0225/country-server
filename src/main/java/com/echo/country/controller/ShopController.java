package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.IntegralOption;
import com.echo.country.pojo.Shop;
import com.echo.country.pojo.UserInfo;
import com.echo.country.service.IntegralOptionService;
import com.echo.country.service.ShopService;
import com.echo.country.service.UserInfoService;
import com.echo.country.vo.ResultInfo;
import com.echo.country.vo.ShopUser;
import com.echo.country.vo.ShopVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/8 19:02
 */
@Api(tags = "商家接口")
@RestController
@Transactional
public class ShopController {

	@Autowired
	ShopService shopService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	IntegralOptionService integralOptionService;


	@ApiOperation("添加商家")
	@PostMapping("/addShop/{tel}")
	public ResultInfo addShop(@PathVariable String tel, @RequestBody ShopVo shopVo) {

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

		userInfoQueryWrapper.eq("tel", tel);

		UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);

		String townshipName = userInfo.getTownshipName();

		QueryWrapper<UserInfo> userInfoQueryWrapper1 = new QueryWrapper<>();

		userInfoQueryWrapper1.eq("township_name", townshipName);

		List<UserInfo> list = userInfoService.list(userInfoQueryWrapper1);

		for (UserInfo info : list) {
			if (info.getIsMerchant() == 1) {
				return ResultInfo.failure("添加失败,该乡已存在商家店铺", 500);
			}
		}
		userInfo.setIsMerchant(1);

		userInfoService.update(userInfo, userInfoQueryWrapper);

		Shop shop = new Shop();
		BeanUtils.copyProperties(shopVo, shop);
		shop.setTel(tel);
		shopService.save(shop);
		return ResultInfo.success("添加成功", "");

	}


	@ApiOperation("获取当前商家（用户）核销的记录")
	@GetMapping("/getAllShopOptionRecodes/{currentPage}/{pageSize}")
	public ResultInfo resultInfo(@ApiParam(name = "tel", value = "商家联系方式", required = false) String tel,
	                             @PathVariable
	                             @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                             @PathVariable
	                             @ApiParam(name = "pageSize", value = "页条数", required = true) Integer pageSize) throws Exception {

		QueryWrapper<Shop> shopQueryWrapper = new QueryWrapper<>();
		shopQueryWrapper.eq("tel", tel);

		Shop one = shopService.getOne(shopQueryWrapper);
		if (one == null) {
			throw new Exception("无该商家记录");
		}
		QueryWrapper<IntegralOption> integralOptionQueryWrapper = new QueryWrapper<>();
		integralOptionQueryWrapper.eq("name", one.getShopName());

		Page<IntegralOption> objectPage = new Page<>(currentPage, pageSize);

		Page<IntegralOption> page = integralOptionService.page(objectPage, integralOptionQueryWrapper);

		return ResultInfo.success("获取成功", page);

	}

	@ApiOperation("获取商家列表")
	@GetMapping("/getAllshop")
	public ResultInfo getAllShop() {

		List<Shop> list = shopService.list();

		ArrayList<ShopUser> shopUsers = new ArrayList<>();

		for (Shop shop : list) {

			QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

			userInfoQueryWrapper.eq("tel", shop.getTel());

			UserInfo one = userInfoService.getOne(userInfoQueryWrapper);

			ShopUser shopUser = new ShopUser();

			BeanUtils.copyProperties(shop, shopUser);

			BeanUtils.copyProperties(one, shopUser);

			shopUsers.add(shopUser);
		}
		return ResultInfo.success("获取商家列表成功", shopUsers);
	}


	@ApiOperation("修改商家信息")
	@PutMapping("/updateShop/tel")
	public ResultInfo updateShop(@PathVariable String tel, @RequestBody ShopVo shopVo) {

		Shop shop = new Shop();

		BeanUtils.copyProperties(shopVo, shop);

		QueryWrapper<Shop> shopQueryWrapper = new QueryWrapper<>();

		shopQueryWrapper.eq("tel", tel);

		shopService.update(shop, shopQueryWrapper);

		return ResultInfo.success("修改成功", "");


	}


	@ApiOperation("获取当前商店核销总积分")
	@GetMapping("/getOprationSum")
	public ResultInfo getOprationSum(@ApiParam(name = "shopName", value = "店铺名称", required = true) String shopName) {


		QueryWrapper<IntegralOption> integralOptionQueryWrapper = new QueryWrapper<>();

		integralOptionQueryWrapper.eq("name", shopName);

		QueryWrapper<Shop> shopQueryWrapper = new QueryWrapper<>();
		shopQueryWrapper.eq("shop_name", shopName);

		Shop one = shopService.getOne(shopQueryWrapper);

		if (null == one) {

			return ResultInfo.success("无该商家信息", "");
		}

		List<IntegralOption> list = integralOptionService.list(integralOptionQueryWrapper);

		Integer num = 0;

		for (IntegralOption integralOption : list) {
			if (integralOption != null) {
				num += integralOption.getNum();
			}
		}
		Map<String, Integer> map = new HashMap<>();
		map.put("integralNum", num);
		return ResultInfo.success("获取成功", map);
	}


}
