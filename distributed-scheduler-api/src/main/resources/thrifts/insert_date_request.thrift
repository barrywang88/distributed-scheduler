namespace java com.barry.api.scheduler.request

/**
* 插入日期范围请求体
* 如果结束日期为空则表示同步大于起始日期之前的；
* 如果结束日期不为空则表示同步起始日期到结束日期之间的
**/
struct InsertDateRequest {
  /***
  * 查询的起始日期
  * 格式：yyyy-MM-dd
  **/
  1: string beginDate,
  /**
    * 查询的结束日期
    * 格式：yyyy-MM-dd
    **/
  2 : optional string  endDate,
  /**
   * 门店编号
   */
  3 : optional string  storeId,
}