package com.zcc.highmyopia.util.old;

import org.springframework.context.annotation.Configuration;

import java.sql.*;
/**
 *
 * 第九步  ConnectExamdetailAndCaselist 同步testyandi和examdetail的caseid, 让examdetail知道病历id，让病历表知道类型
 * 下一步  DownPacs
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class ConnectExamdetailAndCaselist {
//    @Scheduled(cron = "0 20 21 * * ?")
    private void connectExamAndCase() {
        //声明mysql对象
        Connection con1;
        //驱动程序名
        String driver1 = "com.mysql.cj.jdbc.Driver";
        //URL指向要访问的数据库名
        String url1 = "jdbc:mysql://localhost:3306/highmyopiasystem_db?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
        //MySQL配置时的用户名
        String user1 = "root";
        //MySQL配置时的密码
        String password1 = "2287996531";


        PreparedStatement statement1;
        Statement statement3;
        PreparedStatement statement4;
        Statement statement5;
        PreparedStatement statement6;

        Statement statement2;

        try {
            //加载驱动程序
            Class.forName(driver1);
            //1.getConnection()方法，连接MySQL数据库！！
            con1 = DriverManager.getConnection(url1, user1, password1);
            if (!con1.isClosed())
                System.out.println("成功连接到本地数据库!");

            String sql3 = "select * from testyandi";
            String sql4 = "update examdetail set case_id = ? where exam_id = ?";

            statement3 = con1.createStatement();
            ResultSet rs2 = statement3.executeQuery(sql3);
            statement4 = con1.prepareStatement(sql4);
            while (rs2.next()) {
                // 1 30
                statement4.setString(1, rs2.getString(4));
                statement4.setString(2, rs2.getString(2));
                statement4.executeUpdate();
            }

            String sql5 = "select * from examdetail";
            String sql6 = "update caselist set haveiol = ?, haveoct = ?, haveopt = ? where id = ?";

            statement5 = con1.createStatement();
            ResultSet rs3 = statement3.executeQuery(sql5);
            statement6 = con1.prepareStatement(sql6);
            while (rs2.next()) {
                statement6.setString(1, rs3.getString(9));
                if(rs3.getString(9) == "iol") {
                    statement6.setString(1, "1");
                } else if(rs3.getString(9) == "oct") {
                    statement6.setString(2, "1");
                } else {
                    statement6.setString(3, "1");
                }
                statement6.setString(4, rs3.getString(2));
                statement6.executeUpdate();
            }


            statement3.close();
            statement4.close();
            statement5.close();
            statement6.close();
            rs2.close();
            rs3.close();
            con1.close();
            System.out.println("已关闭连接");
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("对不起,找不到驱动程序!");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //System.out.println("数据库数据成功获取！！");
        }
    }
}
