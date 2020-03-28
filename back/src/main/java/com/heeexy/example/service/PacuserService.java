package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface PacuserService {

    JSONObject addPacuser(JSONObject jsonObject);
    /**
     *
     */
    JSONObject listPacuser(JSONObject jsonObject);

    /**
     *
     */
    JSONObject updatePacuser(JSONObject jsonObject);

    JSONObject removePacuser(JSONObject jsonObject);

    JSONObject searchPacuser(JSONObject jsonObject);

    JSONObject importPacuserFiles(JSONObject jsonObject);
}
