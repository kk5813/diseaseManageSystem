package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.OrderDetail;
import com.zcc.highmyopia.po.Visits;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
public interface IOrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> searchOrderDetail(String recipeNumber);
}
