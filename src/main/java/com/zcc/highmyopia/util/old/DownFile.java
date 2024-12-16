package com.zcc.highmyopia.util.old;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
public class DownFile {


    /**
     * 说明：根据指定URL将文件下载到指定目标位置
     *
     * @param urlPath
     *            下载路径
     * @param downloadDir
     *            文件存放目录
     * @return 返回下载文件
     */
    @SuppressWarnings("finally")
    public static File downloadFile(String urlPath, String downloadDir,String fileFullName) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000*5);
            //设置请求方式，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 控制台打印文件大小
            System.out.println("您要下载的文件大小为:" + fileLength / (1024 ) + "KB");

            // 建立链接从请求中获取数据
            URLConnection con = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            // 指定文件名称(有需求可以自定义)
//            String fileFullName = "aaa.apk";
            // 指定存放位置(有需求可以自定义)
            String path = downloadDir + File.separatorChar + fileFullName;
            file = new File(path);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 控制台打印文件下载的百分比情况
//                System.out.println("下载了-------> " + len * 100 / fileLength + "%\n");
            }
            // 关闭资源
            bin.close();
            out.close();
            System.out.println("文件下载成功！");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("文件下载失败！");
        } finally {
            return file;
        }

    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) throws Exception{
//        SpringApplication.run(DownFile.class, args);

//        downFileService.down();
//        SpringApplication.run(DownFile.class, args);
//        ApplicationContext context = SpringUtils.getApplicationContext();
//        ExamdetailMapper examdetailMapper = context.getBean(ExamdetailMapper.class);
//        Map<String,Object> map = new HashMap<>();
//        List<Examdetail> examdetailList = examdetailMapper.getExamDownFileList();
////        List<String> x = new ArrayList<>();
//        if(examdetailList != null && !examdetailList.isEmpty()){
//            for(Examdetail item:examdetailList){
//                if(item.getType().equals("PNG") || item.getType().equals("JPG")){
////                    String input_path = item.getPath();
//                    String down_url_path = "http://10.12.5.36/pacs/getDetailFile?detail_id="+item.getId();
//                    String filename = item.getPath().split("\\\\")[item.getPath().split("\\\\").length-1];
//                    downloadFile(down_url_path,"C:/Users/Administrator/Desktop/1/contentManagerSystem-cms2.0/src/main/webapp/img/images/",filename);
//
////                    String output_path = "E:/contentManagerSystem-cms2.0/src/main/webapp/img/images/"+filename;
////                    ThumbnailUtil thumbnailUtil_cta = new ThumbnailUtil(input_path, output_path);
////                    thumbnailUtil_cta.getNewPath();
////                    x.add("/img/images/"+filename);
//                    examdetailMapper.updateDownFille(item.getId(),"/img/images/"+filename);
//                }
//                else if(item.getType().equals("PDF")){
//                    String filename = item.getPath().split("\\\\")[item.getPath().split("\\\\").length-1];
//                    downloadFile(item.getPath(),"E:/contentManagerSystem-cms2.0/src/main/webapp/img/PDF/",filename);
//                    String input_path = "E:/contentManagerSystem-cms2.0/src/main/webapp/img/PDF/"+filename;
//                    String output_path = "E:/contentManagerSystem-cms2.0/src/main/webapp/img/PDF/"+filename+".png";
//                    ThumbnailUtil thumbnailUtil_cta = new ThumbnailUtil(input_path, output_path);
//                    thumbnailUtil_cta.getThumbnail();
////                    x.add("/img/PDF/"+filename+".png");
//                    examdetailMapper.updateDownFille(item.getId(),"/img/PDF/"+filename+".png");
//                }
//            }
//        }
        // 指定资源地址，下载文件测试
//        downloadFile("http://count.liqucn.com/d.php?id=22709&urlos=android&from_type=web", "B:/myFiles/");
//        String down_url_path = "http://10.12.5.36/pacs/getDetailFile?detail_id="+"2428";
//        String path = "D:\\EyecodePACS\\exam_data\\OCT1\\ExecutorID_1\\20190423ME1133938-1\\doc_report_1.pdf";
//        String filename = path.split("\\\\")[path.split("\\\\").length-1];
//        downloadFile(down_url_path,"C:/Users/Administrator/Desktop/1/contentManagerSystem-cms2.0/src/main/webapp/img/images/",filename);
    }
}
