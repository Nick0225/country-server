package com.echo.country.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echo.country.pojo.Server;
import com.echo.country.service.ServerService;
import com.echo.country.vo.AddServer;
import com.echo.country.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/9 19:29
 */
@RequestMapping("/server")
@Api(tags = "客服通讯录")
@RestController
@Transactional
public class ServerController {


	@Autowired
	ServerService service;


	@ApiOperation("删除指定客服")
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/{id}")
	public ResultInfo deleteServer(@PathVariable Long id) {
		service.removeById(id);
		return ResultInfo.success("移除成功", "");
	}

	@ApiOperation("添加客服")
	@Secured("ROLE_ADMIN")
	@PostMapping("/addServer")
	public ResultInfo addServer(@RequestBody AddServer addServer) {

		Server server = new Server();

		BeanUtils.copyProperties(addServer, server);

		service.save(server);

		return ResultInfo.success("添加成功", "");
	}

	@ApiOperation("修改客服信息")
	@Secured("ROLE_ADMIN")
	@PutMapping("/updateServer/{id}")
	public ResultInfo updateServer(@PathVariable Long id, @RequestBody AddServer addServer) {
		Server server = new Server();
		QueryWrapper<Server> serverQueryWrapper = new QueryWrapper<>();
		serverQueryWrapper.eq("id", id);
		BeanUtils.copyProperties(addServer, server);

		service.update(server, serverQueryWrapper);

		return ResultInfo.success("修改成功", "");

	}

	//	@ApiOperation("分页获取客服信息")
//	@GetMapping("/getServers/{currentPage}/{pageSize}")
	public ResultInfo getAllServer(@PathVariable @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                               @PathVariable @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize
	) {
		Page<Server> serverPage = new Page<>(currentPage, pageSize);
		Page<Server> page = service.page(serverPage);
		if (page.getTotal() == 0) {
			return ResultInfo.success("暂无客服信息", "");
		}
		return ResultInfo.success("获取成功", page);

	}

	@ApiOperation("条件获取客服信息")
	@GetMapping("/getServers/{currentPage}/{pageSize}")
	public ResultInfo getAllServers(@PathVariable @ApiParam(name = "currentPage", value = "当前页", required = true) Integer currentPage,
	                                @PathVariable @ApiParam(name = "pageSize", value = "页容量", required = true) Integer pageSize,
	                                @RequestParam(required = false) String name) {

		Page<Server> serverPage = new Page<>(currentPage, pageSize);


		QueryWrapper<Server> serverQueryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(name)) {
			serverQueryWrapper.like("name", name);
		}
		Page<Server> page = service.page(serverPage, serverQueryWrapper);
		if (page.getTotal() == 0) {
			return ResultInfo.success("暂无客服信息", "");
		}
		return ResultInfo.success("获取成功", page);
	}


}
