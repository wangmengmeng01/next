<?php
/**
 * Description:取消用户订单
 * Date: 2018-05-10 15:45
 * Created by XL.
 */
class filterUserOrderCancel extends msg
{

    public function cancel($params)
    {

        if (empty($params)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        if (empty($params['order_id'])) {

            return $this->outputKaola(false, '订单号不能为空');
        }


        return $this->outputKaola(true,'');
    }
}