<?php
/**
 * 入库单上账接口 cnec_im_1
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-17
 * Time: 15:06
 */

require_once API_ROOT . '/router/interface/erp/custom/common/erpRequest.php';

class erpAsnReceiveInterface extends erpRequest
{

    public function create($param)
    {
        try {

            # 数据检查
            if (empty($param)) {
                return $this->msgObj->outputCustom(false, '错误：请求数据不能为空');
            }

            # 数据发送
            $response = $this->send();
            //访问客户接口
            if ($response['success'] == 'true') {
                $this->customSend();
            }

            # 转发结果检测
            if (!$response['success']) {

                return $this->msgObj->outputCustom(false, '入库单上账接口错误:' . $response['reasons'], $response['addon']);
            }

            # 插入 t_inbound_info_record（入库单状态明细回传单头信息）表
            global $db;

            $sql = "INSERT INTO t_inbound_info_record
                        (order_id,warehouse_code, wms_order_no, external_no2, bill_date,
                         receive_date, total_order_lines, customer_id, create_time)
                    VALUE ({$param['order_id']}, :warehouse_code, :wms_order_no, :external_no2,
                        :bill_date, :receive_date, :total_order_lines, :customer_id, now())";

            $model = $db->prepare($sql);

            # 数据绑定
            $model->bindParam(':warehouse_code', $param['wmwhseid']);
            $model->bindParam(':wms_order_no', $param['externalNo']);
            $model->bindParam(':external_no2', $param['externalNo2']);
            $model->bindParam(':bill_date', $param['billDate']);
            $model->bindParam(':receive_date', $param['receiveDate']);
            $model->bindParam(':total_order_lines', $param['tdq']);
            $model->bindParam(':customer_id', $param['storer']);

            if (!$model->execute()) {

                return $this->msgObj->outputCustom(false, '入库单上账下发成功，但数据入库失败');

            }

            $record_id = $db->lastInsertId();


            # 详情数据入库
            $values = '';

            if (empty($param['item'][0])) {
                $param['item'] = array($param['item']);
            }

            foreach ($param['item'] as $val) {

                $values .= "({$record_id}, {$param['order_id']}, '{$val['sku']}', {$val['qtyQp']}, {$val['qtyDef']}),";
            }

            $values = rtrim($values, ',');

            $sql = "INSERT INTO t_inbound_detail_record (record_id, order_id, sku, qty_qp, qty_def) VALUES {$values}";

            # 详情数据存储
            $model = $db->prepare($sql);

            if ($model->execute()) {

                return $this->msgObj->outputCustom(true, '入库单上账信息转发并存入数据库成功', $response['addon']);
            }

            return $this->msgObj->outputCustom(false, '入库单上账信息转发陈工，但存入数据库失败');


        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, $e->getMessage());
        }

    }
}