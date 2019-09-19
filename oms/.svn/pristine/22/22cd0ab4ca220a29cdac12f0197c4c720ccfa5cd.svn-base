<?php
/**
 * JD请求处理类
 * User: Renee
 * Date: 2018/3/21
 * Time: 15:13
 */
class JdRequest {
    public $c = null;
    public $msgObj = null;
    public $logTxt = null;

    public function __construct()
    {
        $this->c = new JdClient();
        $this->c->appKey = JD_APP_KEY;
        $this->c->appSecret = JD_APP_SECRET;

        $this->msgObj = new msg();

        //日志参数
        $this->logTxt = array(
            'api_url' => OMS_API_URL,
            'api_method' => jd_service::$_method,
            'api_params' => array(
                'method'    => jd_service::$_method,
                'v'         => jd_service::$_v,
                'timestamp' => date("Y-m-d H:i:s"),
                'jsonData'  => jd_service::$_data
            )
        );
    }

    /**
     * 获取京东商家发货地址
     */
    public function getSenderInfo($params) {
        try {
            $db = new DbAction();
            $sellerInfo = array();

            if (!empty($params['vendorCode'])) {
                $sellerInfo['seller_id'] = $params['vendorCode'];
                $sellerInfo['platform_elec'] = 'JD';
            } else {
                //根据货主编码+店铺名称=>商家id
                if (!empty($params['customerCode']) || !empty($params['vendorName'])) {

                    $sql = "SELECT seller_id,platform_elec FROM csk_seller_customerid_relation WHERE platform_elec='JD' AND shop_name=:shop_name AND customer_code=:customer_code";
                    $values = array(
                        ':shop_name'    =>$params['vendorName'],
                        ':customer_code'=>$params['customerCode'],
                    );
                    $sellerInfo = $db->fetchOne($sql,$values);
                } else {
                    return $this->msgObj->outputJd(1, '请求信息中获取JD商家ID的关键条件不完整', '', $this->logTxt);
                }
            }

            if (!empty($sellerInfo)) {
                if (!empty($sellerInfo['seller_id']) && !empty($sellerInfo['platform_elec'])) {
                    $findTokenSql = "SELECT access_token,pop_id FROM csk_seller_access_token WHERE pop_id = :pop_id AND platform_elec='JD'";
                    $values2 = array(
                        ':pop_id'=>$sellerInfo['seller_id']
                    );
                    $tokenInfo = $db->fetchOne($findTokenSql,$values2);

                    if (!empty($tokenInfo)) {
                        $infoArr = array();
                        $infoArr['access_token'] = $tokenInfo['access_token'];
                        $infoArr['seller_id'] = $tokenInfo['pop_id'];

                        $method = jd_service::$_method;
                        if ($method == 'jingdong.ldop.alpha.waybill.receive') {
                            $providerInfo = $this->getJdProvider($params,$tokenInfo['pop_id']);

                            //获取地址信息
                            $findAddrSql = "SELECT ship_prov,ship_prov_id,ship_city,ship_city_id,ship_county,ship_county_id,ship_detail_address,branch_code,settlement_code FROM csk_seller_waybill_info WHERE seller_id = :seller_id AND ship_addr_code = :ship_addr_code AND cp_code=:cp_code AND is_jd=:is_jd";
                            /*$values1 = array(
                                ':seller_id'     =>$tokenInfo['pop_id'],
                                ':ship_addr_code'=>$params['warehouseCode'],
                                ':cp_code'       =>$params['providerCode'],
                                ':is_jd'         =>'1',
                            );*/
                            $values1 = array(
                                ':seller_id'     =>$tokenInfo['pop_id'],
                                ':ship_addr_code'=>$params['warehouseCode'],
                                ':cp_code'       =>$providerInfo['provider_code'],
                                ':is_jd'         =>'1',
                            );
                            $addrInfo = $db->fetchOne($findAddrSql,$values1);

                            if (!empty($addrInfo['ship_prov']) &&
                                !empty($addrInfo['ship_prov_id']) &&
                                !empty($addrInfo['ship_city']) &&
                                !empty($addrInfo['ship_city_id']) &&
                                !empty($addrInfo['ship_county']) &&
                                !empty($addrInfo['ship_county_id']) &&
                                !empty($addrInfo['ship_detail_address'])) {
                                $infoArr['ship_addr_info'] = $addrInfo;
                                return $infoArr;
                            } else {
                                return $this->msgObj->outputJd(1, '该货主'. $params['customerCode'] .'对应的商家没有相应的发货地址或不完整，请联系实施人员', '', $this->logTxt);
                            }
                        } else {
                            return $infoArr;
                        }
                    } else {
                        return $this->msgObj->outputJd(1, '该货主'. $params['customerCode'] .'对应的商家没有维护相应的授权口令，请联系实施人员', '', $this->logTxt);
                    }
                } else {
                    return $this->msgObj->outputJd(1, '该货主'. $params['customerCode'] . '对应的商家信息不完整，请联系实施人员', '', $this->logTxt);
                }
            } else {
                return $this->msgObj->outputJd(1, '无对应的JD商家信息，请联系实施人员进行相关配置', '', $this->logTxt);
            }
        } catch(Exception $e){
            return $this->msgObj->outputJd(1, $e->getMessage(), '', $this->logTxt);
        }
    }

    /**
     * 获取JD承运商编码，ID
     * @param $params 请求信息
     * @return JD承运商信息
     */
    public function getJdProvider($params,$sellerId){
        $db = new DbAction();
        $sql = "SELECT provider_code,provider_id FROM csk_merchant WHERE vendor_code=:vendor_code AND wms_provider_code=:wms_provider_code";
        $values = array(
            ':vendor_code'      =>$sellerId,
            ':wms_provider_code'=>$params['providerCode'],
        );
        $providerInfo = $db->fetchOne($sql,$values);
        return $providerInfo;
    }
}