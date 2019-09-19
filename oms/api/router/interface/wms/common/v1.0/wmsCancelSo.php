<?php
/**
 * 出库单取消
 * ERP => OMS => WMS
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';

class wmsCancelSo extends wmsRequest
{
    /**
     * 创建出库单订单，并推送取消的订单数据给WMS
     * @param $params
     * @return array
     */
    public function create($params)
    {
        if (! empty($params)) {
            // 转发数据给WMS
            $response = $this->send(service::$_methodTo, $params);
            // 解析返回的数据
            if (! empty($response)) {
                // 获取错误数据
                if ($response['returnFlag'] != 1) {
                	$error_info_arr = $this->merge_error_data($response['resultInfo'], msg::$err_arr);
                } else {
                	$error_info_arr = $this->merge_error_data('', msg::$err_arr);
                }
                // 组合失败数据成xml格式
                $xmlData = $this->msgObj->get_error_str($error_info_arr);
                if ($response['returnFlag'] == 0) {
                    return $this->msgObj->output(0, $response['returnDesc'], '0001', $xmlData, $response['addon']);
                } else {
                    if ($response['returnFlag'] == 1 || $response['returnFlag'] == 2) {
                        // 获取操作成功的数据
                        if (! empty($response['resultInfo'])) {
                            foreach ($response['resultInfo'] as $k => $v) {
                                foreach ($params['header'] as $key => $val) {
                                    if ($v['CustomerID'] == $val['CustomerID'] && $v['OrderNo'] == $val['OrderNo'] && $v['OrderType'] == $val['OrderType'] && $v['WarehouseID'] == $val['WarehouseID']) {
                                        unset($params['header'][$key]);
                                    }
                                }
                            }
                        }
                        // 写入数据库
                        if (! empty($params['header'])) {
                            global $db;
                            $utilObj = new util();
                            $multiFlag = $utilObj->isArrayMulti($params);
                            if ($multiFlag) {
                            	$headers = $params['header'];
                            } else {
                            	$headers = array($params['header']);
                            }
                            if (! empty($headers)) {
                            	// 获取出库单取消表接口参数与数据库字段对应关系
                            	$column_arr = $this->get_dataBase_relation('outbound_cancel');
                            	$column_key_str = implode(',', array_values($column_arr)) . ', order_id, create_time';
                                foreach ($headers as $key => $val) {
                                    // 获取订单ID
                                    $orderId = $this->get_outbound_orderId($val['OrderNo'], $val['OrderType'], $val['CustomerID'], $val['WarehouseID']);                                  
                                    $column_value_str = ":" . implode(',:', array_values($column_arr)) . ", {$orderId}, now()";
                                    $sql = "INSERT INTO t_outbound_cancel({$column_key_str}) VALUES({$column_value_str})";
                                    $model = $db->prepare($sql);
                                    $values = array();
                                    foreach ($column_arr as $k => $v) {
                                        $values[':' . $v] = empty($val[$k]) ? '' : $val[$k];
                                    }
                                    if ($model->execute($values)) {
                                        // 更新库存
                                        if (UPDATE_INVENTORY_FLAG) {
                                        	$this->update_cancel_inventory($val['CustomerID'], $val['WarehouseID'], $orderId);
                                        }
                                        //更新订单状态
                                        $this->update_outbound_status($orderId);
                                    }
                                }
                            }
                        }
                    }
                }
                if ($response['returnFlag'] == 1) {
                    if (empty($error_info_arr)) {
                        return $this->msgObj->output(1, 'ok', '0000', $xmlData, $response['addon']);
                    } else {
                        return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon']);
                    }
                } elseif ($response['returnFlag'] == 2) {
                    return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon']);
                }
            } else {
                // 获取错误数据
                $error_info_arr = $this->merge_error_data($params['header'], msg::$err_arr);
                // 组合失败数据成xml格式
                $xmlData = $this->msgObj->get_error_str($error_info_arr);
                return $this->msgObj->output(0, 'fail', 'S007', $xmlData, $response['addon']);
            }
        } else {
            if (! empty(msg::$err_arr)) {
                $xml = new xml();
                $xmlData = '';
                foreach (msg::$err_arr as $key => $val) {
                    $xmlData .= $xml->array2xml(array(
                        'result' => $val
                    ));
                }
            }
            return $this->msgObj->output(0, 'fail', '0001', $xmlData);
        }
    }

    /**
     * 出库取消,更新库存
     * @param $customerId 客户ID
     * @param $warehouseId 仓库ID
     * @param $orderId 订单id
     * @return boolean
     */
    public function update_cancel_inventory($customerId, $warehouseId, $orderId)
    {
        global $db;
        // 获取订单明细
        $Ssql = 'SELECT sku, customer_id, qty_ordered FROM t_outbound_detail WHERE order_id = :order_id';
        $detailModel = $db->prepare($Ssql);
        $detailModel->bindParam(':order_id', $orderId);
        $detailModel->execute();
        $details = $detailModel->fetchAll(PDO::FETCH_ASSOC);
        if (! empty($details)) {    
            foreach ($details as $v){
    			//获取SKU的库存信息
    			$sql = "SELECT sku,qty,occupy_qty,qty_total from t_product_inventory where customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
    			$model = $db->prepare($sql);   			
    			$model->bindParam(':customer_id', $customerId);
    			$model->bindParam(':warehouse_code', $warehouseId);
    			$model->bindParam(':sku', $v['sku']);
    			$model->execute();
    			$rs = $model->fetch(PDO::FETCH_ASSOC);
    			if (!empty($rs)) {
    				$qty = $rs['qty'] + $v['qty_ordered'];
    				$occupyQty = $rs['occupy_qty'] - $v['qty_ordered'];				
    				$sql = "UPDATE t_product_inventory set occupy_qty=:occupy_qty,qty=:qty WHERE customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
    				$model = $db->prepare($sql);
    				$values = array(
    			      	  ':occupy_qty' => $occupyQty,
    		              ':qty' => $qty,
    					  ':customer_id' => $customerId,
    					  ':warehouse_code' => $warehouseId,
    					  ':sku' => $v['sku']   						
    				);
    				$model->execute($values);
    			}
            }
        } 
    }

    /**
     * 更新订单状态
     * @param $orderId 订单ID                 
     *
     */
    public function update_outbound_status($orderId)
    {
        global $db;
        $sql = "UPDATE t_outbound_info set order_status='90' WHERE order_id=:order_id";
        $model = $db->prepare($sql);
        $model->bindParam(':order_id', $orderId);
    	$model->execute();
    }

    /**
     * 获取出库单订单ID
     * @param $orderNo 订单号            
     * @param $customerID 客户ID            
     * @param $orderType 订单类型            
     * @param $warehouseId 仓库ID
     * @return string
     */
    public function get_outbound_orderId($orderNo, $orderType, $customerId, $warehouseId)
    {
        global $db;
        $sql = "SELECT order_id from t_outbound_info where order_no=:order_no AND order_type = :order_type AND customer_id =:customer_id AND warehouse_code=:warehouse_code  AND is_valid = 1";
        $model = $db->prepare($sql);
        $model->bindParam(':order_no', $orderNo);
        $model->bindParam(':order_type', $orderType);
        $model->bindParam(':customer_id', $customerId);
        $model->bindParam(':warehouse_code', $warehouseId);
        $model->execute();
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        return $rs['order_id'];
    }

    /**
     * 解析返回的错误数据，并和之前的错误数据进行组合
     *
     * @param $error_info erp接口返回的resultInfo错误信息            
     * @param $error_arr msg类中存储的错误信息            
     * @return array
     */
    public function merge_error_data($error_info, $error_arr)
    {
        $return_arr = array();
        $i = 0;
        if (! empty($error_info)) {
            foreach ($error_info as $v) {
                $return_arr[$i] = $v;
                $i ++;
            }
        }
        if (! empty($error_arr)) {
            foreach ($error_arr as $val) {
                $return_arr[$i] = $val;
                $i ++;
            }
        }
        return $return_arr;
    }
}

