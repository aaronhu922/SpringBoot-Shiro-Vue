package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface VpsService {

    JSONObject addVps(JSONObject jsonObject);

    /**
     * 文章列表
     */
    JSONObject listVps(JSONObject jsonObject);

    /**
     * 更新文章
     */
    JSONObject updateVps(JSONObject jsonObject);
}
