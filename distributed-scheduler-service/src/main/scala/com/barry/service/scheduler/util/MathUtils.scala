package com.barry.service.scheduler.util

import java.util.concurrent.atomic.AtomicInteger

/**
  * 数据学计算工具类
  *
  * @author BarryWang create at 2018/7/12 16:45
  * @version 0.0.1
  */
object MathUtils {
  /*
    * 格式化金额(精确到小数后2位)
    * @param param
    * @return
    */
  def format(param: BigDecimal): BigDecimal = {
    this.format(param, 2)
  }
  /**
    * 格式化金额(精确到小数后3位)
    * @param param
    * @return
    */
  def format3(param: BigDecimal): BigDecimal = {
    this.format(param, 3)
  }
  /**
    * 格式化金额
    * @param param
    * @return
    */
  def format(param: BigDecimal, precision: Int): BigDecimal = {
    round(param, precision)
  }

  /**
    * 四舍五入(精确2位小数)
    * @param param
    * @return
    */
  def roundToDouble(param: BigDecimal): Double = {
    this.round(param, 2).toDouble
  }

  /**
    * 四舍五入(精确2位小数)
    * @param param
    * @return
    */
  def round(param: BigDecimal): BigDecimal = {
    this.round(param, 2)
  }

  /**
    * 四舍五入
    * @param param
    * @return
    */
  def round(param: BigDecimal, precision: Int): BigDecimal = {
    if(param == null){
      return BigDecimal(0.00)
    }
    param.setScale(precision, BigDecimal.RoundingMode.HALF_UP)
  }

  def main(args: Array[String]): Unit = {
    println(round(1.28770000/1.16, 4))
  }
}
