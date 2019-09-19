<?php
/**
 * OMS
 * 基于Yii framework 1.1.15 开发
 * @version 1.0
 * @copyright 2015.03
 */

header('Content-Type:text/html;charset=utf-8');
header("Cache-Control: no-cache, must-revalidate"); # HTTP/1.1
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); #过去的时间
ini_set('session.cookie_httponly', '1');
date_default_timezone_set('Asia/Shanghai');
define('YII_FRAMEWORK_PATH', __DIR__ . DIRECTORY_SEPARATOR . 'framework');
define('APP_ROOT', __DIR__);
define('APP_URL', 'http://' . $_SERVER['HTTP_HOST'] . substr($_SERVER['PHP_SELF'], 0, strrpos($_SERVER['PHP_SELF'], '/') + 1));

define('YII_DEBUG', false);
define('YII_TRACE_LEVEL', 4);

require 'config.php';

$config = APP_ROOT . '/protected/config/main.php';

//加载log4php自动加载配置文件
require_once(APP_ROOT . '/protected/extensions/log4php/LoggerAutoloader.php');

require_once(YII_FRAMEWORK_PATH . DIRECTORY_SEPARATOR . 'yii.php');

Yii::createWebApplication($config)->run();