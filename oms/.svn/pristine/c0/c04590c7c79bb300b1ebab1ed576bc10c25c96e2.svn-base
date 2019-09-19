<?php
/**
 * 单据取消操作类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';

class wmsOrderCancel extends wmsRequest
{

    /**
     * 单据取消
     * @param $params         
     * @return array
     */
    public function cancel($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            // 转发数据给wms
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success'){
                    $inType = array('B2BRK','SCRK','LYRK','CCRK','CGRK','DBRK','QTRK','XTRK','HHRK');
                    $outType = array('PTCK','DBCK','B2BCK','QTCK');
                    $deliType = array('JYCK','HHCK','BFCK');
                    $orderType = $params['orderType'];
                    $column_arr = $this->get_dataBase_relation('order_cancel');
                    $column_key_str = implode(',', array_values($column_arr)) . ', order_id, create_time';
                    if (in_array($orderType, $inType)) {
                        $this->cancelOrder('inbound', $params, $column_arr, $column_key_str);
                    } else if (in_array($orderType, $outType)) {
                        $this->cancelOrder('outbound', $params, $column_arr, $column_key_str);
                    } else if (in_array($orderType, $deliType)) {
                        $this->cancelOrder('delivery_order', $params, $column_arr, $column_key_str);
                    } else {
                        $this->cancelOrder('store_process_order', $params, $column_arr, $column_key_str);
                    }
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
     * 单据取消功能
     * @param 表名 $tableName
     * @param 数据 $params
     * @param 参数映射 $column_arr
     * @param 字段名 $column_key_str
     */
    public function cancelOrder($tableName,$params,$column_arr,$column_key_str){
        $db = Yii::app()->db;
        $findTable = 't_'. $tableName . '_info';
        $addTable = 't_' . $tableName . '_cancel';
        if ($tableName == 'store_process_order') {
            $sql = "SELECT process_id FROM $findTable WHERE process_order_code = :process_order_code AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND order_type = :order_type AND is_valid = 1";
            $model = $db->createCommand($sql);
            $model->bindValue(':process_order_code', $params['orderCode']);
            $model->bindValue(':customer_id', $params['ownerCode']);
            $model->bindValue(':warehouse_code', $params['warehouseCode']);
            $model->bindValue(':order_type', $params['orderType']);
        }elseif($tableName == 'delivery_order'){
            $sql = "SELECT delivery_id FROM $findTable WHERE delivery_order_code = :delivery_order_code AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND order_type = :order_type AND is_valid = 1";
            $model = $db->createCommand($sql);
            $model->bindValue(':delivery_order_code', $params['orderCode']);
            $model->bindValue(':customer_id', $params['ownerCode']);
            $model->bindValue(':warehouse_code', $params['warehouseCode']);
            $model->bindValue(':order_type', $params['orderType']);
        } else {
            $sql = "SELECT order_id FROM $findTable WHERE order_no = :order_no AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND order_type = :order_type AND is_valid = 1";
            $model = $db->createCommand($sql);
            $model->bindValue(':order_no', $params['orderCode']);
            $model->bindValue(':customer_id', $params['ownerCode']);
            $model->bindValue(':warehouse_code', $params['warehouseCode']);
            $model->bindValue(':order_type', $params['orderType']);
        }
        $rs = $model->queryRow();
        if ($tableName == 'store_process_order') {
            $orderId = $rs['process_id'];
        } elseif ($tableName == 'delivery_order') {
            $orderId = $rs['delivery_id'];
        } else {
            $orderId = $rs['order_id'];
        }
        
        $column_value_str = ":" . implode(",:", array_values($column_arr)) . ",{$orderId},now()";
        $sql = "INSERT INTO $addTable({$column_key_str}) VALUES({$column_value_str})";
        $model = $db->createCommand($sql);
        $values = array();
        foreach ($column_arr as $k => $v) {
            $values[':' . $v] = empty($params[$k]) ? '' : $params[$k] ; 
        }
        $model->bindValues($values);
        $model->execute();
        
        if ($tableName == 'store_process_order') {
            $sql = "UPDATE $findTable SET order_status='CANCELED',is_valid = 0 where process_id='{$orderId}'";
        } elseif ($tableName == 'delivery_order') {
            $sql = "UPDATE $findTable SET order_status='CANCELED',is_valid = 0 where delivery_id='{$orderId}'";
        } else {
            $sql = "UPDATE $findTable SET order_status='CANCELED',is_valid = 0 where order_id='{$orderId}'";
        }
        $model = $db->createCommand($sql);
        $model->execute();
    }
}