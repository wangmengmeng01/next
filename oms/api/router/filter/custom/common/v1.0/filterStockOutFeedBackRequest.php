<?php

/**
 * Description: 转库存出库反馈接口字段信息校验
 * Date: 2018-04-17 11:18
 * Created by XL.
 */
class filterStockOutFeedBackRequest extends msg
{

    public function feedback($param)
    {

        # 请求字段信息校验
        if (empty($param)) {

            return $this->outputCustom(false, '请求数据内容不能为空');
        }

        # 货主编码
        if (empty($param['storer'])) {

            return $this->outputCustom(false, '货主编码不能为空');
        }

        # 仓库代码
        if (empty($param['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }

        # 申报系统出库单号
        if (empty($param['externalNo'])) {

            return $this->outputCustom(false, '申报系统出库单号不能为空');
        }

        # 处理时间(yyyy-MM-dd HH:mm:ss)
        if (empty($param['processDate'])) {

            return $this->outputCustom(false, '处理时间不能为空');
        }

        # 明细行项总数
        if (empty($param['tdq'])) {

            return $this->outputCustom(false, '明细行项总数不能为空');
        }

        # 节点可循环item
        if (empty($param['item'])) {

            return $this->outputCustom(false, '节点可循环item数据不能为空');
        }

        if (empty($param['item'][0])) {

            $param['item'] = array($param['item']);
        }

        foreach ($param['item'] as $key => $value) {

            $rank = $key + 1;

            # 商品编号
            if (empty($value['sku'])) {

                return $this->outputCustom(false, '第' . $rank . '个明细中的商品编号不能为空');
            }

            # 数量
            if (empty($value['qty'])) {

                return $this->outputCustom(false, '第' . $rank . '个明细中的数量不能为空');
            }

            # 报检单号
            if (empty($value['declNo'])) {

                return $this->outputCustom(false, '第' . $rank . '个明细中的报检单号不能为空');
            }

        }


        # 校验通过
        return $this->outputCustom(true, '成功');
    }
}