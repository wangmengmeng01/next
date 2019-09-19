<?php
/**
 * erp内部请求基类
 * User: wp
 */
class erpRequestInner
{

    public static $customerId = '';//客户ID
    public static $warehouseId = '';//仓库ID
    public static $erpBn = '';//erp接口编号
    public static $erpApi = '';//erp接口地址
    public static $erpApiSecret = '';//erp接口密钥
    public static $erpApiVer = '';//erp版本号
    public  $msgObj = null;
    public  $utilObj = null;

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
    			'sign' => strtoupper(base64_encode(md5(self::$erpApiSecret.$xmlData.self::$erpApiSecret))),
    			'timestamp' => date("Y-m-d H:i:s"),
    			'data' => $xmlData
    	);

    	$rs = $this->utilObj->curl(self::$erpApi, $apiParams, 5);
    	if($rs == ''){
    		$msg = '请求超时';
    		$msgcode = 'E001';
    		$status = 0;
    	}else{
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
    		
    		//当推送ERP客商档案信息时记录推送日志
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
        		'api_url' => self::$erpApi,
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
    				if (empty($rs)) {
    				    $msgcode = $v['errorcode'] != '' ? $v['errorcode'] : $msgcode;
    				    $msg = $v['errordescr'] !='' ? $v['errordescr'] : $msg;
    					$sql = "INSERT INTO t_customer_send_log(customer_id,customer_type,send_erp_num,send_erp_status,erp_error_code,erp_error_msg,create_time) VALUES(:customer_id,:customer_type,:send_erp_num,:send_erp_status,:erp_error_code,:erp_error_msg,now())";
    					$model = $db->prepare ($sql);
    					$model->bindParam(':customer_id', $v['CustomerID']);
    					$model->bindParam(':customer_type', $v['Customer_Type']);
    					$model->bindParam(':send_erp_num', 1);
    					$model->bindParam(':send_erp_status', $status);
    					$model->bindParam(':erp_error_code', $msgcode);
    					$model->bindParam(':erp_error_msg', $msg);
    					$model->execute();
    				} else {
    				    $msgcode = $v['errorcode'] != '' ? $v['errorcode'] : $msgcode;
    				    $msg = $v['errordescr'] !='' ? $v['errordescr'] : $msg;
    					$sql = "UPDATE t_customer_send_log SET send_erp_num=send_erp_num+1,send_erp_status=:send_erp_status,erp_error_code=:erp_error_code,erp_error_msg=:erp_error_msg WHERE customer_id=:customer_id and customer_type=:customer_type";
    					$model = $db->prepare ($sql);
    					$model->bindParam(':send_erp_status', $status);
    					$model->bindParam(':customer_id', $v['CustomerID']);
    					$model->bindParam(':customer_type', $v['Customer_Type']);
    					$model->bindParam(':erp_error_code', $msgcode);
    					$model->bindParam(':erp_error_msg', $msg);
    					$model->execute();
    				}
    			}
    		}
    	}   	
    }
}