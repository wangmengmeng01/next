<?php
/**
 * Description: 采购单入库回调接口
 * Date: 2018-05-11 10:22
 * Created by XL.
 */

class filterPurchaseEntryCallback extends msg
{

    public function back($params)
    {

        if (empty($params)) {

            return $this->outputKaola(false, '请求数据不能为空');
        }

        # 采购单号
        if (empty($params['purchase_id'])) {

            return $this->outputKaola(false, '采购单号不能为空');
        }


        # 物品列表
        if (empty($params['order_items'])) {

            return $this->outputKaola(false, '物品列表不能为空');
        }

        $items = $params['order_items'];


        foreach ($items as $k => $value) {

            $line = $k + 1;

            # sku商品编码
            if (empty($value['sku_id'])) {

                return $this->outputKaola(false, '第' . $line . '个物品列表，商品编码不能为空');
            }

            # 商品重量
            /*if (empty($value['weight']) && $value['weight'] !== 0  && $value['weight'] !== '0') {

                return $this->outputKaola(false, '第' . $line . '个物品列表，商品重量不能为空');
            }*/

            # 良品数量
            if (empty($value['qty_good']) && $value['qty_good'] !== 0 && $value['qty_good'] !== '0') {

                return $this->outputKaola(false, '第' . $line . '个物品列表，良品数量不能为空');
            }

            # 次品数量
            if (empty($value['qty_bad']) && $value['qty_bad'] !== 0 && $value['qty_bad'] !== '0') {

                return $this->outputKaola(false, '第' . $line . '个物品列表，次品数量不能为空');
            }

            /*# 报关单号
            if (empty($value['declare_no'])) {

                return $this->outputKaola(false, '第' . $line . '个物品列表，报关单号不能为空');
            }

            # 报检单号
            if (empty($value['inspect_no'])) {

                return $this->outputKaola(false, '第' . $line . '个物品列表，报检单号不能为空');
            }*/

        }

        return $this->outputKaola(true, '成功');
    }
}