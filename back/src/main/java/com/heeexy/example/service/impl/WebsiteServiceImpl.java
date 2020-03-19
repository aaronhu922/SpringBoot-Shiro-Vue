package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.WebsiteDao;
import com.heeexy.example.service.WebsiteService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WebsiteServiceImpl implements WebsiteService {
    @Autowired
    private WebsiteDao websiteDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addWebsite(JSONObject jsonObject) {
        websiteDao.addWebsite(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 文章列表
     */
    @Override
    public JSONObject listWebsite(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = websiteDao.countWebsite(jsonObject);
        List<JSONObject> list = websiteDao.listWebsite(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 更新文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateWebsite(JSONObject jsonObject) {
        websiteDao.updateWebsite(jsonObject);
        return CommonUtil.successJson();
    }
}
