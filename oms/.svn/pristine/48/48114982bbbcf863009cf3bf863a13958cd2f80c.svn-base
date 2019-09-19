<?php
/**
 * 仓库操作类
 * @author wp
 *
 */
class filterWarehouse extends msg
{

	/**
	 * 创建仓库ID
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
		global $db;
		foreach ($headers as $k => $v)
		{
			//校验仓库ID
			if (empty($v['CustomerID'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库ID不能为空', $v, $error_arr);
				continue;
			} else {
				//判断是否有重复数据
				if (in_array($v['CustomerID'], array_keys($exists_arr))) {
					$error_arr[$k] = $this->get_error_data($k, 'S006', '该仓库ID数据重复', $v, $error_arr);
					continue;
				}				
				$sql = "SELECT warehouse_id from t_base_warehouse where warehouse_code=:warehouse_code";
				$model = $db->prepare($sql);
				$model->bindParam(':warehouse_code', $v['CustomerID']);
				$model->execute();
				$rs = $model->fetch(PDO::FETCH_ASSOC);
				if(!empty($rs)) {
					$error_arr[$k] = $this->get_error_data($k, 'S006', '该仓库ID已经存在', $v, $error_arr);
					continue;
				}				
			}
			//校验仓库类型
			if(empty($v['Customer_Type'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库类型不能为空', $v, $error_arr);
				continue;
			} else {
				if ($v['Customer_Type'] != 'WH') {
					$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库类型错误', $v, $error_arr);
					continue;
				}
			}
			//校验仓库名称
			if (empty($v['Descr_C'])) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库名称或中文描述不能为空', $v, $error_arr);
				continue;
			} 
			$success_arr[$k] = $v;
			$exists_arr[$v['CustomerID']] = $v['CustomerID'];
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