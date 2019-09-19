<?php
/**
 * 入库单确认接口过滤类
 * @author Renee
 *
 */
class filterEntryOrderConfirm extends msg 
{

	/**
	 * 入库单确认接口请求信息校验
	 * @param $requestData
	 * @return xml
	 */
	public function confirm(&$requestData)
	{
	    //连接数据库
	    global $db;
	    //校验数据是否为空
	    if (empty($requestData)) {
	        return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
	    }
		
		//校验入库单编码
		if (empty($requestData['entryOrder']['entryOrderCode'])) {
		    return $this->outputQimen('failure', '入库单编码不能为空', 'S003');
		} /*else {
		    $sql = "SELECT order_no FROM t_inbound_info WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
		    $model = $db->prepare($sql);
		    $model->bindParam(':order_no', $request['entryOrderCode']);
		    $model->bindParam(':order_type', $request['entryOrderType']);
		    $model->bindParam(':customer_id', $request['ownerCode']);
		    $model->bindParam(':warehouse_code', $request['warehouseCode']);
		    $model->execute();
		    $rs = $model->fetch(PDO::FETCH_ASSOC);
		    if (empty($rs)) {
		        return $this->outputQimen('failure', '该入库单不存在或者无效', 'S003');
		    }
		}
		*/
		
		//校验仓库编码
		if (empty($requestData['entryOrder']['warehouseCode'])) {
		    return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		//校验外部业务编码
		if (empty($requestData['entryOrder']['outBizCode'])) {
		    return $this->outputQimen('failure', '外部业务编码不能为空', 'S003');
		}
		//校验入库单状态
		if (empty($requestData['entryOrder']['status'])) {
		    return $this->outputQimen('failure', '入库单状态不能为空', 'S003');
		}
		//校验入库单回传明细
		if (empty($requestData['orderLines']['orderLine'])) {
		    return $this->outputQimen('failure', '入库单明细不能为空', 'S003');
		} else {
		    if (empty($requestData['orderLines']['orderLine'][0])) {
		        $requestData['orderLines']['orderLine'] = array($requestData['orderLines']['orderLine']);
		    }
		    foreach ($requestData['orderLines']['orderLine'] as $v) {
		        if (empty($v['itemCode'])) {
		            return $this->outputQimen('failure', '入库单明细中商品编码不能为空', 'S003');
		        }
		    }
		}
	    $orderType = $this->getOriginOrderType($requestData);
	    if (is_array($orderType)) {
	        return $orderType;
	    } else {
	        $orderTypeStr = "<entryOrderType>". $orderType . "</entryOrderType>";
	        qimen_service::$_data = preg_replace("/<entryOrderType>(.*)<\/entryOrderType>/s", $orderTypeStr, qimen_service::$_data);
	        
	        return $this->outputQimen('success');
	    }
	}
	
	/**
	 * 获取原始订单的订单类型
	 * @param 请求数据 $requestData
	 */
	public function getOriginOrderType($requestData) {
	    global $db;
	    if (!empty($requestData['entryOrder']['ownerCode'])) {
	        $customerId = $requestData['entryOrder']['ownerCode'];
	    } else {
	        $customerId = qimen_service::$_customerId;
	    }
	    $getOrderTypeSql = "SELECT order_type FROM t_inbound_info WHERE order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
	    $model = $db->prepare($getOrderTypeSql);
	    $model->bindParam(':order_no', $requestData['entryOrder']['entryOrderCode']);
	    $model->bindParam(':customer_id', $customerId);
	    $model->bindParam(':warehouse_code', $requestData['entryOrder']['warehouseCode']);
	    $model->execute();
	    $orderInfo = $model->fetch(PDO::FETCH_ASSOC);
	    if (!empty($orderInfo)) {
	        return $orderInfo['order_type'];
	    } else {
	        return $this->outputQimen('failure', '该入库单不存在', 'S003');
	    }
	}
}