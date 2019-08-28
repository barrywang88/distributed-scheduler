package com.barry.service.scheduler.action.sql

import com.barry.api.scheduler.scala.enums.TScheduledTaskLogEnum
import com.barry.service.scheduler.datasource.FinanceTaskDataSource
import com.barry.service.scheduler.util.GenIdUtil
import wangzx.scala_commons.sql._

/**
  * 定时任务日志SQL
  */
object ScheduledTaskLogSql {
  /**
    * 插入纪录
    * @param jobId
    */
  def insertScheduledTaskLog(jobId: String): Long ={
    val id = GenIdUtil.getId(GenIdUtil.SCHEDULED_TASK_LOG)
    FinanceTaskDataSource.mysqlData.executeUpdate(
      sql"""INSERT INTO t_scheduled_task_log
               SET id = ${id},
                   status = 0,
                   job_id = ${jobId},
                   remark = 'started',
                   updated_at = NOW(),
                   created_at = NOW()""")
    id
  }

  /**
    *更新导出报表纪录
    */
  def updateExportReportRecord(id: Long, status: TScheduledTaskLogEnum, remark: String) : Unit = {
    FinanceTaskDataSource.mysqlData.withConnection{conn =>
      val sql = sql""" UPDATE t_scheduled_task_log
                        SET updated_at = NOW(),
                            status = ${status.id},
                            remark = ${remark}
                      WHERE id = ${id} """
      FinanceTaskDataSource.mysqlData.executeUpdate(sql)
    }
  }
}
