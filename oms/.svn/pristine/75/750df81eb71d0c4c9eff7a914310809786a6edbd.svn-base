<?php
/**
 * 订单流水通知操作类
 * wms => oms => erp
 * @author Renee
 * 
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpOrderProcessReport extends erpRequest
{
    /**
     * 订单流水通知信息
     * @param $params
     */
    public function report($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    $this->insert_order_process_report($params);
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
     * 插入订单流水通知信息
     * @param $order
     * @param $process
     */
    public function insert_order_process_report($resultInfo) {
        global $db;
        $order = $resultInfo['order'];
        $process = $resultInfo['process'];
        $column_arr = $this->get_dataBase_relation('order_process_report');
        $column_key_str = implode(',', array_values($column_arr)) . ',create_time';
        
        $column_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
        $sql = "INSERT INTO t_order_process_record({$column_key_str}) VALUES({$column_value_str})";
        $model = $db->prepare($sql);
        $values = array();
        foreach ($column_arr as $o_k => $o_v) {
            $values[':' . $o_v] = empty($order[$o_k]) ? '' : $order[$o_k] ;
        }
        unset($column_arr['orderCode']);
        unset($column_arr['orderId']);
        unset($column_arr['orderType']);
        unset($column_arr['warehouseCode']);
        foreach ($column_arr as $p_k => $p_v) {
            $values[':' . $p_v] = empty($process[$p_k]) ? '' : $process[$p_k] ;
        }
        $model->execute($values);
    }
    
}
?>