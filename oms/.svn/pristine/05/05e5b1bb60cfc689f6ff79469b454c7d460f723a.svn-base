<?php
/**
 * 家乐福卖场拣货任务拣货完成接口校验类
 * User: Renee
 * Date: 2018/8/15
 * Time: 15:02
 */
class filterPickedConfirm extends msg
{
    /**
     * 家乐福卖场拣货任务拣货完成接口请求信息校验
     * @param $requestData
     * @return xml
     */
    public function confirm(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }

        //校验货主编码
        if (empty($requestData['ownerCode'])) {
            return $this->outputQimen('failure', '货主编码不能为空', 'S003');
        }
        //校验订单号
        if (empty($requestData['deliveryOrderCode'])) {
            return $this->outputQimen('failure', '订单号不能为空', 'S003');
        }

        return $this->outputQimen('success');
    }
}