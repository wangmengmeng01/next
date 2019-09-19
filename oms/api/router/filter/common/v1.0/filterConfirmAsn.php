<?php
/**
 * 入库单状态明细过滤类
 * @author Jeremy
 *
 */
class filterConfirmAsn extends msg 
{

	/**
	 * 入库单状态明细信息校验
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
		$existsOmsArr = array();
		$existsWmsArr = array();
		global $db;
		foreach ($headers as $k => $v)
		{
			//单头信息校验
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
			//校验验订单类型
			if(empty($v['OrderType'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型不能为空', $v, $error_arr);
				continue;
			} elseif (!in_array($v['OrderType'], array('PO', 'TR', 'RS', 'IP'))) {
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
				if (in_array($v['OMSOrderNo'], array_keys($existsOmsArr[$v['OMSOrderNo']][$v['OrderType']]))) {
					$error_arr[$k] = $this->get_error_data($k, 'S006', 'OMS订单号重复', $v, $error_arr);
					continue;
				}
				$sql = "SELECT order_id from t_inbound_info where order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code";
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
			//判断是否有重复数据
			if (!empty($v['WMSOrderNo']) && in_array($v['WMSOrderNo'], array_keys($existsWmsArr[$v['WMSOrderNo']][$v['OrderType']]))) {
				$error_arr[$k] = $this->get_error_data($k, 'S006', 'WMS订单号重复', $v, $error_arr);
					continue;
			}												
			//校验订单状态
			if (empty($v['Status'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单状态不能为空', $v, $error_arr);
				continue;
			} elseif (!in_array($v['Status'], array('30', '40', '99'))) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单状态错误', $v, $error_arr);
				continue;
			} 		
			//校验状态描述
			if (empty($v['Desc'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '状态描述不能为空', $v, $error_arr);
				continue;
			}
			//明细信息校验
			$m = 0;
			if (empty($v['item'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细不能为空', $v, $error_arr);
				continue;
			} else {
				if (empty($v['item'][0])) {
					$v['item'] = array($v['item']);
				}
				foreach ($v['item'] as $val)
				{
                    //验证货主					
					if (empty($val['CustomerID'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中货主ID不能为空', $v, $error_arr);
						$m++;
						break;
					} else {   //验证单头中的货主ID和明细信息中的货主ID是否一致
						if ($v['CustomerID'] != $val['CustomerID']) {
							$error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中货主ID与单头中的货主ID不一致', $v, $error_arr);
							$m++;
							break;
						}
					}	
					//验证SKU				
					if (empty($val['SKU'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中SKU不能为空', $v, $error_arr);
						$m++;
						break;
					}
					//验证订单明细状态
					if (empty($val['LineStatus'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细状态不能为空', $v, $error_arr);
						$m++;
						break;
					} elseif (!in_array($val['LineStatus'], array('30', '40', '99'))) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细状态错误', $v, $error_arr);
						$m++;
						continue;
					}
					//验证订单明细状态描述
					if (empty($val['LineDesc'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细状态描述不能为空', $v, $error_arr);
						$m++;
						break;
					}
					//验证实收数量
					if (empty($val['ReceivedQty']) && $val['ReceivedQty'] != 0) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '实收数量不能为空', $v, $error_arr);
						$m++;
						break;
					}
					//验证收货时间,YYYY-MM-DD HH:MM:SS 格式
					if (empty($val['ReceivedTime'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '收货时间不能为空', $v, $error_arr);
						$m++;
						break;
					} elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $val['ReceivedTime'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '收货时间格式错误', $v, $error_arr);
						$m++;
						break;
					}
				}
			}
			if ($m > 0) {
				continue;
			}
								
			$success_arr[$k] = $v;
			$existsOmsArr[$v['OMSOrderNo']][$v['OrderType']] = $v['OMSOrderNo'];
			$existsWmsArr[$v['WMSOrderNo']][$v['OrderType']] = $v['WMSOrderNo'];
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
			$requestData = array('data' => array('orderinfo' => $success_arr));
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