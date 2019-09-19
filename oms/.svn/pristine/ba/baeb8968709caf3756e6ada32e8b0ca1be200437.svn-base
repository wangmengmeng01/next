<?php
/**
 * 奇门发货单创建结果通知接口(批量)过滤类
 * @author Renee
 *
 */
class filterDeliveryOrderBatchCreateAnswer extends msg{
    public function answer(&$requestData)
    {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>