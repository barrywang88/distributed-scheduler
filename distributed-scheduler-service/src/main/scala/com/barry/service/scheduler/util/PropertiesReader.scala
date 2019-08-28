package com.barry.service.scheduler.util

import java.io.InputStream
import java.util.Properties

import org.slf4j.LoggerFactory

/**
  * @description: 加载配置文件
  * @author zpluo
  * @date 2018\10\25 0025 19:44
  * @version 1.0.0
  */
object PropertiesReader {
  private val logger = LoggerFactory.getLogger(PropertiesReader.getClass)

  val PRPERTIES =  PropertiesReader.loadProperties("config_finance-task.properties")

  /**
    * 加载配置文件
    * @param fileName
    * @return
    */
  def loadProperties(fileName: String): Properties = {
    if (null == fileName || fileName == "") throw new IllegalArgumentException("Properties file path can not be null" + fileName)
    var inputStream:InputStream = null
    var p:Properties = null
    try {
      inputStream = PropertiesReader.getClass.getClassLoader.getResourceAsStream(fileName)
      p = new Properties
      p.load(inputStream)
    } catch {
      case e: Exception =>
        logger.error("读取配置文件失败", e)
    } finally try
        if (null != inputStream) inputStream.close()
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    p
  }

  /**
    * 优先取环境变量中的值，如果没有取配置文件中的值
    * @param attrName 属性名称
    * @return
    */
  def getAttrValByName(attrName: String):String = {
    val envVal = System.getenv(attrName)
    (envVal == null || envVal.trim == "") match {
      case true => PRPERTIES.getProperty(attrName)
      case _ => envVal
    }
  }

}
