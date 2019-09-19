<?php
/**
 * Description: 贝贝天舟-出库单回传/发货单回传接口
 * User: XL
 * Date: 2019/6/17 0017 16:51
 */

require API_ROOT . '/router/interface/erp/beibei/common/erpRequest.php';

class erpStockoutConfirm extends erpRequest
{
    /**
     * @param $params
     * @return array
     */
    public function confirm($params)
    {
        try {

            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }

            # 转发数据给erp
            $response = $this->send();

            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '', 'wms接口调用失败');
            }

            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }

            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();
            # 写入数据库
            $this->insert_info_confirm($params['stockout']);
            # 事务提交
            OmsDatabase::$oms_db->getPdo()->commit();

            return $this->msgObj->outputBeibei(true, $response['data'], $response['message'], $response['addon']);


        } catch (PDOException $p) {

            # 事务回滚
            OmsDatabase::$oms_db->getPdo()->rollBack();

            return $this->msgObj->outputBeibei(false, '', $p->getMessage());
        } catch (Exception $e) {

            return $this->msgObj->outputBeibei(false, '', $e->getMessage());
        }
    }

    /***
     * Notes: 判断是出库单回传还是发货单回传，并进行数据库操作
     * Date: 2019/7/30
     * Time: 13:37
     * @param $data 请求参数
     */
    public function insert_info_confirm($data)
    {
        # 获取出库单order_id
        $whereInfo = 'order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
        $dataInfo = [':order_no' => $data['billId'], ':warehouse_code' => $data['warehouse'], ':customer_id' => beibei_service::$_customerId];
        $outbound_info = OmsDatabase::$oms_db->fetchOne('order_id', 't_outbound_info', $whereInfo, $dataInfo);
        if (empty($outbound_info)) {
            # 获取发货单delivery_id（发货单回传）
            $whereInfo = 'delivery_order_code=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
            $delivery_info = OmsDatabase::$oms_db->fetchOne('delivery_id', 't_delivery_order_info', $whereInfo, $dataInfo);
            if (empty($delivery_info)) {
                return;
            }
        }
        if ($outbound_info) {
            # 出库单回传处理
            $this->insert_stockout_confirm($outbound_info['order_id'],$data);
        } else {
            # 发货单回传处理
            $this->insert_delivery_info_confirm($delivery_info['delivery_id'],$data);
        }
    }

    /**
     * Notes: 出库单回传信息插入数据库
     * Date: 2019/7/30
     * Time: 13:39
     * @param $order_id 出库单id
     * @param $params
     */
    public function insert_stockout_confirm($order_id,$params)
    {
        # 将出库单回传信息存入t_outbound_info_record表
        $dataInfoRecord = [
            # 出库单order_id
            'order_id' => $order_id,
            # 单据类型
            'order_type' => $params['billType'],
            # 商家id
            'customer_id' => beibei_service::$_customerId,
            # 仓库id
            'warehouse_code' => $params['warehouse'],
            # 操作时间(订单创建时间)？
            'order_time' => $params['opTime'],
            'create_time' => date('Y-m-d H:i:s'),
        ];
        $recordId = OmsDatabase::$oms_db->insert('t_outbound_info_record', $dataInfoRecord);

        # 写入出库单明细 t_outbound_detail_record
        $dataDetailRecord = [];
        foreach ($params['details'] as $k => $detail) {
            $dataDetailRecord[$k] = [
                'sku' => $detail['sku'],
                'order_no' => $params['billId'],
                'record_id' => $recordId,
                'order_id' => $order_id,
                'item_name' => $detail['skuDesc'],
                # 行号
                'line_no' => $detail['lineNo'],
                # 数量，订货数
                'qty_ordered' => $detail['quantity'],
                # 生产批号
                'produce_code' => $detail['productionLot'],
                # 库位（商品仓储系统编码？？）
                'item_id' => $detail['location'],
                # 良品 N：良品   Y：不良
                'lot_att08' => $detail['inventoryStatus'] == 'GOOD' ? 'N' : 'Y',
                # 客户id
                'customer_id' => $detail['company'],
                # 创建时间
                'create_time' => date('Y-m-d H:i:s'),
            ];
        }
        OmsDatabase::$oms_db->insertAll('t_outbound_detail_record', $dataDetailRecord);

        # 更新出库单状态为99订单完成
        OmsDatabase::$oms_db->update('t_outbound_info', ['order_status' => 99], 'order_id = :order_id', [':order_id' => $order_id]);
    }

    /**
     * Notes: 发货单回传信息插入数据库
     * Date: 2019/7/30
     * Time: 13:39
     * @param $order_id 发货单id
     * @param $params
     */
    public function insert_delivery_info_confirm($order_id,$params)
    {
        # 将发货单回传信息存入t_delivery_order_info_record表
        $dataInfoRecord = [
            # 发货单order_id
            'delivery_id' => $order_id,
            # 发货单号
            'delivery_order_code' => $params['billId'],
            # 单据类型
            'order_type' => $params['billType'],
            # 商家id
            'customer_id' => beibei_service::$_customerId,
            # 仓库id
            'warehouse_code' => $params['warehouse'],
            # 操作时间
            'operate_time' => $params['opTime'],
            # 创建时间(入库时间)
            'create_time' => date('Y-m-d H:i:s'),
        ];
        OmsDatabase::$oms_db->insert('t_delivery_order_info_record', $dataInfoRecord);
        # 写入发货单明细 t_delivery_order_detail_record
        $dataDetailRecord = [];
        foreach ($params['details'] as $k => $detail) {
            $dataDetailRecord[$k] = [
                'delivery_id' => $order_id,
                'order_line_no' => $detail['lineNo'],
                'order_source_code' => $params['billId'],
                'customer_id' => $detail['company'],
                'item_code' => $detail['sku'],
                'item_id' => $detail['location'],
                'inventory_type' => $detail['inventoryStatus'],
                'item_name' => $detail['skuDesc'],
                # 生产批号
                'produce_code' => $detail['productionLot'],
                'actual_qty' => $detail['quantity'],
                # 创建时间
                'create_time' => date('Y-m-d H:i:s'),
            ];
        }
        OmsDatabase::$oms_db->insertAll('t_delivery_order_detail_record', $dataDetailRecord);
        # 更新发货单状态为99订单完成
        OmsDatabase::$oms_db->update('t_delivery_order_info', ['order_status' => 99], 'delivery_id = :delivery_id', [':delivery_id' => $order_id]);
    }
}