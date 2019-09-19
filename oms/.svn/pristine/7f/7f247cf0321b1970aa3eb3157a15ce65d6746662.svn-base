<?php
/**
 * 美团货主进销存查询接口过滤类
 * User: Renee
 * Date: 2018/8/7
 * Time: 17:12
 */
class filterSettlementQuery extends msg
{
    /**
     * 查询参数校验
     */
    public function query(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }

        return $this->outputQimen('success');
    }
}