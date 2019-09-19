<?php

/**
 * Notes:拼多多电子面单云打印接口过滤类
 * Date: 2019/3/28
 * Time: 14:55
 */
class filterPddWaybillGet extends msg
{
    public function get(&$request)
    {
        if (empty($request)) {
            return $this->outputPdd(0, 'S003', '请求数据不能为空');
        }
        # 货主ID
        if (empty($request['customerCode'])) {
            return $this->outputPdd(0, 'S003', '货主ID不能为空');
        }

        # 仓库地址编码
        if (empty($request['warehouseCode'])) {
            return $this->outputPdd(0, 'S003', '仓库编码不能为空');
        }

        # 电商平台
        if (empty($request['platformMall'])) {
            return $this->outputPdd(0, 'S003', '电商平台不能为空');
        }
        # 物流公司Code，枚举：YTO-圆通，ZTO-中通，YUNDA-韵达，STO-申通
        if (empty($request['wp_code'])) {
            return $this->outputPdd(0, 'S003', '物流公司Code不能为空');
        }

        #发货人信息
        $senderInfo = $request['sender'];
        if (empty($senderInfo['name'])) {
            return $this->outputPdd(0, 'S003', '发件人姓名不能为空');
        }
        # 订单信息 trade_order_info_dtos
        $tradeOrderInfoDtos = $request['trade_order_info_dtos'];
        if (empty($tradeOrderInfoDtos) || !is_array($tradeOrderInfoDtos)) {
            return $this->outputPdd(0, 'S003', '订单信息不能为空');
        }
        if (empty($tradeOrderInfoDtos[0])) {
            return $this->outputPdd(0, 'S003', '订单信息格式不对');
        }
        # 请求ID
        if (empty($tradeOrderInfoDtos[0]['object_id'])) {
            return $this->outputPdd(0, 'S003', '请求ID不能为空');
        }
        # 订单渠道平台编码，只⼊入参PDD
        $order_info = $tradeOrderInfoDtos[0]['order_info'];
        if (empty($order_info)) {
            return $this->outputPdd(0, 'S003', '订单信息不能为空');
        }
        # 订单信息
        if (empty($order_info['order_channels_type']) || $order_info['order_channels_type'] != 'PDD') {
            return $this->outputPdd(0, 'S003', '订单渠道平台编码必须为PDD');
        }

        # 订单号,数量量限制100
        if (empty($order_info['trade_order_list'])) {
            return $this->outputPdd(0, 'S003', '订单号不能为空');
        }
        if (!is_array($order_info['trade_order_list']) || count($order_info['trade_order_list']) >100) {
            return $this->outputPdd(0, 'S003', '订单号格式不对或订单数量不能超过100');
        }
        # 包裹信息
        $package_info = $tradeOrderInfoDtos[0]['package_info'];
        if (empty($package_info)) {
            return $this->outputPdd(0, 'S003', '包裹信息不能为空');
        }
        //商品信息列表
        $items = $package_info['items'];
        if (empty($items)) {
            return $this->outputPdd(0, 'S003', '商品信息列表不能为空');
        }
        if (empty($items[0])) {
            return $this->outputPdd(0, 'S003', '商品信息列表格式不对');
        }

        # 数量
        if (empty($items[0]['count'])) {
            return $this->outputPdd(0, 'S003', '商品数量不能为空');
        }

        # 名称
        if (empty($items[0]['name'])) {
            return $this->outputPdd(0, 'S003', '商品名称不能为空');
        }

        # 收件人信息
        $recipient = $tradeOrderInfoDtos[0]['recipient'];
        if (empty($recipient)) {
            return $this->outputPdd(0, 'S003', '收件人信息不能为空');
        }
        # 详细地址
        if (empty($recipient['address']['detail'])) {
            return $this->outputPdd(0, 'S003', '详细地址不能为空');
        }
        # 省
        if (empty($recipient['address']['province'])) {
            return $this->outputPdd(0, 'S003', '省份不能为空');
        }
        # 收件人姓名
        if (empty($recipient['name'])) {
            return $this->outputPdd(0, 'S003', '收件人姓名不能为空');
        }
        # 标准模板URL
        if (empty($tradeOrderInfoDtos[0]['template_url'])) {
            return $this->outputPdd(0, 'S003', '标准模板URL不能为空');
        }
        return $this->outputPdd(1, '0000','成功');
    }
}