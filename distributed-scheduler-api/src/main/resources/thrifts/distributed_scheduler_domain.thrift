namespace java com.barry.api.scheduler.domain

include "distributed_scheduler_enum.thrift"
 
/**
*  定时任务体
*/
struct ScheduledTask{
   /**
   * job名称
   */
 1 :  string jobName,
   /**
   * job ID
   */
 2 :  string jobId,
   /**
   * Job cron表达式
   */
 3 :  string jobCron,
   /**
   * Job类型
   */
 4 : optional i32 jobType,
   /**
   * 是否已启动,0:否(no);1:是(yes)
   */
 5 :  distributed_scheduler_enum.TScheduledTaskIsStartEnum isStart,
   /**
   * 备注
   */
 6 : optional string remark,
   /**
   * 更新时间
   */
 7 :  i64 updatedAt,
   /**
   * 创建时间
   */
 8 :  i64 createdAt,
   /**
   * 是否已删除,0:否(no);1:是(yes)
   */
 9 :  distributed_scheduler_enum.TScheduledTaskHasDeletedEnum hasDeleted,
}
