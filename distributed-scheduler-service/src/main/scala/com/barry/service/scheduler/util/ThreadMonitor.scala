package com.barry.service.scheduler.util

import org.slf4j.LoggerFactory

/**
  * 类功能描述：线程监控器
  *
  * @author WangXueXing create at 18-12-25 下午3:44
  * @version 1.0.0
  */
object ThreadMonitor {
  val LOGGER = LoggerFactory.getLogger(getClass)
  /**
    * 线程监控器
    * 注意，当代码块里有子线程，可能无法监控到对应线程日志
    * @param currentThread 当前执行线程
    * @param intervalTime 打印线程堆栈的间隔时间，　单位为毫秒；选传，默认为１秒
    * @param codeBlock 要监控的代码块
    * @tparam T
    */
  def threadMonitor[T](currentThread: Thread, intervalTime: Option[Long]=None) (codeBlock: => T) {
    //set the real real interval time as 1s if the param intervalTime is None
    val realIntervalTime = intervalTime match {
      case Some(x) => x
      case None =>  1000
    }
    @volatile
    var flag = false
    val t = new Thread(() => {
        while (!flag) {
          val stackElements = currentThread.getStackTrace
          LOGGER.info(stackElements.map(_.toString).mkString("\n"))
          Thread.sleep(realIntervalTime)
        }
      })
    t.start()
    try{
      codeBlock
    } finally {
      flag = true
    }
  }
}

