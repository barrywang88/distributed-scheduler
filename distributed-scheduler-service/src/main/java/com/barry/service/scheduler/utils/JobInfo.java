package com.barry.service.scheduler.utils;

import java.lang.annotation.*;

/**
 * 类功能描述：job 信息注解
 *
 * @author WangXueXing create at 19-5-4 上午9:14
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JobInfo {
    /**
     * job id
     * @return
     */
    String jobId();

    /**
     * job name
     * @return
     */
    String jobName();

    /**
     * default cron
     * @return
     */
    String defaultCron();
}
