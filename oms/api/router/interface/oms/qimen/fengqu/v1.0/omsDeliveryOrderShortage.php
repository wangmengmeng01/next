<?php
/**
 * 缺货订单接收接口
 * @author Renee
 *
 */

require API_ROOT . '/router/interface/oms/qimen/fengqu/erpRequest.php';
class omsDeliveryOrderShortage extends erpRequest
{
    public function get($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            if (empty($params['deliveryOrder']) || empty($params['orderLines']['orderLine'])) {
                return $this->msgObj->outputQimen('failure', '失败：请求的数据不完整', 'S003');
            } else {
                try {
                    global $db;
                    //查询有无该发货单
                    $queryInfoSql = "SELECT delivery_id,order_type,deliv_create_time,create_time FROM t_delivery_order_info WHERE delivery_order_code=:delivery_order_code AND customer_id=:customer_id";
                    $queryInfoModel = $db->prepare($queryInfoSql);
                    $queryInfoModel->bindParam(':delivery_order_code',$params['deliveryOrder']['deliveryOrderCode']);
                    $queryInfoModel->bindParam(':customer_id',$params['deliveryOrder']['ownerCode']);
                    $queryInfoModel->execute();
                    $deliveryOrderInfo = $queryInfoModel->fetch(PDO::FETCH_ASSOC);
                    if (!empty($deliveryOrderInfo)) {
                        //转发数据给erp
                        $response = $this->send();

                        //解析返回的数据
                        if (!empty($response)) {
                            if ($response['flag'] == 'success') {
                                return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                            }   else {
                                return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                            }
                        }
                    } else {
                        return $this->msgObj->outputQimen('failure', '无此发货单！', 'S003');
                    }
                } catch (Exception $e) {
                    return $this->msgObj->outputQimen('failure', $e->getMessage(), $e->getCode());
                }
            }
        }
    }

}