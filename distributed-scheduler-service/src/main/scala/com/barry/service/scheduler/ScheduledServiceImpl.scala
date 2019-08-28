package com.barry.service.scheduler

import com.barry.api.scheduler.scala.request._
import com.barry.api.scheduler.scala.response.ServiceResponse
import com.barry.api.scheduler.scala.service.ScheduledService
import com.barry.service.scheduler.action._
import com.barry.service.scheduler.job.define.JobEnum
import org.springframework.transaction.annotation.Transactional

/**
  * 财务定时任务服务
  *
  * @author BarryWang create at 2018/5/10 11:55
  * @version 0.0.1
  */
@Transactional(rollbackFor = Array(classOf[Exception]),value ="taskTransactionManager")
class ScheduledServiceImpl extends ScheduledService{
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
  override def startOrStopByName(request: StartOrStopByNameRequest): ServiceResponse ={
    new StartOrStopByNameAction(request).execute
  }

  /**
    *
    **
    * # 资产提示邮件发送
    * ## 业务描述
    * 资产提示邮件发送
    * ## 接口依赖
    * 无
    * ## 边界异常说明
    * 无
    * ## 输入
    *1.finance_task_request.CapitalNoticeRequest
    * ## 前置检查
    *1.无
    * ##  逻辑处理
    * 无
    * ## 数据库变更
    * 无
    * ##  事务处理
    * ##  输出
    *1.service_response.ServiceResponse
    *
    **/
  @Transactional(rollbackFor = Array(classOf[Exception]))
  override def capitalNoticeSendEmail(request: CapitalNoticeRequest): ServiceResponse = {
    new CapitalNoticeSendEmailAction(request).execute
  }

  /**
    *
    **
    * #获取常规定时任务枚举
    * ## 业务描述
    * 获取常规定时任务枚举
    * ## 接口依赖
    * 无
    * ## 边界异常说明
    * 无
    * ## 输入
    * 无
    * ## 前置检查
    * 无
    * ##  逻辑处理
    * 无
    * ## 数据库变更
    * 无
    * ##  事务处理
    * 无
    * ##  输出
    *1.map<string, string>
    *
    **/
  @Transactional(readOnly = true)
  override def getAllCommonTaskEnum(): Map[String, String] = {
    JobEnum.jobIdNameMap
  }

  /**
    *
    **
    * # 手动触发定时任务
    * ## 业务描述
    * 手动触发定时任务
    * ## 接口依赖
    * 无
    * ## 边界异常说明
    * 无
    * ## 输入
    *1.finance_task_request.ManualTriggerTaskRequest
    * ## 前置检查
    *1.无
    * ##  逻辑处理
    * 无
    * ## 数据库变更
    * 无
    * ##  事务处理
    * ##  输出
    *1.service_response.ServiceResponse
    *
    **/
  override def manualTriggerTask(request: ManualTriggerTaskRequest): ServiceResponse = {
    new ManualTriggerTaskAction(request).execute
  }
}
