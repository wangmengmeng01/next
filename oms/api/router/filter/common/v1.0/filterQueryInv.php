<?php
/**
 * 库存查询过滤类
 *
 */
class filterQueryInv extends msg
{

	/**
	 * 过滤库存查询数据
	 * @param $requestData
	 * @return 
	 */
	public function create(&$requestData)
	{
		if(empty($requestData['data']['header'])) {
			//接口层记录日志
			$logExt = array(
					'api_url' => '',
					'api_method' => service::$_method,
					'api_params' => $requestData,
					'return_msg' => 'filter: header不能为空'
			);
			return $this->output(0, 'header不能为空', 'S003', '', $logExt);
		}
		$utilObj = new util();
		$multiFlag = $utilObj->isArrayMulti($requestData);
		if ($multiFlag) {
			$headers = $requestData['data']['header'];
		} else {
			$headers = array($requestData['data']['header']);
		}	

		$error_arr = array();
		$success_arr = array();
		$exists_arr = array();
		//$customerArr = array();
		$warehouseArr = array();
		$skuArr = array();
		global $db;
		foreach ($headers as $k => $v)
		{
			//校验货主ID
			if(empty($v['CustomerID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不能为空', $v, $error_arr);
				continue;
			} else {
				if ($v['CustomerID'] != service::$_customerId) {
					$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID错误', $v, $error_arr);
				}
				/*
				if (!in_array($v['CustomerID'], $customerArr)) {									
					$sql = "SELECT customer_id FROM t_base_customer WHERE customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
					$model = $db->prepare($sql);
					$model->bindParam(':customer_id', $v['CustomerID']);
					$model->execute();
					$rs = $model->fetch(PDO::FETCH_ASSOC);
					if (empty($rs)) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不存在或者无效', $v, $error_arr);
						continue;
					} elseif ($rs['customer_id'] != $v['CustomerID']) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID大小写错误', $v, $error_arr);
						continue;
					} else {
						$customerArr[$k] = $v['CustomerID'];
					}
				}
				*/
			}    				
			//校验仓库ID
			if (empty($v['WarehouseID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库ID不能为空', $v, $error_arr);
				continue;
			} else {
        		if (!in_array($v['WarehouseID'], $warehouseArr) && STRICT_VERIFY_FLAG) {
        			$sql = "SELECT warehouse_code FROM t_base_warehouse WHERE warehouse_code=:warehouse_code AND active_flag='Y' AND is_valid=1";
        			$model = $db->prepare($sql);
        			$model->bindParam(':warehouse_code', $v['WarehouseID']);
        			$model->execute();
        			$rs = $model->fetch(PDO::FETCH_ASSOC);
        			if (empty($rs)) {
        				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库编码不存在或者无效', $v, $error_arr);
        				continue;
        			} elseif ($rs['warehouse_code'] != $v['WarehouseID']) {
        				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库编码大小写错误', $v, $error_arr);
        				continue;
        			} else {
        				$warehouseArr[$k] = $v['WarehouseID'];
        			}
        		}
        	}	
        	//校验SKU
        	if (!empty($v['SKU']) && STRICT_VERIFY_FLAG && !in_array($v['SKU'], $skuArr)) {
    			//校验SKU是否存在
    			$sql = "SELECT sku FROM t_base_product WHERE sku=:sku AND customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
    			$model = $db->prepare($sql);
    			$model->bindParam(':sku', $v['SKU']);
    			$model->bindParam(':customer_id', $v['CustomerID']);
    			$model->execute();
    			$rs = $model->fetch(PDO::FETCH_ASSOC);
    			if (empty($rs)) {
    				$error_arr[$k] = $this->get_error_data($k, 'S003', 'SKU:'.$v['SKU'].'不存在或不属于货主', $v, $error_arr);
    				continue;
    			} elseif ($rs['sku'] != $v['SKU']) {
    				$error_arr[$k] = $this->get_error_data($k, 'S003', 'SKU大小写错误', $v, $error_arr);
    				continue;
    			}
    			$skuArr[$k] = $v['SKU'];
        	}
			//判断是否有重复数据
			if (in_array($v['WarehouseID'].$v['CustomerID'].$v['SKU'], array_values($exists_arr))) {
				$error_arr[$k] = $this->get_error_data($k, 'S006', '数据重复', $v, $error_arr);
				continue;
			}
			$exists_arr[] = $v['WarehouseID'].$v['CustomerID'].$v['SKU'];
			$success_arr[$k] = $v;
		}		
		if(!empty($error_arr)) {
			$xmlData = $this->get_error_str($error_arr);
		}
		msg::$err_arr = $error_arr;
		if (empty($success_arr)) {		
			//接口层记录日志
			$logExt = array(
					'api_url' => '',
					'api_method' => service::$_method,
					'api_params' => $requestData,
					'return_msg' => 'filter: 数据校验不通过'
			);
			return $this->output(0, 'filter: 数据校验不通过', '0001', $xmlData, $logExt);
		} else {
			$requestData = array('data'=>array('header' => $success_arr));
		} 		
		return $this->output('succ');
	}

	/**
	 * 记录错误数据
	 * @param $key  键名
	 * @param $errorCode  错误编码
	 * @param $errorDescr 错误描述
	 * @param $data       错误详细数据
	 * @param $error_arr  错误数组
	 * @return $error_arr
	 */
	public function get_error_data($key, $errorCode, $errorDescr, $data, $error_arr)
	{
		$return_arr = array();
		if (empty($error_arr[$key])) {
			$return_arr = $data;
			$return_arr['errorcode'] = $errorCode;
			$return_arr['errordescr'] = $errorDescr;
		} else {
			$return_arr = $error_arr[$key];
		}
		return $return_arr;
	}
}