<?php
/**
 * 奇门仓库更新接口过滤类
 * @author Renee
 *
 */
class filterWarehouseUpdate extends msg {
    public function update(&$requestData) {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        } 
        if (empty($requestData['qmWarehouseCode'])) {
            return $this->outputQimen('failure', '奇门仓储编码不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>