package com.barry.service.scheduler.action.sql

import com.barry.api.scheduler.scala.enums.TScheduledTaskHasDeletedEnum
import com.barry.service.scheduler.datasource.FinanceTaskDataSource
import com.barry.service.scheduler.dto.TScheduledTask
import wangzx.scala_commons.sql._

/**
  * 定时任务SQL
  *
  * @author BarryWang create at 2018/5/12 10:53
  * @version 0.0.1
  */
object ScheduledTaskActionSql {
  /**
    * 添加定时任务
    * @param tScheduledTask
    */
  def insertTScheduledTask(tScheduledTask: TScheduledTask): Unit = {
    FinanceTaskDataSource.mysqlData.executeUpdate(
      sql"""INSERT INTO t_scheduled_task SET
          job_name = ${tScheduledTask.jobName},
          job_id = ${tScheduledTask.jobId},
          job_cron = ${tScheduledTask.jobCron},
          job_type = ${tScheduledTask.jobType},
          is_start = ${tScheduledTask.isStart},
          remark = ${tScheduledTask.remark},
          updated_at = NOW(),
          created_at = NOW(),
          has_deleted = ${tScheduledTask.hasDeleted}
        """
    )
  }

  /**
    * 更新任务Cron表达式
    * @param jobId 任务ID
    * @param jobCron cron表达式
    */
  def updateTaskCron(jobId: String, jobCron: String): Unit ={
    val updateShopCashSql =
      sql"""UPDATE t_scheduled_task SET
           job_cron = ${jobCron},
           updated_at = NOW()
          WHERE job_id = ${jobId}
         """
    FinanceTaskDataSource.mysqlData.executeUpdate(updateShopCashSql)
  }

  /**
    * 更新任务是否启动及表达式

    */
  def updateTaskIsStart(flag : Int, cron: String, jobId: String ): Unit ={
    val updateShopCashSql =
      sql"""UPDATE t_scheduled_task SET
           is_start = ${flag},
           job_cron = ${cron},
           updated_at = NOW()
          WHERE job_id = ${jobId}
         """
    FinanceTaskDataSource.mysqlData.executeUpdate(updateShopCashSql)
  }

  /**
    * 删除任务
    * @param tScheduledTask
    */
  def deleteTask(tScheduledTask: TScheduledTask): Unit ={
    val updateShopCashSql =
      sql"""UPDATE t_scheduled_task SET
           has_deleted = ${TScheduledTaskHasDeletedEnum.YES.id},
           updated_at = NOW()
          WHERE job_id = ${tScheduledTask.jobId}
         """
    FinanceTaskDataSource.mysqlData.executeUpdate(updateShopCashSql)
  }

}
