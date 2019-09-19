<?php
/**
 * Description: 取消用户订单
 * Date: 2018-05-08 17:24
 * Created by XL.
 */

require_once API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';

class KlUserOrderCancel extends wmsRequest
{


    public function cancel($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, '取消用户订单接口错误：请求数据不能为空');
            }

            $response = $this->send();

            if (!$response['success']) {

                return $this->msgObj->outputKaola(false, $response['error_msg']);
            }

            # 检测是有此订单
            $deliveryId = OmsDatabase::$oms_db->fetchOne('delivery_id', 'oms.t_delivery_order_info', 'delivery_order_code = :delivery_order_code AND is_valid = 1', array(':delivery_order_code' => $params['order_id']));


            if (empty($deliveryId)) {

                return $this->msgObj->outputKaola(false, '取消用户订单失败：无此订单');
            }

            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            # 更新 t_delivery_order_info 发货单表，设置订单取消
            OmsDatabase::$oms_db->update('oms.t_delivery_order_info',
                array('is_valid' => 0, 'modify_time' => date('Y-m-d H:i:s')),
                'is_valid = 1 AND delivery_order_code = :delivery_order_code',
                array(':delivery_order_code' => $params['order_id'])
            );

            # 新增用户取消订单到 t_delivery_order_cancel 表
            $cancelInfo = array(
                'order_id' => $deliveryId['delivery_id'],
                'order_no' => $params['order_id'],
                'customer_id' => kaola_service::$_ownerId,
                'warehouse_code' => kaola_service::$_stockId,
                'create_time' => date('Y-m-d H:i:s')
            );

            OmsDatabase::$oms_db->insert('oms.t_delivery_order_cancel', $cancelInfo);

            # 提交事务
            OmsDatabase::$oms_db->getPdo()->commit();

            return $this->msgObj->outputKaola(true, $response['error_msg']);


        } catch (PDOException $p) {

            # 事务回滚
            OmsDatabase::$oms_db->getPdo()->rollback();

            return $this->msgObj->outputKaola(false, $p->getMessage());

        } catch (Exception $e) {

            return $this->msgObj->outputKaola(false, $e->getMessage());
        }
    }
}