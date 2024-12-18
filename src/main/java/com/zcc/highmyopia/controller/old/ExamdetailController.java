package com.zcc.highmyopia.controller.old;


import cn.hutool.core.map.MapUtil;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.old.Examdetail;
import com.zcc.highmyopia.mapper.old.CaselistMapper;
import com.zcc.highmyopia.mapper.old.ExamdetailMapper;
import com.zcc.highmyopia.service.old.CaselistService;
import com.zcc.highmyopia.service.old.ExamdetailService;
import com.zcc.highmyopia.util.MultipartFileToFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangyue
 * @since 2021-02-21
 */
@RestController
@RequestMapping("/examdetail")
public class ExamdetailController {


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    @Autowired
    ExamdetailService examdetailService;
    @Autowired
    ExamdetailMapper examdetailMapper;
    @Autowired
    CaselistMapper caselistMapper;
    @Autowired
    CaselistService caselistService;

    @GetMapping("/getOPTByCaseId/{id}")
    @RequiresAuthentication
    public Result getOPTByExamId(@PathVariable(name = "id") Integer id) {
        return Result.succ(examdetailMapper.getOptByCaseId(id));
    }

    @GetMapping("/getOCTByCaseId/{id}")
    @RequiresAuthentication
    public Result getOCTByExamId(@PathVariable(name = "id") Integer id) {
        return Result.succ(examdetailMapper.getOctByCaseId(id));
    }

    @GetMapping("/getIOLByCaseId/{id}")
    @RequiresAuthentication
    public Result getIOLByExamId(@PathVariable(name = "id") Integer id) {
        return Result.succ(examdetailMapper.getIolByCaseId(id));
    }


    @GetMapping("/getByExamId/{id}")
    @RequiresAuthentication
    public Result getPathByExamId(@PathVariable(name = "id") String id) {
//        System.out.println("==============");
//        String path = examdetailMapper.selectExamdetailByExamId(id);
//        PdfRead pdfRead = new PdfRead();
//        String[] str = new String[2];
//        try {
//            str = pdfRead.iolRead(path, id);
//            for(Caselist cl : caselistMapper.selectCaseByIOLExamId(id)) {
//                cl.setALOD(str[0]);
//                cl.setALOS(str[1]);
//                caselistService.saveOrUpdate(cl);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        System.out.println("==============");
        return Result.succ(examdetailMapper.selectExamdetailByExamId(id));
    }

    @PostMapping("/uploadIOLMaster")
    public Result uploadIOLMaster(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

        String alod = null;
        String alos = null;
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = PDDocument.load(MultipartFileToFile.multipartFileToFile(file));
        if("IOL".equals(file.getOriginalFilename().split("-")[0])){
            String LRRegex = "(\\u0053\\u004e\\u0036\\u0030\\u0057\\u0046)";
            String temp1 = pdfStripper.getText(pdDoc).split(LRRegex)[0];
            String temp2 = pdfStripper.getText(pdDoc).split(LRRegex)[1];
            String HaigisRegex = ".+[\\s]+(\\u006d\\u006d)(?=\\u0020\\u0028\\u0053\\u004e\\u0052)";
            Pattern p = Pattern.compile(HaigisRegex);
//            Matcher m  = p.matcher(pdfStripper.getText(pdDoc));
            Matcher m1  = p.matcher(temp1);
            System.out.println(m1);
            while(m1.find()) {
                String result = m1.group();
                System.out.println(result);
                alod = result;

            }
            Matcher m2  = p.matcher(temp2);
            System.out.println(m2);
            while(m2.find()) {
                String result = m2.group();
                System.out.println(result);
                alos = result;
            }

        } else if("MMT".equals(file.getOriginalFilename().split("-")[0])) {
            String LRRegex = "(\\u6709\\u6676\\u72b6\\u4f53)";
            String temp1 = pdfStripper.getText(pdDoc).split(LRRegex)[0];
            String temp2 = pdfStripper.getText(pdDoc).split(LRRegex)[1];
            String HaigisRegex = "(?<=复合AL: )(.+[\\s]+(\\u006d\\u006d))";
            Pattern p = Pattern.compile(HaigisRegex);
            Matcher m1  = p.matcher(temp1);
            System.out.println(m1);
            while(m1.find()) {
                String result = m1.group();
                System.out.println(result);
                alos = result;

            }
            Matcher m2  = p.matcher(temp2);
            System.out.println(m2);
            while(m2.find()) {
                String result = m2.group();
                System.out.println(result);
                alod = result;
            }
        } else {
            return Result.fail("文件名格式错误");
        }

        String format = sdf.format(new Date());
        File folder = new File("D:/eye/highmyopiafront/public/img/PDF/" + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // 对上传的文件重命名，避免文件重名
        String oldName = file.getOriginalFilename();
        file.transferTo(new File(folder, oldName));
        pdDoc.close();
        String needDelete = "D:/eye/highmyopia/";
        File delete = new File(needDelete + oldName);
        System.out.println(delete.getPath());
        if(delete.exists()) {
            delete.delete();
        }
        else {
            System.out.println("文件不存在");
        }
        String filePath = "/img/PDF/" + format + oldName;

        //          数据库examdetail插入
        Examdetail newExam = new Examdetail();
        newExam.setExamId(oldName);
        System.out.println("eaxmid = " + newExam.getExamId());
        newExam.setPath(filePath);
        newExam.setType("PDF");
        newExam.setDownfile(1);
        examdetailService.saveOrUpdate(newExam);

        return Result.succ(MapUtil.builder()
                .put("filepath", filePath)
                .put("examid", oldName)
                .put("alod", alod)
                .put("alos", alos)
                .map());
    }
}

/*

String format = sdf.format(new Date());
        File folder = new File("D:/eye/highmyopiafront/public/img/PDF/" + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // 对上传的文件重命名，避免文件重名
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString()
                + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
//             文件保存
            file.transferTo(new File(folder, oldName));

            // 返回上传文件的访问路径
            String filePath = request.getScheme() + "://" + request.getServerName()
                   + ":" + request.getServerPort() + '/'  + format + oldName;
            String filePath = "/img/PDF/" + format + oldName;
            return Result.succ(filePath);

        } catch (IOException e) {
                        throw new RuntimeException();
            return Result.fail(e.toString());
        }
        return Result.succ(null);
 */
