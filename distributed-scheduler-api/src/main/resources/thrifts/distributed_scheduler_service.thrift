namespace java com.barry.api.scheduler.service

include 'service_response.thrift'
include 'distributed_scheduler_request.thrift'
include 'insert_date_request.thrift'
/**
* 自动日次定时任务服务
**/
service ScheduledService{

/**
#获取常规定时任务枚举
## 业务描述
   获取常规定时任务枚举
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 前置检查
    无
##  逻辑处理
    无
## 数据库变更
    无
##  事务处理
    无
##  输出
    1.map<string, string>
*/
    map<string, string> getAllCommonTaskEnum(),

/**
#启停定时任务
## 业务描述
   通过传入的定时任务名称和启停标志，来判断启动或者停止定时任务
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    1.finance_task_request.DayTimeProcessRequest
## 前置检查
    1.无
##  逻辑处理
    无
## 数据库变更
    无
##  事务处理
    UPDATE t_scheduled_task SET
               is_start = ${flag},
               updated_at = NOW()
              WHERE job_id = ${jobId}
##  输出
    1.service_response.ServiceResponse
*/
        service_response.ServiceResponse startOrStopByName(distributed_scheduler_request.StartOrStopByNameRequest request),

/**
# 手动触发定时任务
## 业务描述
   手动触发定时任务
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    1.finance_task_request.ManualTriggerTaskRequest
## 前置检查
    1.无
##  逻辑处理
    无
## 数据库变更
    无
##  事务处理
##  输出
    1.service_response.ServiceResponse
*/
    service_response.ServiceResponse manualTriggerTask(1:distributed_scheduler_request.ManualTriggerTaskRequest request),

/**
# 资产提示邮件发送
## 业务描述
   资产提示邮件发送
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    1.finance_task_request.CapitalNoticeRequest
## 前置检查
    1.无
##  逻辑处理
    无
## 数据库变更
    无
##  事务处理
##  输出
    1.service_response.ServiceResponse
*/
    service_response.ServiceResponse capitalNoticeSendEmail(1:distributed_scheduler_request.CapitalNoticeRequest request),
}(group="FinanceTask")