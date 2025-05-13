package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.OrderDetail;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.po.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Mapper
@Component
public interface IOrderDetailMapper extends BaseMapper<OrderDetail> {
    List<OrderDetail> searchOrderDetail(@Param("recipeNumber") String recipeNumber);
}
