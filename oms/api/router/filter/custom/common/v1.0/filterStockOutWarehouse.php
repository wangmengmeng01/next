<?php
/**
 * Description: 转库存出库接口字段校验
 * Created by XL.
 * Date: 2018-04-17
 * Time: 9:20
 */

class filterStockOutWarehouse extends msg
{
    public function stock($param)
    {

        # 请求字段信息校验
        if (empty($param)) {

            return $this->outputCustom(false, '请求数据不能为空11');
        }

        # 货主编码
        if (empty($param['storer'])) {

            return $this->outputCustom(false, '货主编码不能为空');
        }

        # 仓库代码
        if (empty($param['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }

        # 跨境平台系统流水号
        if (empty($param['externalNo'])) {

            return $this->outputCustom(false, '跨境平台系统流水号不能为空');
        }

        # 下单时间(yyyy-MM-dd HH:mm:ss)
        if (empty($param['billDate'])) {

            return $this->outputCustom(false, '下单时间不能为空');
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

                return $this->outputCustom(false, '第' . $rank .'个明细中的商品编号不能为空');
            }

            # 单位
            if (empty($value['uom'])) {

                return $this->outputCustom(false, '第' . $rank .'个明细中的单位不能为空');
            }

            # 报检单号
            if (empty($value['declNo'])) {

                return $this->outputCustom(false, '第' . $rank .'个明细中的报检单号不能为空');
            }

            # 预期量
            if (empty($value['expectedQty'])) {

                return $this->outputCustom(false, '第' . $rank .'个明细中的预期量不能为空');
            }

            # 备注
            if (empty($value['remark'])) {

                return $this->outputCustom(false, '第' . $rank .'个明细中的备注不能为空');
            }

        }


        # 校验通过
        return $this->outputCustom(true, '成功');


    }

}