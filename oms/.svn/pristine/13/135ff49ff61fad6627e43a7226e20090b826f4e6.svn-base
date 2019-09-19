<?php
/**
 * Notes:贝贝入库单回传接口
 * Date: 2019/6/13
 * Time: 10:35
 */
require API_ROOT . 'router/interface/erp/beibei/common/erpRequest.php';

class erpEntryOrderConfirm extends erpRequest
{
    /***
     * Notes:入库单状态明细回传信息推送
     * Date: 2019/6/18
     * Time: 16:27
     * @param $params
     * @return array
     */
    public function confirm($params)
    {
        try {

            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }
            # 转发数据给wms
            $response = $this->send();

            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '', 'wms接口调用失败');
            }

            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }
            # 开启事物
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            $this->insert_inbound_record($params);
            # 事务提交
            OmsDatabase::$oms_db->getPdo()->commit();
            return $this->msgObj->outputBeibei(true, $response['data'], $response['message'], $response['addon']);
        } catch (PDOException $p) {
            # 事务回滚
            OmsDatabase::$oms_db->getPdo()->rollBack();
            return $this->msgObj->outputBeibei(false,'', $p->getMessage());
        } catch (Exception $e) {
            return $this->msgObj->outputBeibei(false, '', $e->getMessage());
        }
    }

    /**
     * 把入库单回传信息写入数据库
     * @param array
     */
    public function insert_inbound_record($params)
    {
        $entry_order = $params['entryOrder'];
        $details_info = $params['entryOrder']['details'];
        # 获取入库单order_id
        $rs = OmsDatabase::$oms_db->fetchOne('order_id', 't_inbound_info', 'order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1', array(':order_no' => $entry_order['billId'], ':customer_id' => $details_info[0]['company'], ':warehouse_code' => $entry_order['warehouse']));
        $order_id = $rs['order_id'];
        if ($order_id != '') {
            $insert_arr = array(
                'order_id' => $order_id,
                'oms_order_no' => $entry_order['billId'],
                'order_type' => $entry_order['billType'],
                'operate_time' => $entry_order['opTime'],
                'warehouse_code' => $entry_order['warehouse'],
                'customer_id' => $details_info[0]['company'],
                'create_time' => date('Y-m-d H:i:s')
            );
            $record_id = OmsDatabase::$oms_db->insert('t_inbound_info_record', $insert_arr);
            # 插入库单明细回传记录
            if (!empty($details_info)) {
                if (empty($details_info[0])) {
                    $details_info = array($details_info);
                }
                $detail_arr = array();
                foreach ($details_info as $v) {
                    $detail_arr[] = array(
                        'customer_id' => $v['company'],
                        'line_no' => $v['lineNo'],
                        'sku' => $v['sku'],
                        'item_name' => $v['skuDesc'],
                        'expected_qty' => $v['quantity'],
                        'order_id' => $order_id,
                        'produce_code' => $v['productionLot'],
                        'batch_code' => $v['productionLot'],
                        'inventory_type' => $v['inventoryStatus'],
                        'record_id' => $record_id,
                        'create_time' => date('Y-m-d H:i:s')
                    );
                }
                OmsDatabase::$oms_db->insertAll('t_inbound_detail_record', $detail_arr);
            }
            # 更新入库单状态
            OmsDatabase::$oms_db->update('t_inbound_info', ['order_status' => 99], 'order_id = :order_id', [':order_id' => $order_id]);
        }
    }
}

