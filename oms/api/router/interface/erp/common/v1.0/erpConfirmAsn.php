<?php
/**
 * 入库单状态明细回传操作类
 * wms => oms => ERP
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/erp/common/erpRequest.php';
class erpConfirmAsn extends erpRequest
{
	/**
	 * 入库单状态明细回传信息推送
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
    					//获取推送成功的数据
    					if (!empty($response['resultInfo'])) {
    						foreach ($response['resultInfo'] as $v)
    						{	
    							foreach ($params['data']['orderinfo'] as $key => $val)
    							{
    								if ($val['UserDefine4'] == 'WMS') {
    									$OrderNo = $val['WMSOrderNo'];
    								} else {
    									$OrderNo = $val['OMSOrderNo'];
    								}
    								if ($v['CustomerID'] == $val['CustomerID'] && $v['OrderType'] == $val['OrderType'] && $OrderNo == $v['OrderNo'] && $v['WarehouseID'] == $val['WarehouseID']) {
    									unset($params['data']['orderinfo'][$key]);
    								}
    							}
    						}
    					}
    					//写入数据库
    					$this->insert_inbound_record($params);
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
    			$error_info_arr = $this->merge_error_data($params['data'], msg::$err_arr);
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
     * 把入库单回传信息写入数据库
     * @param array
     * @return 
     */
    public function insert_inbound_record($params)
    {
    	if (!empty($params['data']['orderinfo'])) {
    		global $db;
    		$multiFlag = $this->utilObj->isArrayMulti($params);
    		if ($multiFlag) {
    			$headers = $params['data']['orderinfo'];
    		} else {
    			$headers = array($params['data']['orderinfo']);
    		}
    		if (!empty($headers)) {
    			//获取入库单表数据库字段对应关系
    			$column_inbound_arr = $this->get_dataBase_relation('inbound_info');
    			$column_key_inbound_str = implode(',', array_values($column_inbound_arr)) . ',create_time';
    			//获取入库单明细表与数据库字段对应关系
    			$column_detail_arr = $this->get_dataBase_relation('inbound_detail');
    			$column_key_detail_str = implode(',', array_values($column_detail_arr)) . ',order_id,create_time';
    			//获取入库单状态回传表数据库字段对应关系
    			$column_arr = $this->get_dataBase_relation('inbound_info_record');
    			$column_key_str = implode(',', array_values($column_arr)) . ',order_id,create_time';
    			//获取入库单明细回传表数据库字段对应关系
    			$detail_record_column_arr = $this->get_dataBase_relation('inbound_detail_record');
    			$detail_record_column_key_str = implode(',', array_values($detail_record_column_arr)) . ',record_id,order_id,create_time';
    			foreach ($headers as $val)
    			{
    				//判断入库单号是否存在，如果存在则更新入库单回传记录信息表，否则如果是由wms发起的则创建入库单信息
    				if ($val['UserDefine4'] != 'WMS') {    //入库单由ERP或WMS发起
    					//获取出库单表order_id
    					$sql = "SELECT order_id from t_inbound_info where order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
    					$model = $db->prepare($sql);
    					$model->bindParam(':order_no', $val['OMSOrderNo']);
    					$model->bindParam(':order_type', $val['OrderType']);
    					$model->bindParam(':customer_id', $val['CustomerID']);
    					$model->bindParam(':warehouse_code', $val['WarehouseID']);
    					$model->execute();
    					$rs = $model->fetch(PDO::FETCH_ASSOC);
    					$orderId = $rs['order_id'];
    						
    					if ($orderId != '') {
    						//插入入库单状态回传记录
    						$column_value_str = ':' . implode(',:', array_values($column_arr)) . ',' . $orderId . ',now()';
    						$sql = "INSERT INTO t_inbound_info_record({$column_key_str}) VALUES({$column_value_str})";
    						$model = $db->prepare($sql);
    						$values = array();
    						foreach ($column_arr as $k => $v)
    						{
    							$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];
    						}
    						$model->execute($values);
    						$recordId = $db->lastInsertId();
    						
    						//插入库单明细回传记录
    						if(!empty($val['item'])){
    							$detail_record_column_value_str = ':' . implode(',:', array_values($detail_record_column_arr)) . ',' . $recordId . ',' . $orderId . ',now()';
    							foreach ($val['item'] as $i_val){
    								$i_values = array();
    								foreach ($detail_record_column_arr as $i_k => $i_v)
    								{
    									$i_values[':'.$i_v] = empty($i_val[$i_k]) ? '' : $i_val[$i_k];
    								}
    								$sql = "INSERT INTO t_inbound_detail_record({$detail_record_column_key_str}) VALUE({$detail_record_column_value_str})";
    								$model = $db->prepare($sql);
    								$model->execute($i_values);
    							}
    						}
    						//更新入库单状态
    						$this->update_inbound_status($orderId, $val['Status']);
    						//更新库存信息
    						if (UPDATE_INVENTORY_FLAG) {
    							$this->update_inbound_inventory($val['CustomerID'], $val['WarehouseID'], $val['item']);
    						}
    					}
    				} else{   //插入由WMS发起的入库单信息
    					//判断入库单是否存在
    					$sql = "SELECT order_id from t_inbound_info where order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
    					$model = $db->prepare($sql);
    					$model->bindParam(':order_no', $val['WMSOrderNo']);
    					$model->bindParam(':order_type', $val['OrderType']);
    					$model->bindParam(':customer_id', $val['CustomerID']);
    					$model->bindParam(':warehouse_code', $val['WarehouseID']);
    					$model->execute();
    					$rs = $model->fetch(PDO::FETCH_ASSOC);
    					$order_id = $rs['order_id'];
    					if (empty($rs)) {
    						//写入入库单表
    						$column_value_inbound_str = ':' . implode(',:', array_values($column_inbound_arr)) . ',now()';
    						$sql = "INSERT INTO t_inbound_info({$column_key_inbound_str}) VALUE({$column_value_inbound_str})";
    						$model = $db->prepare($sql);
    						$values = array();
    						foreach ($column_inbound_arr as $k => $v)
    						{
    							if ($v == 'order_no') {
    								$values[':order_no'] = empty($val['WMSOrderNo']) ? '' : $val['WMSOrderNo'];
    							} else {
    								$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];
    							}
    						}
    						$model->execute($values);
    						$order_id = $db->lastInsertId();
    						
    						//写入入库单明细
    						$column_value_detail_str = ":" . implode(',:', array_values($column_detail_arr)) . ",'{$order_id}',now()";
    						$sql = "INSERT INTO t_inbound_detail({$column_key_detail_str}) VALUES({$column_value_detail_str})";
    						$model = $db->prepare($sql);
    						if (!empty($val['item'])) {
    							foreach ($val['item'] as $valItem) {
    								$values = array();
    								foreach ($column_detail_arr as $k => $v)
    								{
    									$values[':'.$v] = empty($valItem[$k]) ? '' : $valItem[$k];
    								}
    								$model->execute($values);
    							}
    						}
    					} else {
    						//更新入库单状态
    						$this->update_inbound_status($order_id, $val['Status']);
    					}    					    					     						
    					//写入入库单状态明细回传单头信息
    					$column_value_str = ':' . implode(',:', array_values($column_arr)) . ',' . $order_id . ',now()';
    					$sql = "INSERT INTO t_inbound_info_record({$column_key_str}) VALUES({$column_value_str})";
    					$model = $db->prepare($sql);
    					$values = array();
    					foreach ($column_arr as $k => $v)
    					{
    						$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];
    					}
    					$model->execute($values);
    						
    					//写入入库单明细回传明细信息
    					if(!empty($val['item'])){
    						$detail_record_column_value_str = ':' . implode(',:', array_values($detail_record_column_arr)) . ',' . $order_id . ',now()';
    						foreach ($val['item'] as $i_val){
    							$i_values = array();
    							foreach ($detail_record_column_arr as $i_k => $i_v)
    							{
    								$i_values[':'.$i_v] = empty($i_val[$i_k]) ? '' : $i_val[$i_k];
    							}
    							$sql = "INSERT INTO t_inbound_detail_record({$detail_record_column_key_str}) VALUE({$detail_record_column_value_str})";
    							$model = $db->prepare($sql);
    							$model->execute($i_values);
    						}
    					}
    					//更新库存信息
    					if (UPDATE_INVENTORY_FLAG) {
    						$this->update_inbound_inventory($val['CustomerID'], $val['WarehouseID'], $val['item']);  
    					}  					
    				}
    			}
    		} 
    	}
    }
    
    /**
     * 更新入库库存
     * @param customerId  客户ID
     * @param warehouseId 仓库ID
     * @param itemArr  入库明细信息
     * @return 
     */
    public function update_inbound_inventory($customerId, $warehouseId, $itemArr)
    {
    	if (!empty($itemArr)) {
    		global $db;
    		foreach ($itemArr as $v)
    		{
    			//获取SKU的库存信息
    			$sql = "SELECT sku,qty,occupy_qty,qty_total from t_product_inventory where customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
    			$model = $db->prepare($sql);
    			$model->bindParam(':customer_id', $customerId);
    			$model->bindParam(':warehouse_code', $warehouseId);
    			$model->bindParam(':sku', $v['SKU']);
    			$model->execute();
    			$rs = $model->fetch(PDO::FETCH_ASSOC);
    			if (!empty($rs)) {
    				$qty = $rs['qty'] + $v['ReceivedQty'];
    				$qty_total = $rs['qty_total'] + $v['ReceivedQty'];
    				$sql = "UPDATE t_product_inventory set qty=:qty,qty_total=:qty_total WHERE customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
    				$model = $db->prepare($sql);
    				$values = array(
    						':qty' => $qty,
    						':qty_total' => $qty_total,
    						':customer_id' => $customerId,
    						':warehouse_code' => $warehouseId,
    						':sku' => $v['SKU']
    				);
    				$model->execute($values);
    			} else {   //写入库存
    		    	$sql = "INSERT INTO t_product_inventory(customer_id,warehouse_code,sku,qty,occupy_qty,qty_total,create_time) VALUES(:customer_id,:warehouse_code,:sku,:qty,0,:qty_total,now())";
    			    $model = $db->prepare($sql);
    			    $model->bindParam(':customer_id', $customerId);
    				$model->bindParam(':warehouse_code', $warehouseId);
    				$model->bindParam(':sku', $v['SKU']);
    				$model->bindParam(':qty', $v['ReceivedQty']);
    				$model->bindParam(':qty_total', $v['ReceivedQty']);
    				$model->execute();
    			}
    		}
    	} 
    }
    
    /**
     * 更新入库单状态
     * @param $orderId
     * @param $status
     * @return
     */
    public function update_inbound_status($orderId, $status)
    {
    	global $db;
    	$sql = "UPDATE t_inbound_info SET order_status=:order_status WHERE order_id=:order_id";
    	$model = $db->prepare($sql);
    	$model->bindParam(':order_status', $status);
    	$model->bindParam(':order_id', $orderId);
    	$model->execute();
    }
}

