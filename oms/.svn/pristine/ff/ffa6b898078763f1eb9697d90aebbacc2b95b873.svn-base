<?php
/**
 * 入库单取消接口 信息过滤
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 14:22
 */

class filterKjAsnCancelInterface extends msg
{

    public function create(&$request)
    {
        # 校验数据是否为空
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

        # 跨境平台系统采购单号
        if (empty($request['externalNo'])) {

            return $this->outputCustom(false, '跨境平台系统采购单号不能为空');
        }

        # 查询对应入库单是否存在
        global $db;

        $sql = "SELECT order_no FROM t_inbound_info WHERE order_no = '{$request['externalNo']}'";

        $model = $db->prepare($sql);

        $model->execute();

        # 不存在对应入库单
        $res = $model->fetch(PDO::FETCH_ASSOC);
        if (!$res) {

            return $this->msgObj->outputCustom(false, '跨境平台系统采购单号不存在');
        }

        # 外部采购单号
        if (empty($request['externalNo2'])) {

            return $this->outputCustom(false, '外部采购单号不能为空');
        }


        # 校验通过
        return $this->outputCustom(true, '成功');

    }
}