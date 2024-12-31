package com.zcc.highmyopia.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
//import com.sun.org.apache.regexp.internal.RE;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.OrderDetail;
import com.zcc.highmyopia.po.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {

    private String patientName;  // 患者姓名
    private String deptName;     // 科室名称
    private String regNumber;    // 挂号编号
    private String doctorName;   // 医生姓名
    private String recipeNumber; // 处方编号
    private Integer recipeType;      // 处方类型
    private Long patientId;      // 患者ID
    private String billingTime;  // 开方时间
    private Long id;             // 处方ID
    private List<OrderDetail> orderDetail; // 处方（医嘱）信息

    public static RecipeEntity poToVo(Recipe recipe, String deptName, String doctorName, String patientName, List<OrderDetail> orderDetail){
        RecipeEntity build = RecipeEntity.builder().patientId(recipe.getPatientId())
                .id(recipe.getId())
                .recipeNumber(recipe.getRecipeNumber())
                .recipeType(recipe.getRecipeType())
                .billingTime(recipe.getBillingTime().toString())
                .regNumber(recipe.getRegNumber())
                .orderDetail(orderDetail)
                .deptName(deptName)
                .doctorName(doctorName)
                .patientName(patientName)
                .build();
        return build;
    }

}
