<?php
/**
 * 商品过滤类
 * @author J
 *
 */
class filterProduct extends msg
{

	/**
	 * 过滤商品资料数据
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
		//$customerArr = array();
		//global $db;
		foreach ($headers as $k => $v)
		{
			//校验SKU编码
			if (empty($v['SKU'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '商品编码不能为空', $v, $error_arr);
				continue;
			} else {
				//判断是否有重复数据
				if (in_array($v['SKU'], array_keys($exists_arr))) {
					$error_arr[$k] = $this->get_error_data($k, 'S006', '该商品编码数据重复', $v, $error_arr);
					continue;
				}							
			}
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
			//校验激活标志
			if (empty($v['Active_Flag'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '商品激活标志不能为空', $v, $error_arr);
				continue;
			} elseif (!in_array($v['Active_Flag'], array('Y', 'N'))) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '商品激活标志只能为Y和N', $v, $error_arr);
				continue;
			}
			//校验商品名称
			if (empty($v['Descr_C'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '商品名称或中文描述不能为空', $v, $error_arr);
				continue;
			} 
			$success_arr[$k] = $v;
			$exists_arr[$v['SKU']] = $v['SKU'];
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