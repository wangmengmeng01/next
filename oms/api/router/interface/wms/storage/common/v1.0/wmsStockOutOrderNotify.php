<?php
/**
 * 仓储普通出库单下发接口处理类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/storage/common/wmsRequest.php';
class wmsStockOutOrderNotify extends wmsRequest
{
	/**
	 * 创建出库单
	 * @param $params
	 * @return array
	 */
	public function notify($params)
    {   
    	if (empty($params)) {
    	    return $this->msgObj->outputCnStorage(false, '失败：请求的数据为空', 'S003');
    	} else {
    	    if (empty($params['orderItemList']['orderItem'])) {
    	        return $this->msgObj->outputCnStorage(false, '失败：订单明细为空', 'S003');
    	    } else {
    	        global $db;
    	        
    	        $orderTypeRelation = array(
    	            '301' => 'DBCK',
    	            '303' => 'B2BCK',
    	            '901' => 'PTCK',
    	            '903' => 'QTCK',
    	            '305' => 'B2BCK'
    	        );
    	        
    	        $tmsServiceCode = !empty($params['tmsServiceCode'])&& isset($params['tmsServiceCode']) ? $params['tmsServiceCode'] : '' ;
    	        
    	        $xmlStr = '';
    	        $xmlStr .= '<?xml version="1.0" encoding="utf-8"?><request><deliveryOrder>';
    	        $xmlStr .= '<deliveryOrderCode>' .  $params['erpOrderCode']                     . '</deliveryOrderCode>';
    	        $xmlStr .= '<cnOrderId>'         .  $params['orderCode']                        . '</cnOrderId>';
    	        $xmlStr .= '<orderType>'         .  $orderTypeRelation[$params['orderType']]    . '</orderType>';
    	        $xmlStr .= '<warehouseCode>'     .  $params['storeCode']                        . '</warehouseCode>';
    	        $xmlStr .= '<createTime>'        .  $params['orderCreateTime']                  . '</createTime>';
    	        $xmlStr .= '<logisticsCode>'     .  $params['tmsServiceCode']                   . '</logisticsCode>';

                $xmlStr .= '<senderInfo>';
                $xmlStr .= '<zipCode>'     .  $params['senderInfo']['senderZipCode']  . '</zipCode>';
                $xmlStr .= '<province>'    .  $params['senderInfo']['senderProvince'] . '</province>';
                $xmlStr .= '<city>'        .  $params['senderInfo']['senderCity']     . '</city>';
                $xmlStr .= '<area>'        .  $params['senderInfo']['senderArea']     . '</area>';
                $xmlStr .= '<town>'        .  $params['senderInfo']['senderTown']     . '</town>';
                $xmlStr .= '<detailAddress>'. $params['senderInfo']['senderAddress']  . '</detailAddress>';
                $xmlStr .= '<name>'        .  $params['senderInfo']['senderName']     . '</name>';
                $xmlStr .= '<mobile>'      .  $params['senderInfo']['senderMobile']   . '</mobile>';
                $xmlStr .= '<tel>'         .  $params['senderInfo']['senderPhone']    . '</tel>';
                $xmlStr .= '<email>'       .  $params['senderInfo']['senderEmail']    . '</email>';
                $xmlStr .= '</senderInfo>';

    	        $xmlStr .= '<receiverInfo>';
    	        $xmlStr .= '<name>'              .  $params['receiverInfo']['receiverName']     . '</name>';
    	        $xmlStr .= '<mobile>'            .  $params['receiverInfo']['receiverMobile']   . '</mobile>';
    	        $xmlStr .= '<province>'          .  $params['receiverInfo']['receiverProvince'] . '</province>';
    	        $xmlStr .= '<city>'              .  $params['receiverInfo']['receiverCity']     . '</city>';
    	        $xmlStr .= '<detailAddress>'     .  $params['receiverInfo']['receiverAddress']  . '</detailAddress>';
    	        $xmlStr .= '</receiverInfo>';
    	        $xmlStr .= '</deliveryOrder><orderLines>';  
    	        if (empty($params['orderItemList']['orderItem'][0])) {
    	            $params['orderItemList']['orderItem'] = array($params['orderItemList']['orderItem']);
    	        }
    	        foreach ($params['orderItemList']['orderItem'] as $val) {
    	            $xmlStr .= '<orderLine>';
    	            $xmlStr .= '<cnOrderItemId>'. $val['orderItemId']    . '</cnOrderItemId>';
    	            $xmlStr .= '<ownerCode>'    . $params['ownerUserId'] . '</ownerCode>';
    	            $xmlStr .= '<itemCode>'     . $val['itemCode']       . '</itemCode>';
    	            $xmlStr .= '<planQty>'      . $val['itemQuantity']   . '</planQty>';
    	            $xmlStr .= '</orderLine>';
    	        }
    	        $xmlStr .= '</orderLines></request>';
    	        cn_storage_service::$_data = $xmlStr;
    	        
    	        //推送给wms
    	        $response = $this->send(cn_storage_service::$_data,'stockout.create');
    	        //解析返回的数据
    	        if (!empty($response)) {
    	            if ($response['success'] == 1) {
    	                $wmsOrderStatusUploadStr  = '<?xml version="1.0" encoding="utf-8"?><request>';
    	                $wmsOrderStatusUploadStr .= '<orderType>' . $params['orderType'] . '</orderType>';
    	                $wmsOrderStatusUploadStr .= '<orderCode>' . $params['orderCode'] . '</orderCode>';
    	                $wmsOrderStatusUploadStr .= '<status>WMS_ACCEPT</status>';
    	                $wmsOrderStatusUploadStr .= '<operator>张三</operator>';
    	                $wmsOrderStatusUploadStr .= '<operatorContact>13514516577</operatorContact>';
    	                $wmsOrderStatusUploadStr .= '<operateDate>'.date("Y-m-d H:i:s").'</operateDate>';
    	                $wmsOrderStatusUploadStr .= '</request>';

    	                $orderStatusUploadRs = $this->requestOmsToCn($params, 'WMS_ORDER_STATUS_UPLOAD', $wmsOrderStatusUploadStr);
    	                if ($orderStatusUploadRs['success']) { 
    	                    $orderInfo = $this->check_outbound($params);
    	                    
    	                    if (empty($orderInfo)) {
    	                        //写入出库单数据
    	                        $this->insert_stock_order_info($params);
    	                    } else {
    	                        $columnsArr = array();
    	                        $updateInfoArr = array();
    	                        if ($orderInfo['c_province'] != $params['receiverInfo']['receiverProvince']) {
    	                            $columnsArr[] = 'c_province';
    	                            $updateInfoArr[':c_province'] = $params['receiverInfo']['receiverProvince'];
    	                        }
    	                        if ($orderInfo['c_city'] != $params['receiverInfo']['receiverCity']) {
    	                            $columnsArr[] = 'c_city';
    	                            $updateInfoArr[':c_city'] = $params['receiverInfo']['receiverCity'];
    	                        }
    	                        if ($orderInfo['c_address2'] != $params['receiverInfo']['receiverArea']) {
    	                            $columnsArr[] = 'c_address2';
    	                            $updateInfoArr[':c_address2'] = $params['receiverInfo']['receiverArea'];
    	                        }
    	                        if ($orderInfo['c_address3'] != $params['receiverInfo']['receiverTown']) {
    	                            $columnsArr[] = 'c_address3';
    	                            $updateInfoArr[':c_address3'] = $params['receiverInfo']['receiverTown'];
    	                        }
    	                        if ($orderInfo['c_address1'] != $params['receiverInfo']['receiverAddress']) {
    	                            $columnsArr[] = 'c_address1';
    	                            $updateInfoArr[':c_address1'] = $params['receiverInfo']['receiverAddress'];
    	                        }
    	                        if ($orderInfo['consignee_name'] != $params['receiverInfo']['receiverName']) {
    	                            $columnsArr[] = 'consignee_name';
    	                            $updateInfoArr[':consignee_name'] = $params['receiverInfo']['receiverName'];
    	                        }
    	                        if ($orderInfo['c_tel1'] != $params['receiverInfo']['receiverMobile']) {
    	                            $columnsArr[] = 'c_tel1';
    	                            $updateInfoArr[':c_tel1'] = $params['receiverInfo']['receiverMobile'];
    	                        }
    	                        if ($orderInfo['c_tel2'] != $params['receiverInfo']['receiverPhone']) {
    	                            $columnsArr[] = 'c_tel2';
    	                            $updateInfoArr[':c_tel2'] = $params['receiverInfo']['receiverPhone'];
    	                        }
    	                        if (!empty($columnsArr)) {
    	                            $setSqlStr = '';
    	                            foreach ($columnsArr as $cval) {
    	                                $setSqlStr .= $cval . "=:" . $cval . ',';
    	                            }
    	                            $setSqlStr = substr($setSqlStr, 0, -1);
    	                            $updateSql = "UPDATE t_outbound_info SET " . $setSqlStr . " WHERE order_id=" . $orderInfo['order_id'];
    	                            $model = $db->prepare($updateSql);
    	                            $model->execute($updateInfoArr);
    	                        }     
    	                    }
    	                    return $this->msgObj->outputCnStorage(true,'','',$response['addon']);
    	                } else {
    	                    //调用取消接口
    	                    $cancelStr  = '<?xml version="1.0" encoding="utf-8"?>';
    	                    $cancelStr .= '<request>';
    	                    $cancelStr .= '<ownerUserId>'. $params['ownerUserId'] . '</ownerUserId>';
    	                    $cancelStr .= '<orderCode>'  . $params['orderCode']   . '</orderCode>';
    	                    $cancelStr .= '<storeCode>'  . $params['storeCode']   . '</storeCode>';
    	                    $cancelStr .= '<orderType>'  . $params['orderType']   . '</orderType>';
    	                    $cancelStr .= '</request>';
    	                    
    	                    $cancelRs = $this->requestOmsToWms($cancelStr, 'WMS_ORDER_CANCEL_NOTIFY');
    	                    if ($cancelRs['success']) {
    	                        return $this->msgObj->outputCnStorage(false,'下发失败：调用订单流水接口失败！','S004');
    	                    } else {
    	                        return $this->msgObj->outputCnStorage(false,'下发失败：调用订单流水接口失败！','S004');
    	                    }
    	                }
    	            } else {
    	                return $this->msgObj->outputCnStorage(false, $response['errorMsg'], $response['errorCode']);
    	            }
    	        } else {
    	            return $this->msgObj->outputQimen(false, 'wms接口调用失败', 'S007');
    	        }
    	    }
    	}
    } 
    
    /**
     * 校验订单号是否存在
     */
    public function check_outbound($orderData)
    {
    	global $db;
    	$sql = 'SELECT * FROM t_outbound_info WHERE order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
    	$model = $db->prepare($sql);
    	$model->bindParam(':order_no', $orderData['erpOrderCode']);
    	$model->bindParam(':customer_id', $orderData['ownerUserId']);
    	$model->bindParam(':warehouse_code', $orderData['storeCode']);
    	$model->execute(); 
    	$rs = $model->fetch(PDO::FETCH_ASSOC);
    	return $rs;
    }

    /**
     * 插入出库单信息
     * @param 请求参数 $params
     * @return 返回值
     */
    public function insert_stock_order_info($params){
        $orderItemList = $params['orderItemList']['orderItem'];
        
        $column_order_info_arr = $this->get_dataBase_relation('stock_out_order_info');
        $column_order_info_arr1 = $column_order_info_arr;
        unset($column_order_info_arr1['receiverInfo']);
        unset($column_order_info_arr1['senderInfo']);
        unset($column_order_info_arr1['driverInfo']);
        $column_key_orderInfo = implode(',', array_values($column_order_info_arr1)) . ',';
        $column_receiver_orderInfo = implode(',', array_values($column_order_info_arr['receiverInfo'])) . ',';
        $column_sender_orderInfo = implode(',', array_values($column_order_info_arr['senderInfo'])) . ',';
        $column_driverInfo_orderInfo = implode(',', array_values($column_order_info_arr['driverInfo'])) . ',create_time';
        $column_key_orderInfo .= $column_receiver_orderInfo . $column_sender_orderInfo . $column_driverInfo_orderInfo;
        
        $column_order_detail_arr = $this->get_dataBase_relation('stock_out_order_detail');
        $column_key_orderDetail = implode(',', array_values($column_order_detail_arr)) . ',order_id,create_time';
        
        global $db;
        if (!empty($params)) {
            $column_value_orderInfo = ":" . implode(',:', array_values($column_order_info_arr1)) . ',';
            $column_vreceiver_orderInfo = ":" . implode(',:', array_values($column_order_info_arr['receiverInfo'])) . ',';
            $column_vsender_orderInfo = ":" . implode(',:', array_values($column_order_info_arr['senderInfo'])) . ',';
            $column_vdriverInfo_orderInfo = ":" . implode(',:', array_values($column_order_info_arr['driverInfo'])) . ',now()';
            $column_value_orderInfo .= $column_vreceiver_orderInfo.$column_vsender_orderInfo.$column_vdriverInfo_orderInfo;
            
            $sql = "INSERT INTO t_outbound_info(" . $column_key_orderInfo . ') VALUES(' . $column_value_orderInfo . ')';
            $model = $db->prepare($sql);
            $values = array();
            foreach ($column_order_info_arr as $k=>$v) {
                if (in_array($k, array('receiverInfo','senderInfo','driverInfo'))) {
                    foreach ($v as $i=>$j) {
                        $values[':'.$j] = empty($params[$k][$i]) ? '' : $params[$k][$i];
                    }
                } else {
                    $values[':'.$v] = empty($params[$k]) ? '' : $params[$k];
                }
            }
            $model->execute($values);
            $orderId = $db->lastInsertId();
            
            if (!empty($orderItemList)) {
                if (empty($orderItemList[0])) {
                    $orderItemList = array($orderItemList);
                }
                foreach ($orderItemList as $o_d_v) {
                    $column_value_orderDetail = ':' . implode(',:', $column_order_detail_arr) . ',:order_id,now()';
                    $detail_sql = "INSERT IGNORE INTO t_outbound_detail(" . $column_key_orderDetail . ') VALUES(' . $column_value_orderDetail . ')';
                    $dValues = array();
                    $dValues[':order_id'] = $orderId;
                    foreach ($column_order_detail_arr as $d_k=>$d_v) {
                        $dValues[':'.$d_v] =  empty($o_d_v[$d_k]) ? '' : $o_d_v[$d_k] ;
                    }
                    $dModel = $db->prepare($detail_sql);
                    $dModel->execute($dValues);
                }
            }
        } 
        return true;
    }
}

