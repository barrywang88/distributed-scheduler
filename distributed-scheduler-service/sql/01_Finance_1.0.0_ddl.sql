/**
* 创建数据库
*/
CREATE DATABASE distributed_scheduler_db CHARACTER SET utf8mb4;
USE distributed_scheduler_db;

SET FOREIGN_KEY_CHECKS = 0;

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
