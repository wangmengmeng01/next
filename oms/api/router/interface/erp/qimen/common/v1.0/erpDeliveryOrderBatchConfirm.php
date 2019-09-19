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
			    $xmlObj = new xml();
			    $responseArr = $xmlObj->xmlStr2array($response['addon']['return_msg']);//将YWMS返回的xml转化为数组
			    if (!empty($responseArr['orders']['order']) && empty($responseArr['orders']['order'][0])) {
		            $responseArr['orders']['order'] = array($responseArr['orders']['order']);
			    }
				if ($response['flag'] == 'success') {
				    //将发货单信息写入数据库
				    foreach ($params['orders']['order'] as $v) {
				        //判断该发货单是否存在
				        $this->insertConfirmOrder($v);
				    }
				    //判断校验时有无错误
				    if (!empty(msg::$err_arr)) {//校验有错误
				        qimen_service::$_errorMsg = msg::$err_arr;
				        $responseArr['flag'] = 'failure';
				        $responseArr['code'] = '0001';
				        $responseArr['message'] = '失败';
				    
				        $orderStr = '<orders>';
				        foreach (msg::$err_arr as $key=>$val) {
				            $orderStr .= '<order>';
				            $orderStr .= $xmlObj->array2xml($val);
				            $orderStr .= '</order>';
				        }
				        $orderStr .= '</orders>';
				        $response['addon']['return_msg'] = '<?xml version="1.0" encoding="utf-8"?><response>' . $xmlObj->array2xml($responseArr) . $orderStr . '</response>';
				        return $this->msgObj->outputQimen('failure', '失败', '0001', $response['addon'], $response['addon']['return_msg']);
				    } else {//全部成功
				        return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
				    }
				} else {
				    //失败的其他情况,直接将message返回
				    if (!empty($responseArr['orders']['order'])) {
				        //找出成功的发货单
				        foreach ($params['orders']['order'] as $p_k=>$p_v) {
				            foreach ($responseArr['orders']['order'] as $key=>$val) {
				                if ($p_v['deliveryOrder']['deliveryOrderCode'] == $val['deliveryOrderCode']) {
				                    unset($params['orders']['order'][$p_k]);
				                }
				            }
				        }
				         
				        //将成功的数据写入数据库
				        if (!empty($params['orders']['order'])) {
				            foreach ($params['orders']['order'] as $i_v) {
				                if ($this->checkDeliveryOrder($i_v)) {
				                    $this->insertDeliveryOrder($i_v);
				                }
				            }
				        }
				        //添加校验时的错误
				        if (!empty(msg::$err_arr)) {
				            qimen_service::$_errorMsg = msg::$err_arr;
				            if (!empty($responseArr['orders']['order'])) {
				                //取出YWMS返回错误数组的最大键值
				                $keys =  array_keys($responseArr['orders']['order'], max($responseArr['orders']['order']));
				            }
				            //拼接到返回的数组中
				            $key = $keys[0]+1;
				            foreach (msg::$err_arr as $v) {
				                $responseArr['orders']['order'][$key] = $v;
				                $key++;
				            }
				        }
				        //将返回的数组转化为xml
				        $orders = $responseArr['orders']['order'];
				        unset($responseArr['orders']);
				        $ordersXml = '<orders>';
				        foreach ($orders as $b) {
				            $ordersXml .= '<order>' . $xmlObj->array2xml($b) . '</order>';
				        }
				        $ordersXml .= '</orders>';
				        $response['addon']['return_msg'] = '<?xml version="1.0" encoding="utf-8"?><response>' . $xmlObj->array2xml($responseArr) . $ordersXml . '</response>';
				    }
				    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon'], $response['addon']['return_msg']);
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