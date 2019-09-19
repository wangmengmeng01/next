<?php
/**
 * Notes:贝贝入库单创建过滤类
 * Date: 2019/6/19
 * Time: 16:38
 */
class filterEntryOrderCreate extends msg
{
    public function create(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'body中数据不能为空');
        }
        if (empty($requestData['billId'])) {
            return $this->outputBeibei(false, '', '入库单号不能为空');
        }
        if (empty($requestData['billType'])) {
            return $this->outputBeibei(false, '', '单据类型不能为空');
        }
        if (empty($requestData['company'])) {
            return $this->outputBeibei(false, '', '货主编码不能为空');
        }
        if (empty($requestData['warehouse'])) {
            return $this->outputBeibei(false, '', '仓库编码不能为空');
        }
        if (empty($requestData['opTime'])) {
            return $this->outputBeibei(false, '', '操作时间不能为空');
        } elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['opTime'])) {
            return $this->outputBeibei(false, '', '操作时间格式错误');
        }

        //商品详情信息
        $details = $requestData['details'];
        if (!$details) {
            return $this->outputBeibei(false, '', '商品详情不能为空');
        }
        if (empty($details[0])) {
            $details =  array($details);
        }
        foreach ($details as $v) {
            if (!$v['sku']) {
                return $this->outputBeibei(false, '', '商品编码sku不能为空');
            }
            if (!$v['quantity']) {
                return $this->outputBeibei(false, '', '应收数量不能为空');
            }
            if (!$v['inventoryStatus']) {
                return $this->outputBeibei(false, '', '是否良品inventoryStatus不能为空');
            }
        }
        return $this->outputBeibei(true);
    }
}
