package com.barry.service.scheduler.action

import com.barry.api.scheduler.scala.enums.{TScheduledTaskHasDeletedEnum, TScheduledTaskIsStartEnum}
import com.barry.api.scheduler.scala.request.ManualTriggerTaskRequest
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
  * 类功能描述：手动触发定时任务Action
  *
  * @author WangXueXing create at 19-5-5 上午11:21
  * @version 1.0.0
  */
class ManualTriggerTaskAction(request: ManualTriggerTaskRequest) extends Action[ServiceResponse] {
  override def preCheck: Unit = {
//    assert(CheckUtil.isValidHourMinute(request.processTime), illegalArgumentException("执行时间格式错误!"))
    assert(JobEnum.jobIdNameMap.contains(request.processName), illegalArgumentException("定时任务不存在!"))
  }

  override def action: ServiceResponse = {
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
    val jobClass = JobEnum.jobIdClassMap(request.processName)
    val jobInfo = JobEnum.getJobInfo(jobClass)
    //修改定时任务时间, 保存入库
    val isExist = ScheduledTaskQuerySql.isExists(jobInfo.jobId)
    isExist match {
      case true => ScheduledTaskActionSql.updateTaskCron(jobInfo.jobId, cron)
      case false => ScheduledTaskActionSql.insertTScheduledTask(
        TScheduledTask(jobInfo.jobName,
          jobInfo.jobId,
          cron,
          None,
          TScheduledTaskIsStartEnum.YES.id,
          None,
          null,
          null,
          TScheduledTaskHasDeletedEnum.NO.id))
    }

    QuartzManager.modifyJobTime(jobInfo.jobId, cron, jobClass)
    ServiceResponse("200","success")
  }
}
