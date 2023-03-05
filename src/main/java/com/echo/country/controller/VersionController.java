package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.Version;
import com.echo.country.service.VersionService;
import com.echo.country.vo.ResultInfo;
import com.echo.country.vo.VersionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/3 19:31
 */
@RestController
@Transactional
@Api(tags = "版本接口")
@RequestMapping("/version")
public class VersionController {

	private String UPLOAD_LOCATION = "upload/app";

	@Autowired
	private VersionService versionService;

	@ApiOperation("获取所有版本信息")
	@GetMapping("/getVersion/{currentPage}/{pageSize}")
	public ResultInfo getVersion(@PathVariable Integer currentPage,
	                             @PathVariable Integer pageSize) {

		QueryWrapper<Version> versionQueryWrapper = new QueryWrapper<>();
		versionQueryWrapper.orderByDesc("create_date");
		Page<Version> page = new Page<>(currentPage, pageSize);

		Page<Version> versionPage = versionService.page(page, versionQueryWrapper);

		return ResultInfo.success("获取成功", versionPage);
	}


	@ApiOperation("删除版本信息")
	@DeleteMapping("/delversion/{id}")
	@Secured("ROLE_ADMIN")
	public ResultInfo delVersion(@PathVariable Long id) {

		boolean removeById = versionService.removeById(id);

		if (!removeById) {
			return ResultInfo.success("删除失败,该不存在版本", "");
		}
		return ResultInfo.success("删除成功", "");
	}


	@ApiOperation("修改版本信息")
	@PostMapping("/updateVersion/{id}")
	@Secured("ROLE_ADMIN")
	public ResultInfo addVersion(@PathVariable String id,
	                             @RequestBody(required = true) VersionVo versionVo) throws Exception {


		Version version = versionService.getById(id);

		BeanUtils.copyProperties(versionVo, version);

		versionService.updateById(version);

		return ResultInfo.success("修改成功", "");


	}


	@ApiOperation("上传软件")
	@PostMapping("/upload")
	public ResultInfo upload(@RequestBody(required = true) MultipartFile file,
	                         HttpServletRequest request) throws Exception {

		if (file == null) {
			throw new Exception("请选择上传文件");
		}

		Path directory = Paths.get(UPLOAD_LOCATION);
		if (!Files.exists(directory)) {
			Files.createDirectories(directory);
		}

		String filename = file.getOriginalFilename();
		String substring = filename.substring(filename.lastIndexOf("."));
		String realFilename = "szxc" + substring;

		Files.copy(file.getInputStream(), directory.resolve(realFilename));

		String href = "http://" + "36.134.80.177" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//		String href = "http://" + "101.132.153.67" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//		String href = "http://" + "localhost" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;

		return ResultInfo.success("上传成功", href);

	}
	
}
