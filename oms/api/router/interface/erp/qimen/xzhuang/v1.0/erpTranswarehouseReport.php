<?php
/**
 * 转仓通知操作类
 * 业务在oms进行转仓操作  => oms => erp
 * @author Renee
 * 
 */
require API_ROOT . '/router/interface/erp/qimen/xzhuang/erpRequest.php';
class erpTranswarehouseReport extends erpRequest
{
    /**
     * 转仓通知信息
     * @param $params
     */
    public function report($params) {
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