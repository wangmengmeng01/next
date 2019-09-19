<?php
/**
 * 库存查询接口  信息校验
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 14:46
 */

class filterStockInterface extends msg
{
    public function create(&$request)
    {

        if (empty($request)) {

            return $this->outputCustom(false, 'body中数据不能为空');
        }


        # 货主代码
        if (empty($request['storer'])) {

            return $this->outputCustom(false, '货主代码不能为空');
        }

        # 仓库代码
        if (empty($request['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }

        # 报检单号
        if (empty($request['declNo'])) {

            return $this->outputCustom(false, '报检单号不能为空');
        }

        # 货品编码
        if (empty($request['sku'])) {

            return $this->outputCustom(false, '货品编码不能为空');
        }

        # 校验通过
        return $this->outputCustom(true, '成功');

    }
}