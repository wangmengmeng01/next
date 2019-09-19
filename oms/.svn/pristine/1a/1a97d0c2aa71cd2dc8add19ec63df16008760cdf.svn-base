<?php
/**
 * 奇门出库单确认接口
 * wms => oms => ERP
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/fx/erpRequest.php';
class erpStockOutConfirm extends erpRequest
{
    public $is_depositpay = 0;
    public $fx_customer = NULL;

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
    	    try {
                //转发数据给erp
                $response = $this->send();
                //解析返回的数据
                if (!empty($response)) {
                    if ($response['flag'] == 'success') {
                        //写入出库单回传数据
                        $this->insert_stock_out_confirm($params);
                        //更新发货订单表中的订单状态
                        $this->updateOrderInfo($params);

                        //判断是否网点下单并且押金支付
                        if (($this->is_depositpay == 1) && ($this->fx_customer == 'branch')) {
                            //下发入库单
                            $this->postEntryOrder($params);
                        }
                        return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                    } else {
                        return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                    }
                } else {
                    return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
                }
            } catch (Exception $e) {
                return $this->msgObj->outputQimen('failure', '失败:'. $e->getMessage(), 'S007');
            }
    	}
    }

    /**
     * 下发入库单
     * @param $params
     */
    public function postEntryOrder($params)
    {
        //通过网点编码找到仓库编码(网点下单，总部发货【订单中的网点编码就是仓库编码】)
        $branchCode = $params['deliveryOrder']['branchCode'];
        //入库单号
        $orderCode = 'fx' . $params['deliveryOrder']['deliveryOrderCode'];

        $postData = '<?xml version="1.0" encoding="utf-8"?>';
        $postData .= '<request><entryOrder>';
        $postData .= '<entryOrderCode>' . $orderCode . '</entryOrderCode>';
        $postData .= '<ownerCode>' . qimen_service::$_customerId . '</ownerCode>';
        $postData .= '<warehouseCode>' . $branchCode . '</warehouseCode>';
        $postData .= '<orderType>CGRK</orderType>';
        $postData .= '</entryOrder>';
        $postData .= '<orderLines>';

        if (empty($params['orderLines']['orderLine'][0])) {
            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
        }
        foreach ($params['orderLines']['orderLine'] as $item) {
            $postData .= '<orderLine>';
            $postData .= '<ownerCode>'.qimen_service::$_customerId.'</ownerCode>';
            $postData .= '<itemCode>'.$item['itemCode'].'</itemCode>';
            $postData .= '<planQty>'.$item['actualQty'].'</planQty>';
            $postData .= '</orderLine>';
        }
        $postData .= '</orderLines>';
        $postData .= '</request>';

        $apiParams = array(
            'method' => 'entryorder.create',
            'timestamp' => date("Y-m-d H:i:s"),
            'format' => 'xml',
            'app_key' => SRD_APP_KEY,
            'v' => '1.0',
            'sign_method' => 'md5',
            'customerId' => qimen_service::$_customerId
        );
        $sign = $this->utilObj->makeQmSign($apiParams,SRD_APP_SECRET,$postData);
        $apiParams['sign'] = $sign;

        $urlParamStr = http_build_query($apiParams);
        $url = OMS_API_URL . '?' . $urlParamStr;
        $entryRs = $this->utilObj->post_data($url,$postData,60);

        if (empty($entryRs)) {
            $this->insertErrInfo($orderCode,$branchCode,qimen_service::$_customerId,$params['deliveryOrder']['deliveryOrderCode'],'请求超时！');
        } else {
            $xmlObj = new xml();
            $entryRsArr = $xmlObj->xmlStr2array($entryRs);
            if ($entryRsArr['flag'] != 'success') {
                $this->insertErrInfo($orderCode,$branchCode,qimen_service::$_customerId,$params['deliveryOrder']['deliveryOrderCode'],$entryRsArr['message']);
            }
        }
    }

    /**
     * 插入异常入库单
     * @param 入库单号 $inNo
     * @param 仓库编码 $warehouseCode
     * @param 货主编码 $ownerCode
     * @param 订单号   $orderNo
     * @param 错误信息 $errInfo
     */
    public function insertErrInfo($inNo,$warehouseCode,$ownerCode,$orderNo,$errInfo='')
    {
        global $db;

        $querySql = 'SELECT in_no FROM t_fx_err_inbound_list WHERE order_no=:order_no AND customer_id=:customer_id AND flag=0';
        $queryValues[':order_no'] = $orderNo;
        $queryValues[':customer_id'] = $ownerCode;
        $model = $db->prepare($querySql);
        $model->execute($queryValues);
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        if (empty($rs)) {
            $values = array();
            $sql = "INSERT INTO t_fx_err_inbound_list(in_no,order_no,customer_id,warehouse_code,err_msg,create_time) VALUES(:in_no,:order_no,:customer_id,:warehouse_code,:err_msg,:create_time)";
            $model = $db->prepare($sql);
            $values[':in_no'] = $inNo;
            $values[':order_no'] = $orderNo;
            $values[':customer_id'] = $ownerCode;
            $values[':warehouse_code'] = $warehouseCode;
            $values[':err_msg'] = $errInfo;
            $values[':create_time'] = date("Y-m-d H:i:s");
            $model->execute($values);
        }
    }
    
    public function updateOrderInfo($params){
        global $db;
    
        $values = array();
        $values[':order_no']        = $params['deliveryOrder']['deliveryOrderCode'];
        $values[':customer_id']     = qimen_service::$_customerId;
        $values[':warehouse_code']  = $params['deliveryOrder']['warehouseCode'];
    
        if (!empty($params['packages']['package'])) {
            if (empty($params['packages']['package'][0])) {
                $params['packages']['package'] = array($params['packages']['package']);
            }
            $expressCode = $params['packages']['package'][0]['expressCode'];
        } else {
            $expressCode = '';
        }
        
        $updateSql = "UPDATE t_outbound_info
	                   SET order_status='99',so_reference5='{$expressCode}'
	                   WHERE order_no=:order_no
	                   AND customer_id=:customer_id
	                   AND warehouse_code=:warehouse_code;";
        $model = $db->prepare($updateSql);
        $model->execute($values);
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
        $sql = "SELECT order_id,fx_is_depositpay,fx_customer from t_outbound_info where order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':order_no', $params['deliveryOrder']['deliveryOrderCode']);
        //$model->bindParam(':order_type', $params['deliveryOrder']['orderType']);
        $model->bindParam(':customer_id', qimen_service::$_customerId);
        $model->bindParam(':warehouse_code', $params['deliveryOrder']['warehouseCode']);
        $model->execute();
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        $orderId = $rs['order_id'];

        $this->is_depositpay = $rs['fx_is_depositpay'];
        $this->fx_customer = $rs['fx_customer'];
        
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
