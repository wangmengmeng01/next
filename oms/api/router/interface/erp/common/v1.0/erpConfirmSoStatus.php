<?php
/**
 * 出库单状态回传
 * wms => oms => ERP
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/erp/common/erpRequest.php';
class erpConfirmSoStatus extends erpRequest
{
	/**
	 * 推送出库单状态回传信息给ERP
	 * @param $params
	 * @return array
	 */
	public function push($params)
    {  	
    	if (!empty($params)) {
    		//转发数据给erp
    		$response = $this->send(service::$_methodTo, $params);
    		//解析返回的数据
    		if (!empty($response)) {
    			//获取错误数据
    			if ($response['returnFlag'] != 1) {
    				$error_info_arr = $this->merge_error_data($response['resultInfo'], msg::$err_arr);
    			} else {
    				$error_info_arr = $this->merge_error_data('', msg::$err_arr);
    			}
    			//组合失败数据成xml格式
    			$xmlData = $this->msgObj->get_error_str($error_info_arr); 	
    			if ($response['returnFlag'] == 0) {
    				return $this->msgObj->output(0, $response['returnDesc'], '0001', $xmlData, $response['addon']);    				    				
    			} else { 
    				if ($response['returnFlag'] == 1 || $response['returnFlag'] == 2) { 
    					//获取操作成功的数据
    					if (!empty($response['resultInfo'])) {
    						foreach ($response['resultInfo'] as $k => $v)
    						{	
    							foreach ($params['data']['orderinfo'] as $key => $val)
    							{
    								if ($v['CustomerID'] == $val['CustomerID'] && $v['OrderType'] == $val['OrderType'] && $v['OrderNo'] == $val['OrderNo'] && $v['WarehouseID'] == $val['WarehouseID']) {
    									unset($params['data']['orderinfo'][$key]);
    								}
    							}
    						}
    					}

    					//写入数据库
    					if (!empty($params['data']['orderinfo'])) {
    						 global $db;
    						 $multiFlag = $this->utilObj->isArrayMulti($params);
    						 if ($multiFlag) {
    						 	$headers = $params['data']['orderinfo'];
    						 } else {
    						 	$headers = array($params['data']['orderinfo']);
    						 }		 	

    						 if (!empty($headers)) {
    							 //获取出库单状态回传表数据库字段对应接口参数关系
    							 $column_arr = $this->get_dataBase_relation('outbound_status_record');
    						     foreach ($headers as $key => $val)
    							 {	 
    							 	 //获取出单号对应的order_id
    							 	 $orderId = $this::get_order_info($val['OrderNo'], $val['OrderType'], $val['CustomerID'], $val['WarehouseID']);
    							 	 if($orderId){
    							 	 	//插入出库单状态回传记录
	    							 	$column_key_str = implode(',', array_values($column_arr)) . ',order_id,create_time';
	    							 	$column_value_str = ':' . implode(',:', array_values($column_arr)) . ',' . $orderId . ',now()';
	    							 	$sql = "INSERT INTO t_outbound_status_record({$column_key_str}) VALUES({$column_value_str})";
					    				$model = $db->prepare($sql);
		    							foreach ($column_arr as $k => $v)
		    							{
		    							 	$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];	    							 	  	
		    							}
		    							$model->execute($values);
    							 	 }
    							 	 //更新出库单表的状态信息
    							 	 $sql = 'UPDATE t_outbound_info SET order_status=:order_status WHERE order_id=:order_id';
    							 	 $model = $db->prepare($sql);
    							 	 $model->bindParam(':order_status', $val['Status']);
    							 	 $model->bindParam(':order_id', $orderId);
    							 	 $model->execute();
    							 }    							 
    						 }
    					}  
    				} 				
    			}
    			if ($response['returnFlag'] == 1) {
    				if (empty($error_info_arr)) {
    					return $this->msgObj->output(1, 'ok', '0000', $xmlData, $response['addon']);
    				} else {
    					return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon']);
    				}
    			} elseif ($response['returnFlag'] == 2) {
    				return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon']);
    			}
    		} else {
    			//获取错误数据
    			$error_info_arr = $this->merge_error_data($params['data']['orderinfo'], msg::$err_arr);
    			//组合失败数据成xml格式
    			$xmlData = $this->msgObj->get_error_str($error_info_arr);
    			return $this->msgObj->output(0, 'fail', 'S007', $xmlData, $response['addon']);
    		}  		    		
    	} else {
    		if (!empty(msg::$err_arr)){
    			$xml = new xml();
    			$xmlData = '';
    			foreach (msg::$err_arr as $key => $val)
    			{
    				$xmlData .= $xml->array2xml(array('result' => $val));
    			}
    		}
    		return $this->msgObj->output(0, 'fail', '0001', $xmlData);
    	}
    } 
    
    /**
     * 解析返回的错误数据，并和之前的错误数据进行组合
     * @param $error_info  erp接口返回的resultInfo错误信息
     * @param $error_arr   msg类中存储的错误信息
     * @return array
     */
    public function merge_error_data($error_info, $error_arr)
    {
    	$return_arr = array();
    	$i=0;
    	if (!empty($error_info)) {
    		foreach ($error_info as $v)
    		{
    			$return_arr[$i] = $v;
    			$i++;
    		}
    	}
    	if (!empty($error_arr)) {
    		foreach ($error_arr as $val)
    		{
    			$return_arr[$i] = $val;
    			$i++;
    		}
    	}
    	return $return_arr;
    }


    /**
     * 判断入库单订单号
     * @param $orderNo   
     * @param $oderType   	
     * @param $customerID
     * @param $warehousrID
     * @return $orderId
     */
    public function get_order_info($orderNo, $oderType, $customerID, $warehousrID)
    {
    	global $db;
    	$sql='SELECT order_id FROM t_outbound_info WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
    	$model = $db->prepare($sql);
		$model->bindParam(':order_no', $orderNo);
		$model->bindParam(':order_type', $oderType);
		$model->bindParam(':customer_id', $customerID);
		$model->bindParam(':warehouse_code', $warehousrID);
		$model->execute();
		$rs = $model->fetch(PDO::FETCH_ASSOC);
		$orderId = $rs['order_id'];
		return $orderId;
    }
}

