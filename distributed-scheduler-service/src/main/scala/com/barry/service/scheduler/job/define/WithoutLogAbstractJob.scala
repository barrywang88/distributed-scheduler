package com.barry.service.scheduler.job.define

import org.springframework.transaction.TransactionStatus

/**
  * 类功能描述：每日报表生成Job父类
  *
  * @author WangXueXing create at 18-11-15 下午9:13
  * @version 1.0.0
  */
trait WithoutLogAbstractJob extends AbstractJob{
  /**
    * 成功日志记录
    * @param logId
    */
  override def successLog(logId: Long): Unit = {
    // none
  }

  /**
    * 失败日志记录
    * @param logId
    */
  override def failureLog(logId: Long, e: Throwable): Unit = {
    // none
  }
}
