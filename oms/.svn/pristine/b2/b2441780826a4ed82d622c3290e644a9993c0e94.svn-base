<?php
/**
 * 出库单接口过滤类
 * User: Renee
 * Date: 2018/1/16
 * Time: 17:43
 */
class filterKjSoOrder extends msg{
    public function create(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        if (empty($requestData['storer'])) {
            return $this->outputCustom(false,'货主编码不能为空！');
        }
        if (empty($requestData['wmwhseid'])) {
            return $this->outputCustom(false,'仓库代码不能为空！');
        }
        if (empty($requestData['externalNo'])) {
            return $this->outputCustom(false,'跨境平台订单号不能为空！');
        }
        if (empty($requestData['shipToName'])) {
            return $this->outputCustom(false,'交货人姓名不能为空！');
        }
        if (empty($requestData['shipToPhone'])) {
            return $this->outputCustom(false,'交货人联系电话不能为空！');
        }
        if (empty($requestData['userName'])) {
            return $this->outputCustom(false,'买家用户名不能为空！');
        }
        if (empty($requestData['billDate'])) {
            return $this->outputCustom(false,'下单时间不能为空！');
        }
        if (empty($requestData['paymentDateTime'])) {
            return $this->outputCustom(false,'付款时间不能为空！');
        }
        if (empty($requestData['receipType'])) {
            return $this->outputCustom(false,'订单分类不能为空！');
        }
        if (empty($requestData['provinceName'])) {
            return $this->outputCustom(false,'省名不能为空！');
        }
        if (empty($requestData['cityName'])) {
            return $this->outputCustom(false,'市名不能为空！');
        }
        if (empty($requestData['regionName'])) {
            return $this->outputCustom(false,'区/县名不能为空！');
        }
        if (empty($requestData['shipToAddr'])) {
            return $this->outputCustom(false,'详细地址不能为空！');
        }
        if (empty($requestData['dsPlatform'])) {
            return $this->outputCustom(false,'电商平台不能为空！');
        }
        if (empty($requestData['carrierKey'])) {
            return $this->outputCustom(false,'承运商代码不能为空！');
        }
        if (empty($requestData['expressID'])) {
            return $this->outputCustom(false,'快递单号不能为空！');
        }
        if (empty($requestData['payment'])) {
            return $this->outputCustom(false,'实际支付金额不能为空！');
        }
        if (empty($requestData['tdq'])) {
            return $this->outputCustom(false,'明细行项总数不能为空！');
        }
        //校验通过
        return $this->outputCustom(true,'成功');
    }
}