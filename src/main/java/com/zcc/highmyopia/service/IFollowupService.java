package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.FollowupPatientVO;
import com.zcc.highmyopia.po.Followup;
import com.baomidou.mybatisplus.extension.service.IService;

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


    public IPage<FollowupPatientVO> SearchFollowPatient(String patientId, String dateStart, String dateEnd, Integer visitResult, int pageNo, int pageSize);

}
