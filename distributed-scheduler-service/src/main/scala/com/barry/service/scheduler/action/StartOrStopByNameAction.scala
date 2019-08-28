package com.barry.service.scheduler.action

import com.barry.api.scheduler.scala.enums.{TScheduledTaskHasDeletedEnum, TScheduledTaskIsStartEnum}
import com.barry.api.scheduler.scala.request.StartOrStopByNameRequest
import com.barry.api.scheduler.scala.response.ServiceResponse
import com.barry.service.scheduler.common.Action
import com.barry.service.scheduler.common.Assert.assert
import com.barry.service.scheduler.common.CommonException.illegalArgumentException
import com.barry.service.scheduler.action.sql.ScheduledTaskActionSql
import com.barry.service.scheduler.dto.TScheduledTask
import com.barry.service.scheduler.job.define.JobEnum
import com.barry.service.scheduler.query.sql.ScheduledTaskQuerySql
import com.barry.service.scheduler.util.{CheckUtil, CronConverter, QuartzManager}

/**
  * @description: 启停定时任务
  * @author zhangc
  * @date 2018\8\1 0001 15:02
  * @version 1.0.0
  */
class StartOrStopByNameAction (request: StartOrStopByNameRequest)  extends Action[ServiceResponse] {
  override def preCheck: Unit = {
    assert(JobEnum.jobIdNameMap.contains(request.processName), illegalArgumentException("定时任务不存在!"))
    assert(!TScheduledTaskIsStartEnum.isUndefined(request.flag.id), illegalArgumentException("错误的启停标志!"))
  }

  /**
    * 根据传入的定时任务名称和启停标志，来判断启动或者停止定时任务
    * 如果是1则更新数据库，删除定时任务，重新添加定时任务
    * 如果是0则更新数据库，删除定时任务
    * @return
    */
  override def action: ServiceResponse = {
    val scheduledTask = ScheduledTaskQuerySql.queryByJobId(request.processName)
    val cron = CheckUtil.isValidHourMinute(request.processTime) match {
      case true => CronConverter.convertHourMinuteToCron(request.processTime)
      case false => {
        CronConverter.isValidExpression(request.processTime) match {
          case true => request.processTime
          case false => ""
        }
      }
    }
    //cron表达式不合法
    if(cron.isEmpty){
      return ServiceResponse("201","the cron expression error")
    }
    val jobInfo = JobEnum.jobIdInfoMap(request.processName)
    //修改定时任务时间, 保存入库
    if(scheduledTask.isEmpty){
      ScheduledTaskActionSql.insertTScheduledTask(
        TScheduledTask(jobInfo.jobName,
          request.processName,
          cron,
          None,
          request.flag.id,
          None,
          null,
          null,
          TScheduledTaskHasDeletedEnum.NO.id))
    } else {
      ScheduledTaskActionSql.updateTaskIsStart(request.flag.id,cron, request.processName)
    }
    request.flag match {
      case TScheduledTaskIsStartEnum.YES =>  QuartzManager.modifyJobTime(request.processName, cron, JobEnum.jobIdClassMap(jobInfo.jobId()))
      case _ =>  QuartzManager.removeJob(request.processName)
    }

    ServiceResponse("200","success")
  }
}
