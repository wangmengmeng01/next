<?php
/**
 * 网易清单信息同步接口
 * User: Renee
 * Date: 2018/8/22
 * Time: 17:38
 */
header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
error_reporting(0);
date_default_timezone_set('Asia/Shanghai');

define('ROOT_DIR', dirname(realpath(__FILE__)));
define('API_ROOT', ROOT_DIR . '/api');

require_once './config.php';
require_once API_ROOT . '/ext/util.php';
require_once API_ROOT . '/ext/httpclient.php';
require_once API_ROOT.'/config/database.php';

//核放单接口（网易）【仓库->报关系统】
if ($_REQUEST['type'] == 'reviewRelease') {
    require_once API_ROOT . '/router/interface/erp/kaola/common/ReviewRelease.php';

    $releaseObj = new ReviewRelease();
    echo $releaseObj->postData($_REQUEST);
    exit();
}

if (!empty($_REQUEST['data'])) {
    $params = array(
        'data' => $_REQUEST['data']
    );
    $httpObj = new httpclient();
    $resp = $httpObj->post(KAOLA_INVT_WMS_URL,$params);
	error_log(print_r(date("Y-m-d H:i:s").$_REQUEST['data'].PHP_EOL,1),3,'/yd/oms/log/invt.log');
	error_log(print_r(date("Y-m-d H:i:s").$resp.PHP_EOL,1),3,'/yd/oms/log/invt_resp.log');
    if (empty($resp)) {
        echo '{"success":false,"error_msg":"请求超时！"}';
    } else {
        echo $resp;
    }
} elseif (!empty($_REQUEST['msg'])) {
    $utilObj = new util();
    $rsp = $utilObj->curl(CUSTOM_BATCHSENDHFD_URL, $_REQUEST['msg']);

    if (empty($rsp)) {
        $messageId = $utilObj->createUuid();
        $releMessageId = $utilObj->createUuid();
        echo '<MessageRoot><MessageHead><MessageId>' . $uuid . '</MessageId><MessageType>HFD</MessageType><SendTime>' . date("Y-m-d H:i:s") . '</SendTime></MessageHead><MessageBody><ReleMessageId>' . $releMessageId . '</ReleMessageId><RspResult>F</RspResult><RspMsg>调用超时</RspMsg></MessageBody></MessageRoot>';
    } else {
        echo $rsp;
		error_log(print_r(date("Y-m-d H:i:s").$_REQUEST['msg'],1),3,'/yd/oms/log/hfd.log');
		error_log(print_r(date("Y-m-d H:i:s").$rsp,1),3,'/yd/oms/log/hfd_rsp.log');
    }
} else {
    echo '无效参数';
}
exit;
