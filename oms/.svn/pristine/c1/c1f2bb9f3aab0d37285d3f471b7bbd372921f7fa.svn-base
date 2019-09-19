<?php
/**
 * 库存异动通知操作类
 * wms => oms => erp
 * @author Renee
 * 
 */
require API_ROOT . '/router/interface/erp/qimen/fx/erpRequest.php';
class erpStockChangeReport extends erpRequest
{
    /**
     * 库存异动通知信息
     * @param $params
     */
    public function report($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    $this->insert_stock_change_record($params);
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
     * 插入库存通知信息
     * @param $order
     * @param $process
     */
    public function insert_stock_change_record($params) {
        $item = $params['items']['item'];
        global $db;
        
        $column_arr = $this->get_dataBase_relation('stock_change_record');
        $column_key_str = implode(',', array_values($column_arr)) . ',create_time';
        
        $column_batch_arr = $this->get_dataBase_relation('stock_change_batch_report');
        $column_batch_key_str = implode(',', array_values($column_batch_arr)) . ',change_id,create_time';
        
        if (empty($item[0])) {
            $item = array($item);
        }
        foreach ($item as $v) {
            $column_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
            $sql = "INSERT INTO t_stock_change_record({$column_key_str}) VALUES({$column_value_str})";
            $model = $db->prepare($sql);
            $values = array();
            foreach ($column_arr as $s_k=>$s_v) {
                $values[':' . $s_v] = empty($v[$s_k]) ? '' : $v[$s_k] ;
            }
            $model->execute($values);
            $changeId = $db->lastInsertID();
            
            $batch = $v['batchs']['batch'];
            if (!empty($batch)) {
                $column_batch_value_str = ':' . implode(',:', array_values($column_batch_arr)) . ",'{$changeId}',now()";
                $sql2 = "INSERT INTO t_stock_change_batch_record({$column_batch_key_str}) VALUES({$column_batch_value_str})";
                $model2 = $db->prepare($sql2);
                $values2 = array();
                if (empty($batch[0])) {
                    $batch = array($batch);
                }
                foreach ($batch as $b_v) {
                    foreach ($column_batch_arr as $k2 => $v2) {
                        $values2[':' . $v2] = empty($b_v[$k2]) ? '' : $b_v[$k2];
                    }
                    $model2->execute($values2);
                }
            }
        }
        
    }
}
?>