<?php
class cnRequest {
    public $msgObj = null;
    
    public function __construct() {
        $this->msgObj = new msg();
    }
    
    /**
     * 根据WMS的货主来获取表中维护的access_token
     * @param  $params 请求数据
     * @return mixed
     */
    public function getTokenByCustomerCode ($params) {
        global $db;
        $apiParams = array(
            'method' => cainiao_service::$_method,
            'v' => cainiao_service::$_v,
            'timestamp' => date("Y-m-d H:i:s"),
            'sign' => strtoupper(base64_encode(md5(CAINIAO_APP_SECRET.cainiao_service::$_data.CAINIAO_APP_SECRET))),
            'data' => cainiao_service::$_data
        );
        
        $logTxt = array(
                'api_url' => OMS_API_URL,
                'api_method' => cainiao_service::$_method,
                'api_params' => $apiParams
        );
        
        if (!empty($params['customerCode']) || !empty($params['warehouseCode']) || !empty($params['platformMall'])) {
            $platformElec = cainiao_service::$_platFormElec;
            //$shopName = empty($params['shopName']) ? '' : $params['shopName'] ;
            if (!empty($params['shopName'])) {
                $shopName = $params['shopName'];
                $extraSql = " AND shop_name = :shop_name";
            } else {
                $extraSql = '';
            }
            $sellerSql = "SELECT seller_id,platform_elec FROM csk_seller_customerid_relation WHERE customer_code = :customer_code AND platform_elec = :platform_elec AND platform_mall = :platform_mall AND ship_addr_code = :ship_addr_code AND is_valid=1" . $extraSql;
            $sellerModel = $db->prepare($sellerSql);
            $sellerModel->bindParam(':customer_code', $params['customerCode']);
            $sellerModel->bindParam(':platform_elec', $platformElec);
            $sellerModel->bindParam(':platform_mall', $params['platformMall']);
            $sellerModel->bindParam(':ship_addr_code', $params['warehouseCode']);
            if (!empty($params['shopName'])) {
                $sellerModel->bindParam(':shop_name', $shopName);
            }
            $sellerModel->execute();
            $sellerInfo = $sellerModel->fetch(PDO::FETCH_ASSOC);
            
            if (!empty($sellerInfo)) {
                if (!empty($sellerInfo['seller_id']) && !empty($sellerInfo['platform_elec'])) {
                    $findTokenSql = "SELECT access_token,seller_id,product_type FROM csk_seller_access_token WHERE seller_id = :seller_id AND platform_elec = :platform_elec";
                    $tokenModel = $db->prepare($findTokenSql);
                    $tokenModel->bindParam(':seller_id', $sellerInfo['seller_id']);
                    $tokenModel->bindParam(':platform_elec', $sellerInfo['platform_elec']);
                    $tokenModel->execute();
                    $tokenInfo = $tokenModel->fetch(PDO::FETCH_ASSOC);
                    if (!empty($tokenInfo)) {
                        $infoArr = array();
                        $infoArr['access_token'] = $tokenInfo['access_token'];
                        $infoArr['seller_id'] = $tokenInfo['seller_id'];
                        $infoArr['product_type'] = $tokenInfo['product_type'];
                        
                        //获取地址信息
                        $findAddrSql = "SELECT ship_prov,ship_city,ship_county,ship_town,ship_detail_address FROM csk_seller_waybill_info WHERE seller_id = :seller_id AND ship_addr_code = :ship_addr_code AND cp_code=:cp_code";
                        $findModel = $db->prepare($findAddrSql);
                        $findModel->bindParam(':seller_id', $tokenInfo['seller_id']);
                        $findModel->bindParam(':ship_addr_code', $params['warehouseCode']);
                        $findModel->bindParam(':cp_code', $params['cp_code']);
                        $findModel->execute();
                        $addrInfo = $findModel->fetch(PDO::FETCH_ASSOC);
                        if (!empty($addrInfo['ship_prov']) && !empty($addrInfo['ship_detail_address'])) {
                            $infoArr['ship_addr_info'] = $addrInfo;
                            return $infoArr;
                        } else {
                            return $this->msgObj->outputCainiao(0, 'S003', '该货主'. $params['customerCode'] .'对应的商家没有相应的发货地址，请联系实施人员', $logTxt);
                        }
                    } else {
                        return $this->msgObj->outputCainiao(0, 'S003', '该货主'. $params['customerCode'] .'对应的商家没有维护相应的授权口令，请联系实施人员', $logTxt);
                    }
                } else {
                    return $this->msgObj->outputCainiao(0, 'S003', '该货主'. $params['customerCode'] . '对应的商家信息不完整，请联系实施人员', $logTxt);
                }
            } else {
                return $this->msgObj->outputCainiao(0, 'S003', '该货主'. $params['customerCode'] . '无对应的商家信息，请联系实施人员进行相关配置', $logTxt);
            }
        } else {
            return $this->msgObj->outputCainiao(0, 'S003', '请求信息中获取商家ID的关键条件不完整', $logTxt);
        }
    }
    
    /**
     * 获取token
     * @param unknown $params
     */
    public function getTokenForSearch ($params) {
        global $db;
        $apiParams = array(
            'method' => cainiao_service::$_method,
            'v' => cainiao_service::$_v,
            'timestamp' => date("Y-m-d H:i:s"),
            'sign' => strtoupper(base64_encode(md5(CAINIAO_APP_SECRET.cainiao_service::$_data.CAINIAO_APP_SECRET))),
            'data' => cainiao_service::$_data
        );
        
        $logTxt = array(
            'api_url' => OMS_API_URL,
            'api_method' => cainiao_service::$_method,
            'api_params' => $apiParams
        );
        
        if (!empty($params['customerCode']) || !empty($params['warehouseCode']) || !empty($params['platformMall'])) {
            $platformElec = cainiao_service::$_platFormElec;
            //$shopName = empty($params['shopName']) ? '' : $params['shopName'] ;
            if (!empty($params['shopName'])) {
                $shopName = $params['shopName'];
                $extraSql = " AND shop_name = :shop_name";
            } else {
                $extraSql = '';
            }
            $sellerSql = "SELECT seller_id,platform_elec FROM csk_seller_customerid_relation WHERE customer_code = :customer_code AND platform_elec = :platform_elec AND platform_mall = :platform_mall AND ship_addr_code = :ship_addr_code AND is_valid=1" . $extraSql;
            $sellerModel = $db->prepare($sellerSql);
            $sellerModel->bindParam(':customer_code', $params['customerCode']);
            $sellerModel->bindParam(':platform_elec', $platformElec);
            $sellerModel->bindParam(':platform_mall', $params['platformMall']);
            $sellerModel->bindParam(':ship_addr_code', $params['warehouseCode']);
            if (!empty($params['shopName'])) {
                $sellerModel->bindParam(':shop_name', $shopName);
            }
            $sellerModel->execute();
            $sellerInfo = $sellerModel->fetch(PDO::FETCH_ASSOC);
        
            if (!empty($sellerInfo)) {
                if (!empty($sellerInfo['seller_id']) && !empty($sellerInfo['platform_elec'])) {
                    $findTokenSql = "SELECT access_token,seller_id,product_type FROM csk_seller_access_token WHERE seller_id = :seller_id AND platform_elec = :platform_elec";
                    $tokenModel = $db->prepare($findTokenSql);
                    $tokenModel->bindParam(':seller_id', $sellerInfo['seller_id']);
                    $tokenModel->bindParam(':platform_elec', $sellerInfo['platform_elec']);
                    $tokenModel->execute();
                    $tokenInfo = $tokenModel->fetch(PDO::FETCH_ASSOC);
                    if (!empty($tokenInfo['access_token'])) {
                        return $tokenInfo;
                    } else {
                        return $this->msgObj->outputCainiao(0, 'S003', '该货主'. $params['customerCode'] .'对应的商家没有维护相应的授权口令，请联系实施人员', $logTxt);
                    }
                } else {
                    return $this->msgObj->outputCainiao(0, 'S003', '该货主'. $params['customerCode'] . '对应的商家信息不完整，请联系实施人员', $logTxt);
                }
            } else {
                return $this->msgObj->outputCainiao(0, 'S003', '该货主'. $params['customerCode'] . '无对应的商家信息，请联系实施人员进行相关配置', $logTxt);
            }
        } else {
            return $this->msgObj->outputCainiao(0, 'S003', '请求信息中获取商家ID的关键条件不完整', $logTxt);
        }
    }
    
    /**
     * 根据RYD发送过来的seller_id来获取对应的access_token
     * @param unknown $params
     */
    public function getTokenBySellerId ($params) {
        global $db;
        
        $apiParams = array(
            'method' => cainiao_service::$_method,
            'v' => cainiao_service::$_v,
            'timestamp' => date("Y-m-d H:i:s"),
            'sign' => strtoupper(base64_encode(md5(CAINIAO_APP_SECRET.cainiao_service::$_data.CAINIAO_APP_SECRET))),
            'data' => cainiao_service::$_data
        );
        
        $logTxt = array(
            'api_url' => OMS_API_URL,
            'api_method' => cainiao_service::$_method,
            'api_params' => $apiParams
        );
        
        $sellerId = $params['seller_id'];
        $platFormElec = cainiao_service::$_platFormElec;
        $sql = 'SELECT access_token,product_type FROM csk_seller_access_token WHERE seller_id = :seller_id AND platform_elec = :platform_elec';
        $model = $db->prepare($sql);
        $model->bindParam(':seller_id', $sellerId);
        $model->bindParam(':platform_elec', $platFormElec);
        $model->execute();
        $accessTokenInfo = $model->fetch(PDO::FETCH_ASSOC);
        
        if (!empty($accessTokenInfo)) {
            $infoArr = array();
            $infoArr['seller_id'] = $params['seller_id'];
            $infoArr['access_token'] = $accessTokenInfo['access_token'];
            $infoArr['product_type'] = $accessTokenInfo['product_type'];
            
            $infoArr['ship_addr_info']['ship_prov'] = $params['sender']['address']['province'];
            $infoArr['ship_addr_info']['ship_city'] = empty($params['sender']['address']['city']) ? '' : $params['sender']['address']['city'];
            $infoArr['ship_addr_info']['ship_county'] = empty($params['sender']['address']['district']) ? '' : $params['sender']['address']['district'];
            $infoArr['ship_addr_info']['ship_town'] = empty($params['sender']['address']['town']) ? '' : $params['sender']['address']['town'];
            $infoArr['ship_addr_info']['ship_detail_address'] = $params['sender']['address']['detail'];
            return $infoArr;
        } else {
            return $this->msgObj->outputCainiao(0, 'S003', '该商家'. $params['seller_id'] .'没有维护相应的授权口令，请联系实施人员', $logTxt);
        }
    }
    
    /**
     * 获取请求数据与数据库字段对应关系
     */
    public function get_database_relation ($cname) {
        $returnArr = array();
        if ($cname == 'search_addr') {
            $returnArr['province'] = 'ship_prov';
            $returnArr['city'] = 'ship_city';
            $returnArr['area'] = 'ship_county';
            $returnArr['town'] = 'ship_town';
            $returnArr['address_detail'] = 'ship_detail_address';
            $returnArr['waybill_address_id'] = 'waybill_address_id';
        } elseif ($cname == 'search_branch') {
            $returnArr['seller_id'] = 'seller_id';
            $returnArr['branch_code'] = 'branch_code';
            $returnArr['branch_name'] = 'branch_name';
            $returnArr['print_quantity'] = 'print_quantity';
            $returnArr['cancel_quantity'] = 'cancel_quantity';
            $returnArr['allocated_quantity'] = 'allocated_quantity';
            $returnArr['quantity'] = 'quantity';
        } elseif ($cname == 'cp') {
            $returnArr['cp_type'] = 'cp_type';
            $returnArr['cp_code'] = 'cp_code';
        } elseif ($cname == 'search_all') {
            $returnArr['province'] = 'ship_prov';
            $returnArr['city'] = 'ship_city';
            $returnArr['area'] = 'ship_county';
            $returnArr['town'] = 'ship_town';
            $returnArr['address_detail'] = 'ship_detail_address';
            $returnArr['waybill_address_id'] = 'waybill_address_id';
            $returnArr['seller_id'] = 'seller_id';
            $returnArr['branch_code'] = 'branch_code';
            $returnArr['branch_name'] = 'branch_name';
            $returnArr['print_quantity'] = 'print_quantity';
            $returnArr['cancel_quantity'] = 'cancel_quantity';
            $returnArr['allocated_quantity'] = 'allocated_quantity';
            $returnArr['quantity'] = 'quantity';
            $returnArr['cp_type'] = 'cp_type';
            $returnArr['cp_code'] = 'cp_code';
        } elseif ($cname == 'seller_authorization') {
            $returnArr['seller_id'] = 'seller_id';
            $returnArr['access_token'] = 'access_token';
            $returnArr['platform_elec'] = 'platform_elec';
            $returnArr['time'] = 'time';
        } elseif ($cname == 'seller_waybill_info') {
            $returnArr['province'] = 'ship_prov';
            $returnArr['city'] = 'ship_city';
            $returnArr['district'] = 'ship_county';
            $returnArr['town'] = 'ship_town';
            $returnArr['detail'] = 'ship_detail_address';
            $returnArr['seller_id'] = 'seller_id';
            $returnArr['branch_code'] = 'branch_code';
            $returnArr['branch_name'] = 'branch_name';
            $returnArr['branch_status'] = 'branch_status';
            $returnArr['print_quantity'] = 'print_quantity';
            $returnArr['cancel_quantity'] = 'cancel_quantity';
            $returnArr['allocated_quantity'] = 'allocated_quantity';
            $returnArr['quantity'] = 'quantity';
            $returnArr['cp_type'] = 'cp_type';
            $returnArr['cp_code'] = 'cp_code';
        } elseif ($cname == 'seller_waybill_branch') {
            $returnArr['seller_id'] = 'seller_id';
            $returnArr['branch_code'] = 'branch_code';
            $returnArr['branch_name'] = 'branch_name';
            $returnArr['branch_status'] = 'branch_status';
            $returnArr['print_quantity'] = 'print_quantity';
            $returnArr['cancel_quantity'] = 'cancel_quantity';
            $returnArr['allocated_quantity'] = 'allocated_quantity';
            $returnArr['quantity'] = 'quantity';
        } elseif ($cname == 'seller_waybill_addr') {
            $returnArr['province'] = 'ship_prov';
            $returnArr['city'] = 'ship_city';
            $returnArr['district'] = 'ship_county';
            $returnArr['town'] = 'ship_town';
            $returnArr['detail'] = 'ship_detail_address';
        } 
        return $returnArr;
    }
    
    /**
     * Convert a SimpleXML object into an array (last resort).
     * @param object $xml
     * @param bool   $root    Should we append the root node into the array
     * @return array|string
     */
    public function xmlToArr($xml, $root = true)
    {
        if (!$xml->children()) {
            return (string)$xml;
        }
        $array = array();
        foreach ($xml->children() as $element => $node) {
            $totalElement = count($xml->{$element});
            if (!isset($array[$element])) {
                $array[$element] = "";
            }
            // Has attributes
            if($attributes = $node->attributes()) {
                $data = array('attributes' => array(), 'value' => (count($node) > 0) ? $this->xmlToArr($node, false) : (string)$node);
                foreach($attributes as $attr => $value)	{
                    $data['attributes'][$attr] = (string)$value;
                }
                if($totalElement > 1) {
                    $array[$element][] = $data;
                }
                else {
                    $array[$element] = $data;
                }
                // Just a value
            }
            else {
                if ($totalElement > 1) {
                    $array[$element][] = $this->xmlToArr($node, false);
                }
                else {
                    $array[$element] = $this->xmlToArr($node, false);
                }
            }
        }
        if($root) {
            return array($xml->getName() => $array);
        }
        else {
            return $array;
        }
    }
}
?>