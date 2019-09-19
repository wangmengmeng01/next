<?php
/**
 * 退货入库单确认接口过滤类
 * @author Renee
 *
 */
class filterReturnOrderConfirm extends msg 
{
	public function confirm(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		$request = $requestData['returnOrder'];
		$orderLine = $requestData['orderLines']['orderLine'];
		//校验退货入库单编码
		if (empty($request['returnOrderCode'])) {
		    return $this->outputQimen('failure', '退货单编码不能为空', 'S003');
		}
		//校验仓库编码
		if (empty($request['warehouseCode'])) {
		    return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		/*
		//校验发件人信息
		if (empty($request['senderInfo']['name'])) {
		    return $this->outputQimen('failure', '发件人信息中姓名不能为空', 'S003');
		}
		if (empty($request['senderInfo']['mobile'])) {
		    return $this->outputQimen('failure', '发件人信息中移动电话不能为空', 'S003');
		}
		*/
		//校验退货入库单回传明细
		if (empty($orderLine)) {
		    return $this->outputQimen('failure', '退货入库单明细不能为空', 'S003');
		} else {
		    if (empty($orderLine[0])) {
		        $orderLine = array($orderLine);
		    }
		    foreach ($orderLine as $v) {
		        if (empty($v['itemCode'])) {
		            return $this->outputQimen('failure', '退货入库单明细中商品编码不能为空', 'S003');
		        }
		        if (empty($v['actualQty'])) {
		            return $this->outputQimen('failure', '退货入库单明细中实收数量不能为空', 'S003');
		        }
		    }
		}
		
	    $orderType = $this->getOriginOrderType($requestData);
		if (is_array($orderType)) {
		    return $orderType;
		} else {
		    $orderTypeStr = "<orderType>". $orderType . "</orderType>";
		    qimen_service::$_data = preg_replace("/<orderType>(.*)<\/orderType>/s", $orderTypeStr, qimen_service::$_data);
		    
		    return $this->outputQimen('success');
		}
	}
	
	/**
	 * 获取原始订单的订单类型
	 * @param 请求数据 $requestData
	 */
	public function getOriginOrderType($requestData) {
	    global $db;
	    $getOrderTypeSql = "SELECT order_type from t_inbound_info where order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
	    $model = $db->prepare($getOrderTypeSql);
	    $model->bindParam(':order_no', $requestData['returnOrder']['returnOrderCode']);
	    $model->bindParam(':customer_id', qimen_service::$_customerId);
	    $model->bindParam(':warehouse_code', $requestData['returnOrder']['warehouseCode']);
	    $model->execute();
	    $orderInfo = $model->fetch(PDO::FETCH_ASSOC);
	    if (!empty($orderInfo)) {
	        return $orderInfo['order_type'];
	    } else {
	        return $this->outputQimen('failure', '该退货入库单不存在', 'S003');
	    }
	}
}