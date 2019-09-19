<?php
/**
 * oms供应商和店铺信息业务处理类
 * @author wp
 *
 */
require_once API_ROOT . '/router/interface/oms/common/omsRequest.php';
class omsSupplierAndShop extends omsRequest
{
	/**
	 * 创建供应商和店铺
	 * @param $params
	 * @return array
	 */
	public function create($params)
    {     	
    	if (!empty($params)) {    		
    		//写入数据库
    		if (!empty($params['header'])) {
    			global $db;
    		    $multiFlag = $this->utilObj->isArrayMulti($params);
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
    		
    				foreach ($headers as $val)
    				{
    					$values = array();
    					if ($val['Customer_Type'] == 'VE') {
    						$model = $db->prepare($sql_supplier);
    						foreach ($column_supplier_arr as $k => $v)
    						{
    							$values[':'.$k] = empty($val[$k]) ? '' : $val[$k];
    						}
    						$model->execute($values);
    					} elseif ($val['Customer_Type'] == 'SH') {
    						$model = $db->prepare($sql_shop);
    						foreach ($column_shop_arr as $k => $v)
    						{
    							$values[':'.$k] = empty($val[$k]) ? '' : $val[$k];
    						}
    						$model->execute($values);
    					}
    				}
    			}
    		}	
    		//组合失败数据成xml格式
    		$xmlData = $this->msgObj->get_error_str(msg::$err_arr);
    		if ($xmlData !='') {
    			return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData);
    		} else {
    			return $this->msgObj->output(1, 'ok', '0000', $xmlData);
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
    
}

