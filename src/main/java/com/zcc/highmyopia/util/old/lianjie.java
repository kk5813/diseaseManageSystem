package com.zcc.highmyopia.util.old;

import com.zcc.highmyopia.po.old.Caselist;
import com.zcc.highmyopia.po.old.Examdetail;
import com.zcc.highmyopia.mapper.old.CaselistMapper;
import com.zcc.highmyopia.mapper.old.ExamdetailMapper;
import com.zcc.highmyopia.pool.ConnectionBean;
import com.zcc.highmyopia.pool.DBConnectionPool;
import com.zcc.highmyopia.service.old.CaselistService;
import com.zcc.highmyopia.service.old.ExamdetailService;
import com.zcc.highmyopia.util.PdfRead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * 第一步  lianjie his数据库到患者表patient
 * 下一步  lianjiebingli
 */

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class lianjie {
    @Autowired
    ExamdetailMapper examdetailMapper;

    @Autowired
    ExamdetailService examdetailService;

    @Autowired
    CaselistMapper caselistMapper;

    @Autowired
    CaselistService caselistService;

    @Scheduled(cron = "0 15 13 * * ?")
    public void lianjiePatient() throws SQLException {
        log.info("进入连接部分");
        // 1. 从连接池获得数据库连接
        ConnectionBean localConnectionPool = new DBConnectionPool("/local-connection.properties");
        ConnectionBean remoteConnectionPool = new DBConnectionPool("/remote-connection.properties");
        ConnectionBean pacsConnectionPool = new DBConnectionPool("/pacs-connection.properties");
        Connection localConnection = localConnectionPool.getConnection();
        Connection remoteConnection = remoteConnectionPool.getConnection();
        Connection pacsConnection = pacsConnectionPool.getConnection();
        try {
            if (!localConnection.isClosed()) {
                log.info("成功连接到本地数据库");
            }
            if (!remoteConnection.isClosed()) {
                log.info("成功连接到医院Oracle数据库");
            }
            log.info("1. 开始同步患者表");
            syncPatient(localConnection, remoteConnection);
            log.info("1. 患者表同步完成");
            log.info("----------------");

            log.info("2. 开始同步病历表");
            syncCaselist(localConnection, remoteConnection);
            log.info("2. 病历表同步完成");
            log.info("----------------");

            log.info("3. 开始IOL数据");
            syncIOL(localConnection, remoteConnection);
            log.info("3. IOL数据同步完成");
            log.info("----------------");

            log.info("4. 开始同步OCT数据");
            syncOCT(localConnection, remoteConnection);
            log.info("4. OCT数据同步完成");
            log.info("----------------");

            log.info("5. 开始同步OPT数据");
            syncOPT(localConnection, remoteConnection);
            log.info("5. OPT数据同步完成");
            log.info("----------------");

            log.info("6. 开始同步PACS数据");
            syncPacs(localConnection, pacsConnection);
            log.info("6. PACS数据同步完成");
            log.info("----------------");

            log.info("7. 开始同步患者表和病历表");
            syncPatientAndCase(localConnection);
            log.info("7. 患者表和病历表同步完成");
            log.info("----------------");

            log.info("8. 开始同步testyandi和病历表");
            syncTestyandiAndCase(localConnection);
            log.info("8. testyandi和病历表同步完成");
            log.info("----------------");

            log.info("9. 开始同步testyandi和examdetail");
            syncExamdetailAndCase(localConnection);
            log.info("9. 病历表同步完成");
            log.info("----------------");

            log.info("10. 开始下载图片");
            int count = downloadPacs();
            log.info("10. 病历表同步完成, 共下载" + count + "张图片");
            log.info("----------------");

            log.info("");
            log.info("定时任务执行完毕");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                localConnection.close();
                remoteConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 第一步：同步病人表
     * @param localConnection
     * @param remoteConnection
     */
    private void syncPatient(Connection localConnection, Connection remoteConnection) {
        try {
            // 2.
            PreparedStatement localStatement;
            Statement remoteStatement = remoteConnection.createStatement();
            //要执行的SQL语句
            String sql = "select * from VIEW_GDJS_hzxx";
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = remoteStatement.executeQuery(sql);
            log.info(rs.toString());
            String sql2 = "insert into patient(patient_id,patient_name,gender,address,birthday,unit,idcard,telephone,only_id) select ?,?,?,?,?,?,?,?,? from dual where not exists(select * from patient where only_id = ?)";
            localStatement = localConnection.prepareStatement(sql2);
            while (rs.next()) {
                localStatement.setString(1, rs.getString(1));
                localStatement.setString(2, rs.getString(3));
                localStatement.setString(3, rs.getString(4));
                localStatement.setString(4, rs.getString(9));
                localStatement.setString(5, rs.getString(6));
                localStatement.setString(6, rs.getString(8));
                localStatement.setString(7, rs.getString(7));
                localStatement.setString(8, rs.getString(11));
                localStatement.setString(9, rs.getString(13));
                localStatement.setString(10, rs.getString(13));
                localStatement.executeUpdate();
            }
            rs.close();
            localStatement.close();
            remoteStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第二步：同步病历表
     * @param localConnection
     * @param remoteConnection
     */
    private void syncCaselist(Connection localConnection, Connection remoteConnection) {
        try {
            Statement remoteStatement = remoteConnection.createStatement();
            String selectSql = "select * from VIEW_GDJS_hzxx_ZL";
            ResultSet resultSet = remoteStatement.executeQuery(selectSql);
            String insertSql = "insert into caselist(patient_id,eyesightOD,eyesightOS,iOPOD,iOPOS,checktime,wanshan,retCAMOS,retCAMresult,retCAMremarks,diagnosis,recommend) select ?,?,?,?,?,?,?,?,?,?,?,? from dual where not exists(select * from caselist where patient_id = ? and checktime = ?)";
            PreparedStatement localStatement = localConnection.prepareStatement(insertSql);
            while(resultSet.next()) {
                // 9现病史     10既往史       11个人史       12体格检查      13诊断        14治疗建议
                localStatement.setString(1, resultSet.getString(1));
                localStatement.setString(2, resultSet.getString(5));
                localStatement.setString(3, resultSet.getString(6));
                if(resultSet.getString(3)!=null && resultSet.getString(3).matches("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"))
                    localStatement.setDouble(4, Double.parseDouble(resultSet.getString(3)));
                else
                    localStatement.setDouble(4, 0);
                if(resultSet.getString(4)!=null && resultSet.getString(4).matches("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"))
                    localStatement.setDouble(5, Double.parseDouble(resultSet.getString(4)));
                else
                    localStatement.setDouble(5, 0);
                localStatement.setString(6, resultSet.getString(2));
                if("".equals(resultSet.getString(13))) {
                    localStatement.setInt(7, 0);
                } else {
                    localStatement.setInt(7, 1);
                }
                localStatement.setString(8, resultSet.getString(9));
                localStatement.setString(9, resultSet.getString(10));
                localStatement.setString(10, resultSet.getString(12));
                localStatement.setString(11, resultSet.getString(13));
                localStatement.setString(12, resultSet.getString(14));
                localStatement.setString(13, resultSet.getString(1));
                Date time = resultSet.getDate(2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                localStatement.setString(14, sdf.format(time));
                localStatement.executeUpdate();
            }

            resultSet.close();
            localStatement.close();
            remoteStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三步：同步IOL数据
     * @param localConnection
     * @param remoteConnection
     */
    private void syncIOL(Connection localConnection, Connection remoteConnection) {
        try {
            PreparedStatement localStatement;
            Statement remoteStatement = remoteConnection.createStatement();
            //OCT的EXAMINE_ITEM_CODE黄斑、前节、白内障、青光眼视盘
            String selectSql = "select * from HIS_AEYK.V_MTW_EXAM where COST_STATUS = '计费' and (EXAMINE_ITEM_CODE = '310300045C' or EXAMINE_ITEM_CODE = '310300045F' or EXAMINE_ITEM_CODE = '20160504042448' or EXAMINE_ITEM_CODE = '20160504042519' or EXAMINE_ITEM_CODE = '20160504042448')";
            ResultSet rs = remoteStatement.executeQuery(selectSql);
            String insertSql = "insert into testyandi(exam_id, only_id) select ?,? from dual where not exists(select * from testyandi where exam_id = ?) and exists(select * from patient where only_id = ?)";
            localStatement = localConnection.prepareStatement(insertSql);
            while(rs.next()) {
                localStatement.setString(1, rs.getString(3));
                localStatement.setString(2, rs.getString(2));
                localStatement.setString(3, rs.getString(3));
                localStatement.setString(4, rs.getString(2));
                localStatement.executeUpdate();
            }
            rs.close();
            localStatement.close();
            remoteStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第四步：同步OCT数据
     * @param localConnection
     * @param remoteConnection
     */
    private void syncOCT(Connection localConnection, Connection remoteConnection) {
        try {
            PreparedStatement localStatement;
            Statement remoteStatement = remoteConnection.createStatement();
            //OCT的EXAMINE_ITEM_CODE黄斑、前节、白内障、青光眼视盘
            String selectSql = "select * from HIS_AEYK.V_MTW_EXAM where COST_STATUS = '计费' and (EXAMINE_ITEM_CODE = '310300064' or EXAMINE_ITEM_CODE = '1216000399' or EXAMINE_ITEM_CODE = '1216000398'  or EXAMINE_ITEM_CODE = '121600408583' or EXAMINE_ITEM_CODE = '121600408584')";
            ResultSet rs = remoteStatement.executeQuery(selectSql);
            //OCT（视盘)
            String insertSql = "insert into testyandi(exam_id, only_id) select ?,? from dual where not exists(select * from testyandi where exam_id = ?) and exists(select * from patient where only_id = ?)";
            localStatement = localConnection.prepareStatement(insertSql);
            while(rs.next()) {
                localStatement.setString(1, rs.getString(3));
                localStatement.setString(2, rs.getString(2));
                localStatement.setString(3, rs.getString(3));
                localStatement.setString(4, rs.getString(2));
                localStatement.executeUpdate();
            }
            rs.close();
            localStatement.close();
            remoteStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第五步：同步OPT数据
     * @param localConnection
     * @param remoteConnection
     */
    private void syncOPT(Connection localConnection, Connection remoteConnection) {
        try {
            PreparedStatement localStatement;
            Statement remoteStatement = remoteConnection.createStatement();
            //OCT的EXAMINE_ITEM_CODE黄斑、前节、白内障、青光眼视盘
            String selectSql = "select * from HIS_AEYK.V_MTW_EXAM where COST_STATUS = '计费' and (EXAMINE_ITEM_CODE = '310300056' or EXAMINE_ITEM_CODE = '310300049' or EXAMINE_ITEM_CODE = '20160504044505' or EXAMINE_ITEM_CODE = '121600408926' or EXAMINE_ITEM_CODE = '121600307684' or EXAMINE_ITEM_CODE = '121600408649' or EXAMINE_ITEM_CODE = '20190510051411' or EXAMINE_ITEM_CODE = '20170524030457')";
            ResultSet rs = remoteStatement.executeQuery(selectSql);
            String insertSql = "insert into testyandi(exam_id, only_id) select ?,? from dual where not exists(select * from testyandi where exam_id = ?) and exists(select * from patient where only_id = ?)";
            localStatement = localConnection.prepareStatement(insertSql);
            while(rs.next()) {
                localStatement.setString(1, rs.getString(3));
                localStatement.setString(2, rs.getString(2));
                localStatement.setString(3, rs.getString(3));
                localStatement.setString(4, rs.getString(2));
                localStatement.executeUpdate();
            }
            rs.close();
            localStatement.close();
            remoteStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第六步： 同步PACS数据
     * @param localConnection
     * @param pacsConnection
     */
    private void syncPacs(Connection localConnection, Connection pacsConnection) {
        try {
            Statement pacsStatement = pacsConnection.createStatement();
            String selectSql = "select * from exam_detail where dev_code = 'EA_IOL1' or dev_code = 'EA_OCT1' or dev_code = 'EA_OPT1' or dev_code = 'EA_OPT2'";
            ResultSet rs = pacsStatement.executeQuery(selectSql);
            String insertSql = "insert ignore into examdetail(id,exam_id ,path ,type,downfile,dev ) select ?,?,?,?,0,? from dual where not exists(select * from examdetail where id = ?) and exists(select * from testyandi where exam_id = ?)";
            PreparedStatement localStatement = localConnection.prepareStatement(insertSql);
            while(rs.next()){
                localStatement.setString(1,rs.getString(1));
                localStatement.setString(2,rs.getString(2));
                localStatement.setString(3,rs.getString(7));
                localStatement.setString(4,rs.getString(8));
                if("EA_OPT1".equals(rs.getString(4))) {
                    localStatement.setString(5, "opt");
                } else if("EA_OPT2".equals(rs.getString(4))) {
                    localStatement.setString(5, "opt");
                } else if("EA_IOL1".equals(rs.getString(4))) {
                    localStatement.setString(5, "iol");
                } else if("EA_OCT1".equals(rs.getString(4))) {
                    localStatement.setString(5, "oct");
                } else {
                    localStatement.setString(5, "");
                }
                localStatement.setString(6,rs.getString(1));
                localStatement.setString(7,rs.getString(2));
                localStatement.executeUpdate();
            }
            rs.close();
            localStatement.close();
            pacsStatement.close();
        } catch (Exception e) {

        }
    }

    /**
     * 第七步：同步患者表和病历表的only_id
     * @param connection
     */
    private void syncPatientAndCase(Connection connection) {
        try {
            Statement selectStatement = connection.createStatement();
            String selectSql = "select * from patient";
            String updateSql = "update caselist set only_id = ? , patient_name = ? where patient_id = ?";
            ResultSet rs = selectStatement.executeQuery(selectSql);
            PreparedStatement insertStatement = connection.prepareStatement(updateSql);
            while(rs.next()) {
                insertStatement.setString(1, rs.getString(10));
                insertStatement.setString(3, rs.getString(2));
                insertStatement.setString(2, rs.getString(3));
                insertStatement.executeUpdate();
            }
            rs.close();
            selectStatement.close();
            insertStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 第八步：同步testyandi和病历表的caseid, 让testyandi知道病历id
     * @param connection
     */
    private void syncTestyandiAndCase(Connection connection) {
        try {
            Statement selectStatement = connection.createStatement();
            String selectSql = "select * from caselist";
            String updateSql = "update testyandi set case_id = ? where only_id = ?";
            ResultSet rs = selectStatement.executeQuery(selectSql);
            PreparedStatement insertStatement = connection.prepareStatement(updateSql);
            while(rs.next()) {
                insertStatement.setString(1, rs.getString(1));
                insertStatement.setString(2, rs.getString(30));
                insertStatement.executeUpdate();
            }
            rs.close();
            selectStatement.close();
            insertStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第九步：同步testyandi和examdetail的caseid, 让examdetail知道病历id，让病历表知道类型
     * @param connection
     */
    private void syncExamdetailAndCase(Connection connection) {
        try {
            Statement selectStatement = connection.createStatement();
            String selectSql = "select * from testyandi";
            String updateSql = "update examdetail set case_id = ? where exam_id = ?";
            ResultSet rs = selectStatement.executeQuery(selectSql);
            PreparedStatement insertStatement = connection.prepareStatement(updateSql);
            while(rs.next()) {
                insertStatement.setString(1, rs.getString(4));
                insertStatement.setString(2, rs.getString(2));
                insertStatement.executeUpdate();
            }
            selectSql = "select * from examdetail";
            String sql = "update caselist set haveiol = ?, haveoct = ?, haveopt = ? where id = ?";
            rs = selectStatement.executeQuery(selectSql);
            PreparedStatement updateStatement = connection.prepareStatement(sql);
            while (rs.next()) {
                updateStatement.setString(1, rs.getString(9));
                if(rs.getString(9) == "iol") {
                    updateStatement.setString(1, "1");
                } else if(rs.getString(9) == "oct") {
                    updateStatement.setString(2, "1");
                } else {
                    updateStatement.setString(3, "1");
                }
                updateStatement.setString(4, rs.getString(2));
                updateStatement.executeUpdate();
            }
            rs.close();
            updateStatement.close();
            insertStatement.close();
            selectStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第十步：下载检查图片
     */
    private int downloadPacs() {
        log.info("执行下载图片任务时间：" + LocalDateTime.now());
        int count = 0;
        List<Examdetail> examdetails = examdetailMapper.getDownloadFileList();
        if (examdetails != null && !examdetails.isEmpty()) {
            for (Examdetail item : examdetails) {
                if ("opt".equals(item.getDev())) {
                    String down_url_path = "http://10.12.5.36/pacs/getDetailFile?detail_id=" + item.getId();
                    String filename = item.getPath().split("\\\\")[item.getPath().split("\\\\").length - 1];
                    //      注意下载到本地的路径
                    DownFile.downloadFile(down_url_path, "C:/Users/Administrator/Desktop/nginx-1.18.0/html/img/OPT/", filename);
                    Examdetail temp = examdetailService.getById(item.getId());
                    temp.setDownfile(1);
                    temp.setLocalpath("/img/OPT/" + filename);
                    examdetailService.saveOrUpdate(temp);
                    count++;
                } else if ("iol".equals(item.getDev())) {
                    String down_url_path = "http://10.12.5.36/pacs/getDetailFile?detail_id=" + item.getId();
                    String filename = item.getPath().split("\\\\")[item.getPath().split("\\\\").length - 1];
                    //      注意下载到本地的路径
                    DownFile.downloadFile(down_url_path, "C:/Users/Administrator/Desktop/nginx-1.18.0/html/img/IOL/", filename);
                    System.out.println(filename);
                    Examdetail temp = examdetailService.getById(item.getId());
                    //      自动调用PDF识别
                    String path = "C:/Users/Administrator/Desktop/nginx-1.18.0/html/img/IOL/" + filename;
                    System.out.println(path);
                    System.out.println(item.getExamId());
                    PdfRead pdfRead = new PdfRead();
                    String[] str;
                    try {
                        str = pdfRead.iolRead(path, item.getExamId());
                        for (Caselist cl : caselistMapper.selectCaseByIOLExamId(item.getExamId())) {
                            cl.setALOD(str[0]);
                            cl.setALOS(str[1]);
                            System.out.println(str[0]);
                            System.out.println(str[1]);
                            caselistService.saveOrUpdate(cl);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    //      调用结束
                    temp.setDownfile(1);
                    temp.setLocalpath("/img/IOL/" + filename);
                    examdetailService.saveOrUpdate(temp);
                    count++;
                } else if("oct".equals(item.getDev())) {
                    String down_url_path = "http://10.12.5.36/pacs/getDetailFile?detail_id=" + item.getId();
                    String filename = item.getPath().split("\\\\")[item.getPath().split("\\\\").length - 1];
                    //      注意下载到本地的路径
                    DownFile.downloadFile(down_url_path, "C:/Users/Administrator/Desktop/nginx-1.18.0/html/img/OCT/", filename);
                    Examdetail temp = examdetailService.getById(item.getId());
                    temp.setDownfile(1);
                    temp.setLocalpath("/img/OCT/" + filename);
                    examdetailService.saveOrUpdate(temp);
                    count++;
                }
            }
        }
        return count;
    }
}