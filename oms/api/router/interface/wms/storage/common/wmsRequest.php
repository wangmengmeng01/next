<?php
/**
 * wms请求基类
 */
class wmsRequest
{

    public $msgObj  = null;
    public $utilObj = null;
    public $funcObj = null;
    public $xmlObj  = null;

    public function __construct()
    {
        $this->msgObj  = new msg();
        $this->utilObj = new util();
        $this->funcObj = new func();
        $this->xmlObj  = new xml();
    }

    public function send($data,$method)
    {
        $apiParams = array(
            'method'    => $method,
            'appkey'    => SRD_APP_KEY,
            'sign'      => strtoupper(base64_encode(md5(SRD_APP_SECRET . $data . SRD_APP_SECRET))),
            'timestamp' => date("Y-m-d H:i:s"),
            'data'      => $data
        );
        
        $httpObj = new httpclient();
        
        if (CHECK_TIANCAN == 1) {
            //配置接口返回字段属性，用于配置天蚕校验返回与接口返回数据格式保持一致(必写项)
            $apiReturnDataType = '<?xml version="1.0" encoding="utf-8"?><response><code>%s</code><message>%s</message></response>';
            $check_tc = new tiancan($apiReturnDataType);
            
            cn_storage_service::$_mid_req_time = util::microtime_float();
            $rs = $check_tc->tiancanActive('wiq','wiq.qimen.request',$apiParams);
            cn_storage_service::$_mid_resp_time = util::microtime_float();
            
        } else {
            cn_storage_service::$_mid_req_time = util::microtime_float();
            $rs = $httpObj->post(STORAGE_WMS_API_URL, $apiParams);
            cn_storage_service::$_mid_resp_time = util::microtime_float();
            
            if($rs == '' || $rs == '-3'){
                $msg = '请求超时';
                $msgcode = 'E001';
                $status = false;
            } else {
                $xmlObj = new xml();
                $response = $xmlObj->xmlStr2array($rs);
                $response = $this->utilObj->filter_null($response);
            
                $status = $response['flag'] == 'success' ? true : false;
                $msg = $response['message'];
                $msgcode = $response['code'];
            }
            
            //接口层记录日志
            $logExt = array(
                'api_params' => $apiParams,
                'rs_flag'    => $status == 1 ? 'true' : 'false',
                'return_msg' => $rs == '' ? '请求超时' : $rs,
            );
            return $this->msgObj->outputCnStorage($status, $msg, $msgcode, $logExt);
        }
    }
    
    public function requestOmsToCn($params,$method,$data){
        $requestParams = array(
            'method'        => $method,
            'customerid'    => $params['ownerUserId'],
            'warehouseid'   => $params['storeCode'],
            'data'          => $data,
            'appkey'        => SRD_APP_KEY,
            'sign'          => strtoupper(base64_encode(md5(SRD_APP_SECRET . $data . SRD_APP_SECRET))),
            'timestamp'     => date("Y-m-d H:i:s")
        ); 
        $httpObj = new httpclient();
        $rs = $httpObj->post(STORAGE_API_URL, $requestParams);
        
        if ($rs == '' || $rs == '-3') {
            $msg = '请求超时';
            $msgcode = 'E001';
            $status = false;
        } else {
            $xmlObj = new xml();
            $response = $xmlObj->xmlStr2array($rs);
            $response = $this->utilObj->filter_null($response);

            $success = $response['success'];
            if($success == 'true' || $success == true){
                $status = true;
                $msg = '';
                $msgcode = '';
            }else{
                $status = false;
                $msg = $response['errorMsg'];
                $msgcode = $response['errorCode'];
            }
            $logExt = array(
                'return_msg' => $rs
            );
        }
        return $this->msgObj->outputCnStorage($status,$msg,$msgcode,$logExt);
    }
    
    public function requestOmsToWms($data,$method){
        $requestParams = array(
            'msg_type'              => $method,
            'from_code'             => STORAGE_APP_KEY,
            'partner_code'          => cn_storage_service::$_partner_code,
            'data_digest'           => base64_encode(md5( $data . STORAGE_APP_SECRET,true)),
            'logistics_interface'   => $data,
            'msg_id'                => rand(10000001,99999999)
        );
        $httpObj = new httpclient();
        $rs = $httpObj->post(STORAGE_API_URL, $requestParams);
        if ($rs == '' || $rs == '-3') {
            $errorMsg = '请求超时';
            $errorCode = 'E001';
            $success = false;
        } else {
            $xmlObj = new xml();
            $response = $xmlObj->xmlStr2array($rs);
            $response = $this->utilObj->filter_null($response);

            $success = $response['success'];
            
            if($success == 'true' || $success == true){
                $success = true;
                $errorMsg = '';
                $errorCode = '';
            }else{
                $success = false;
                $errorMsg = $response['errorMsg'];
                $errorCode = $response['errorCode'];
            }

        }
        $logExt = array(
            'return_msg' => $rs
        );

        return $this->msgObj->outputCnStorage($success,$errorMsg,$errorCode,$logExt);
    }
    
    public function getProduct($params){
        global $db;
        //首先判断订单明细中商品是否存在
        $queryPrdctArr = array();//商品编码数组
        $customerId    = $params['ownerUserId'];
        $warehouseCode = $params['storeCode'];
        if (empty($params['orderItemList']['orderItem'][0])) {
            $params['orderItemList']['orderItem']  =array($params['orderItemList']['orderItem']);
        }
        foreach ($params['orderItemList']['orderItem'] as $p_o_v) {
            $itemCode  = $p_o_v['itemCode'];
            $p_sql = "SELECT product_id
                       FROM t_base_product
                       WHERE sku=:sku
                       AND customer_id=:customer_id
                       AND warehouse_code=:warehouse_code;";
            $model = $db->prepare($p_sql);
            $pValues = array();
            $pValues[':sku']            = $itemCode;
            $pValues[':customer_id']    = $customerId;
            $pValues[':warehouse_code'] = $warehouseCode;
            $model->execute($pValues);
            $productInfo = $model->fetch(PDO::FETCH_ASSOC);
            
            if (empty($productInfo)) {
                $queryPrdctArr[] = $itemCode;
            }
        }
        
        if (!empty($queryPrdctArr)) {
            //商品查询接口报文
            $queryPrdctXml  = '<?xml version="1.0" encoding="utf-8"?><request>';
            $queryPrdctXml .= '<queryItemOrderCode>' . $params['erpOrderCode'] . '</queryItemOrderCode>';
            $queryPrdctXml .= '<providerTpId>'       . $customerId             . '</providerTpId>';
            $queryPrdctXml .= '<itemCodes>';
            
            foreach ($queryPrdctArr as $prdctCode) {
                $queryPrdctXml .= '<itemCode>' . $prdctCode . '</itemCode>';
            }      
            $queryPrdctXml     .= '</itemCodes>';
            $queryPrdctXml     .= '</request>';
            
            $queryResponse = $this->requestOmsToCn($params, 'WMS_ITEM_QUERY', $queryPrdctXml);  
            if (!$queryResponse['success']) {
                return $this->msgObj->outputCnStorage(false, '订单下发时订单明细不存在反查商品时失败：'. $queryResponse['errorMsg'], $queryResponse['errorCode']);
            } else {
                return $this->msgObj->outputCnStorage(true);
            }
        } else {
            return $this->msgObj->outputCnStorage(true);
        }
    }
    
    /**
     * 接口参数与数据库字段对应关系
     * @param  $type 类型
     * @return array
     */
    public function get_dataBase_relation($type)
    {
        $return_arr = array();
        switch($type) {
            case 'stock_in_order_notify':
                $return_arr['ownerUserId']      = 'customer_id';
                $return_arr['erpOrderCode']     = 'order_no';
                $return_arr['orderCode']        = 'order_code';
                $return_arr['storeCode']        = 'warehouse_code';
                $return_arr['orderCreateTime']  = 'order_create_time';
                $return_arr['orderType']        = 'order_type';
                break;
            case 'stock_in_order_notify_sender':
                $return_arr['senderName']       = 'sender_name';
                $return_arr['senderZipCode']    = 'sender_zip_code';
                $return_arr['senderPhone']      = 'sender_tel';
                $return_arr['senderMobile']     = 'sender_mobile';
                $return_arr['senderProvince']   = 'sender_province';
                $return_arr['senderCity']       = 'sender_city';
                break;
            case 'stock_in_order_notify_detail':
                $return_arr['itemCode']         = 'sku';
                $return_arr['ownerUserId']      = 'customer_id';
                $return_arr['itemId']           = 'item_id';
                $return_arr['itemName']         = 'item_name';
                $return_arr['itemQuantity']     = 'expected_qty';
                $return_arr['inventoryType']    = 'inventory_type';
                $return_arr['produceDate']      = 'product_date';
                $return_arr['orderItemId']      = 'line_no';
                $return_arr['orderSourceCode']  = 'source_order_code';
                $return_arr['subSourceCode']    = 'sub_source_order_code';
                $return_arr['batchCode']        = 'batch_code';
                $return_arr['produceCode']      = 'produce_code';
                break;
            case 'stock_in_order_notify_detail_sub':
                $return_arr['itemId']           = 'item_id';
                $return_arr['inventoryType']    = 'inventory_type';
                break;
            case 'stock_out_order_info':
                $return_arr['erpOrderCode']                     = 'order_no';
                $return_arr['ownerUserId']                      = 'customer_id';
                $return_arr['storeCode']                        = 'warehouse_code';
                $return_arr['orderCode']                        = 'cn_order_code';
                $return_arr['orderType']                        = 'order_type';
                $return_arr['orderFlag']                        = 'cn_order_flag';
                $return_arr['orderCreateTime']                  = 'order_time';
                $return_arr['sendTime']                         = 'expected_shipment_time1';
                $return_arr['pickCompany']                      = 'picker_company';
                $return_arr['pickName']                         = 'picker_name';
                $return_arr['pickCall']                         = 'picker_mobile';
                $return_arr['pickTel']                          = 'picker_tel';
                $return_arr['pickId']                           = 'picker_id';
                $return_arr['tmsServiceCode']                   = 'carrier_id';
                $return_arr['tmsServiceName']                   = 'carrier_name';
                $return_arr['supplierCode']                     = 'supplier_code';
                $return_arr['supplierName']                     = 'supplier_name';
                $return_arr['receiverInfo']['receiverProvince'] = 'c_province';
                $return_arr['receiverInfo']['receiverCity']     = 'c_city';
                $return_arr['receiverInfo']['receiverArea']     = 'c_address2';
                $return_arr['receiverInfo']['receiverTown']     = 'c_address3';
                $return_arr['receiverInfo']['receiverAddress']  = 'c_address1';
                $return_arr['receiverInfo']['receiverName']     = 'consignee_name';
                $return_arr['receiverInfo']['receiverMobile']   = 'c_tel1';
                $return_arr['senderInfo']['senderAddress']      = 'sender_detail_address';
                $return_arr['senderInfo']['senderName']         = 'sender_name';
                $return_arr['driverInfo']['driverName']         = 'driver_name';
                $return_arr['driverInfo']['driverIdentityId']   = 'driver_identity_id';
                $return_arr['driverInfo']['driverPhone']        = 'driver_phone';
                $return_arr['driverInfo']['vehicleType']        = 'vehicle_type';
                $return_arr['driverInfo']['vehicleLoad']        = 'vehicle_load';
                $return_arr['driverInfo']['licensePlate']       = 'license_plate';
                break;
            case 'stock_out_order_detail':
                $return_arr['orderItemId']        = 'line_no';
                $return_arr['ownerUserId']        = 'customer_id';
                $return_arr['itemCode']           = 'sku';
                $return_arr['itemId']             = 'cn_item_code';
                $return_arr['itemName']           = 'item_name';
                $return_arr['inventoryType']      = 'inventory_type';
                $return_arr['itemQuantity']       = 'qty_ordered';
                $return_arr['itemPrice']          = 'price';
                $return_arr['itemVersion']        = 'item_version';
                $return_arr['batchCode']          = 'batch_code';
                $return_arr['purchaseOrderCode']  = 'purchase_order_code';
                $return_arr['produceDate']        = 'product_date';
                $return_arr['produceCode']        = 'produce_code';
                break;
            case 'delivery_order_info':
                $return_arr['erpOrderCode']                     = 'delivery_order_code';
                $return_arr['ownerUserId']                      = 'customer_id';
                $return_arr['storeCode']                        = 'warehouse_code';
                $return_arr['orderCode']                        = 'cn_order_code';
                $return_arr['orderType']                        = 'order_type';
                $return_arr['userId']                           = 'shop_id';
                $return_arr['userName']                         = 'shop_nick';
                $return_arr['orderFlag']                        = 'cn_order_flag';
                $return_arr['orderSource']                      = 'source_platform_code';
                $return_arr['orderCreateTime']                  = 'deliv_create_time';
                $return_arr['orderPayTime']                     = 'pay_time';
                $return_arr['orderExaminationTime']             = 'operate_time';
                $return_arr['orderShopCreateTime']              = 'place_order_time';
                $return_arr['orderAmount']                      = 'total_amount';
                $return_arr['discountAmount']                   = 'discount_amount';
                $return_arr['arAmount']                         = 'ar_amount';
                $return_arr['gotAmount']                        = 'got_amount';
                $return_arr['postfee']                          = 'freight';
                $return_arr['serviceFee']                       = 'service_fee';
                $return_arr['tmsOrderCode']                     = 'express_code';
                $return_arr['tmsServiceCode']                   = 'logistics_code';
                $return_arr['tmsServiceName']                   = 'logistics_name';
                //---------
                $return_arr['prevOrderCode']                    = 'prev_express_code';
                $return_arr['receiverInfo']['receiverProvince'] = 'receiver_province';
                $return_arr['receiverInfo']['receiverCity']     = 'receiver_city';
                $return_arr['receiverInfo']['receiverArea']     = 'receiver_area';
                $return_arr['receiverInfo']['receiverTown']     = 'receiver_town';
                $return_arr['receiverInfo']['receiverAddress']  = 'receiver_detail_address';
                $return_arr['receiverInfo']['receiverName']     = 'receiver_name';
                $return_arr['receiverInfo']['receiverNick']     = 'buyer_nick';
                $return_arr['receiverInfo']['receiverMobile']   = 'receiver_mobile';
                $return_arr['senderInfo']['senderAddress']      = 'sender_detail_address';
                $return_arr['senderInfo']['senderZipCode']      = 'sender_zipcode';
                $return_arr['senderInfo']['senderProvince']     = 'sender_province';
                $return_arr['senderInfo']['senderCity']         = 'sender_city';
                $return_arr['senderInfo']['senderMobile']       = 'sender_mobile';
                $return_arr['senderInfo']['senderName']         = 'sender_name';
                break;    
            case 'delivery_order_detail':
                $return_arr['orderItemId']        = 'order_line_no';
                $return_arr['orderSourceCode']    = 'source_order_code';
                $return_arr['subSourceCode']      = 'sub_source_order_code';
                $return_arr['userId']             = 'cn_user_id';
                $return_arr['userName']           = 'cn_user_name';
                $return_arr['ownerUserId']        = 'customer_id';
                $return_arr['itemCode']           = 'item_code';
                $return_arr['itemId']             = 'cn_item_code';
                $return_arr['itemName']           = 'item_name';
                $return_arr['inventoryType']      = 'inventory_type';
                $return_arr['itemQuantity']       = 'plan_qty';
                $return_arr['itemPrice']          = 'retail_price';
                $return_arr['actualPrice']        = 'actual_price';
                $return_arr['itemVersion']        = 'item_version';
                $return_arr['batchCode']          = 'batch_code';
                $return_arr['purchaseOrderCode']  = 'purchase_order_code';
                $return_arr['produceDate']        = 'product_date';
                break;

            case 'sku_info_notify':
                $return_arr['storeCode'] = 'warehouse_code';
                $return_arr['ownerUserId'] = 'customer_id';
                $return_arr['supplierCode'] = 'supplier_code';
                $return_arr['supplierName'] = 'supplier_name';
                $return_arr['itemCode'] = 'sku';
                $return_arr['itemId'] = 'item_id';
                $return_arr['cnItemId'] = 'cnitem_id';
                $return_arr['goodsCode'] = 'goods_code';
                $return_arr['itemName'] = 'descr_c';
                $return_arr['shortName'] = 'sku_short';
                $return_arr['englishName'] = 'descr_e';
                $return_arr['barCode'] = 'alternate_sku1';
                $return_arr['skuProperty'] = 'sku_property';
                $return_arr['stockUnit'] = 'stock_unit';
                $return_arr['length'] = 'sku_Length';
                $return_arr['width'] = 'sku_width';
                $return_arr['height'] = 'sku_height';
                $return_arr['volume'] = 'cube';
                $return_arr['grossWeight'] = 'gross_weight';
                $return_arr['netWeight'] = 'net_weight';
                $return_arr['color'] = 'sku_group2';
                $return_arr['size'] = 'sku_group3';
                $return_arr['title'] = 'title';
                $return_arr['categoryId'] = 'category_id';
                $return_arr['categoryName'] = 'category_name';
                $return_arr['pricingCategory'] = 'pricing_category';
                $return_arr['safetyStock'] = 'safety_stock';
                $return_arr['type'] = 'freight_class';
                $return_arr['tagPrice'] = 'tag_price';
                $return_arr['retailPrice'] = 'price';
                $return_arr['costPrice'] = 'cost_price';
                $return_arr['purchasePrice'] = 'purchase_price';
                $return_arr['seasonCode'] = 'season_code';
                $return_arr['seasonName'] = 'sku_group6';
                $return_arr['brandCode'] = 'brand_code';
                $return_arr['brandName'] = 'sku_group4';
                $return_arr['isSNMgmt'] = 'is_SN_mgmt';
                $return_arr['productDate'] = 'product_date';
                $return_arr['expireDate'] = 'expire_date';
                $return_arr['isShelfLifeMgmt'] = 'is_shelfLife_mgmt';
                $return_arr['shelfLife'] = 'shelf_life';
                $return_arr['rejectLifecycle'] = 'reject_lifecycle';
                $return_arr['lockupLifecycle'] = 'lockup_lifecycle';
                $return_arr['adventLifecycle'] = 'advent_lifecycle';
                $return_arr['isBatchMgmt'] = 'is_batch_mgmt';
                $return_arr['batchCode'] = 'batch_code';
                $return_arr['batchRemark'] = 'batch_remark';
                $return_arr['packCode'] = 'pack_code';
                $return_arr['pcs'] = 'pcs';
                $return_arr['originAddress'] = 'origin_address';
                $return_arr['approvalNumber'] = 'approval_no';
                $return_arr['isFragile'] = 'is_fragile';
                $return_arr['isHazardous'] = 'is_hazardous';
                $return_arr['remark'] = 'remark';
                $return_arr['createTime'] = 'sku_create_time';
                $return_arr['updateTime'] = 'sku_update_time';
                $return_arr['isValid'] = 'active_flag';
                $return_arr['isSku'] = 'is_sku';
                $return_arr['packageMaterial'] = 'package_material';
                $return_arr['extendProps'] = 'extend_props';
                break;
                
                /* 
            case 'product':
                $return_arr['itemId']        = 'line_no';
                $return_arr['itemCode']        = 'line_no';
                $return_arr['version']        = 'line_no';
                $return_arr['itemName']        = 'line_no';
                $return_arr['productCode']        = 'line_no';
                $return_arr['providerTpId']        = 'line_no';
                $return_arr['barCode']        = 'line_no';
                $return_arr['type']        = 'line_no';
                $return_arr['brandName']        = 'line_no';
                $return_arr['specification']        = 'line_no';
                $return_arr['color']        = 'line_no';
                $return_arr['approvalNumber']        = 'line_no';
                $return_arr['grossWeight']        = 'line_no';
                $return_arr['netWeight']        = 'line_no';
                $return_arr['length']        = 'line_no';
                $return_arr['width']        = 'line_no';
                $return_arr['height']        = 'line_no';
                $return_arr['itemName']        = 'line_no';    
                $return_arr['itemName']        = 'line_no';
                $return_arr['itemName']        = 'line_no';
                $return_arr['itemName']        = 'line_no'; */
            default:
                break;
        }
        return $return_arr;
    }


}