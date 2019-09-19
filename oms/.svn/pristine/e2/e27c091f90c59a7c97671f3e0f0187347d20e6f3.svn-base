<?php
/**
 * 退货入库单创建操作类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/xzhuang/wmsRequest.php';

class wmsReturnOrderCreate extends wmsRequest
{

    /**
     * 创建退货入库单
     * @param $params         
     * @return array
     */
    public function create($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            // 转发数据给wms
            $response = $this->send();
            //解析返回的数据
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    $this->insert_return_order($params);
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
     * 添加退货入库单信息
     * @param  $params
     */
    public function insert_return_order($params){
        $returnOrder = $params['returnOrder'];
        $senderInfo = $returnOrder['senderInfo'];
        global $db;
        // 获取入库单单头参数与数据库字段对应关系
        $column_arr1 = $this->get_dataBase_relation('return_order_create');
        $column_arr2 = $this->get_dataBase_relation('return_order_sender_create');//发件人
        
        $title_key_str1 = implode(',', array_values($column_arr1)) . ',';
        $title_key_str2 = implode(',', array_values($column_arr2)) . ',create_time';
        
        $title_value_str1 = ':' . implode(',:', array_values($column_arr1)) . ',';
        $title_value_str2 = ':' . implode(',:', array_values($column_arr2)) . ',now()';
        
        // 获取入库单明细参数与数据库字段对应关系
        $column_detail_arr = $this->get_dataBase_relation('return_order_create_detail');
        $detail_key_str = implode(',', array_values($column_detail_arr)) . ',order_id,create_time';
        
        $customerId = qimen_service::$_customerId;
        $this->has_upd_return_order($returnOrder['returnOrderCode'], $returnOrder['orderType'],$customerId, $returnOrder['warehouseCode']);
        $sql = "INSERT INTO t_inbound_info (" . $title_key_str1 . $title_key_str2 . ",customer_id) VALUES (" . $title_value_str1 . $title_value_str2 . ",'" . $customerId . "')";
        $model = $db->prepare($sql);
        
        $values = array();
        foreach ($column_arr2 as $k1 => $v1) {
            $values[':' . $v1] = empty($senderInfo[$k1]) ? '' : $senderInfo[$k1] ;
        }
        
        foreach ($column_arr1 as $k => $v) {
            $values[':' . $v] = empty($returnOrder[$k]) ? '' : $returnOrder[$k] ;
        }
        $model->execute($values);
        
        //写入入库单明细
        if (!empty($params['orderLines']['orderLine'])) {
            $values = array();
            $detail_value_str = ":" . implode(',:', array_values($column_detail_arr)) . ",'{$db->lastInsertID()}',now()";
            $sql = "INSERT INTO t_inbound_detail ({$detail_key_str}) VALUES ({$detail_value_str})";
            $model = $db->prepare($sql);
            
            if (empty($params['orderLines']['orderLine'][0])) {
                $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
            }
            foreach ($params['orderLines']['orderLine'] as $val3) {
                foreach ($column_detail_arr as $k3 => $v3) {
                    if ($k3 == 'inventoryType') {
                        $values[':' . $v3] = empty($val3[$k3]) ? 'ZP' : $val3[$k3] ;
                    } else {
                        $values[':' . $v3] = empty($val3[$k3]) ? '' : $val3[$k3] ;
                    }
                }
                $model->execute($values);
            }
        }
    }
    
    /**
     * 判断是否存在退货入库单号，存在则逻辑删除处理
     * @param $orderNo 订单号         
     * @param $orderType 订单类型 
     * @param $preDeliveryOrderCode 原出库单号
     * @param $warehouseCode  仓库id   
     * @return bool
     */
    public function has_upd_return_order($orderNo, $orderType,$customerId,$warehouseCode)
    {
        $values = array();
        $Ssql = '';
        $Usql = '';
        global $db;
        $values[':order_no'] = $orderNo;
        $values[':order_type'] = $orderType;
        $values[':customer_id'] = $customerId;
        $values[':warehouse_code'] = $warehouseCode;
        $Ssql = 'SELECT COUNT(*) t, order_id FROM `t_inbound_info` WHERE order_no = :order_no AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1;';
        $datas = $db->prepare($Ssql);
        $datas->execute($values);
        $rs = $datas->fetch(PDO::FETCH_ASSOC);
        
        if ($rs['t'] > 0) {
            $Usql = ' UPDATE `t_inbound_info` SET is_valid = 0 WHERE order_no = :order_no AND order_type=:order_type AND customer_id = :customer_id AND warehouse_code=:warehouse_code; ';
            $Usql .= ' UPDATE `t_inbound_detail` SET is_valid = 0 WHERE order_id= :order_id; ';
            $values[':order_id'] = $rs['order_id'];
            $model = $db->prepare($Usql);
            if ($model->execute($values)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}

