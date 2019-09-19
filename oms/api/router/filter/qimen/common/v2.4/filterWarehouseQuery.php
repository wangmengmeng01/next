<?php
/**
 * 奇门仓库查询接口过滤文件
 * @author Renee
 *
 */
class filterWarehouseQuery extends msg{
    public function reg(&$requestData)
    {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        } 
        if (empty($requestData['qmWarehouseCode'])) {
            return $this->outputQimen('failure', '奇门仓库编码不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>