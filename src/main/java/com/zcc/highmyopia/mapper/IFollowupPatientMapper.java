package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.vo.FollowupPatientVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Mapper
@Component
public interface IFollowupPatientMapper extends BaseMapper<FollowupPatientVO> {

//    @Select("SELECT followup_id, patient_id, plan_visit_date, visit_plan, visit_result, "
//            + "visit_content, visit_remark, visit_date, patient_name, gender, telephone, id_number "
//            + "FROM followup_patient_view "
//            + "<where>"
//            + "<if test='patientId != null and patientId != \"\"'> AND patient_id = #{patientId}</if>"
//            + "<if test='visitResult != null'>AND visit_result = #{visitResult}</if>"
//            + "<if test='dateStart != null and dateStart != \"\"'>AND TO_DAYS(plan_visit_date) >= TO_DAYS(#{dateStart})</if>"
//            + "<if test='dateEnd != null and dateEnd != \"\"'>AND TO_DAYS(plan_visit_date) <= TO_DAYS(#{dateEnd})</if>"
//            + "</where>")

    @Select("<script>"
            + "SELECT followup_id, patient_id, plan_visit_date, visit_plan, visit_result, "
            + "visit_content, visit_remark, visit_date, patient_name, gender, telephone, id_number "
            + "FROM followup_patient_view "
            + "<where>"
            + "<if test='patientId != null and patientId != \"\"'> AND patient_id = #{patientId}</if>"
            + "<if test='visitResult != null'> AND visit_result = #{visitResult}</if>"
            + "<if test='dateStart != null and dateStart != \"\"'> AND plan_visit_date &gt;= #{dateStart}</if>"
            + "<if test='dateEnd != null and dateEnd != \"\"'> AND plan_visit_date &lt;= #{dateEnd}</if>"
            + "</where>"
            + "</script>")
    Page<FollowupPatientVO> selectFollowupPatientPage(Page<?> page,
                                                      @Param("patientId") String patientId,
                                                      @Param("visitResult") Integer visitResult,
                                                      @Param("dateStart") String dateStart,
                                                      @Param("dateEnd") String dateEnd);


    Page<FollowupPatientVO> selectFollowupPatientPageWithMore(Page<?> page,
                                                              @Param("patientId") String patientId,
                                                              @Param("visitNumber") String visitNumber,
                                                              @Param("visitResult") Integer visitResult,
                                                              @Param("dateStart") String dateStart,
                                                              @Param("dateEnd") String dateEnd,
                                                              @Param("doctorName") String doctorName,
                                                              @Param("deptName") String deptName);
}
