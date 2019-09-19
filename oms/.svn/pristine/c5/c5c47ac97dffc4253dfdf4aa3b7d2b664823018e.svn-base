<?php
/**
 * 奇门发货单波次通知过滤类
 * @author Renee
 *
 */
class filterWaveNumReport extends msg{
    public function report(&$requestData) {
        if (empty($requestData)) {
            return $this->outputQimen('failure','body中数据不能为空','S003');
        }
        if (empty($requestData['waveNum'])) {
            return $this->outputQimen('failure','波次号不能为空','S003');
        }
        if (empty($requestData['orders']['order'])) {
            return $this->outputQimen('failure','单据信息不能为空','S003');
        } else {
            if (empty($requestData['orders']['order'][0])) {
                $requestData['orders']['order'] = array($requestData['orders']['order']);
            }
            foreach ($requestData['orders']['order'] as $order_v) {
                if (empty($order_v['deliveryOrderCode'])) {
                    return $this->outputQimen('failure','出库单号不能为空','S003');
                } 
            }
        }
        return $this->outputQimen('success');
    }
}
?>