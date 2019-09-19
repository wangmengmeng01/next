<?php
/**
 * erp仓库类
 * @author wp
 *
 */
require_once API_ROOT . '/router/interface/erp/common/erpRequest.php';
class erpWarehouse extends erpRequest
{
	/**
	 * 创建仓库ID
	 * @param $params
	 * @return array
	 */
	public function create($params)
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
    					//获取操作成功的仓库ID数据
    					if (!empty($response['resultInfo'])) {
    						foreach ($response['resultInfo'] as $k => $v)
    						{
    							foreach ($params['header'] as $key => $val)
    							{
    								if ($v['CustomerID'] == $val['CustomerID']) {
    									unset($params['header'][$key]);
    								}
    							}
    						}
    					}
    					
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
    						 	 //获取仓库基础信息接口参数与数据库字段对应关系
    						 	 $column_arr = $this->get_dataBase_relation('wareHouse');
    						 	 $column_key_str = implode(',', array_values($column_arr)) . ',create_time';    						 	 
    						 	 $column_value_str = ':' . implode(',:', array_keys($column_arr)) . ',now()';
    						     $sql = "INSERT IGNORE INTO t_base_warehouse({$column_key_str}) VALUES({$column_value_str})";
    						     $model = $db->prepare($sql);

    						     foreach ($headers as $key => $val)
    							 {
    							 	  $values = array();
    							 	  foreach ($column_arr as $k => $v)
    							 	  {
    							 	  	  $values[':'.$k] = empty($val[$k]) ? '' : $val[$k];	    							 	  	
    							 	  }   							      
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
}

