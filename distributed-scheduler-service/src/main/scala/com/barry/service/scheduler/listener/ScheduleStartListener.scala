package com.barry.service.scheduler.listener

import com.barry.service.scheduler.utils.JobInfo
import com.barry.api.scheduler.scala.enums.TScheduledTaskIsStartEnum
import com.barry.service.scheduler.job.define.JobEnum
import com.barry.service.scheduler.query.sql.ScheduledTaskQuerySql
import com.barry.service.scheduler.util.QuartzManager
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service

/**
  *  类功能描述: 定时器监听器, 服务启动时启动定时器
  *
  * @author BarryWang create at 2018/5/11 12:04
  * @version 0.0.1
  */
@Service
class ScheduleStartListener extends ApplicationListener[ContextRefreshedEvent] {
  /** 日志 */
  val logger = LoggerFactory.getLogger(getClass)

  /**
    * 启动加载执行定时任务
    */
  override def onApplicationEvent(event: ContextRefreshedEvent): Unit = {
    logger.info("=======服务器重启定时任务启动start=======")
    //启动服务时恢复常规定时任务
    JobEnum.values.foreach(recoveryCommonJob(_))
    logger.info("=======服务器重启定时任务启动end=======")
  }

  /**
    * 恢复通用定时任务
    * @param jobInfo 定时任务枚举
    */
  private def recoveryCommonJob(jobInfo: JobInfo)={
    try {
      val jobClass = JobEnum.jobIdClassMap(jobInfo.jobId)
      ScheduledTaskQuerySql.queryByJobId(jobInfo.jobId) match {
        case Some(x) => {
          x.isStart match {
            case TScheduledTaskIsStartEnum.YES.id => {
              QuartzManager.addJobByCron(jobInfo.jobId, x.jobCron, jobClass)
              logger.info(s"定时任务：'${jobInfo.jobName}'启动成功!")
            }
            case _ => logger.info(s"定时任务：'${jobInfo.jobName}'is_start标志为0，不启动")
          }
        }
        case None => QuartzManager.addJobByCron(jobInfo.jobId, jobInfo.defaultCron(), jobClass)
      }
    } catch {
      case e : Exception => logger.error(s"定时任务：'${jobInfo.jobName}'启动失败， 失败原因：", e)
    }
  }
}
