package com.heeexy.example.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.VpsDao;
import com.heeexy.example.dao.WebsiteDao;
import com.heeexy.example.util.CommonUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExcelImportService {

    private static Logger logger = LoggerFactory.getLogger(ExcelImportService.class);


    @Autowired
    private WebsiteDao websiteDao;
    @Autowired
    private VpsDao vpsDao;

    public List<JSONObject> parseExcel(MultipartFile file) {
        Map<String, Integer> cachedKVMap = new HashMap();
        List<JSONObject> listAllWebsite = websiteDao.listAllWebsite();
        for (JSONObject website : listAllWebsite) {
            cachedKVMap.put(website.getString("websitename"), website.getIntValue("id"));
            logger.info("website name: {}, id: {}.", website.getString("websitename"), website.getIntValue("id"));
        }
        JSONObject vpsListInfo = new JSONObject();
        vpsListInfo.put("offSet", 0);
        vpsListInfo.put("pageRow", 100);
        List<JSONObject> vpsList = vpsDao.listVps(vpsListInfo);
        for (JSONObject vps : vpsList) {
            cachedKVMap.put(vps.getString("vpsname"), vps.getIntValue("id"));
            logger.info("vps name: {}, id: {}.", vps.getString("vpsname"), vps.getIntValue("id"));
        }

//        tempPacuser: {
//        id: "",
//        username: "",
//        userphone: "",
//        wxname: "",
//        websites: [],
//        vpsId: "",
//        comments: "",
//        deleteStatus: "1"
//      },

        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getPhysicalNumberOfRows();
            logger.info("This excel has {} rows total.", rowNum);
            DataFormatter formatter = new DataFormatter();
            for (int i =1; i<rowNum; i++) {
                Row row = sheet.getRow(i);

                String name = formatter.formatCellValue(row.getCell(0));
                if(name=="") break;
                String phone = formatter.formatCellValue(row.getCell(1));
                if(phone=="") break;
                String websiteList = formatter.formatCellValue(row.getCell(2));
                if(websiteList=="") break;
                String[] websiteNames = websiteList.split(",");
                int[] websiteIds = new int[websiteNames.length];
                int l=0;
                for (String wsname: websiteNames) {
                    logger.info("website: {}", wsname);
                    websiteIds[l++] = cachedKVMap.get(wsname);
                }
                String vps = formatter.formatCellValue(row.getCell(3));
                if(vps=="") break;

                String comment = formatter.formatCellValue(row.getCell(4));
                JSONObject tempPacuser = new JSONObject();
                tempPacuser.put("username", name);
                tempPacuser.put("userphone", phone);
                tempPacuser.put("websites", websiteIds);
                tempPacuser.put("vpsId", cachedKVMap.get(vps));
                tempPacuser.put("wxname", comment);
                logger.info("Imported pacuser: {}", tempPacuser);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject storeFile(MultipartFile file, String newFileName) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Copy file to the target location (Replacing existing file with the new name)
        Path targetLocation = Paths.get(newFileName);
        String sha1 = "";
        InputStream in = null;
        try {
            in = file.getInputStream();
            Files.copy(in, targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            logger.error("Failed to save file {}:\n {}", targetLocation, e);
        } finally {
            close(in);
        }
        JSONObject info = new JSONObject();
        info.put("totalCount", 1);
        info.put("totalPage", 2);
        return CommonUtil.successJson(info);
    }

    public void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            logger.error("Failed to save close input stream:\n {}", e);
        }
    }


}
