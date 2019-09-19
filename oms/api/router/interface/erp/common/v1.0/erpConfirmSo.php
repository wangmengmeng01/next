<?php
/**
 * 出库单明细回传操作类
 * wms => oms => ERP
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/erp/common/erpRequest.php';
class erpConfirmSo extends erpRequest
{
	/**
	 * 出库单明细回传信息推送给ERP
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
    					//出库单回传信息写入数据库
    					$this->insert_outbound_record($params);
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
    			$error_info_arr = $this->merge_error_data($params['header'], msg::$err_arr);
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
     * 把出库单回传信息写入数据库
     * @param array
     * @return boolean
     */
    public function insert_outbound_record($params)
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
    			//获取出库单单头基础信息接口参数与数据库字段对应关系
    			$column_orderInfo_arr = $this->get_dataBase_relation('outbound_info');
    			$column_key_orderInfo = implode(',', array_values($column_orderInfo_arr)) . ',order_status,create_time';
    			//获取出库单明细基础信息接口参数与数据库字段对应关系
    			$column_orderDetail_arr = $this->get_dataBase_relation('outbound_detail');
    			$column_key_orderDetail = implode(',', array_values($column_orderDetail_arr)) . ',order_id,create_time';
    			
    			//获取出库单明细回传单头信息接口参数与数据库字段对应关系
    			$recode_column_arr = $this->get_dataBase_relation('outbound_info_recode');
    			$recode_column_key = implode(',', array_values($recode_column_arr)) . ',order_id,create_time';
    			//获取出库单回传明细信息接口参数与数据库字段对应关系
    			$detail_column_arr = $this->get_dataBase_relation('outbound_detail_record');
    			$detail_column_key = implode(',', array_values($detail_column_arr)) . ',record_id,order_id,create_time';
    			
    			foreach ($headers as $val)
    			{
    				$values = array();
    				//判断出单单号是否存在，如果存在则更新出库单信息表，否则如果是由wms发起的则创建出库单信息
    				if ($val['UserDefine4'] != 'WMS') {    //更新由ERP和OMS发起的出单单信息
    					//获取出库单表order_id
    					$sql = "SELECT order_id from t_outbound_info where order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
    					$model = $db->prepare($sql);
    					$model->bindParam(':order_no', $val['OMSOrderNo']);
    					$model->bindParam(':order_type', $val['OrderType']);
    					$model->bindParam(':customer_id', $val['CustomerID']);
    					$model->bindParam(':warehouse_code', $val['WarehouseID']);
    					$model->execute();
    					$rs = $model->fetch(PDO::FETCH_ASSOC);
    					$orderId = $rs['order_id'];
    					if ($orderId != '') {   						 					
	    					//插入出库单明细回传单头信息表
	    					$recode_column_value = ":" . implode(",:", array_values($recode_column_arr)) . ",'$orderId',now()";
	    					$sql_recode = "INSERT INTO t_outbound_info_record({$recode_column_key}) VALUES({$recode_column_value})";
	    					$model = $db->prepare($sql_recode);
	    					$values = array();
	    					foreach ($recode_column_arr as $k => $v)
	    					{
	    						$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];
	    					}
	    					$model->execute($values);
	    					$recordId = $db->lastInsertId();
	    					
	    					//插入出库单明细回传表
	    					$detail_column_value = ':' . implode(',:', array_keys($detail_column_arr)) . ',' . $recordId . ',' . $orderId . ',now()';
	    					$detail_sql = "INSERT INTO t_outbound_detail_record({$detail_column_key}) VALUES({$detail_column_value})";
	    					if(!empty($val['item'])){
	    						$model = $db->prepare($detail_sql);
	    						foreach ($val['item'] as $d_v)
	    						{
	    							$values = array();
	    							foreach ($detail_column_arr as $k => $v)
	    							{
	    								$values[':'.$k] = empty($d_v[$k]) ? '' : $d_v[$k];
	    							}
	    							$model->execute($values);
	    						}
	    						//更新库存信息
	    						if (UPDATE_INVENTORY_FLAG) {
	    							$this->update_outbound_inventory($val['CustomerID'], $val['WarehouseID'], $val['item'], $val['UserDefine4']);
	    						}
	    					}
    					}
    				} else {   //由WMS发起的出库单信息
    					//判断该订单号是否存在，如果存在则更新回传信息，如果不存在则新增订单
    					$sql = "SELECT order_id from t_outbound_info where order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
    					$model = $db->prepare($sql);
    					$model->bindParam(':order_no', $val['WMSOrderNo']);
    					$model->bindParam(':order_type', $val['OrderType']);
    					$model->bindParam(':customer_id', $val['CustomerID']);
    					$model->bindParam(':warehouse_code', $val['WarehouseID']);
    					$model->execute();
    					$rs = $model->fetch(PDO::FETCH_ASSOC);
    					$orderId = $rs['order_id'];
    					if ($orderId == '') {
    						//写入出库单单头信息
    						$column_value_orderInfo = ":" . implode(",:", array_values($column_orderInfo_arr)) . ",'99',now()";
    						$sql_orderInfo = "INSERT INTO t_outbound_info({$column_key_orderInfo}) VALUES({$column_value_orderInfo})";
    						$model = $db->prepare($sql_orderInfo);
    						$values = array();
    						foreach ($column_orderInfo_arr as $k => $v)
    						{
    							if ($v == 'order_no') {
    								$values[':order_no'] = empty($val['WMSOrderNo']) ? '' : $val['WMSOrderNo'];
    							} else {
    								$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];
    							}
    						}
    						$model->execute($values);
    						$orderId = $db->lastInsertId();
    						//写入出库单明细信息
    						$column_value_orderDetail = ":" . implode(",:", array_keys($column_orderDetail_arr)) . ",'{$orderId}',now()";
    						$sql_orderDetail = "INSERT INTO t_outbound_detail({$column_key_orderDetail}) VALUES({$column_value_orderDetail})";
    						if (!empty($val['item'])) {
    							$model = $db->prepare($sql_orderDetail);
    							foreach ($val['item'] as  $b)
    							{
    								$values = array();
    								foreach ($column_orderDetail_arr as $k => $v)
    								{
    									$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
    								}
    								$values[':CustomerID'] = $val['CustomerID'];
    								$model->execute($values);
    							}
    						}
    					}    					    	    						
    					//写入出库单状态回传记录，默认为完成状态
    					$sql = "INSERT INTO t_outbound_status_record(order_id,order_no,order_type,customer_id,warehouse_code,order_status,order_desc,operator_time,create_time) VALUES('$orderId',:order_no,:order_type,:customer_id,:warehouse_code,'99','订单完成',:operator_time,now())";
    					$values = array(
    							':order_no' => $val['WMSOrderNo'],
    							':order_type' => $val['OrderType'],
    							':customer_id' => $val['CustomerID'],
    							':warehouse_code' => $val['WarehouseID'],
    							':operator_time' => $val['OrderTime']
    					);
    					$model = $db->prepare($sql);
    					$model->execute($values);
    					//写入出库单明细回传单头信息表
    					$recode_column_value = ":" . implode(",:", array_values($recode_column_arr)) . ",'$orderId',now()";
    					$sql_recode = "INSERT INTO t_outbound_info_record({$recode_column_key}) VALUES({$recode_column_value})";
    					$model = $db->prepare($sql_recode);
    					$values = array();
    					foreach ($recode_column_arr as $k => $v)
    					{
    						$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];
    					}
    					$model->execute($values);
    					//写入出库单明细回传记录表
    					$detail_column_value = ':' . implode(',:', array_keys($detail_column_arr)) . ',' . $orderId . ',now()';
    					$detail_sql = "INSERT INTO t_outbound_detail_record({$detail_column_key}) VALUES({$detail_column_value})";
    					if(!empty($val['item'])){
    						$model = $db->prepare($detail_sql);
    						foreach ($val['item'] as $d_v)
    						{
    							$values = array();
    							foreach ($detail_column_arr as $k => $v)
    							{
    								$values[':'.$k] = empty($d_v[$k]) ? '' : $d_v[$k];
    							}
    							$model->execute($values);
    						}
    					}
    					//更新库存信息
    					if (UPDATE_INVENTORY_FLAG) {
    						$this->update_outbound_inventory($val['CustomerID'], $val['WarehouseID'], $val['item'], $val['UserDefine4']);   	
    					}				
    				}
    			}
    		}
    	} 
    }
    
    /**
     * 更新出库库存
     * @param $customerId  客户ID
     * @param $warehouseId 仓库ID
     * @param $itemArr  出库明细信息
     * @param $orderSource  订单来源
     */
    public function update_outbound_inventory($customerId, $warehouseId, $itemArr, $orderSource)
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
    				if ($orderSource == 'WMS') {
    					$qty = $rs['qty'] - $v['QtyShipped'];
    					$occupy_qty = $rs['occupy_qty'];
    					$qty_total = $rs['qty_total'] - $v['QtyShipped'];
    				} else {
    					$qty = $rs['qty'];
    					$occupy_qty = $rs['occupy_qty'] - $v['QtyShipped'];
    					//$occupy_qty = $occupy_qty <0 ? 0 : $occupy_qty;
    					$qty_total = $rs['qty_total'] - $v['QtyShipped'];
    				}
    				 				
    				$sql = "UPDATE t_product_inventory set qty=:qty,occupy_qty=:occupy_qty,qty_total=:qty_total WHERE customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
    				$model = $db->prepare($sql);
    				$values = array(
    						  ':qty' => $qty,
    				      	  ':occupy_qty' => $occupy_qty,
    			              ':qty_total' => $qty_total,
    						  ':customer_id' => $customerId,
    						  ':warehouse_code' => $warehouseId,
    						  ':sku' => $v['SKU']   						
    				);
    				$model->execute($values);
    			} else {
    				return false;
    			}
    		}
    	}
    }
    
}

