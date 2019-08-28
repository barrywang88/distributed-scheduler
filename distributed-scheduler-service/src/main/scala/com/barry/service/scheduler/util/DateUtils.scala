package com.barry.service.scheduler.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.{Calendar, Date}

import com.barry.service.scheduler.utils.Properties

/**
  * 日期工具类
  *
  * @author BarryWang create at 2018/5/22 20:14
  * @version 0.0.1
  */
object DateUtils{
  val CALENDER_INSTANCE = Calendar.getInstance
  //默认分区时前后范围的月份数
  val DEFAULT_ACQUISITION_MONTH_INTERVAL  = Properties.getDefaultAcquisitionMonthInterval.toInt

  /**
    * 生成文件的路径：年/月/日/
    */
  def filePathGen: String = {
    val today = LocalDate.now
    today.getYear + "/" + today.getMonthValue + "/" + today.getDayOfMonth + "/"
  }

  /**
    * 获取当前时间字符串(格式:yyyyMMddHHmmssSSS)
    * @return
    */
  def getCurrentTimeStr: String ={
    return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date)
  }

  /**
    * 获取当前时间戳
    * @return
    */
  def getCurrentTimestamp: java.sql.Timestamp = {
    new java.sql.Timestamp(new Date().getTime)
  }
  def getCurrentDate: java.sql.Date = {
    new java.sql.Date(new Date().getTime)
  }
  /**
    * 获取当前年
    * @return yyyy
    */
  def getCurrentYear: Int = {
    CALENDER_INSTANCE.get(Calendar.YEAR)
  }

  /**
    * 根据Month字符串获取当前月起始天到结束天
    * @param yearMonth 2108-05
    */
  def getFirstAndLastDayOfMonth(yearMonth: String): String ={
    val firstDayOfMonth = DateTools.parseDate(s"${yearMonth}-01", DateTools.DATE_PATTERN)
    val lastDayOfMonth = DateTools.lastDayOfMonth(firstDayOfMonth);
    s"${yearMonth}-01 至 ${DateTools.format(lastDayOfMonth, DateTools.DATE_PATTERN)}"
  }

  /**
    * 根据当前年月获取上个月
    * @param yearMonth 当前年月
    * @param pattern 格式
    * @return
    */
  def getLastYearMonth(yearMonth: String, pattern: String): Int ={
    val date = DateTools.parseDate(yearMonth, pattern)
    CALENDER_INSTANCE.setTime(date)
    CALENDER_INSTANCE.add(Calendar.MONTH, -1)
    (CALENDER_INSTANCE.get(Calendar.YEAR)*100+CALENDER_INSTANCE.get(Calendar.MONTH))+1
  }

  /**
   * 根据Month字符串获取当前月结束天
   * @param yearMonth 2108-05
  */
  def getLastDayOfMonth(yearMonth: String): String ={
    val firstDayOfMonth = DateTools.parseDate(s"${yearMonth}-01", DateTools.DATE_PATTERN)
    val lastDayOfMonth = DateTools.lastDayOfMonth(firstDayOfMonth);
    DateTools.format(lastDayOfMonth, DateTools.DATE_PATTERN)
  }
  /**
    * 获取前后几天日期
    * @param date 当天日期
    * @param num  负正 为前后天数
    * @return
    */
  def getBeforeOrAfterDay(date: String,num: Int): String = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(dateFormat.parse(date))
    cal.add(Calendar.DATE, num)
    dateFormat.format(cal.getTime())
  }

  /**
    * 获取今天日期
    * @param pattern
    * @return
    */
  def getToday(pattern:String): String = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat(pattern)
    dateFormat.format(new Date)
  }

  case class Claz(a: Int, b: BigDecimal)
  def main(args: Array[String]): Unit = {
//    println(getLastYearMonth("201807", "yyyyMM"))
    println(getNextMonth("2019-05-23"))
    println(getPreMonth("2019-05-23"))
    println(getAcquisitionMonthInterval("2019-05-23", "yyyy-MM-dd"))
    println(getBetweenMonthInterval("2019-05-01","2019-05-31", "yyyy-MM-dd"))
  }

  /**
    * 根据date获取addNum月之前与addNum月之后的日期语句，使用created_at,addNum默认使用1个月
    * @param date
    * @param dateFormat
    */
  def getAcquisitionMonthInterval(date: String, dateFormat:String ):String = {
    getAcquisitionMonthInterval(date, dateFormat, DEFAULT_ACQUISITION_MONTH_INTERVAL)
  }
  /**
    * 根据date获取addNum月之前与addNum月之后的日期语句，使用created_at
    * @param date
    * @param dateFormat
    * @param addNum
    **/
  def getAcquisitionMonthInterval(date: String, dateFormat:String, addNum: Int) = {
    val defaultFormat = new SimpleDateFormat(DateTools.DATE_PATTERN)
    val cal = Calendar.getInstance()
    val sdf = new SimpleDateFormat(dateFormat)
    cal.setTime(sdf.parse(date))
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    cal.add(Calendar.MONTH, -addNum)
    val beginDate = defaultFormat.format(cal.getTime)
    cal.setTime(sdf.parse(date))
    cal.add(Calendar.MONTH, addNum)
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    s""" AND created_at between '""" + beginDate + """' AND '"""+ defaultFormat.format(cal.getTime).concat(" 23:59:59")+"""'"""
  }
  /**
    * 根据beginDate、endDate获取addNum月之前与addNum月之后的日期语句，使用created_at,addNum默认使用1个月
    * @param beginDate
    * @param endDate
    * @param dateFormat
    */
  def getBetweenMonthInterval(beginDate: String, endDate: String, dateFormat:String ):String = {
    getBetweenMonthInterval(beginDate, endDate, dateFormat, DEFAULT_ACQUISITION_MONTH_INTERVAL)
  }

  /**
    * 根据date获取addNum月之前与addNum月之后的日期语句，使用created_at
    * @param beginDate
    * @param endDate
    * @param dateFormat
    * @param addNum
    **/
  def getBetweenMonthInterval(beginDate: String,  endDate: String, dateFormat:String, addNum: Int) = {
    val defaultFormat = new SimpleDateFormat(DateTools.DATE_PATTERN)
    val cal = Calendar.getInstance()
    val sdf = new SimpleDateFormat(dateFormat)
    cal.setTime(sdf.parse(beginDate))
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    cal.add(Calendar.MONTH, -addNum)
    val data1 = defaultFormat.format(cal.getTime)
    cal.setTime(sdf.parse(endDate))
    cal.add(Calendar.MONTH, addNum)
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    s""" AND created_at between '""" + data1 + """' AND '"""+ defaultFormat.format(cal.getTime).concat(" 23:59:59")+"""'"""
  }

  /**
    * 计算下addNum个月的最后一天
    * @param day
    * @param dateFormat
    * @param addNum
    * @return
    */
  def getNextMonth(day: String, dateFormat:String, addNum: Int): String = {
    val defaultFormat = new SimpleDateFormat(DateTools.DATE_PATTERN)
    val cal = Calendar.getInstance()
    val sdf = new SimpleDateFormat(dateFormat)
    cal.setTime(sdf.parse(day))
    cal.add(Calendar.MONTH, addNum)
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    defaultFormat.format(cal.getTime).concat(" 23:59:59")
  }

  /**
    * 计算下addNum个月的最后一天
    * @param day
    * @return
    */
  def getNextMonth(day: String): String = {
    getNextMonth(day, DateTools.DATE_PATTERN, DEFAULT_ACQUISITION_MONTH_INTERVAL)
  }

  /**
    * 计算上addNum个月的第一天，addNum为正数
    * @param day
    * @return
    */
  def getPreMonth(day: String): String = {
    getPreMonth(day, DateTools.DATE_PATTERN, -DEFAULT_ACQUISITION_MONTH_INTERVAL)
  }

  /**
    * 计算上addNum个月的第一天,addNum为正数
    * @param day
    * @param dateFormat
    * @param addNum
    * @return
    */
  def getPreMonth(day: String, dateFormat:String, addNum: Int): String = {
    val defaultFormat = new SimpleDateFormat(DateTools.DATE_PATTERN)
    val cal = Calendar.getInstance()
    val sdf = new SimpleDateFormat(dateFormat)
    cal.setTime(sdf.parse(day))
    val value = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
    cal.set(Calendar.DAY_OF_MONTH, value)
    cal.add(Calendar.MONTH, addNum)
    defaultFormat.format(cal.getTime)
  }

  /**
    * 获取当前时间的上个年月
    */
  def getPreYearAndMonth(): Int ={
    CALENDER_INSTANCE.get(Calendar.MONTH) match {
      case 0 => (CALENDER_INSTANCE.get(Calendar.YEAR)-1)*100+12
      case _ => CALENDER_INSTANCE.get(Calendar.YEAR)*100 + CALENDER_INSTANCE.get(Calendar.MONTH)
    }

  }
}
