<?php
/**
 * 缺货订单接收接口
 * @author Renee
 *
 */
require_once API_ROOT .'/router/interface/oms/qimen/common/omsRequest.php';
class omsDeliveryOrderShortage extends omsRequest {
    public function get($params){
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            if (empty($params['deliveryOrder']) || empty($params['orderLines']['orderLine'])) {
                return $this->msgObj->outputQimen('failure', '失败：请求的数据不完整', 'S003');
            } else {
                try {
                    qimen_service::$_method = 'taobao.qimen.itemlack.report';//发货单缺货接口

                    if (empty($params['orderLines']['orderLine'])) {
                        return $this->msgObj->outputQimen('failure', '失败：请求的数据明细为空', 'S003');
                    } else {
                        $xmlStr  = '<?xml version="1.0" encoding="utf-8"?><request>';
                        $xmlStr .= '<warehouseCode>'. $params['deliveryOrder']['warehouseCode'] .'</warehouseCode>';
                        $xmlStr .= '<deliveryOrderCode>'. $params['deliveryOrder']['deliveryOrderCode'] .'</deliveryOrderCode>';
                        $xmlStr .= '<createTime>'. date("Y-m-d H:i:s") .'</createTime>';
                        $xmlStr .= '<outBizCode>'. $params['deliveryOrder']['outBizCode'] .'</outBizCode>';
                        $xmlStr .= '<items>';

                        if (empty($params['orderLines']['orderLine'][0])) {
                            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
                        }
                        foreach ($params['orderLines']['orderLine'] as $item) {
                            $xmlStr .= '<item>';
                            $xmlStr .= '<itemCode>' . $item['itemCode'] . '</itemCode>';
                            $xmlStr .= '<planQty>' . $item['planQty'] . '</planQty>';
                            $xmlStr .= '<lackQty>' . $item['lackQty'] . '</lackQty>';
                            $xmlStr .= '</item>';
                        }
                        $xmlStr .= '</items>';
                        $xmlStr .= '</request>';

                        qimen_service::$_data = $xmlStr;

                        $response = $this->send();
                        if (empty($response)) {
                            return $this->msgObj->outputQimen('failure', '失败：返回数据为空', 'S002');
                        } else {
                            return $this->msgObj->outputQimen($response['flag'], $response['message'], $response['code'], $response['addon']);
                        }
                    }
                } catch (Exception $e) {
                    return $this->msgObj->outputQimen('failure', $e->getMessage(), $e->getCode());
                }
            }
        }
    }
}