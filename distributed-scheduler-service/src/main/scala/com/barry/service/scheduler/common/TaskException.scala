package com.barry.service.scheduler.common

import com.github.dapeng.core.SoaBaseCodeInterface

/**
  * 类功能描述：异常代码定义类
  *
  * @author BarryWang create at 18-9-4 上午11:25
  * @version 1.0.0
  */
class TaskException(val errorCode: String, val message: String) extends SoaBaseCodeInterface {
  override def getMsg: String = message

  override def getCode: String = errorCode
}

object TaskException {
  def toException(errorCode: String, msg: String): TaskException = {
    new TaskException(errorCode, msg)
  }
  /**
    * 当前账表月不存在
    */
  val NOT_EXIST_CHECK_MONTH = new TaskException("Err-Task-001", "账表月不存在")
  /**
    * 账表月格式不正确
    */
  val ERROR_OF_CHECK_MONTH_FORMAT = new TaskException("Err-Task-002", "账表月格式不正确")
  /**
    * 账表月不能为空
    */
  val NULL_OF_CHECK_MONTH_FORMAT = new TaskException("Err-Task-003", "账表月不能为空")
  /**
    * 上次关账操作未完成，不能重复关账
    */
  val EXIST_UNFINISH_CLOSE_ACCOUNT = new TaskException("Err-Task-004", "上次关账操作未完成，不能重复关账")
}