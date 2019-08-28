package com.barry.service.scheduler.common

import com.github.dapeng.core.{SoaBaseCodeInterface, SoaException}

object Assert {

//  def assert(assertion: scala.Boolean, code: String, message: String): scala.Unit =
//    if (!assertion) throw new SoaException(code, message)

  def assert(assertion: scala.Boolean, errorCodeEnums: SoaBaseCodeInterface): Unit =
    if (!assertion) throw new SoaException(errorCodeEnums.getCode, errorCodeEnums.getMsg)

}
