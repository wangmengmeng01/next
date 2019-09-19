<?php
/**
 * Description: 贝贝天舟-单据取消接口
 * User: XL
 * Date: 2019/6/19 0019 16:39
 */

class filterBillCancel extends msg
{
    public function cancel($requestData)
    {
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'data中的数据不能为空');
        }
        if (empty($requestData['billId'])) return $this->outputBeibei(false, '', '单据编号不能为空');

        if (empty($requestData['billType'])) return $this->outputBeibei(false, '', '单据类型不能为空');

        if (empty($requestData['company'])) return $this->outputBeibei(false, '', '货主不能为空');

        if (empty($requestData['warehouse'])) return $this->outputBeibei(false, '', '仓库编码不能为空');

        if (empty($requestData['opTime'])) return $this->outputBeibei(false, '', '操作时间不能为空');

        return $this->outputBeibei(true, '', '成功');
    }
}