<?php
/**
 * 库存盘点通知操作类
 * wms => oms => erp
 * @author Renee
 * 
 */
require API_ROOT . '/router/interface/erp/qimen/xianyu/erpRequest.php';
class erpInventoryReport extends erpRequest
{
    /**
     * 库存盘点通知信息
     * @param $params
     */
    public function report($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    $this->insert_inventory_check($params);
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
     * 插入库存盘点通知表
     * @param  $params
     */
    public function insert_inventory_check($params){
        global $db;
        
        $column_arr = $this->get_dataBase_relation('inventory_check_record');
        $column_key_arr = implode(',', array_values($column_arr)) . ',create_time';
        $column_value_arr = ':' . implode(',:', array_values($column_arr)) . ',now()';
        $sql = "INSERT INTO t_inventory_check_record({$column_key_arr}) VALUES({$column_value_arr})";
        $model = $db->prepare($sql);
        $values = array();
        foreach ($column_arr as $k => $v) {
            $values[':' . $v] = empty($params[$k]) ? '' : $params[$k] ;
        }
        $model->execute($values);

        $column_item_arr = $this->get_dataBase_relation('inventory_product_check_record');
        $column_item_key_arr = implode(',', array_values($column_item_arr)) . ',inventory_id,create_time';
        $column_item_value_arr = ':' . implode(',:', array_values($column_item_arr)) . ",'{$db->lastInsertID()}',now()";
        $sql = "INSERT INTO t_inventory_check_product_record({$column_item_key_arr}) VALUES({$column_item_value_arr})";
        $model = $db->prepare($sql);
        $values = array();
        $item = $params['items']['item'];
        if (empty($item[0])) {
            $item = array($item);
        }
        foreach ($item as $v_i) {
            foreach ($column_item_arr as $k_i_c => $v_i_c) {
                if ($k_i_c == 'inventoryType') {
                    $values[':' . $v_i_c] = empty($v_i[$k_i_c]) ? 'ZP' : $v_i[$k_i_c] ;
                } else {
                    $values[':' . $v_i_c] = empty($v_i[$k_i_c]) ? '' : $v_i[$k_i_c] ;
                }
                
            } 
            $model->execute($values);
        }
    }
}
?>