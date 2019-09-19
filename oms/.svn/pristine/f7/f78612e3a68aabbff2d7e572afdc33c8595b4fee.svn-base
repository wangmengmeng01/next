<?php
/**
 * 奇门仓内加工单确认业务处理类
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpStoreProcessConfirm extends erpRequest
{
	/**
	 * 推送erp仓内加工单确认信息
	 * @param $params
	 * @return array
	 */
	public function confirm($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
		    //转发数据给erp
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
				if ($response['flag'] == 'success') {
					//写入数据库
					$this->insertStoreProcessConfirmOrder($params);
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
	 * 写入仓内加工单确认数据到数据库
	 */
	public function insertStoreProcessConfirmOrder($params)
	{
	    $materialItem = $params['materialitems']['item'];
	    $productItem = $params['productitems']['item'];
		global $db;
		$column_arr = $this->get_dataBase_relation('store_process_confirm');
		$column_key_arr  = implode(',', array_values($column_arr)) . ',process_id,create_time';
		
		$column_material_arr = $this->get_dataBase_relation('store_process_material_confirm');
		$column_material_key_arr = implode(',', array_values($column_material_arr)) . ',process_id,create_time';
		
		$column_product_arr = $this->get_dataBase_relation('store_process_product_confirm');
		$column_product_key_arr = implode(',', array_values($column_product_arr)) . ',process_id,create_time';
		
		$sql = "SELECT process_id,warehouse_code FROM t_store_process_order_info WHERE process_order_code = :process_order_code AND customer_id = :customer_id AND order_type = :order_type";
		$model = $db->prepare($sql);
		$model->bindParam(':process_order_code', $params['processOrderCode']);
		//$model->bindParam(':customer_id', qimen_service::$_customerId);
		$model->bindParam(':customer_id', $params['ownerCode']);
		$model->bindParam(':order_type', $params['orderType']);
		$model->execute();
		$rs = $model->fetch(PDO::FETCH_ASSOC);
		$processId = $rs['process_id'];
		$warehouseCode = $rs['warehouse_code'];
		
		if ($processId != '') {
		    $column_value_arr = ':' . implode(',:', array_values($column_arr)) . ",'{$processId}',now()";
		    $sql = "INSERT INTO t_store_process_order_record({$column_key_arr}) VALUES({$column_value_arr})";
		    $model = $db->prepare($sql);
		    $values = array();
		    foreach ($column_arr as $k => $v) {
		        $values[':' . $v] = empty($params[$k]) ? '' : $params[$k] ;
		    }
		    //$values[':customer_id'] = qimen_service::$_customerId;
		    $values[':warehouse_code'] = $warehouseCode;
		    $model->execute($values);
		    
		    $column_material_value_arr = ':' . implode(',:', array_values($column_material_arr)) . ",'{$processId}',now()";
		    $sql = "INSERT INTO t_store_process_order_material_record({$column_material_key_arr}) VALUES({$column_material_value_arr})";
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
		    
		    $column_product_value_arr = ':' . implode(',:', array_values($column_product_arr)) . ",'{$processId}',now()";
		    $sql = "INSERT INTO t_store_process_order_product_record({$column_product_key_arr}) VALUES({$column_product_value_arr})";
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
}