<?php
/**
 * 纸品分销目的分拨和仓编码查询接口过滤类
 * User: Renee
 * Date: 2018/11/21
 * Time: 10:46
 */
class filterFxQueryWarehouseInfo extends msg
{
    public function query(&$requestData)
    {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空！', 'S003');
        }

        if (empty($requestData['receiverInfo']['province'])) {
            return $this->outputQimen('failure', '收件人省份不能为空！', 'S003');
        }
        if (empty($requestData['receiverInfo']['city'])) {
            return $this->outputQimen('failure', '收件人城市不能为空！', 'S003');
        }
        if (empty($requestData['receiverInfo']['detailAddress'])) {
            return $this->outputQimen('failure', '收件人详细地址不能为空！', 'S003');
        }

        return $this->outputQimen('success');
    }
}