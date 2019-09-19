<?php
/**
 * Notes:修改出仓单信息2.0
 * Date: 2019/1/10
 * Time: 14:48
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';

class editMultiPoDelivery extends erpRequest
{
    public function editMulti($jsonData)
    {
        try {
            $dataArr = json_decode($jsonData, true);
            $params = array(
                'method' => 'editMultiPoDelivery',
                'data' => $jsonData
            );
            $res = $this->send($params);
            $data = json_decode($res, true);
            if ($data['returnCode'] === '0') {
                //更新出仓单信息表
                $update_arr = array();
                foreach ($dataArr as $k => $v) {
                    if (!in_array($k, array('vendor_id', 'storage_no'))) {
                        $update_arr[$k] = $v;
                    }
                }
                $update_arr['update_time'] = date('Y-m-d H:i:s');
                OmsDatabase::$oms_db->update('t_vip_delivery_info', $update_arr, 'vendor_id=:vendor_id and storage_no=:storage_no', array(':vendor_id' => $dataArr['vendor_id'], ':storage_no' => $dataArr['storage_no']));
            } else {
                echo '修改出仓单信息失败!!';
            }
            return $res;
        } catch (\Exception $e) {
            $e->getMessage();
        }
    }

}