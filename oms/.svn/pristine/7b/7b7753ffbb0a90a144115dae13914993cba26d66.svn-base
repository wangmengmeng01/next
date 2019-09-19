<?php
/**
 * 入库单取消过滤类
 * @author J
 *
 */
class filterCancelAsn extends msg
{
	/**
	 * 入库单取消数据校验
	 * @param $requestData
	 * @return 
	 */
	public function create(&$requestData)
	{
	    if(empty($requestData['header'])) {
			//接口层记录日志
			$logExt = array(
					'api_url' => '',
					'api_method' => service::$_method,
					'api_params' => $requestData,
					'return_msg' => 'filter: header不能为空'
			);
			return $this->output(0, 'header不能为空', 'S003', '', $logExt);
		}
        //判断是否为二维数组
		$utilObj = new util();
		$multiFlag = $utilObj->isArrayMulti($requestData);
		if ($multiFlag) {
			$headers = $requestData['header'];
		} else {
			$headers = array($requestData['header']);
		}
	    //判断是否为批量取消
		if (count($headers)>1) {
			//接口层记录日志
			$logExt = array(
					'api_url' => '',
					'api_method' => service::$_method,
					'api_params' => $requestData,
					'return_msg' => '不允许批量取消订单'
			);
			return $this->output(0, '不允许批量取消订单', 'S003', '', $logExt);
		}
		
		$error_arr = array();
		$success_arr = array();
		$exists_arr = array();
		$customerArr = array();
		$warehouseArr = array();
		global $db;
		foreach ($headers as $k => $v)
		{
			//校验货主ID
			if(empty($v['CustomerID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不能为空', $v, $error_arr);
				continue;
			} else {
				if (!in_array($v['CustomerID'], $customerArr) && STRICT_VERIFY_FLAG) {									
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
			}    
			//校验所属仓库
			if (empty($v['WarehouseID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '所属仓库不能为空', $v, $error_arr);
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
			//校验订单类型
			if (empty($v['OrderType'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型不能为空', $v, $error_arr);
				continue;
			} elseif (!in_array($v['OrderType'], array('PO', 'TR', 'RS', 'IP'))) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型错误', $v, $error_arr);
				continue;
			}
			//校验入库单号
			if (empty($v['OrderNo'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单号不能为空', $v, $error_arr);
				continue;
			} else {
				//判断是否有重复数据
				if (in_array($v['OrderNo'], array_keys($exists_arr))) {
					$error_arr[$k] = $this->get_error_data($k, 'S006', '取消订单'.$v['OrderNo'].'数据重复', $v, $error_arr);
					continue;
				}								
				$sql = "SELECT order_id,order_status from t_inbound_info where order_no=:order_no AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
				$model = $db->prepare($sql);
				$model->bindParam(':order_no', $v['OrderNo']);
				$model->bindParam(':order_type', $v['OrderType']);
				$model->bindParam(':customer_id', $v['CustomerID']);
				$model->bindParam(':warehouse_code', $v['WarehouseID']);
				$model->execute();
				$rs = $model->fetch(PDO::FETCH_ASSOC);
				if(empty($rs)) {
					$error_arr[$k] = $this->get_error_data($k, 'S006', '该入库单号不存在', $v, $error_arr);
					continue;
				} elseif ($rs['order_status'] == '90') {
					$error_arr[$k] = $this->get_error_data($k, 'S006', '该入库单号状态为已经取消', $v, $error_arr);
					continue;
				}				
			}
			
			$success_arr[$k] = $v;
			$exists_arr[$v['OrderNo']] = $v['OrderNo'];
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
					'return_msg' => $error_arr[0]['errordescr']
			);
			return $this->output(0, $error_arr[0]['errordescr'], '0001', $xmlData, $logExt);
		} else {
			$requestData = array('header' => $success_arr);
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