package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IOrderDetailMapper;
import com.zcc.highmyopia.mapper.IVisitsMapper;
import com.zcc.highmyopia.po.OrderDetail;
import com.zcc.highmyopia.po.Visits;
import com.zcc.highmyopia.service.IOrderDetailService;
import com.zcc.highmyopia.service.IVisitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Service
@RequiredArgsConstructor
public class OderDetailService extends ServiceImpl<IOrderDetailMapper, OrderDetail> implements IOrderDetailService {

}