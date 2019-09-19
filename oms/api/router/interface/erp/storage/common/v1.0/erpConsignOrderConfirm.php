<?php
require API_ROOT . '/router/interface/erp/storage/common/cnRequest.php';
class erpConsignOrderConfirm extends cnRequest{
    public function confirm($data,$params){
        if (empty($params)) {
            return $this->msgObj->outputCnStorage(false,'失败：请求的数据为空','S003');
        } else {
            $order = $this->getOriginOrderType($params['orderCode']);
            if (!array_key_exists('order_type', $order)) {
                return $order;
            } else {
                $orderType = $order['order_type'];
                $typeStr = "<orderType>" . $orderType . "</orderType>";
                $data =  preg_replace("/<orderType>(.*)<\/orderType>/s", $typeStr, $data);
                
                $response = $this->send($data);
                if (!empty($response)) {
                    if ($response['success']) {
                        $this->insertDeliveryOrderRecord($params,$order['delivery_id']);
                        return $this->msgObj->outputCnStorage(true,'','',$response['addon']);
                    } else {
                        return $this->msgObj->outputCnStorage($response['success'], $response['errorMsg'], $response['errorCode']);
                    }
                } else {
                    return $this->msgObj->outputCnStorage(false, 'wms接口调用失败', 'S007');
                }
            }
        }
    }
    
    public function insertDeliveryOrderRecord($params){
        if (!empty($params)) {
            global $db;

            $detailInfo  = $params['orderItems']['orderItem'];

            unset($params['orderItems']);
            unset($params['tmsOrders']);
            
            $delivery_record_relation = $this->get_dataBase_relation('delivery_info_record');
            $delivery_detail_record_relation = $this->get_dataBase_relation('delivery_detail_record');
            //$delivery_package_record_relation = $this->get_dataBase_relation('delivery_package_record');
            //$delivery_package_material_record_relation = $this->get_dataBase_relation('delivery_package_material_record');
            
            $column_delivery_record_key = implode(',', $delivery_record_relation) . ',create_time';
            $column_delivery_detail_record_key = implode(',', $delivery_detail_record_relation) . ',delivery_id,create_time'; 
            //$column_delivery_package_record_key = implode(',', $delivery_package_record_relation) . ',delivery_id,create_time';
            //$column_delivery_package_material_record_key = implode(',', $delivery_package_material_record_relation) . ',package_id';
            
            $column_delivery_record_value = ':'. implode(',:', $delivery_record_relation) . ',now()';
            $deliveryRecordSql = "INSERT INTO t_delivery_order_info_record(". $column_delivery_record_key .") VALUES(". $column_delivery_record_value .")";
            $deliveryRecordModel = $db->prepare($deliveryRecordSql);
            $values = array();
            foreach ($delivery_record_relation as $d_k=>$d_v) {
                $values[':'.$d_v] = !empty($params[$d_k]) ? $params[$d_k] : '' ;
            }
            $deliveryRecordModel->execute($values);
            $deliveryId = $db->lastInsertId();
            
            $column_delivery_detail_record_value = ':'.implode(',:', $delivery_detail_record_relation) . ",{$deliveryId},now()";
            $deliveryDetailRecordSql = "INSERT INTO t_delivery_order_detail_record(" . $column_delivery_detail_record_key . ") VALUES(" . $column_delivery_detail_record_value . ")";
            $deliveryDetailRecordModel = $db->prepare($deliveryDetailRecordSql);            
            if (empty($detailInfo[0])) {
                $detailInfo = array($detailInfo);
            }
            foreach ($detailInfo as $o_d_v) {
                $values2 = array();
                foreach ($delivery_detail_record_relation as $d_d_r_k=>$d_d_r_v) {
                    $values2[':'.$d_d_r_v] = !empty($o_d_v[$d_d_r_k]) ? $o_d_v[$d_d_r_k] : '';
                }
            }
            $deliveryDetailRecordModel->execute($values2);
        }
    }
    
    //获取菜鸟下发时的订单类型
    public function getOriginOrderType($cnOrderCode){
        global $db;
    
        $sql = "SELECT order_type,delivery_id
                FROM t_delivery_order_info
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
            return $this->msgObj->outputCnStorage(false, "该发货单不存在!", 'S003');
        } else {
            return $order;
        }
    }
}