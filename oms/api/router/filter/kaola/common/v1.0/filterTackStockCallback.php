<?php
/**
 * Description: 盘点情况回调接口
 * Date: 2018-05-10 14:42
 * Created by XL.
 */

class filterTackStockCallback extends msg
{

    public function back($params)
    {

        if (empty($params)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        if (empty($params['check_id'])) {

            return $this->outputKaola(false, '盘点单号不能为空');
        }

        $items = $params['inventory'];

        foreach ($items as $k => $item) {

            $line = $k + 1;

            # 商品编码
            if (empty($item['sku_id'])) {

                return $this->outputKaola(false, '第' . $line . '物品的商品编码sku_id不能为空');
            }
        }
        return $this->outputKaola(true,'');
    }
}