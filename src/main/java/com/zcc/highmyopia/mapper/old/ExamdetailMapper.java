package com.zcc.highmyopia.mapper.old;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.entity.old.Examdetail;
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
 * @since 2021-02-21
 */
@Mapper
@Component
public interface ExamdetailMapper extends BaseMapper<Examdetail> {

    @Select("select localpath from examdetail where exam_id = #{id} and downfile = 1")
    String selectExamdetailByExamId(@Param("id") String id);

    @Select("select * from examdetail where downfile = 0")
    List<Examdetail> getDownloadFileList();

    @Select("select * from examdetail where type = 'PDF' and iolread is null and dev = 'iol'")
    List<Examdetail> getNotReadIOL();

    @Select("select * from examdetail where case_id = #{id} and dev = 'opt'")
    List<Examdetail> getOptByCaseId(@Param("id") Integer id);

    @Select("select * from examdetail where case_id = #{id} and dev = 'oct'")
    List<Examdetail> getOctByCaseId(@Param("id") Integer id);

    @Select("select * from examdetail where case_id = #{id} and dev = 'iol'")
    List<Examdetail> getIolByCaseId(@Param("id") Integer id);



}
