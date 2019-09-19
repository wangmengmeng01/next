<?php
/**
 * Notes:如意达拼多多电子面单商家授权接口类
 * Date: 2019/3/22
 * Time: 10:30
 */
require API_ROOT . 'router/interface/erp/pdd/erpRequest.php';
class pddWaybillAuthorization extends erpRequest
{
    /***
     * Notes:商家授权信息处理方法
     * Date: 2019/3/22
     * Time: 13:36
     * @param $params  商家授权信息
     * @return array
     */
    public function authorize($params)
    {
        try {
            $token_info = json_decode($params['auth_session'],true);
            $seller_id = $token_info['owner_id'];
            $access_token = $token_info['access_token'];
            $refresh_token = $token_info['refresh_token'];
            $shop_name = $token_info['owner_name'];
            $db = OmsDatabase::$oms_db;
            //授权信息表csk_seller_access_token
            $sellerInfo = $db->fetchOne('*', 'csk_seller_access_token', 'seller_id=:seller_id and platform_elec= :platform_elec', array(':seller_id' => $seller_id, ':platform_elec' => 'PDD'));
            //判断是否存在记录
            if (empty($sellerInfo)) {
                $insert_arr = array(
                    'seller_id' => $seller_id,
                    'access_token' => $access_token,
                    'refresh_token' => $refresh_token,
                    'shop_name' => $shop_name,
                    'platform_elec' => 'PDD',
                    'time' => date("Y-m-d H:i:s"),
                );
                $seller_res = $db->insert('csk_seller_access_token', $insert_arr,false);
            } else {
                $update_arr = array(
                    'access_token' => $access_token,
                    'refresh_token' => $refresh_token,
                    'shop_name' => $shop_name,
                    'time' => date("Y-m-d H:i:s")
                );
                $seller_res = $db->update('csk_seller_access_token', $update_arr, 'seller_id=:seller_id and platform_elec=:platform_elec', array(':seller_id' => $seller_id, ':platform_elec' => 'PDD'));
            }
            if ($seller_res === true) {
                return $this->msgObj->outputPdd(2, '0000', '拼多多电子面单商家授权接口访问成功，且更新数据库成功');
            } else {
                return $this->msgObj->outputPdd(3,'S003','拼多多电子面单商家授权接口访问成功，但更新数据库失败');
            }
        } catch (Exception $e) {
            $logName = date('Ymd') . '_pdd_execute_log.log';
            error_log(print_r($e->getMessage(), 1) . PHP_EOL, 3, LOG_PATH . $logName);
            return $this->msgObj->outputPdd(0,$e->getCode(),$e->getMessage());
        }
    }
}