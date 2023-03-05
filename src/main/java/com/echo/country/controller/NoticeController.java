package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.Notice;
import com.echo.country.pojo.User;
import com.echo.country.pojo.UserInfo;
import com.echo.country.service.NoticeService;
import com.echo.country.service.UserInfoService;
import com.echo.country.vo.NoticeVo;
import com.echo.country.vo.QueryNoticeVo;
import com.echo.country.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/2/26 13:09
 */
@Transactional
@RestController
@RequestMapping("/notice")
@Api(tags = "通知接口")
public class NoticeController {

	@Autowired
	NoticeService noticeService;

	@Autowired
	UserInfoService userInfoService;

	//Notice图片存放位置
	private String UPLOAD_LOCATION = "upload/img";

	@ApiOperation("分页获取所有通知")
	@GetMapping("/getNotices/{currentPage}/{pageSize}")
	public ResultInfo getNotices(@PathVariable @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                             @PathVariable @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize,
	                             QueryNoticeVo queryNoticeVo) {

		Page<Notice> noticePage = new Page<>(currentPage, pageSize);

		QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();

		if (queryNoticeVo != null) {
			if (queryNoticeVo.getTitle() != null && queryNoticeVo.getTitle().trim() != "") {
				queryWrapper.like("title", queryNoticeVo.getTitle());
			}
			if (queryNoticeVo.getInstitutions() != null && queryNoticeVo.getInstitutions().trim() != "") {
				queryWrapper.like("institutions", queryNoticeVo.getInstitutions());
			}

			if (queryNoticeVo.getDescb() != null && queryNoticeVo.getDescb().trim() != "") {
				queryWrapper.like("descb", queryNoticeVo.getDescb());
			}
		}
		queryWrapper.orderByDesc("create_time");


		Page<Notice> page = noticeService.page(noticePage);


		if (page.getTotal() == 0) {
			return ResultInfo.success("未找到", page);

		}

		return ResultInfo.success("获取通知成功", page);
	}


	@ApiOperation("用户获取本村通知通知")
	@GetMapping("/findNotice")
	public ResultInfo getNotice() {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();

		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tel", username);
		UserInfo userInfo = userInfoService.getOne(queryWrapper);
		String villageName = userInfo.getVillageName();

		QueryWrapper<Notice> noticeQueryWrapper = new QueryWrapper<>();

		noticeQueryWrapper.eq("institutions", villageName);

		List<Notice> list = noticeService.list(noticeQueryWrapper);

		noticeQueryWrapper.clear();

		if ("村长".equals(userInfo.getRole()) || "乡长".equals(userInfo.getRole())) {
			String townshipName = userInfo.getTownshipName();

			noticeQueryWrapper.eq("institutions", townshipName);

			List<Notice> list0 = noticeService.list(noticeQueryWrapper);

			if (list0.size() != 0) {

				list0.forEach((l) -> {
					list.add(l);
				});
			}
		}

		if (list == null || list.size() == 0) {
			return ResultInfo.success("暂无通知", "");
		}

		return ResultInfo.success("成功", list);

	}


	@ApiOperation("添加通知")
	@PostMapping("/addNotice")
	public ResultInfo addNotice(HttpServletRequest request,
	                            @ApiParam(value = "接收通知实体类", required = true)
	                            @RequestBody NoticeVo noticeVo) throws Exception {

		Notice notice = new Notice();

		BeanUtils.copyProperties(noticeVo, notice);

		if (notice.getViews() == null) {

			notice.setViews(0);

		}
		noticeService.save(notice);

		return ResultInfo.success("添加成功", "");

	}

	@ApiOperation("查看指定通知且浏览量+1")
	@GetMapping("/getOneNotice/{noticeId}")
	public ResultInfo getOneNotice(@ApiParam(name = "noticeId", value = "指定通知编号") @PathVariable Long noticeId) throws Exception {


		QueryWrapper<Notice> noticeQueryWrapper = new QueryWrapper<>();

		noticeQueryWrapper.eq("id", noticeId);

		Notice notice = noticeService.getOne(noticeQueryWrapper);

		if (null == notice) {
			return ResultInfo.success("查无该通知信息", "");
		}
		notice.setViews(notice.getViews() + 1);
		noticeService.update(notice, noticeQueryWrapper);

		Notice notice1 = noticeService.getById(noticeId);

		Map<String, Object> map = new HashMap<>();
		map.put("notice", notice1);
		return ResultInfo.success("获得编号为" + noticeId + "的通知成功", map);

	}

	@ApiOperation("删除指定通知")
	@DeleteMapping("/delNotice/{noticeId}")
	@Secured("ROLE_ADMIN")
	public ResultInfo delNotice(@ApiParam(name = "noticeId", value = "通知编号", required = true) @PathVariable Long noticeId) {
		noticeService.removeById(noticeId);
		return ResultInfo.success("移除成功", null);
	}


	@ApiOperation("上传通知图片")
	@PostMapping("/uploadImg")
	public ResultInfo uploadImg(@RequestBody MultipartFile file,
	                            HttpServletRequest request) throws Exception {
		try {

			if (file == null) {
				throw new Exception("请选择上传图片");
			}

			//创建打成jar后，用户上传的图片储存路径（未部署时，与src同级，打成jar包后，该文件与jar包同级）
			Path directory = Paths.get(UPLOAD_LOCATION);
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}

			String filename = file.getOriginalFilename();
			String substring = filename.substring(filename.lastIndexOf("."));
			String realFilename = System.currentTimeMillis() + substring;

			Files.copy(file.getInputStream(), directory.resolve(realFilename));

			String img = "http://" + "36.134.80.177" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//			String img = "http://" + "101.132.153.67" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;
//			String img = "http://" + "localhost" + ":" + request.getServerPort() + "/" + UPLOAD_LOCATION + "/" + realFilename;

			return ResultInfo.success("上传成功", img);
		} catch (Exception e) {
			throw new Exception("更新失败");
		}
	}

	@ApiOperation("修改指定通知")
	@PutMapping("/updateNotice/{noticeId}")
	@Secured("ROLE_ADMIN")
	public ResultInfo updateNotice(HttpServletRequest request,
	                               @ApiParam(name = "noticeId", value = "需要修改通知的编号", required = true)
	                               @PathVariable Long noticeId,
	                               @RequestBody NoticeVo noticeVo) throws Exception {

		try {
//			String path = ResourceUtils.getURL("src/main/resources/static/img").getPath();


			QueryWrapper<Notice> noticeQueryWrapper = new QueryWrapper<>();

			noticeQueryWrapper.eq("id", noticeId);

			Notice notice = noticeService.getOne(noticeQueryWrapper);

			if (null == notice) {
				return ResultInfo.failure("无该通知信息", 500);
			}

			Path directory = Paths.get(UPLOAD_LOCATION);
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}

			if (notice.getImg() != null && notice.getImg().trim() != "") {
				String image = notice.getImg();
				String substring1 = image.substring(image.lastIndexOf("/") + 1);
				File file = new File(directory.toAbsolutePath().toString(), substring1);
				file.delete();
			}

			BeanUtils.copyProperties(noticeVo, notice);
			noticeService.update(notice, noticeQueryWrapper);
			return ResultInfo.success("更新成功", "");

		} catch (Exception e) {
			throw new Exception("更新失败");
		}

	}



}
