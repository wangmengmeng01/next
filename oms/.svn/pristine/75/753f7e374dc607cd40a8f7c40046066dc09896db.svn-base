<?php
/**
 * 家乐福库存信息接收接口过滤类
 * User: Renee
 * Date: 2018/8/9
 * Time: 17:15
 */
class filterInventorySync extends msg
{
    public function sync(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }

        if (empty($requestData['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        if (empty($requestData['ownerCode'])) {
            return $this->outputQimen('failure', '货主编码不能为空', 'S003');
        }
    }
}