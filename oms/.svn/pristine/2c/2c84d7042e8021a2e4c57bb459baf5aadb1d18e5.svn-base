<?php

// This is the configuration for yiic console application.
// Any writable CConsoleApplication properties can be configured here.
return array(
    'basePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
    'name'=>'console',
    'import'=>array(
        'application.models.*',
        'application.components.*',
    ),

    // preloading 'log' component
    'preload'=>array('log'),

    // application components
    'components'=>array(
        'log'=>array(
            'class'=>'CLogRouter',
            'routes'=>array(
                array(
                    'class'=>'CFileLogRoute',
                    'levels'=>'error, info',
                    'logFile'=> LOG_FILENAME,
                    'logPath' => LOG_PATH
                ),
            ),
        ),
    	'db'=>array(
    				'connectionString' => 'mysql:host='.DB_EM_HOST.';dbname='.DB_EM_NAME,
    				//缓存表结构
    				//'schemaCachingDuration'=>3600,
    				'emulatePrepare' => true,
    				//在log中显示参数值而不是占位符
    				'enableParamLogging' => true,
    				'username' => DB_EM_USER,
    				'password' => DB_EM_PASS,
    				'charset' => DB_EM_CHAR,
    				'tablePrefix' => DB_EM_PREFIX,
    	),
    ),
);