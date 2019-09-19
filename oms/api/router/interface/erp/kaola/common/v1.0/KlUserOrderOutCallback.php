<?php
/**
 * Description: 用户订单出库回调
 * Date: 2018-05-11 10:33
 * Created by XL.
 */
require_once API_ROOT . 'router/interface/erp/kaola/common/erpRequest.php';


class KlUserOrderOutCallback extends erpRequest
{

    public function back($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, '用户订单出库回调接口错误：请求数据不能为空');
            }

            $response = $this->send();

            if (!$response['success']) {

                return $this->msgObj->outputKaola(false, $response['error_msg']);
            }


            # 获取 发货单order_id
            $deliveryId = OmsDatabase::$oms_db->fetchOne('delivery_id', 'oms.t_delivery_order_info', 'delivery_order_code = :delivery_order_code AND is_valid = 1', array(':delivery_order_code' => $params['order_id']));

            if (empty($deliveryId)) {

                $deliveryId['delivery_id'] = '';
//                return $this->msgObj->outputKaola(false, '该发货单不存在或已失效');
            }

            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            # 发货单 t_delivery_order_info_record 回传单头信息新增
            OmsDatabase::$oms_db->insert('oms.t_delivery_order_info_record',
                array('delivery_id' => $deliveryId['delivery_id'],
                    'order_status' => $params['order_status'],
                    'customer_id' => kaola_service::$_ownerId,
                    'warehouse_code' => kaola_service::$_stockId,
                    'create_time' => date('Y-m-d H:i:s'),
                    'operate_time' => $params['op_time'])
            );

            # 状态为300 - 发货（包裹交付物流商揽收），则存物品详情
            if ($params['order_status'] == 300) {

                # 发货单回传详情信息新增 t_delivery_order_detail_record
                $detailValues = array();

                foreach ($params['order_items'] as $val) {

                    $detailValues[] = array('delivery_id' => $deliveryId['delivery_id'],
                        'item_code' => $val['sku_id'],
                        'customer_id' => kaola_service::$_ownerId,
                        'create_time' => date('Y-m-d H:i:s'));
                }

                OmsDatabase::$oms_db->insertAll('oms.t_delivery_order_detail_record', $detailValues);
            }


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