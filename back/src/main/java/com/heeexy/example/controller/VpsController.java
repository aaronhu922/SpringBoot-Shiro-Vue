package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.VpsService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@RestController
@RequestMapping("/vps")
public class VpsController {

	@Autowired
	private VpsService vpsService;

	/**
	 * 查询文章列表
	 */
	@RequiresPermissions("vps:list")
	@GetMapping("/listVps")
	public JSONObject listVps(HttpServletRequest request) {
		return vpsService.listVps(CommonUtil.request2Json(request));
	}

	/**
	 * 新增文章
	 */
	@RequiresPermissions("vps:add")
	@PostMapping("/addVps")
	public JSONObject addVps(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "vpsname,vpsipport");
		return vpsService.addVps(requestJson);
	}

	/**
	 * 修改文章
	 */
	@RequiresPermissions("vps:update")
	@PostMapping("/updateVps")
	public JSONObject updateVps(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "id,vpsname,vpsipport");
		return vpsService.updateVps(requestJson);
	}
}
