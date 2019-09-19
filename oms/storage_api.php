<?php
header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
//error_reporting(0);
date_default_timezone_set('Asia/Shanghai');

define('ROOT_DIR', dirname(realpath(__FILE__)));
define('API_ROOT', ROOT_DIR . '/api');

require_once './config.php';
require_once API_ROOT . '/config/config.php';
require_once API_ROOT . '/ext/xml.php';
require_once API_ROOT . '/ext/util.php';
require_once API_ROOT . '/func.php';
require_once API_ROOT . '/msg.php';
require_once API_ROOT . '/ext/httpclient.php';

$wmsRequestArr = array(
    'WMS_INVENTORY_COUNT',
    'WMS_INVENTORY_ADJUST_UPLOAD',
    'WMS_STOCK_IN_ORDER_CONFIRM',
    'WMS_STOCK_OUT_ORDER_CONFIRM',
    'WMS_CONSIGN_ORDER_CONFIRM'
);

if (!empty($_REQUEST['method']) && in_array($_REQUEST['method'], $wmsRequestArr)) {
    require_once API_ROOT . '/ext/tiancan/tiancan.php';
    require_once API_ROOT . '/ext/tiancan/config_tc.php';
    //接入天蚕
    if (CHECK_TIANCAN == 1) {
        //配置接口返回字段属性，用于配置天蚕校验返回与接口返回数据格式保持一致(必写项)
        $apiReturnDataType = '<?xml version="1.0" encoding="utf-8"?><error_response><code>%s</code><msg>%s</msg></error_response>';
        $check_tc = new tiancan($apiReturnDataType);
        $isPass = $check_tc->tiancanUnactive();
        $xmlObj = new xml();
        $result = $xmlObj->xmlStr2array($isPass);
        if ($result['code'] != 'SUCC') die($isPass);
    }
}

//连接数据库
$db = connectDb();

require API_ROOT . '/cn_storage_service.php';
$serviceObj = new cn_storage_service();
$serviceObj->process();