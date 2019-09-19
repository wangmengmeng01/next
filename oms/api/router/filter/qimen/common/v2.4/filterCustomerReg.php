<?php
/**
 * 奇门用户注册接口过滤类
 * @author Renee
 *
 */
class filterCustomerReg extends msg{
    public function reg(&$requestData) {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        if (empty($requestData['qmWarehouseCode'])) {
            return $this->outputQimen('failure', '奇门仓储编码不能为空', 'S003');
        }
        if (empty($requestData['customerInfo']['customerid'])) {
            return $this->outputQimen('failure', '用户编码不能为空', 'S003');
        }
        if (empty($requestData['customerInfo']['customerName'])) {
            return $this->outputQimen('failure', '货主名称(公司名称)不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>