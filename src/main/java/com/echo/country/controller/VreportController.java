package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.echo.country.pojo.Vreport;
import com.echo.country.service.impl.GroupDescServiceImpl;
import com.echo.country.service.impl.VReportServiceImpl;
import com.echo.country.vo.ResultInfo;
import com.echo.country.vo.VreportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/4/23 14:53
 */
@RestController
@Transactional
@RequestMapping("/vreport")
@Api(tags = "村民上报")
public class VreportController {

	@Autowired
	VReportServiceImpl vReportService;

	@Autowired
	GroupDescServiceImpl groupDescService;


	@ApiOperation("村民上报接口")
	@PostMapping("/uploadNews")
	public ResultInfo uploadNews(@RequestBody VreportVo vreportVo) {

		if (null == vreportVo) {
			return ResultInfo.failure("请填写完整上报信息", 500);
		}

		Vreport vReport = new Vreport();
		BeanUtils.copyProperties(vreportVo, vReport);

		vReportService.save(vReport);

		return ResultInfo.success("成功", "");

	}


	@ApiOperation("获取村民上报")
	@GetMapping("/getUploadNews")
	public ResultInfo getUploadNews(@ApiParam(name = "村名", value = "villageName", required = false) String villageName
	) {

		if (villageName == null) {

			QueryWrapper<Vreport> vreportQueryWrapper = new QueryWrapper<>();
			vreportQueryWrapper.orderByDesc("create_time");
			List<Vreport> list = vReportService.list(vreportQueryWrapper);
			return ResultInfo.success("成功", list);
		}

		QueryWrapper<Vreport> vReportQueryWrapper = new QueryWrapper<>();
		vReportQueryWrapper.eq("village_name", villageName);
		vReportQueryWrapper.orderByDesc("create_time");
		List<Vreport> list = vReportService.list(vReportQueryWrapper);
		return ResultInfo.success("成功", list);

	}


	@ApiOperation("按照姓名查询上报")
	@GetMapping("/getUploadByname/{name}")
	public ResultInfo getUploadByname(@PathVariable String name) {

		QueryWrapper<Vreport> vReportQueryWrapper = new QueryWrapper<>();
		vReportQueryWrapper.like("name", name);
		vReportQueryWrapper.orderByDesc("create_time");
		List<Vreport> list = vReportService.list(vReportQueryWrapper);
		return ResultInfo.success("成功", list);
	}


	@ApiOperation("查看具体上报")
	@GetMapping("/getReportNewById/{id}")
	public ResultInfo getReportNewById(@PathVariable String id) {

		Vreport vreport = vReportService.getById(id);

		if (null == vreport) {
			return ResultInfo.failure("暂无该上报相关信息", 500);
		}
		return ResultInfo.success("成功", vreport);
	}


}
