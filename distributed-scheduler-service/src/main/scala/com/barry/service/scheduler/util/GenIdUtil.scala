package com.barry.service.scheduler.util

import com.today.soa.idgen.scala.cache.IDCacheClient

/**
  * 生成ID工具类
  */
object GenIdUtil {
  //单据
  val INVOICE = "invoice_id"
  //门店营收
  val SHOPINCOME = "shopincome_id"
  //报废
  val RETIREMENT_DOCUMENT = "retirement_id"
  //报废明细
  val RETIREMENT_DOCUMENT_LINE = "retirement_line_id"
  //门店营收-现金比对
  val SHOPCASH = "shopcash_id"
  //定时任务日志
  val SCHEDULED_TASK_LOG = "scheduled_task_log_id"
  /**
    * 获取主键ID
    * @param bizTag
    * @return
    */
  def getId(bizTag: String) : Long = {
    IDCacheClient.getId(bizTag)
  }
}
