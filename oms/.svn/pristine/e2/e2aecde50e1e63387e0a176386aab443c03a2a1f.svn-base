<?php
/**
 * 奇门erp请求基类
 */
class omsRequest
{
    public static $customerId = ''; //客户ID
    public static $erpApi = '';     //erp接口地址

    public  $msgObj = null;
    public  $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }
    
    /**
     * 生成签名
     */
    public function makeSign()
    {
    	$signStr = qimen_service::$_appSecret;

    	foreach (qimen_service::$_systemParams as $val)
    	{
    	    if ($val != 'v') {
    	        if ($val == 'customerId') {
    	            $signStr .= $val . qimen_service::$_customerId;
    	        } elseif ($val == 'method') {
    	            $signStr .= $val . qimen_service::$_method;
    	        } else {
                    $signStr .= $val . $_REQUEST[$val];
                }
    	    }
    	}
        $signStr .= 'v' . qimen_service::$_v . qimen_service::$_data . qimen_service::$_appSecret;
        $sign = strtoupper(md5($signStr));
        return $sign;
    }
    
    /**
     * 转发数据给ERP
     */
    public function send()
    {   	
    	//生成签名    	
    	$sign = self::makeSign();

    	self::$erpApi = qimen_service::$_toApiUrl;
    	if (strstr(self::$erpApi,'?')) {
    	    $apiUrl = self::$erpApi . '&method=' . qimen_service::$_method . '&timestamp=' . urlencode(qimen_service::$_timeStamp) . '&format=' . qimen_service::$_format . '&app_key=' . qimen_service::$_appKey . '&v=' . qimen_service::$_v . '&sign=' . $sign . '&sign_method=' . qimen_service::$_signMethod . '&customerId=' . qimen_service::$_customerId;
    	} else {
    	    $apiUrl = self::$erpApi . '?method=' . qimen_service::$_method . '&timestamp=' . urlencode(qimen_service::$_timeStamp) . '&format=' . qimen_service::$_format . '&app_key=' . qimen_service::$_appKey . '&v=' . qimen_service::$_v . '&sign=' . $sign . '&sign_method=' . qimen_service::$_signMethod . '&customerId=' . qimen_service::$_customerId;
    	}

    	//推送数据到ERP接口
        qimen_service::$_mid_req_time = util::microtime_float();
        $rs = $this->utilObj->curl($apiUrl, qimen_service::$_data);
    	qimen_service::$_mid_resp_time = util::microtime_float();
    	
    	if($rs == ''){
    		$msg = '请求超时';
    		$msgcode = 'E001';
    		$status = 'failure';
    	}else{
    		$xmlObj = new xml();
    		$response = $xmlObj->xmlStr2array($rs);
    		$response = $this->utilObj->filter_null($response);
    		$status = $response['flag'];
    		$msg = $response['message'];
    		$msgcode = $response['code'];
    	}
    	
    	//接口层记录日志
    	$apiParams = array(
    			'method' => qimen_service::$_method,
    			'timestamp' => qimen_service::$_timeStamp,
    			'format' => qimen_service::$_format,
    	        'app_key' => qimen_service::$_appKey,
    			'app_secret' => qimen_service::$_appSecret,
    	        'v' => qimen_service::$_v,
    	        'sign' => $sign,
    	        'sign_method' => qimen_service::$_signMethod,
    			'customerid' => self::$customerId,
    			'data' => qimen_service::$_data
    	);
        $logExt = array(
            'api_url' => self::$erpApi,
            'api_method' => qimen_service::$_method,
            'api_params' => $apiParams,
            'return_msg' => $rs == '' ? '请求超时' : $rs
        );
        return $this->msgObj->outputQimen($status, $msg, $msgcode, $logExt);
    }

    /**
     * 接口参数与数据库字段对应关系
     * @param  $type 类型
     * @return array
     */
    public function get_dataBase_relation($type)
    {
    	$return_arr = array();

    	return $return_arr;
    }
}