<?php
/**
 * Notes:删除指定单号的出仓明细
 * Date: 2019/1/10
 * Time: 15:01
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';

class deleteDeliveryDetail extends erpRequest
{
    public function delete($jsonData)
    {
        try {
            if (empty($jsonData)) {
                return '错误：请求数据不能为空';
            }
            $dataArr = json_decode($jsonData, true);
            $vendor_id = $dataArr['vendor_id'];
            $storage_no = $dataArr['storage_no'];
            $params = array(
                'method' => 'deleteDeliveryDetail',
                'data' => $jsonData
            );
            $res = $this->send($params);
            $data = json_decode($res, true);
            if ($data['returnCode'] === '0') {
                $where = "vendor_id = :vendor_id and storage_no=:storage_no";
                $param = array(':vendor_id' => $vendor_id, ':storage_no' => $storage_no);
                $delivery_id = OmsDatabase::$oms_db->fetchOne('id', 't_vip_delivery_info', $where, $param);
                if (!empty($delivery_id)) {
                    OmsDatabase::$oms_db->getPdo()->beginTransaction();
                    $del_res = OmsDatabase::$oms_db->delete('t_vip_delivery_detail','dlv_id=:dlv_id',array(':dlv_id' => $delivery_id['id']));
                    //修改拣货单状态信息
                    $pick_where = 'vendor_id=:vendor_id AND storage_no=:storage_no';
                    $whereParams = array(':vendor_id'=>$vendor_id,':storage_no'=>$storage_no);
                    $update_res= OmsDatabase::$oms_db->update('t_vip_pick_list',array('status'=>'已下发'),$pick_where,$whereParams);
                    if ($del_res && $update_res) {
                        OmsDatabase::$oms_db->getPdo()->commit();
                        echo '删除指定单号的出仓明细并操作数据库成功';exit;
                    } else {
                        OmsDatabase::$oms_db->getPdo()->rollBack();
                        echo '删除指定单号的出仓明细成功但操作数据库失败';exit;
                    }
                }
            } else {
                throw new Exception('删除出仓明细失败:'.$data['returnMessage']);
            }
        } catch (\Exception $e) {
            return $e->getMessage();
        }
    }

}