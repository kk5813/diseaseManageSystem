package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.common.dto.ListFollowup;
import com.zcc.highmyopia.entity.Followup;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangyue
 * @since 2021-03-04
 */
@Mapper
@Component
public interface FollowupMapper extends BaseMapper<Followup> {

//    @Select("select * from followup " +
//            "where TO_DAYS(followup.plan_visit_date) = TO_DAYS(NOW()) " +
//            "and visit_date is null " +
//            "")
    @Select("select followup.id, case_id, followup.patient_id, plan_visit_date, visit_plan, visit_result, visit_content, " +
            "visit_remark, visit_date, patient_name, gender, telephone " +
            "from followup join patient on patient.patient_id=followup.patient_id " +
            "where TO_DAYS(followup.plan_visit_date) = TO_DAYS(NOW()) " +
            "and visit_date is null ")
    List<ListFollowup> selectToDayFollowUpList();

//    @Select("select * from followup " +
//            "where TO_DAYS(followup.plan_visit_date) < TO_DAYS(NOW()) " +
//            "and visit_date is null")
    @Select("select followup.id, case_id, followup.patient_id, plan_visit_date, visit_plan, visit_result, visit_content, " +
        "visit_remark, visit_date, patient_name, gender, telephone " +
        "from followup join patient on patient.patient_id=followup.patient_id " +
        "where TO_DAYS(followup.plan_visit_date) < TO_DAYS(NOW()) " +
        "and visit_date is null ")
    List<ListFollowup> selectOverdueFollowUpList();

//    @Select("select * from followup where visit_date is null order by plan_visit_date DESC")
    @Select("select followup.id, case_id, followup.patient_id, plan_visit_date, visit_plan, visit_result, visit_content, " +
            "visit_remark, visit_date, patient_name, gender, telephone " +
            "from followup join patient on patient.patient_id=followup.patient_id " +
            "where visit_date is null ")
    List<ListFollowup> selectUndoFollowUpList();


    @Select("select followup.id, case_id, followup.patient_id, plan_visit_date, visit_plan, visit_result, visit_content, " +
            "visit_remark, visit_date, patient_name, gender, telephone " +
            "from followup join patient on patient.patient_id=followup.patient_id " +
            "where TO_DAYS(followup.plan_visit_date) = TO_DAYS(NOW()) + 1 " +
            "and visit_date is null ")
    List<ListFollowup> selectTomorrowFollowUpList();
}
