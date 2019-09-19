<?php
/**
 * 缺货订单接收接口
 * @author Renee
 *
 */

require API_ROOT . '/router/interface/oms/qimen/meituan/erpRequest.php';
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
                    //转发数据给erp
                    $response = $this->send();

                    //解析返回的数据
                    if (!empty($response)) {
                        if ($response['flag'] == 'success') {
                            return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                        }   else {
                            return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                        }
                    } else {
						return $this->msgObj->outputQimen('failure', '无返回！', 'S002');
					}
                } catch (Exception $e) {
                    return $this->msgObj->outputQimen('failure', $e->getMessage(), $e->getCode());
                }
            }
        }
    }

}