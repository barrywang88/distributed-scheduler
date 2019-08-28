package com.barry.service.scheduler.common

import com.github.dapeng.core.SoaException
import org.slf4j.{Logger, LoggerFactory}

import scala.util.control.NonFatal

trait Action[Output] {

  def execute: Output = {
    try {

      preCheck

      val output: Output = action

      postCheck

      output
    } catch {
      case e:SoaException =>
        val logger: Logger = LoggerFactory.getLogger("com.barry.service.scheduler.common")

        if (e.getCode.startsWith("Err-Core")) {
          logger.error(e.getMsg, e)
        } else {
          logger.error(e.getMsg)
        }
        throw e
      case NonFatal(e) =>
        val logger: Logger = LoggerFactory.getLogger("com.barry.service.scheduler.common")

        logger.error(e.getMessage, e)

        throw e
    } finally {
    }
  }

  /**
    * 前置条件检查：动作、状态等业务逻辑
    */
  def preCheck

  /**
    * 动作
    */
  def action: Output

  /**
    * 后置条件检查
    */
  def postCheck = {}
}






