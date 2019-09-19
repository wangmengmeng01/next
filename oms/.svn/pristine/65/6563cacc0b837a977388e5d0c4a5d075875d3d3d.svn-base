<?php
/**
 * 入库单接口信息过滤
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 10:28
 */

class filterKjAsnInterface extends msg
{

    public function create(&$request)
    {
        # 校验数据是否为空
        if (empty($request)) {

            return $this->outputCustom(false, 'body中数据不能为空');
        }

        # 货主代码
        if (empty($request['storer'])) {

            return $this->outputCustom(false, '货主编码不能为空');
        }

        # 仓库代码
        if (empty($request['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }

        # 跨境平台系统采购单号
        if (empty($request['externalNo'])) {

            return $this->outputCustom(false, '跨境平台系统采购单号不能为空');
        }

        # 订单类型(1=采购单；20=退货；21=布控退货)
        if (empty($request['receipType'])) {

            return $this->outputCustom(false, '订单类型不能为空');
        }

        # 订单日期(yyyyMMdd)
        if (empty($request['billDate'])) {

            return $this->outputCustom(false, '订单日期不能为空');
        }

        # 明细行项总数
        if (empty($request['tdq'])) {

            return $this->outputCustom(false, '明细行项总数不能为空');
        }

        #商品详情
        if (empty($request['item'])) {
            return $this->outputCustom(false, '入库单商品详情不能为空');
        }

        if (empty($request['item'][0])) {
            $request['item'] = array($request['item']);
        }

        # 商品详情信息校验
        foreach ($request['item'] as $key => $item) {

            $i = $key + 1;

            # 商品编号
            if (empty($item['sku'])) {

                return $this->outputCustom(false, "第{$i}个商品详情中：商品编号不能为空");
            }

            # 报检单号
            if (empty($item['declNo'])) {

                return $this->outputCustom(false, "第{$i}个商品详情中：报检单号不能为空");
            }

            # 数量
            if (empty($item['qty']) && $item['qty'] != 0) {

                return $this->outputCustom(false, "第{$i}个商品详情中：数量不能为空");
            }

            # 单位(最小计量单位)
            if (empty($item['uom'])) {

                return $this->outputCustom(false, "第{$i}个商品详情中：单位不能为空");
            }

            # 货值
            if (empty($item['currencyValue']) && $item['currencyValue'] != 0) {

                return $this->outputCustom(false, "第{$i}个商品详情中：货值不能为空");
            }

            # 币值
            if (empty($item['currency'])) {

                return $this->outputCustom(false, "第{$i}个商品详情中：币值不能为空");
            }

        }

        # 校验通过
        return $this->outputCustom(true, '成功');

    }
}