package com.zcc.highmyopia.common.vo;

import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.po.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class PatientsVO {

    private Long total;

    private List<Patients> patients;

}
