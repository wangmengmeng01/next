<?php
/**
 * Notes:获取所有的标准电子面单模板接口过滤类
 * Date: 2019/3/28
 * Time: 17:10
 */

class filterPddCloudprintStdtemplatesGet extends msg
{
    public function get(&$request)
    {
        if (empty($request)) {
            return $this->outputPdd(0, 'S003', '请求数据不能为空');
        }
        # 货主ID
        if (empty($request['customerCode'])) {
            return $this->outputPdd(0, 'S003', '货主ID不能为空');
        }
        # 仓库地址编码
        if (empty($request['warehouseCode'])) {
            return $this->outputPdd(0, 'S003', '仓库编码不能为空');
        }

        # 电商平台
        if (empty($request['platformMall'])) {
            return $this->outputPdd(0, 'S003', '电商平台不能为空');
        }
        return $this->outputPdd(1, '0000','成功');
    }
}