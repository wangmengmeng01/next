<?php
/**
 * Notes:修改出仓单信息
 * Date: 2019/1/10
 * Time: 13:59
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';

class editDelivery extends erpRequest
{
    public function edit($jsonData)
    {
        try {
            # 数据检查
            if (empty($jsonData)) {
                return '错误：请求数据不能为空';
            }
            $dataArr = json_decode($jsonData,true);
            $params = array(
                'method' => 'editDelivery',
                'data' => $jsonData
            );
            $res = $this->send($params);
            $data = json_decode($res, true);
            if($data['returnCode'] === '0') {
                //更新出仓单信息表
                $update_arr = array();
                foreach ($dataArr as $k => $v) {
                    if (!in_array($k,array('vendor_id','storage_no','warehouse','carrier_code'))) {
                        $update_arr[$k] = $v;
                    }
                }
                $update_arr['update_time'] = date('Y-m-d H:i:s');
                $result = OmsDatabase::$oms_db->update('t_vip_delivery_info', $update_arr, 'vendor_id=:vendor_id and storage_no=:storage_no', array(':vendor_id' => $dataArr['vendor_id'],':storage_no'=>$dataArr['storage_no']));
                if ($result) {
                    echo '修改出仓单信息成功并存入数据库成功';exit;
                } else {
                    echo '修改出仓单信息成功但存入数据库失败';exit;
                }
            } else {
                throw new Exception('修改出仓单信息失败:'.$data['returnMessage']);
            }
        } catch (\Exception $e) {
            return $e->getMessage();
        }
    }

}