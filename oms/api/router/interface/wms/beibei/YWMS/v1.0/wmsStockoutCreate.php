<?php
/**
 * Description: 贝贝天舟-出库单创建接口
 * User: XL
 * Date: 2019/6/16 0016 14:12
 */

require API_ROOT . '/router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsStockoutCreate extends wmsRequest
{

    /**
     * 创建出库单
     * @param $params
     * @return array
     */
    public function create($params)
    {
        try {

            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '',  '失败：请求的数据为空');
            }

            # 推送至wms
            $response = $this->send();

            # 失败
            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '',  'wms接口调用失败');
            }
            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }

            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            # 判断订单信息是否存在，存在更新，不存在写入
            $checkRes = $this->checkOutbound($params);

            # 写入
            if (empty($checkRes)) {

                # 写入出库单数据
                $this->insert_outbound($params);

            } else {

                # 更新原出库单相关信息无效
                $this->update_outbound($checkRes['order_id']);
                # 写入新的出库单数据
                $this->insert_outbound($params);
            }

            # 事务提交
            OmsDatabase::$oms_db->getPdo()->commit();
            return $this->msgObj->outputBeibei(true, $response['data'], $response['message'], $response['addon']);

        } catch (PDOException $p) {

            # 事务回滚
            OmsDatabase::$oms_db->getPdo()->rollBack();
            return $this->msgObj->outputBeibei(false, '',  $p->getMessage());

        } catch (Exception $e) {

            return $this->msgObj->outputBeibei(false, '',  $e->getMessage());
        }
    }

    /**
     * 校验订单号是否存在
     * @param $orderData
     * @return mixed
     */
    public function checkOutbound($orderData)
    {
        $field = 'order_id,total_order_lines,finish_flag';
        $where = 'order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
        $params = [
            ':order_no' => $orderData['billId'],
            ':customer_id' => $orderData['company'],
            ':warehouse_code' => $orderData['warehouse'],
        ];
        return OmsDatabase::$oms_db->fetchOne($field, 't_outbound_info', $where, $params);
    }

    /**
     * 更新出库单信息
     * @param $orderId
     */
    public function update_outbound($orderId)
    {
        # 更新出库单单头无效
        OmsDatabase::$oms_db->update('t_outbound_info', ['is_valid' => 0], 'order_id=:order_id', ['order_id' => $orderId]);
        # 更新出库单明细无效
        OmsDatabase::$oms_db->update('t_outbound_detail', ['is_valid' => 0], 'order_id=:order_id', ['order_id' => $orderId]);
    }

    /**
     * 写入订单数据到数据库
     * @param $orderData
     */
    public function insert_outbound($orderData)
    {
        $info = [
            'order_no' => $orderData['billId'],
            'order_type' => $orderData['billType'],
            'warehouse_code' => $orderData['warehouse'],
            'customer_id' => $orderData['company'],
            'order_time' => $orderData['opTime'],
            'create_time' => date('Y-m-d H:i:s')
        ];

        # 写入出库单单头信息t_outbound_info
        $outboundId = OmsDatabase::$oms_db->insert('t_outbound_info', $info);

        #
        $detailInfo = [];

        foreach ($orderData['details'] as $key => $val) {

            $detailInfo[$key] = [
                # 出库单order_id 不能为空
                'order_id' => $outboundId,
                # 产品
                'sku' => $val['sku'],
                # 行号
                'line_no' => $val['lineNo'],
                # 不良品 贝贝天舟传输值 GOOD/DEFECTIVE
                'lot_att08' => $val['inventoryStatus'] == 'GOOD' ? 'N' : 'Y',
                # 订货数 not null
                'qty_ordered' => $val['quantity'],
                # 创建时间
                'create_time' => date('Y-m-d H:i:s'),

                # 客户编码
                'customer_id' => $orderData['company'],
            ];
        }

        # 写入出单单明细信息
        OmsDatabase::$oms_db->insertAll('t_outbound_detail', $detailInfo);
    }

}