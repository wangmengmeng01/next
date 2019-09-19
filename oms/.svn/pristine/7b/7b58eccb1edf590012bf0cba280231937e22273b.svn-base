<?php
/**
 * 奇门发货单确认接口过滤类
 */
class filterDeliveryOrderConfirm extends msg
{
	public function confirm(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		//校验出库单号
		if (empty($requestData['deliveryOrder']['deliveryOrderCode'])) {
			return $this->outputQimen('failure', '出库单号不能为空', 'S003');
		}
		//校验仓库编码
		if (empty($requestData['deliveryOrder']['warehouseCode'])) {
			return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		//校验出库单类型
		if (empty($requestData['deliveryOrder']['orderType'])) {
			return $this->outputQimen('failure', '出库单类型不能为空', 'S003');
		}
		//校验包裹中的信息
		if (empty($requestData['packages']['package'])) {
			return $this->outputQimen('failure', '包裹信息不能为空', 'S003');
		} else {
			if (empty($requestData['packages']['package'][0])) {
				$requestData['packages']['package'] = array($requestData['packages']['package']);
			}
			foreach ($requestData['packages']['package'] as $val)
			{
				//校验物流公司编码
				if (empty($val['logisticsCode'])) {
					return $this->outputQimen('failure', '物流公司编码不能为空', 'S003');
				}
				//校验运单号
				if (empty($val['expressCode'])) {
					return $this->outputQimen('failure', '运单号不能为空', 'S003');
				}
				//校验商品信息
				if (empty($val['items']['item'])) {
					return $this->outputQimen('failure', '包裹中商品信息不能为空', 'S003');
				} else {
					if (empty($val['items']['item'][0])) {
						$val['items']['item'] = array($val['items']['item']);
					}
					foreach ($val['items']['item'] as $v)
					{
						//校验商品编码
					    if (empty($v['itemCode'])) {
					    	return $this->outputQimen('failure', '包裹商品信息中商品编码不能为空', 'S003');
					    } 
					    //校验包裹内该商品的数量
					    if (empty($v['quantity']) && $v['quantity'] !=0) {
					    	return $this->outputQimen('failure', '包裹内商品的数量不能为空', 'S003');
					    } elseif (!preg_match("/^\d+$/", $v['quantity'])) {
					    	return $this->outputQimen('failure', '包裹内商品的数量必须为整数', 'S003');
					    }
					}
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
	    
	    if (strpos($requestData['deliveryOrder']['deliveryOrderCode'], ",")) {
	        $deliveryOrderCodeInfo = explode(',', $requestData['deliveryOrder']['deliveryOrderCode']);
	        $deliveryOrderCode = $deliveryOrderCodeInfo[0];
	    } else {
	        $deliveryOrderCode = $requestData['deliveryOrder']['deliveryOrderCode'];
	    }
	    
	    $customerCode = isset($_REQUEST['customerId']) ? $_REQUEST['customerId'] : $_REQUEST['customerid'];
	    $getOrderTypeSql = "SELECT order_type FROM t_delivery_order_info WHERE customer_id = :customer_id AND warehouse_code = :warehouse_code AND delivery_order_code = :delivery_order_code";
	    $model = $db->prepare($getOrderTypeSql);
	    $model->bindParam(':delivery_order_code', $deliveryOrderCode);
	    $model->bindParam(':customer_id', $customerCode);
	    $model->bindParam(':warehouse_code', $requestData['deliveryOrder']['warehouseCode']);
	    $model->execute();
	    $orderInfo = $model->fetch(PDO::FETCH_ASSOC);
	    if (!empty($orderInfo)) {
	        return $orderInfo['order_type'];
	    } else {
	        return $this->outputQimen('failure', '该发货单不存在', 'S003');
	    }
	}
}
