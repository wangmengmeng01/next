<?php
/**
 * 承运商备案信息过滤
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-09
 * Time: 15:29
 */

class filterKjCarrierInterface extends msg
{

    public function create(&$request)
    {

        # 校验数据是否为空
        if (empty($request)) {

            return $this->outputCustom(false, 'body中数据不能为空');
        }

        # 承运商代码
        if (empty($request['carrier'])) {

            return $this->outputCustom(false, '承运商代码不能为空');
        }

        # 仓库代码
        if (empty($request['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }


        # 公司名称
        if (empty($request['company'])) {

            return $this->outputCustom(false, '公司名称不能为空');
        }


        # 校验通过
        return $this->outputCustom(true, '成功');

    }
}