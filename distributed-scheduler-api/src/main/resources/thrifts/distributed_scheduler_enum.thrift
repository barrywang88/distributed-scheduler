namespace java com.barry.api.scheduler.enums
/**
* 是否已启动
**/
enum TScheduledTaskIsStartEnum{
   /**
   * 否
   **/
   NO = 0,
   /**
   * 是
   **/
   YES = 1,
}
/**
* 是否已删除
**/
enum TScheduledTaskHasDeletedEnum{
   /**
   * 否
   **/
   NO = 0,
   /**
   * 是
   **/
   YES = 1,
}

/**
* 定时任务执行状态
**/
enum TScheduledTaskLogEnum{
   /**
   * 已开始
   **/
   STARTED = 0,
   /**
   * 执行成功
   **/
   SUCCESS = 1,
  /**
  * 执行失败
  **/
  FAILURE = 2,
}