namespace java com.barry.api.scheduler.request
include 'distributed_scheduler_enum.thrift'

/**
* 手动触发定时任务请求体
**/
struct ManualTriggerTaskRequest {
  /***
  * 定时任务名称
  **/
  1: string processName,
  /***
    * 每天处理时间(固定格式:hh-mm)
    **/
  2: string processTime,
}

/**
* 根据名称启停定时任务请求体
**/
struct StartOrStopByNameRequest {
  /***
  * 定时任务名称
  **/
  1: string processName,
  /**
  * 启停标志
  **/
  2: distributed_scheduler_enum.TScheduledTaskIsStartEnum flag,
  /***
    * 每天处理时间(固定格式:hh-mm)
    **/
   3: string processTime,
}

/**
* 发送资金通知邮件请求体
**/
struct CapitalNoticeRequest {
  /***
  * 资金通知ID
  **/
  1: optional i64 capitalNoticeId,
}