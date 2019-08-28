package com.barry.service.scheduler.query.sql

import com.barry.service.scheduler.datasource.FinanceTaskDataSource
import com.barry.service.scheduler.dto.TScheduledTask
import wangzx.scala_commons.sql._

/**
  * 定时任务查询SQL
  *
  * @author BarryWang create at 2018/5/12 14:36
  * @version 0.0.1
  */
object ScheduledTaskQuerySql {
  /**
    * 查询是否存在该jobId定时任务
    * @param jobId
    * @return
    */
  def isExists(jobId: String): Boolean = {
    val sql = sql""" SELECT count(1) FROM t_scheduled_task WHERE job_id = ${jobId} and has_deleted = 0 """
    FinanceTaskDataSource.mysqlData.queryInt(sql) match {
      case 0 => false
      case _ => true
    }
  }

  /**
    * 根据任务ID查询任务
    * @param jobId
    * @return
    */
  def queryByJobId(jobId: String): Option[TScheduledTask] = {
    val query = sql"""SELECT * FROM t_scheduled_task WHERE job_id = ${jobId} and has_deleted = 0"""
    FinanceTaskDataSource.mysqlData.row[TScheduledTask](query)
  }

}
