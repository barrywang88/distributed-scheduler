package com.barry.service.scheduler.job

import com.barry.service.scheduler.utils.JobInfo
import com.barry.api.scheduler.scala.request.CapitalNoticeRequest
import com.barry.service.scheduler.action.CapitalNoticeSendEmailAction
import com.barry.service.scheduler.job.define.AbstractJob
import org.quartz.JobExecutionContext

/**
  * 发送预付款邮件和已付款定时任务
  * @author zpluo create at  2018\10\25 0025 15:37
  * @version 1.0.0
  */
@JobInfo(jobId="SEND_EMAIL_PROCESS", jobName="计划付款通知和已付款通知定时任务", defaultCron="0 0 20 * * ?")
class SendEmailProcessJob extends AbstractJob{
  /**
    * start up the scheduled task
    *
    * @param context JobExecutionContext
    */
  override def run(context: JobExecutionContext): Unit = {
    new CapitalNoticeSendEmailAction(CapitalNoticeRequest()).execute
  }
}
