<?php
/**
 * Notes:贝贝erp请求基类
 * Date: 2019/6/13
 * Time: 10:34
 */
class erpRequest
{
    public static $erpApi = ''; //erp接口地址
    public static $erpApiSecret = ''; //erp接口密钥
    public static $erpApiVer = ''; //erp版本号
    public  $msgObj = null;
    public  $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }

    /***
     * Notes: 生成签名
     * Date: 2019/6/18
     * Time: 17:06
     * @param $params 请求参数
     * @return string
     */
    public function makeSign($params)
    {
        ksort($params);
        $signStr = self::$erpApiSecret;
        foreach ($params as $k => $v)
        {
            $signStr .= $k . $v;
        }
        $signStr .= self::$erpApiSecret;
        $sign = strtoupper(md5($signStr));
        return $sign;
    }
    /**
     * 转发数据给ERP
     */
    public function send()
    {
        //生成签名
        $apiParams = array(
            'method' => beibei_service::$_method,
            'format' => 'json',
            'timestamp' => date('Y-m-d H:i:s'),
            'appId' => beibei_service::$_appId,
            'session' => beibei_service::$_session,
            'version' => self::$erpApiVer,
            'data' => beibei_service::$_data,
        );
        $apiParams['sign'] = self::makeSign($apiParams);
        //推送数据到ERP接口
        beibei_service::$_mid_req_time = util::microtime_float();
        $rs = $this->utilObj->curlData(self::$erpApi, json_encode($apiParams), 60,array("Content-type: application/json"));
        beibei_service::$_mid_resp_time = util::microtime_float();
        if ($rs == '') {
            $success = false;
            $data = '';
            $message = '请求超时';
        } else {
            $response = json_decode($rs, true);
            if (isset($response['success'])) {
                $success = $response['success'];
                $data = $response['data'];
                $message = $response['message'];
            } else {
                //curl请求未正常返回
                $success = false;
                $data = '';
                $message = $rs;
            }
        }
        //接口层记录日志
        $logExt = array(
            'api_url' => self::$erpApi,
            'api_method' => beibei_service::$_method,
            'api_params' => $apiParams,
            'return_msg' => $rs == '' ? '请求超时' : $rs
        );
        return $this->msgObj->outputBeibei($success, $data, $message, $logExt);
    }
}