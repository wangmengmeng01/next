<?php
/**
 * 库存盘点通知操作类
 * wms => oms => erp
 * @author Renee
 *
 */
require_once API_ROOT . '/router/interface/erp/common/erpRequest.php';
class erpInventoryReport extends erpRequest
{
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
    								if ($v['CustomerID'] == $val['CustomerID'] && $v['checkOrderCode'] == $val['checkOrderCode'] && $v['WarehouseID'] == $val['WarehouseID']) {
    									unset($params['data']['orderinfo'][$key]);
    								}
    							}
    						}
    					}
    					//写入数据库
    					$this->insert_inventory_info($params);
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
    			foreach (msg::$err_arr as $val)
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
     * 将库存盘点的信息插入数据库
     * @param array
     * @return 
     */
    public function insert_inventory_info($params)
    {
    	if (!empty($params['data']['orderinfo'])) {
    		global $db;
    		$orderInfo = $params['data']['orderinfo'][0];
    		if (!empty($orderInfo)) {
    		    //获取库存盘点回传表数据库字段对应关系
    		    $column_inv_arr = $this->get_dataBase_relation('inventory_report');
    		    $column_key_inv_str = implode(',', array_values($column_inv_arr)) . ',create_time';
                //获取库存盘点回传明细表数据库字段对应关系    		    
    		    $column_detail_arr = $this->get_dataBase_relation('inventory_item_report');
    		    $column_key_detail_str = implode(',', array_values($column_detail_arr)) . ',inventory_id,create_time';
    		    
    		    $column_value_inv_str = ':' . implode(',:', array_values($column_inv_arr)) . ',now()';
		        $sql = "INSERT INTO t_inventory_check_record({$column_key_inv_str}) VALUES({$column_value_inv_str})";
		        $model = $db->prepare($sql);
		        $values = array();
		        foreach ($column_inv_arr as $k => $v) {
		            $values[':' . $v] = empty($orderInfo[$k]) ? '' : $orderInfo[$k] ;
		        }
		        $model->execute($values);
    		    
		        $column_value_detail_str = ':' . implode(',:', array_values($column_detail_arr)) . ",'{$db->lastInsertId()}',now()";
    		    if (empty($orderInfo['items']['item'][0])) {
    		        $orderInfo['items']['item'] = array($orderInfo['items']['item']);
    		    }
    		    foreach ($orderInfo['items']['item'] as $val) {
    		        $sql = "INSERT INTO t_inventory_check_product_record({$column_key_detail_str}) VALUES({$column_value_detail_str})";
    		        $model = $db->prepare($sql);
    		        $values = array();
    		        foreach ($column_detail_arr as $a => $b) {
    		            if ($a == 'inventoryType') {
    		                $values[':' . $b] = empty($val[$a]) ? 'ZP' : $val[$a] ;
    		            } else {
    		                $values[':' . $b] = empty($val[$a]) ? '' : $val[$a] ;
    		            }
    		        }
    		        $model->execute($values);
    		    }
    		} 
    	}
    }
}

