<?php
return array(
    'basePath' => dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
    'name'=>'OMS API',
    'language' => 'zh_cn',
    'import'=>array(
        'application.lib.*',
        'application.lib.ext.*',
    ),
    'modules'=>array(
    ),
    'defaultController' => 'Service',
    'components'=>array(
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
        'cache'=>array(
            'class'=>'CXCache'
        ),
        'errorHandler'=>array(
            'errorAction'=>'Service/error',
        ),
        'log'=>array(
            'class'=>'CLogRouter',
            'routes'=>array(
                array(
                    'class'=>'CFileLogRoute',
                    'levels'=>'error, warning',
                    'logPath' => LOG_PATH,
                    'logFile'=> LOG_FILENAME
                )
            ),
        ),
    ),
    'params'=>require(dirname(__FILE__).'/params.php'),
);