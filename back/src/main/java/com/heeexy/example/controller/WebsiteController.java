package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.WebsiteService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@RestController
@RequestMapping("/website")
public class WebsiteController {

	@Autowired
	private WebsiteService websiteService;

	/**
	 * 查询文章列表
	 */
//	@RequiresPermissions("website:list")
	@GetMapping("/listWebsite")
	public JSONObject listWebsite(HttpServletRequest request) {
		return websiteService.listWebsite(CommonUtil.request2Json(request));
	}

	/**
	 * 新增文章
	 */
//	@RequiresPermissions("website:add")
	@PostMapping("/addWebsite")
	public JSONObject addWebsite(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "websitename,proxyaddr");
		return websiteService.addWebsite(requestJson);
	}

	/**
	 * 修改文章
	 */
//	@RequiresPermissions("website:update")
	@PostMapping("/updateWebsite")
	public JSONObject updateWebsite(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "id,websitename,proxyaddr");
		return websiteService.updateWebsite(requestJson);
	}
}
