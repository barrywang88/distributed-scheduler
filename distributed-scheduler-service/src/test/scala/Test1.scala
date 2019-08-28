

/**
  * @author zhangc
  * @date 2018\5\16 0016 20:40
  * @version 1.0.0
  */
object Test1 {

  def main(args: Array[String]): Unit = {

    val invoiceList = List("1","1-1")
    val selectDeliveryList = List("1")
    val insertDeliveryList = selectDeliveryList.filterNot(x => (invoiceList.contains(x + "-1") || invoiceList.contains(x + "-2")))
    println(insertDeliveryList)
  }
}
