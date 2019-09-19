<?php
/**
 * 奇门发货单创建结果通知接口(批量)
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpDeliveryOrderBatchCreateAnswer extends erpRequest{
    public function answer($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                } else {
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', 'erp接口调用失败', 'S007');
            }
        }
    }
}
?>