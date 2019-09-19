<?php
/**
 * 出库单明细推送数据校验类
 * @author J
 *
 */
class filterConfirmSo extends msg
{

	/**
	 * 出库单明细推送数据校验
	 * @param $requestData
	 * @return 
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
			return $this->output(0, 'data不能为空', 'S003', '', $logExt);
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
		$existsOmsArr = array();
		$existsWmsArr = array();
		global $db;
		foreach ($headers as $k => $v)
		{
			//校验货主ID
			if(empty($v['CustomerID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不能为空', $v, $error_arr);
				continue;
			}				
			//校验所属仓库
			if (empty($v['WarehouseID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '所属仓库不能为空', $v, $error_arr);
				continue;
			}			
			//校验订单类型
			if (empty($v['OrderType'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型不能为空', $v, $error_arr);
				continue;
			} elseif (!in_array($v['OrderType'], array('SO', 'TO', 'RP', 'IL', 'OO','B2B'))) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型错误', $v, $error_arr);
				continue;
			} 
			//校验订单下发方
			if (empty($v['UserDefine4'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单下发方不能为空', $v, $error_arr);
				continue;
			} elseif (!in_array($v['UserDefine4'], array('ERP','OMS','WMS'))) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单下发方错误', $v, $error_arr);
				continue;				
			}
			//校验OMS订单号
			if (empty($v['OMSOrderNo']) && $v['UserDefine4'] != 'WMS') {
				$error_arr[$k] = $this->get_error_data($k, 'S003', 'OMS订单号不能为空', $v, $error_arr);
				continue;
			} 			
			if (!empty($v['OMSOrderNo']) && in_array($v['UserDefine4'], array('ERP', 'OMS'))) {
				//判断是否有重复数据
				if ($existsOmsArr[$v['WarehouseID']][$v['OrderType']][$v['OMSOrderNo']] !='') {
					$error_arr[$k] = $this->get_error_data($k, 'S006', 'OMS订单号重复', $v, $error_arr);
					continue;
				}				
				$sql = "SELECT order_id from t_outbound_info where order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
				$model = $db->prepare($sql);
				$model->bindParam(':order_no', $v['OMSOrderNo']);
				$model->bindParam(':order_type', $v['OrderType']);
				$model->bindParam(':customer_id', $v['CustomerID']);
				$model->bindParam(':warehouse_code', $v['WarehouseID']);
				$model->execute();
				$rs = $model->fetch(PDO::FETCH_ASSOC);
				if(empty($rs)) {
					$error_arr[$k] = $this->get_error_data($k, 'S003', 'OMS订单号不存在', $v, $error_arr);
					continue;
				}				
			}
			//校验WMS订单号
			if (empty($v['WMSOrderNo']) && $v['UserDefine4'] == 'WMS') {
				$error_arr[$k] = $this->get_error_data($k, 'S003', 'WMS订单号不能为空', $v, $error_arr);
				continue;
			}
			if (!empty($v['WMSOrderNo']) && $existsWmsArr[$v['WarehouseID']][$v['OrderType']][$v['WMSOrderNo']] != '') {
				//判断是否有重复数据
				$error_arr[$k] = $this->get_error_data($k, 'S006', 'WMS订单号重复', $v, $error_arr);
					continue;
			}																
			//校验订单创建时间
			if (empty($v['OrderTime'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单创建时间不能为空', $v, $error_arr);
				continue;
			} elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $v['OrderTime'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单创建时间格式错误', $v, $error_arr);
				break;
			} 
			//明细信息校验
			$m = 0;
			if (empty($v['item'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细不能为空', $v, $error_arr);
				continue;
			} else {
				if (empty($v['item'][0])) {
					$v['item'] = array($v['item']);
				}
				foreach ($v['item'] as $val)
				{
					if (empty($val['OrderNo'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中出库单号不能为空', $v, $error_arr);
						$m++;
						break;
					} else {   
						//验证单头中的出库单号和明细信息中的出库单号是否一致
						if (!empty($val['OMSOrderNo']) && $v['OrderNo'] != $val['OMSOrderNo']) {
							$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中出库单号与单头中的OMS订单号不一致', $v, $error_arr);
							$m++;
							break;
						} elseif (!empty($val['WMSOrderNo']) && $v['UserDefine4'] == 'WMS' && $v['OrderNo'] != $val['WMSOrderNo']) {
							$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中出库单号与单头中的WMS订单号不一致', $v, $error_arr);
							$m++;
							break;
						}
					}
					//验证SKU
					if (empty($val['SKU'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中SKU不能为空', $v, $error_arr);
						$m++;
						break;
					}
// 					//验证订货数
// 					if ($val['QtyOrdered'] == '') {
// 						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中订货数不能为空', $v, $error_arr);
// 						$m++;
// 						break;
// 					} elseif (!preg_match("/(^\d{1,10}$)|(^\d{1,10}\.?[\d]{1,8}$)/", $val['QtyOrdered'])) {
// 						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中订货数必须为数字', $v, $error_arr);
// 						$m++;
// 						break;
// 					}
					//验证实际发运数量
					if ($val['QtyShipped'] =='') {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中实际发运数量不能为空', $v, $error_arr);
						$m++;
						break;
					} elseif (!preg_match("/(^\d{1,10}$)|(^\d{1,10}\.?[\d]{1,8}$)/", $val['QtyShipped'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中实际发运数量必须为数字', $v, $error_arr);
						$m++;
						break;
					}
					//验证实际发运时间 
					if (empty($val['ShippedTime'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中实际发运时间不能为空', $v, $error_arr);
						$m++;
						break;
					} elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $val['ShippedTime'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '出库单明细中实际发运时间格式错误', $v, $error_arr);
						$m++;
						break;
					}
				}
			}
			if ($m > 0) {
				continue;
			}
			
			$success_arr[$k] = $v;
			$existsOmsArr[$v['WarehouseID']][$v['OrderType']][$v['OMSOrderNo']] = $v['OMSOrderNo'];
			$existsWmsArr[$v['WarehouseID']][$v['OrderType']][$v['WMSOrderNo']] = $v['WMSOrderNo'];
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
			if ($data['UserDefine4'] == 'WMS') {
				$data['OrderNo'] = $data['WMSOrderNo'];
			} else {
				$data['OrderNo'] = $data['OMSOrderNo'];
			}
			$return_arr = $data;
			$return_arr['errorcode'] = $errorCode;
			$return_arr['errordescr'] = $errorDescr;
		} else {
			$return_arr = $error_arr[$key];
		}
		return $return_arr;
	}
}