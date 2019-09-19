<?php

/**
 * 订单流水通知接口过滤类
 *
 */
class filterOrderProcessReport extends msg
{

    /**
     * 订单流水通知请求数据
     * @param  $requestData         
     * @return array
     *
     */
    public function report(&$requestData)
    {
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
        $order = $requestData['order'];
        $process = $requestData['process'];
        
        //校验单据号
        if (empty($order['orderCode'])) {
            return $this->outputQimen('failure', '单据号不能为空', 'S003');
        }
        //校验单据状态
        if (empty($process['processStatus'])) {
            return $this->outputQimen('failure', '单据状态不能为空', 'S003');
        }
		//return $this->outputQimen('success');
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

        $orderNo = $requestData['order']['orderCode'];
        $customerCode = isset($_REQUEST['customerId']) ? $_REQUEST['customerId'] : $_REQUEST['customerid'];
        $warehouseCode = $requestData['order']['warehouseCode'];
        $getOrderTypeSql = "SELECT order_type FROM t_orderno_type_relation WHERE customer_id = :customer_id AND warehouse_code = :warehouse_code AND order_no = :order_no";
        $model = $db->prepare($getOrderTypeSql);
        $model->bindParam(':order_no', $orderNo);
        $model->bindParam(':customer_id', $customerCode);
        $model->bindParam(':warehouse_code', $warehouseCode);
        $model->execute();
        $orderInfo = $model->fetch(PDO::FETCH_ASSOC);
        if (!empty($orderInfo)) {
            return $orderInfo['order_type'];
        } else {
            return $this->outputQimen('failure', '找不到该订单的单据类型!', 'S003');
        }
    }
}