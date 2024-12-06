package com.zcc.highmyopia.util;


import com.zcc.highmyopia.mapper.CaselistMapper;
import com.zcc.highmyopia.mapper.ExamdetailMapper;
import com.zcc.highmyopia.service.CaselistService;
import com.zcc.highmyopia.service.ExamdetailService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfRead {
    @Autowired
    ExamdetailService examdetailService;
    @Autowired
    CaselistMapper caselistMapper;
    @Autowired
    CaselistService caselistService;
    @Autowired
    ExamdetailMapper examdetailMapper;
    public String[] iolRead(String path, String examid) throws Exception {
        String[] str = new String[2];
        File iol = new File(path);
        if(iol.exists()) {
            String alod = null;
            String alos = null;
            PDFTextStripper pdfStripper = new PDFTextStripper();
            PDDocument pdDoc = PDDocument.load(iol);
            if("IOL".equals(iol.getName().split("-")[0])){
                String HaigisRegex = ".+[\\s]+(\\u006d\\u006d)(?=\\u0020\\u0028\\u0053\\u004e\\u0052)";
                Pattern p = Pattern.compile(HaigisRegex);
                Matcher m = p.matcher(pdfStripper.getText(pdDoc));
                while(m.find()) {
                    System.out.println(m.group());
                    if(alod != null) alos = m.group();
                    else alod = m.group();
                }

            } else if("MMT".equals(iol.getName().split("-")[0])) {
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
                System.out.println("文件名格式错误");
            }
            str[0] = alod;
            str[1] = alos;
            pdDoc.close();
        } else {
            System.out.println("要读取的文件不存在");
        }
        return str;
    }

    public static void main(String[] args) {
//        File iol = new File("D:/eye/highmyopiafront/public/img/PDF/", "IOL-Haigis_2723.pdf");
        File iol = new File("D:/eye/highmyopiafront/public/img/PDF/", "IOL-Haigis_408.pdf");
        if(iol.exists()) {
            String alod = null;
            String alos = null;
            try{
                PDFTextStripper pdfStripper = new PDFTextStripper();
                PDDocument pdDoc = PDDocument.load(iol);
                if("IOL".equals(iol.getName().split("-")[0])){
//                    String LRRegex = "(\\u0053\\u004e\\u0036\\u0030\\u0057\\u0046)";
//                    String temp1 = pdfStripper.getText(pdDoc).split(LRRegex)[0];
//                    String temp2 = pdfStripper.getText(pdDoc).split(LRRegex)[1];
                    String HaigisRegex = ".+[\\s]+(\\u006d\\u006d)(?=\\u0020\\u0028\\u0053\\u004e\\u0052)";
                    Pattern p = Pattern.compile(HaigisRegex);
                    Matcher m = p.matcher(pdfStripper.getText(pdDoc));
                    while(m.find()) {
                        System.out.println(m.group());
                        if(alod != null) alos = m.group();
                        else alod = m.group();
                    }
//                    Matcher m1  = p.matcher(temp1);
//                    System.out.println(m1);
//                    while(m1.find()) {
//                        String result = m1.group();
//                        System.out.println(result);
//                        alod = result;
//                    }
//                    Matcher m2  = p.matcher(temp2);
//                    System.out.println(m2);
//                    while(m2.find()) {
//                        String result = m2.group();
//                        System.out.println(result);
//                        alos = result;
//                    }

                } else if("MMT".equals(iol.getName().split("-")[0])) {
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
                    System.out.println("文件名格式错误");
                }
                pdDoc.close();
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
