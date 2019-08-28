package com.barry.service.scheduler.dto

/**
  * @author dapeng-tool
  */
case class TCheckMonth (
   /**
   * 主键
   */
id : Long,

   /**
   * 当前账表月yyyyMM
   */
checkMonth : Int,

   /**
   * 开始日期
   */
startAt : Option[java.sql.Date],

   /**
   * 结束日期
   */
endAt : Option[java.sql.Date],

   /**
   * 账表类型,1=营收(shopincome)2=单据(invoice)3=应收应付(arap)
   */
checkType : Int,

   /**
   * 是否已关账,0=否false,1=是yes
   */
hasClosed : Boolean,

   /**
   * 创建时间
   */
createdAt : java.sql.Timestamp,

   /**
   * 更新时间
   */
updatedAt : java.sql.Timestamp,

)
