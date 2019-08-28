package com.barry.service.scheduler.util

import java.text.SimpleDateFormat

import org.apache.commons.lang3.time.DateFormatUtils

/**
  * 财务隐式转换工具类
  */
object FinanceImplicits {

  /**
    * java.sql.Date转格式为yyyy-MM-dd字符串
    * @param date java.sql.Date
    * @return "yyyy-MM-dd"
    */
  implicit def date2String(date : java.sql.Date): String = {
    if (date == null) return ""
    DateFormatUtils.format(date, DateTools.DATE_PATTERN)
  }

  /**
    * 转格式为yyyy-MM-dd字符串转换为java.sql.Date
    * @param dateStr "yyyy-MM-dd"
    * @return java.sql.Date
    */
  implicit def string2Date(dateStr : String): java.sql.Date = {
    if (dateStr.isEmpty) return null
    new java.sql.Date(new SimpleDateFormat(DateTools.DATE_PATTERN).parse(dateStr).getTime)
  }

  implicit def date2String(date : java.sql.Date, pattern: String): String = {
    if (date == null) return ""
    DateFormatUtils.format(date, pattern)
  }

  /**
    * 转格式为yyyy-MM-dd字符串转换为java.sql.Date
    * @param dateStr "yyyy-MM-dd"
    * @return java.sql.Date
    */
  implicit def string2Date(dateStr : String, pattern: String): java.sql.Date = {
    if (dateStr.isEmpty) return null
    new java.sql.Date(new SimpleDateFormat(DateTools.DATE_PATTERN_WITHOUT).parse(dateStr).getTime)
  }
}
