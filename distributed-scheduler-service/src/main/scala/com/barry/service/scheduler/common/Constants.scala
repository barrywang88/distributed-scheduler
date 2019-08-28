package com.barry.service.scheduler.common

/**
  * @classNane:ConstantUtil
  * @author Charsel zhang
  * @date 2018/11/9 10:11
  * @version 0.0.1
  */
object Constants {

  //================================单据类型编码beigin================
  /**
    * 寄库35
    */
  val INVOICE_TYPE_DEPOSIT = 35
  /**
    * 经由34
    */
  val INVOICE_TYPE_VIA = 34
  /**
    * 巡回33
    */
  val INVOICE_TYPE_TOUR = 33
  /**
    * 直纳32
    */
  val INVOICE_TYPE_NONSTOP = 32
  /**
    * 直送（香烟）25
    */
  val INVOICE_TYPE_DIRECT = 25
  /**
    * 物流缺货修正101
    */
  val INVOICE_TYPE_LOGISTICS_ABSENCE = 101
  /**
    * 厂商缺货修正201
    */
  val INVOICE_TYPE_SUPPLIER_ABSENCE = 201
  /**
    * 物流拒收修正102
    */
  val INVOICE_TYPE_LOGISTICS_REJECTION = 102
  /**
    * 厂商拒收修正202
    */
  val INVOICE_TYPE_SUPPLIER_REJECTION = 202
  /**
    * 退货-物流38
    */
  val INVOICE_TYPE_LOGISTICS_RETURN = 38
  /**
    * 退货-厂商39
    */
  val INVOICE_TYPE_SUPPLIER_RETURN = 39
  /**
    * 物流退货缺货修正111
    */
  val INVOICE_TYPE_LOGISTICS_RETURN_ABSENCE = 111
  /**
    * 厂商退货缺货修正211
    */
  val INVOICE_TYPE_SUPPLIER_RETURN_ABSENCE = 211
  /**
    * 物流退货拒收修正112
    */
  val INVOICE_TYPE_LOGISTICS_RETURN_REJECTION = 112
  /**
    * 厂商退货拒收修正212
    */
  val INVOICE_TYPE_SUPPLIER_RETURN_REJECTION = 212
  /**
    * 转出62
    */
  val INVOICE_TYPE_STORE_OUT = 62
  /**
    * 转入52
    */
  val INVOICE_TYPE_STORE_IN = 52
  /**
    * 总部采购30
    */
  val INVOICE_TYPE_CENTER_PURCHASE = 30
  /**
    * 经由:31
    */
  val INVOICE_TYPE_LOGISTICS_VIA = 31
  /**
    * 总部退货36
    */
  val INVOICE_TYPE_CENTER_RETURN = 36
  /**
    * 调出90
    */
  val INVOICE_TYPE_LOGISTICS_OUT = 90
  /**
    * 调入91
    */
  val INVOICE_TYPE_LOGISTICS_IN = 91
  //================================单据类型编码end================
  //================================单据状态编码beigin================
  /**
    * 14:待配送(TO_DELIVERIED)
    */
  val INVOICE_STATUS_TO_DELIVERIED = 14
  /**
    * 15:配送中(DELIVERING)
    */
  val INVOICE_STATUS_DELIVERING = 15
  /**
    * ;16:配送完成(DELIVERIED)
    */
  val INVOICE_STATUS_DELIVERIED = 16
  /**
    * 21:待验收(TO_IN_STORAGE)
    */
  val INVOICE_STATUS_TO_IN_STORAGE = 21
  /**
    * 25:已发货(OUT_STORAGE)
    */
  val INVOICE_STATUS_OUT_STORAGE = 25
  /**
    * 26:已验收(DONE)
    */
  val INVOICE_STATUS_DONE = 26
  /**
    * 17：超期关闭(CLOSED_FOR_OVERDUE)
    */
  val INVOICE_STATUS_CLOSED_FOR_OVERDUE = 17
  /**
    * 10:修正待确认(TO_BE_CONFIRMED)
    */
  val INVOICE_STATUS_TO_BE_CONFIRMED = 10
  /**
    * 11:裁决中(ADJUDGING)
    */
  val INVOICE_STATUS_ADJUDGING = 11
  /**
    * 12:修正已完结(FINISHED)
    */
  val INVOICE_STATUS_FINISHED = 12
  /**
    * 13:裁决已完结(ADJUDGE_FINISHED)
    */
  val INVOICE_STATUS_ADJUDGE_FINISHED = 13
  /**
    * 18:预约未送(UNDELIVERED)
    */
  val INVOICE_STATUS_UNDELIVERED = 18
  /**
    * 19:红冲关单(HEDGING_CLOSED)
    */
  val INVOICE_STATUS_HEDGING_CLOSED = 19
  //================================单据类型编码end================
  //================================t_base_entry begin================
  /**
    * 32：公司编码
    */
  val BASE_ENTRY_COMPANY = 32
  /**
    * 41：黑名单
    */
  val BASE_ENTRY_BLACKSTORE = 41
  //================================t_base_entry end================
  //================================PO2单据状态 begin================
  /**
    * 30:待供应方发货(TO_DELIVERIED)
    */
  val PO2_PURCHASE_STATUS_TO_DELIVERIED = 30
  /**
    * 40:待验收(TO_BE_RECEIVED)
    */
  val PO2_PURCHASE_STATUS_TO_BE_RECEIVED = 40
  /**
    * 41:部分验收(PARTIAL_RECEIVED)
    */
  val PO2_PURCHASE_STATUS_PARTIAL_RECEIVED = 41
  /**
    * 42:已验收(RECEIVED)
    */
  val PO2_PURCHASE_STATUS_RECEIVED = 42
  /**
    * 43:已自动验收(AUTO_RECEIVED)
    */
  val PO2_PURCHASE_STATUS_AUTO_RECEIVED = 43
  /**
    * 93:收货超时(RECEIVE_TIMEOUT)
    */
  val PO2_PURCHASE_STATUS_RECEIVE_TIMEOUT = 93
  /**
    * 94:仓库关单(CLOSED_BY_STORAGE)
    */
  val PO2_PURCHASE_STATUS_CLOSED_BY_STORAGE = 94
  /**
    * 95:已关单(CLOSED)
    */
  val PO2_PURCHASE_STATUS_CLOSED = 95
  /**
    * 99:已取消(CANCELED)
    */
  val PO2_PURCHASE_STATUS_CANCELED = 99
  /**
    *10:待确认(TO_BE_CONFIRMED)
    */
  val PO2_PURCHASE_STATUS_TO_BE_CONFIRMED = 10
  /**
    * 11:裁决中(ADJUDGING)
    */
  val PO2_PURCHASE_STATUS_ADJUDGING = 11
  /**
    * 12:已完结(FINISHED)
    */
  val PO2_PURCHASE_STATUS_FINISHED = 12
  /**
    * 13:裁决已完结(ADJUDGE_FINISHED)
    */
  val PO2_PURCHASE_STATUS_ADJUDGE_FINISHED = 13
  //================================PO2单据状态 end================
  //================================PO2单据类型编码beigin================
  /**
    * 寄库35
    */
  val PO2_TYPE_DEPOSIT = 35
  /**
    * 经由34
    */
  val PO2_TYPE_VIA = 34
  /**
    * 巡回33
    */
  val PO2_TYPE_TOUR = 33
  /**
    * 直纳32
    */
  val PO2_TYPE_NONSTOP = 32
  /**
    * 直送（香烟）25
    */
  val PO2_TYPE_DIRECT = 25
  /**
    * 进货缺货修正41
    */
  val PO2_TYPE_STOCK_ABSENCE = 41
  /**
    * 进货拒收修正42
    */
  val PO2_TYPE_STOCK_REJECTION = 42
  /**
    * 退货-物流38
    */
  val PO2_TYPE_LOGISTICS_RETURN = 38
  /**
    * 退货-厂商39
    */
  val PO2_TYPE_SUPPLIER_RETURN = 39
  /**
    * 退货缺货修正43
    */
  val PO2_TYPE_RETURN_ABSENCE = 43
  /**
    * 退货拒收修正44
    */
  val PO2_TYPE_RETURN_REJECTION = 44
  /**
    * 转货52
    */
  val PO2_TYPE_STORE_IN = 52
  /**
    * 总部采购30
    */
  val PO2_TYPE_CENTER_PURCHASE = 30
  /**
    * 经由:31
    */
  val PO2_TYPE_LOGISTICS_VIA = 31
  /**
    * 总部退货36
    */
  val PO2_TYPE_CENTER_RETURN = 36
  /**
    * 调拨90
    */
  val PO2_TYPE_LOGISTICS_OUT = 90
  //================================单据类型编码end================
  //================================报废单ScrapOrder类型编码beigin================
  /**
    * 门店类型1
    */
  val SCRAP_STORE_ITEM_TYPE = 1
  /**
    * 仓库类型2
    */
  val SCRAP_STOCK_ITEM_TYPE = 2

  //================================报废单ScrapOrder类型编码end================
  //================================报废单ScrapOrder状态编码beigin================
  /**
    * 报废状态,1:待确认(to_be_confirmed)
    */
  val SCRAP_STATUS_UNCONFIRMED = 1
  /**
    * 报废状态,2:已报废(discarded)
    */
  val SCRAP_STATUS_DISCARDED = 2
  /**
    * 报废状态,3:已撤销(revoked)
    */
  val SCRAP_STATUS_REVOKED = 3

  //================================报废单ScrapOrder状态编码end================
  //4位年前缀
  val YEAR_PREFIX = "20"
  //自动结账推移天数
  val OVERLAP_DAYS = -2
}
