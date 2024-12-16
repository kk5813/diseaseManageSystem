package com.zcc.highmyopia.util.old;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
/**
 *
 * 第五步  ConnectOpT 找欧宝图到testyandi
 * 下一步  ConnectPacs
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class ConnectOPT {
//    @Scheduled(cron = "0 0 17 * * ?")
    private void testYandibing() {
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
        try {
            //加载驱动程序
            Class.forName(driver1);
            //1.getConnection()方法，连接MySQL数据库！！
            con1 = DriverManager.getConnection(url1, user1, password1);
            if (!con1.isClosed())
                System.out.println("成功连接到本地MysQl数据库!");


            try {
                //加载驱动程序
                Class.forName(driver2);
                //1.getConnection()方法，连接ORAClE数据库！！
                con2 = DriverManager.getConnection(url2, user2, password2);
                if (!con2.isClosed())
                    System.out.println("成功连接到PACS数据库!");

                //要执行的SQL语句
                //OCT的EXAMINE_ITEM_CODE黄斑、前节、白内障、青光眼视盘
                String sql = "select * from HIS_AEYK.V_MTW_EXAM where COST_STATUS = '计费' and (EXAMINE_ITEM_CODE = '310300056' or EXAMINE_ITEM_CODE = '310300049' or EXAMINE_ITEM_CODE = '20160504044505' or EXAMINE_ITEM_CODE = '121600408926' or EXAMINE_ITEM_CODE = '121600307684' or EXAMINE_ITEM_CODE = '121600408649' or EXAMINE_ITEM_CODE = '20190510051411' or EXAMINE_ITEM_CODE = '20170524030457')";
                //3.ResultSet类，用来存放获取的结果集！！
                Statement statement2 = con2.createStatement();
                ResultSet rs = statement2.executeQuery(sql);

                //OCT（视盘)
                String sql4 = "insert into testyandi(exam_id, only_id) select ?,? from dual where not exists(select * from testyandi where exam_id = ?) and exists(select * from patient where only_id = ?)";

                PreparedStatement statement1 = con1.prepareStatement(sql4);

                while (rs.next()) {
                    statement1.setString(1, rs.getString(3));
                    statement1.setString(2, rs.getString(2));
                    statement1.setString(3, rs.getString(3));
                    statement1.setString(4, rs.getString(2));
                    statement1.executeUpdate();
                }

                //病历视图的结果
                rs.close();

                //关闭连接
                //同步病历信息
                statement1.close();
                statement2.close();
                //关2个数据库的连接
                con1.close();
                con2.close();
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
