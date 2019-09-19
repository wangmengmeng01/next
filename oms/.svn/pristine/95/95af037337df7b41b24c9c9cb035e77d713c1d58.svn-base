<?php
/**
 * Description: 贝贝天舟-单据取消接口
 * User: XL
 * Date: 2019/6/18 0018 15:08
 */

require API_ROOT . '/router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsBillCancel extends wmsRequest
{
    public function cancel($params)
    {
        try {

            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }

            # 取消请求
            $response = $this->send();
            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '', 'wms接口调用失败');
            }

            # 取消失败
            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }

            # 根据订单类型确定要取消的订单所在表
            $tbName = $this->judgeOrderType($params['billType']);

            if (!$tbName) {
                return $this->msgObj->outputBeibei(false, '', '失败：单据类型错误');
            }

            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            $this->cancelOrder($params, $tbName);

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

    /**
     * 订单取消处理
     * @param $data
     * @param $tbName
     */
    public function cancelOrder($data, $tbName)
    {

        # 对应查询、取消表
        $findTb = 't_' . $tbName . '_info';
        $cancelTb = 't_' . $tbName . '_cancel';

        # 发货单表
        $deliverParam = [
            'field' => 'delivery_id',
            'where' => 'delivery_order_code = :delivery_order_code AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1',
            'param' => [
                ':delivery_order_code' => $data['billId'],
                ':customer_id' => $data['company'],
                ':warehouse_code' => $data['warehouse']
            ]
        ];
        # 出库单或入库单
        $boundParam = [
            'field' => 'order_id',
            'where' => 'order_no = :order_no AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1',
            'param' => [
                ':order_no' => $data['billId'],
                ':customer_id' => $data['company'],
                ':warehouse_code' => $data['warehouse']
            ]
        ];

        $param = $tbName == 'delivery_order' ? $deliverParam : $boundParam;

        # 查询要取消的单据的订单id
        $res = OmsDatabase::$oms_db->fetchOne($param['field'], $findTb, $param['where'], $param['param']);
        # 如果没查到，直接返回（多次取消处理）
        if (empty($res)) {
            return '';
        }
        $orderId = $tbName == 'delivery_order' ? $res['delivery_id'] : $res['order_id'];

        # 新增取消数据到对应的数据库
        $cancelData = [
            'order_id' => $orderId,
            'order_no' => $data['billId'],
            'order_type' => $data['billType'],
            'customer_id' => $data['company'],
            'warehouse_code' => $data['warehouse'],
            'reason' => $data['reason'],
            'create_time' => date('Y-m-d H:i:s')
        ];

        OmsDatabase::$oms_db->insert($cancelTb, $cancelData);

        $boundWhere = 'order_id = ' . $orderId;
        $deliverWhere = 'delivery_id = ' . $orderId;

        $setData = [
            'order_status' => 'CANCELED',
            'is_valid' => 0
        ];

        $where = $tbName == 'delivery_order' ? $deliverWhere : $boundWhere;

        # 更新订单订单状态
        OmsDatabase::$oms_db->update($findTb, $setData, $where);
    }

    /**
     * 根据取消订单单据类型判断订单表
     * @param 单据类型 $orderType
     * @return 表名
     */
    public function judgeOrderType($orderType)
    {
        # 入库取消 采购入库ASN 调拨入库STI 销售退货RMA 领用还回RBR
        $inType = array('ASN', 'STI', 'RMA', 'RBR');
        # 出库 销售出库SO 调拨出库STO 采购退货RTS 领用出库RB 线下销售ESO
        $outType = array('STO', 'RTS', 'RB', 'ESO');

        # 发货单 线上销售SO

        if (in_array($orderType, $inType)) {
            return 'inbound';
        } else if (in_array($orderType, $outType)) {
            return 'outbound';
        } else if ($orderType == 'SO') {
            return 'delivery_order';
        } else {
            return false;
        }
    }

}