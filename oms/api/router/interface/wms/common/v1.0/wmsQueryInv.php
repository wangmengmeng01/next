<?php
/**
 * 库存查询操作类
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';
class wmsQueryInv extends wmsRequest
{
	/**
	 * 库存查询
	 * @param $params
	 * @return array
	 */
	public function create($params)
    {   	    
    	if (!empty($params)) {
    		//转发数据给WMS
    		$response = $this->send(service::$_methodTo, $params);
    		$xml = new xml();
    		//解析返回的数据
    		if (!empty($response)) {			
    			//获取错误数据
    			if ($response['returnFlag'] != 1) {
    				$error_info_arr = $this->merge_error_data($response['resultInfo'], msg::$err_arr);
    			} else {
    				$error_info_arr = $this->merge_error_data('', msg::$err_arr);
    			}   			

    			if ($response['returnFlag'] == 0) {
    				return $this->msgObj->output(0, $response['returnDesc'], '0001', '', $response['addon']);    				    				
    			} elseif ($response['returnFlag'] == 1) {
    				$params = $response['addon']['return_msg'];
    				$params = $xml->xmlStr2array($params);
    				//写入数据库
    				if (!empty($params['items'])) {
    					global $db;
    					$items = $params['items']['item'];
    					$item = array_pop($items);
    					if (!is_array($item) || (is_array($item) && empty($item))) {
    						$item = array($params['items']['item']);
    					} else {
    						$item = $params['items']['item'];
    					}
    					if (!empty($item)) {
    						//获取库存信息参数与数据库字段对应关系
    						$column_arr = $this->get_dataBase_relation('inventory');
    						foreach ($item as $val)
    						{
    							//判断库存数据是否存在
    							$hasInv = $this::has_inv($val['CustomerID'],$val['WarehouseID'],$val['SKU']);
    							if($hasInv){
    								$up_column_arr = array();
    								$search_column_arr = array();
    								$column_value_str = '';
    								$search_column = array('sku','customer_id','warehouse_code');
    								foreach(array_values($column_arr) as $c_val){
    									if(in_array($c_val,$search_column)){
    										$search_column_arr[] = $c_val.'=:'.$c_val;
    									}else{
    										$up_column_arr[] = $c_val.'=:'.$c_val;
    									}
    								}
    								$up_column_arr = implode(',', array_values($up_column_arr));
    								$search_column_str = ' WHERE '.implode(' AND ', array_values($search_column_arr));
    								$sql = "update t_product_inventory set {$up_column_arr} {$search_column_str}";
    								$model = $db->prepare($sql);
    							}else{
    								$column_key_str = implode(',', array_values($column_arr)) . ',create_time';
    								$column_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
    								$sql = "INSERT IGNORE INTO t_product_inventory({$column_key_str}) VALUES({$column_value_str})";
    								$model = $db->prepare($sql);
    							}
    							foreach ($column_arr as $k => $v)
    							{
    								$values[':'.$v] = empty($val[$k]) ? '' : $val[$k];
    							}
    							$model->execute($values);
    						}
    					}
    				}
    				if (empty($error_info_arr)) {
    					return $this->msgObj->output(1, 'ok', '0000', 0, $response['addon'], $response['addon']['return_msg']);
    				} else {
    					return $this->msgObj->output(2, '部分成功部分失败', '0001', 0, $response['addon'], $response['addon']['return_msg']);
    				}
    			}    			
    		} else {
    			return $this->msgObj->output(0, 'fail', 'S007', 0);
    		}  		    		
    	} else {
    		return $this->msgObj->output(0, 'fail', '0001', 0);
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
     * 判断库存是否存在
     * @param $warehouseCode  仓库编码
     * @param $customerCode   客户编码
     * @param $sku   		  SKU编码
     * @return bool
     */
    public function has_inv($customerId, $warehouseCode, $sku)
    {
    	global $db;
    	$values=array();
		$values[':sku'] = $sku;
		$values[':customer_id'] = $customerId;
		$values[':warehouse_code'] = $warehouseCode;
		$sql='SELECT COUNT(*) t FROM `t_product_inventory` WHERE sku = :sku AND customer_id=:customer_id AND warehouse_code =:warehouse_code';
		$datas = $db->prepare($sql);
		$datas->execute($values);
		$rs = $datas->fetch(PDO::FETCH_ASSOC);
		if($rs['t']>0){
			return true;
		}else{
			return false;
		}
    }
}

