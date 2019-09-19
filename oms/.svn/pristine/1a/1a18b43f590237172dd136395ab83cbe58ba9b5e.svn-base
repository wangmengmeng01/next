<?php
/**
 * wms内部请求基类
 * User: wp
 */
class wmsRequestInner
{

    public static $customerId = '';//客户ID
    public static $warehouseId = '';//仓库ID
    public static $wmsBn = '';//wms接口编号
    public static $wmsApi = '';//wms接口地址
    public static $wmsApiSecret = '';//wms接口密钥
    public static $wmsApiVer = '';//wms接口密钥
    public $msgObj = null;
    public $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }

    /**
     *
     * @param $method
     * @param $params
     */
    public function send($method, $params)
    {
    	$xml = new xml();
    	$xmlData = $this->utilObj->arrayToXml($params, $xml);
    	$apiParams = array(
    			'method' => $method,
    			'customerid' => self::$customerId,
    			'warehouseid' => self::$warehouseId,
    			'messageid' => inner_service::$_messageId,
    			'apptoken' => inner_service::$_appToken,
    			'appkey' => inner_service::$_appKey,
    			'sign' => strtoupper(base64_encode(md5(self::$wmsApiSecret.$xmlData.self::$wmsApiSecret))),
    			'timestamp' => date("Y-m-d H:i:s"),
    			'data' => $xmlData
    	);

    	//推送数据到wms接口
    //	$rs = $this->utilObj->curl(self::$wmsApi, $apiParams, $timeout=10);
    	$httpObj = new httpclient();   	
     	$rs = $httpObj->post(self::$wmsApi, $apiParams);
    	if($rs == ''){
    		$msg = '请求超时';
    		$msgcode = 'E001';
    		$status = 0;
    	}else{
    		$rs = urldecode($rs);
    		$response = $xml->xmlStr2array($rs);
    		$response = $response['return'];
    		$response = $this->utilObj->filter_null($response);
    		$response['resultInfo'] = $this->utilObj->getResultInfo($response['resultInfo']);  
    	
    		if ($response['returnFlag'] == '1') {   //全部成功
    			$status = 1;
    		} elseif ($response['returnFlag'] == '2') {  //部分成功，部分失败
    			$status = 2;
    		} else {   //失败
    			$status = 0;
    		}
    		    		
    		$msg = $response['returnDesc'];
    		$msgcode = $response['returnCode'];
    		$data = $response['resultInfo'];
    		
    		//当推送wms客商档案信息时记录推送日志
    		if (preg_match("/^(putCustData)/", $method)) {
    			//记录推送日志   			
    			if ($status == 1) {
    				$this->write_send_log($params['xmldata']['header'], 1, $msgcode, $msg);
    			} elseif ($status == 2) {
    				//获取成功推送的数据
	    		    $successArr = $this->join_data($params['xmldata']['header'], $response['resultInfo']);
	    		    $this->write_send_log($successArr, 1, $msgcode, $msg);
	    		    $this->write_send_log($response['resultInfo'], 0, $msgcode, $msg);
    			} else {
    				$this->write_send_log($params['xmldata']['header'], 0, $msgcode, $msg);
    			}
    		}   			    		    		   		
    	}
    	
    	//接口层记录日志
        $logExt = array(
            'api_url' => self::$wmsApi,
            'api_method' => $method,
            'api_params' => $apiParams,
            'return_msg' => $rs == '' ? '请求超时' : $rs
        );
        return $this->msgObj->output($status, $msg, $msgcode, $data, $logExt);
    }
       
    /**
     * 获取推送成功的记录
     * @param  array
     * @return array
     */
    public function join_data($responseArr, $errorArr)
    {
    	$returnArr = array();
    	if (!empty($errorArr)) {
    		foreach ($errorArr as $v)
    		{
    			foreach ($responseArr as $a => $b)
    			{
    				if ($v['CustomerID'] == $b['CustomerID'] && $v['Customer_Type'] == $b['Customer_Type']) {
    					unset($responseArr[$a]);
    				}
    			}
    		}
    		$returnArr = $responseArr;
    	} else {
    		$returnArr = $responseArr;
    	}
    	return $returnArr;
    }
    
    /**
     * 记录推送日志
     * @param
     * @return
     */
    public function write_send_log($logArr, $status, $msgcode, $msg)
    {
    	global $db;
    	if (!empty($logArr)) {
    		foreach ($logArr as $v)
    		{
    			if (empty($v)) {
    				break;
    			} else {
    			//查找推送数据是否存在
    				$sql = 'SELECT * FROM t_customer_send_log WHERE customer_id=:customer_id and customer_type=:customer_type';
    				$model = $db->prepare ($sql);
    				$model->bindParam(':customer_id', $v['CustomerID']);
    				$model->bindParam(':customer_type', $v['Customer_Type']);
    				$model->execute();
    				$rs = $model->fetch(PDO::FETCH_ASSOC);
    				$errorCode = !empty($v['errorcode']) ? $v['errorcode'] : $msgcode;
    				$errorDescr = !empty($v['errordescr']) ? $v['errordescr'] : $msg;
    				if (empty($rs)) {
    				    $num = 1;
    					$sql = "INSERT INTO t_customer_send_log(customer_id,customer_type,send_wms_num,send_wms_status,wms_error_code,wms_error_msg,create_time) VALUES(:customer_id,:customer_type,:send_wms_num,:send_wms_status,:wms_error_code,:wms_error_msg,now())";
    					$model = $db->prepare($sql);
    					$model->bindParam(':customer_id', $v['CustomerID']);
    					$model->bindParam(':customer_type', $v['Customer_Type']);
    					$model->bindParam(':send_wms_num', $num);
    					$model->bindParam(':send_wms_status', $status);
    					$model->bindParam(':wms_error_code', $errorCode);
    					$model->bindParam(':wms_error_msg', $errorDescr);
    					$model->execute();
    				} else {
    					$sql = "UPDATE t_customer_send_log SET send_wms_num=send_wms_num+1,send_wms_status=:send_wms_status,wms_error_code=:wms_error_code,wms_error_msg=:wms_error_msg WHERE customer_id=:customer_id and customer_type=:customer_type";
    					$model = $db->prepare($sql);
    					$model->bindParam(':send_wms_status', $status);
    					$model->bindParam(':customer_id', $v['CustomerID']);
    					$model->bindParam(':customer_type', $v['Customer_Type']);
    					$model->bindParam(':wms_error_code', $errorCode);
    					$model->bindParam(':wms_error_msg', $errorDescr);
    					$model->execute();
    				}
    			}
    		}
    	}
    }
}