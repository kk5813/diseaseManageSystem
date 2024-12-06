package com.zcc.highmyopia.util;

import org.springframework.context.annotation.Configuration;

import java.sql.*;

/**
 *
 * 第七步  ConnectPatientAndCaselist 同步患者表和病历表的only_id
 * 下一步  ConnectTestyandiAndCaselist
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class ConnectPatientAndCaselist {
//    @Scheduled(cron = "0 0 20 * * ?")
    private void connectPatientAndCase ()   {
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

                Statement statement2;

                try {
                    //加载驱动程序
                    Class.forName(driver1);
                    //1.getConnection()方法，连接MySQL数据库！！
                    con1 = DriverManager.getConnection(url1, user1, password1);
                    if (!con1.isClosed())
                        System.out.println("成功连接到本地数据库!");

                    String sql3 = "select * from patient";
                    String sql4 = "update caselist set only_id = ? , patient_name = ? where patient_id = ?";
                    statement3 = con1.createStatement();
                    ResultSet rs2 = statement3.executeQuery(sql3);
                    statement4 = con1.prepareStatement(sql4);
                    while (rs2.next()) {
                        statement4.setString(1, rs2.getString(10));
                        statement4.setString(3, rs2.getString(2));
                        statement4.setString(2, rs2.getString(3));
                        statement4.executeUpdate();
                    }
                    statement3.close();
                    statement4.close();
                    rs2.close();
                    con1.close();
                    System.out.println("连接已关闭");
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
