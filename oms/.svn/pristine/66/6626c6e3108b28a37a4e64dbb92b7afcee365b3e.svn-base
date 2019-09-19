<?php
/**
 * Notes:贝贝wms请求基类
 * Date: 2019/6/13
 * Time: 10:39
 */

class wmsRequest
{

    public static $customerId = ''; //客户ID
    public static $wmsApi = ''; //wms接口地址
    public static $wmsApiSecret = ''; //wms接口密钥
    public static $wmsApiVer = ''; //wms接口地址
    public $msgObj = null;
    public $utilObj = null;
    public $funcObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
        $this->funcObj = new func();
    }

    /**
     * 转发数据给WMS
     */
    public function send($params = null)
    {
        $apiParams = array(
            'method' => beibei_service::$_method,
            'format' => beibei_service::$_format,
            'data' => beibei_service::$_data
        );
        //推送数据到wms接口
        beibei_service::$_mid_req_time = util::microtime_float();
        $rs = $this->utilObj->curlData(self::$wmsApi, http_build_query($apiParams), 60);
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
            'api_url' => self::$wmsApi,
            'api_method' => beibei_service::$_method,
            'api_params' => $apiParams,
            'return_msg' => $rs == '' ? '请求超时' : $rs
        );
        return $this->msgObj->outputBeibei($success, $data, $message, $logExt);
    }
}