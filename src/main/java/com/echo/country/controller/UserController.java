package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.*;
import com.echo.country.service.*;
import com.echo.country.service.impl.NoContactServiceImpl;
import com.echo.country.service.impl.UserServiceImpl;
import com.echo.country.utils.JwtUtil;
import com.echo.country.utils.MD5Util;
import com.echo.country.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/25 20:09
 */
@Api(tags = "用户功能接口")
@RestController
@Transactional
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private IntegralService integralService;

	@Autowired
	ShopService shopService;

	@Autowired
	RoleService rolesService;

	@Autowired
	JwtUtil jwtUtil;

	@Value("${jwt.tokenHeader}")
	private String tokenHeader;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	NoContactServiceImpl noContactService;


	private String UPLOAD_LOCATION = "upload/head";


	@ApiOperation(value = "添加用户信息")
	@PostMapping("/addUserInfo")
	public ResultInfo addUserInfo(@RequestBody LoginUser loginUser) throws Exception {
		//验证消息
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		String code = (String) ops.get(loginUser.getUsername());

		if (!loginUser.getMsg().equals(code)) {
			return ResultInfo.failure("验证码错误", 500);
		}

		//查看该用户是否属于联系人中的数据
		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();
		contactQueryWrapper.eq("tel", loginUser.getUsername());
		Contact contact = contactService.getOne(contactQueryWrapper);
		User user = new User();

		if (null == contact) {

			QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.eq("username", loginUser.getUsername());
			User userOne = userService.getOne(userQueryWrapper);

			QueryWrapper<NoContact> noContactQueryWrapper = new QueryWrapper<>();
			noContactQueryWrapper.eq("tel", loginUser.getUsername());
			NoContact one = noContactService.getOne(noContactQueryWrapper);

			if (null != userOne && one == null) {

				return ResultInfo.failure("请填写具体信息", 500);

			}


			if (null != userOne) {

				return ResultInfo.failure("该用户已注册,请等待审核", 500);

			}

			BeanUtils.copyProperties(loginUser, user);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userService.save(user);
			return ResultInfo.failure("注册失败,不属于信息库中数据,请联系管理员添加", 500);
		}

		QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("username", loginUser.getUsername());
		User userOne = userService.getOne(userQueryWrapper);

		if (null != userOne) {

			return ResultInfo.failure("该用户已经注册", 500);

		}

		BeanUtils.copyProperties(loginUser, user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.save(user);

		UserInfo userInfo = new UserInfo();

		BeanUtils.copyProperties(contact, userInfo);

		userInfo.setId(null);

		userInfo.setIsMerchant(0);

		userInfoService.save(userInfo);

		Integral integral = new Integral();
		integral.setSize(contact.getIntegralSize());
		integral.setUserId(Long.parseLong(userInfo.getId()));
		integralService.save(integral);

		return ResultInfo.success("注册成功", "");
	}

	@ApiOperation("获取短信验证码")
	@GetMapping("/getViliCode/{phoneNumber}")
	public void getViliCode(@ApiParam(name = "phoneNumber", value = "手机号", required = true)
	                        @PathVariable String phoneNumber) {

		String MD5PASS = MD5Util.MD5Upper("18679720725");

		String code = radomCode();

		String param = "action=sendtemplate&username=18679720725&password=" + MD5PASS + "&token=8ff6f7ef"
				+ "&timestamp=" + System.currentTimeMillis();

		String sign = MD5Util.MD5Upper(param);

		String url = "http://www.lokapi.cn/smsUTF8.aspx";

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

		body.add("action", "sendtemplate");
		body.add("username", "18679720725");
		body.add("password", MD5PASS);
		body.add("templateid", "16C2835B");
		body.add("token", "8ff6f7ef");
		body.add("param", phoneNumber + "|" + code);
		body.add("timestamp", String.valueOf(System.currentTimeMillis()));
		body.add("sign", sign);

		HttpEntity http = new HttpEntity(body, new HttpHeaders());

		ValueOperations<String, Object> ops = redisTemplate.opsForValue();

		ops.set(phoneNumber, code, 60 * 10, TimeUnit.SECONDS);

		try {
			restTemplate.exchange(url, HttpMethod.POST, http, MessageCode.class);
		} catch (RestClientException e) {

		}

	}


	@ApiOperation(value = "删除用户")
	@DeleteMapping("/delUserInfo/{tel}")
	@Secured("ROLE_ADMIN")
	public ResultInfo delUserInfo(@ApiParam(name = "tel", value = "手机号", required = true) @PathVariable String tel) {

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
		userInfoQueryWrapper.eq("tel", tel);
		userInfoService.remove(userInfoQueryWrapper);

		QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("username", tel);
		userService.remove(userQueryWrapper);

		return ResultInfo.success("删除成功", "");

	}

	@ApiOperation("修改密码(后台)")
	@PutMapping("/updatePassword/{tel}")
	public ResultInfo updatePassword(@ApiParam(name = "tel", value = "用户名/手机号", required = true) @PathVariable String tel,
	                                 @ApiParam(name = "password", value = "密码", required = true) @RequestBody(required = true) String password) {


		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq("username", tel);


		User user = userService.getOne(wrapper);

		if (user == null) {
			return ResultInfo.failure("无该用户信息", 500);
		}

		user.setPassword(passwordEncoder.encode(password));

		userService.update(user, wrapper);

		return ResultInfo.success("修改密码成功", "");

	}

	@ApiOperation("修改密码(APP)")
	@PutMapping("/updatePasswordAPP/{tel}")
	public ResultInfo updatePasswordAPP(@ApiParam(name = "tel", value = "用户名/手机号", required = true) @PathVariable String tel,
	                                    @RequestBody UpdatePasswordApp user0) {

		System.out.println(user0);

		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		String code = (String) ops.get(tel);

		if (!code.equals(user0.getMsg())) {

			return ResultInfo.failure("验证码错误", 500);
		}

		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq("username", tel);

		User user = userService.getOne(wrapper);

		if (user == null) {
			return ResultInfo.failure("无该用户信息", 500);
		}

		user.setPassword(passwordEncoder.encode(user0.getPassword()));

		userService.update(user, wrapper);

		return ResultInfo.success("修改密码成功", "");

	}


	@ApiOperation(value = "上传用户头像")
	@PostMapping("/uploadHead")
	public ResultInfo uploadHead(HttpServletRequest request,
	                             @RequestBody MultipartFile file) throws Exception {

		try {
			if (file == null) {
				throw new Exception("请选择上传图片");
			}

			//创建打成jar后，用户上传的图片储存路径（未部署时，与src同级，打成jar包后，该文件与jar包同级）
			Path directory = Paths.get(UPLOAD_LOCATION);
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}

//这种写法是上传文件到resource/static/...,打包部署后目录层级会变化，因此会报错
//			String path = ResourceUtils.getURL("src/main/resources/static/head").getPath();

			String filename = file.getOriginalFilename();
			String substring = filename.substring(filename.lastIndexOf("."));
			String realFilename = System.currentTimeMillis() + substring;

			Files.copy(file.getInputStream(), directory.resolve(realFilename));

//			multipartFile.transferTo(new File(directory.toAbsolutePath().toString(),realFilename));

			String image = "http://" + "36.134.80.177" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//			String image = "http://" + "101.132.153.67" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//			String image="http://" + "localhost" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;

			return ResultInfo.success("上传成功", image);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("上传失败");
		}
	}

	@ApiOperation("修改用户信息")
	@PutMapping("/updateUserInfo/{tel}")
	public ResultInfo updateUserInfo(@ApiParam(name = "tel", value = "被修改人的联系方式", required = true) @PathVariable String tel,
	                                 @RequestBody UserInfo userInfo1, HttpServletRequest request) throws Exception {

		Path directory = Paths.get(UPLOAD_LOCATION);
		if (!Files.exists(directory)) {
			Files.createDirectories(directory);
		}

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

		userInfoQueryWrapper.eq("tel", tel);

		UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);

		if (null == userInfo) {
			throw new Exception("无该用户信息");
		}


		//判断是否修改头像，修改删除原有图片
		if (userInfo.getImage() != null && userInfo.getImage().trim() != "") {
			String image = userInfo.getImage();
			String substring1 = image.substring(image.lastIndexOf("/") + 1);
			File file = new File(directory.toAbsolutePath().toString(), substring1);
			file.delete();
		}


		//修改用户信息
		if (userInfo1.getName() != null) {

			BeanUtils.copyProperties(userInfo1, userInfo);


		} else {

			userInfo.setImage(userInfo1.getImage());
		}

		userInfoService.update(userInfo, userInfoQueryWrapper);


		//修改联系人信息
		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();

		contactQueryWrapper.eq("tel", tel);

		Contact contact = contactService.getOne(contactQueryWrapper);


		BeanUtils.copyProperties(userInfo, contact);


		contactService.update(contact, contactQueryWrapper);


		//修改积分信息
		if (userInfo1.getIntegralSize() != null) {
			Integral integral = new Integral();
			integral.setSize(userInfo1.getIntegralSize());
			QueryWrapper<Integral> integralQueryWrapper = new QueryWrapper<>();
			integralQueryWrapper.eq("user_id", userInfo.getId());
			integralService.update(integral, integralQueryWrapper);
		}
		return ResultInfo.success("更新成功", "");

	}

	@ApiOperation(value = "获取当前用户信息")
	@GetMapping("/getUserInfo")
	public ResultInfo getUserInfo() throws Exception {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

		userInfoQueryWrapper.eq("tel", user.getUsername());

		UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);


		HashMap<String, Object> map = new HashMap<>();

		if (userInfo.getIsMerchant() == 1) {
			QueryWrapper<Shop> shopQueryWrapper = new QueryWrapper<Shop>();
			shopQueryWrapper.eq("tel", userInfo.getTel());
			Shop one = shopService.getOne(shopQueryWrapper);
			ShopUser shopUser = new ShopUser();
			BeanUtils.copyProperties(one, shopUser);
			BeanUtils.copyProperties(userInfo, shopUser);
			map.put("currentUserInfo", shopUser);
			return ResultInfo.success("获得用户信息成功", map);
		}

		map.put("currentUserInfo", userInfo);

		return ResultInfo.success("获得用户信息成功", map);

	}


	@ApiOperation("获取所有用户信息（附带后台管理模糊查询）")
	@GetMapping("/getUserByQuery/{currentPage}/{pageSize}")
	public ResultInfo getUserInfo(@PathVariable @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                              @PathVariable @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize,
	                              QueryUserInfoVo queryUserInfoVo) {

		QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();

		wrapper.ne("role", "管理员");

		if (queryUserInfoVo != null) {
			if (queryUserInfoVo.getName() != null && !queryUserInfoVo.getName().trim().equals("")) {
				wrapper.like("name", queryUserInfoVo.getName());
			}
			if (queryUserInfoVo.getGroupName() != null && !queryUserInfoVo.getGroupName().trim().equals("")) {
				wrapper.like("group_name", queryUserInfoVo.getGroupName());
			}
			if (queryUserInfoVo.getVillageName() != null && !queryUserInfoVo.getVillageName().trim().equals("")) {
				wrapper.like("village_name", queryUserInfoVo.getVillageName());
			}
			if (queryUserInfoVo.getTownshipName() != null && !queryUserInfoVo.getTownshipName().trim().equals("")) {
				wrapper.like("township_name", queryUserInfoVo.getTownshipName());
			}
		}

		Page<UserInfo> userInfoPage = new Page<>(currentPage, pageSize);

		Page<UserInfo> page = userInfoService.page(userInfoPage, wrapper);

		if (page.getTotal() == 0) {
			return ResultInfo.success("未找到用户信息", "");
		}
		return ResultInfo.success("用户信息", page);

	}


//	@ApiOperation("获取指定用户信息")
//	@GetMapping("/getUserBytel/{tel}")
//	public ResultInfo getUserBytel(@PathVariable String tel) {
//
//		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
//
//		userInfoQueryWrapper.eq("tel", tel);
//
//
//		UserInfo one = userInfoService.getOne(userInfoQueryWrapper);
//
//
//		if (one == null) {
//			return ResultInfo.success("无用户信息", "");
//		}
//
//		return ResultInfo.success("获取用户信息成功", one);
//
//	}

	@ApiOperation("获取指定用户信息")
	@GetMapping("/getUserById/{id}")
	public ResultInfo getUserById(@PathVariable String id) {

		UserInfo userInfo = userInfoService.getById(id);

		if (null == userInfo) {
			return ResultInfo.failure("无该用户信息", 500);
		}

		return ResultInfo.success("成功", userInfo);

	}


	@ApiOperation("获取本乡商家信息")
	@GetMapping("/getShop")
	public ResultInfo getShop() {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = user.getUsername();

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

		userInfoQueryWrapper.eq("tel", username);

		UserInfo one = userInfoService.getOne(userInfoQueryWrapper);


		String townshipName = one.getTownshipName();


		userInfoQueryWrapper.clear();


		userInfoQueryWrapper.eq("township_name", townshipName);

		userInfoQueryWrapper.eq("is_merchant", 1);

		UserInfo one1 = userInfoService.getOne(userInfoQueryWrapper);

		if (one1 == null) {

			return ResultInfo.success("本乡暂无商家", "");

		}

		String tel = one1.getTel();


		QueryWrapper<Shop> shopQueryWrapper = new QueryWrapper<>();


		shopQueryWrapper.eq("tel", tel);

		Shop one2 = shopService.getOne(shopQueryWrapper);

		ShopUser shopUser = new ShopUser();


		BeanUtils.copyProperties(one1, shopUser);

		BeanUtils.copyProperties(one2, shopUser);

		return ResultInfo.success("获取本村店铺成功", shopUser);
	}


	@ApiOperation("判断用户Token是否过期")
	@GetMapping("/getTokenIsExprie")
	public ResultInfo getTokenIsExprie(HttpServletRequest request) {

		String authorization = request.getHeader("Authorization");

		if (authorization == null) {
			return ResultInfo.success("", "");
		}

		String authToken = authorization.substring(tokenHead.length());

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		boolean b = jwtUtil.validateToken(authToken, user);

		return b == false ? ResultInfo.failure("身份过期，请重新登录", 500) : ResultInfo.success("未过期", "");
	}


	@ApiOperation("取消用户商家")
	@PostMapping("/cancelShop/{tel}")
	public ResultInfo removeShopUser(@PathVariable String tel) {

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

		userInfoQueryWrapper.eq("tel", tel);

		UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
		if (userInfo.getIsMerchant() == 0) {
			return ResultInfo.success("该用户不是商家", "");
		}

		userInfo.setIsMerchant(0);

		QueryWrapper<Shop> shopQueryWrapper = new QueryWrapper<>();
		shopQueryWrapper.eq("tel", tel);
		shopService.remove(shopQueryWrapper);

		userInfoService.update(userInfo, userInfoQueryWrapper);

		return ResultInfo.success("该用户取消商家成功", "");


	}


	@ApiOperation("修改用户权限")
	@PutMapping("/updateRole/{tel}")
	public ResultInfo updateRole(@ApiParam(name = "userId", value = "用户编号", required = true) @PathVariable String tel,
	                             @ApiParam(name = "role", value = "需要修改的权限名", required = true) String role) {


		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
		userInfoQueryWrapper.eq("tel", tel);

		UserInfo user = userInfoService.getOne(userInfoQueryWrapper);

		if (user == null) {
			return ResultInfo.failure("无该用户信息", 500);
		}

		user.setRole(role);

		userInfoService.updateById(user);

		String userTel = user.getTel();

		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();

		contactQueryWrapper.eq("tel", userTel);

		Contact contact = contactService.getOne(contactQueryWrapper);

		contact.setRole(role);

		contactService.update(contact, contactQueryWrapper);

		return ResultInfo.success("成功", "");

	}


	@ApiOperation("获取权限列表")
	@GetMapping("/getRole")
	public ResultInfo getRole() {

		List<Role> roles = rolesService.list();

		roles.remove(0);
		roles.remove(roles.size() - 1);


		return ResultInfo.success("成功", roles);
	}


	@ApiOperation("客户端多条件模糊查询用户")
	@GetMapping("/getUserByName")
	public ResultInfo getUserByName(@ApiParam(name = "param", value = "查询条件", required = true)
	                                @RequestParam String param) {
		QueryWrapper<Contact> wrapper = new QueryWrapper<>();

		if (param.length() > 5) {
			wrapper.eq("tel", param);
			wrapper.or();
			wrapper.eq("tail", param);
		} else {
			wrapper.like("name", param);
		}
		List<Contact> list = contactService.list(wrapper);

		return ResultInfo.success("成功", list);

	}

	/**
	 * 生成六位验证码
	 *
	 * @return
	 */
	private String radomCode() {

		Random random = new Random();
		String code = "";
		for (int i = 0; i < 6; i++) {
			code += String.valueOf(random.nextInt(10));
		}

		return code;

	}

	@ApiOperation("后台管理登录")
	@PostMapping("/adminLogin")
	public ResultInfo adminLogin(@RequestBody LoginAdminUser adminUser) {

		QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("username", adminUser.getUsername());
		User one = userService.getOne(userQueryWrapper);
		if (null == one) {
			return ResultInfo.failure("无该管理员信息", 500);
		}

		if (!passwordEncoder.matches(adminUser.getPassword(), one.getPassword())) {
			return ResultInfo.failure("账号密码不匹配", 500);
		}

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
		userInfoQueryWrapper.eq("tel", adminUser.getUsername());
		UserInfo one1 = userInfoService.getOne(userInfoQueryWrapper);

		if (!"管理员".equals(one1.getRole())) {
			return ResultInfo.failure("请使用管理员账号登录", 500);
		}

		String token = jwtUtil.generateToken(one);

		Map<String, Object> map = new HashMap<>();

		map.put("tokenHeader", tokenHeader);

		map.put("token", tokenHead + " " + token);

		return ResultInfo.success("登录成功", map);
	}
}
