use distributed_scheduler_db;
CREATE TABLE `id_info` (
  `biz_tag` varchar(50) DEFAULT NULL COMMENT '业务标识',
  `max_id` bigint(20) DEFAULT NULL COMMENT '分配的id号段的最大值',
  `step` bigint(20) DEFAULT NULL COMMENT '步长',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `biz_tag_unique` (`biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='号段存储表';
INSERT INTO `id_info`(biz_tag,max_id,step) VALUES ('scheduled_task_log_id', '1', '1024');
