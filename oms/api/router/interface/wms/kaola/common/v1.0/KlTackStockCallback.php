<?php
/**
 * Description: 盘点情况回调接口
 * Date: 2018-05-09 10:20
 * Created by XL.
 */

require_once API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';

class KlTackStockCallback extends wmsRequest
{

    public function back($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, '盘点情况回调失败：请求数据不能为空');
            }

            $response = $this->send();

            if (!$response['success']) {

                $this->msgObj->outputKaola(false, '盘点情况回调错误：' . $response['error_msg']);
            }


            $inventory = $params['inventory'];

            $insertValues = [];

            foreach ($inventory as $value) {

                $insertValues[] = array(
                    'bills_no' => $params['check_id'],
                    'sku' => $value['sku_id'],
                    'customer_id' => kaola_service::$_ownerId,
                    'warehouse_id' => kaola_service::$_stockId,
                    'create_time' => date('Y-m-d H:i:s')
                );
            }


            OmsDatabase::$oms_db->insertAll('oms.t_inventory_record', $insertValues);

            return $this->msgObj->outputKaola(true, $response['error_msg']);


        } catch(PDOException $p) {

            return $this->msgObj->outputKaola(false, $p->getMessage());

        } catch (Exception $e) {

            return $this->msgObj->outputKaola($e->getMessage());
        }


    }
}