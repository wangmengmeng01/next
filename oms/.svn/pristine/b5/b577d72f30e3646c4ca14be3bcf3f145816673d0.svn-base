<?php
/**
 * 订单审核接口（订单审批状态） 信息校验
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 15:05
 */
class filterMftVerifyInterface extends msg
{

    public function create(&$request)
    {

        if (empty($request)) {

            return $this->outputCustom(false, 'body中数据不能为空');
        }

        # 跨境平台系统申报单号
        if (empty($request['externalNo'])) {

            return $this->outputCustom(false, '跨境平台系统申报单号不能为空');
        }

        # 判断待审核出库单单号是否已存在
        global $db;

        $sql = "SELECT delivery_order_code FROM t_delivery_order_info WHERE delivery_order_code = :delivery_order_code";

        $model = $db->prepare($sql);

        $model->bindParam(':delivery_order_code', $request['externalNo']);

        $model->execute();

        $rs = $model->fetch(PDO::FETCH_ASSOC);
        
        if (empty($rs)) {

            return $this->outputCustom(false, '跨境平台系统申报单号不存在');
        }



        # 外部电商订单号
        if (empty($request['externalNo2'])) {

            return $this->outputCustom(false, '外部电商订单号不能为空');
        }

        # 更新时间(yyyyMMdd HH:mm:ss)
        if (empty($request['date'])) {

            return $this->outputCustom(false, '更新时间不能为空');
        }

        # 电商海关代码
        if (empty($request['storer'])) {

            return $this->outputCustom(false, '电商海关代码不能为空');
        }

        # 仓库代码
        if (empty($request['wmwhseid'])) {

            return $this->outputCustom(false, '仓库代码不能为空');
        }

        # 1=国检,2=海关
        if (empty($request['type'])) {

            return $this->outputCustom(false, '订单审核处不能为空');
        }

        # 1=抽检(国检)已禁用，2=放行，3=审核不过，4=货物放行（海关）
        if (empty($request['flag']) && $request['flag'] != 0) {

            return $this->outputCustom(false, '订单审核状态不能为空');
        }

        return $this->outputCustom(true, '成功');


    }
}