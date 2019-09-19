<?php
/**
 * 奇门入库单查询业务类
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsEntryOrderQuery extends wmsRequest
{
	/**
	 * 入库单查询
	 * @param $params
	 */
	public function search($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
			//转发数据给wms
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
				if ($response['flag'] == 'failure' && $response['code'] == 'E001') {
					return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
				} else {
				    if ($response['flag'] == 'failure') {
				        return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
				    }
					qimen_service::$_queryFlag = true;
					
					//转换订单类型
					$xmlObj = new xml();
					$requestData = $response['addon']['api_params']['data'];
					$requestDataArr = $xmlObj->xmlStr2array($requestData);
					$warehouseCode = '';
					if (isset($requestDataArr['warehouseCode']) && !empty($requestDataArr['warehouseCode'])) {
					    $warehouseCode = $requestDataArr['warehouseCode'];
					} 
					$orderType = $this->getOriginOrderType($requestDataArr['ownerCode'], $warehouseCode, $requestDataArr['entryOrderCode']);
					if (is_array($orderType)) {
					    return $orderType;
					} else {
					    $orderTypeStr = "<entryOrderType>". $orderType . "</entryOrderType>";
					    $response['addon']['return_msg'] = preg_replace("/<entryOrderType>(.*)<\/entryOrderType>/s", $orderTypeStr, $response['addon']['return_msg']);
					}
					
				    return $this->msgObj->outputQimen($response['flag'], $response['message'], $response['code'], $response['addon']);
				}
			} else {
				return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
			}
		}
	}
	
	/**
	 * 
	 * @param 货主编码     $ownerCode
	 * @param 仓库编码     $warehouseCode
	 * @param 入库单编码 $entryOrderCode
	 * @return 订单类型
	 */
    public function getOriginOrderType($ownerCode,$warehouseCode,$entryOrderCode) {
	    global $db;
	    
	    if ($warehouseCode != '') {
	        $condition = " AND warehouse_code=:warehouse_code";
	    }
	    $getOrderTypeSql = "SELECT order_type FROM t_inbound_info WHERE order_no=:order_no AND customer_id=:customer_id AND is_valid=1 ";
	    $getOrderTypeSql .= $condition;
	    $model = $db->prepare($getOrderTypeSql);
	    $model->bindParam(':order_no', $entryOrderCode);
	    $model->bindParam(':customer_id', $ownerCode);
	    if ($warehouseCode != '') {
	        $model->bindParam(':warehouse_code', $warehouseCode);
	    }
	    $model->execute();
	    $orderInfo = $model->fetch(PDO::FETCH_ASSOC);
	    if (!empty($orderInfo)) {
	        return $orderInfo['order_type'];
	    } else {
	        return $this->outputQimen('failure', '该入库单不存在', 'S003');
	    }
	}
}