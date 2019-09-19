<?php

/**
 * 奇门出库单确认接口过滤类
 *
 */
class filterStockOutConfirm extends msg
{
    /**
     * 过滤出库单确认接口请求数据
     * @param  $requestData         
     * @return array
     *
     */
    public function confirm(&$requestData)
    {
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
        $data = $requestData;
        $request = $data['deliveryOrder'];
        $packages= $data['packages']['package'];
        $orderLines = $data['orderLines']['orderLine'];
        
        //校验出库单号
        if (empty($request['deliveryOrderCode'])) {
            return $this->outputQimen('failure', '出库单号不能为空', 'S003');
        }
        
        //校验仓库
        if (empty($request['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        
        //校验出库单类型
        if (empty($request['orderType'])) {
            return $this->outputQimen('failure', '出库单类型不能为空', 'S003');
        }
        
        //校验包裹明细
        if (empty($packages)) {
            return $this->outputQimen('failure', '出库单包裹信息不能为空', 'S003');
        } else {
            if (empty($packages[0])) {
                $packages = array($packages);
            }
            foreach ($packages as $p_v) {
                if (empty($p_v['items']['item'])) {
                    return $this->outputQimen('failure', '出库单包裹明细不能为空', 'S003');
                } else {
                    if (empty($p_v['items']['item'][0])) {
                        $p_v['items']['item'] = array($p_v['items']['item']);
                    }
                    foreach ($p_v['items']['item'] as $val) {
                        if (empty($val['itemCode'])) {
                            return $this->outputQimen('failure', '出库单包裹明细的商品明细中商品编码不能为空', 'S003');
                        }
                        if (empty($val['quantity'])) {
                            return $this->outputQimen('failure', '出库单包裹明细的商品明细中包裹内商品数量不能为空', 'S003');
                        }
                    }
                }
            }
        }
        
        //校验出库单明细
        if (empty($orderLines)) {
            return $this->outputQimen('failure', '出库单明细不能为空', 'S003');
        } else {
        	if (empty($orderLines[0])) {
        		$orderLines = array($orderLines);
        	}
            foreach ($orderLines as $val) {
                if (empty($val['itemCode'])) {
                    return $this->outputQimen('failure', '出库单明细中商品编码不能为空', 'S003');
                } 
                if (empty($val['actualQty'])) {
                    return $this->outputQimen('failure', '出库单明细中实发商品数量不能为空', 'S003');
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
        $getOrderTypeSql = "SELECT order_type from t_outbound_info where order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
        $model = $db->prepare($getOrderTypeSql);
        $model->bindParam(':order_no', $requestData['deliveryOrder']['deliveryOrderCode']);
        $model->bindParam(':customer_id', qimen_service::$_customerCode);
        $model->bindParam(':warehouse_code', $requestData['deliveryOrder']['warehouseCode']);
        $model->execute();
        $orderInfo = $model->fetch(PDO::FETCH_ASSOC);
        if (!empty($orderInfo)) {
            return $orderInfo['order_type'];
        } else {
            return $this->outputQimen('failure', '该出库单不存在', 'S003');
        }
    }
}