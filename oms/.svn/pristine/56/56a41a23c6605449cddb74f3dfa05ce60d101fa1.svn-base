<?php
/**
 * 商品备案(货品)信息过滤
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 10:00
 */

class filterKjSkuInterface extends msg
{

    public function create(&$request)
    {

        # 校验数据是否为空
        if (empty($request)) {

            return $this->outputCustom(false, 'body中数据不能为空');
        }

        # 货主编码
        if (empty($request['storer'])) {

            return $this->outputCustom(false, '货主编码不能为空');
        }

        # 仓库代码
        if (empty($request['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }

        # 商品编码（申报系统商品备案唯一标识）
        if (empty($request['skuKey'])) {

            return $this->outputCustom(false, '商品编码不能为空');
        }

        # 商品名称
        if (empty($request['sku'])) {

            return $this->outputCustom(false, '商品名称不能为空');
        }

        # 计量单位(最小计量单位)
        if (empty($request['uom'])) {

            return $this->outputCustom(false, '计量单位不能为空');
        }

        # 毛重
        if (empty($request['swt'])) {

            return $this->outputCustom(false, '毛重不能为空');
        }

        # HS编码
        if (empty($request['hsNumber'])) {

            return $this->outputCustom(false, 'HS编码不能为空');
        }


        # 校验通过
        return $this->outputCustom(true, '成功');

    }
}