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
            $tableName = $this->judgeOrderType($params['orderType']);
            //判断货主是否为校妆网：不使用缺货转仓功能的货主，正常的走单据取消接口
            if ($params['ownerCode'] == 'XZW') {
                $shortOrders = $this->findDeliveryShortOrder($params);
                //查询根据货主+单号的其他订单
                $otherWhOrders = $this->findDeliveryOrder($tableName,$params);
                //是否存在缺货订单
                if (!empty($shortOrders)) {
                    //缺货订单已经下发到wms
                    if (!empty($otherWhOrders)) {
                        foreach ($otherWhOrders as $o_v) {
                            $orderCancelStr = '';
                            $orderCancelStr .= '<?xml version="1.0" encoding="utf-8"?><request>';
                            $orderCancelStr .= '<warehouseCode>' . $o_v['warehouse_code'] . '</warehouseCode>';
                            $orderCancelStr .= '<ownerCode>' . $params['ownerCode'] . '</ownerCode>';
                            $orderCancelStr .= '<orderCode>' . $params['orderCode'] . '</orderCode>';
                            $orderCancelStr .= '<orderType>' . $params['orderType'] . '</orderType>';
                            $orderCancelStr .= '<cancelReason>oms取消转仓订单</cancelReason>';
                            $orderCancelStr .= '</request>';
                            
                            $otherResponse = $this->requestWms($orderCancelStr);
                            $xmlObj = new xml();
                            $requestData = $xmlObj->xmlStr2array($orderCancelStr);
                            $responseParam = $otherResponse['addon']['return_msg'];
                            $msgId = $this->funcObj->makeMsgId();
                            if ($otherResponse['flag'] == 'failure'){
                                $this->funcObj->writeQimenInterfaceLog(qimen_service::$_method, $orderCancelStr, '', $requestData, $responseParam, $msgId, qimen_service::$_toApiReturn, 0);
                                return $this->msgObj->outputQimen('failure', $otherResponse['message'], $otherResponse['code'], $otherResponse['addon']);
                            } else {
                                //更新重新下发的订单状态
                                $otherOdInfo = array();
                                $otherOdInfo['is_valid'] = 0;
                                $otherOdInfo['delivery_id'] = $o_v['delivery_id'];
                                $otherOdInfo['order_status'] = 'CANCELED';
                                $this->updateOrderStatus($tableName, $otherOdInfo);
                                $this->funcObj->writeQimenInterfaceLog(qimen_service::$_method, $orderCancelStr, '', $requestData, $responseParam, $msgId, qimen_service::$_toApiReturn, 1);
                            }
                        }
                    } //缺货订单未下发到wms
                    //更新缺货订单列表订单状态
                    foreach ($shortOrders as $s_v) {
                        $this->updateShortOrderStatus($s_v['order_id']);
                    }
                } else {//不存在缺货订单 
                    if (!empty($otherWhOrders)) {
                        foreach ($otherWhOrders as $o_v) {
                            $orderCancelStr = '';
                            $orderCancelStr .= '<?xml version="1.0" encoding="utf-8"?><request>';
                            $orderCancelStr .= '<warehouseCode>' . $o_v['warehouse_code'] . '</warehouseCode>';
                            $orderCancelStr .= '<ownerCode>' . $params['ownerCode'] . '</ownerCode>';
                            $orderCancelStr .= '<orderCode>' . $params['orderCode'] . '</orderCode>';
                            $orderCancelStr .= '<orderType>' . $params['orderType'] . '</orderType>';
                            $orderCancelStr .= '<cancelReason>oms取消转仓订单</cancelReason>';
                            $orderCancelStr .= '</request>';
                    
                            $otherResponse = $this->requestWms($orderCancelStr);
                            $xmlObj = new xml();
                            $requestData = $xmlObj->xmlStr2array($orderCancelStr);
                            $responseParam = $otherResponse['addon']['return_msg'];
                            $msgId = $this->funcObj->makeMsgId();
                            if ($otherResponse['flag'] == 'failure'){
                                $this->funcObj->writeQimenInterfaceLog(qimen_service::$_method, $orderCancelStr, '', $requestData, $responseParam, $msgId, qimen_service::$_toApiReturn, 0);
                                return $this->msgObj->outputQimen('failure', $otherResponse['message'], $otherResponse['code'], $otherResponse['addon']);
                            } else {
                                //更新重新下发的订单状态
                                $otherOdInfo = array();
                                $otherOdInfo['is_valid'] = 0;
                                $otherOdInfo['delivery_id'] = $o_v['delivery_id'];
                                $otherOdInfo['order_status'] = 'CANCELED';
                                $this->updateOrderStatus($tableName, $otherOdInfo);
                                $this->funcObj->writeQimenInterfaceLog(qimen_service::$_method, $orderCancelStr, '', $requestData, $responseParam, $msgId, qimen_service::$_toApiReturn, 1);
                            }
                        }
                    }
                }
            } 
            //正常流程-转发数据给wms(两种情况走这边：货主编码不是XZW;订单在缺货订单列表中不存在)
            $response = $this->send();
            return self::dealWmsResponse($response, $params);
        }
    }
    
    /**
     * 查询下发订单列表
     * @param   表名          $tableName
     * @param   请求参数   $params
     * @return  订单信息
     */
    public function findDeliveryOrder($tableName,$params){
        global $db;
        if ($tableName == 'delivery_order') {
            $sql = "SELECT delivery_id,warehouse_code
                    FROM t_delivery_order_info
                    WHERE customer_id=:customer_id
                    AND delivery_order_code=:order_no
                    AND warehouse_code!=:warehouse_code
                    AND is_valid=1;";
        } else {
            $sql = "SELECT order_id,warehouse_code 
                    FROM t_outbound_info 
                    WHERE customer_id=:customer_id
                    AND order_no=:order_no
                    AND warehouse_code!=:warehouse_code
                    AND is_valid=1;";
        }
        $model = $db->prepare($sql);
        $model->bindParam(':customer_id', $params['ownerCode']);
        $model->bindParam(':order_no', $params['orderCode']);
        $model->bindParam(':warehouse_code', $params['warehouseCode']);
        $model->execute();
        return $model->fetchAll(PDO::FETCH_ASSOC);
    }
    
    /**
     * 将缺货列表订单更改状态为3 已取消【接口】
     * @param $params
     */
    public function updateShortOrderStatus($orderId){
        global $db;
        $sql = "UPDATE t_delivery_order_shortage 
                SET done_flag=3 
                WHERE order_id=:order_id;";
        $model = $db->prepare($sql);
        $model->bindParam(':order_id', $orderId);
        $model->execute();
    }
    
    /**
     * 更新订单状态
     * @param 表名         $tableName
     * @param 请求参数  $params
     */
    public function updateOrderStatus($tableName,$params){
        global $db;
        $sql  = "UPDATE t_".$tableName ."_info SET is_valid=:is_valid,order_status=:order_status WHERE delivery_id=:delivery_id;";
        $model = $db->prepare($sql);
        $model->bindParam(':is_valid', $params['is_valid']);
        $model->bindParam(':order_status', $params['order_status']);
        $model->bindParam(':delivery_id', $params['delivery_id']);
        $model->execute();
    }
    
    /**
     * 查询缺货订单列表有无取消中的订单
     * @param 查询条件参数  $findParam
     * @return 查询结果
     */
    public function findDeliveryShortOrder($findParam){
        global $db;
        $sql = "SELECT order_id FROM t_delivery_order_shortage 
                WHERE customer_id=:customer_id
                AND delivery_order_code=:delivery_order_code 
                AND order_type=:order_type
                AND done_flag=0;";
        $model = $db->prepare($sql);
        $model->bindParam(':customer_id', $findParam['ownerCode']);
        $model->bindParam(':delivery_order_code', $findParam['orderCode']);
        $model->bindParam(':order_type', $findParam['orderType']);
        $model->execute();
        return $model->fetchAll(PDO::FETCH_ASSOC);
    }
    
    /**
     * 根据取消订单单据类型判断订单表
     * @param 单据类型  $orderType
     * @return 表名
     */
    public function judgeOrderType($orderType){
        $inType = array('B2BRK','SCRK','LYRK','CCRK','CGRK','DBRK','QTRK','XTRK','HHRK','THRK','LJRK');
        $outType = array('PTCK','DBCK','B2BCK','QTCK','CGTH');
        $deliType = array('JYCK','HHCK','BFCK');
        if (in_array($orderType, $inType)) {
            return 'inbound';
        } else if (in_array($orderType, $outType)) {
            return 'outbound';
        } else if (in_array($orderType, $deliType)) {
            return 'delivery_order';
        } else {
            return 'store_process_order';
        }
    }
    
    /**
     * 根据wms返回消息进行相关处理
     * @param wms返回消息  $response
     * @param 请求参数         $params
     * @return 取消结果
     */
    private function dealWmsResponse($response,$params) {
        global $db;
        if (!empty($response)) {
            if ($response['flag'] == 'success'){
                $partTbName = $this->judgeOrderType($params['orderType']);
                
                $column_arr = $this->get_dataBase_relation('order_cancel');
                $column_key_str = implode(',', array_values($column_arr)) . ', order_id, create_time';
                $this->cancelOrder($partTbName, $params, $column_arr, $column_key_str);
                return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
            } else {
                return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
            }
        } else {
            return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
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
        global $db;
        $findTable = 't_'. $tableName . '_info';
        $addTable = 't_' . $tableName . '_cancel';
        if ($tableName == 'store_process_order') {
            $sql = "SELECT process_id 
                    FROM $findTable 
                    WHERE process_order_code = :process_order_code 
                    AND customer_id = :customer_id 
                    AND warehouse_code = :warehouse_code 
                    AND is_valid = 1";
            $model = $db->prepare($sql);
            $model->bindParam(':process_order_code', $params['orderCode']);
            $model->bindParam(':customer_id', $params['ownerCode']);
            $model->bindParam(':warehouse_code', $params['warehouseCode']);
        }elseif($tableName == 'delivery_order'){
            $sql = "SELECT delivery_id 
                    FROM $findTable 
                    WHERE delivery_order_code = :delivery_order_code 
                    AND customer_id = :customer_id 
                    AND warehouse_code = :warehouse_code 
                    AND is_valid = 1";
            $model = $db->prepare($sql);
            $model->bindParam(':delivery_order_code', $params['orderCode']);
            $model->bindParam(':customer_id', $params['ownerCode']);
            $model->bindParam(':warehouse_code', $params['warehouseCode']);
        } else {
            $sql = "SELECT order_id 
                    FROM $findTable 
                    WHERE order_no = :order_no 
                    AND customer_id = :customer_id 
                    AND warehouse_code = :warehouse_code 
                    AND is_valid = 1";
            $model = $db->prepare($sql);
            $model->bindParam(':order_no', $params['orderCode']);
            $model->bindParam(':customer_id', $params['ownerCode']);
            $model->bindParam(':warehouse_code', $params['warehouseCode']);
        }
        $model->execute();
        $rs = $model->fetch(PDO::FETCH_ASSOC);

        if ($tableName == 'store_process_order') {
            $orderId = $rs['process_id'];
        } elseif ($tableName == 'delivery_order') {
            $orderId = $rs['delivery_id'];
        } else {
            $orderId = $rs['order_id'];
        }
    
        $column_value_str = ":" . implode(",:", array_values($column_arr)) . ",'{$orderId}',now()";
        $sql = "INSERT INTO $addTable({$column_key_str}) VALUES({$column_value_str})";
        $model = $db->prepare($sql);
        $values = array(); 
        foreach ($column_arr as $k => $v) {
            $values[':' . $v] = empty($params[$k]) ? '' : $params[$k] ;
        }
        $model->execute($values);
    
        if ($tableName == 'store_process_order') {
            $sql = "UPDATE $findTable SET order_status='CANCELED',is_valid = 0 where process_id='{$orderId}'";
        } elseif ($tableName == 'delivery_order') {
            $sql = "UPDATE $findTable SET order_status='CANCELED',is_valid = 0 where delivery_id='{$orderId}'";
        } else {
            $sql = "UPDATE $findTable SET order_status='CANCELED',is_valid = 0 where order_id='{$orderId}'";
        }
        $model = $db->prepare($sql);
        $model->execute();
    }
}