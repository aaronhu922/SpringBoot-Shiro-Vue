package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.PacuserService;
import com.heeexy.example.service.impl.ExcelImportService;
import com.heeexy.example.service.impl.PacuserServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@RestController
@RequestMapping("/pacuser")
public class PacuserController {

    private Logger logger = LoggerFactory.getLogger(PacuserController.class);

    @Autowired
    private PacuserService pacuserService;

    @Autowired
    private ExcelImportService excelImportService;

    /**
     * 查询用户列表
     */
    @RequiresPermissions("pacuser:list")
    @GetMapping("/listPacuser")
    public JSONObject listPacuser(HttpServletRequest request) {
        return pacuserService.listPacuser(CommonUtil.request2Json(request));
    }

    @RequiresPermissions("pacuser:list")
    @GetMapping("/searchPacuser")
    public JSONObject searchPacuser(HttpServletRequest request) {
        return pacuserService.searchPacuser(CommonUtil.request2Json(request));
    }

    @RequiresPermissions("pacuser:add")
    @PostMapping("/addPacuser")
    public JSONObject addPacuser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "userphone,username");
        return pacuserService.addPacuser(requestJson);
    }

    @RequiresPermissions("pacuser:update")
    @PostMapping("/updatePacuser")
    public JSONObject updatePacuser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,userphone,username");
        return pacuserService.updatePacuser(requestJson);
    }

    @RequiresPermissions("pacuser:delete")
    @PostMapping("/removePacuser")
    public JSONObject removePacuser(@RequestBody JSONObject requestJson) {
        return pacuserService.removePacuser(requestJson);
    }

    @GetMapping("/importPacuser")
    public JSONObject importPacuser(HttpServletRequest request) {
        return pacuserService.importPacuserFiles(CommonUtil.request2Json(request));
    }

    @PostMapping(path = "/uploadPacusers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JSONObject uploadPacFile(@RequestPart("uploadFile") MultipartFile inputFile) {
        try {
            if (inputFile == null) {
                logger.error("Missing parameters while uploading.");
                return null;
            }
            excelImportService.parseExcel(inputFile);
           return excelImportService.storeFile(inputFile, "test.xlsx");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
