<?php
/**
 * 奇门仓内加工单创建接口业务处理类
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsStoreProcessCreate extends wmsRequest
{
	/**
	 * 创建仓内加工单
	 * @param $params
	 * @return array
	 */
	public function create($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
			//转发数据给wms
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
				if ($response['flag'] == 'success') {
				    $processOrderCode = $params['processOrderCode'];
				    $orderType = $params['orderType'];
				    $customerId = qimen_service::$_customerId;
				    $warehouseCode = $params['warehouseCode'];
				    $this->hadOrderProcess($processOrderCode, $orderType, $customerId, $warehouseCode);
					//写入数据库
					$this->insertStoreProcessOrder($params);
					//返回
					return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
				} else {
					return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
				}
			} else {
				return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
			}
		}
	}
	
	/**
	 * 是否存在仓内加工单
	 * @param 加工单编码 $processOrderCode
	 * @param 单据类型 $orderType
	 * @param 货主编码 $customerId
	 * @param 仓库编码 $warehouseCode
	 * @return boolean
	 */
	public function hadOrderProcess($processOrderCode, $orderType, $customerId, $warehouseCode){
	    $values = array();
	    $Ssql = '';
	    $Usql = '';
	    global $db;
	    $values[':process_order_code'] = $processOrderCode;
	    $values[':order_type'] = $orderType;
	    $values[':customer_id'] = $customerId;
	    $values[':warehouse_code'] = $warehouseCode;
	    $Ssql = 'SELECT COUNT(*) t, process_id FROM `t_store_process_order_info` WHERE process_order_code = :process_order_code AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1;';
	    $datas = $db->prepare($Ssql);
	    $datas->execute($values);
	    $rs = $datas->fetch(PDO::FETCH_ASSOC);
	    if ($rs['t'] > 0) {
	        $Usql = ' UPDATE `t_store_process_order_info` SET is_valid = 0 WHERE process_order_code = :process_order_code AND order_type=:order_type AND customer_id = :customer_id AND warehouse_code=:warehouse_code; ';
	        $Usql .= ' UPDATE `t_store_process_order_material_info` SET is_valid = 0 WHERE process_id= :process_id; UPDATE `t_store_process_order_product_info` SET is_valid = 0 WHERE process_id= :process_id; ';
	        $values[':process_id'] = $rs['process_id'];
	        $model = $db->prepare($Usql);
	        if ($model->execute($values)) {
	            return true;
	        } else {
	            return false;
	        }
	    } else {
	        return true;
	    }
	}
	
	/**
	 * 写入仓内加工单到数据库
	 * @param $params
	 * @return boolean
	 */
	public function insertStoreProcessOrder($params)
	{
	    $materialItem = $params['materialitems']['item'];
	    $productItem = $params['productitems']['item'];
		global $db;
		$column_arr = $this->get_dataBase_relation('insert_store_process_order');
		$column_key_arr = implode(',', array_values($column_arr)) . ',create_time';
		
		$column_material_arr = $this->get_dataBase_relation('insert_store_process_material');
		$column_material_key_arr = implode(',', array_values($column_material_arr)) . ',process_id,create_time';
		
		$column_product_arr = $this->get_dataBase_relation('insert_store_process_product');
		$column_product_key_arr = implode(',', array_values($column_product_arr)) . ',process_id,create_time';
		
		$column_value_arr = ':' . implode(',:', array_values($column_arr)) . ',now()';
		$sql = "INSERT INTO t_store_process_order_info({$column_key_arr}) VALUES({$column_value_arr})";
		$model = $db->prepare($sql);
		$values = array();
		foreach ($column_arr as $k => $v) {
		    $values[':' . $v] = empty($params[$k]) ? '' : $params[$k] ;
		}
		$values[':customer_id'] = qimen_service::$_customerId;
		$model->execute($values);
		$processId = $db->lastInsertID();
		
		$column_material_value_arr = ':' . implode(',:', array_values($column_material_arr)) . ",$processId,now()";
		$sql = "INSERT INTO t_store_process_order_material_info({$column_material_key_arr}) VALUES({$column_material_value_arr})";
		$model = $db->prepare($sql);
		$values = array();
		if (empty($materialItem[0])) {
		    $materialItem = array($materialItem);
		}
		foreach ($materialItem as $m_v) {
		    foreach ($column_material_arr as $m_k_c => $m_v_c) {
		        if ($m_k_c == 'inventoryType') {
		            $values[':' . $m_v_c] = empty($m_v[$m_k_c]) ? 'ZP' : $m_v[$m_k_c] ;
		        } else {
		            $values[':' . $m_v_c] = empty($m_v[$m_k_c]) ? '' : $m_v[$m_k_c] ;
		        }
		    }
		    $values[':process_order_code'] = $params['processOrderCode'];
		    $model->execute($values);
		}
		
		$column_product_value_arr = ':' . implode(',:', array_values($column_product_arr)) . ",$processId,now()";
		$sql = "INSERT INTO t_store_process_order_product_info({$column_product_key_arr}) VALUES({$column_product_value_arr})";
		$model = $db->prepare($sql);
		$values = array();
		if (empty($productItem[0])) {
		    $productItem = array($productItem);
		}
		foreach ($productItem as $p_v) {
		    foreach ($column_product_arr as $p_k_c => $p_v_c) {
		        if ($p_k_c == 'inventoryType') {
		            $values[':' . $p_v_c] = empty($p_v[$p_k_c]) ? 'ZP' : $p_v[$p_k_c] ;
		        } else {
		            $values[':' . $p_v_c] = empty($p_v[$p_k_c]) ? '' : $p_v[$p_k_c] ;
		        }
		    }
		    $values[':process_order_code'] = $params['processOrderCode'];
		    $model->execute($values);
		}
	}
}