<?php
/**
 * erp订单类
 * User:  独孤羽<123517746@qq.com>
 * Date: 15-4-27 下午4:38
 */
require '../erpRequest.php';
class erpOrder extends erpRequest
{

    /**
     * erp发货通知
     * @param $params
     * @return mixed
     */
    public function delivery($params)
    {
        //订单业务处理

        $method = 'erp.order.delivery';
        return $this->send($method, $params);
    }


}