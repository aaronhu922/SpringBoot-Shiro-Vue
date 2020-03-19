package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.VpsDao;
import com.heeexy.example.service.VpsService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VpsServiceImpl implements VpsService {

    @Autowired
    private VpsDao vpsDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addVps(JSONObject jsonObject) {
        vpsDao.addVps(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 文章列表
     */
    @Override
    public JSONObject listVps(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = vpsDao.countVps(jsonObject);
        List<JSONObject> list = vpsDao.listVps(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 更新文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateVps(JSONObject jsonObject) {
        vpsDao.updateVps(jsonObject);
        return CommonUtil.successJson();
    }
}
