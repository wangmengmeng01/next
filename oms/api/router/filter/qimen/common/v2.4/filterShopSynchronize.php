<?php
/**
 * 奇门店铺同步接口过滤类
 * @author Renee
 *
 */
class filterShopSynchronize extends msg 
{
    public function create(&$requestData)
    {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        //校验操作类型不能为空
        if (empty($requestData['actionType'])) {
            return $this->outputQimen('failure', '操作类型不能为空', 'S003');
        }
        //校验来源平台编码不能为空
        if (empty($requestData['shop']['sourcePlatformCode'])) {
            return $this->outputQimen('failure', '来源平台编码不能为空', 'S003');
        }
        //校验ERP店铺编码不能为空
        if (empty($requestData['shop']['shopCode'])) {
            return $this->outputQimen('failure', 'ERP店铺编码不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>