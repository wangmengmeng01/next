<?php
/**
 * 供应商和店铺应用级参数校验类
 * @author wp
 *
 */
class filterSupplierAndShop extends msg
{
	/**
	 * 供应商或店铺信息校验
	 * @param $requestData
	 * @return xml
	 */
	public function create(&$requestData)
	{
		if(empty($requestData['header'])) {
			//接口层记录日志
			$logExt = array(
					'api_url' => '',
					'api_method' => service::$_method,
					'api_params' => $requestData,
					'return_msg' => 'filter: orderinfo不能为空'
			);
			return $this->output(0, 'header不能为空', 'S003', '', $logExt);
		}
		$utilObj = new util();
		$multiFlag = $utilObj->isArrayMulti($requestData);
		if ($multiFlag) {
			$headers = $requestData['header'];
		} else {
			$headers = array($requestData['header']);
		}
		
		$error_arr = array();
		$success_arr = array();
		$exists_arr = array();
		global $db;
		foreach ($headers as $k => $v)
		{
			//校验验供应商或店铺类型
			if(empty($v['Customer_Type'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '类型参数Customer_Type不能为空', $v, $error_arr);
				continue;
			} else {
				if (!in_array($v['Customer_Type'], array('VE', 'OT'))) {
					$error_arr[$k] = $this->get_error_data($k, 'S003', '类型参数Customer_Type:'.$v['Customer_Type'].'错误', $v, $error_arr);
					continue;
				} else {
					if ($v['Customer_Type'] == 'VE') {  //供应商信息校验
						//校验供应商ID
						if (empty($v['CustomerID'])) {
							$error_arr[$k] = $this->get_error_data($k, 'S003', '供应商ID不能为空', $v, $error_arr);
							continue;
						} else {
							if (!empty($exists_arr['VE'][$v['CustomerID']])) {
								$error_arr[$k] = $this->get_error_data($k, 'S006', '该供应商ID数据重复', $v, $error_arr);
								continue;
							}							
						}
						//校验供应商名称
						if (empty($v['Descr_C'])) {
							$error_arr[$k] = $this->get_error_data($k, 'S003', '供应商中文描述不能为空', $v, $error_arr);
							continue;
						}
					} elseif ($v['Customer_Type'] == 'OT') {  //校验店铺信息
						//校验店铺ID
						if (empty($v['CustomerID'])) {
							$error_arr[$k] = $this->get_error_data($k, 'S003', '店铺ID不能为空', $v, $error_arr);
							continue;
						} else {
							if (!empty($exists_arr['OT'][$v['CustomerID']])) {
								$error_arr[$k] = $this->get_error_data($k, 'S006', '该店铺ID数据重复', $v, $error_arr);
								continue;
							}
						}
						//校验店铺名称
						if (empty($v['Descr_C'])) {
							$error_arr[$k] = $this->get_error_data($k, 'S003', '店铺中文描述不能为空', $v, $error_arr);
							continue;
						}
					}
					//校验激活标志
					if (empty($v['Active_Flag'])) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '激活标志不能为空', $v, $error_arr);
						continue;
					} elseif (!in_array($v['Active_Flag'], array('Y', 'N'))) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '激活标志只能为Y或N', $v, $error_arr);
						continue;
					}
					//校验该货主是否有操作权限
					$sql = 'SELECT customer_id FROM t_customer_relation WHERE code=:code AND type=:type AND is_valid=1';
					$model = $db->prepare($sql);
					$model->bindParam(':code', $v['CustomerID']);
					$model->bindParam(':type', $v['Customer_Type']);
					$model->execute();
					$rs = $model->fetchAll(PDO::FETCH_ASSOC);
					$accessArr = array();
					if (!empty($rs)) {
						foreach ($rs as $rsVal)
						{
							$accessArr[] = $rsVal['customer_id'];
						}
					}
					if (!empty($rs) && !in_array(service::$_customerId, $accessArr)) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '你没有操作CustomerID:'.$v['CustomerID'].'的权限', $v, $error_arr);
						continue;
					}
				}
			}						
			$success_arr[$k] = $v;
			$exists_arr[$v['Customer_Type']][$v['CustomerID']] = $v['CustomerID'];
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