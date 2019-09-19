<?php
return array(
    'basePath' => dirname(__FILE__) . DIRECTORY_SEPARATOR . '..',
    'name' => 'OMS 订单管理系统',
    'preload' => array(
        'log'
    ),
    'language' => 'zh_cn',
    'import' => array(
        'application.models.*',
        'application.components.*',
        'application.extensions.phpexcel.*',
    	'application.extensions.log4php.*'
    ),
    'modules' => array(
        'base',
        'inbound',
        'outbound',
        'inventory',
        'interfaceLog'
//         'gii' => array(
//             'class' => 'system.gii.GiiModule',
//             'password' => '1'
//         )
    ),
    'defaultController' => 'home',
    'components' => array(
        'user' => array(
            'allowAutoLogin' => true,
            'loginUrl' => array(
                'site/login'
            )
        ),
        'db' => array(
            'connectionString' => 'mysql:host=' . DB_EM_HOST . ';dbname=' . DB_EM_NAME,
            // 缓存表结构
            // 'schemaCachingDuration'=>3600,
            'emulatePrepare' => true,
            // 在log中显示参数值而不是占位符
            'enableParamLogging' => true,
            'username' => DB_EM_USER,
            'password' => DB_EM_PASS,
            'charset' => DB_EM_CHAR,
            'tablePrefix' => DB_EM_PREFIX
        ),
        'db1'=>array(
            'connectionString' => 'mysql:host=' . DB_JD_HOST . ';dbname=' . DB_JD_NAME,
            
            'emulatePrepare' => true,
            // 在log中显示参数值而不是占位符
            'enableParamLogging' => true,
            'username' => DB_JD_USER,
            'password' => DB_JD_PASS,
            'charset' => DB_JD_CHAR
        ),
        'cache' => array(
            'class' => 'CXCache'
        ),
        'errorHandler' => array(
            'errorAction' => 'site/error'
        ),
        'log' => array(
            'class' => 'CLogRouter',
            'routes' => array(
                array(
                    'class' => 'CFileLogRoute',
                    'levels' => 'trace, info, error, warning'
                ),
                array(
                    'class' => 'CFileLogRoute',
                    'levels' => 'trace', // 级别为trace
                    'categories' => 'system.db.*'
                ),
            ),
        ),
    ),
); 
// 只显示关于数据库信息,包括数据库连接,数据库执行语句


// 'params'=>require(dirname(__FILE__).'/params.php'),
