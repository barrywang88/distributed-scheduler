package com.barry.service.scheduler.dto

/**
  * @author lotey
  * @date 2018/12/11 15:00
  * @version 0.0.1
  */
case class CapitalNoticeStas (
                             companyName: String,
                             capitalUserName: String,
                             capitalUserEmail: String,
                             createDate:String,
                             actualPayTime: String,
                             deptName: String,
                             feeItem: String,
                             amount: Double
                             )