package com.zcc.highmyopia.util;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @ClassName PDFToImg
 * @Description
 * @Author aigao
 * @Date 2024/12/22 20:03
 * @Version 1.0
 */
public class PDFToImg {

    public static final String PNG = "png";
    /**
     * @Description dpi 越大图片越清晰， 但耗时
     * */
    public static final Integer DPI_HIGH = 300;
    public static final Integer DPI_MID = 200;
    public static final Integer DPI_LOW = 100;
    /**
     * @Description pdf转成多张图片
     * */
    public static void pdfToImages(String filePath, String destPath, String fileName, String type, int dpi){
        File file = new File(filePath , fileName);
        try{
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                int dot = file.getName().lastIndexOf('.');
                ImageIO.write(image, type, new File(destPath, fileName.substring(0,dot)+ "_" + (i + 1) + "." + type));
            }
        } catch (InvalidPasswordException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @Description pdf转成一张图片
     * */
    public static void pdf2Image(String filePath, String destPath, String fileName, String type, int dpi) throws IOException {
        File file = new File(filePath, fileName);
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            int pageCount = pdDocument.getNumberOfPages();
            //PdfReader reader = new PdfReader(Paths.get(filePath,fileName).toString());
            // 将每页图片存储在列表中
            BufferedImage[] images = new BufferedImage[pageCount];
            int totalHeight = 0;
            int width = 0;

            // 遍历每一页，将其渲染为BufferedImage，并计算总高度和宽度
            for (int i = 0; i < pageCount; i++) {
                images[i] = pdfRenderer.renderImageWithDPI(i, dpi);
                totalHeight += images[i].getHeight();
                // 获取最宽的一页的宽度
                width = Math.max(width, images[i].getWidth());
            }
            // 创建最终合并的图片
            BufferedImage combinedImage = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = combinedImage.getGraphics();
            // 将每页图片画在最终图片上
            int currentHeight = 0;
            for (BufferedImage image : images) {
                g.drawImage(image, 0, currentHeight, null);
                currentHeight += image.getHeight();
            }
            g.dispose();
            int dot = file.getName().lastIndexOf('.');
            // 将合并的图片保存为文件
            ImageIO.write(combinedImage, type, new File(destPath, fileName.substring(0,dot) + "." + type));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally {
            // 关闭PDF文档
            pdDocument.close();
        }
    }

    public static void main(String[] args) throws IOException {
        File destPath = new File("E:\\Download\\project\\test");
        if(!destPath.exists()){
            destPath.mkdirs();
        }
        pdf2Image("E:\\Download\\project","E:\\Download\\project\\test","xajd_grpyjh.pdf",PNG, DPI_MID);
    }

}
