package com.barry.service.scheduler.datasource

import javax.annotation.Resource
import javax.sql.DataSource

/**
  * 财务任务数据库连接池
  *
  * @author BarryWang create at 2018/5/12 12:05
  * @version 0.0.1
  */
class FinanceTaskDataSource {
  @Resource(name="tx_finance_task_dataSource")
  def setDataSource(mysqlData: DataSource): Unit ={
    FinanceTaskDataSource.mysqlData = mysqlData
  }
}

object FinanceTaskDataSource{
  var mysqlData: DataSource = _
}
