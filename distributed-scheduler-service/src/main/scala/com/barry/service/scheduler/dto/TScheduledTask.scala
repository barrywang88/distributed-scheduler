package com.barry.service.scheduler.dto

/**
  * @author dapeng-tool
  */
case class TScheduledTask (
   /**
   * job名称
   */
jobName : String,

   /**
   * job ID
   */
jobId : String,

   /**
   * Job cron表达式
   */
jobCron : String,

   /**
   * Job类型
   */
jobType : Option[Int],

   /**
   * 是否已启动,0:否(no);1:是(yes)
   */
isStart : Int,

   /**
   * 备注
   */
remark : Option[String],

   /**
   * 更新时间
   */
updatedAt : java.sql.Timestamp,

   /**
   * 创建时间
   */
createdAt : java.sql.Timestamp,

   /**
   * 是否已删除,0:否(no);1:是(yes)
   */
hasDeleted : Int,

)
