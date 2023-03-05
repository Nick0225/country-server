package com.echo.country.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.echo.country.handler.GroupDecExcelListener;
import com.echo.country.pojo.GroupDesc;
import com.echo.country.service.GroupDescService;
import com.echo.country.utils.ExcelUtil;
import com.echo.country.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/26 18:20
 */
@RestController
@Transactional
@Api(tags = "村地图接口")
@RequestMapping("/groupDec")
@Slf4j
public class GroupDecController {

	@Autowired
	private GroupDescService groupDescService;

	@ApiOperation(value = "获取地图节点")
	@GetMapping("/getNode/{groupName}")
	public ResultInfo getNode(@ApiParam(name = "groupName", value = "查该groupName下的节点", required = true)
	                          @PathVariable String groupName) throws Exception {

		QueryWrapper<GroupDesc> groupDescQueryWrapper = new QueryWrapper<>();

		groupDescQueryWrapper.eq("name", groupName);

		GroupDesc groupDesc = groupDescService.getOne(groupDescQueryWrapper);


		if (null == groupDesc) {

			throw new Exception("没有该节点信息");

		}
		Long id = groupDesc.getId();

		List<GroupDesc> groupDescs = groupDescService.findDictById(id);

		if (groupDescs.size() == 0) {
			return ResultInfo.success("该节点无子节点", "");

		}

		return ResultInfo.success("查询节点成功", groupDescs);

	}


	@ApiOperation("导出字典")
	@GetMapping("/getGroupExcel")
	public ResultInfo getContactExcel(@ApiParam(name = "response", value = "响应", required = true)
			                                  HttpServletResponse response) throws Exception {

		String fileName = "dict";
		ExcelUtil.export2Web(response, fileName, fileName, GroupDesc.class, groupDescService.list());

		return ResultInfo.success("导出成功", null);

	}

	@ApiOperation("导入字典")
	@PostMapping("/insertGroupDecExcel")
	public ResultInfo insertContactExcel(
			@ApiParam(name = "file", value = "上传的excel", required = true)
			@RequestParam("file") MultipartFile file) throws IOException {
		EasyExcel.read(file.getInputStream(), GroupDesc.class, new GroupDecExcelListener(groupDescService)).sheet().doRead();
		return ResultInfo.success("上传成功", "");
	}

}
