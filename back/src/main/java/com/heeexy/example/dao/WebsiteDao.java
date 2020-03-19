package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface WebsiteDao {

    int addWebsite(JSONObject jsonObject);

    /**
     *
     */
    int countWebsite(JSONObject jsonObject);

    /**
     *
     */
    List<JSONObject> listWebsite(JSONObject jsonObject);

    /**
     *
     */
    int updateWebsite(JSONObject jsonObject);
}
