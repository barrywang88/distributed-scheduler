package com.barry.service.scheduler.action

import com.barry.api.scheduler.scala.request.CapitalNoticeRequest
import com.barry.api.scheduler.scala.response.ServiceResponse
import com.barry.service.scheduler.common.Action
import com.barry.service.scheduler.util.EmailUtil
import org.slf4j.LoggerFactory

/**
  * @description: 发送预付款邮件和已付款定时任务
  * @author zpluo
  * @date 2018\10\25 0025 15:54
  * @version 1.0.0
  */
class CapitalNoticeSendEmailAction(request: CapitalNoticeRequest) extends Action[ServiceResponse] {
  /** 日志 */
  val logger = LoggerFactory.getLogger(getClass)

  override def preCheck: Unit = {
    // 无前置校验条件
  }

  /**
    * 发送资金通知邮件
    * @return
    */
  override def action = {
    ServiceResponse("200","success")
  }
}
