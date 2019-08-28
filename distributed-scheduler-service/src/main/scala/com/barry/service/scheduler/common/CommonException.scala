package com.barry.service.scheduler.common

import com.github.dapeng.core.SoaBaseCodeInterface

class CommonException(val errorCode: String, val message: String) extends SoaBaseCodeInterface{
  override def getMsg: String = message
  override def getCode: String = errorCode
}

object CommonException {
  // 参数异常
  def illegalArgumentException(message: String) =  new CommonException("Err-Common-001", message)
  // 数据不存在
  def dataIsEmpty(message: String) = new CommonException("Err-Common-002", message)
}
