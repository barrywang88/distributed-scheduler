
package com.barry.service.scheduler.job.define

import java.util

import com.barry.service.scheduler.utils.JobInfo
import org.quartz.Job
import org.reflections.Reflections

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * 通过注解获取所有通用Job信息
  *
  * @author BarryWang create at 2018/5/12 10:45
  * @version 0.0.1
  */
object JobEnum {
  /**
    * 获取添加JobInfo注解的类信息
    */
  val jobWithAnnotation: util.Set[Class[_]] = new Reflections("com.barry.service.scheduler.job").getTypesAnnotatedWith(classOf[JobInfo])

  /**
    * 获取所有job枚举值
    * @return
    */
  def values : mutable.Set[JobInfo] = jobWithAnnotation.asScala.map(getJobInfo(_))

  /**
    * 根据job class 获取job 信息
    * @param jobClass
    * @return
    */
  def getJobInfo(jobClass : Class[_]): JobInfo = jobClass.getAnnotation(classOf[JobInfo])

  /**
    * jobId与jobName映射关系
    * @return
    */
  def jobIdNameMap : Map[String, String]={
    jobWithAnnotation.asScala.map{sub =>
      val jobInfo = getJobInfo(sub)
      Map(jobInfo.jobId() -> jobInfo.jobName())
    }.fold(Map())((i,j) => i++j)
  }

  /**
    * JobObject与JobEnum映射关系
    * @return
    */
  def jobClassInfoMap: Map[String, JobInfo] = {
    jobWithAnnotation.asScala.map{sub =>
      Map(sub.getName -> getJobInfo(sub))
    }.fold(Map())((i,j) => i++j)
  }

  /**
    * jobId与JobEnum映射关系
    * @return
    */
  def jobIdInfoMap: Map[String, JobInfo] = {
    jobWithAnnotation.asScala.map{sub =>
      val jobInfo = getJobInfo(sub)
      Map(jobInfo.jobId() -> jobInfo)
    }.fold(Map())((i,j) => i++j)
  }

  /**
    * jobId与JobObject映射关系
    * @return
    */
  def jobIdClassMap: Map[String, Class[_ <: Job]] = {
    jobWithAnnotation.asScala.map{sub =>
      Map(getJobInfo(sub).jobId() -> sub.asInstanceOf[Class[_ <: Job]])
    }.fold(Map[String, Class[_ <: Job]]())((i,j) => i++j)
  }

  def main(args: Array[String]): Unit = {
    println(jobIdClassMap)
  }
}