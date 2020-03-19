package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface WebsiteService {

    JSONObject addWebsite(JSONObject jsonObject);

    /**
     * 文章列表
     */
    JSONObject listWebsite(JSONObject jsonObject);

    /**
     * 更新文章
     */
    JSONObject updateWebsite(JSONObject jsonObject);
}
