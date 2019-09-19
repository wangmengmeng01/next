<?php
require API_ROOT . '/router/interface/erp/storage/common/cnRequest.php';
class erpStockOutOrderConfirm extends cnRequest{
    public function confirm($data,$params){
        if (empty($params)) {
            return $this->msgObj->outputCnStorage(false,'失败：请求的数据为空','S003');
        } else {
            $orderInfo = $this->getOriginOrderType($params['orderCode']);
            if (!array_key_exists('order_type', $orderInfo)) {
                return $orderInfo;
            } else {
                $orderType = $orderInfo['order_type'];
                $typeStr = "<orderType>" . $orderType . "</orderType>";
                $data = preg_replace("/<orderType>(.*)<\/orderType>/s", $typeStr, $data);
                
                $response = $this->send($data);
                if (!empty($response)) {
                    if ($response['success']) {
                        $this->insertOutboundRecord($params,$orderInfo['order_id']);
                        return $this->msgObj->outputCnStorage(true,'','',$response['addon']);
                    } else {
                        return $this->msgObj->outputCnStorage(false,$response['errorMsg'],$response['errorCode'],$response['addon']);
                    }
                } else {
                    return $this->msgObj->outputCnStorage(false, 'wms接口调用失败', 'S007');
                }
            }
        }
    }
    
    public function insertOutboundRecord($params,$orderId){
        global $db;
        if (!empty($params)) {
            $customerId    = cn_storage_service::$_customerid;
            $warehouseCode = cn_storage_service::$_warehouseid;
            
            //字段对应关系
            $outbound_record_relation                 = $this->get_database_relation('outbound_info_record');
            $outbound_detail_record_relation          = $this->get_database_relation('outbound_info_detail_record');
            $outbound_package_record_relation         = $this->get_database_relation('outbound_package_info_record');
            $outbound_package_product_record_relation = $this->get_database_relation('outbound_package_product_record');
            
            $column_outbound_record_key                 = implode(',', array_values($outbound_record_relation)) . ',order_id,customer_id,warehouse_code,create_time';
            $column_outbound_detail_record_key          = implode(',', array_values($outbound_detail_record_relation)) .',record_id,order_id,customer_id,create_time';
            $column_outbound_package_record_key         = implode(',', array_values($outbound_package_record_relation)) .',record_id,order_id,create_time';
            $column_outbound_package_product_record_key = implode(',', array_values($outbound_package_product_record_relation)) . ',package_id,create_time';
            
            //订单回传表
            $column_outbound_record_value = ':' . implode(',:', array_values($outbound_record_relation)) . ",{$orderId},'{$customerId}','{$warehouseCode}',now()";
            $outboundSql = "INSERT INTO t_outbound_info_record(" . $column_outbound_record_key .
                           ") VALUES(" . $column_outbound_record_value . ")";
            $outboundModel = $db->prepare($outboundSql);
            $values = array();
            foreach ($outbound_record_relation as $o_k=>$o_v) {
                $values[':'.$o_v] = !empty($params[$o_k]) ?  $params[$o_k] : '';
            }
            $outboundModel->execute($values);
            $recordId = $db->lastInsertId();
            
            //订单明细回传表   
            $column_outbound_detail_record_value = ":".implode(',:', array_values($outbound_detail_record_relation)) .",{$recordId},{$orderId},'{$customerId}',now()";
            $outboundDetailSql = "INSERT INTO t_outbound_detail_record(" .$column_outbound_detail_record_key. ") VALUES( " . $column_outbound_detail_record_value .")";
            $outboundDetailModel = $db->prepare($outboundDetailSql);
            if (empty($params['orderItems']['orderItem'][0])) {
                $params['orderItems']['orderItem'] = array($params['orderItems']['orderItem']);
            }
            foreach ($params['orderItems']['orderItem'] as $o_i_v) {
                $values1 = array();
                foreach ($outbound_detail_record_relation as $o_d_k=>$o_d_v) {
                    $values1[':'.$o_d_v] = !empty($o_i_v[$o_d_k]) ? $o_i_v[$o_d_k] : '' ;
                }
                $outboundDetailModel->execute($values1);
            }
            
            $column_outbound_package_record_value = ':' . implode(',:', $outbound_package_record_relation) . ",{$recordId},{$orderId},now()";
            $outboundPackageSql = "INSERT INTO t_outbound_package_record(" . $column_outbound_package_record_key . ") VALUES(" . $column_outbound_package_record_value . ")";
            $outboundPackageModel = $db->prepare($outboundPackageSql);
            
            if (empty($params['packageInfos']['packageInfo'][0])) {
                $params['packageInfos']['packageInfo'] = array($params['packageInfos']['packageInfo']);
            }
            foreach ($params['packageInfos']['packageInfo'] as $o_p_v) {
                $values2 = array();
                foreach ($outbound_package_record_relation as $o_p_r_k=>$o_p_r_v) {
                    $values2[':'.$o_p_r_v] = !empty($o_p_v[$o_p_r_k]) ? $o_p_v[$o_p_r_k] : '';
                }
                $outboundPackageModel->execute($values2);
                $packageId = $db->lastInsertId();
                
                $column_outbound_package_product_record_value = ':'. implode(',:', $outbound_package_product_record_relation) . ",{$packageId},now()";
                $outboundPackagePrdctSql = "INSERT INTO t_outbound_package_product_record(". $column_outbound_package_product_record_key .") VALUES( ". $column_outbound_package_product_record_value ." )";
                $outboundPackagePrdctModel = $db->prepare($outboundPackagePrdctSql);
                if (!empty($o_p_v['packageItemItems'])) {
                    if (empty($o_p_v['packageItemItems']['packageItem'][0])) {
                        $o_p_v['packageItemItems']['packageItem'] = array($o_p_v['packageItemItems']['packageItem']);
                    }
                    foreach ($o_p_v['packageItemItems']['packageItem'] as $o_pp_v) {
                        $values3 = array();
                        foreach ($outbound_package_product_record_relation as $o_pp_r_k=>$o_pp_r_v) {
                            $values3[':'.$o_pp_r_v] = !empty($o_pp_v[$o_pp_r_k]) ? $o_pp_v[$o_pp_r_k] : '' ;
                        }
                        $outboundPackagePrdctModel->execute($values3);
                    }
                }
            }
        }
    }   
    
    //获取菜鸟下发时的订单类型
    public function getOriginOrderType($cnOrderCode){
        global $db;
       
        $sql = "SELECT order_id,order_type
                FROM t_outbound_info
                WHERE cn_order_code=:cn_order_code
                AND customer_id=:customer_id
                AND warehouse_code=:warehouse_code;";
        $model = $db->prepare($sql);
        $values = array();
        $values[':cn_order_code'] = $cnOrderCode;
        $values[':customer_id'] = cn_storage_service::$_customerid;
        $values[':warehouse_code'] = cn_storage_service::$_warehouseid;
        $model->execute($values);
        $order = $model->fetch(PDO::FETCH_ASSOC);
        if (empty($order)) {
            return $this->msgObj->outputCnStorage(false, "该出库单不存在!", 'S003');
        } else {
            return $order;
        }
    }
}