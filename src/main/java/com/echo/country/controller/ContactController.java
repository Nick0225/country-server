package com.echo.country.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.handler.ContactExcelListener;
import com.echo.country.pojo.*;
import com.echo.country.service.ContactService;
import com.echo.country.service.GroupDescService;
import com.echo.country.service.UserInfoService;
import com.echo.country.utils.ExcelUtil;
import com.echo.country.vo.ContactVo;
import com.echo.country.vo.QueryUserInfoVo;
import com.echo.country.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/25 20:29
 */
@RestController
@Transactional
@Api(tags = "通讯录功能接口")
@RequestMapping("/contact")
@Slf4j
public class ContactController {

	@Autowired
	private ContactService contactService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private GroupDescService groupDescService;


//	@ApiOperation(value = "获取指定用户信息")
//	@GetMapping("/getUserInfo/{tel}")
//	public ResultInfo getUserInfo(@ApiParam(name = "手机号", value = "tel", required = true)
//	                              @PathVariable String tel) throws Exception {
//
//
//		QueryWrapper<Contact> contactWrapper = new QueryWrapper<>();
//		contactWrapper.eq("tel", tel);
//		Contact contact = contactService.getOne(contactWrapper);
//		if (null == contact) {
//
//			log.error("请求接口：/getUserInfo/" + tel + "无该用户信息");
//
//			throw new Exception("无该用户信息");
//
//		}
//
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("currentContact", contact);
//		return ResultInfo.success("获得用户信息成功", map);
//
//	}

	@ApiOperation("获取指定联系人信息")
	@GetMapping("/getUserInfo/{id}")
	public ResultInfo getUserInfo(@PathVariable String id) {

		Contact contact = contactService.getById(id);

		if (null == contact) {
			return ResultInfo.failure("无该联系人信息", 500);
		}
		return ResultInfo.success("成功", contact);
	}


	@ApiOperation("获取通讯录")
	@GetMapping("/getContact")
	public ResultInfo getContact() throws Exception {
		/*Map<String, Object> map = new HashMap<>();

		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		QueryWrapper<UserInfo> userQueryWrapper = new QueryWrapper<>();

		userQueryWrapper.eq("tel", user.getUsername());

		UserInfo userInfo = userInfoService.getOne(userQueryWrapper);

		String name = userInfo.getVillageName();

		String role = userInfo.getRole();

		QueryWrapper<GroupDesc> groupDescQueryWrapper = new QueryWrapper<>();
//		if (name != null && name.trim() != "") {
		if ("村民".equals(role) || "村长".equals(role) || "组长".equals(role)) {
			groupDescQueryWrapper.eq("name", name);
			GroupDesc groupDesc = groupDescService.getOne(groupDescQueryWrapper);
			List<ContactVo> contactVos = new ArrayList<>();
			Long parentId = groupDesc.getId();

			groupDescQueryWrapper.clear();

			groupDescQueryWrapper.eq("parent_id", parentId);
			List<GroupDesc> list = groupDescService.list(groupDescQueryWrapper);

			for (GroupDesc desc : list) {
				contactQueryWrapper.eq("group_name", desc.getName());
				contactQueryWrapper.orderByAsc("name");
				List<Contact> contacts = contactService.list(contactQueryWrapper);
				contactQueryWrapper.clear();

				ContactVo contactVo = new ContactVo();
				contactVo.setName(userInfo.getTownshipName() + "-" + userInfo.getVillageName() + "-" + desc.getName());
				contactVo.setContacts(contacts);
				contactVos.add(contactVo);

			}
			map.put("contact",userInfo.getCountyName()+userInfo.getTownshipName()+userInfo.getVillageName()+"通讯录");
			map.put("contents",contactVos);
			return ResultInfo.success("获取成功",map);
		}

		groupDescQueryWrapper.clear();
		String townshipName = userInfo.getTownshipName();
		groupDescQueryWrapper.eq("name", townshipName);
		GroupDesc one = groupDescService.getOne(groupDescQueryWrapper);
		Long id = one.getId();
		groupDescQueryWrapper.clear();
		groupDescQueryWrapper.eq("parent_id", id);
		List<GroupDesc> list1 = groupDescService.list(groupDescQueryWrapper);

		ArrayList<ContactV> contactVS = new ArrayList<>();

		for (GroupDesc groupDesc : list1) {

			ContactV contact = new ContactV();
			contact.setName(groupDesc.getName());

			ArrayList<ContactVo> contactVos = new ArrayList<>();

			Long parentId = groupDesc.getId();

			groupDescQueryWrapper.clear();

			groupDescQueryWrapper.eq("parent_id", parentId);

			List<GroupDesc> list = groupDescService.list(groupDescQueryWrapper);

			for (GroupDesc desc : list) {
				ContactVo vo1 = new ContactVo();
				contactQueryWrapper.eq("group_name", desc.getName());
				contactQueryWrapper.orderByAsc("name");
				List<Contact> contacts = contactService.list(contactQueryWrapper);
				contactQueryWrapper.clear();
				vo1.setContacts(contacts);
				vo1.setName(userInfo.getTownshipName() + "-" + groupDesc.getName() + "-" + desc.getName());
				contactVos.add(vo1);
			}
			contact.setContacts(contactVos);
			contactVS.add(contact);
		}
		map.put("contact",userInfo.getCountyName()+userInfo.getCountyName()+userInfo.getTownshipName()+"通讯录");
		map.put("contents",contactVS);*/
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();

		queryWrapper.eq("tel", user.getUsername());
		Map<String, Object> map = new HashMap<>();
		UserInfo userInfo = userInfoService.getOne(queryWrapper);
		String villageName = userInfo.getVillageName();
		ArrayList<ContactVo> contactVos = new ArrayList<>();

		String townshipName = userInfo.getTownshipName();

		QueryWrapper<GroupDesc> groupDescQueryWrapper = new QueryWrapper<>();
		groupDescQueryWrapper.eq("name", townshipName);
		GroupDesc one = groupDescService.getOne(groupDescQueryWrapper);

		Long id = one.getId();

		groupDescQueryWrapper.clear();


		groupDescQueryWrapper.eq("parent_id", id);

		List<GroupDesc> list = groupDescService.list(groupDescQueryWrapper);

		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();
		list.forEach(group -> {
			contactQueryWrapper.eq("village_name", group.getName());
			List<Contact> list1 = contactService.list(contactQueryWrapper);
			ContactVo contactVo = new ContactVo();
			contactVo.setName(group.getName());
			contactVo.setContacts(list1);
			contactVos.add(contactVo);
			contactQueryWrapper.clear();
		});
		map.put("contectName", userInfo.getCountyName() + userInfo.getTownshipName() + "通讯录");
		map.put("records", contactVos);
		return ResultInfo.success("获取成功", map);

	}


	@ApiOperation("获取通讯录所有信息")
	@GetMapping("/getContact/{currentPage}/{pageSize}")
	public ResultInfo getContact(@PathVariable @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                             @PathVariable @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize,
	                             QueryUserInfoVo queryUserInfoVo) {

		QueryWrapper<Contact> wrapper = new QueryWrapper<>();
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
		Page<Contact> contactPage = new Page<>(currentPage, pageSize);
		Page<Contact> page = contactService.page(contactPage, wrapper);
		if (page.getTotal() == 0) {
			return ResultInfo.success("未找到用户信息", "");
		}
		return ResultInfo.success("用户信息", page);
	}


	@ApiOperation("导出联系人")
	@GetMapping("/getContactExcel")
	public ResultInfo getContactExcel(@ApiParam(name = "response", value = "响应", required = true)
			                                  HttpServletResponse response) throws Exception {

		String fileName = "contact";

		ExcelUtil.export2Web(response, fileName, fileName, Contact.class, contactService.list());

		return ResultInfo.success("导出成功", "");

	}

	@ApiOperation("导入联系人")
	@PostMapping("/insertContactExcel")
	public ResultInfo insertContactExcel(
			@ApiParam(name = "file", value = "上传的excel", required = true)
			@RequestParam("file") MultipartFile file) throws IOException {

		ContactExcelListener contactExcelListener = new ContactExcelListener(contactService);

		EasyExcel.read(file.getInputStream(), ContactExcel.class, contactExcelListener).sheet().doRead();

		Map<String, Integer> map = new HashMap<>();

		map.put("count", contactExcelListener.getSum());
		map.put("successCount", contactExcelListener.getSuccessCount());
		map.put("failedCount", contactExcelListener.getFailedCount());

		return ResultInfo.success("上传成功", map);

	}

	@ApiOperation("删除指定通讯录人员")
	@DeleteMapping("/{id}")
	public ResultInfo resultInfo(@PathVariable Long id) {

		QueryWrapper<Contact> contactQueryWrapper = new QueryWrapper<>();
		contactQueryWrapper.eq("id", id);
		Contact one = contactService.getOne(contactQueryWrapper);

		if (one == null) {
			return ResultInfo.success("无该联系人信息", "");
		}
		String tel = one.getTel();

		QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

		userInfoQueryWrapper.eq("tel", tel);

		UserInfo one1 = userInfoService.getOne(userInfoQueryWrapper);
		if (one1 == null) {

			contactService.remove(contactQueryWrapper);

			return ResultInfo.success("删除成功", "");
		}
		return ResultInfo.success("该用户已注册,删除失败", "");

	}


	@ApiOperation("添加单个通讯录人员")
	@PostMapping("/addOneContact")
	public ResultInfo addOneContact(@RequestBody Contact contact) {

		contact.setAddress("江西省赣州市" + contact.getAddress());

		contactService.save(contact);

		return ResultInfo.success("添加成功", "");

	}


}
