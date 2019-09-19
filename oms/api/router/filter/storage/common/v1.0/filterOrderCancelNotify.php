<?php
/**
 * 仓储订单取消报文下发(单据取消接口)
 * Created by PhpStorm.
 * User: xl
 * Date: 2017/7/19
 * Time: 15:09
 */

class filterOrderCancelNotify extends msg
{
    /**
     * 信息校验
     * @param $requestData
     * @return array
     */
    public function cancel(&$requestData)
    {
        //校验数据是否为空
        if(empty($requestData)) {
            return $this->outputCnStorage(false, 'body中数据不能为空', 'S003');
        }
        $request = $requestData;

        //货主ID
        if(empty($request['ownerUserId'])) {
            return $this->outputCnStorage(false, '货主ID不能为空', 'S003');
        }
        //仓储编码
        if(empty($request['storeCode'])) {
            return $this->outputCnStorage(false, '仓储编码不能为空', 'S003');
        }

        return $this->outputCnStorage(true,'成功','');
    }
}