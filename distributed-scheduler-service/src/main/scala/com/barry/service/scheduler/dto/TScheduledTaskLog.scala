package com.barry.service.scheduler.dto

case class TScheduledTaskLog (
 /**
   * 主键
   */
 id : Long,
  /**
    * job ID
    */
  jobId : String,

  /**
    * 定时任务状态,0:已开始(started);1:执行成功(success);2:执行失败(failure)
    */
  status : Int,

  /**
    * 备注
    */
  remark : String,

  /**
    * 更新时间
    */
  updatedAt : java.sql.Timestamp,

  /**
    * 创建时间
    */
  createdAt : java.sql.Timestamp,
)
