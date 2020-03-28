package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface VpsDao {

    int addVps(JSONObject jsonObject);

    /**
     *
     */
    int countVps(JSONObject jsonObject);

    /**
     *
     */
    List<JSONObject> listVps(JSONObject jsonObject);

    /**
     *
     */
    int updateVps(JSONObject jsonObject);

    String getVpsIpport(int id);
}
