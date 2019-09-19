<?php
/**
 * 奇门发货单确认(批量)业务处理类
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpDeliveryOrderBatchConfirm extends erpRequest
{
	/**
	 * 推送erp发货单确认信息
	 * @param $params
	 * @return array
	 */
	public function confirm($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
		    //转发数据给erp
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
			    if ($response['flag'] == 'success') {
			        foreach ($params['orders']['order'] as $p_v) {
			            $this->insertConfirmOrder($p_v);
			        }
			    } else {
			        return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
			    }
			} else {
				return $this->msgObj->outputQimen('failure', 'erp接口调用失败', 'S007');
			}
		}
	}
	
	/**
	 * 写入发货单确认数据到数据库
	 */
	public function insertConfirmOrder($params)
	{
		global $db;
		//获取发货单单头基础信息接口参数与数据库字段对应关系
		$orderInfoColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmInfo');
		$orderInfoKey = implode(',', array_values($orderInfoColumnArr)) . ',customer_id,delivery_id,create_time';		
		
		//获取发货单发票信息接口参数与数据库字段对应关系
		$orderBillColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmBill');
		$orderBillKey = implode(',', array_values($orderBillColumnArr)) . ',delivery_id,create_time';
		
		//获取发货单发票中商品信息接口参数与数据库字段对应关系
		$orderBillProductColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmBillProduct');
		$orderBillProductKey = implode(',', array_values($orderBillProductColumnArr)) . ',bill_id,create_time';
		
		//获取发货单包裹基础信息接口参数与数据库字段对应关系
		$orderPackageColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmPackage');
		$orderPackageKey = implode(',', array_values($orderPackageColumnArr)) . ',delivery_id,create_time';
		
		//获取发货单包裹中包材基础信息接口参数与数据库字段对应关系
		$orderPackageMaterialColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmPackageMaterial');
		$orderPackageMaterialKey = implode(',', array_values($orderPackageMaterialColumnArr)) . ',package_id,create_time';
		
		//获取发货单包裹中商品明细基础信息接口参数与数据库字段对应关系
		$orderPackageProductColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmPackageProduct');
		$orderPackageProductKey = implode(',', array_values($orderPackageProductColumnArr)) . ',package_id,create_time';
		
		//获取发货明细基础信息接口参数与数据库字段对应关系
		$orderDetailColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmDetail');
		$orderDetailKey = implode(',', array_values($orderDetailColumnArr)) . ',delivery_id,create_time';	

		//获取发货明细中批次基础信息接口参数与数据库字段对应关系
		$orderDetailBathColumnArr = $this->get_dataBase_relation('DeliveryOrderConfirmDetailBath');
		$orderDetailBathKey = implode(',', array_values($orderDetailBathColumnArr)) . ',detail_id,create_time';

		//查询出deliveryId
		$sql = "SELECT delivery_id FROM t_delivery_order_info WHERE customer_id = :customer_id AND warehouse_code = :warehouse_code AND delivery_order_code = :delivery_order_code";
		$model = $db->prepare($sql);
		$values = array();
		$values[':customer_id'] = qimen_service::$_customerId;
		$values[':warehouse_code'] = $params['deliveryOrder']['warehouseCode'];
		//$values[':order_type'] = $params['deliveryOrder']['orderType'];
		$values[':delivery_order_code'] = $params['deliveryOrder']['deliveryOrderCode'];
		$model->execute($values);
		$rs = $model->fetch(PDO::FETCH_ASSOC);
		$deliveryId = $rs['delivery_id'];
		
		if ($deliveryId != '') {
		    //写入发货单单头信息
		    $orderInfoValue = ":" . implode(",:", array_keys($orderInfoColumnArr)) . ",:customer_id,:delivery_id,now()";
		    $orderInfoSql = "INSERT INTO t_delivery_order_info_record({$orderInfoKey}) VALUES({$orderInfoValue})";
		    $model = $db->prepare($orderInfoSql);
		    $values = array();
		    foreach ($orderInfoColumnArr as $k => $v)
		    {
		        $values[':'.$k] = empty($params['deliveryOrder'][$k]) ? '' : $params['deliveryOrder'][$k];
		    }
		    $values[':customer_id'] = qimen_service::$_customerId;
		    $values[':delivery_id'] = $deliveryId;
		    $model->execute($values);
		    
		    //写入发货单发票信息
		    if (!empty($params['deliveryOrder']['invoices']['invoice'])) {
		        if (empty($params['deliveryOrder']['invoices']['invoice'][0])) {
		            $params['deliveryOrder']['invoices']['invoice'] = array($params['deliveryOrder']['invoices']['invoice']);
		        }
		        //获取发票信息参数对应的字段
		        $orderBillValue = ":" . implode(",:", array_keys($orderBillColumnArr)) . ",'{$deliveryId}',now()";
		        $orderBillSql = "INSERT INTO t_delivery_order_bill_info_record({$orderBillKey}) VALUES({$orderBillValue})";
		        foreach ($params['deliveryOrder']['invoices']['invoice'] as $k => $v)
		        {
		            $model = $db->prepare($orderBillSql);
		            $values = array();
		            foreach ($orderBillColumnArr as $a => $b)
		            {
		                $values[':'.$a] = empty($v[$a]) ? '' : $v[$a];
		            }
		            $model->execute($values);
		            $billId = $db->lastInsertID();
		            
		            if (!empty($v['detail']['items']['item'])) {
		                if (empty($v['detail']['items']['item'][0])) {
		                    $v['detail']['items']['item'] = array($v['detail']['items']['item']);
		                }
		                //获取发票信息中的商品参数对应的字段
		                $orderBillProductValue = ":" . implode(",:", array_keys($orderBillProductColumnArr)) . ",'{$billId}',now()";
		                $orderBillProductSql = "INSERT INTO t_delivery_order_bill_product_detail_record({$orderBillProductKey}) VALUES({$orderBillProductValue})";
		                $model = $db->prepare($orderBillProductSql);
		                foreach ($v['detail']['items']['item'] as $a => $b)
		                {
		                    $values = array();
		                    foreach ($orderBillProductColumnArr as $c => $d)
		                    {
		                        $values[':'.$c] = empty($b[$c]) ? '' : $b[$c];
		                    }
		                    $model->execute($values);
		                }
		            }
		        }
		    }
		    
		    //写入包裹信息
		    if (!empty($params['packages']['package'])) {
		        if (empty($params['packages']['package'][0])) {
		            $params['packages']['package'] = array($params['packages']['package']);
		        }
		        //获取包裹信息参数对应的字段
		        $orderPackageValue = ":" . implode(",:", array_keys($orderPackageColumnArr)) . ",'{$deliveryId}',now()";
		        $orderPackageSql = "INSERT INTO t_delivery_package_record({$orderPackageKey}) VALUES({$orderPackageValue})";
		        foreach ($params['packages']['package'] as $k => $v)
		        {
		            $model = $db->prepare($orderPackageSql);
		            $values = array();
		            foreach ($orderPackageColumnArr as $a => $b)
		            {
		                $values[':'.$a] = empty($v[$a]) ? '' : $v[$a];
		            }
		            $model->execute($values);
		            $packageId = $db->lastInsertID();
		            //写入包裹中包材数据
		            if (!empty($v['packageMaterialList']['packageMaterial'])) {
		                if (empty($v['packageMaterialList']['packageMaterial'][0])) {
		                    $v['packageMaterialList']['packageMaterial'] = array($v['packageMaterialList']['packageMaterial']);
		                }
		                //获取包裹中包材信息参数对应的字段
		                $orderPackageMaterialValue = ":" . implode(",:", array_keys($orderPackageMaterialColumnArr)) . ",'{$packageId}',now()";
		                $orderPackageMaterialSql = "INSERT INTO t_delivery_package_material_record({$orderPackageMaterialKey}) VALUES({$orderPackageMaterialValue})";
		                $model = $db->prepare($orderPackageMaterialSql);
		                foreach ($v['packageMaterialList']['packageMaterial'] as $a => $b)
		                {
		                    $values = array();
		                    foreach ($orderPackageMaterialColumnArr as $c => $d)
		                    {
		                        $values[':'.$c] = empty($b[$c]) ? '' : $b[$c];
		                    }
		                    $model->execute($values);
		                }
		            }
		            //写入包裹中商品信息
		            if (!empty($v['items']['item'])) {
		                if (empty($v['items']['item'][0])) {
		                    $v['items']['item'] = array($v['items']['item']);
		                }
		                //获取包裹中商品信息参数对应的字段
		                $orderPackageProductValue = ":" . implode(",:", array_keys($orderPackageProductColumnArr)) . ",'{$packageId}',now()";
		                $orderPackageProductSql = "INSERT INTO t_delivery_package_product_record({$orderPackageProductKey}) VALUES({$orderPackageProductValue})";
		                $model = $db->prepare($orderPackageProductSql);
		                foreach ($v['items']['item'] as $a => $b)
		                {
		                    $values = array();
		                    foreach ($orderPackageProductColumnArr as $c => $d)
		                    {
		                        $values[':'.$c] = empty($b[$c]) ? '' : $b[$c];
		                    }
		                    $model->execute($values);
		                }
		            }
		        }
		    }
		    
		    //写入发货单确认明细信息
		    if (!empty($params['orderLines']['orderLine'])) {
		        if (empty($params['orderLines']['orderLine'][0])) {
		            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
		        }
		        //获取明细参数对应的字段
		        $orderDetailValue = ":" . implode(",:", array_keys($orderDetailColumnArr)) . ",'{$deliveryId}',now()";
		        $orderDetailSql = "INSERT INTO t_delivery_order_detail_record({$orderDetailKey}) VALUES({$orderDetailValue})";
		        foreach ($params['orderLines']['orderLine'] as $k => $v)
		        {
		            $model = $db->prepare($orderDetailSql);
		            $values = array();
		            foreach ($orderDetailColumnArr as $a => $b)
		            {
		                $values[':'.$a] = empty($v[$a]) ? '' : $v[$a];
		            }
		            $model->execute($values);
		            $detailId = $db->lastInsertID();
		            //写入明细中商品批次信息
		            if (!empty($v['batchs']['batch'])) {
		                if (empty($v['batchs']['batch'][0])) {
		                    $v['batchs']['batch'] = array($v['batchs']['batch']);
		                }
		                //获取明细中商品批次信息参数对应的字段
		                $orderDetailBathValue = ":" . implode(",:", array_keys($orderDetailBathColumnArr)) . ",'{$detailId}',now()";
		                $orderDetailBathSql = "INSERT INTO t_delivery_order_detail_batch_record({$orderDetailBathKey}) VALUES({$orderDetailBathValue})";
		                $model = $db->prepare($orderDetailBathSql);
		                foreach ($v['batchs']['batch'] as $a => $b)
		                {
		                    $values = array();
		                    foreach ($orderDetailBathColumnArr as $c => $d)
		                    {
		                        $values[':'.$c] = empty($b[$c]) ? '' : $b[$c];
		                    }
		                    $model->execute($values);
		                }
		            }
		        }
		    }
		    return true;
		} else {
		    return false;
		}
	}
}