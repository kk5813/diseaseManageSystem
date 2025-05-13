package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.common.vo.FollowupPatientVO;
import com.zcc.highmyopia.mapper.IFollowupPatientMapper;
import com.zcc.highmyopia.po.Followup;
import com.zcc.highmyopia.mapper.IFollowupMapper;
import com.zcc.highmyopia.service.IFollowupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.util.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangyue
 * @since 2021-03-04
 */
@Service
public class FollowupService extends ServiceImpl<IFollowupMapper, Followup> implements IFollowupService {

    @Autowired
    IFollowupMapper followupMapper;

    @Override
    public Result SearchFollowup(String patientId, String dateStart, String dateEnd, Integer visitResult, int pageNo, int pageSize) {
        LambdaQueryWrapper<Followup> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(patientId)){
            queryWrapper.eq(Followup::getPatientId,patientId);
        }
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now();
        if(StringUtils.isNotBlank(dateStart)){
            try{
                date = LocalDate.parse(dateStart, formatter);
            }catch (DateTimeParseException e) {
                ThrowUtils.throwIf(true,new BusinessException(ResultCode.DATE_VARIABLE_ERROR));
            }
            queryWrapper.ge(Followup::getPlanVisitDate, date);
        }
        LocalDate date1 = LocalDate.now();
        if(StringUtils.isNotBlank(dateEnd)){
            try{
                date1 = LocalDate.parse(dateStart, formatter);
            }catch (DateTimeParseException e) {
                ThrowUtils.throwIf(true,new BusinessException(ResultCode.DATE_VARIABLE_ERROR));
            }
            queryWrapper.le(Followup::getPlanVisitDate, date1);
        }
        if(visitResult !=null){
            queryWrapper.eq(Followup::getVisitResult, visitResult);
        }
        Page<Followup> page = new Page<>(pageNo,pageSize);
        IPage<Followup> followupIpage = followupMapper.selectPage(page,queryWrapper);
        return Result.succ(followupIpage);
    }

    @Autowired
    private IFollowupPatientMapper followupPatientMapper;
    @Override
    public IPage<FollowupPatientVO> SearchFollowPatient( int pageNo, int pageSize, String patientId, String visitNumber, Integer visitResult, String dateStart, String dateEnd,String doctorName, String deptName) {
        Page<FollowupPatientVO> page = new Page<>(pageNo, pageSize);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (StringUtils.isNotBlank(dateStart)) {
            try {
                startDate = LocalDate.parse(dateStart.trim(), formatter);
            } catch (DateTimeParseException e) {
                throw new BusinessException(ResultCode.DATE_VARIABLE_ERROR, "Invalid start date format");
            }
        }

        if (StringUtils.isNotBlank(dateEnd)) {
            try {
                endDate = LocalDate.parse(dateEnd.trim(), formatter);
            } catch (DateTimeParseException e) {
                throw new BusinessException(ResultCode.DATE_VARIABLE_ERROR, "Invalid end date format");
            }
        }
        return followupPatientMapper.selectFollowupPatientPageWithMore(page,patientId,visitNumber,visitResult,startDate == null ? "" : startDate.toString() , endDate == null ? "" : endDate.toString(),doctorName, deptName);
    }
}
