package com.barry.service.scheduler.util

import com.github.dapeng.core.InvocationContextImpl
import org.slf4j.LoggerFactory
import org.springframework.context.{ApplicationContext, ApplicationContextAware}

/**
  * @author zhangc
  *         date 2018\5\18 0018 16:55
  * @version 1.0.0
  */
object AppContextHolder extends ApplicationContextAware {
  val logger = LoggerFactory.getLogger(getClass)

  private var context: ApplicationContext = _

  //取得存储在静态变量中的ApplicationContext.
  def getApplicationContext: ApplicationContext = {
    checkApplicationContext()
    context
  }

  override def setApplicationContext(applicationContext: ApplicationContext): Unit = {
    context = applicationContext
  }


  //从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
  def getBean[T](name: String): T = {
    checkApplicationContext()
    context.getBean(name).asInstanceOf[T]
  }

  private def checkApplicationContext(): Unit = {
    if (context == null) throw new IllegalStateException("applicaitonContext未注入,请在resources/META-INF/spring/services.xml中定义SpringContextHolder")
  }

  def getInstance: this.type = this

  /**
    * 为了防止接口调用连接断开，每分钟调用一次，为了保持心跳
    * @param interval 心跳间隔
    * @param getServiceAddress 获取服务端IP&port方法
    */
  def connectionHolder[T](interval: Option[Long]=None)(getServiceAddress: => List[Int]) (process: => T)={
    val invocationContext = InvocationContextImpl.Factory.currentInstance.asInstanceOf[InvocationContextImpl]
    val serviceAddress = getServiceAddress
    invocationContext.calleeIp(serviceAddress.apply(0))
    invocationContext.calleePort(serviceAddress.apply(1))
    InvocationContextImpl.Factory.currentInstance(invocationContext)

    //set the real real interval time as 1s if the param intervalTime is None
    val realIntervalTime = interval match {
      case Some(x) => x
      case None =>  60*1000L
    }
    @volatile
    var flag = false
    val t = new Thread(() => {
      while (!flag) {
        Thread.sleep(realIntervalTime)
        getServiceAddress
      }
    })
    t.start()
    try{
      process
    } finally {
      flag = true
    }
  }
}
