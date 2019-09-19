<?php
/**
 * Notes: 贝贝天舟-入库单更新过滤类
 * User: wj
 * Date: 2019/7/25
 * Time: 14:16
 */
class filterEntryorderUpdate extends msg
{
    public function update($requestData)
    {
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'data中数据不能为空');
        }
        if (empty($requestData['billId'])) {
            return $this->outputBeibei(false, '', '入库单号不能为空');
        }
        if (empty($requestData['warehouse'])) {
            return $this->outputBeibei(false, '', '仓库编码不能为空');
        }
        if (empty($requestData['updator'])) {
            return $this->outputBeibei(false, '', '更新人不能为空');
        }
        if (empty($requestData['updateFiled'])) {
            return $this->outputBeibei(false, '', '要更新的属性不能为空');
        }
        if (empty($requestData['updateFiled']['subscribeArrivalTime'])) {
            return $this->outputBeibei(false, '', '预约到货时间不能为空');
        }
        return $this->outputBeibei(true, '', '成功');
    }
}