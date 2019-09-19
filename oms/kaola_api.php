<?php
header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
error_reporting(0);
date_default_timezone_set('Asia/Shanghai');

define('ROOT_DIR', dirname(realpath(__FILE__)) . '/');
define('API_ROOT', ROOT_DIR . '/api/');
define('APP_ROOT', __DIR__ . '/');

require_once './config.php';
require_once API_ROOT . 'ext/xml.php';
require_once API_ROOT . 'ext/util.php';
require_once API_ROOT . 'ext/httpclient.php';
require_once API_ROOT . 'ext/rsa.php';
require_once API_ROOT . 'msg.php';
require_once API_ROOT . 'func.php';


require API_ROOT . 'kaola_service.php';
$serviceObj = new kaola_service();
$serviceObj->process();
?>
