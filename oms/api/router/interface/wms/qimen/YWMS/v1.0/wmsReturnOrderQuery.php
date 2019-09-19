<?php
/**
 * 奇门退货单查询业务类
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsReturnOrderQuery extends wmsRequest
{
	/**
	 * 退货单查询
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
					qimen_service::$_queryFlag = true;
					
					//转换订单类型
					$xmlObj = new xml();
					$requestData = $response['addon']['api_params']['data'];//请求数据
					$requestDataArr = $xmlObj->xmlStr2array($requestData);
					$orderType = $this->getOriginOrderType($requestDataArr['ownerCode'], $requestDataArr['warehouseCode'], $requestDataArr['returnOrderCode']);
					if (is_array($orderType)) {
					    return $orderType;
					} else {
					    $orderTypeStr = "<orderType>". $orderType . "</orderType>";
					    $response['addon']['return_msg'] = preg_replace("/<orderType>(.*)<\/orderType>/s", $orderTypeStr, $response['addon']['return_msg']);
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
	 * @param 入库单编码 $returnOrderCode
	 * @return 订单类型
	 */
	public function getOriginOrderType($ownerCode,$warehouseCode,$returnOrderCode) {
	    global $db;
	    $getOrderTypeSql = "SELECT order_type FROM t_inbound_info WHERE order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
	    $model = $db->prepare($getOrderTypeSql);
	    $model->bindParam(':order_no', $returnOrderCode);
	    $model->bindParam(':customer_id', $ownerCode);
	    $model->bindParam(':warehouse_code', $warehouseCode);
	    $model->execute();
	    $orderInfo = $model->fetch(PDO::FETCH_ASSOC);
	    if (!empty($orderInfo)) {
	        return $orderInfo['order_type'];
	    } else {
	        return $this->outputQimen('failure', '该退货入库单不存在', 'S003');
	    }
	}
}