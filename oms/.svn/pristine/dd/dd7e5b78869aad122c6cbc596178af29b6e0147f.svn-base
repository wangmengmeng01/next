<?php
/**
 * Description: 网易考拉 库存查询接口
 * Date: 2018-05-10 16:22
 * Created by XL.
 */
class filterStockSearch extends msg
{

    public function search($params)
    {

        if (empty($params)) {

            return $this->outputKaola(false, '库存查询接口：请求数据不能为空');
        }

        if (empty($params['sku_ids'])) {

            return $this->outputKaola(false, '查询sku id 列表不能为空');
        }

        return $this->outputKaola(true, '');
    }
}