import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

/**
  *
  * Desc: 测试类，在服务端测试代码使用
  * author: maple
  * Date: 2018-01-12 14:35
  *
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:META-INF/spring/services.xml"))
@Transactional
class SpringServiceTest {

}