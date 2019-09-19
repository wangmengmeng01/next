<?php
/**
 * Notes:贝贝销售退货下传过滤类
 * Date: 2019/6/19
 * Time: 16:40
 */
class filterRmaCreate extends msg
{
    public function create(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'body中数据不能为空');
        }
        if (empty($requestData['header']['returnId'])) {
            return $this->outputBeibei(false, '', '销售退货单号不能为空');
        }
        if (empty($requestData['header']['orderType'])) {
            return $this->outputBeibei(false, '', '订单类型不能为空');
        }
        if (empty($requestData['header']['company'])) {
            return $this->outputBeibei(false, '', '货主编码不能为空');
        }
        if (empty($requestData['header']['createTime'])) {
            return $this->outputBeibei(false, '', '销售退货单生成时间不能为空');
        } elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['header']['createTime'])) {
            return $this->outputBeibei(false, '', '销售退货单生成时间格式错误');
        }
        if (empty($requestData['header']['originalExpressNo'])) {
            return $this->outputBeibei(false, '', '原快递单号不能为空');
        }
        if (!isset($requestData['header']['platformOrderNo'])) {
            return $this->outputBeibei(false, '', '平台单号不能为空');
        }
        if (empty($requestData['header']['refundExpressNo'])) {
            return $this->outputBeibei(false, '', '退货快递单号不能为空');
        }
        if (empty($requestData['header']['senderPhone'])) {
            return $this->outputBeibei(false, '', '手机号码不能为空');
        }
        if (empty($requestData['header']['shippingNo'])) {
            return $this->outputBeibei(false, '', '原出库单号不能为空');
        }
        if (empty($requestData['header']['refundExpressCompany'])) {
            return $this->outputBeibei(false, '', '退货快递公司不能为空');
        }
        if (empty($requestData['header']['gottenRefundId'])) {
            return $this->outputBeibei(false, '', '拼接后的售后单ID不能为空');
        }
        if (empty($requestData['header']['refundWarehouse'])) {
            return $this->outputBeibei(false, '', '退货接收仓库不能为空');
        }
        if (empty($requestData['header']['originalExpressCompany'])) {
            return $this->outputBeibei(false, '', '原快递公司不能为空');
        }
        if (empty($requestData['header']['shopUID'])) {
            return $this->outputBeibei(false, '', '店铺UID不能为空');
        }
        if (empty($requestData['header']['originalShippingWarehouse'])) {
            return $this->outputBeibei(false, '', '原仓库id不能为空');
        }


        //商品详情信息
        $detailsItem = $requestData['items'];
        if (!$detailsItem) {
            return $this->outputBeibei(false, '', '商品详情不能为空');
        }
        if (empty($detailsItem[0])) {
            $detailsItem =  array($detailsItem);
        }
        foreach ($detailsItem as $v) {
            if (!$v['company']) {
                return $this->outputBeibei(false, '', '货主id不能为空');
            } else {
                $rs = OmsDatabase::$oms_db->fetchOne('customer_id', 't_qimen_customer_bind', 'customer_id = :customer_id AND is_valid = 1', array(':customer_id' => $v['company']));
                if (empty($rs)) {
                    return $this->outputBeibei(false, '', '货主id不存在或无效');
                } elseif ($rs['customer_id'] != $v['company']) {
                    return $this->outputBeibei(false, '', '货主id大小写错误');
                }
            }
            if (!$v['sku']) {
                return $this->outputBeibei(false, '', '商品编码sku不能为空');
            }
            if (!$v['quantity']) {
                return $this->outputBeibei(false, '', '数量不能为空');
            }
        }
        return $this->outputBeibei(true);
    }
}