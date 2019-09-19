<?php
/**
 * 奇门仓库注册接口过滤类
 * @author Renee
 *
 */
class filterWarehouseReg extends msg{
    public function reg(&$requestData)
    {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        } 
        if (empty($requestData['warehouseCode'])) {
            return $this->outputQimen('failure', 'WMS的仓库编码不能为空', 'S003');
        }
        if (empty($requestData['warehouseName'])) {
            return $this->outputQimen('failure', '仓库名称不能为空', 'S003');
        }
        if (empty($requestData['warehouseInfo']['province'])) {
            return $this->outputQimen('failure', '仓库省份不能为空', 'S003');
        }
        if (empty($requestData['warehouseInfo']['city'])) {
            return $this->outputQimen('failure', '仓库城市不能为空', 'S003');
        }
        if (empty($requestData['warehouseInfo']['area'])) {
            return $this->outputQimen('failure', '仓库区域不能为空', 'S003');
        }
        if (empty($requestData['warehouseInfo']['detailAddress'])) {
            return $this->outputQimen('failure', '详细地址不能为空', 'S003');
        }
        if (empty($requestData['wmsURL'])) {
            return $this->outputQimen('failure', '该仓库所用的wms的URL不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>