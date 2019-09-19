<?php

/**
 * Notes:拼多多erp请求基类
 * Date: 2019/3/21
 * Time: 10:34
 */
class erpRequest
{
    public function __construct()
    {
        $this->msgObj = new msg();
    }

    /***
     * Notes:生成签名
     * Date: 2019/1/31
     * Time: 12:02
     * @param $appSecret
     * @param $param
     * @return string
     */
    public function mk_sign($params)
    {
        ksort($params);
        $signStr = '';
        foreach ($params as $k => $v) {
            $signStr .= $k . $v;
        }
        $signStr = strtoupper(md5(pdd_service::$_client_secret . $signStr . pdd_service::$_client_secret));
        return $signStr;
    }


    public function send($access_token, $params = array())
    {
        # 请求时间
        pdd_service::$_requstPddTime = date("Y-m-d H:i:s");
        $apiParams = array(
            'type' => pdd_service::$_method,
            'client_id' => pdd_service::$_client_id,
            'access_token' => $access_token,
            'timestamp' => round($this->currentTimeMillis() / 1000),
            'data_type' => 'JSON',
        );
        if (!empty($params)) {
            $apiParams = array_merge($apiParams, $params);
        }
        $apiParams['sign'] = $this->mk_sign($apiParams);
        $headers = array("Content-Type: application/json");
        $utilObj = new util();
        $rs = $utilObj->curlData(PDD_URL, json_encode($apiParams), 60,$headers);
        # 响应时间
        pdd_service::$_responseOmsTime = date("Y-m-d H:i:s");
        pdd_service::$_pddReturnStr = $rs;
        //接口层记录日志
        $logTxt = array(
            'api_url'    => pdd_service::$_requestUrl,
            'api_method' => pdd_service::$_method,
            'api_params' => $apiParams,
        );
        if ($rs == '') {
            return $this->msgObj->outputPdd(0, 'S003', '请求超时', $logTxt);
        } else {
            $response = json_decode($rs, true);
            $logTxt['return_msg'] = $rs;
            if (isset($response['error_response'])) {
                $code = $response['error_response']['error_code'];
                $msg = $response['error_response']['error_msg'];
                return $this->msgObj->outputPdd(4, $code, $msg, $logTxt);
            } else {
                return $this->msgObj->outputPdd(1, '0000', $rs, $logTxt);
            }
        }
    }

    /**
     * 根据WMS的货主来获取表中维护的access_token  云打印接口
     * @param  $params 请求数据
     * @return mixed
     */
    public function getTokenByCustomerCode($params)
    {
        $db = OmsDatabase::$oms_db;
        $apiParams = array(
            'method' => pdd_service::$_method,
            'v' => pdd_service::$_v,
            'timestamp' => date("Y-m-d H:i:s"),
            'sign' => strtoupper(base64_encode(md5(pdd_service::$_secret . pdd_service::$_data . pdd_service::$_secret))),
            'data' => pdd_service::$_data
        );

        $logTxt = array(
            'api_url' => pdd_service::$_requestUrl,
            'api_method' => pdd_service::$_method,
            'api_params' => $apiParams,
        );
        if (!empty($params['customerCode']) || !empty($params['warehouseCode']) || !empty($params['platformMall'])) {
            $platformElec = pdd_service::$_platFormElec;
            $where = 'customer_code = :customerCode and platform_elec = :platform_elec and platform_mall = :platform_mall and ship_addr_code = :ship_addr_code and is_valid =1 ';
            $whereParams = array(
                ':customerCode' => $params['customerCode'],
                ':platform_elec' => $platformElec,
                ':platform_mall' => $params['platformMall'],
                ':ship_addr_code' => $params['warehouseCode']
            );
            if (!empty($params['shopName'])) {
                $where .= 'AND shop_name = :shop_name';
                $whereParams[':shop_name'] = $params['shopName'];
            }
            $sellerInfo = $db->fetchOne('seller_id,platform_elec', 'csk_seller_customerid_relation', $where, $whereParams);
            if (!empty($sellerInfo)) {
                if (!empty($sellerInfo['seller_id']) && !empty($sellerInfo['platform_elec'])) {
                    $tokenInfo = $db->fetchOne('access_token,seller_id', 'csk_seller_access_token', 'seller_id = :seller_id and platform_elec = :platform_elec', array(':seller_id' => $sellerInfo['seller_id'], ':platform_elec' => $sellerInfo['platform_elec']));
                    if (!empty($tokenInfo)) {
                        $infoArr = array();
                        $infoArr['access_token'] = $tokenInfo['access_token'];
                        $infoArr['seller_id'] = $tokenInfo['seller_id'];
                        //获取地址信息

                        //匹配pdd承运商编码
                        $providerInfo = $this->getPddProvider($params['wp_code'],$tokenInfo['seller_id']);
                        $addrInfo = $db->fetchOne('cp_code,ship_prov,ship_city,ship_county,ship_town,ship_detail_address', 'csk_seller_waybill_info', 'seller_id = :seller_id and ship_addr_code = :ship_addr_code and cp_code=:cp_code and is_jd = 2 ', array(':seller_id' => $tokenInfo['seller_id'], ':ship_addr_code' => $params['warehouseCode'],'cp_code' => $providerInfo['provider_code']));
                        if (!empty($addrInfo['ship_prov']) && !empty($addrInfo['ship_detail_address'])) {
                            $infoArr['ship_addr_info'] = $addrInfo;
                            return $infoArr;
                        } else {
                            return $this->msgObj->outputPdd(0, 'S003', '该货主'. $params['customerCode'] .'对应的商家没有相应的发货地址，请联系实施人员', $logTxt);
                        }
                    } else {
                        return $this->msgObj->outputPdd(0, 'S003', '该货主'. $params['customerCode'] .'对应的商家没有维护相应的授权口令，请联系实施人员', $logTxt);
                    }
                } else {
                    return $this->msgObj->outputPdd(0, 'S003', '该货主'. $params['customerCode'] . '对应的商家信息不完整，请联系实施人员', $logTxt);
                }
            } else {
                return $this->msgObj->outputPdd(0, 'S003', '该货主'. $params['customerCode'] . '无对应的商家信息，请联系实施人员进行相关配置', $logTxt);
            }
        } else {
            return $this->msgObj->outputPdd(0, 'S003', '请求信息中获取商家ID的关键条件不完整', $logTxt);
        }
    }

    /**
     * 根据WMS的货主来获取表中维护的access_token 不获取发货地址
     * @param  $params 请求数据
     * @return mixed
     */
    public function getTokenForSearch($params)
    {
        $db = OmsDatabase::$oms_db;
        $apiParams = array(
            'method' => pdd_service::$_method,
            'v' => pdd_service::$_v,
            'timestamp' => date("Y-m-d H:i:s"),
            'sign' => strtoupper(base64_encode(md5(pdd_service::$_secret . pdd_service::$_data . pdd_service::$_secret))),
            'data' => pdd_service::$_data
        );

        $logTxt = array(
            'api_url' => pdd_service::$_requestUrl,
            'api_method' => pdd_service::$_method,
            'api_params' => $apiParams,
        );
        if (!empty($params['customerCode']) || !empty($params['warehouseCode']) || !empty($params['platformMall'])) {
            $platformElec = pdd_service::$_platFormElec;
            $where = 'customer_code = :customerCode and platform_elec = :platform_elec and platform_mall = :platform_mall and ship_addr_code = :ship_addr_code and is_valid =1 ';
            $whereParams = array(
                ':customerCode' => $params['customerCode'],
                ':platform_elec' => $platformElec,
                ':platform_mall' => $params['platformMall'],
                ':ship_addr_code' => $params['warehouseCode']
            );
            if (!empty($params['shopName'])) {
                $where .= 'AND shop_name = :shop_name';
                $whereParams[':shop_name'] = $params['shopName'];
            }
            $sellerInfo = $db->fetchOne('seller_id,platform_elec', 'csk_seller_customerid_relation', $where, $whereParams);
            if (!empty($sellerInfo)) {
                if (!empty($sellerInfo['seller_id']) && !empty($sellerInfo['platform_elec'])) {
                    $tokenInfo = $db->fetchOne('access_token,seller_id', 'csk_seller_access_token', 'seller_id = :seller_id and platform_elec = :platform_elec', array(':seller_id' => $sellerInfo['seller_id'], ':platform_elec' => $sellerInfo['platform_elec']));
                    if (!empty($tokenInfo)) {
                        $infoArr = array();
                        $infoArr['access_token'] = $tokenInfo['access_token'];
                        $infoArr['seller_id'] = $tokenInfo['seller_id'];
                        return $infoArr;
                    } else {
                        return $this->msgObj->outputPdd(0, 'S003', '该货主'. $params['customerCode'] .'对应的商家没有维护相应的授权口令，请联系实施人员', $logTxt);
                    }
                } else {
                    return $this->msgObj->outputPdd(0, 'S003', '该货主'. $params['customerCode'] . '对应的商家信息不完整，请联系实施人员', $logTxt);
                }
            } else {
                return $this->msgObj->outputPdd(0, 'S003', '该货主'. $params['customerCode'] . '无对应的商家信息，请联系实施人员进行相关配置', $logTxt);
            }
        } else {
            return $this->msgObj->outputPdd(0, 'S003', '请求信息中获取商家ID的关键条件不完整', $logTxt);
        }
    }

    /***
     * Notes:获取PDD承运商编码，ID
     * Date: 2019/4/29
     * Time: 9:56
     * @param $wp_code wms承运商编码
     * @param $sellerId 商家编码
     * @return bool|mixed
     */
    public function getPddProvider($wp_code,$sellerId){
        $providerInfo = OmsDatabase::$oms_db->fetchOne('provider_code', 'csk_merchant_deploy', 'vendor_code=:vendor_code AND wms_provider_code=:wms_provider_code AND type =1', array(':vendor_code' => $sellerId,':wms_provider_code'=>$wp_code));
        return $providerInfo;
    }


    protected function currentTimeMillis()
    {
        list($t1, $t2) = explode(' ', microtime());
        return (float)(floatval($t1) + floatval($t2)) * 1000;
    }
}