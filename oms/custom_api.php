<?php
/**
 * 跨境贸易仓储接口入口文件custom_api.php
 * User: Renee
 * Date: 2017/12/29
 * Time: 13:57
 */
header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
error_reporting(0);
date_default_timezone_set('Asia/Shanghai');

define('ROOT_DIR', dirname(realpath(__FILE__)) . '/');
define('API_ROOT', ROOT_DIR . 'api/');

require_once './config.php';
require_once API_ROOT . 'config/config.php';
require_once API_ROOT . 'ext/xml.php';
require_once API_ROOT . 'ext/util.php';
require_once API_ROOT . 'ext/httpclient.php';
require_once API_ROOT . 'msg.php';
require_once API_ROOT . 'func.php';


//连接数据库
$db = connectDb();

require API_ROOT . 'custom_service.php';
$serviceObj = new custom_service();
$serviceObj->process();
