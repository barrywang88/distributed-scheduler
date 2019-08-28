package com.barry.service.scheduler.util

import java.util.Date

import org.quartz.impl.triggers.CronTriggerImpl

import scala.util.control.NonFatal

/**
  * Quartz cron表达式转换器
  *
  * @author BarryWang create at 2018/5/11 17:23
  * @version 0.0.1
  */
object CronConverter {
  /**
    * 每天定时触发-转换时分格式(hh:mm)为cron表达式
    * @param hourMibuteStr
    * @return
    */
  def convertHourMinuteToCron(hourMibuteStr : String) : String = {
    val splitHm = hourMibuteStr.split(":")
    s"0 ${splitHm(1).trim.toInt} ${splitHm(0).trim.toInt} * * ?"
  }

  /**
    * 每天定时触发-转换时分格式(hh:hh:mm)为cron表达式
    * @param dayStr
    * @return
    */
  def convertDayToCron(dayStr : String) : String = {
    val splitHm = dayStr.split(":")
    s"0 ${splitHm(1).trim.toInt} ${splitHm(0).trim.toInt} * * ?"
  }

  /**
    * 解析cron
    * @param cron
    * @return
    */
  def explainCron(cron : String) :String = {
    val split = cron.split(" ")
    val time = zeroize(split(2))+":"+zeroize(split(1))+":"+zeroize(split(0))
    if(!CheckUtil.isValidDate(time, "HH:mm:ss")){
      throw new RuntimeException("cron表达式错误")
    } else {
      time
    }
  }

  /**
    * 检查cron表达式是否合法
    * @param cronExpression
    * @return
    */
  def isValidExpression(cronExpression: String): Boolean = {
    try {
      val trigger = new CronTriggerImpl
      trigger.setCronExpression(cronExpression)
      val date = trigger.computeFirstFireTime(null)
      return date != null && date.after(new Date)
    } catch {
      case NonFatal(e) =>
    }
    return false
  }

  def zeroize(split: String):String ={
    if(split.length == 1)
      "0"+split
    else
      split
  }

  def main(args:Array[String]):Unit={
    println("08".trim.toInt)
  }
}
