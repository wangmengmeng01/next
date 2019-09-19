<?php
/**
 * Created by PhpStorm.
 * User: 20171012
 * Date: 2019/1/30
 * Time: 14:06
 */
header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
error_reporting(0);
date_default_timezone_set('Asia/Shanghai');
$SCRIPT_FILENAME = $_SERVER['SCRIPT_FILENAME'];
$arr = explode('/api', $SCRIPT_FILENAME);
require_once $arr[0] . '/config.php';

$_sign = $_REQUEST['sign'];
$_data = $_REQUEST['data'];

try {
    if (empty($_sign) || empty($_data)) {
        send_user_error($_data,'请求参数不完整!');
    }
    if ($_sign != strtoupper(md5($_data))) {
        send_user_error($_data,'签名效验失败!');
    }
    $param = json_decode($_data, true);
    $order_id = @$param['order_id'];
    $store_id = @$param['store_id'];
    $kaola_express_id = @$param['kaola_express_id'];
    $express_no = @$param['express_no'];
    if (empty($order_id)) {
        send_user_error($_data,'订单ID不能为空!');
    }
    if (empty($store_id)) {
        send_user_error($_data,'店铺海关备案ID不能为空!');
    }
    if (empty($kaola_express_id)) {
        send_user_error($_data,'快递公司ID不能为空!');
    }
    if (empty($express_no)) {
        send_user_error($_data,'快递单号不能为空!');

    }
    $store_info = OmsDatabase::$oms_db->fetchOne('access_token,kaola_key,kaola_secert', 't_kaola_store_info', 'store_id=:store_id', array(':store_id' => $store_id));
    if (!$store_info) {
        send_user_error($_data,'授权码信息不存在!');
    }

    $app_fields = array(
        'order_id' => $order_id,
        'kaola_express_id' => $kaola_express_id,
        'express_no' => $express_no
    );
    $param = array(
        'method' => 'kaola.order.customsclearance.deliver',
        'access_token' => $store_info['access_token'],
        'app_key' => $store_info['kaola_key'],
        'timestamp' => date('Y-m-d H:i:s'),
        'v' => '2.0',
        'biz_content' =>json_encode($app_fields),
    );
    ksort($param);

    $url_par = mk_url_par($store_info['kaola_secert'], $param);
    $url = KAOLA_URL . '?' . $url_par;
    //发送数据到考拉
    $rs = curl($url);

    if($rs == '' ){
        $json_return = '{"success":false,"reasons":"请求超时"}';
    }else{
        $response = json_decode($rs,true);
        $success = $response['kaola_order_customsclearance_deliver_response']['success'];
        $reasons = $response['kaola_order_customsclearance_deliver_response']['err_msg'];
        if ($success) {
            $success = 'true';
        } else {
            $success = 'false';
        }
        $json_return = '{"success":'.$success.',"reasons":"'.$reasons.'"}';
    }

    //添加日志
    add_log($_data,$json_return);
    echo $json_return;
} catch (\Exception $e) {
    $errMsg = $e->getMessage();
    add_log($_data,$errMsg);
    echo  $json_return = '{"success":false,"reasons":"'.$errMsg.'"}';
}
exit;

/***
 * Notes:生成签名并拼接url参数
 * Date: 2019/1/31
 * Time: 12:02
 * @param $appSecret
 * @param $param
 * @return string
 */
function mk_url_par($appSecret, $param)
{
    $str = '';
    $url_par = '';
    foreach ($param as $k => $v) {
        $str .= $k . $v;
        $url_par .= $k . '=' . urlencode($v) . '&';
    }
    $sign = strtoupper(md5($appSecret . $str . $appSecret));
    $url_par .= 'sign=' . $sign;
    return $url_par;
}

function curl($url, $data = null,$headers = array( "Content-Type:application/x-www-form-urlencoded"))
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_SSLVERSION, 1); //设定SSL版本
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_FAILONERROR, false);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT,5);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
    $reponse = curl_exec($ch);
    if ($error = curl_error($ch)){
        $errorMessage='调用接口错误，接口URL-'.$url.' ,错误信息-'.$error;
        throw new Exception($errorMessage, curl_errno($ch));
    }else{
        $httpStatusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        if (200 !== $httpStatusCode){
            return $reponse;
        }
    }
    curl_close($ch);
    return $reponse;
}

/***
 * Notes:添加日志
 * Date: 2019/1/31
 * Time: 12:00
 * @param $data 传入数据
 * @param $result
 */
function add_log($requ_param, $result)
{
    //生成日志id
    $microtime = mk_microtime();
    $unique_key = str_replace('.', '', strval($microtime));
    $randval = uniqid('', true);
    $unique_key .= strval($randval);
    $log_id = md5($unique_key);
    $insert_arr = array(
        'api_log_id' => $log_id,
        'api_func' => 'kaolaOrderDeliver',
        'api_url' => $_SERVER['SERVER_NAME'] . $_SERVER['REQUEST_URI'],
        'required_parameter' => $requ_param,
        'return_msg' => $result,
        'ip' => $_SERVER['REMOTE_ADDR'],
        'create_time' => date('Y-m-d H:i:s'),
    );
    OmsDatabase::$oms_db->insert('t_api_log', $insert_arr);
}

/***
 * Notes:接口前置错误输出
 * Date: 2019/1/31
 * Time: 11:59
 * @param $requ_param  传入数据
 * @param $message  错误原因
 */
function send_user_error($requ_param,$message)
{
    $res = '{"success":false,"reasons":"'.$message.'"}';
    add_log($requ_param,$res);
    echo $res;
    exit();
}

function mk_microtime()
{
    list($usec, $sec) = explode(" ", microtime());
    return ((float)$usec + (float)$sec);
}