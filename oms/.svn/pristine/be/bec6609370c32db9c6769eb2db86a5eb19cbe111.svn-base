<?php
/**
 * 奇门发货单创建接口业务处理类
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsDeliveryOrderCreate extends wmsRequest
{
	/**
	 * 创建发货单
	 * @param $params
	 * @return array
	 */
	public function create($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
			//转发数据给wms
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
				if ($response['flag'] == 'success') {
					//校验发货单是否存在，如果存在则状态置为无效
					$this->checkDeliveryOrder($params);
					//写入数据库
					$this->insertDeliveryOrder($params);
					//返回
					return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
				} else {
					return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
				}
			} else {
				return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
			}
		}
	}

    /**
     * 更新订单号和订单类型关系数据
     * @param $params
     */
    public function updateOrderNoTypeRelation($params){
        global $db;

        if (empty($params['orderLines']['orderLine'][0])) {
            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
        }
        $customerCode = !empty($params['orderLines']['orderLine'][0]['ownerCode']) ? $params['orderLines']['orderLine'][0]['ownerCode'] : qimen_service::$_customerId;

        $sql = "UPDATE t_orderno_type_relation 
                SET order_type=:order_type 
                WHERE order_no=:order_no 
                  AND customer_id=:customer_id 
                  AND warehouse_code=:warehouse_code";
        $model = $db->prepare($sql);
        $model->bindParam(':order_no',$params['deliveryOrder']['deliveryOrderCode']);
        $model->bindParam(':order_type',$params['deliveryOrder']['orderType']);
        $model->bindParam(':customer_id',$customerCode);
        $model->bindParam(':warehouse_code',$params['deliveryOrder']['warehouseCode']);
        $model->execute();
    }

    /**
     * 写入订单号和订单类型关系数据
     * @param $params
     */
    public function insertOrderNoTypeRelation($params){
        global $db;

        if (empty($params['orderLines']['orderLine'][0])) {
            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
        }
        $customerCode = !empty($params['orderLines']['orderLine'][0]['ownerCode']) ? $params['orderLines']['orderLine'][0]['ownerCode'] : qimen_service::$_customerId;

        $sql = "INSERT INTO t_orderno_type_relation(order_no,order_type,customer_id,warehouse_code,create_time) 
                VALUES(:order_no,:order_type,:customer_id,:warehouse_code,now())";
        $model = $db->prepare($sql);
        $model->bindParam(':order_no',$params['deliveryOrder']['deliveryOrderCode']);
        $model->bindParam(':order_type',$params['deliveryOrder']['orderType']);
        $model->bindParam(':customer_id',$customerCode);
        $model->bindParam(':warehouse_code',$params['deliveryOrder']['warehouseCode']);
        $model->execute();
    }

	/**
	 * 校验发货单号是否存在
	 * @parameter $params
	 * @return boolean
	 */
	public function checkDeliveryOrder($params)
	{
		//连接数据库
		global $db;
		//$sql = 'SELECT delivery_id FROM t_delivery_order_info WHERE delivery_order_code=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
        $sql = 'SELECT delivery_id FROM t_delivery_order_info WHERE delivery_order_code=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
		$model = $db->prepare($sql);
		$model->bindParam(':order_no', $params['deliveryOrder']['deliveryOrderCode']);
		//$model->bindParam(':order_type', $params['deliveryOrder']['orderType']);
		$customerId = empty($params['orderLines']['orderLine']['ownerCode']) ? (empty($params['orderLines']['orderLine'][0]['ownerCode']) ? qimen_service::$_customerId : $params['orderLines']['orderLine'][0]['ownerCode']) : $$params['orderLines']['orderLine']['ownerCode'] ;
		$model->bindParam(':customer_id', $customerId);
		$model->bindParam(':warehouse_code', $params['deliveryOrder']['warehouseCode']);
		$model->execute();
        
		$rs = $model->fetch(PDO::FETCH_ASSOC);
		if (!empty($rs)) {
		    $this->updateOrderNoTypeRelation($params);

			//更新原发货单有效性
			$this->updateDeliveryOrder($rs['delivery_id']);
		} else {
		    $this->insertOrderNoTypeRelation($params);
        }
		return true;
	}
	
	/**
	 * 更新发货单有效性
	 * @param $deliveryId
	 * @return boolean
	 */
	public function updateDeliveryOrder($deliveryId)
	{
		//连接数据库
		global $db;
		$sql = 'UPDATE t_delivery_order_info SET is_valid=0 WHERE delivery_id=:delivery_id';
		$model = $db->prepare($sql);
		$model->bindParam(':delivery_id', $deliveryId);
		$model->execute();
        return true;
	}
	
	/**
	 * 写入发货单到数据库
	 * @param $params
	 * @return boolean
	 */
	public function insertDeliveryOrder($params)
	{
		//连接数据库
		global $db;
		//获取发货单单头基础信息接口参数与数据库字段对应关系
		$orderInfoColumnArr = $this->get_dataBase_relation('DeliveryOrderCreateInfo');
		$tempInfoColumnArr = $orderInfoColumnArr;
		unset($tempInfoColumnArr['deliveryRequirements']);
		unset($tempInfoColumnArr['senderInfo']);
		unset($tempInfoColumnArr['receiverInfo']);
		unset($tempInfoColumnArr['insurance']);
		$orderInfoKey = implode(',', array_values($tempInfoColumnArr)) . ',' . implode(',', array_values($orderInfoColumnArr['deliveryRequirements'])) . ',' . implode(',', array_values($orderInfoColumnArr['senderInfo'])) . ',' . implode(',', array_values($orderInfoColumnArr['receiverInfo'])) . ',' . implode(',', array_values($orderInfoColumnArr['insurance'])) . ',customer_id,create_time';
		 
		//获取发货明细基础信息接口参数与数据库字段对应关系
		$orderDetailColumnArr = $this->get_dataBase_relation('DeliveryOrderCreateDetail');
		$orderDetailKey = implode(',', array_values($orderDetailColumnArr)) . ',delivery_id,create_time';		
		
		//获取发货单发票信息接口参数与数据库字段对应关系
		$orderBillColumnArr = $this->get_dataBase_relation('DeliveryOrderCreateBill');
		$orderBillKey = implode(',', array_values($orderBillColumnArr)) . ',delivery_id,create_time';
				
		//获取发货单发票中商品信息接口参数与数据库字段对应关系
		$orderBillProductColumnArr = $this->get_dataBase_relation('DeliveryOrderCreateBillProduct');
		$orderBillProductKey = implode(',', array_values($orderBillProductColumnArr)) . ',bill_id,create_time';
		
		//写入发货单单头信息
		$orderInfoValue = ":" . implode(",:", array_keys($tempInfoColumnArr)) . ',:' . implode(",:", array_keys($orderInfoColumnArr['deliveryRequirements'])) . ',:' . implode(",:", array_keys($orderInfoColumnArr['senderInfo'])) . ',:' . implode(",:", array_keys($orderInfoColumnArr['receiverInfo'])) . ',:' . implode(",:", array_keys($orderInfoColumnArr['insurance'])) . ",:customer_id,now()";
		$orderInfoSql = "INSERT INTO t_delivery_order_info({$orderInfoKey}) VALUES({$orderInfoValue})";
		
		$model = $db->prepare($orderInfoSql);
		$values = array();		
		foreach ($orderInfoColumnArr as $k => $v)
		{
			if (in_array($k, array('deliveryRequirements', 'senderInfo', 'receiverInfo', 'insurance'))) {
				foreach ($v as $a => $b)
				{
					$values[':'.$a] = empty($params['deliveryOrder'][$k][$a]) ? '' : $params['deliveryOrder'][$k][$a];
				}
			}  else {
			    $values[':'.$k] = empty($params['deliveryOrder'][$k]) ? '' : $params['deliveryOrder'][$k];
			}
		}
		if (!empty($params['orderLines']['orderLine']) && empty($params['orderLines']['orderLine'][0])) {
		    $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
		}
		$values[':customer_id'] = $values[':customer_id'] = empty($params['orderLines']['orderLine'][0]['ownerCode']) ? (empty($params['orderLines']['orderLine']['ownerCode']) ? '': $params['orderLines']['orderLine']['ownerCode']): $params['orderLines']['orderLine'][0]['ownerCode'];
		$model->execute($values);
		$deliveryId = $db->lastInsertID();
		
		//写入发货单明细信息
		$orderDetailValue = ":" . implode(",:", array_keys($orderDetailColumnArr)) . ",'{$deliveryId}',now()";
		$orderDetailSql = "INSERT IGNORE INTO t_delivery_order_detail({$orderDetailKey}) VALUES({$orderDetailValue})";
		if (!empty($params['orderLines']['orderLine'])) {
			if (empty($params['orderLines']['orderLine'][0])) {
				$params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
			}
			$model = $db->prepare($orderDetailSql);			
			foreach ($params['orderLines']['orderLine'] as $a => $b)
			{
				$values = array();
				foreach ($orderDetailColumnArr as $k => $v)
				{
					$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
				}						
				$model->execute($values);
			}
		}
		
		//写入发货单发票数据
		$orderBillValue = ":" . implode(",:", array_keys($orderBillColumnArr)) . ",'{$deliveryId}',now()";
		$orderBillSql = "INSERT IGNORE INTO t_delivery_order_bill_info({$orderBillKey}) VALUES({$orderBillValue})";
		if (!empty($params['deliveryOrder']['invoices']['invoice'])) {
			if (empty($params['deliveryOrder']['invoices']['invoice'][0])) {
				$params['deliveryOrder']['invoices']['invoice'] = array($params['deliveryOrder']['invoices']['invoice']);
			}
			foreach ($params['deliveryOrder']['invoices']['invoice'] as $a => $b)
			{
			    $model = $db->prepare($orderBillSql);
				$values = array();
				foreach ($orderBillColumnArr as $k => $v)
				{
					$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
				}
				$model->execute($values);
				$billId = $db->lastInsertID();
				//写入发货单发票中商品数据
				if (!empty($b['detail']['items']['item'])) {
					if (empty($b['detail']['items']['item'][0])) {
						$b['detail']['items']['item'] = array($b['detail']['items']['item']);
					}
					$orderBillProductValue = ":" . implode(",:", array_keys($orderBillProductColumnArr)) . ",'{$billId}',now()";
					$orderBillProductSql = "INSERT IGNORE INTO t_delivery_order_bill_product_detail({$orderBillProductKey}) VALUES({$orderBillProductValue})";
					$model = $db->prepare($orderBillProductSql);
					foreach ($b['detail']['items']['item'] as $d)
					{
						$values = array();
						foreach ($orderBillProductColumnArr as $k => $v)
						{
							$values[':'.$k] = empty($d[$k]) ? '' : $d[$k];
						}
						$model->execute($values);
					}
				}
			}
		}		
		return true;
	}
	
}