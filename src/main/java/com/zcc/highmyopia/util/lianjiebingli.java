package com.zcc.highmyopia.util;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 第二步  lianjiebingli his数据库到病历表caselist
 * 下一步  ConnectIOL
 */

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class lianjiebingli {
//    @Scheduled(cron = "0 0 11 * * ?")
    private void lianjieCaselist() {
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
                //声明oracle对象
                Connection con2;
                //驱动程序名
                String driver2 = "oracle.jdbc.driver.OracleDriver";
                //URL指向要访问的数据库名
                String url2 = "jdbc:oracle:thin:@10.12.5.70:1521/dbm";
                //MySQL配置时的用户名
                String user2 = "his_aeyk";
                //MySQL配置时的密码
                String password2 = "123456";
                //遍历查询结果集

                Statement statement2;

                try {
                    //加载驱动程序
                    Class.forName(driver1);
                    //1.getConnection()方法，连接MySQL数据库！！
                    con1 = DriverManager.getConnection(url1, user1, password1);
                    if (!con1.isClosed())
                        System.out.println("成功连接到本地数据库!");


                    try {
                        //加载驱动程序
                        Class.forName(driver2);
                        //1.getConnection()方法，连接ORAClE数据库！！
                        con2 = DriverManager.getConnection(url2, user2, password2);
                        if (!con2.isClosed())
                            System.out.println("成功连接到oracle数据库!");


                        //2.创建statement类对象，用来执行SQL语句！！
                        statement2 = con2.createStatement();
                        //要执行的SQL语句
                        String sql3 = "select * from VIEW_GDJS_hzxx_ZL";
                        //3.ResultSet类，用来存放获取的结果集！！
                        Statement statement4 = con2.createStatement();
                        ResultSet rs3 = statement4.executeQuery(sql3);


                        //要执行的SQL语句
                        String sql4 = "insert into caselist(patient_id,eyesightOD,eyesightOS,iOPOD,iOPOS,checktime,wanshan,retCAMOS,retCAMresult,retCAMremarks,diagnosis,recommend) select ?,?,?,?,?,?,?,?,?,?,?,? from dual where not exists(select * from caselist where patient_id = ? and checktime = ?)";
                        PreparedStatement statement3 = con1.prepareStatement(sql4);

//                        String sql6 = "update caselist set diagnosis = ? , recommend = ? where patient_id = ?";
//                        statement6 = con1.prepareStatement(sql6);
                        while (rs3.next()) {
//                            System.out.println("1:" + rs3.getString(1)+ "/");
//                            System.out.println("5:" + rs3.getString(5)+ "/");
//                            System.out.println("6:" + rs3.getString(6)+ "/");
//                            System.out.println("3:" + rs3.getString(3)+ "/");
//                            System.out.println("4:" + rs3.getString(4)+ "/");
//                            System.out.println("2:" + rs3.getString(2)+ "/");
//                            System.out.println("9:" + rs3.getString(9)+ "/");
//                            System.out.println("10:" + rs3.getString(10)+ "/");
//                            System.out.println("12:" + rs3.getString(12)+ "/");
//                            System.out.println("13:" + rs3.getString(13)+ "/");
//                            System.out.println("14:" + rs3.getString(14)+ "/");
//                            System.out.println("1:" + rs3.getString(1)+ "/");
                            // 9现病史
                            // 10既往史
                            // 11个人史
                            // 12体格检查
                            // 13诊断
                            // 14治疗建议

                            statement3.setString(1, rs3.getString(1));
                            statement3.setString(2, rs3.getString(5));
                            statement3.setString(3, rs3.getString(6));
                            if(rs3.getString(3)!=null && rs3.getString(3).matches("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"))
                                statement3.setDouble(4, Double.parseDouble(rs3.getString(3)));
                            else
                                statement3.setDouble(4, 0);
                            if(rs3.getString(4)!=null && rs3.getString(4).matches("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"))
                                statement3.setDouble(5, Double.parseDouble(rs3.getString(4)));
                            else
                                statement3.setDouble(5, 0);
                            statement3.setString(6, rs3.getString(2));
                            if("".equals(rs3.getString(13))) {
                                statement3.setInt(7, 0);
                            } else {
                                statement3.setInt(7, 1);
                            }

                            statement3.setString(8, rs3.getString(9));
                            statement3.setString(9, rs3.getString(10));
                            statement3.setString(10, rs3.getString(12));
                            statement3.setString(11, rs3.getString(13));
                            statement3.setString(12, rs3.getString(14));
                            statement3.setString(13, rs3.getString(1));
                            Date time = rs3.getDate(2);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            statement3.setString(14, sdf.format(time));
                            statement3.executeUpdate();

                        }

                        rs3.close();

                        //关闭链接
                        //同步病人信息
//                        statement1.close();
                        System.out.println("连接已关闭");
                        //获取ORACLE的数据信息
                        statement2.close();
//                        //同步病历信息
                        statement3.close();
                        statement4.close();
                        //关2个数据库的连接
                        con1.close();
                        con2.close();
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
