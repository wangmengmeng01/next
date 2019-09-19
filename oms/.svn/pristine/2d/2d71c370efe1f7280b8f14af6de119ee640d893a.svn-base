<?php
/**
 * 贝贝天舟-库存异动接口
 */
require API_ROOT . '/router/interface/erp/beibei/common/erpRequest.php';

class erpStockChangeReport extends erpRequest
{

    public function report($params) {

        try {

            if (empty($params)) {
                return $this->msgObj->outputBeibei(false,  '', '失败：请求的数据为空');
            }

            $response = $this->send();

            if (empty($response)) {
                return $this->msgObj->outputBeibei(false,  '', 'wms接口调用失败');
            }

            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }

            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            # 库存异动数据写入
            $this->recordInsert($params['stockChangeReport']);

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

    /**
     * @param $params
     */
    public function recordInsert($params)
    {
        # 写入 t_stock_change_record表
        $recordInfo =  [
            # 货主编码
            'customer_id' => $params['company'],
            # 仓库编码
            'warehouse_code' => $params['warehouse'],
            # 单据编码
            'order_code' => $params['billId'],
            # 单据类型
            'order_type' => $params['billType'],
            'create_time' => date('Y-m-d H:i:s'),
        ];

        $changeId = OmsDatabase::$oms_db->insert('t_stock_change_record', $recordInfo);

        # t_stock_change_batch_record表
        $batchInfo = [];

        foreach ($params['items'] as $k => $det) {

            $batchInfo[$k] = [

                'change_id' => $changeId,

                # 批次编码(生产批号)
                'produce_code' => $det['productionLot'],
                # 异动数量
                'quantity' => $det['quantity'],

                'create_time' => date('Y-m-d H:i:s'),
            ];
        }

        OmsDatabase::$oms_db->insertAll('t_stock_change_batch_record', $batchInfo);
    }
}