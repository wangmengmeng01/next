<?php
/**
 * 出库单状态回传过滤类
 * @author Jeremy
 *
 */
class filterConfirmSoStatus extends msg 
{

	/**
	 * 出库单状态回传信息校验
	 * @param $requestData
	 * @return xml
	 */
	public function push(&$requestData)
	{
		if(empty($requestData['data']['orderinfo'])) {
			//接口层记录日志
			$logExt = array(
					'api_url' => '',
					'api_method' => service::$_method,
					'api_params' => $requestData,
					'return_msg' => 'filter: orderinfo不能为空'
			);
			return $this->output(0, 'orderinfo不能为空', 'S003', '', $logExt);
		}
		$utilObj = new util();
		$multiFlag = $utilObj->isArrayMulti($requestData);
		if ($multiFlag) {
			$headers = $requestData['data']['orderinfo'];
		} else {
			$headers = array($requestData['data']['orderinfo']);
		}
		$error_arr = array();
		$success_arr = array();
		$exists_arr = array();
		global $db;
		foreach ($headers as $k => $v)
		{		
			//校验出库单单号
			if (empty($v['OrderNo'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单单号不能为空', $v, $error_arr);
				continue;
			} 		
			//校验验订单类型
			if (empty($v['OrderType'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型不能为空', $v, $error_arr);
				continue;
			} elseif (!in_array($v['OrderType'], array('SO', 'TO', 'RP', 'IL', 'OO','B2B'))) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型错误', $v, $error_arr);
				continue;
			} 				
			//校验货主
			if (empty($v['CustomerID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不能为空', $v, $error_arr);
				continue;
			}				
			//校验仓库ID
			if (empty($v['WarehouseID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库ID不能为空', $v, $error_arr);
				continue;
			}			
			//校验出库单号是否存在
			$sql='SELECT order_id FROM t_outbound_info WHERE order_no = :order_no AND order_type = :order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
			$model = $db->prepare($sql);
			$model->bindParam(':order_no', $v['OrderNo']);
			$model->bindParam(':order_type', $v['OrderType']);
			$model->bindParam(':customer_id', $v['CustomerID']);
			$model->bindParam(':warehouse_code', $v['WarehouseID']);
			$model->execute();
			$rs = $model->fetch(PDO::FETCH_ASSOC);
			if(empty($rs)) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单单号不存在', $v, $error_arr);
				continue;
			}
			//校验订单状态
			if (empty($v['Status'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单状态不能为空', $v, $error_arr);
				continue;
			} 
// 			elseif (!in_array($v['Status'], array('40', '60','63', '99'))) {
// 				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单状态错误', $v, $error_arr);
// 				continue;
// 			} 		
			//校验状态描述
			if (empty($v['Desc'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '状态描述不能为空', $v, $error_arr);
				continue;
			}
			//校验操作时间
			if (empty($v['Time'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '操作时间不能为空', $v, $error_arr);
				continue;
			} elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $v['Time'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '操作时间格式错误', $v, $error_arr);
				continue;
			}
						
			$success_arr[$k] = $v;
			$exists_arr[$v['OrderNo']]= $v['OrderNo'];
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
			$requestData = array('data'=>array('orderinfo' => $success_arr));
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