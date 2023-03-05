package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.*;
import com.echo.country.service.GroupDescService;
import com.echo.country.service.NewsGroupService;
import com.echo.country.service.NewsService;
import com.echo.country.service.UserInfoService;
import com.echo.country.vo.ResultInfo;
import com.echo.country.vo.UpdateNews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/7 21:28
 */
@Api(tags = "公告接口")
@RestController
@Transactional
@RequestMapping("/news")
public class NewController {

	private String UPLOAD_LOCATION = "upload/newsIco";


	@Autowired
	NewsService newsService;

	@Autowired
	UserInfoService userService;

	@Autowired
	NewsGroupService newsGroupService;

	@Autowired
	GroupDescService groupDescService;

	@ApiOperation("删除指定公告")
	@DeleteMapping("/delNews/{id}")
	public ResultInfo delNews(@ApiParam(name = "id", value = "消息编号", required = true)
	                          @PathVariable Long id) throws Exception {

		try {
			newsService.removeById(id);
		} catch (Exception e) {
			throw new Exception("删除成功");
		}
		return ResultInfo.success("删除成功", "");

	}

	@ApiOperation("用户获取所有公告")
	@GetMapping("/getNews")
	public ResultInfo getNotices() {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		QueryWrapper<UserInfo> userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("tel", user.getUsername());
		UserInfo userInfo = userService.getOne(userQueryWrapper);

		QueryWrapper<GroupDesc> groupDescQueryWrapper = new QueryWrapper<>();

		groupDescQueryWrapper.eq("name", userInfo.getVillageName());

		GroupDesc one = groupDescService.getOne(groupDescQueryWrapper);

		QueryWrapper<NewsGroup> newsGroupQueryWrapper = new QueryWrapper<>();

		newsGroupQueryWrapper.eq("group_id", one.getId());

		List<NewsGroup> list = newsGroupService.list(newsGroupQueryWrapper);

		ArrayList<Long> news = new ArrayList<>();

		if (list.size() > 0) {
			list.forEach(l -> {
				news.add(l.getNewsId());
			});
			List<News> news1 = newsService.listByIds(news);

			QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();

			newsQueryWrapper.eq("is_allr", 1);

			List<News> list1 = newsService.list(newsQueryWrapper);

			if (list1 != null && list1.size() != 0) {
				news1.addAll(list1);
			}
			Collections.sort(news1, new Comparator<News>() {
				@Override
				public int compare(News o1, News o2) {
					return o2.getCreateTime().compareTo(o1.getCreateTime());
				}
			});

			return ResultInfo.success("获取成功", news1);

		}


		QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();

		newsQueryWrapper.eq("is_allr", 1);

		List<News> list1 = newsService.list(newsQueryWrapper);

		return ResultInfo.success("获取成功", list1);

	}

	@ApiOperation("后台获取所有公告")
	@GetMapping("/getNew/{currentPage}/{pageSize}")
	public ResultInfo getNoticess(@PathVariable @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                              @PathVariable @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize) {
		Page<News> newsPage = new Page<>(currentPage, pageSize);

		QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();

		newsQueryWrapper.orderByDesc("create_time");

		Page<News> page = newsService.page(newsPage, newsQueryWrapper);

		return ResultInfo.success("获取成功", page);

	}

//	@ApiOperation("添加公告")
//	@PostMapping("/addNews")
//	public ResultInfo addNews(@RequestBody NewsVo params) {
//
//		News news = new News();
//		if (params == null) {
//			return ResultInfo.failure("请传入有效参数", 500);
//		}
//
//		if (params.getIsAllr() == 1) {
//			BeanUtils.copyProperties(params, news);
//			newsService.save(news);
//			return ResultInfo.success("添加消息成功", "");
//		}
//
//		BeanUtils.copyProperties(params, news);
//
//		newsService.save(news);
//
//		QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
//
//		newsQueryWrapper.eq("title", params.getTitle());
//		newsQueryWrapper.eq("descb", params.getDescb());
//		newsQueryWrapper.eq("institutions", params.getInstitutions());
//
//		News one = newsService.getOne(newsQueryWrapper);
//
//		return ResultInfo.success("添加成功", one);
//
//	}


	@ApiOperation("选择公告接收村")
	@GetMapping("/choseVillages/{newsId}")
	public ResultInfo choseVillages(@PathVariable Long newsId, @RequestParam List<Long> ids) {

		if (ids == null || ids.size() == 0) {
			return ResultInfo.failure("请选择消息接收村", 500);
		}

		for (Long string : ids) {

			QueryWrapper<GroupDesc> groupDescQueryWrapper = new QueryWrapper<>();

			groupDescQueryWrapper.eq("id", string);

			GroupDesc one = groupDescService.getOne(groupDescQueryWrapper);

			NewsGroup newsGroup = new NewsGroup();

			newsGroup.setGroupId(one.getId());

			newsGroup.setNewsId(newsId);

			newsGroupService.save(newsGroup);

		}
		return ResultInfo.success("添加成功", "");

	}


	@ApiOperation("修改消息公告")
	@PutMapping("/updateNews/{id}")
	public ResultInfo updateNews(@PathVariable Long id,
	                             @RequestBody UpdateNews newsVo) {

		Path directory = Paths.get(UPLOAD_LOCATION);
		QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
		newsQueryWrapper.eq("id", id);
		News news = newsService.getOne(newsQueryWrapper);
		if (news == null) {
			return ResultInfo.failure("无该消息信息", 500);
		}
		String ico = news.getIco();
		if (StringUtils.isNotEmpty(newsVo.getIco())) {
			if (ico != null && ico.trim() != "") {
				String substring = ico.substring(ico.lastIndexOf("/") + 1);
				File file = new File(directory.toAbsolutePath().toString(), substring);
				file.delete();
			}
		}

		BeanUtils.copyProperties(newsVo, news);

		newsService.update(news, newsQueryWrapper);

		return ResultInfo.success("更新成功", "");

	}


	@ApiOperation("上传公告图标")
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
		String img = "http://" + "36.134.80.177" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//		String img = "http://" + "101.132.153.67" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;

		return ResultInfo.success("上传成功", img);
	}


	@ApiOperation("查看具体某条公告")
	@GetMapping("/getOneNews/{newsId}")
	public ResultInfo getOneNews(@PathVariable Long newsId) {

		System.out.println(newsId);
		News news = newsService.getById(newsId);

		if (news == null) {
			return ResultInfo.success("暂无该消息", "");
		}

		return ResultInfo.success("成功", news);

	}


}

