package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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

    int insertUserWebsite(@Param("pacuserId") int pacuserId, @Param("websites") List<Integer> websites);

    Set<Integer> getWebsitesByUserId(int pacuserId);

    void removeOldWebsites(int pacuserId, @Param("websites") List<Integer> waitRemove);

    int deletePacuser(int pacuserId);

    int deletePacuserWebsites(int pacuserId);

    int queryExistUserPhone(JSONObject jsonObject);

    List<JSONObject> searchPacuser(JSONObject jsonObject);
}
