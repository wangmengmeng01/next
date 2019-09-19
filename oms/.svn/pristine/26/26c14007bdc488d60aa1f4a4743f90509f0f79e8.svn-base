<?php
/**
 * 奇门仓库查询接口
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpWarehouseQuery extends erpRequest{
    public function reg($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            //转发给奇门
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'failure' && $response['code'] == 'E001') {
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                } else {
                    qimen_service::$_queryFlag = true;
                    return $this->msgObj->outputQimen($response['flag'], $response['message'], $response['code'], $response['addon']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', '失败：接口调用失败', 'S003');
            }
        }
    }
}
?>