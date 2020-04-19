package com.heeexy.example.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.CommonUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
public class ExcelImportService {

    private static Logger logger = LoggerFactory.getLogger(ExcelImportService.class);

    public List<JSONObject> parseExcel(MultipartFile file) {

        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            for (Row row : sheet) {
                for (Cell cell : row) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                    //单元格名称
                    System.out.print(cellRef.formatAsString());
                    System.out.print(" - ");

                    //通过获取单元格值并应用任何数据格式（Date，0.00，1.23e9，$ 1.23等），获取单元格中显示的文本
                    String text = formatter.formatCellValue(cell);
                    System.out.println(text);

                    //获取值并自己格式化
                    switch (cell.getCellType()) {
                        case STRING:// 字符串型
                            System.out.println(cell.getRichStringCellValue().getString());
                            break;
                        case NUMERIC:// 数值型
                            if (DateUtil.isCellDateFormatted(cell)) { // 如果是date类型则 ，获取该cell的date值
                                System.out.println(cell.getDateCellValue());
                            } else {// 纯数字
                                System.out.println(cell.getNumericCellValue());
                            }
                            break;
                        case BOOLEAN:// 布尔
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case FORMULA:// 公式型
                            System.out.println(cell.getCellFormula());
                            break;
                        case BLANK:// 空值
                            System.out.println();
                            break;
                        case ERROR: // 故障
                            System.out.println();
                            break;
                        default:
                            System.out.println();
                    }
                }
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
