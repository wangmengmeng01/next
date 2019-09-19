<?php
/**
 * 入库单上账接口 信息校验
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 15:38
 */

class filterAsnReceiveInterface extends msg
{
    public function create(&$request)
    {
        if (empty($request)) {

            return $this->outputCustom(false, 'body中数据不能为空');
        }

        # 仓库代码
        if (empty($request['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }

        # 系统订单号
        if (empty($request['externalNo'])) {

            return $this->outputCustom(false, '系统订单号不能为空');
        }

        # 校验系统订单号是否存在
        global $db;

        $sql = 'SELECT order_id FROM t_inbound_info WHERE order_no = :order_no';

        $model = $db->prepare($sql);
        $model->bindParam(':order_no', $request['externalNo']);
        $model->execute();
        $res = $model->fetch(PDO::FETCH_ASSOC);

        if (empty($res)) {

            return $this->outputCustom(false, '系统订单号不存在');
        }

        # 系统订单号存在，存入数组中供确认入库
        $request['order_id'] = $res['order_id'];

        # 订单日期
        if (empty($request['billDate'])) {

            return $this->outputCustom(false, '订单日期不能为空');
        }

        # 收货时间
        if (empty($request['receiveDate'])) {

            return $this->outputCustom(false, '收货时间不能为空');
        }

        # 明细行
        if (empty($request['tdq'])) {

            return $this->outputCustom(false, '明细行不能为空');
        }

        # 货主
        if (empty($request['storer'])) {

            return $this->outputCustom(false, '货主不能为空');
        }

        # 入库单上账信息详情
        if (empty($request['item'])) {

            return $this->outputCustom(false, '入库单上账信息详情不能为空');
        }

        if (empty($request['item'][0])) {
            $request['item'] = array($request['item']);
        }

        # 上账信息详情校验
        foreach ($request['item'] as $key => $item) {

            $i = $key + 1;

            # 货品编码
            if (empty($item['sku'])) {

                return $this->outputCustom(false, "第{$i}个上账信息中：货品编码不能为空");
            }

            # 良品数量
            if ((empty($item['qtyQp']) && $item['qtyDef'] != 0) || !is_numeric($item['qtyQp'])) {

                return $this->outputCustom(false, "第{$i}个个上账信息中：良品数量不能为空,且只能为数字");
            }


            # 次品数量
            if ((empty($item['qtyDef']) && $item['qtyDef'] != 0) || !is_numeric($item['qtyDef'])) {

                return $this->outputCustom(false, "第{$i}个个上账信息中：次品数量不能为空,且只能为数字");
            }

        }

        return $this->outputCustom(true, '成功');


    }
}