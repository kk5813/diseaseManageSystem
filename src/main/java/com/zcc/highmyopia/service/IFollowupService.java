package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.FollowupPatientVO;
import com.zcc.highmyopia.po.Followup;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangyue
 * @since 2021-03-04
 */
public interface IFollowupService extends IService<Followup> {

    public Result SearchFollowup(String patientId, String dateStart, String dateEnd, Integer visitResult, int pageNo, int pageSize);


    public IPage<FollowupPatientVO> SearchFollowPatient( int pageNo, int pageSize, String patientId, String visitNumber, Integer visitResult, String dateStart, String dateEnd,String doctorName, String deptName);
}
