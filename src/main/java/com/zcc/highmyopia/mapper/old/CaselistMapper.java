package com.zcc.highmyopia.mapper.old;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.entity.old.Caselist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangyue
 * @since 2021-02-08
 */
@Mapper
@Component
public interface CaselistMapper extends BaseMapper<Caselist> {

    @Select("select * from caselist where patient_id = #{id} and wanshan = 1")
    List<Caselist> selectPastCaseByPId(@Param("id") Integer id);

    @Select("select * from caselist where patient_id = #{id} and wanshan = 1")
    List<Caselist> selectPastCaseByPatientId(@Param("id") String id);

    @Select("select * from caselist where wanshan = 1 order by checktime DESC ")
    List<Caselist> selectPastCaselist();

    @Select("select * from caselist where wanshan != 1 or wanshan is null order by checktime DESC")
    List<Caselist> selectCaselist();

//    @Select("select * from caselist where IOLMaster = #{iol}")
    @Select("SELECT * from caselist where id in (SELECT case_id from examdetail where dev='iol' and exam_id = #{iolmaster})")
    List<Caselist> selectCaseByIOLExamId(@Param("iolmaster") String iolmaster);

    @Select("select * from caselist where retCAMOD = #{type} order by checktime DESC")
    List<Caselist> selectCaselistHave(@Param("type") String type);

    @Select("select * from caselist where haveiol = 1 and wanshan = 0")
    List<Caselist> selectCaselistHaveIol();

    @Select("select * from caselist where haveoct = 1 and wanshan = 0")
    List<Caselist> selectCaselistHaveOct();

    @Select("select * from caselist where haveopt = 1 and wanshan = 0")
    List<Caselist> selectCaselistHaveOpt();

    @Select("select * from caselist where ALOD is not null or ALOS is not null order by checktime DESC")
    List<Caselist> selectCaselistIOL();

    @Select("select * from caselist where retCAMOD = #{type} and wanshan = 1 order by checktime DESC")
    List<Caselist> selectPastCaselistHave(@Param("type") String type);

    @Select("select * from caselist where haveiol = 1 and wanshan = 1")
    List<Caselist> selectPastCaselistHaveIol();

    @Select("select * from caselist where haveoct = 1 and wanshan = 1")
    List<Caselist> selectPastCaselistHaveOct();

    @Select("select * from caselist where haveopt = 1 and wanshan = 1")
    List<Caselist> selectPastCaselistHaveOpt();

    @Select("select * from caselist where ( ALOD is not null or ALOS is not null ) and (wanshan = 1) order by checktime DESC")
    List<Caselist> selectPastCaselistIOL();

}
