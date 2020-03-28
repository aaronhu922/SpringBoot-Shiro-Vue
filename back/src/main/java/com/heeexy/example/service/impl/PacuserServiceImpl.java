package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.pacfile.PacfileConfiguration;
import com.heeexy.example.dao.PacuserDao;
import com.heeexy.example.dao.VpsDao;
import com.heeexy.example.dao.WebsiteDao;
import com.heeexy.example.service.PacuserService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class PacuserServiceImpl implements PacuserService {

    private Logger logger = LoggerFactory.getLogger(PacuserServiceImpl.class);

    @Autowired
    private PacuserDao pacuserDao;

    @Autowired
    private WebsiteDao websiteDao;

    @Autowired
    private VpsDao vpsDao;

    @Autowired
    private PacFilesService pacFilesService;

    @Autowired
    private PacfileConfiguration pacfileConfiguration;

    @Override
    public JSONObject addPacuser(JSONObject jsonObject) {
        logger.info("Creating pacuser {}.", jsonObject);
        int exist = pacuserDao.queryExistUserPhone(jsonObject);
        if (exist > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }

        fillCommentsWithPacURL(jsonObject);

        pacuserDao.addPacuser(jsonObject);
        pacuserDao.insertUserWebsite(jsonObject.getIntValue("id"), (List<Integer>) jsonObject.get("websites"));

        createPacFile(jsonObject);
        return CommonUtil.successJson();
    }

    private boolean createPacFile(JSONObject jsonObject) {
        String userPhone = jsonObject.getString("userphone");
        List<Integer> newWebs = (List<Integer>) jsonObject.get("websites");
        StringBuffer sb = new StringBuffer();
        for (int id : newWebs) {
            sb.append(websiteDao.getProxyaddr(id));
        }
        String proxyString = sb.toString();
        logger.info("proxyString is {}. File path is {}.", proxyString, pacfileConfiguration.getPacfilePath());
        String vpsIpport = vpsDao.getVpsIpport(jsonObject.getIntValue("vpsId"));
        logger.info("Vps ip and port is {}.", vpsIpport);
        return pacFilesService.writePacFile(pacfileConfiguration.getPacfilePath() + userPhone + ".pac", proxyString, vpsIpport);
    }

    @Override
    public JSONObject listPacuser(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = pacuserDao.countPacuser(jsonObject);
        List<JSONObject> list = pacuserDao.listPacuser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject updatePacuser(JSONObject jsonObject) {
        logger.info("Updating pacuser {}.", jsonObject);
        List<Integer> newWebs = (List<Integer>) jsonObject.get("websites");
//        fillCommentsWithPacURL(jsonObject);
        pacuserDao.updatePacuser(jsonObject);
        int pacuserId = jsonObject.getIntValue("id");
        Set<Integer> oldWebs = (Set<Integer>) pacuserDao.getWebsitesByUserId(pacuserId);
        saveNewWebsite(pacuserId, newWebs, oldWebs);
        removeOldWebsites(pacuserId, newWebs, oldWebs);

        createPacFile(jsonObject);
        return CommonUtil.successJson();
    }

    private void fillCommentsWithPacURL(JSONObject jsonObject) {
        String userPhone = jsonObject.getString("userphone");
        String pacUrl = pacfileConfiguration.getPacfileURL() + userPhone + ".pac";
        jsonObject.put("comments", pacUrl);
    }

    private void removeOldWebsites(int pacuserId, List<Integer> newWebs, Set<Integer> oldWebs) {
        List<Integer> waitRemove = new ArrayList<>();
        for (Integer oldWeb : oldWebs) {
            if (!newWebs.contains(oldWeb)) {
                waitRemove.add(oldWeb);
            }
        }
        if (waitRemove.size() > 0) {
            pacuserDao.removeOldWebsites(pacuserId, waitRemove);
        }

    }

    private void saveNewWebsite(int pacuserId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitInsert = new ArrayList<>();
        for (Integer newPerm : newPerms) {
            if (!oldPerms.contains(newPerm)) {
                waitInsert.add(newPerm);
            }
        }
        if (waitInsert.size() > 0) {
            pacuserDao.insertUserWebsite(pacuserId, waitInsert);
        }
    }

    @Override
    public JSONObject removePacuser(JSONObject jsonObject) {
        logger.info("Removing pacuser {}.", jsonObject);
        int pacuserId = jsonObject.getIntValue("pacuserId");
        String userPhone = jsonObject.getString("userphone");
        logger.info("Remove user with phone {}.", userPhone);

        pacuserDao.deletePacuser(pacuserId);
        pacuserDao.deletePacuserWebsites(pacuserId);
        pacFilesService.deletePacFile(pacfileConfiguration.getPacfilePath() + userPhone + ".pac");
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject searchPacuser(JSONObject jsonObject) {
        logger.info("Searching pacuser {}.", jsonObject);
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = pacuserDao.searchPacuser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, list.size());
    }

    @Override
    public JSONObject importPacuserFiles(JSONObject jsonObject) {
        List<Integer> websiteIds = websiteDao.listWebsitesIds();
        List<JSONObject> jsonObjects = pacFilesService.importPacFiles(pacfileConfiguration.getPacfilePath(), pacfileConfiguration.getPacfileURL());
        for (JSONObject pacuser : jsonObjects) {
            pacuserDao.addPacuser(pacuser);
            pacuserDao.insertUserWebsite(pacuser.getIntValue("id"), websiteIds);
            logger.info("Insert into pacuser {} into db.", pacuser);
        }
        return CommonUtil.successJson();
    }

}
