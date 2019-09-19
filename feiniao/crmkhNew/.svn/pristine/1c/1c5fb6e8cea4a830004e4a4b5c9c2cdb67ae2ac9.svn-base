CREATE TABLE `crmkhv2_log_suckdata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creat_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `log_info` varchar(3000) DEFAULT NULL,
  `log_type` tinyint(4) DEFAULT NULL,
  `event_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2289 DEFAULT CHARSET=utf8;

CREATE TABLE `crmkhv2_record_suck` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `suck_date` int(11) DEFAULT NULL COMMENT '抽数目标日，int型yyyyMMdd',
  `creat_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `gp_nums` bigint(20) DEFAULT '0' COMMENT '抽数开始时候GP的数据条数',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除，0正常-1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;


ALTER TABLE `bootdo_sys_task`
ADD COLUMN `ext_data`  varchar(255) NULL COMMENT '扩展参数,json字符串，map形式，里面放着供task使用的扩展参数' AFTER `job_name`;

