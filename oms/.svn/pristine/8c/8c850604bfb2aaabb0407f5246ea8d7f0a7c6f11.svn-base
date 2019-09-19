<?php
/**
 * 奇门仓库更新接口处理类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpWarehouseUpdate extends erpRequest {
    public function update($params) {
        if (!empty($params)) {
            $response = $this->send();
            if (!empty($response)) {
                global $db;
                $sql = "SELECT warehouse_id FROM t_base_warehouse WHERE qm_warehouse_code=:qm_warehouse_code AND is_valid=1";
                $model = $db->prepare($sql);
                $model->bindParam(':qm_warehouse_code', $params['qmWarehouseCode']);
                $model->execute();
                $warehouseId = $model->fetchColumn();

                if (!empty($warehouseId)) {
                    $column_warehouse_arr = $this->get_dataBase_relation('warehouse_update');
                    $column_warehouse_contact_arr = $this->get_dataBase_relation('warehouse_update_contact_info');
                    $column_warehouse_addr_arr = $this->get_dataBase_relation('warehouse_update_addr_info');
                    $updateStr = '';
                    $values = array();
                    foreach ($column_warehouse_addr_arr as $addrKey=>$addrVal) {
                        $updateStr .= $addrVal . '=:' . $addrVal . ',';
                        $values[':'.$addrVal] = empty($params['warehouseInfo'][$addrKey]) ? '' : $params['warehouseInfo'][$addrKey] ;
                    }
                    foreach ($column_warehouse_contact_arr as $contactKey=>$contactVal) {
                        $updateStr .= $contactVal . '=:' . $contactVal .',';
                        $values[':'.$contactVal] = empty($params['warehouseInfo']['contactInfo'][$contactKey]) ? '' : $params['warehouseInfo']['contactInfo'][$contactKey] ;
                    }
                    foreach ($column_warehouse_arr as $warehouseKey=>$warehouseVal) {
                        $updateStr .= $warehouseVal . '=:' . $warehouseVal . ',';
                        $values[':'.$warehouseVal] = empty($params[$warehouseKey]) ? '' : $params[$warehouseKey] ;
                    }
                    $updateStr = substr($updateStr, 0, -1);
                    $updateSql = "UPDATE t_base_warehouse SET " . $updateStr . " WHERE warehouse_id=$warehouseId";
                    $updateModel = $db->prepare($updateSql);
                    $updateModel->execute($values);
                    return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                }                
            } else {
                return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
            }
        } else {
            return $this->msgObj->outputQimen('failure', '失败：接口调用失败', 'S003');
        }
    }
}
?>