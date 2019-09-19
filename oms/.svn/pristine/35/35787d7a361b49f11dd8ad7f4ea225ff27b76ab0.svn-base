<?php
/**
 * Notes:贝贝查询第三方仓库库存过滤类
 * Date: 2019/6/26
 * Time: 14:23
 */

class filterInventoryQuery extends msg
{
    public function search(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'body中数据不能为空');
        }
        if (empty($requestData['warehouseNo'])) {
            return $this->outputBeibei(false, '', '仓库编号不能为空');
        }
        return $this->outputBeibei(true);
    }
}