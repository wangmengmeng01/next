<?php
/**
 * Notes:确认某一条出仓单
 * Date: 2019/1/10
 * Time: 15:07
 */

require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';

class confirmDelivery extends erpRequest
{
    public function confirm($jsonData)
    {
        try {
            if (empty($jsonData)) {
                return '错误：请求数据不能为空';
            }
            $dataArr = json_decode($jsonData,true);
            $vendor_id = $dataArr['vendor_id'];
            $storage_no = $dataArr['storage_no'];
            $params = array(
                'method' => 'confirmDelivery',
                'data' => $jsonData
            );
            $res = $this->send($params);
            $data = json_decode($res, true);
            if ($data['returnCode'] === '0') {
                OmsDatabase::$oms_db->getPdo()->beginTransaction();
                $del_res = OmsDatabase::$oms_db->update('t_vip_delivery_info', array('confirm_time' => date('Y-m-d H:i:s')), 'vendor_id=:vendor_id and storage_no=:storage_no', array(':vendor_id' => $vendor_id, ':storage_no' => $storage_no));
                $pick_res = OmsDatabase::$oms_db->update('t_vip_pick_list', array('status' => '已发货'), 'vendor_id=:vendor_id and storage_no=:storage_no', array(':vendor_id' => $vendor_id, ':storage_no' => $storage_no));
                if ($del_res && $pick_res) {
                    OmsDatabase::$oms_db->getPdo()->commit();
                    echo '确认出仓单成功并更新数据库成功';exit;
                } else {
                    OmsDatabase::$oms_db->getPdo()->rollBack();
                    echo '确认出仓单成功但更新数据库失败';exit;
                }
            } else {
                throw new Exception('确认出仓单失败:'.$data['returnMessage']);
            }
        } catch (\Exception $e) {
            return $e->getMessage();
        }
    }

}