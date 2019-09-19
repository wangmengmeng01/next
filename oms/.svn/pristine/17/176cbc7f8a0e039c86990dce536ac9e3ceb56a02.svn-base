<?php
/**
 * Description: 采购单推送接口
 * Date: 2018-05-04 15:58
 * Created by XL.
 */

require_once API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';

class KlPurchasePush extends wmsRequest
{

    public function push($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, '错误：请求数据不能为空');
            }

            #数据发送
            $response = $this->send();

            if (!$response['success']) {

                return $this->msgObj->outputKaola(false, '采购单推送接口错误：' . $response['error_msg']);
            }

            #查看是否已存在此订单，存在更新，不存在新增
            $orders = OmsDatabase::$oms_db->fetchOne('order_no',
                'oms.t_inbound_info',
                'order_no = :order_no AND is_valid = 1',
                array(':order_no' => $params['purchase_id']));

            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();


            if (!empty($orders)) {

                # 更新已存在订单无效
                OmsDatabase::$oms_db->update('oms.t_inbound_info', array('is_valid' => 0), 'order_no = :order_no AND is_valid = 1', array(':order_no' => $params['purchase_id']));

            }


            $infoData = array(
                'order_no' => $params['purchase_id'],
                'remark' => $params['remark'],
                'customer_id' => kaola_service::$_ownerId,
                'warehouse_code' => kaola_service::$_stockId,
                'supplier_code' => $params['supplier_id'],
                'modify_time' => date('Y-m-d H:i:s'),
                'create_time' => date('Y-m-d H:i:s')
            );


            # 插入采购单信息到t_inbound_info表中
            $lastInsertId = OmsDatabase::$oms_db->insert('oms.t_inbound_info', $infoData);


            $items = $params['order_items'];

            # 物品列表详情插入
            foreach ($items as $val) {

                $detialInfo[] = array(
                    'order_id' => $lastInsertId,
                    'sku' => $val['sku_id'],
                    'customer_id' => kaola_service::$_ownerId,
                    'item_name' => $val['goods_name'],
                    'sku_property' => $val['sku_name'],
                    'expected_qty' => $val['qty'],
                    'modify_time' => date('Y-m-d H:i:s'),
                    'create_time' => date('Y-m-d H:i:s')
                );
            }

            # 批量插入
            OmsDatabase::$oms_db->insertAll('oms.t_inbound_detail', $detialInfo);

            OmsDatabase::$oms_db->getPdo()->commit();

            return $this->msgObj->outputKaola(true, '成功');

        } catch (PDOException $p) {

            # 事务回滚
            OmsDatabase::$oms_db->getPdo()->rollback();

            return $this->msgObj->outputKaola(false, $p->getMessage());

        } catch (PDOException $e) {

            return $this->msgObj->outputKaola(false, $e->getMessage());
        }
    }


}