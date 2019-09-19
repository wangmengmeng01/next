<?php
/**
 * Notes:贝贝发货单创建过滤类
 * Date: 2019/6/19
 * Time: 16:39
 */

class filterDeliveryOrderCreate extends msg
{
    public function create(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'body中数据不能为空');
        }
        //校验出库单号(发货单号)
        if (empty($requestData['orderNo'])) {
            return $this->outputBeibei(false, '', '发货单号不能为空');
        }
        //校验出库单类型
        if (empty($requestData['orderType'])) {
            return $this->outputBeibei(false, '', '订单类型不能为空');
        }
        //校验订单时间
        if (empty($requestData['orderTime'])) {
            return $this->outputBeibei(false, '', '订单时间不能为空');
        } elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['orderTime'])) {
            return $this->outputBeibei(false, '', '订单时间格式错误');
        }
        //校验快递单号
        if (empty($requestData['expressNo'])) {
            return $this->outputBeibei(false, '', '快递单号不能为空');
        }
        //校验付款时间
        if (empty($requestData['paidTime'])) {
            return $this->outputBeibei(false, '', '付款时间不能为空');
        } elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['paidTime'])) {
            return $this->outputBeibei(false, '', '付款时间格式错误');
        }
        //校验店铺uid
        if (empty($requestData['shopUid'])) {
            return $this->outputBeibei(false, '', '店铺uid不能为空');
        }
        //校验快递编码
        if (empty($requestData['expressCompanyCode'])) {
            return $this->outputBeibei(false, '', '快递编码不能为空');
        }
        //校验快递名称
        if (empty($requestData['expressCompany'])) {
            return $this->outputBeibei(false, '', '快递名称不能为空');
        }
        //校验收货人手机号
        if (empty($requestData['receiverPhone'])) {
            return $this->outputBeibei(false, '', '收货人手机号不能为空');
        }
        //校验货主编码
        if (empty($requestData['companyId'])) {
            return $this->outputBeibei(false, '', '货主编码不能为空');
        }
        if (empty($requestData['saleChannel'])) {
            return $this->outputBeibei(false, '', '销售渠道不能为空');
        }
        //校验收货人姓名
        if (empty($requestData['receiverName'])) {
            return $this->outputBeibei(false, '', '收货人姓名不能为空');
        }
        //校验收货人省份
        if (empty($requestData['receiverProvince'])) {
            return $this->outputBeibei(false, '', '收货人省份不能为空');
        }
        //校验收货人城市
        if (empty($requestData['receivingCity'])) {
            return $this->outputBeibei(false, '', '收货人城市不能为空');
        }
        //校验收货人地址
        if (empty($requestData['receivingAddress'])) {
            return $this->outputBeibei(false, '', '收货人地址不能为空');
        }
        //校验收货人区
        if (empty($requestData['receivingCounty'])) {
            return $this->outputBeibei(false, '', '收货人区不能为空');
        }
        //校验收货人快递区域
        if (empty($requestData['receivingExpressArea'])) {
            return $this->outputBeibei(false, '', '收货人快递区域不能为空');
        }
        //校验仓库编码
        if (empty($requestData['warehouseNo'])) {
            return $this->outputBeibei(false, '', '仓库编码不能为空');
        }
        //校验三段码
        if (empty($requestData['titleAddress'])) {
            return $this->outputBeibei(false, '', '三段码不能为空');
        }

        //商品详情信息
        $detailsItem = $requestData['detailsItem'];
        if (!$detailsItem) {
            return $this->outputBeibei(false, '', '商品详情不能为空');
        }
        if (empty($detailsItem[0])) {
            $detailsItem =  array($detailsItem);
        }
        foreach ($detailsItem as $v) {
            if (!$v['companyId']) {
                return $this->outputBeibei(false, '', '货主编号不能为空');
            } else {
                $rs = OmsDatabase::$oms_db->fetchOne('customer_id', 't_qimen_customer_bind', 'customer_id = :customer_id AND is_valid = 1', array(':customer_id' => $v['companyId']));
                if (empty($rs)) {
                    return $this->outputBeibei(false, '', '货主编码不存在或无效');
                } elseif ($rs['customer_id'] != $v['companyId']) {
                    return $this->outputBeibei(false, '', '货主编码大小写错误');
                }
            }
            if (!$v['sku']) {
                return $this->outputBeibei(false, '', '商品编码sku不能为空');
            }
            if (!$v['quantity']) {
                return $this->outputBeibei(false, '', '数量不能为空');
            }
            if (!$v['inventoryStatus']) {
                return $this->outputBeibei(false, '', '是否非良品不能为空');
            }
            if (!$v['price']) {
                return $this->outputBeibei(false, '', '单价不能为空');
            }
        }
        return $this->outputBeibei(true);
    }
}