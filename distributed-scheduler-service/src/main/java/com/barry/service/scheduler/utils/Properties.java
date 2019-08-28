package com.barry.service.scheduler.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 所有配置文件属性值获取
 * @author Charsel zhang create at 2019/5/17 16:54
 * @version 0.0.1
 */
@Component
public class Properties {
    @Value("${DEFAULT_ACQUISITION_MONTH_INTERVAL}")
    public  void setDefaultAcquisitionMonthInterval(String defaultAcquisitionMonthInterval) {
        DEFAULT_ACQUISITION_MONTH_INTERVAL = defaultAcquisitionMonthInterval;
    }

    public static String getDefaultAcquisitionMonthInterval() {
        if(DEFAULT_ACQUISITION_MONTH_INTERVAL == null || DEFAULT_ACQUISITION_MONTH_INTERVAL.trim().equals("")){
            return "1";
        }
        return DEFAULT_ACQUISITION_MONTH_INTERVAL;
    }
    //默认分区时前后范围的月份数
    private static String DEFAULT_ACQUISITION_MONTH_INTERVAL;
}


