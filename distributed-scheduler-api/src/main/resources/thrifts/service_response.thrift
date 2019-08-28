namespace java com.barry.api.scheduler.response

/**
* 服务通用返回体
**/
struct ServiceResponse {
  /*
   * 状态码
   **/
  1: string code,
  /**
   * 提示信息
   **/
  2: string message,
  /**
   * 其他数据(多种数据结构可转换为Json字符串)
   **/
  3: optional string data,
}