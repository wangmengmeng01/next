<?php
	//天蚕公共配置项(必配项)
	define('ZOOKEEPER_IDC', 'ifm');//qconf连接天蚕zookeeper集群地址名称
	define('ZOOKEEPER_ROOT_DIR', '/IFM/');//天蚕根节点	
	
	//天蚕被调配置项(被调时必配项)
	define('CHECK_TIANCAN', 0);//是否开启天蚕验证,1为开启
	define('PROJECT_NAME', 'oms');//项目ID，注册天蚕时使用该ID  
	define('LOGGER_PHP_PATH', dirname(dirname(__FILE__)).'/tiancan/log4php/Logger.php');//log4php日志类文件地址
	define('TIANCAN_LOG_CONFIG_PATH', dirname(dirname(__FILE__)).'/tiancan/tcAppenderRollingFile.xml');//log4php日志配置文件地址
	define('API_RETURN_DATA_CODE_FIELD', 'code');//接口返回数据状态值字段常量
	define('API_RETURN_DATA_MESSAGE_FIELD', 'infos');//接口返回数据提示信息字段常量
	
	//天蚕主调配置项(主调时必配项)
	define('ZOOKEEPER_PROJECT_SYSTEM', 'oms');//系统在天蚕注册ID