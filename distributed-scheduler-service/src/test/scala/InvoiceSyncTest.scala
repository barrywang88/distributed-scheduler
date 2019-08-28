import org.junit.Test
import org.quartz.Job
import org.springframework.test.annotation.Rollback


/**
  * @author lotey
  * @date 2018/5/11 15:39
  * @version 0.0.1
  */
class InvoiceSyncTest extends SpringServiceTest {

  var job: Job = _

  @Test
  @Rollback(false)
  def transcaionalTest: Unit = {
    //    //获取昨天日期
//    val cal: Calendar = Calendar.getInstance
//    cal.add(Calendar.DATE, -1)
//    val preDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime)
//    val request = InsertDateRequest(preDate, Some(preDate))
//    new PO2SyncAction(request).execute

  }
}