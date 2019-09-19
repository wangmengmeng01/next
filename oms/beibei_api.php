<?php
/**
 * Notes:孝感仓入口
 * Date: 2019/6/13
 * Time: 9:48
 */

header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
error_reporting(0);
date_default_timezone_set('Asia/Shanghai');

define('ROOT_DIR', dirname(realpath(__FILE__)));
define('API_ROOT', ROOT_DIR . '/api/');
define('APP_ROOT', __DIR__);
require_once './config.php';
require_once API_ROOT . 'ext/xml.php';
require_once API_ROOT . 'ext/util.php';
require_once API_ROOT . 'ext/httpclient.php';
require_once API_ROOT . 'msg.php';
require_once API_ROOT . 'func.php';
require_once API_ROOT . 'api_config.php';
require_once API_ROOT . 'beibei_service.php';
$serviceObj = new beibei_service();
$serviceObj->process();