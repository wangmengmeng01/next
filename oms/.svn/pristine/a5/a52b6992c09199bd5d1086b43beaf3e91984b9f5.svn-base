<?php
/**
 * 商品同步接口(批量)过滤类
 * @author Renee
 */
class filterItemsCreate extends msg
{
	public function create(&$requestData)
	{
	    //连接数据库
		global $db;
		
		//校验数据是否为空
		if (empty($requestData)) {
		    $logExt = $this->return_msg(qimen_service::$_method, $requestData, 'body中数据不能为空');
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003', '', $logExt);
		}
        
		$sucArr = array();//存放校验成功的商品信息
		$errArr = array();//存放错误数据
		
		//校验操作类型
		if (empty($requestData['actionType'])) {
		    $logExt = $this->return_msg(qimen_service::$_method, $requestData, '操作类型不能为空');
		    return $this->outputQimen('failure', '操作类型不能为空', 'S003', '', $logExt);
		} else {
		    //判断操作类型是否正确
		    if (!in_array($requestData['actionType'], array("add","update"))) {
		        $logExt = $this->return_msg(qimen_service::$_method, $requestData, '操作类型错误或者无效');
		        return $this->outputQimen('failure', '操作类型错误或者无效', 'S003', '', $logExt);
		    }
		}
		//校验仓库
		if (empty($requestData['warehouseCode'])) {
		    $logExt = $this->return_msg(qimen_service::$_method, $requestData, '仓库编码不能为空');
		    return $this->outputQimen('failure', '仓库编码不能为空', 'S003', '', $logExt);
		}
		//校验货主
		if (empty($requestData['ownerCode'])) {
		    $logExt = $this->return_msg(qimen_service::$_method, $requestData, '货主编码不能为空');
		    return $this->outputQimen('failure', '货主编码不能为空', 'S003', '', $logExt);
		} else {
	        $querySql = "SELECT customer_id FROM t_base_customer WHERE customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
	        $model = $db->prepare($querySql);
	        $model->bindParam(':customer_id', $requestData['ownerCode']);
	        $model->execute();
	        $rs = $model->fetch(PDO::FETCH_ASSOC);
	        if (empty($rs)) {
	            $logExt = $this->return_msg(qimen_service::$_method, $requestData, '货主ID不存在或者无效');
	            return $this->outputQimen('failure', '货主ID不存在或者无效', 'S003');
	        } else {
	            if ($rs['customer_id'] != $requestData['ownerCode']) {
	                $logExt = $this->return_msg(qimen_service::$_method, $requestData, '货主ID大小写错误');
    	            return $this->outputQimen('failure', '货主ID大小写错误', 'S003');
	            }
	        }
		}
		
		$items = $requestData['items']['item'];
		if (empty($items[0])) {
		    $items = array($items);
		}
		foreach ($items as $key => $val) {
		    //校验商品编码
		    if (empty($val['itemCode'])) {
		        $errArr[$key] = $this->get_error_data($key, '商品编码不能为空', $val['itemCode'], $errArr);
		        continue;
		    }
		    //校验商品名称
		    if (empty($val['itemName'])) {
		        $errArr[$key] = $this->get_error_data($key, '商品名称不能为空', $val['itemCode'], $errArr);
		        continue;
		    }
		    //校验条形码
		    if (empty($val['barCode'])) {
		        $errArr[$key] = $this->get_error_data($key, '条形码不能为空', $val['itemCode'], $errArr);
		        continue;
		    }
		    //校验商品类型
		    $types = array('ZC','FX','ZH','ZP','BC','HC','FL','XN','FS','CC','OTHER');
		    if (empty($val['itemType'])) {
		        $errArr[$key] = $this->get_error_data($key, '商品类型不能为空', $val['itemCode'], $errArr);
		        continue;
		    } else {
		        if (!in_array($val['itemType'], $types)) {
		            $errArr[$key] = $this->get_error_data($key, '该商品类型' . $val['itemType'] . '不存在', $val['itemCode'], $errArr);
		            continue;
		        }
		    }
		    $sucArr[$key] = $val;
		}
		
		msg::$err_arr = $errArr;

		//组装返回格式
		$xmlObj = new xml();
		$xmlData = '';
		foreach ($errArr as $b) {
		    $xmlData .= '<item>';
		    $xmlData .= $xmlObj->array2xml($b);
		    $xmlData .= '</item>';
		}
		
		if (empty($sucArr)) {
		    //接口层记录日志
		    $logExt = $this->return_msg(qimen_service::$_method, $requestData, $xmlData);
		    return $this->outputQimen('failure', '校验不通过', 'S003', $logExt, $xmlData);
		} else {
		    foreach ($errArr as $k => $v) {
		        unset($requestData['items']['item'][$k]);
		    }
		}
		return $this->outputQimen('success');
	}
	
	/**
	 * 返回校验的单头错误
	 * @param 方法 $method
	 * @param 请求参数 $requestData
	 * @param 返回错误 $returnMsg
	 */
	public function return_msg ($method,$requestData,$returnMsg)
	{
	    $logExt = array(
	        'api_url' => '',
	        'api_method' => $method,
	        'api_params' => $requestData,
	        'return_msg' => $returnMsg
	    );
	    return $logExt;
	}
	
	/**
	 * 
	 * @param 键名  $k
	 * @param 错误描述  $errorDescr
	 * @param 商品编码  $itemCode
	 * @param 错误数组  $error_arr
	 * @return $error_arr
	 */
	public function get_error_data($k, $errorDescr, $itemCode, $error_arr)
	{
	    $return_arr = array();
	    if (empty($error_arr[$k])) {
	        $return_arr['itemCode'] = $itemCode;
	        $return_arr['message'] = $errorDescr;
	    } else {
	        $return_arr = $error_arr[$k];
	    }
	    return $return_arr;
	}
}