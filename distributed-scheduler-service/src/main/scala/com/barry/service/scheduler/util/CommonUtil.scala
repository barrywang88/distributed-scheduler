package com.barry.service.scheduler.util

import java.text.SimpleDateFormat
import java.util.Calendar
import com.barry.service.scheduler.dto._
import org.slf4j.LoggerFactory

/**
  * @description: 公共方法
  * @author lotey
  * @date 2018/5/7 10:29
  * @version 0.0.1
  */
object CommonUtil {

  private val logger = LoggerFactory.getLogger(getClass)

  /**
    * 计算下addNum天
    * addNum可以正可以负，负则表示上addNum个天
    * @param day
    * @param addNum
    * @return
    */
  def getNextDay(day: String, addNum: Int): String = {

    val cal2 = Calendar.getInstance()
    val sdf2 = new SimpleDateFormat("yyyy-MM-dd")
    cal2.setTime(sdf2.parse(day))
    cal2.add(Calendar.DATE, addNum)
    val nextDay = sdf2.format(cal2.getTime)
    nextDay
  }
}
