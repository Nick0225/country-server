package com.echo.country.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.Welfare;
import com.echo.country.service.WelfareService;
import com.echo.country.vo.QueryWelfareVo;
import com.echo.country.vo.ResultInfo;
import com.echo.country.vo.WelfareVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/26 13:02
 */
@Transactional
@RestController
@RequestMapping("/welfare")
@Api(tags = "宣传功能接口")
public class WelfareController {
	@Autowired
	WelfareService welfareService;

	@ApiOperation("分页获取所有宣传")
	@GetMapping("/getWelfare/{currentPage}/{pageSize}")
	public ResultInfo getWelfare(@PathVariable
	                             @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                             @PathVariable
	                             @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize,
	                             QueryWelfareVo queryWelfareVo) {
		Page<Welfare> welfarePage = new Page<>(currentPage, pageSize);

		QueryWrapper<Welfare> queryWrapper = new QueryWrapper<>();
		if (queryWelfareVo != null) {
			if (queryWelfareVo.getTitle() != null && queryWelfareVo.getTitle().trim() != "") {
				queryWrapper.like("title", queryWelfareVo.getTitle());
			}
			if (queryWelfareVo.getInstitutions() != null && queryWelfareVo.getInstitutions().trim() != "") {
				queryWrapper.like("institutions", queryWelfareVo.getInstitutions());
			}

			if (queryWelfareVo.getDescb() != null && queryWelfareVo.getDescb().trim() != "") {
				queryWrapper.like("descb", queryWelfareVo.getDescb());
			}
		}

		queryWrapper.orderByDesc("create_time");

		Page<Welfare> page = welfareService.page(welfarePage, queryWrapper);

		return ResultInfo.success("获取分页成功", page);
	}

	@ApiOperation("添加宣传")
	@PostMapping("/addWelfare")
	@Secured("ROLE_ADMIN")
	public ResultInfo addWelfare(
			@ApiParam(name = "包含数据：title,img,descb,institutions", value = "接收通知实体类", required = true)
			@RequestBody WelfareVo welfareVo) {
		Welfare welfare = new Welfare();
		BeanUtils.copyProperties(welfareVo, welfare);
		if (welfare.getViews() == null) {
			welfare.setViews(0);
		}
		welfareService.save(welfare);
		return ResultInfo.success("添加成功", "");

	}

	@ApiOperation("查看指定通知且浏览量+1")
	@GetMapping("/getOneWelfare/{welfareId}")
	public ResultInfo getOneNotice(@ApiParam(name = "welfareId", value = "指定宣传编号") @PathVariable Long welfareId) throws Exception {
		Welfare welfare = welfareService.getById(welfareId);
		if (null == welfare) {
			throw new Exception("查无该通知信息");
		}
		welfare.setViews(welfare.getViews() + 1);

		welfareService.updateById(welfare);
		Welfare welfare1 = welfareService.getById(welfareId);
		Map<String, Object> map = new HashMap<>();
		map.put("welfare", welfare1);
		return ResultInfo.success("查看宣传成功", map);

	}

	@ApiOperation("删除指定宣传")
	@DeleteMapping("/delWelfare/{welfareId}")
	@Secured("ROLE_ADMIN")
	public ResultInfo delNotice(@ApiParam(name = "welfareId", value = "宣传编号", required = true)
	                            @PathVariable Long welfareId) {
		QueryWrapper<Welfare> welfareQueryWrapper = new QueryWrapper<>();
		welfareQueryWrapper.eq("id", welfareId);
		welfareService.remove(welfareQueryWrapper);
		return ResultInfo.success("移除成功", "");
	}

	@ApiOperation("修改指定宣传")
	@PutMapping("/updateWelfare/{welfareId}")
	@Secured("ROLE_ADMIN")
	public ResultInfo updateNotice(@ApiParam(name = "welfareId", value = "需要修改宣传的编号", required = true)
	                               @PathVariable Long welfareId,
	                               @RequestBody WelfareVo welfareVo) {
		Welfare welfare = new Welfare();
		BeanUtils.copyProperties(welfareVo, welfare);

		QueryWrapper<Welfare> welfareQueryWrapper = new QueryWrapper<>();
		welfareQueryWrapper.eq("id", welfareId);
		welfareService.update(welfare, welfareQueryWrapper);
		return ResultInfo.success("修改成功", "");

	}


}
