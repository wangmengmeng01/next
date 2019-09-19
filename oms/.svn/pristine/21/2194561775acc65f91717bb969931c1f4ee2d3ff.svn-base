<?php
/**
 * 家乐福库存信息接收接口处理类
 * User: Renee
 * Date: 2018/8/10
 * Time: 10:31
 */
require API_ROOT . '/router/interface/erp/qimen/xianyu/erpRequest.php';
class erpInventorySync extends erpRequest
{
    public function sync($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            //转发数据给erp
            $response = $this->send();
            //解析返回的数据
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                } else {
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
            }
        }
    }
}