<?php
/**
 * 奇门用户注册接口处理类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpCustomerReg extends erpRequest{
    public function reg($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure','失败：请求的数据为空','S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                } else {
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', '失败：接口调用失败', 'S003');
            }
        }
    }
}