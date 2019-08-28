USE distributed_scheduler_db;

CREATE TABLE `t_scheduled_task_log` (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT '主键ID',
  `job_id` varchar(40) NOT NULL DEFAULT '' COMMENT 'job ID',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '定时任务状态,0:已开始(started);1:执行成功(success);2:执行失败(failure)',
  `remark`  varchar(5126) NOT NULL DEFAULT '' COMMENT '备注',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务执行记录表'