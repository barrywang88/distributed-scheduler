package com.barry.service.scheduler.dto

/**
  * @author dapeng-tool
  */
case class TCapitalNotice (
   /**
   * 主键
   */
id : Long,

   /**
   * 申请部门
   */
deptName : String,

   /**
   * 公司名称
   */
companyName : String,

   /**
   * 费用事项
   */
feeItem : String,

   /**
   * 经办人
   */
capitalUserName : String,

   /**
   * 经办人电话
   */
capitalUserTel : String,

   /**
   * 经办人邮箱
   */
capitalUserEmail : String,

   /**
   * 金额
   */
amount : BigDecimal,

   /**
   * 计划支付时间
   */
planPayTime : Option[java.sql.Date],

   /**
   * 实际支付时间
   */
actualPayTime : Option[java.sql.Date],

   /**
   * 备注
   */
remarks : Option[String],

   /**
   * 是否删除,0:否(no);1:是(yes)
   */
hasDeleted : Int,

   /**
   * 创建时间
   */
createdAt : java.sql.Timestamp,

   /**
   * 更新时间
   */
updatedAt : java.sql.Timestamp,

)
