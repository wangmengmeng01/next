<?php
/**
 * Notes:同步网易考拉平台的店铺的授权码
 * Date: 2019/1/30
 * Time: 16:55
 */

require_once API_ROOT . '/router/interface/erp/custom/common/erpRequest.php';

class erpGetKaolaStoreInfo extends erpRequest
{
    public function update($param)
    {
        try {
            // 数据检查
            if (empty($param)) {
                return $this->msgObj->outputCustom(false, '错误：请求数据不能为空');
            }
            $store_info = OmsDatabase::$oms_db->fetchOne('store_id', 't_kaola_store_info', 'store_id=:store_id', array(':store_id' => $param['StoreIDForKeepOn']));
            if ($store_info) {
                //更新信息
                $update_arr = array(
                    'access_token' => $param['StoreInfo']['access_token'],
                    'kaola_key' => $param['StoreInfo']['Kaola_key'],
                    'kaola_secert' => $param['StoreInfo']['Kaola_secert'],
                );
                $res = OmsDatabase::$oms_db->update('t_kaola_store_info', $update_arr, 'store_id=:store_id', array(':store_id' => $param['StoreIDForKeepOn']));
            } else {
                //插入信息
                $insert_arr =array(
                    'store_id' => $param['StoreIDForKeepOn'],
                    'access_token' => $param['StoreInfo']['access_token'],
                    'kaola_key' => $param['StoreInfo']['Kaola_key'],
                    'kaola_secert' => $param['StoreInfo']['Kaola_secert'],
                );
                $res = OmsDatabase::$oms_db->insert('t_kaola_store_info', $insert_arr);
            }
            if (!$res) {
                return $this->msgObj->outputCustom(false, '同步授权码成功，但数据入库失败');
            }
            return $this->msgObj->outputCustom(true, '同步授权码成功且数据入库成功');
        } catch (Exception $e) {
            return $this->msgObj->outputCustom(false, $e->getMessage());
        }
    }
}