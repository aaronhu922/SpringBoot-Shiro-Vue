package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PacFilesService {

    private Logger logger = LoggerFactory.getLogger(PacFilesService.class);


    private static final String proxyString1 = "var list = [";
    private static final String proxyString2 = "]; var proxy = \"PROXY ";
    private static final String proxyString3 = "\";\n" +
            "function FindProxyForURL(url, host) {\n" +
            "    for (var i = 0, l = list.length; i < l; i++) {\n" +
            "        if (shExpMatch(host, list[i])) {\n" +
            "            return proxy;\n" +
            "        }\n" +
            "    }\n" +
            "    return \"DIRECT\";\n" +
            "}";

    public boolean writePacFile(String filename, String sitesString, String serverAddr) {
        StringBuffer sb = new StringBuffer(proxyString1).append(sitesString).append(proxyString2).append(serverAddr).append(proxyString3);
        try {
            Files.write(sb.toString().getBytes(), new File(filename));
            logger.info("创建pacfile{}成功！", filename);
            return true;
        } catch (IOException e) {
            logger.error("创建pacfile{}失败, {}！", filename, e);
        }
        return false;
    }


    public boolean deletePacFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info("删除单个文件{}成功！", fileName);
                return true;
            } else {
                logger.info("删除单个文件{}失败！", fileName);
                return false;
            }
        } else {
            logger.info("删除单个文件失败！ {}不存在！", fileName);
            return false;
        }
    }

    public static void main(String[] args) {
        List<JSONObject> jsonObjects = new PacFilesService().importPacFiles("/home/aaronhu/Documents", "http://47.114.176.127/");
    }

    public List<JSONObject> importPacFiles(String pacfilePath, String pacServerURL) {
        File file = new File(pacfilePath);
        File[] list = file.listFiles();
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();


        if (file.exists() && file.isDirectory()) {
            for (int i = 0; i < list.length; i++) {
                String name = list[i].getName();
                if (name.endsWith(".pac")) {
                    JSONObject info = new JSONObject();
                    String userPhone = name.substring(0, name.length() - 4);

                    info.put("username", userPhone);
                    info.put("userphone", userPhone);
                    info.put("vpsId", (i % 2 == 0) ? 20 : 21);
                    info.put("comments", pacServerURL + name);
                    logger.info("Imported pacuser info: {}", info);
                    jsonObjects.add(info);
                }

            }
        }
        return jsonObjects;
    }
}
