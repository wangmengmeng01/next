<?php
/**
 * Description: 采购单入库回调接口
 * Date: 2018-05-11 10:18
 * Created by XL.
 */
require_once API_ROOT . '/router/interface/erp/kaola/common/erpRequest.php';

class KlPurchaseEntryCallback extends erpRequest
{

    public function back($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, '采购单入库回调接口错误：请求数据不能为空');
            }

            $response = $this->send();

            if (!$response['success']) {

                return $this->msgObj->outputKaola(false, '采购单入库回调接口错误：' . $response['error_msg']);
            }

            # 获取采购单对应入库单表id
            $orderId = OmsDatabase::$oms_db->fetchOne('order_id', 'oms.t_inbound_info', 'order_no = :order_no AND is_valid = 1', array(':order_no' => $params['purchase_id']));

            if (empty($orderId)) {

                return $this->msgObj->outputKaola(false, '该采购单不存在，回调失败');
            }

            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            # 新增数据到 t_inbound_info_record 入库单状态明细回传单头信息表
            $recordId = OmsDatabase::$oms_db->insert('oms.t_inbound_info_record',
                array(
                    'order_id' => $orderId['order_id'],
                    'customer_id' => kaola_service::$_ownerId,
                    'warehouse_code' => kaola_service::$_stockId,
                    'order_confirm_time' => $params['arrival_end_time'],
                    'create_time' => date('Y-m-d H:i:s')
                ));

            # 新增详情数据到 t_inbound_detail_record 入库单回传明细记录表
            $detailValues = array();

            foreach ($params['order_items'] as $key => $value) {

                $detailValues[] = array(
                    'record_id' => $recordId,
                    'order_id' => $orderId['order_id'],
                    'customer_id' => kaola_service::$_ownerId,
                    'sku' => $value['sku_id'],
                    'qty_qp' => $value['qty_good'],
                    'qty_def' => $value['qty_bad'],
                    'create_time' => date('Y-m-d H:i:s')
                );
            }

            OmsDatabase::$oms_db->insertAll('oms.t_inbound_detail_record', $detailValues);

            OmsDatabase::$oms_db->getPdo()->commit();

            return $this->msgObj->outputKaola(true, '采购单入库回调接口成功');

        } catch (PDOException $p) {

            OmsDatabase::$oms_db->getPdo()->rollback();

            return $this->msgObj->outputKaola(false, $p->getMessage());

        } catch (Exception $e) {

            return $this->msgObj->outputKaola(false, $e->getMessage());
        }


    }
}