package com.zcc.highmyopia.hospital.service;

import com.zcc.highmyopia.hospital.utils.State;

/**
 * @Author zcc
 * @Date 2024/12/31
 * @Description
 */
public interface IGetDataService {

    void getTodayData();

    void getDataTest(String beginData, String endData);

    State getDataToday();
}
