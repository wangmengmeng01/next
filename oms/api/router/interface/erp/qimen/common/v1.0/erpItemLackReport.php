<?php
/**
 * 发货单缺货通知操作类
 * wms => oms => erp
 * @author Renee
 * 
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpItemLackReport extends erpRequest
{
    /**
     * 发货单缺货通知信息
     * @param $params
     */
    public function report($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    $this->insert_item_lack_report($params);
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
     * 插入发货单缺货通知信息
     * @param $order
     * @param $process
     */
    public function insert_item_lack_report($params) {
        global $db;
        $item = $params['items']['item'];
        $customerId = qimen_service::$_customerId;
        $column_arr = $this->get_dataBase_relation('item_lack_record');
        $column_key_str = implode(',', array_values($column_arr)) . ',create_time';
        
        $column_detail_arr = $this->get_dataBase_relation('item_lack_product_record');
        $column_detail_key_arr = implode(',', array_values($column_detail_arr)) . ',deliv_lack_id,create_time';
        
        $column_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
        $sql = "INSERT INTO t_item_lack_record({$column_key_str}) VALUES({$column_value_str})";
        $model = $db->prepare($sql);
        $values = array();
        foreach ($column_arr as $k => $v) {
            $values[':' . $v] = empty($params[$k]) ? '' : $params[$k] ;
        }
        $values[':customer_id'] = $customerId;
        $model->execute($values);
        
        $delivLackId = $db->lastInsertID(); 
        $column_detail_value_arr = ':' . implode(',:', array_values($column_detail_arr)) . ",'{$delivLackId}',now()";
        $p_sql = "INSERT INTO t_item_lack_product_record({$column_detail_key_arr}) VALUES({$column_detail_value_arr})";
        $model = $db->prepare($p_sql);
        $i_values = array();
        
        if (empty($item[0])) {
            $item = array($item);
        }
        foreach ($item as $i_v) {
            foreach ($column_detail_arr as $i_k_c => $i_v_c) {
                $i_values[':' . $i_v_c] = empty($i_v[$i_k_c]) ? '' : $i_v[$i_k_c] ;
            }
            $model->execute($i_values);
        }
    }
    
}
?>