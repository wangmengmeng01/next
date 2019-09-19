<?php
/**
 * Description: 订单推送接口
 * Date: 2018-05-08 14:09
 * Created by XL.
 * 涉及表 t_delivery_order_info、t_delivery_order_detail
 */

require_once API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';

class KlOrderPush extends wmsRequest
{

    public function push($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, '订单推送接口错误：请求数据不能为空');

            }

            $response = $this->send();

            if (!$response['success']) {

                return $this->msgObj->outputKaola(false, '订单推送接口错误：' . $response['error_msg']);
            }

            # 检查发货单表，存在更新，不存在新增
            $orders = OmsDatabase::$oms_db->fetchOne('delivery_order_code',
                'oms.t_delivery_order_info',
                'delivery_order_code = :delivery_order_code AND is_valid = 1',
                array(':delivery_order_code' => $params['order_id']));

            #开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            if (!empty($orders)) {

                # 更新已存在订单无效
                OmsDatabase::$oms_db->update('oms.t_delivery_order_info', array('is_valid' => 0), 'delivery_order_code = :delivery_order_code AND is_valid = 1', array(':delivery_order_code' => $params['order_id']));
            }

            # 数据存放入 t_delivery_order_info 发货单表表
            $orderData = array(
                'remark' => $params['remark'],
                'pay_time' => $params['pay_time'],
                'buyer_nick' => $params['buyer_name'],
                'customer_id' => kaola_service::$_ownerId,
                'warehouse_code' => kaola_service::$_stockId,
                'receiver_name' => $params['receiver_name'],
                'receiver_city' => $params['receiver_city'],
                'receiver_tel' => $params['receiver_phone'],
                'delivery_order_code' => $params['order_id'],
                'receiver_area' => $params['receiver_county'],
                'express_code' => $params['transport_order_id'],
                'receiver_mobile' => $params['receiver_mobile'],
                'place_order_time' => $params['order_create_time'],
                'receiver_province' => $params['receiver_province'],
                'logistics_code' => $params['transport_service_code'],
                'receiver_detail_address' => $params['receiver_address']
            );

            $insertId = OmsDatabase::$oms_db->insert('oms.t_delivery_order_info', $orderData);

            #商品详情存入 t_delievery_order_detail 发货单商品明细表表
            $items = $params['order_items'];

            $detialValues = array();

            foreach ($items as $val) {

                $detialValues[] = array(
                    'delivery_id' => $insertId,
                    'item_code' => $val['sku_id'],
                    'plan_qty' => $val['qty'],
                    'retail_price' => $val['unit_price'],
                    'uom' => $val['unit'],
                    'item_name' => $val['goods_name']
                );
            }

            OmsDatabase::$oms_db->insertAll('oms.t_delivery_order_detail', $detialValues);

            OmsDatabase::$oms_db->getPdo()->commit();

            return $this->msgObj->outputKaola(true, '订单推送接口：' . $response['error_msg']);


        } catch(PDOException $p) {

            OmsDatabase::$oms_db->getPdo()->rollback();

            return $this->msgObj->outputKaola(false, '订单推送接口出错：' . $p->getMessage());

        } catch (Exception $e) {


            return $this->msgObj->outputKaola(false, '订单推送接口出错：' . $e->getMessage());
        }
    }
}