<?php
/**
 * 奇门单据挂起(恢复)接口
 * @author Renee
 *
 */
class filterOrderPending extends msg{
    public function operate(&$requestData) {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        if (empty($requestData['actionType'])) {
            return $this->outputQimen('failure', '操作类型不能为空', 'S003');
        }
        if (empty($requestData['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        if (empty($requestData['orderCode'])) {
            return $this->outputQimen('failure', '单据编码不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>