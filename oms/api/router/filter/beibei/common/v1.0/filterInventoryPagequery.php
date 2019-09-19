<?php
/**
 * Notes: 贝贝分页查询第三方库存过滤类
 * User: wj
 * Date: 2019/7/25
 * Time: 18:04
 */

class filterInventoryPagequery extends msg
{
    public function query($requestData)
    {
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'data中数据不能为空');
        }
        if (empty($requestData['warehouse'])) {
            return $this->outputBeibei(false, '', '仓库编码不能为空');
        }
        if (empty($requestData['page'])) {
            return $this->outputBeibei(false, '', '当前页不能为空');
        }
        if (empty($requestData['pageSize'])) {
            return $this->outputBeibei(false, '', '每页多少条不能为空');
        }
        if ($requestData['pageSize'] > 100) {
            return $this->outputBeibei(false, '', '每页最多100条');
        }
        return $this->outputBeibei(true, '', '成功');

    }
}
