package com.zcc.highmyopia.util.old;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 第六步  ConnectPacs 连接his到examdetail
 * 下一步  ConnectPatientAndCaselist
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class ConnectPacs {
//    public static void main(String[] args) {
//        Runnable runnable = new Runnable() {
//            public void run() {
//      @Scheduled(cron = "0 0 18 * * ?")
      public void connectPacs() {

//    private void pacsConnect() {
                //声明Connection对象
                Connection con1;
                //驱动程序名
                String driver1 = "com.mysql.cj.jdbc.Driver";
                //URL指向要访问的数据库名
                String url1 = "jdbc:mysql://localhost:3306/highmyopiasystem_db?useAffectedRows=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
                //MySQL配置时的用户名
                String user1 = "root";
                //MySQL配置时的密码
                String password1 = "2287996531";


                //声明Connection对象
                Connection con2;
                //驱动程序名
                String driver2 = "com.mysql.cj.jdbc.Driver";
                //URL指向要访问的数据库名
                String url2 = "jdbc:mysql://10.12.5.36:3306/ec_pacs?useAffectedRows=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
                //MySQL配置时的用户名
                String user2 = "eyecode";
                //MySQL配置时的密码
                String password2 = "s851228R";
                //遍历查询结果集
                try {
                    //加载驱动程序
                    Class.forName(driver1);
                    //1.getConnection()方法，连接MySQL数据库！！
                    con1 = DriverManager.getConnection(url1,user1,password1);
                    if(!con1.isClosed())
                        System.out.println("成功连接到本地数据库!");
                    try {
                        //加载驱动程序
                        Class.forName(driver2);
                        //1.getConnection()方法，连接MySQL数据库！！
                        con2 = DriverManager.getConnection(url2,"eyecode","s851228R");
                        if(!con2.isClosed())
                            System.out.println("成功连接到PACS数据库!");

                        //2.创建statement类对象，用来执行SQL语句！！
                        Statement statement2 = con2.createStatement();
                        //要执行的SQL语句
//                        String sql = "select * from exam_detail order by id  desc LIMIT 10000";
                        String sql = "select * from exam_detail where dev_code = 'EA_IOL1' or dev_code = 'EA_OCT1' or dev_code = 'EA_OPT1' or dev_code = 'EA_OPT2'";
//                        String sql = "select * from exam_detail where dev_code = 'EA_OCT1'";
                        //3.ResultSet类，用来存放获取的结果集！！
                        ResultSet rs = statement2.executeQuery(sql);

                        //要执行的SQL语句
//                        String sql2 = "insert into examdetail(exam_id,path,type,downfile) select ?,?,?,0 from dual where not exists(select * from examdetail where id = ?) and exists(select * from caslist where IOLMaster = ? or oct = ?)";
//                        String sql2 = "insert into examdetail(id,exam_id ,path ,type,downfile ) select ?,?,?,?,0 from dual where not exists(select * from examdetail where exam_id = ?) and exists(select * from caselist where IOLMaster = ? or oct = ?)";
//                        String sql2 = "insert ignore into examdetail(id,exam_id ,path ,type,downfile ) select ?,?,?,?,0 from dual where not exists(select * from examdetail where exam_id = ?) and exists(select * from caselist where IOLMaster = ? or oct = ?)";
                        String sql2 = "insert ignore into examdetail(id,exam_id ,path ,type,downfile,dev ) select ?,?,?,?,0,? from dual where not exists(select * from examdetail where id = ?) and exists(select * from testyandi where exam_id = ?)";
                        PreparedStatement statement1 = con1.prepareStatement(sql2);
                        while(rs.next()){
                            statement1.setString(1,rs.getString(1));
                            statement1.setString(2,rs.getString(2));
                            statement1.setString(3,rs.getString(7));
                            statement1.setString(4,rs.getString(8));
                            if("EA_OPT1".equals(rs.getString(4))) {
                                statement1.setString(5, "opt");
                            } else if("EA_OPT2".equals(rs.getString(4))) {
                                statement1.setString(5, "opt");
                            } else if("EA_IOL1".equals(rs.getString(4))) {
                                statement1.setString(5, "iol");
                            } else if("EA_OCT1".equals(rs.getString(4))) {
                                statement1.setString(5, "oct");
                            } else {
                                statement1.setString(5, "");
                            }
                            statement1.setString(6,rs.getString(1));
                            statement1.setString(7,rs.getString(2));
                            statement1.executeUpdate();
                        }
                        rs.close();

                        //关闭链接
//                        statement1.close();
                        statement2.close();
                        con1.close();
                        con2.close();
                        System.out.println("连接已关闭");
                    } catch(ClassNotFoundException e) {
                        //数据库驱动类异常处理
                        System.out.println("对不起,找不到驱动程序!");
                        e.printStackTrace();
                    } catch(SQLException e) {
                        //数据库连接失败异常处理
                        e.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }finally{
                        // System.out.println("数据库数据成功获取！！");
                    }

                } catch(ClassNotFoundException e) {
                    //数据库驱动类异常处理
                    System.out.println("对不起,找不到驱动程序!");
                    e.printStackTrace();
                } catch(SQLException e) {
                    //数据库连接失败异常处理
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    //System.out.println("数据库数据成功获取！！");
                }
            }
//        };
//        ScheduledExecutorService service = Executors
//                .newSingleThreadScheduledExecutor();
//        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
//        service.scheduleAtFixedRate(runnable, 10, 60*60*6, TimeUnit.SECONDS);


}
