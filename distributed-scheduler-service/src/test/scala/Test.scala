import java.util
import com.today.service.financetask.job.define.JobEnum
import com.today.service.financetask.util.EmailUtil

import scala.reflect.runtime.universe._
import org.reflections.Reflections

import scala.collection.JavaConverters._


/**
  * @author zhangc
  * @date 2018\5\16 0016 20:40
  * @version 1.0.0
  */
object Test {
  def subObject[T](underlying: Class[T]): Unit = {
//    val reflects = new Reflections("com.today.service.financetask.job")
//    val s: util.Set[Class[_ <: T]] = reflects.getSubTypesOf(underlying)
//    s.asScala.foreach(sub=>
//      println(sub)
//    )

  }

  def main(args: Array[String]): Unit = {
   /* val data = List(("HomeWay","Male",1),("XSDYM","Femail",2),("Mr.Wang","Male", 1))
    val group = data.groupBy{ case x => (x._2, x._3) }
    val list = group.toList
    println(group)
    println(list)
    val list1 = list.zipWithIndex.map{
      item => item._1
    }
    val list2 = group.keys.map {
      item => item._1
    }
    println(list1)
    println(list2)
*/
    /*val v: Map[String, JobEnum] = JobEnum.namesToValuesMap
    v.foreach(x => println(x._1))*/

    /*val instance = JobEnum.jobIdEnumMap
    instance.foreach(x=> println(x._1))*/
//    com.today.service.financetask.job.define.AbstractJob

//    subObject(com.today.service.financetask.job.define.AbstractJob.getClass)
    EmailUtil.sendEmail(EmailUtil.CAPITAL_EMAIL_PREFIX,"jhu@today36524.com.cn", "123", "123")
  }
}
