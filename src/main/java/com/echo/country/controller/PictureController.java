package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.Picture;
import com.echo.country.service.PictureService;
import com.echo.country.vo.PictureVo;
import com.echo.country.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2022/2/26 17:21
 */
@Slf4j
@Api(tags = "图片链接接口")
@RequestMapping("/picture")
@RestController
@Transactional
public class PictureController {

	@Autowired
	private PictureService pictureService;

	private String UPLOAD_LOCATION = "upload/picture";

	@ApiOperation("分页获取图片信息（按插入时间排序）")
	@GetMapping("/getPictures/{currentPage}/{pageSize}")
	public ResultInfo getPictures(@PathVariable
	                              @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                              @PathVariable
	                              @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize) {
		Page<Picture> picturePage = new Page<>(currentPage, pageSize);
		QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
		pictureQueryWrapper.orderByDesc("update_time");
		Page<Picture> page = pictureService.page(picturePage, pictureQueryWrapper);
		if (page.getTotal() == 0) {
			return ResultInfo.success("未找到", page);

		}
		return ResultInfo.success("获取成功", page);
	}

	@ApiOperation("删除指定图片信息")
	@DeleteMapping("/deldPicture/{id}")
	@Secured("ROLE_ADMIN")
	public ResultInfo delPicture(@ApiParam(name = "id", value = "图片编号", required = true)
	                             @PathVariable Long id) {
		pictureService.removeById(id);

		return ResultInfo.success("移除成功", null);
	}

	@ApiOperation("上传图片")
	@PostMapping("/uploadImg")
	public ResultInfo uploadImg(HttpServletRequest request,
	                            @RequestBody MultipartFile file) throws Exception {

		if (file == null) {
			throw new Exception("请选择上传图片");
		}

		Path directory = Paths.get(UPLOAD_LOCATION);
		if (!Files.exists(directory)) {
			Files.createDirectories(directory);
		}

		String filename = file.getOriginalFilename();
		String substring = filename.substring(filename.lastIndexOf("."));
		String realFilename = System.currentTimeMillis() + substring;

		Files.copy(file.getInputStream(), directory.resolve(realFilename));

		String img = "http://" + "36.134.80.177" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//		String img = "http://" + "101.132.153.67" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//		String img = "http://" + "localhost" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;

		return ResultInfo.success("上传成功", img);

	}


	@ApiOperation("添加图片信息")
	@PostMapping("/addPicture")
	@Secured("ROLE_ADMIN")
	public ResultInfo addPicture(
			@RequestBody PictureVo pictureVo,
			HttpServletRequest request) throws Exception {

		Picture picture = new Picture();

		BeanUtils.copyProperties(pictureVo, picture);

		pictureService.save(picture);

		return ResultInfo.success("添加成功", "");
	}

	@ApiOperation("修改图片信息")
	@PutMapping("/updatePicture/{id}")
	@Secured("ROLE_ADMIN")
	public ResultInfo updatePicture(
			@ApiParam(name = "id", value = "编号", required = true)
			@PathVariable Integer id,
			@RequestBody PictureVo pictureVo,
			HttpServletRequest request) throws Exception {

//		String path = ResourceUtils.getURL("src/main/resources/static/picture").getPath();

		QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
		pictureQueryWrapper.eq("id", id);
		Picture picture = pictureService.getById(id);
		if (picture == null) {
			throw new Exception("无该图片信息");
		}
		Path directory = Paths.get(UPLOAD_LOCATION);
		if (!Files.exists(directory)) {
			Files.createDirectories(directory);
		}

		String img = picture.getImg();
		if (img != null && img.trim() != "") {
			String substring = img.substring(img.lastIndexOf("/") + 1);
			File file = new File(directory.toAbsolutePath().toString(), substring);
			file.delete();
		}
		BeanUtils.copyProperties(pictureVo, picture);
		pictureService.update(picture, pictureQueryWrapper);
		return ResultInfo.success("更新成功", null);

	}


}
