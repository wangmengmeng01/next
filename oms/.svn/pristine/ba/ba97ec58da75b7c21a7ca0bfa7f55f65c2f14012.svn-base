<?php
/**
 * oms仓库类
 * 方向 WMS->OMS
 * @author wp
 *
 */
require_once API_ROOT . '/router/interface/oms/common/omsRequest.php';
class omsWarehouse extends omsRequest
{
	/**
	 * 创建仓库ID
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
    				//获取仓库基础信息接口参数与数据库字段对应关系
    				$column_arr = $this->get_dataBase_relation('wareHouse');
    				$column_key_str = implode(',', array_values($column_arr)) . ',create_time';    						 	 
    				$column_value_str = ':' . implode(',:', array_keys($column_arr)) . ',now()';
    				$sql = "INSERT IGNORE INTO t_base_warehouse({$column_key_str}) VALUES({$column_value_str})";
    				$model = $db->prepare($sql);

    				foreach ($headers as $val)
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
    		return $this->msgObj->output(0, '仓库ID创建失败', '0001', $xmlData);
    	}
    } 
    
}

