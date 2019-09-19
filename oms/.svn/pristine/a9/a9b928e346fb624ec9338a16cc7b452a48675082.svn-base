<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/21 15:29
 * @description: 库存状态批次调整接口
 */

require API_ROOT . '/router/interface/erp/storage/common/cnRequest.php';


class erpInventoryAdjustUpload  extends cnRequest{
    public function create($data,$arrData){
        if($this->save($arrData)){
            return   $this->send($data);
        }else{
            return  $this->msgObj->outputCnStorage(false,'失败:保存数据系统异常','s003');
        }
    }

    //保存库存盘点数据
    protected function save($params){

        try{


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
            $item = $params['itemList']['item'];
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
        } catch(Exception $e){
            return false;

        }

        return true;
    }
}