package com.barry.service.scheduler.util

import java.io.File

import javax.activation.{CommandMap, DataHandler, FileDataSource, MailcapCommandMap}
import javax.mail._
import javax.mail.internet._


/**
  * 邮件发送工具类
  *
  * @author BarryWang create at 2018/6/19 11:15
  * @version 0.0.1
  */
object EmailUtil {
  val OTHER_EMAIL_PREFIX = "OTHER"
  val CAPITAL_EMAIL_PREFIX = "CAPITAL"

  /**
    * 发送邮件
    *
    * @param receiverEmail 收件邮箱
    * @param subject       邮件标题
    * @param content       邮件内容
    * @throws MessagingException
    */
  def sendEmail(prefix: String, receiverEmail: String, subject: String, content: String): Unit = {
    this.sendEmail(prefix, null, receiverEmail, subject, content, "")
  }

  /**
    * 发送邮件
    *
    * @param receiverEmail 收件邮箱
    * @param subject       邮件标题
    * @param content       邮件内容
    * @param attachment    邮件附件
    * @throws MessagingException
    */
  def sendEmail(prefix: String, receiverEmail: String, subject: String, content: String, attachment: String): Unit = {
    this.sendEmail(prefix, null, receiverEmail, subject, content, attachment)
  }

  /**
    * 发送邮件
    *
    * @param from 发件人
    * @param receiverEmail 收件邮箱
    * @param subject       邮件标题
    * @param content       邮件内容
    * @param attachment    邮件附件
    * @throws MessagingException
    */
  def sendEmail(prefix: String, from: String, receiverEmail: String, subject: String, content: String, attachment: String): Unit = {
    val mc: MailcapCommandMap = CommandMap.getDefaultCommandMap().asInstanceOf[MailcapCommandMap]
    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html")
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml")
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain")
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed")
    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822")
    CommandMap.setDefaultCommandMap(mc)
    Thread.currentThread().setContextClassLoader(getClass.getClassLoader)

    // 1.创建一个程序与邮件服务器会话对象 Sessionss
    val props = new java.util.Properties
    props.setProperty("mail.transport.protocol", "smtp")
    props.setProperty("mail.smtp.host", PropertiesReader.getAttrValByName(prefix+"_SMTP_HOST"))
    props.setProperty("mail.smtp.port", "25")
    // 指定验证为true
    props.setProperty("mail.smtp.auth", PropertiesReader.getAttrValByName(prefix+"_SMTP_AUTH"))
    props.setProperty("mail.smtp.timeout", "1000")
    // 验证账号及密码，密码需要是第三方授权码
    val auth = new Authenticator() {
      override def getPasswordAuthentication = new PasswordAuthentication(PropertiesReader.getAttrValByName(prefix+"_SMPT_USER"),
                                                                          PropertiesReader.getAttrValByName(prefix+"_SMTP_PASSWORD"))
    }
    val session = Session.getInstance(props, auth)
    //2、通过session得到transport对象
    val ts = session.getTransport
    //3、连上邮件服务器
    ts.connect(PropertiesReader.getAttrValByName(prefix+"_SMTP_HOST"),
               PropertiesReader.getAttrValByName(prefix+"_SMPT_USER"),
               PropertiesReader.getAttrValByName(prefix+"_SMTP_PASSWORD"))
    //4、创建邮件
    val message = createAttachMail(prefix, from, session, receiverEmail, subject, content, attachment)
    //5、发送邮件
    ts.sendMessage(message, message.getAllRecipients)
    ts.close()
  }

  /**
    * 创建一封带附件的邮件
    */
  private def createAttachMail(prefix: String, from: String, session: Session,
                               receiverEmail: String, subject: String,
                               content: String, attachment: String): MimeMessage = {
    val message = new MimeMessage(session)
    //设置邮件的基本信息

    //发件人
    var fromMail = PropertiesReader.getAttrValByName(prefix+"_FROM_EMAIL")
    if(from != null){
      fromMail = from
    }
    message.setFrom(new InternetAddress(fromMail))
    //收件人
    message.setRecipients(Message.RecipientType.TO, receiverEmail.split(";").map(new InternetAddress(_)).toArray[Address])
    //邮件标题
    message.setSubject(subject)
    //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
    val text = new MimeBodyPart
    text.setContent(content, "text/html;charset=UTF-8")
    //创建容器描述数据关系
    val mp = new MimeMultipart
    mp.addBodyPart(text)
    mp.setSubType("mixed")
    //创建邮件附件
    if (new File(attachment).exists) {
      val attach = new MimeBodyPart
      val dh = new DataHandler(new FileDataSource(attachment))
      attach.setDataHandler(dh)
      attach.setFileName(MimeUtility.encodeText(dh.getName))
      mp.addBodyPart(attach)
    }
    message.setContent(mp)
    message.saveChanges()
    //返回生成的邮件
    message
  }

  def main(args: Array[String]): Unit = {
    sendEmail(CAPITAL_EMAIL_PREFIX, "xxwang-2@today36524.com.cn", "报表生成", "报表生成内容")
  }
}
