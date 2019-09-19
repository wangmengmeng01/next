<?php
header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
error_reporting(0);
date_default_timezone_set('Asia/Shanghai');

define('ROOT_DIR', dirname(realpath(__FILE__)));
define('API_ROOT', ROOT_DIR . '/api');
define('APP_ROOT', __DIR__);

require_once './config.php';
require_once API_ROOT . '/config/config.php';
require_once API_ROOT . '/ext/xml.php';
require_once API_ROOT . '/ext/util.php';
require_once API_ROOT . '/ext/httpclient.php';
require_once API_ROOT . '/msg.php';
require_once API_ROOT . '/func.php';
require_once API_ROOT . '/ext/taobao-sdk-PHP-auto_1453274410736-20160120/TopSdk.php';
require_once API_ROOT . '/ext/taobao_sdk/TopSdk.php';
require_once API_ROOT . '/ext/jos_sdk/JdSdk.php';
require_once API_ROOT . '/config/DbAction.php';

//接入天蚕
if (CHECK_TIANCAN == 1 && $_REQUEST['appkey'] == 'YWMS') {
    require_once API_ROOT . '/ext/tiancan/tiancan.php';
    require_once API_ROOT . '/ext/tiancan/config_tc.php';

    //配置接口返回字段属性，用于配置天蚕校验返回与接口返回数据格式保持一致(必写项)
    $apiReturnDataType = '<?xml version="1.0" encoding="utf-8"?><error_response><code>%s</code><msg>%s</msg></error_response>';
    $check_tc = new tiancan($apiReturnDataType);
    $isPass = $check_tc->tiancanUnactive();
    $xmlObj = new xml();
    $result = $xmlObj->xmlStr2array($isPass);
    if ($result['code'] != 'SUCC') die($isPass);
}

//连接数据库
$db = connectDb();
$jdDb = connectJdDb();

require API_ROOT . '/cainiao_service.php';
$serviceObj = new cainiao_service();
$serviceObj->process();
?>
