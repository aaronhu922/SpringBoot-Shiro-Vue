package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface PacuserDao {

    int addPacuser(JSONObject jsonObject);

    /**
     *
     */
    int countPacuser(JSONObject jsonObject);

    /**
     *
     */
    List<JSONObject> listPacuser(JSONObject jsonObject);

    /**
     *
     */
    int updatePacuser(JSONObject jsonObject);
}
