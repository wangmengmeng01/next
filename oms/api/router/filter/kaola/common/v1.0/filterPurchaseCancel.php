<?php
/**
 * Description:
 * Date: 2018-05-10 14:01
 * Created by XL.
 */

class filterPurchaseCancel extends msg
{

    public function cancel($params)
    {
        if (empty($params)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        if (empty($params['purchase_id'])) {

            return $this->outputKaola(false, '采购单号不能为空');
        }


        return $this->outputKaola(true,'');
    }
}