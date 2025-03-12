package com.zcc.highmyopia.hospital.utils;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @ClassName PathGenerateUtil
 * @Description
 * @Author aigao
 * @Date 2025/3/12 16:11
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Setter
public class PathGenerateUtil {
    @Value("${hospital.filePath}")
    private String targetPath;
    private final static  String DefaultPatientID = "xxx";
    private final static  String DefaultItemName = "未知检查项目";
    private final static String DefaultUnknown = "检查时间未知";
    private final static DateTimeFormatter formatterWithSplit = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /*
     *1.根据patientID 获取到报告以后，立刻根据patientID，checkTime,itemName
     * 构建出/{病人ID}/{年}/{月}/{检查项目名称}/{文件名}
     * // 检查时间，格式为 "YYYY-MM-DD HH:MM:SS"
     * */
    public String generatePath(String patientID, String checkTime, String itemName, String type){
        if (StringUtils.isBlank(patientID)) {
            patientID = DefaultPatientID;
        }
        if(StringUtils.isBlank(itemName)){
            itemName = DefaultItemName;
        }
        String year = null, month = null, day;
        boolean flag = true;
        LocalDateTime parse;
        if(StringUtils.isBlank(checkTime)){
            checkTime = LocalDateTime.now().format(formatterWithSplit);
            flag = false;
        }
        parse = LocalDateTime.parse(checkTime, formatterWithSplit);
        year = String.valueOf(parse.getYear());
        month = String.valueOf(parse.getMonthValue());
        String fileName = parse.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +"_" + UUID.randomUUID().toString().substring(0,8) +"." + type;
        return flag ? Paths.get(targetPath, patientID, year, month,itemName, fileName).toString() :
                Paths.get(targetPath, patientID, DefaultUnknown, year, month,itemName, fileName).toString();
    }

    public static void main(String[] args) {
        PathGenerateUtil pathGenerateUtil = new PathGenerateUtil();
        pathGenerateUtil.setTargetPath("E:\\Download\\test");
        String s = pathGenerateUtil.generatePath("1832707106263822337", null, "光学相干断层成像（OCT）", "jpeg");
        System.out.println(s);
        File file = new File(s);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
            System.out.println(file.getName());
        }
    }
}
