<?php
/**
 * 奇门erp请求基类
 */
class erpRequest
{

    public static $userId = ''; //客户ID
    public static $erpApi = ''; //erp接口地址
    public static $erpApiSecret = ''; //erp接口密钥
    public  $msgObj  = null;
    public  $utilObj = null;

    public function __construct()
    {
        $this->msgObj  = new msg();
        $this->utilObj = new util();
    }

    /**
     * 生成签名
     * @param $userId 用户账号
     * @return string 签名
     */
    private static function makeSign(){
        $userId = self::$userId;
        $secret = self::$erpApiSecret;
        $sign = md5($userId.$secret);
        return $sign;
    }

    public function send(){
        //生成签名
        $sign = self::makeSign();

        $apiParams = array(
            'msgtype' => custom_service::$_msgtype,
            'userid'  => self::$userId,
            'msg'     => custom_service::$_msg,
            'sign'    => $sign,
        );
        //推送数据到保税仓
        /*$httpObj = new httpclient();
        $rs = $httpObj->post(self::$erpApi, $apiParams);*/
        $rs = $this->utilObj->post(self::$erpApi, $apiParams);

        //接口层记录日志
        $logExt = array(
            'api_url'    => self::$erpApi,
            'api_method' => custom_service::$_msgtype,
            'api_params' => $apiParams,
        );
        if($rs == ''){
            $msg = '请求超时';
            $success = 'false';
            $logExt['return_msg'] = $msg;
        }else{
            $xmlObj = new xml();
            $response = $xmlObj->xmlStr2array($rs);
            $response = $this->utilObj->filter_null($response);
            $success = $response['success'];

            //部分接口响应报文无reasons字段
            if (empty($response['reasons']) && $success == 'false') {
                $msg = '失败【S005】';
            } else {
                $msg = $response['reasons'];
            }
            $logExt['return_msg'] = $rs;
        }

        return $this->msgObj->outputCustom($success, $msg, $logExt);
    }

    public function customSend()
    {
        //生成签名
        $sign = self::makeSign();
        $apiParams = array(
            'msgtype' => custom_service::$_msgtype,
            'userid'  => self::$userId,
            'msg'     => custom_service::$_msg,
            'sign'    => $sign,
        );
        //推送数据到客户OMS
        $rs = $this->utilObj->post(BSC_URL, $apiParams);
        //接口层记录日志
        $logExt = array(
            'api_url'    => BSC_URL,
            'api_method' => custom_service::$_msgtype,
            'api_params' => $apiParams,
        );
        if($rs == ''){
            $msg = '请求超时';
            $success = 'false';
            $logExt['return_msg'] = $msg;
        }else{
            $xmlObj = new xml();
            $response = $xmlObj->xmlStr2array($rs);
            $response = $this->utilObj->filter_null($response);

            $success = $response['success'];

            //部分接口响应报文无reasons字段
            if (empty($response['reasons']) && $success == 'false') {
                $msg = '失败【S005】';
            } else {
                $msg = $response['reasons'];
            }
            $logExt['return_msg'] = $rs;
        }
        return $this->msgObj->outputCustom($success, $msg, $logExt);
    }
}