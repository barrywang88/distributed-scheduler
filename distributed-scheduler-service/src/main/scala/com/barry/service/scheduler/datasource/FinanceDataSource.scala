package com.barry.service.scheduler.datasource

import javax.annotation.Resource
import javax.sql.DataSource

object FinanceDataSource {
  var mysqlData: DataSource = _
}

class FinanceDataSource {
  @Resource(name="tx_finance_dataSource")
  def setDataSource(mysqlData: DataSource): Unit ={
    FinanceDataSource.mysqlData = mysqlData
  }
}
