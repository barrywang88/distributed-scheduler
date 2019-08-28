# distributed-scheduler
可配置分布式定时任务

关键词：

1. 定时任务

2. 分布式

3. 可动态配置触发时间

一般通过Quartz实现定时任务很简单。如果实现分布式定时任务需要结合分布式框架选择master节点触发也可以实现。但我们有个实际需求是，页面可动态配置定时任务触发周期（比如，假如下班前如果把先决条件完成了，正常可以18:00触发完成批量任务，假如完不成，需要将任务设置到很晚，等到先决条件完成再触发）。这个时候需要满足1,2,3都要满足。这样实现起来就有一定难度了。

下面来看看我是怎样实现的，如有更好的实现方案，欢迎在评论区提出，谢谢！

1. Quartz实现定时任务

我通过一个工具类来实现，如下：　

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
98
99
100
101
102
103
104
105
106
107
108
109
110
111
112
113
114
115
116
117
118
119
120
121
122
123
124
125
126
127
128
129
130
131
132
133
134
135
136
137
138
139
140
141
142
143
144
145
146
147
148
149
150
151
152
153
154
155
156
157
158
159
160
161
162
163
164
165
166
167
168
169
170
171
172
173
174
175
176
177
178
179
180
181
182
183
184
185
186
187
188
189
190
191
192
193
194
195
196
197
198
199
200
201
202
203
204
205
206
207
208
209
210
211
212
213
214
215
216
217
218
219
220
221
222
223
224
225
226
227
228
229
230
231
232
233
234
235
236
237
238
239
240
241
242
import java.text.SimpleDateFormat
import java.util.Date
 
import org.quartz.{CronScheduleBuilder, CronTrigger, JobBuilder, JobKey, TriggerBuilder, TriggerKey, _}
import org.quartz.impl.StdSchedulerFactory
 
import scala.collection.JavaConverters._
 
/**
  * 定时任务管理类
  *
  * @author BarryWang create at 2018/5/11 14:22
  * @version 0.0.1
  */
object QuartzManager {
  private val stdSchedulerFactory = new StdSchedulerFactory
  private val JOB_GROUP_NAME = "JOB_GROUP_NAME"
  private val TRIGGER_GROUP_NAME = "TRIGGER_NAME"
 
  /**
    * 根据指定格式(yyyy-MM-dd HH:mm:ss)时间字符串添加定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName  任务名
    * @param time     时间设置，参考quartz说明文档
    * @param jobClass 任务类名
    */
  def addJobByTime(jobName: String, time: String, jobClass: Class[_ <: Job]) : Unit = {
    QuartzManager.addJobByTime(jobName, time, jobClass, Map("1"->"otherData"))
  }
 
  /**
    * 根据指定时间(java.util.Date)添加定时任务，使用默认的任务组名，触发器名，触发器组名
    *
    * @param jobName 任务名
    * @param date 日期
    * @param jobClass 任务类名
    */
  def addJobByDate(jobName: String, date: Date, jobClass: Class[_ <: Job]): Unit = {
    QuartzManager.addJobByDate(jobName, date, jobClass, Map("1"->"otherData"))
  }
 
  /**
    * 根据指定cron表达式添加定时任务，使用默认的任务组名，触发器名，触发器组名
    *
    * @param jobName 任务名
    * @param jobClass 任务类名
    * @param cron cron表达式
    */
  def addJobByCron(jobName: String, cron : String, jobClass: Class[_ <: Job]): Unit = {
    QuartzManager.addJobByCron(jobName, cron, jobClass, Map("1"->"otherData"))
  }
 
  /**
    * 函数描述: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName    任务名
    * @param time       时间字符串, 格式为(yyyy-MM-dd HH:mm:ss)
    * @param jobClass   任务类名
    * @param paramsMap  定时器需要额外数据
    */
  def addJobByTime(jobName: String, time: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    addJobByTime(jobName, time, "yyyy-MM-dd HH:mm:ss", jobClass, paramsMap)
  }
 
  /**
    * 函数描述: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName    任务名
    * @param time       时间设置，参考quartz说明文档
    * @param jobClass   任务类名
    * @param paramsMap  定时器需要额外数据
    */
  def addJobByTime(jobName: String, time: String, timePattern: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    val df = new SimpleDateFormat(timePattern)
    val cron = getCron(df.parse(time))
    addJobByCron(jobName, cron, jobClass, paramsMap)
  }
 
  /**
    * Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    *
    * @param jobName 任务名
    * @param date 日期
    * @param cls 任务
    * @param paramsMap  定时器需要额外数据
    */
  def addJobByDate(jobName: String, date: Date, cls: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
      val cron = getCron(date)
      addJobByCron(jobName, cron, cls, paramsMap)
  }
 
  /**
    * 函数描述: 根据cron表达式添加定时任务(默认触发器组名及任务组名)
    * @param jobId            任务ID
    * @param cron             时间设置 表达式，参考quartz说明文档
    * @param jobClass         任务的类
    * @param paramsMap        可变参数需要进行传参的值
    */
  def addJobByCron(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    addJob(jobId, cron, jobClass, paramsMap, JOB_GROUP_NAME, TRIGGER_GROUP_NAME)
  }
 
  /**
    * 函数描述: 根据cron表达式添加定时任务
    * @param jobId            任务ID
    * @param cron             时间设置 表达式，参考quartz说明文档
    * @param jobClass         任务的类类型  eg:TimedMassJob.class
    * @param paramsMap        可变参数需要进行传参的值
    * @param jobGroupName     任务组名
    * @param triggerGroupName 触发器组名
    */
  def addJob(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef],
             jobGroupName: String, triggerGroupName: String): Unit = {
      val scheduler = stdSchedulerFactory.getScheduler
      // 任务名，任务组，任务执行类
      val jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobId, jobGroupName).build
       //设置参数
      jobDetail.getJobDataMap.putAll(paramsMap.asJava)
 
      val triggerBuilder = TriggerBuilder.newTrigger
      // 触发器名,触发器组
      //默认设置触发器名与任务ID相同
      val triggerName = jobId
      triggerBuilder.withIdentity(triggerName, triggerGroupName)
      triggerBuilder.startNow
      // 触发器时间设定
      triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron))
      // 创建Trigger对象
      val trigger = triggerBuilder.build.asInstanceOf[CronTrigger]
      // 调度容器设置JobDetail和Trigger
      scheduler.scheduleJob(jobDetail, trigger)
      // 启动
      if (!scheduler.isShutdown) scheduler.start()
  }
 
  /**
    * 函数描述: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
    * @param cron 时间字符串
    */
  def modifyJobTime(jobId: String, cron: String, jobClass: Class[_ <: Job]): Unit = {
    modifyJobTime(jobId, cron, jobClass, Map("1"->"otherData"), JOB_GROUP_NAME, TRIGGER_GROUP_NAME)
  }
 
  /**
    * 函数描述: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
    * @param cron 时间字符串
    */
  def modifyJobTime(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    modifyJobTime(jobId, cron, jobClass, paramsMap, JOB_GROUP_NAME, TRIGGER_GROUP_NAME)
  }
 
  /**
    * 函数描述: 修改一个任务的触发时间
    * @param jobId 任务ID
    * @param cron cron表达式
    * @param jobClass 任务类名
    * @param paramsMap 其他参数
    * @param jobGroupName 任务组名
    * @param triggerGroupName 触发器组
    */
  def modifyJobTime(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef],
                    jobGroupName: String, triggerGroupName: String): Unit = {
    val scheduler = stdSchedulerFactory.getScheduler()
    //默认设置触发器名与任务ID相同
    val triggerName = jobId
    val triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName)
    var trigger = scheduler.getTrigger(triggerKey).asInstanceOf[CronTrigger]
    if (trigger != null) {
      removeJob(jobId)
    }
    addJob(jobId, cron, jobClass, paramsMap, jobGroupName, triggerGroupName)
  }
 
  /**
    * 函数描述: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
    * @param jobId 任务名称
    */
  def removeJob(jobId: String): Unit = {
    val scheduler = stdSchedulerFactory.getScheduler
    //默认设置触发器名与任务ID相同
    val triggerName = jobId
    val triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP_NAME)
    // 停止触发器
    scheduler.pauseTrigger(triggerKey)
    // 移除触发器
    scheduler.unscheduleJob(triggerKey)
    // 删除任务
    scheduler.deleteJob(JobKey.jobKey(jobId , JOB_GROUP_NAME))
  }
 
  /**
    * 函数描述: 移除一个任务
    * @param jobId 任务ID
    * @param jobGroupName 任务组
    * @param triggerName 触发器名称
    * @param triggerGroupName 触发器组名
    */
  def removeJob(jobId: String, jobGroupName: String, triggerName: String, triggerGroupName: String): Unit = {
    val scheduler = stdSchedulerFactory.getScheduler
    val triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName)
    // 停止触发器
    scheduler.pauseTrigger(triggerKey)
    // 移除触发器
    scheduler.unscheduleJob(triggerKey)
    // 删除任务
    scheduler.deleteJob(JobKey.jobKey(jobId , jobGroupName))
  }
 
  /**
    * 函数描述:启动所有定时任务
    */
  def startJobs(): Unit = {
      stdSchedulerFactory.getScheduler.start()
  }
 
  /**
    * 函数描述:关闭所有定时任务
    *
    */
  def shutdownJobs(): Unit = {
      val sched = stdSchedulerFactory.getScheduler
      if (!sched.isShutdown) sched.shutdown()
  }
 
  /**
    * 根据时间获取Cron表达式
    * @param date 日期
    * @return
    */
  def getCron(date: Date): String = {
    val dateFormat = "ss mm HH dd MM ? yyyy"
    formatDateByPattern(date, dateFormat)
  }
 
  /**
    * 日期格式转换
    * @param date 日期
    * @param dateFormat 格式
    * @return
    */
  def formatDateByPattern(date : Date, dateFormat : String): String = {
    val sdf = new SimpleDateFormat(dateFormat)
    sdf.format(date)
  }
}
　　



2.分布式定时任务实现
分布式定时任务：即在分布式服务的环境下，启动定时任务。分布式定时任务需要解决的问题是：同一定时任务，同一时间点，只能在一台服务器上启动。
为了解决分布式定时任务的问题，我们需要框架层面解决定时任务触发时，选举一台服务器作为master节点。
实现思路如下：
1. 服务启动注册服务时，为服务编号；
2．从注册服务中随机选中一台服务器作为master节点；
3. 服务挂掉或添加时重新选举。

代码如下：　
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
98
99
100
101
102
103
104
105
106
107
108
109
110
111
112
113
114
115
import java.util.HashMap;
import java.util.Map;
 
/**
 * Created by tangliu on 2016/7/13.
 */
public class MasterHelper {
 
    public static Map<String, Boolean> isMaster = new HashMap<>();
 
    /**
     * 根据serviceName, versionName，判断当前服务是否集群中的master
     *  todo 服务版本号是否作为master判断的依据??
     * @param servieName
     * @param versionName
     * @return
     */
    public static boolean isMaster(String servieName, String versionName) {
 
        String key = generateKey(servieName, versionName);
 
        if (!isMaster.containsKey(key))
            return false;
        else
            return isMaster.get(key);
 
    }
 
    public static String generateKey(String serviceName, String versionName) {
        return serviceName + ":" + versionName;
    }
}
 
竞选master:
/**
 * 监听服务节点下面的子节点（临时节点，实例信息）变化
 */
public void watchInstanceChange(RegisterContext context) {
    String watchPath = context.getServicePath();
    try {
        List<String> children = zk.getChildren(watchPath, event -> {
            LOGGER.warn("ServerZk::watchInstanceChange zkEvent:" + event);
            //Children发生变化，则重新获取最新的services列表
            if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                LOGGER.info("容器状态:{}, {}子节点发生变化，重新获取子节点...", ContainerFactory.getContainer().status(), event.getPath());
                if (ContainerFactory.getContainer().status() == Container.STATUS_SHUTTING
                        || ContainerFactory.getContainer().status() == Container.STATUS_DOWN) {
                    LOGGER.warn("Container is shutting down");
                    return;
                }
                watchInstanceChange(context);
            }
        });
        boolean _isMaster = false;
        if (children.size() > 0) {
            _isMaster = checkIsMaster(children, MasterHelper.generateKey(context.getService(), context.getVersion()), context.getInstanceInfo());
        }
        //masterChange响应
        LifecycleProcessorFactory.getLifecycleProcessor().onLifecycleEvent(
                new LifeCycleEvent(LifeCycleEvent.LifeCycleEventEnum.MASTER_CHANGE,
                        context.getService(), _isMaster));
    } catch (KeeperException | InterruptedException e) {
        LOGGER.error(e.getMessage(), e);
        create(context.getServicePath() + "/" + context.getInstanceInfo(), context, true);
    }
}
 
//-----竞选master---
private static Map<String, Boolean> isMaster = MasterHelper.isMaster;
 
/**
 * @param children     当前方法下的实例列表，        eg 127.0.0.1:9081:1.0.0,192.168.1.12:9081:1.0.0
 * @param serviceKey   当前服务信息                eg com.github.user.UserService:1.0.0
 * @param instanceInfo 当前服务节点实例信息         eg  192.168.10.17:9081:1.0.0
 */
public boolean checkIsMaster(List<String> children, String serviceKey, String instanceInfo) {
    if (children.size() <= 0) {
        return false;
    }
 
    boolean _isMaster = false;
 
    /**
     * 排序规则
     * a: 192.168.100.1:9081:1.0.0:0000000022
     * b: 192.168.100.1:9081:1.0.0:0000000014
     * 根据 lastIndexOf :  之后的数字进行排序，由小到大，每次取zk临时有序节点中的序列最小的节点作为master
     */
    try {
        Collections.sort(children, (o1, o2) -> {
            Integer int1 = Integer.valueOf(o1.substring(o1.lastIndexOf(":") + 1));
            Integer int2 = Integer.valueOf(o2.substring(o2.lastIndexOf(":") + 1));
            return int1 - int2;
        });
 
        String firstNode = children.get(0);
        LOGGER.info("serviceInfo firstNode {}", firstNode);
 
        String firstInfo = firstNode.replace(firstNode.substring(firstNode.lastIndexOf(":")), "");
 
        if (firstInfo.equals(instanceInfo)) {
            isMaster.put(serviceKey, true);
            _isMaster = true;
            LOGGER.info("({})竞选master成功, master({})", serviceKey, CURRENT_CONTAINER_ADDR);
        } else {
            isMaster.put(serviceKey, false);
            _isMaster = false;
            LOGGER.info("({})竞选master失败，当前节点为({})", serviceKey);
        }
    } catch (NumberFormatException e) {
        LOGGER.error("临时节点格式不正确,请使用新版，正确格式为 etc. 192.168.100.1:9081:1.0.0:0000000022");
    }
 
    return _isMaster;
}
　　

分布式环境判断是否是master：
1
2
3
if (!MasterHelper.isMaster("com.today.api.financetask.service.FinanceScheduledService", "1.0.0")) {
  //excute the task
}
　　


3. 动态配置定时任务触发时间
实现定时任务可动态配置，需要通过数据库表保存最新一次修改的cron表达式来实现：
建表如下：
复制代码
CREATE TABLE t_scheduled_task  (
      job_name  varchar(50)    NOT NULL     COMMENT 'job名称'
    ,  job_id  varchar(40)    NOT NULL     COMMENT 'job ID'
    ,  job_cron  varchar(50)    NOT NULL     COMMENT 'Job cron表达式'
    ,  job_type  int         COMMENT 'Job类型'
    ,  is_start  tinyint(2)   DEFAULT 1 NOT NULL     COMMENT '是否已启动,0:否(no);1:是(yes)'
    ,  remark  varchar(256)         COMMENT '备注'
    ,  updated_at  timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL     COMMENT '更新时间'
    ,  created_at  timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL     COMMENT '创建时间'
    ,  has_deleted  tinyint(2)   DEFAULT 0 NOT NULL     COMMENT '是否已删除,0:否(no);1:是(yes)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务配置表';
CREATE  UNIQUE INDEX uk_t_scheduled_task ON t_scheduled_task ( job_id);
复制代码
 


建表后需要页面调用接口实现先停掉上次的定时任务，再根据最新修改的触发时间新建一个新的定时任务：
如下：
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
//页面设置每天触发的时间，格式：HH:mm
val cron = convertHourMinuteToCron(processTime)
//修改定时任务时间, 保存入库
ScheduledTaskQuerySql.isExists(jobId) match {
  case true => ScheduledTaskActionSql.updateTaskCron(jobId, cron)
  case false => ScheduledTaskActionSql.insertTScheduledTask(
    TScheduledTask( report.name,
                    jobId,
                    cron,
                    None,
                    TScheduledTaskIsStartEnum.YES.id,
                    None,
                    null,
                    null,
                    TScheduledTaskHasDeletedEnum.NO.id))
}
//关掉老的定时任务，添加新的定时任务
QuartzManager.modifyJobTime(jobId, cron, classOf[DailyGenIncomeDetailJob])
　　

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
/**
  * 每天定时触发-转换时分格式(hh:mm)为cron表达式
  * @param hourMibuteStr
  * @return
  */
def convertHourMinuteToCron(hourMibuteStr : String) : String = {
  val splitHm = hourMibuteStr.split(":")
  s"0 ${splitHm(1).trim.toInt} ${splitHm(0).trim.toInt} * * ?"
}
 
/**
  * 每天定时触发-转换时分格式(hh:hh:mm)为cron表达式
  * @param dayStr
  * @return
  */
def convertDayToCron(dayStr : String) : String = {
  val splitHm = dayStr.split(":")
  s"0 ${splitHm(1).trim.toInt} ${splitHm(0).trim.toInt} * * ?"
}
　　

定义Job及父类:

Job定义

复制代码
import java.util.Calendar

import com.today.api.checkaccount.scala.enums.FlatFormTypeEnum
import com.today.api.checkaccount.scala.request.ReconciliationRequest
import com.today.api.checkaccount.scala.CheckAccountServiceClient
import com.today.service.financetask.job.define.{AbstractJob, JobEnum}
import org.quartz.JobExecutionContext

class CheckAccountJob extends AbstractJob{
  /**
    * get the api information
    *
    * @return (interface name, interface version, JobEnum)
    */
  override def getJobAndApiInfo(context: JobExecutionContext): (String, String, JobEnum) = {
    ("com.today.api.financetask.service.CloseAccountScheduleService", "1.0.0",  JobEnum.CHECK_ACCOUNT_PROCESS)
  }

  /**
    * start up the scheduled task
    *
    * @param context JobExecutionContext
    */
  override def run(context: JobExecutionContext): Unit = {
    val cal = Calendar.getInstance
    cal.add(Calendar.DATE, -1)
    new CheckAccountServiceClient().appReconciliation(new ReconciliationRequest(FlatFormTypeEnum.TODAY_APP,None))

  }
}
 
复制代码
公共父类

复制代码
import java.io.{PrintWriter, StringWriter}

import com.github.dapeng.core.helper.MasterHelper
import com.today.api.financetask.scala.enums.TScheduledTaskLogEnum
import com.today.service.financetask.action.sql.ScheduledTaskLogSql
import com.today.service.financetask.util.{AppContextHolder, Debug}
import org.quartz.{Job, JobExecutionContext}
import org.slf4j.LoggerFactory
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionTemplate

import scala.util.{Failure, Success, Try}

/**
  * the abstract class for job
  */
trait AbstractJob extends Job{
  /** 日志 */
  val logger = LoggerFactory.getLogger(getClass)

  /**
    * execute the job
    * @param context
    */
  override def execute(context: JobExecutionContext): Unit = {
    val jobAndApiInfo = getJobAndApiInfo(context)
    if (!MasterHelper.isMaster(jobAndApiInfo._1, jobAndApiInfo._2)) {
      logger.info(s"Can't select master to run the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName}")
      return
    }

    val logId = ScheduledTaskLogSql.insertScheduledTaskLog(jobAndApiInfo._3)
    context.put("logId", logId)
    logger.info(s"Starting the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName} ...")

    /**
      * 事物处理
      */
    val transactionTemplate: TransactionTemplate = AppContextHolder.getBean("transactionTemplate")
    transactionTemplate.execute((status: TransactionStatus) =>{
      Debug.reset()
      Try(Debug.trace(s"${jobAndApiInfo._3.jobId}:${jobAndApiInfo._3.jobName}")(run(context))) match
      {
        case Success(x) => {
          logger.info(s"Successfully execute the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName}")
          successLog(logId)
        }
        case Failure(e) => {
          logger.error(s"Failure execute the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName}", e)
          failureLog(logId, status, e)
        }
      }
      Debug.info()
    })
  }

  /**
    * get the api information
    * @return (interface name, interface version, JobEnum)
    */
  def getJobAndApiInfo(context: JobExecutionContext) : (String, String, JobEnum)

  /**
    * start up the scheduled task
    * @param context JobExecutionContext
    */
  def run(context: JobExecutionContext)

  /**
    * 成功日志记录
    * @param logId
    */
  def successLog(logId: Long): Unit ={
    ScheduledTaskLogSql.updateExportReportRecord(logId, TScheduledTaskLogEnum.SUCCESS, "Success")
  }

  /**
    * 失败日志记录
    * @param logId
    */
  def failureLog(logId: Long, status: TransactionStatus, e: Throwable): Unit ={
    status.setRollbackOnly()
    ScheduledTaskLogSql.updateExportReportRecord(logId, TScheduledTaskLogEnum.FAILURE, getExceptionStack(e))
  }

  /**
    *
    * 功能说明:在日志文件中 ，打印异常堆栈
    * @param e : Throwable
    * @return : String
    */
  def getExceptionStack(e: Throwable): String = {
    val errorsWriter = new StringWriter
    e.printStackTrace(new PrintWriter(errorsWriter))
    errorsWriter.toString
  }
}
复制代码
 


4. 重启服务器，启动所有定时任务
重启定时任务，需要重启所有定时任务
这个过程需要将所有定时任务及触发周期保存到数据库，重启后，读取数据库启动恢复所有定时任务
代码如下(Spring框架下启动服务自动会启动ApplicationListener.onApplicationEvent(event: ContextRefreshedEvent))：
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
import com.today.api.financetask.scala.enums.{TReportTypeEnum, TScheduledTaskIsStartEnum}
import com.today.api.financetask.scala.request.QueryAutoConfigRequest
import com.today.service.financetask.job._
import com.today.service.financetask.job.define.JobEnum
import com.today.service.financetask.query.sql.{AutoConfigQuerySql, ScheduledTaskQuerySql}
import com.today.service.financetask.util.QuartzManager
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service
 
/**
  *  类功能描述: 定时器监听器, 服务启动时启动定时器
  *
  * @author BarryWang create at 2018/5/11 12:04
  * @version 0.0.1
  */
@Service
class ScheduleStartListener extends ApplicationListener[ContextRefreshedEvent] {
  /** 日志 */
  val logger = LoggerFactory.getLogger(getClass)
  /**
    * 启动加载执行定时任务
    */
  override def onApplicationEvent(event: ContextRefreshedEvent): Unit = {
    logger.info("=======服务器重启定时任务启动start=======")
    //1. 恢复日次处理定时任务
    recoveryDayTimeProcessJob()
    //2. 恢复每天营收明细报表生成定时任务
    recoveryImcomeDetailGenJob()
    logger.info("=======服务器重启定时任务启动end=======")
  }
 
  /**
    * 恢复日次处理定时任务
    */
  private def recoveryDayTimeProcessJob(): Unit ={
    try {
      ScheduledTaskQuerySql.queryByJobId(JobEnum.DAY_TIME_PROCESS.jobId) match {
        case Some(x) =>
          if(x.isStart == TScheduledTaskIsStartEnum.YES.id)
            QuartzManager.addJobByCron(JobEnum.DAY_TIME_PROCESS.jobId, x.jobCron, classOf[DayTimeProcessJob])
          else
            logger.info("定时任务：" + JobEnum.DAY_TIME_PROCESS.jobName  + "is_start标志为0，不启动")
        case None =>
          QuartzManager.addJobByCron(JobEnum.DAY_TIME_PROCESS.jobId, "0 30 2 * * ?", classOf[DayTimeProcessJob])
      }
    } catch {
      case e : Exception => logger.error(JobEnum.DAY_TIME_PROCESS.jobName + "启动失败， 失败原因：", e)
    }
 
  }
 
  /**
    * 恢复营收明细报表生成定时任务
    */
  private def recoveryImcomeDetailGenJob(): Unit = {
    val jobName = TReportTypeEnum.INCOMEDETAIL_REPORT.name
    try {
      val jobId = TReportTypeEnum.INCOMEDETAIL_REPORT.id.toString
      ScheduledTaskQuerySql.queryByJobId(jobId) match {
        case Some(x) =>
          if (x.isStart == TScheduledTaskIsStartEnum.YES.id)
            QuartzManager.addJobByCron(jobId, x.jobCron, classOf[DailyGenIncomeDetailJob])
          else
            logger.info("定时任务：" + jobName  + "is_start标志为0，不启动")
        case None =>
          QuartzManager.addJobByCron(jobId, "0 10 0 * * ?", classOf[DailyGenIncomeDetailJob])
      }
    }catch {
      case e : Exception => logger.error(jobName + "启动失败， 失败原因：", e)
    }
  }
}
　　

大家也看到上面代码会使用使用JobEnum的枚举来定义不同定时任务信息。

如下就是使用Scala枚举定义定时信息如下：

复制代码
case class JobEnum(val jobId: String, val jobName: String) extends Enumeration

/**
  * 所有Job 枚举定义在此类, 不能重复
  * jobId不能重复
  * @author BarryWang create at 2018/5/12 10:45
  * @version 0.0.1
  */
object JobEnum {
  val DAY_TIME_PROCESS = JobEnum("DAY_TIME_PROCESS", "日次处理定时任务")

  val INVOICE_SYNC_PROCESS = JobEnum("INVOICE_SYNC_PROCESS", "采购系统同步单据数据定时任务")

  val RETIREMENT_SYNC_PROCESS = JobEnum("RETIREMENT_SYNC_PROCESS", "采购系统同步报废单据数据定时任务")

  val CLOSE_ACCOUNT_STATE_PROCESS = JobEnum("CLOSE_ACCOUNT_STATE_PROCESS","更新关账状态定时任务")

  val PURCHASE_ORDER_2_SYNC_PROCESS = JobEnum("PURCHASE_ORDER_2_SYNC_PROCESS","采购系统同步PO2数据定时任务")

  val SEND_EMAIL_PROCESS = JobEnum("SEND_EMAIL_PROCESS","计划付款通知和已付款通知定时任务")

  val CLOSE_ACCOUNT_BASE_DATA_PROCESS = JobEnum("CLOSE_ACCOUNT_BASE_DATA_PROCESS","关账基础数据同步定时任务")
}
复制代码
这是可动态配置的分布式定时任务第一版已实现，但还是还是有优化的地方：

１．枚举里面只有jobId及jobName,为了启动服务时不需要每个定时任务都写一个恢复方法，可以将枚举里面定时任务信息多添加jobClass及cron表达式，这样以后添加新定时任务只需要维护枚举信息就可以；

２．每个Job子类里面都需要实现 override def getJobAndApiInfo(context: JobExecutionContext): (String, String, JobEnum) 方法，这个也可以省掉；

３．新添加定时任务完全可以制定一个Job子类，其他操作自动维护进去；

1,2请参考: Scala 定义复杂枚举 是实现，这种方式虽然做了简化，只需要定义Job子类及对应JobEnum。但理想状态所有定时任务信息只需要维护在Job子类就行了

3通过使用反射实现将所有定时任务信息只需要维护在Job子类，请参考: Java Scala获取注解的类信息

至此整个实现分布式环境下实现动态可配置的定时任务过程已完成

