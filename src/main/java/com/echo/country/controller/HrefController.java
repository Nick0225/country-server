package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.Href;
import com.echo.country.service.HrefService;
import com.echo.country.vo.HrefVo;
import com.echo.country.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/5 14:21
 */
@Api(tags = "外链接口")
@RequestMapping("/href")
@RestController
@Transactional
public class HrefController {

	private String UPLOAD_LOCATION = "upload/ico";


	@Autowired
	private HrefService hrefService;

	@ApiOperation("删除外链")
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/del/{id}")
	public ResultInfo delHref(@ApiParam(name = "id", value = "外链编号", required = true)
	                          @PathVariable Long id) {
		boolean remove = hrefService.removeById(id);

		return remove == true ? ResultInfo.success("移除成功", "") : ResultInfo.failure("移除失败", 500);

	}


	@ApiOperation("修改外链内容")
	@Secured("ROLE_ADMIN")
	@PutMapping("/updateHref/{id}")
	public ResultInfo updateHref(@ApiParam(name = "id", value = "外链编号", required = true)
	                             @PathVariable Long id,
	                             @RequestBody HrefVo hrefVo) {
		Path directory = Paths.get(UPLOAD_LOCATION);
		QueryWrapper<Href> hrefQueryWrapper = new QueryWrapper<>();
		hrefQueryWrapper.eq("id", id);


		Href href1 = hrefService.getById(id);
		String ico = href1.getIco();
		if (ico != null && ico.trim() != "") {
			String substring = ico.substring(ico.lastIndexOf("/") + 1);
			File file = new File(directory.toAbsolutePath().toString(), substring);
			file.delete();
		}

		Href href = new Href();
		BeanUtils.copyProperties(hrefVo, href);

		hrefService.update(href, hrefQueryWrapper);

		return ResultInfo.success("修改成功", "");
	}

	@ApiOperation("添加外链")
	@Secured("ROLE_ADMIN")
	@PostMapping("/addHref")
	public ResultInfo addHref(@RequestBody HrefVo hrefVo) {

		Href href = new Href();
		BeanUtils.copyProperties(hrefVo, href);

		hrefService.save(href);

		return ResultInfo.success("添加成功", "");

	}


	@ApiOperation("查找外链")
	@GetMapping("/findHref/{currentPage}/{pageSize}")
	public ResultInfo resultInfo(@PathVariable @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                             @PathVariable @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize,
	                             @ApiParam(name = "name", value = "外链名称", required = false) String name) {

		QueryWrapper<Href> hrefQueryWrapper = new QueryWrapper<>();
		if (name != null) {
			hrefQueryWrapper.like("name", name);
		}

		Page<Href> hrefPage = new Page<>(currentPage, pageSize);
		Page<Href> page = hrefService.page(hrefPage, hrefQueryWrapper);
		if (page.getTotal() == 0) {
			return ResultInfo.success("无该外链信息", "");
		}

		return ResultInfo.success("查找成功", page);

	}


	@ApiOperation("上传外链图标")
	@Secured("ROLE_ADMIN")
	@PostMapping("/uploadIco")
	public ResultInfo uploadIco(@RequestBody(required = true) MultipartFile file,
	                            HttpServletRequest request) throws Exception {

		if (file == null) {
			throw new Exception("请选择上传图片");
		}
		Path path = Paths.get(UPLOAD_LOCATION);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		String filename = file.getOriginalFilename();
		String substring = filename.substring(filename.lastIndexOf("."));
		String realFilename = System.currentTimeMillis() + substring;

		Files.copy(file.getInputStream(), path.resolve(realFilename));

//		String img = "http://" + "localhost" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//		String img = "http://" + "101.132.153.67" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
		String img = "http://" + "36.134.80.177" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;

		return ResultInfo.success("上传成功", img);
	}


}
