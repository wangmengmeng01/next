<?php
/**
 * Description: 取消采购单接口
 * Date: 2018-05-07 16:59
 * Created by XL.
 */

require_once API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';

class KlPurchaseCancel extends wmsRequest
{

    public function cancel($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, '取消采购单接口错误：请求数据不能为空');
            }

            $response = $this->send();

            if (!$response['success']) {

                return $this->msgObj->outputKaola(false, '取消采购单接口错误：' . $response['error_msg']);
            }


            # 查询将要更新取消的订单，获取订单的id
            $order_id = OmsDatabase::$oms_db->fetchOne('order_id',
                'oms.t_inbound_info',
                'is_valid = 1 AND order_no = :order_no',
                array(':order_no' => $params['purchase_id'])
            );

            if (empty($order_id)) {

                $order_id['order_id'] = '';

            }


            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            # 更新t_inbound_info入库单表订单为无效
            OmsDatabase::$oms_db->update('oms.t_inbound_info', array('is_valid' => 0), 'order_no = :order_no AND is_valid = 1', array(':order_no' => $params['purchase_id']));


            # 存入t_inbound_cancel入库单取消表
            OmsDatabase::$oms_db->insert('oms.t_inbound_cancel',
                array('order_id' => $order_id['order_id'],
                    'order_no' => $params['purchase_id'],
                    'customer_id' => kaola_service::$_ownerId,
                    'warehouse_code' => kaola_service::$_stockId,
                    'create_time' => date('Y-m-d H:i:s')
                ));


            OmsDatabase::$oms_db->getPdo()->commit();

            return $this->msgObj->outputKaola(true, $response['error_msg']);

        } catch (PDOException $p) {

            OmsDatabase::$oms_db->getPdo()->rollback();

            return $this->msgObj->outputKaola(false, $p->getMessage());

        } catch (Exception $e) {


            return $this->msgObj->outputKaola(false, $e->getMessage());

        }

    }


}