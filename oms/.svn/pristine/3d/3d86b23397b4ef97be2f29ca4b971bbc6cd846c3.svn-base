<?php
/**
 * Notes:贝贝入库单回传过滤类
 * Date: 2019/6/19
 * Time: 16:39
 */
class filterEntryOrderConfirm extends msg
{
    public function confirm(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'body中数据不能为空');
        }
        if (empty($requestData['entryOrder']['billId'])) {
            return $this->outputBeibei(false, '', '单据编号不能为空');
        }
        if (empty($requestData['entryOrder']['billType'])) {
            return $this->outputBeibei(false, '', '单据类型不能为空');
        }
        if (empty($requestData['entryOrder']['opTime'])) {
            return $this->outputBeibei(false, '', '操作时间不能为空');
        } elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['entryOrder']['opTime'])) {
            return $this->outputBeibei(false, '', '操作时间格式错误');
        }
        if (empty($requestData['entryOrder']['warehouse'])) {
            return $this->outputBeibei(false, '', '仓库编码不能为空');
        }

        //商品详情信息
        $details = $requestData['entryOrder']['details'];
        if (!$details) {
            return $this->outputBeibei(false, '', '商品详情不能为空');
        }
        if (empty($details[0])) {
            $details =  array($details);
        }
        foreach ($details as $v) {
            if (!$v['lineNo']) {
                return $this->outputBeibei(false, '', '行号不能为空');
            }
            if (!$v['sku']) {
                return $this->outputBeibei(false, '', '商品编码不能为空');
            }
            if (!$v['skuDesc']) {
                return $this->outputBeibei(false, '', '商品名称不能为空');
            }
            if (!$v['quantity']) {
                return $this->outputBeibei(false, '', '数量不能为空');
            }
            if (!$v['lot']) {
                return $this->outputBeibei(false, '', '单据单号不能为空');
            }
            if (!$v['productionLot']) {
                return $this->outputBeibei(false, '', '批次号不能为空');
            }
            if (!$v['inventoryStatus']) {
                return $this->outputBeibei(false, '', '是否良品不能为空');
            }
            if (!$v['operator']) {
                return $this->outputBeibei(false, '', '操作人不能为空');
            }
            if (!$v['company']) {
                return $this->outputBeibei(false, '', '货主编码不能为空');
            } else {
                $rs = OmsDatabase::$oms_db->fetchOne('customer_id', 't_qimen_customer_bind', 'customer_id = :customer_id AND is_valid = 1', array(':customer_id' => $v['company']));
                if (empty($rs)) {
                    return $this->outputBeibei(false, '', '货主编码不存在或无效');
                } elseif ($rs['customer_id'] != $v['company']) {
                    return $this->outputBeibei(false, '', '货主编码大小写错误');
                }
            }
        }
        return $this->outputBeibei(true);
    }
}