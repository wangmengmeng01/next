<?php
/**
 * Notes:拼多多电子面单云打印接口
 * Date: 2019/3/22
 * Time: 15:24
 */
require API_ROOT . 'router/interface/erp/pdd/erpRequest.php';

class pddWaybillGet extends erpRequest
{
    /***
     * Notes:电子面单云打印接口处理方法
     * Date: 2019/3/22
     * Time: 13:36
     * @param $params
     * @return array
     */
    public function get($params)
    {
        try {
            if (empty($params)) {
                return $this->outputPdd(0, 'S003', '请求数据不能为空');
            }
            //获取access_token、发货人信息
            $tokenInfo = $this->getTokenByCustomerCode($params);
            if (!array_key_exists('access_token', $tokenInfo)) {//返回数组有键为access_token就是返回正确信息
                return $tokenInfo;
            } else {
                $accessToken = $tokenInfo['access_token'];
                $sellerId = $tokenInfo['seller_id'];
                $cp_code = $tokenInfo['ship_addr_info']['cp_code'];
                $shipProv = $tokenInfo['ship_addr_info']['ship_prov'];
                $shipCity = $tokenInfo['ship_addr_info']['ship_city'];
                $shipCounty = $tokenInfo['ship_addr_info']['ship_county'];
                $shipTown = $tokenInfo['ship_addr_info']['ship_town'];
                $shipDetailAddress = $tokenInfo['ship_addr_info']['ship_detail_address'];
            }
            //拼接参数
            $trade_order_info_dtos = $params['trade_order_info_dtos'];
            foreach ($trade_order_info_dtos as &$dtos) {
                $dtos['user_id'] = $sellerId;
            }
            $apiArr = array(
                'wp_code' => $cp_code,
                'sender' => array(
                    'address' => array(
                        'city' => $shipCity,
                        'detail' => $shipDetailAddress,
                        'district' => $shipCounty,
                        'province' => $shipProv,
                        'town' => $shipTown,
                    ),
                    'name' => $params['sender']['name'],   //发货人姓名
                    'phone' => $params['sender']['phone']?$params['sender']['phone']:'',
                    'mobile' => $params['sender']['mobile']?$params['sender']['mobile']:'',
                ),
                'trade_order_info_dtos' => $trade_order_info_dtos,
            );
            $req = json_encode($apiArr);
            $reqParam = array('param_waybill_cloud_print_apply_new_request' => $req);
            $response = $this->send($accessToken, $reqParam);
            if (!$response['error_response']) {
                $resArr = json_decode(pdd_service::$_pddReturnStr,true);
                $resArr['pdd_waybill_get_response']['mall_id'] = $sellerId; //使⽤用者ID，即店铺mall_id，取号账号
                $response['addon']['return_msg'] = json_encode($resArr);
                return $this->msgObj->outputPdd(1, '0000', $response['addon']['return_msg'], $response['addon']);
            } else {
                $code = $response['error_response']['error_code'];
                $msg = $response['error_response']['error_msg'];
                return $this->msgObj->outputPdd(4, $code, $msg, $response['addon']);
            }
        } catch (Exception $e) {
            $logName = date('Ymd') . '_pdd_execute_log.log';
            error_log(print_r($e->getMessage(), 1) . PHP_EOL, 3, LOG_PATH . $logName);
            return $this->msgObj->outputPdd(0,$e->getCode(),$e->getMessage());
        }
    }
}







