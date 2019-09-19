<?php
/**
 * 奇门出库单确认接口
 * wms => oms => ERP
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/meituan/erpRequest.php';
class erpStockOutConfirm extends erpRequest
{
	/**
	 * 推送出库单状态明细回传给ERP
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
		            //写入出库单回传数据
		            $this->insert_stock_out_confirm($params);
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
     * 出库单回传信息插入数据库
     * @param $params
     */
    public function insert_stock_out_confirm($params)
    {
        global $db;
        //单头
        $column_arr = $this->get_dataBase_relation('stock_out_confirm');
        $column_key_str = implode(',', array_values($column_arr)) . ',order_id,create_time';
        //包裹信息
        $column_package_arr = $this->get_dataBase_relation('stock_out_package_confirm');
        $column_key_package_str = implode(',', array_values($column_package_arr)) . ',order_id,record_id,create_time';
        //包裹包材明细
        $column_package_material_arr = $this->get_dataBase_relation('stock_out_package_material_confirm');
        $column_key_package_material_str = implode(',', array_values($column_package_material_arr)) . ',package_id,create_time';
        //包裹商品明细
        $column_package_product_arr = $this->get_dataBase_relation('stock_out_package_product_confirm');
        $column_key_package_product_str = implode(',', array_values($column_package_product_arr)) . ',package_id,create_time';
        //出库单确认接口明细
        $column_detail_arr = $this->get_dataBase_relation('stock_out_detail_confirm');
        $column_key_detail_str = implode(',', array_values($column_detail_arr)) . ',order_id,record_id,create_time';
        //明细批次信息
        $column_detail_batch_arr = $this->get_dataBase_relation('stock_out_detail_batch_confirm');
        $column_key_detail_batch_str = implode(',', array_values($column_detail_batch_arr)) . ',detail_id,create_time';
        
        //获取出库单表order_id
        $sql = "SELECT order_id from t_outbound_info where order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':order_no', $params['deliveryOrder']['deliveryOrderCode']);
        //$model->bindParam(':order_type', $params['deliveryOrder']['orderType']);
        $model->bindParam(':customer_id', qimen_service::$_customerId);
        $model->bindParam(':warehouse_code', $params['deliveryOrder']['warehouseCode']);
        $model->execute();
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        $orderId = $rs['order_id'];
        
        //出库单通知接口单头信息
        $column_value_str = ':' . implode(',:', array_values($column_arr)) . ",'{$orderId}',now()";
        $sql = "INSERT INTO t_outbound_info_record({$column_key_str}) VALUES($column_value_str)";
        $model = $db->prepare($sql);
        $values = array();
        foreach ($column_arr as $k => $v) {
            $values[':' . $v] = empty($params['deliveryOrder'][$k]) ? '' : $params['deliveryOrder'][$k] ;
        }
        $values[':customer_id'] = qimen_service::$_customerId;
        $model->execute($values);
        $recordId = $db->lastInsertID();
        
        //写入出库单通知包裹信息
        $column_value_package_str = ':' . implode(',:', array_values($column_package_arr)) . ",'{$orderId}','{$recordId}',now()";
        $sql = "INSERT INTO t_outbound_package_record({$column_key_package_str}) VALUES({$column_value_package_str})";
        
        if (!empty($params['packages']['package'])) {
        	if (empty($params['packages']['package'][0])) {
        		$params['packages']['package'] =  array($params['packages']['package']);
        	}
        	foreach ($params['packages']['package'] as $val)
        	{
        		//写入包裹头信息
        	    $model = $db->prepare($sql);
        		$values = array();
        		foreach ($column_package_arr as $a => $b)
        		{
        			$values[':' . $b] = empty($val[$a]) ? '' : $val[$a];
        		}
        		$model->execute($values);
        		$packageId = $db->lastInsertID();
        	    //写入包裹中包材数据
				if (!empty($val['packageMaterialList']['packageMaterial'])) {
					if (empty($val['packageMaterialList']['packageMaterial'][0])) {
						$val['packageMaterialList']['packageMaterial'] = array($val['packageMaterialList']['packageMaterial']);
					}
					//获取包裹中包材信息参数对应的字段
					$orderPackageMaterialValue = ":" . implode(",:", array_values($column_package_material_arr)) . ",'{$packageId}',now()";
					$orderPackageMaterialSql = "INSERT INTO t_outbound_package_material_record({$column_key_package_material_str}) VALUES({$orderPackageMaterialValue})";
					$model = $db->prepare($orderPackageMaterialSql);
					foreach ($val['packageMaterialList']['packageMaterial'] as $a => $b)
					{
						$values = array();
						foreach ($column_package_material_arr as $c => $d)
						{
							$values[':'.$d] = empty($b[$c]) ? '' : $b[$c];
						}						
						$model->execute($values);
					}
				}	
				//写入包裹中商品信息
				if (!empty($val['items']['item'])) {
					if (empty($val['items']['item'][0])) {
						$val['items']['item'] = array($val['items']['item']);
					}
					//获取包裹中商品信息参数对应的字段
					$orderPackageProductValue = ":" . implode(",:", array_values($column_package_product_arr)) . ",'{$packageId}',now()";
				    $orderPackageProductSql = "INSERT INTO t_outbound_package_product_record({$column_key_package_product_str}) VALUES({$orderPackageProductValue})";
					$model = $db->prepare($orderPackageProductSql);
					foreach ($val['items']['item'] as $a => $b)
					{
						$values = array();
						foreach ($column_package_product_arr as $c => $d)
						{
							$values[':'.$d] = empty($b[$c]) ? '' : $b[$c];
						}		
						$model->execute($values);
					}
				}		
        	}
        }
        
        //写入出库单确认明细信息
        if (!empty($params['orderLines']['orderLine'])) {
        	if (empty($params['orderLines']['orderLine'][0])) {
        		$params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
        	}
        	//获取明细参数对应的字段
        	$orderDetailValue = ":" . implode(",:", array_values($column_detail_arr)) . ",'{$orderId}','{$recordId}',now()";
        	$orderDetailSql = "INSERT INTO t_outbound_detail_record({$column_key_detail_str}) VALUES({$orderDetailValue})";
        	foreach ($params['orderLines']['orderLine'] as $k => $v)
        	{
        	    $model = $db->prepare($orderDetailSql);
        		$values = array();
        		foreach ($column_detail_arr as $a => $b)
        		{
        			$values[':'.$b] = empty($v[$a]) ? '' : $v[$a];
        		}
        		$values[':order_no'] = $params['deliveryOrder']['deliveryOrderCode'];
        		$model->execute($values);
        		$detailId = $db->lastInsertID();
        		//写入明细中商品批次信息
        		if (!empty($v['batchs']['batch'])) {
        			if (empty($v['batchs']['batch'][0])) {
        				$v['batchs']['batch'] = array($v['batchs']['batch']);
        			}
        			//获取明细中商品批次信息参数对应的字段
        			$orderDetailBathValue = ":" . implode(",:", array_values($column_detail_batch_arr)) . ",'{$detailId}',now()";
        			$orderDetailBathSql = "INSERT INTO t_outbound_detail_batch_record({$column_key_detail_batch_str}) VALUES({$orderDetailBathValue})";
        			$model = $db->prepare($orderDetailBathSql);
        			foreach ($v['batchs']['batch'] as $a => $b)
        			{
        				$values = array();
        				foreach ($column_detail_batch_arr as $c => $d)
        				{
        					$values[':'.$d] = empty($b[$c]) ? '' : $b[$c];
        				}
        				$model->execute($values);
        			}
        		}
        	}
        }
        //更新出库单状态
        $this->update_outbound_status($orderId, $params['deliveryOrder']['status']);
        return true;    
    }
    
    /**
     * 更新出库单状态
     * @param $orderId
     * @param $status
     * @return
     */
    public function update_outbound_status($orderId, $status)
    {
        global $db;
        $sql = "UPDATE t_outbound_info SET order_status=:order_status WHERE order_id=:order_id";
        $model = $db->prepare($sql);
        $model->bindParam(':order_status', $status);
        $model->bindParam(':order_id', $orderId);
        $model->execute();
    }
}
