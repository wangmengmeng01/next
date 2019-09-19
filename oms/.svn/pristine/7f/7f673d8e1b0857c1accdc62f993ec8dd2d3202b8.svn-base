<?php
/**
 * 缺货订单推送校验类
 * @author Renee
 *
 */
class filterDeliveryOrderShortage extends msg{
    public function get(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        if (empty($requestData['deliveryOrder']['deliveryOrderCode'])) {
            return $this->outputQimen('failure', '订单号不能为空', 'S003');
        }
        if (empty($requestData['deliveryOrder']['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        if (empty($requestData['deliveryOrder']['ownerCode'])) {
            return $this->outputQimen('failure', '货主编码不能为空', 'S003');
        }
        if (empty($requestData['orderLines']['orderLine'])) {
            return $this->outputQimen('failure', '缺货商品明细不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}