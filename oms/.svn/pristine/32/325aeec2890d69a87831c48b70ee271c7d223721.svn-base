<?php
/**
 * 供应商和店铺信息业务处理类
 * 方向：erp->oms->wms
 * @author wp
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';
class wmsSupplierAndShop extends wmsRequest
{
	/**
	 * 创建供应商和店铺
	 * @param $params
	 * @return array
	 */
	public function create($params)
    {      	
    	if (!empty($params)) {
    		//转发数据给wms
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
    							foreach ($params['header'] as $key => $val)
    							{
    								if ($v['CustomerID'] == $val['CustomerID'] && $v['Customer_Type'] == $val['Customer_Type']) {
    									unset($params['header'][$key]);
    								}
    							}
    						}
    					}

    					//写入数据库
    					if (!empty($params['header'])) {
    						 global $db;
    					     $utilObj = new util();
                             $multiFlag = $utilObj->isArrayMulti($params);
                             if ($multiFlag) {
                            	$headers = $params['header'];
                             } else {
                            	$headers = array($params['header']);
                             }   		
    						 if (!empty($headers)) {
    						 	 //获取供应商基础信息接口参数与数据库字段对应关系
    						 	 $column_supplier_arr = $this->get_dataBase_relation('supplier');
    						 	 $column_key_supplier = implode(',', array_values($column_supplier_arr)) . ',create_time';    						 	 
    						 	 $column_value_supplier = ':' . implode(',:', array_keys($column_supplier_arr)) . ',now()';
    						 	 $sql_supplier = "INSERT IGNORE INTO t_base_supplier({$column_key_supplier}) VALUES({$column_value_supplier})";
    						 	 
    						 	 //获取店铺基础信息接口参数与数据库字段对应关系
    						 	 $column_shop_arr = $this->get_dataBase_relation('shop');
    						 	 $column_key_shop = implode(',', array_values($column_shop_arr)) . ',create_time';
    						 	 $column_value_shop = ':' . implode(',:', array_keys($column_shop_arr)) . ',now()';
    						 	 $sql_shop = "INSERT IGNORE INTO t_base_shop({$column_key_shop}) VALUES({$column_value_shop})";

    						     foreach ($headers as $key => $val)
    							 {
    							 	  $values = array();
    							 	  if ($val['Customer_Type'] == 'VE') {
    							 	  	  //校验供应商是否存在
    							 	  	  $sql = "SELECT supplier_id FROM t_base_supplier WHERE supplier_code=:supplier_code AND is_valid=1";
    							 	  	  $model = $db->prepare($sql);
    							 	  	  $model->bindParam(':supplier_code', $val['CustomerID']);
    							 	  	  $model->execute();
    							 	  	  $rs = $model->fetch(PDO::FETCH_ASSOC);
    							 	  	  if(!empty($rs)) {
    							 	  	  	  //把原有的供应商有效性置为无效
    							 	  	  	  $sql = "UPDATE t_base_supplier SET is_valid=0 WHERE supplier_id='{$rs['supplier_id']}' AND is_valid=1";
    							 	  	  	  $db->exec($sql);
    							 	  	  }
    							 	  	  //写入新的供应商信息					
    							 	  	  $model = $db->prepare($sql_supplier);	    							 	  
	    							 	  foreach ($column_supplier_arr as $k => $v)
	    							 	  {
	    							 	  	  $values[':'.$k] = empty($val[$k]) ? '' : $val[$k];
	    							 	  }
	    							 	  $model->execute($values);
    							 	  } elseif ($val['Customer_Type'] == 'OT') {
    							 	  	  //校验供应商是否存在
    							 	  	  $sql = "SELECT shop_id FROM t_base_shop WHERE shop_code=:shop_code AND is_valid=1";
    							 	  	  $model = $db->prepare($sql);
    							 	  	  $model->bindParam(':shop_code', $val['CustomerID']);
    							 	  	  $model->execute();
    							 	  	  $rs = $model->fetch(PDO::FETCH_ASSOC);
    							 	  	  if(!empty($rs)) {
    							 	  		  //把原有的供应商有效性置为无效
    							 	  		  $sql = "UPDATE t_base_shop SET is_valid=0 WHERE shop_id='{$rs['shop_id']}' AND is_valid=1";
    							 	  		  $db->exec($sql);
    							 	  	  }
    							 	  	  //写入新的店铺信息
    							 	  	  $model = $db->prepare($sql_shop);
	    							 	  foreach ($column_shop_arr as $k => $v)
	    							 	  {
	    							 	  	  $values[':'.$k] = empty($val[$k]) ? '' : $val[$k];
	    							 	  }
	    							 	  $model->execute($values);
    							 	  } 
    							 	  //写入数据到客商档案绑定关系表  	
    							 	  $sql = "INSERT IGNORE INTO t_customer_relation(customer_id,code,type,is_valid,create_time) VALUES(:customer_id,:code,:type,1,now())";
    							 	  $values = array(
    							 	  		':customer_id' => service::$_customerId,
    							 	  		':code' => $val['CustomerID'],
    							 	  		':type' => $val['Customer_Type']
    							 	  );
	    							  $model = $db->prepare($sql);
    							 	  $model->execute($values);
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
     * @param $error_info  wms接口返回的resultInfo错误信息
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
    
   
}

