<?php
/**
 * 奇门仓库注册接口
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpWarehouseReg extends erpRequest{
    public function reg($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            //转发给奇门
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    global $db;
                    $xmlObj = new xml();
                    $return_msg = $response['addon']['return_msg'];
                    $returnMsg = $xmlObj->xmlStr2array($return_msg);
                    $qmWarehouseCode = $returnMsg['qmWarehouseCode'];
                    $selectSql = 'SELECT warehouse_id FROM t_base_warehouse WHERE warehouse_code=:warehouse_code AND qm_warehouse_code=:qm_warehouse_code AND wms_url = :wms_url;';
                    $selectModel = $db->prepare($selectSql);
                    $selectModel->bindParam(':warehouse_code', $params['warehouseCode']);   
                    $selectModel->bindParam(':qm_warehouse_code', $qmWarehouseCode);
                    $selectModel->bindParam(':wms_url', $params['wmsURL']);
                    $selectModel->execute();
                    $warehouseIdArr = $selectModel->fetchAll(PDO::FETCH_ASSOC);
                    //判断t_base_warehouse表中是否有该仓库信息
                    if (empty($warehouseIdArr)) {
                        $this->addWarehouse($params,$qmWarehouseCode);
                    } else {
                        $this->updateWarehouse($warehouseIdArr);
                        $this->addWarehouse($params,$qmWarehouseCode);
                    }
                    return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                } else {
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', '失败：接口调用失败', 'S003');
            }
        }
    }
    
    /**
     * 添加仓库信息
     * @param 仓库信息 $params
     */
    public function addWarehouse($params,$qmWarehouseCode) {
        global $db;
        $column_warehouseInfo_arr = $this->get_dataBase_relation('warehouse_reg');
        $column_key_warehouseInfo = implode(',', array_values($column_warehouseInfo_arr)) ;
        $column_value_warehouseInfo = ':' . implode(',:',array_values($column_warehouseInfo_arr)) ;
        
        $column_warehouseContact_arr = $this->get_dataBase_relation('warehouse_reg_contact_info');
        $column_key_warehouseContact = implode(',', array_values($column_warehouseContact_arr));
        $column_value_warehouseContact = ':' . implode(',:', array_values($column_warehouseContact_arr));
        
        $column_warehouseAddr_arr = $this->get_dataBase_relation('warehouse_reg_addr_info');
        $column_key_warehouseAddr = implode(',', array_values($column_warehouseAddr_arr)) . ',create_time';
        $column_value_warehouseAddr = ':' . implode(',:', array_values($column_warehouseAddr_arr)) . ",now()";
        
        $sql = "INSERT INTO t_base_warehouse({$column_key_warehouseInfo},{$column_key_warehouseContact},{$column_key_warehouseAddr}) VALUES({$column_value_warehouseInfo},{$column_value_warehouseContact},{$column_value_warehouseAddr})";
        $values = array();
        
        foreach ($column_warehouseContact_arr as $contact_k=>$contact_v) {
            $values[':' . $contact_v] = empty($params['warehouseInfo']['contactInfo'][$contact_k]) ? '' : $params['warehouseInfo']['contactInfo'][$contact_k] ;
        }
        foreach ($column_warehouseAddr_arr as $addr_k=>$addr_v) {
            $values[':' . $addr_v] = empty($params['warehouseInfo'][$addr_k]) ? '' : $params['warehouseInfo'][$addr_k] ;
        }
        foreach ($column_warehouseInfo_arr as $warehouse_k=>$warehouse_v) {
            if ($warehouse_v == 'qm_warehouse_code') {
                $values[':' . $warehouse_v] = $qmWarehouseCode;
            } else {
                $values[':' . $warehouse_v] = empty($params[$warehouse_k]) ? '' : $params[$warehouse_k] ;
            }
        }
        
        $model = $db->prepare($sql);
        $model->execute($values);
    }
    
    /**
     * 更新仓库有效性
     * @param 仓库id $warehouseIdArr
     */
    public function updateWarehouse($warehouseIdArr) {
        global $db;
        $sql = "UPDATE t_base_warehouse SET is_valid=0 WHERE warehouse_id=:warehouse_id ";
        foreach ($warehouseIdArr as $warehouseIdVal) {
            $model = $db->prepare($sql);
            $model->bindParam(':warehouse_id', $warehouseIdVal['warehouse_id']);
            $model->execute();
        }
    }
}
?>