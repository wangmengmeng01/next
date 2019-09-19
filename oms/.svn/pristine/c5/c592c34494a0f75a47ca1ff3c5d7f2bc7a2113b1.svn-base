<?php

/**
 * Description: 采购单推送接口数据信息过滤
 * Date: 2018-05-04 16:07
 * Created by XL.
 */
class filterPurchasePush extends msg
{

    public function push($params)
    {

        if (empty($params)) {

            return $this->outputKaola(false, '请求数据不能为空');
        }

        #采购单号 对应数据库字段 order_no
        if (empty($params['purchase_id'])) {

            return $this->outputKaola(false, '采购单号不能为空');
        }


        #物品列表
        if (empty($params['order_items'])) {

            return $this->outputKaola(false, '物品列表不能为空');
        }


        $items = $params['order_items'];

        foreach ($items as $k => $val) {

            $line = $k + 1;

            #sku_id
            if (empty($val['sku_id'])) {

                return $this->outputKaola(false, '物品列表第' . $line . '行，sku_id不能为空');
            }

            #sku_type
            if (empty($val['sku_type']) && $val['sku_type'] !== 0) {

                return $this->outputKaola(false, '物品列表第' . $line . '行，sku_type不能为空');
            }

            #商品序列号
            if (empty($val['product_no'])) {

                return $this->outputKaola(false, '物品列表第' . $line . '行，商品序列号不能为空');
            }

            #商品原生条形码(多个用逗号隔开)
            if (empty($val['barcode'])) {

                return $this->outputKaola(false, '物品列表第' . $line . '行，商品原生条形码不能为空');
            }

            #商品名
            if (empty($val['goods_name'])) {

                return $this->outputKaola(false, '物品列表第' . $line . '行，商品名不能为空');
            }

            #规格名称
            if (empty($val['sku_name'])) {

                return $this->outputKaola(false, '物品列表第' . $line . '行，规格名称不能为空');
            }

            #数量
            if (empty($val['qty'])  && $val['qty'] !== 0) {

                return $this->outputKaola(false, '物品列表第' . $line . '行，数量不能为空');
            }

        }


        return $this->outputKaola(true, '成功');

    }

}