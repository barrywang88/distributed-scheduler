package com.barry.service.scheduler.job.define

import java.io.{PrintWriter, StringWriter}

import com.barry.service.scheduler.utils.JobInfo
import com.github.dapeng.core.helper.MasterHelper
import com.barry.api.scheduler.scala.enums.TScheduledTaskLogEnum
import com.barry.service.scheduler.action.sql.ScheduledTaskLogSql
import com.barry.service.scheduler.util.{AppContextHolder, Debug}
import org.quartz.{Job, JobExecutionContext}
import org.slf4j.LoggerFactory
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionTemplate

import scala.util.{Failure, Success, Try}

/**
  * the abstract class for job
  */
trait AbstractJob extends Job{
  /** 日志 */
  val logger = LoggerFactory.getLogger(getClass)

  /**
    * execute the job
    * @param context
    */
  override def execute(context: JobExecutionContext): Unit = {
    getJobAndApiInfo(context) match {
      case Some(x) => execute(context, x.jobId, x.jobName)
      case None => logger.error("没有定义JobEnum及对应JobObject，请检查")
    }
  }

  /**
    * execute the job
    * @param context
    * @param jobId
    * @param jobName
    */
  def execute(context: JobExecutionContext, jobId : String, jobName: String): Unit = {
    //判断是否是选中的master节点，不是master节点不执行定时任务
    if (!MasterHelper.isMaster("com.barry.api.scheduler.service.ScheduledService", "1.0.0")) {
      logger.info(s"Can't select master to run the job ${jobId}: ${jobName}")
      return
    }

    //事物处理
    val taskTransactionTemplate: TransactionTemplate = AppContextHolder.getBean("taskTransactionTemplate")
    val logId = taskTransactionTemplate.execute((taskStatus: TransactionStatus) =>{
      //记录开始日志
      val logId = ScheduledTaskLogSql.insertScheduledTaskLog(jobId)
      context.put("logId", logId)
      logger.info(s"Starting the job ${jobId}: ${jobName} ...")

      Debug.reset()
      logId
    })
      //执行任务
      Try(Debug.trace(s"${jobId}:${jobName}")(taskTransactionTemplate.execute((status: TransactionStatus) =>run(context)))) match
      {
        case Success(x) => {
          logger.info(s"Successfully execute the job ${jobId}: ${jobName}")
          taskTransactionTemplate.execute((taskStatus: TransactionStatus) => successLog(logId))
        }
        case Failure(e) => {
          logger.error(s"Failure execute the job ${jobId}: ${jobName}", e)
          taskTransactionTemplate.execute((taskStatus: TransactionStatus) => failureLog(logId, e))
        }
      }
      Debug.info()

  }

  /**
    * get the api information
    * @return (interface name, interface version, JobEnum)
    */
  def getJobAndApiInfo(context: JobExecutionContext) : Option[JobInfo] ={
    JobEnum.jobClassInfoMap.get(this.getClass.getName)
  }

  /**
    * start up the scheduled task
    * @param context JobExecutionContext
    */
  def run(context: JobExecutionContext)

  /**
    * 成功日志记录
    * @param logId
    */
  def successLog(logId: Long): Unit ={
    ScheduledTaskLogSql.updateExportReportRecord(logId, TScheduledTaskLogEnum.SUCCESS, "Success")
  }

  /**
    * 失败日志记录
    * @param logId
    */
  def failureLog(logId: Long, e: Throwable): Unit ={
    ScheduledTaskLogSql.updateExportReportRecord(logId, TScheduledTaskLogEnum.FAILURE, getExceptionStack(e))
  }

  /**
    *
    * 功能说明:在日志文件中 ，打印异常堆栈
    * @param e : Throwable
    * @return : String
    */
  def getExceptionStack(e: Throwable): String = {
    val errorsWriter = new StringWriter
    e.printStackTrace(new PrintWriter(errorsWriter))
    errorsWriter.toString
  }
}