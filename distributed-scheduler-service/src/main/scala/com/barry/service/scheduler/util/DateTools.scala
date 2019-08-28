package com.barry.service.scheduler.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, Locale}

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils


/**
  * 类 名：DateUtils.java<br />
  * <p/>
  * 功能描述：实体处理相关工具类 <br />
  * <p/>
  * 版本信息：v 1.0<br />
  * <p/>
  */
object DateTools {
  val DATE_PATTERN = "yyyy-MM-dd"
  val TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss"
  val TIMESTAMP_CN = "yyyy年MM月dd日 HH:mm:ss"
  val FULL_TIMESTAMP_PATTERN = "yyyyMMddHHmmssSSS"
  val TIME_PATTERN = "HH:mm"
  val DATE_PATTERN_WITHOUT: String = "yyyyMMdd"

  /**
    * 解析日期<br>
    * 支持格式：<br>
    *
    * @param dateStr
    * @return
    */
  def parseDate(dateStr: String): Date = {
    var format:SimpleDateFormat = null
    if (StringUtils.isBlank(dateStr)) return null
    var _dateStr = dateStr.trim
    try {
      if (_dateStr.matches("\\d{1,2}[A-Z]{3}")) _dateStr = _dateStr + (Calendar.getInstance.get(Calendar.YEAR) - 2000)
      // 01OCT12
      if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{2}")) format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH)
      else if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{4}.*")) { // 01OCT2012
        // ,01OCT2012
        // 1224,01OCT2012
        // 12:24
        _dateStr = _dateStr.replaceAll("[^0-9A-Z]", "").concat("000000").substring(0, 15)
        format = new SimpleDateFormat("ddMMMyyyyHHmmss", Locale.ENGLISH)
      }
      else {
        val sb = new StringBuffer(_dateStr)
        var tempArr = _dateStr.split("\\s+")
        //yyyy-MM-dd、yyyy/MM/dd、yyyy年MM月dd
        tempArr = tempArr(0).split("[-\\/\u4e00-\u9fa5]")
        if (tempArr.length == 3) {
          if (tempArr(1).length == 1) sb.insert(5, "0")
          if (tempArr(2).length == 1) sb.insert(8, "0")
        }
        _dateStr = sb.append("000000").toString.replaceAll("[^0-9]", "").substring(0, 14)
        if (_dateStr.matches("\\d{14}")) format = new SimpleDateFormat("yyyyMMddHHmmss")
      }
      val date = format.parse(_dateStr)
      date
    } catch {
      case e: Exception =>
        throw new RuntimeException("无法解析日期字符[" + dateStr + "]")
    }
  }

  /**
    * 解析日期字符串转化成日期格式<br>
    * generate by: vakin jiang at 2012-3-1
    *
    * @param dateStr
    * @param pattern
    * @return
    */
  def parseDate(dateStr: String, pattern: String): Date = try {
    var format:SimpleDateFormat = null
    if (StringUtils.isBlank(dateStr)) return null
    if (StringUtils.isNotBlank(pattern)) {
      format = new SimpleDateFormat(pattern)
      return format.parse(dateStr)
    }
    parseDate(dateStr)
  } catch {
    case e: Exception =>
      throw new RuntimeException("无法解析日期字符[" + dateStr + "]")
  }

  /**
    * 获取一天开始时间<br>
    * generate by: vakin jiang at 2011-12-23
    *
    * @param date
    * @return
    */
  def getDayBegin(date: Date): Date = {
    val format = DateFormatUtils.format(date, DATE_PATTERN)
    parseDate(format.concat(" 00:00:00"))
  }

  /**
    * 获取一天结束时间<br>
    * generate by: vakin jiang at 2011-12-23
    *
    * @param date
    * @return
    */
  def getDayEnd(date: Date): Date = {
    val format = DateFormatUtils.format(date, DATE_PATTERN)
    parseDate(format.concat(" 23:59:59"))
  }

  /**
    * 时间戳格式转换为日期（年月日）格式<br>
    * generate by: vakin jiang at 2011-12-23
    *
    * @param date
    * @return
    */
  def timestamp2Date(date: Date): Date = formatDate(date, DATE_PATTERN)

  /**
    * 格式化日期格式为：ddMMMyy<br>
    * generate by: vakin jiang
    * at 2012-10-17
    *
    * @return
    */
  def format2ddMMMyy(date: Date): String = {
    val format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH)
    format.format(date).toUpperCase
  }

  def format2ddMMMyy(dateStr: String): String = {
    val format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH)
    format.format(DateTools.parseDate(dateStr)).toUpperCase
  }

  def format2yyMMdd(dateStr: String): String = {
    val format = new SimpleDateFormat("yyMMdd", Locale.ENGLISH)
    format.format(DateTools.parseDate(dateStr)).toUpperCase
  }

  /**
    * 格式化日期字符串<br>
    * generate by: vakin jiang at 2012-3-7
    *
    * @param dateStr
    * @param patterns
    * @return
    */
  def formatDateStr(dateStr: String, patterns: String*): String = {
    var pattern = TIMESTAMP_PATTERN
    if (patterns != null && patterns.length > 0 && StringUtils.isNotBlank(patterns(0))) pattern = patterns(0)
    DateFormatUtils.format(parseDate(dateStr), pattern)
  }

  /**
    * 格式化日期为日期字符串<br>
    * generate by: vakin jiang at 2012-3-7
    *
    * @param patterns
    * @return
    */
  def format(date: Date, patterns: String*): String = {
    if (date == null) return ""
    var pattern = TIMESTAMP_PATTERN
    if (patterns != null && patterns.length > 0 && StringUtils.isNotBlank(patterns(0))) pattern = patterns(0)
    DateFormatUtils.format(date, pattern)
  }

  def format2DateStr(date: Date): String = format(date, DATE_PATTERN)

  /**
    * 格式化日期为指定格式<br>
    * generate by: vakin jiang at 2012-3-7
    *
    * @param orig
    * @param patterns
    * @return
    */
  def formatDate(orig: Date, patterns: String*): Date = {
    var pattern = TIMESTAMP_PATTERN
    if (patterns != null && patterns.length > 0 && StringUtils.isNotBlank(patterns(0))) pattern = patterns(0)
    parseDate(DateFormatUtils.format(orig, pattern))
  }

  /**
    * 比较两个时间相差多少分钟
    */
  def getDiffMinutes(d1: Date, d2: Date): Long = {
    val between = Math.abs((d2.getTime - d1.getTime) / 1000)
    val min = between / 60 // 转换为分
    min
  }

  /**
    * 比较两个时间相差多少天
    */
  def getDiffDay(d1: Date, d2: Date): Long = {
    val between = Math.abs((d2.getTime - d1.getTime) / 1000)
    val day = between / 60 / 60 / 24
    Math.floor(day).toLong
  }

  /**
    * 返回传入时间月份的最后一天
    */
  def lastDayOfMonth(date: Date): Date = {
    val cal = Calendar.getInstance
    cal.setTime(date)
    val value = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    cal.set(Calendar.DAY_OF_MONTH, value)
    cal.getTime
  }

  /**
    * 返回传入时间月份的第一天
    */
  def firstDayOfMonth(date: Date): Date = {
    val cal = Calendar.getInstance
    cal.setTime(date)
    val value = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
    cal.set(Calendar.DAY_OF_MONTH, value)
    cal.getTime
  }

  /**
    * 获取两个时间相差月份
    */
  def getDiffMonth(start: Date, end: Date): Int = {
    val startCalendar = Calendar.getInstance
    startCalendar.setTime(start)
    val endCalendar = Calendar.getInstance
    endCalendar.setTime(end)
    val temp = Calendar.getInstance
    temp.setTime(end)
    temp.add(Calendar.DATE, 1)
    val year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
    val month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
    if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) year * 12 + month + 1
    else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) year * 12 + month + 1
    else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) year * 12 + month + 1
    else if ((year * 12 + month - 1) < 0) 0
    else year * 12 + month
  }

  /**
    * 计算并格式化消耗时间<br>
    * generate by: vakin jiang at 2012-2-16
    *
    * @param startPoint
    * @return
    */
  def formatTimeConsumingInfo(startPoint: Long): String = {
    val buff = new StringBuffer
    val totalMilTimes = System.currentTimeMillis - startPoint
    val hour = Math.floor(totalMilTimes / (60 * 60 * 1000)).toInt
    val mi = Math.floor(totalMilTimes / (60 * 1000)).toInt
    val se = Math.floor((totalMilTimes - 60000 * mi) / 1000).toInt
    if (hour > 0) buff.append(hour).append("小时")
    if (mi > 0) buff.append(mi).append("分")
    if (hour == 0) buff.append(se).append("秒")
    buff.toString
  }

  def formatCnTime(date: Date): String = {
    if (date == null) throw new RuntimeException("Date不能为空")
    new SimpleDateFormat(TIMESTAMP_CN).format(date)
  }

  def formatCn2Time(date: Date): String = {
    if (date == null) throw new RuntimeException("Date不能为空")
    new SimpleDateFormat(TIMESTAMP_PATTERN).format(date)
  }

  /**
    * 判断是否为闰年<br>
    * generate by: zengqw at 2012-9-26
    */
  def isLeapYear(year: Int): Boolean = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

  def add(date: Date, calendarField: Int, amount: Int): Date = {
    if (date == null) throw new IllegalArgumentException("The date must not be null")
    val c = Calendar.getInstance
    c.setTime(date)
    c.add(calendarField, amount)
    c.getTime
  }

  /**
    * 去掉时间中的毫秒,置为0
    *
    * @param date
    * @return
    */
  def resetMillisecond(date: Date): Date = {
    val ms = date.getTime / 1000 * 1000
    new Date(ms)
  }

  /**
    * 日期天数加减
    *
    * @param date
    * @param day
    * @return
    */
  def addSubtractDate(date: Date, day: Int): Date = {
    val calendar = Calendar.getInstance
    calendar.setTime(date)
    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day)
    calendar.getTime
  }

  def main(args: Array[String]): Unit = {
    var date = parseDate("2014年09月12日 16:47:25")
    date = resetMillisecond(new Date)
    System.out.println(date.getTime)
  }
}
