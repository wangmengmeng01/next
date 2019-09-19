<?php
/**
 * 奇门wms请求基类
 */
require_once API_ROOT.'/ext/tiancan/config_tc.php';
require_once API_ROOT.'/ext/tiancan/tiancan.php';

class wmsRequest
{

    public static $customerId = ''; //客户ID
    public static $wmsApi = ''; //wms接口地址
    public static $wmsApiSecret = ''; //wms接口密钥
    public static $wmsApiVer = ''; //wms接口地址
    public $msgObj = null;
    public $utilObj = null;
    public $funcObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
        $this->funcObj = new func();
    }
    
    /**
     * 转发数据给WMS
     */
    public function send($params=null)
    {
    	$apiParams = array(
    			'method' => qimen_service::$_method,
    			'customerid' => self::$customerId,
    			'appkey' => qimen_service::$_appKey,
    			'sign' => strtoupper(base64_encode(md5(self::$wmsApiSecret . qimen_service::$_data . self::$wmsApiSecret))),
    			'timestamp' => qimen_service::$_timeStamp,
    			'data' => qimen_service::$_data
    	);
    	//推送数据到wms接口
    	$httpObj = new httpclient();
    	
    	//接入天蚕
    	if (CHECK_TIANCAN == 1){
    	    //配置接口返回字段属性，用于配置天蚕校验返回与接口返回数据格式保持一致(必写项)
    	    $apiReturnDataType = '<?xml version="1.0" encoding="utf-8"?><response><code>%s</code><message>%s</message></response>';
    	    $check_tc = new tiancan($apiReturnDataType);
    	    qimen_service::$_mid_req_time = util::microtime_float();
    	    $rs = $check_tc->tiancanActive('wiq','wiq.qimen.request',$apiParams);
    	    qimen_service::$_mid_resp_time = util::microtime_float();
    	    
    	    if($rs == '' || $rs == '-3'){
    	        $msg = '请求超时';
    	        $msgcode = 'E001';
    	        $status = 'failure';
    	        
    	        
    	    } else {
    	        $xmlObj = new xml();
        	    $response = $xmlObj->xmlStr2array($rs);
        	    $response = $this->utilObj->filter_null($response);
        	    if (in_array($response['code'],array('TCA01','TCA02','TCA03'))) {
        	        //接口层记录日志
        	        $logExt = array(
        	            'api_url' => self::$wmsApi,
        	            'api_method' => qimen_service::$_method,
        	            'api_params' => $apiParams,
        	            'return_msg' => $rs
        	        );
        	        return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $logExt);
        	    } else {
        	        $status  = $response['flag'];
        	        $msg     = $response['message'];
        	        $msgcode = $response['code'];
        	        
        	        //接口层记录日志
        	        $logExt = array(
        	            'api_url' => self::$wmsApi,
        	            'api_method' => qimen_service::$_method,
        	            'api_params' => $apiParams,
        	            'return_msg' => $rs == '' ? '请求超时' : $rs
        	        );
        	        return $this->msgObj->outputQimen($status, $msg, $msgcode, $logExt);
    	        }
    	    }
    	} else {
            if (qimen_service::$_method == 'singleitem.synchronize') {
                $autoFlag = $this->isAuto($params['ownerCode']);

                if ($autoFlag == 1) {
                    $httpObj->post(AUTO_WH_URL, $apiParams);
                }
            }

			qimen_service::$_mid_req_time = util::microtime_float();
			$rs = $httpObj->post(self::$wmsApi, $apiParams);
			qimen_service::$_mid_resp_time = util::microtime_float();
			
			if($rs == '' || $rs == '-3'){
				$msg = '请求超时';
				$msgcode = 'E001';
				$status = 'failure';
			}else{
				$xmlObj = new xml();
				$response = $xmlObj->xmlStr2array($rs);
				$response = $this->utilObj->filter_null($response);
				$status = $response['flag'];
				$msg = $response['message'];
				$msgcode = $response['code'];
			}
			
			//接口层记录日志
			$logExt = array(
				'api_url' => self::$wmsApi,
				'api_method' => qimen_service::$_method,
				'api_params' => $apiParams,
				'return_msg' => $rs == '' ? '请求超时' : $rs
			);
			return $this->msgObj->outputQimen($status, $msg, $msgcode, $logExt);
		}
    }

    /**
     * 判断是否为智能仓的货主
     * @param $customerId
     * @return int
     */
    public function isAuto($customerId)
    {
        global $db;

        $sql = "SELECT auto_flag FROM t_qimen_customer_bind WHERE qimen_customer_id=:qimen_customer_id AND customer_id=:customer_id AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':qimen_customer_id',qimen_service::$_customerId);
        $model->bindParam(':customer_id',$customerId);
        $model->execute();
        $cusInfo = $model->fetch(PDO::FETCH_ASSOC);

        if (isset($cusInfo['auto_flag'])) {
            return $cusInfo['auto_flag'];
        } else {
            return 0;
        }
    }

    /**
     * 接口请求完wms后，OMS再次请求wms
     * @param   请求的xml数据  $xmlData
     * @return  封装的结果
     */
    public function requestWms($xmlData){
        $apiParams = array(
            'method' => qimen_service::$_method,
            'customerid' => self::$customerId,
            'appkey' => qimen_service::$_appKey,
            'sign' => strtoupper(base64_encode(md5(self::$wmsApiSecret . $xmlData . self::$wmsApiSecret))),
            'timestamp' => qimen_service::$_timeStamp,
            'data' => $xmlData
        );
        
        $httpObj = new httpclient();
        $rs = $httpObj->post(self::$wmsApi, $apiParams);
    
        if($rs == ''){
            $msg = '请求超时';
            $msgcode = 'E001';
            $status = 'failure';
        }else{
            $xmlObj = new xml();
            $response = $xmlObj->xmlStr2array($rs);
            $response = $this->utilObj->filter_null($response);
            $status = $response['flag'];
            $msg = $response['message'];
            $msgcode = $response['code'];
        }
         
        //接口层记录日志
        $logExt = array(
            'api_url' => self::$wmsApi,
            'api_method' => qimen_service::$_method,
            'api_params' => $apiParams,
            'return_msg' => $rs == '' ? '请求超时' : $rs
        );
        return $this->msgObj->outputQimen($status, $msg, $msgcode, $logExt);
    }
    
    /**
     * 接口参数与数据库字段对应关系
     * @param  $type 类型
     * @return array
     */
    public function get_dataBase_relation($type)
    {
    	$return_arr = array();
    	if ($type == 'DeliveryOrderCreateInfo') {
    		$return_arr['deliveryOrderCode'] = 'delivery_order_code';
    		$return_arr['preDeliveryOrderCode'] = 'pre_delivery_order_code';
    		$return_arr['preDeliveryOrderId'] = 'pre_delivery_order_id';
    		$return_arr['orderType'] = 'order_type';
    		$return_arr['warehouseCode'] = 'warehouse_code';
    		$return_arr['orderFlag'] = 'order_flag';
    		$return_arr['sourcePlatformCode'] = 'source_platform_code';
    		$return_arr['sourcePlatformName'] = 'source_platform_name';
    		$return_arr['createTime'] = 'deliv_create_time';
    		$return_arr['placeOrderTime'] = 'place_order_time';
    		$return_arr['payTime'] = 'pay_time';
    		$return_arr['payNo'] = 'pay_no';
    		$return_arr['operatorCode'] = 'operator_code';
    		$return_arr['operatorName'] = 'operator_name';
    		$return_arr['operateTime'] = 'operate_time';
    		$return_arr['shopNick'] = 'shop_nick';
    		$return_arr['sellerNick'] = 'seller_nick';
    		$return_arr['buyerNick'] = 'buyer_nick';
    		$return_arr['totalAmount'] = 'total_amount';
    		$return_arr['itemAmount'] = 'item_amount';
    		$return_arr['discountAmount'] = 'discount_amount';
    		$return_arr['freight'] = 'freight';
    		$return_arr['arAmount'] = 'ar_amount';
    		$return_arr['gotAmount'] = 'got_amount';
    		$return_arr['serviceFee'] = 'service_fee';
    		$return_arr['logisticsCode'] = 'logistics_code';
    		$return_arr['logisticsName'] = 'logistics_name';
    		$return_arr['expressCode'] = 'express_code';
    		$return_arr['logisticsAreaCode'] = 'logistics_area_code';
    		$return_arr['deliveryRequirements']['scheduleType'] = 'schedule_type';
    		$return_arr['deliveryRequirements']['scheduleDay'] = 'schedule_day';
    		$return_arr['deliveryRequirements']['scheduleStartTime'] = 'schedule_start_time';
    		$return_arr['deliveryRequirements']['scheduleEndTime'] = 'schedule_end_time';
    		$return_arr['deliveryRequirements']['deliveryType'] = 'delivery_type';
    		$return_arr['senderInfo']['company'] = 'sender_company';
    		$return_arr['senderInfo']['name'] = 'sender_name';
    		$return_arr['senderInfo']['zipCode'] = 'sender_zipcode';
    		$return_arr['senderInfo']['tel'] = 'sender_tel';
    		$return_arr['senderInfo']['mobile'] = 'sender_mobile';
    		$return_arr['senderInfo']['email'] = 'sender_email';
    		$return_arr['senderInfo']['countryCode'] = 'sender_countrycode';
    		$return_arr['senderInfo']['province'] = 'sender_province';
    		$return_arr['senderInfo']['city'] = 'sender_city';
    		$return_arr['senderInfo']['area'] = 'sender_area';
    		$return_arr['senderInfo']['town'] = 'sender_town';
    		$return_arr['senderInfo']['detailAddress'] = 'sender_detail_address';
    		$return_arr['receiverInfo']['company'] = 'receiver_company';
    		$return_arr['receiverInfo']['name'] = 'receiver_name';
    		$return_arr['receiverInfo']['zipCode'] = 'receiver_zipcode';
    		$return_arr['receiverInfo']['tel'] = 'receiver_tel';
    		$return_arr['receiverInfo']['mobile'] = 'receiver_mobile';
    		$return_arr['receiverInfo']['email'] = 'receiver_email';
    		$return_arr['receiverInfo']['countryCode'] = 'receiver_countrycode';
    		$return_arr['receiverInfo']['province'] = 'receiver_province';
    		$return_arr['receiverInfo']['city'] = 'receiver_city';
    		$return_arr['receiverInfo']['area'] = 'receiver_area';
    		$return_arr['receiverInfo']['town'] = 'receiver_town';
    		$return_arr['receiverInfo']['detailAddress'] = 'receiver_detail_address';
    		$return_arr['isUrgency'] = 'is_urgency';
    		$return_arr['invoiceFlag'] = 'invoice_flag';
    		$return_arr['insuranceFlag'] = 'insurance_flag';
    		$return_arr['insurance']['type'] = 'insurance_type';
    		$return_arr['insurance']['amount'] = 'insurance_amount';
    		$return_arr['buyerMessage'] = 'buyer_message';
    		$return_arr['sellerMessage'] = 'seller_message';
    		$return_arr['remark'] = 'remark';
            $return_arr['custmSource'] = 'fx_customer';
            $return_arr['branchCode'] = 'fx_branch_code';
            $return_arr['branchName'] = 'fx_branch_name';
            $return_arr['depositPay'] = 'fx_is_depositpay';
            $return_arr['distributionCode'] = 'fx_distbt_code';
            $return_arr['distributionName'] = 'fx_distbt_name';
            $return_arr['fx_flag'] = 'fx_flag';
    	} elseif ($type == 'DeliveryOrderCreateDetail') {
    		$return_arr['orderLineNo'] = 'order_line_no';
    		$return_arr['sourceOrderCode'] = 'source_order_code';
    		$return_arr['subSourceOrderCode'] = 'sub_source_order_code';
    		$return_arr['payNo'] = 'pay_no';
    		$return_arr['ownerCode'] = 'customer_id';
    		$return_arr['itemCode'] = 'item_code';
    		$return_arr['itemId'] = 'item_id';
    		$return_arr['inventoryType'] = 'inventory_type';
    		$return_arr['itemName'] = 'item_name';
    		$return_arr['extCode'] = 'ext_code';
    		$return_arr['planQty'] = 'plan_qty';
    		$return_arr['retailPrice'] = 'retail_price';
    		$return_arr['actualPrice'] = 'actual_price';
    		$return_arr['discountAmount'] = 'discount_amount';
    		$return_arr['batchCode'] = 'batch_code';
    		$return_arr['productDate'] = 'product_date';
    		$return_arr['expireDate'] = 'expire_date';   		
    	} elseif ($type == 'DeliveryOrderCreateBill') {
    		$return_arr['type'] = 'type';
    		$return_arr['header'] = 'header';
    		$return_arr['amount'] = 'amount';
    		$return_arr['content'] = 'content';    		
    	} elseif ($type == 'DeliveryOrderCreateBillProduct') {
    		$return_arr['itemName'] = 'item_name';
    		$return_arr['unit'] = 'unit';
    		$return_arr['price'] = 'price';
    		$return_arr['quantity'] = 'quantity';
    		$return_arr['amount'] = 'amount';
    	} elseif ($type == 'product') {
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['supplierCode'] = 'supplier_code';
    	    $return_arr['supplierName'] = 'supplier_name';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
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
    	    $return_arr['itemType'] = 'freight_class';
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
    	} elseif($type == 'combine_item'){
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	} elseif($type == 'combine_item_detail'){
    	    $return_arr['combineItemCode'] = 'combine_item_code';
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['quantity'] = 'quantity';
    	} elseif ($type == 'entry_order_create') {
    	    $return_arr['totalOrderLines'] = 'total_order_lines';
    	    $return_arr['entryOrderCode'] = 'order_no';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['purchaseOrderCode'] = 'purchase_order_code';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['orderCreateTime'] = 'order_create_time';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['expectStartTime'] = 'expected_arrive_time1';
    	    $return_arr['expectEndTime'] = 'expect_end_time';
    	    $return_arr['logisticsCode'] = 'carrier_id';
    	    $return_arr['logisticsName'] = 'carrier_name';
    	    $return_arr['expressCode'] = 'asn_reference2';
    	    $return_arr['supplierCode'] = 'supplier_code';
    	    $return_arr['supplierName'] = 'supplier_name';
    	    $return_arr['operatorCode'] = 'operator_code';
    	    $return_arr['operatorName'] = 'operator_name';
    	    $return_arr['operateTime'] = 'operate_time';
    	    $return_arr['remark'] = 'remark';
    	    $return_arr['extendProps'] = 'extend_props';
    	} elseif ($type == 'entry_order_sender_create') {
    	    $return_arr['company'] = 'sender_company';
    	    $return_arr['name'] = 'sender_name';
    	    $return_arr['zipCode'] = 'sender_zip_code';
    	    $return_arr['tel'] = 'sender_tel';
    	    $return_arr['mobile'] = 'sender_mobile';
    	    $return_arr['email'] = 'sender_email';
    	    $return_arr['countryCode'] = 'sender_countrycode';
    	    $return_arr['province'] = 'sender_province';
    	    $return_arr['city'] = 'sender_city';
    	    $return_arr['area'] = 'sender_area';
    	    $return_arr['town'] = 'sender_town';
    	    $return_arr['detailAddress'] = 'sender_detail_address';
    	} elseif ($type == 'entry_order_receiver_create') {
    	    $return_arr['company'] = 'receiver_company';
    	    $return_arr['name'] = 'receiver_name';
    	    $return_arr['zipCode'] = 'receiver_zip_code';
    	    $return_arr['tel'] = 'receiver_tel';
    	    $return_arr['mobile'] = 'receiver_mobile';
    	    $return_arr['email'] = 'receiver_email';
    	    $return_arr['countryCode'] = 'receiver_countrycode';
    	    $return_arr['province'] = 'receiver_province';
    	    $return_arr['city'] = 'receiver_city';
    	    $return_arr['area'] = 'receiver_area';
    	    $return_arr['town'] = 'receiver_town';
    	    $return_arr['detailAddress'] = 'receiver_detail_address';
    	} elseif ($type == 'entry_order_create_detail') {
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['orderLineNo'] = 'line_no';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['itemName'] = 'item_name';
    	    $return_arr['planQty'] = 'expected_qty';
    	    $return_arr['skuProperty'] = 'sku_property';
    	    $return_arr['purchasePrice'] = 'purchase_price';
    	    $return_arr['retailPrice'] = 'retail_price';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['extendProps'] = 'extend_props';
    	} elseif ($type == 'return_order_create') {
    	    $return_arr['returnOrderCode'] = 'order_no';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['orderFlag'] = 'order_flag';
    	    $return_arr['preDeliveryOrderCode'] = 'pono';
    	    $return_arr['preDeliveryOrderId'] = 'pre_delivery_order_id';
    	    $return_arr['logisticsCode'] = 'carrier_id';
    	    $return_arr['logisticsName'] = 'carrier_name';
    	    $return_arr['expressCode'] = 'user_define2';
    	    $return_arr['returnReason'] = 'user_define3';
    	    $return_arr['buyerNick'] = 'buyer_nick';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'return_order_sender_create') {
    	    $return_arr['company'] = 'sender_company';
    	    $return_arr['name'] = 'sender_name';
    	    $return_arr['zipCode'] = 'sender_zip_code';
    	    $return_arr['tel'] = 'sender_tel';
    	    $return_arr['mobile'] = 'sender_mobile';
    	    $return_arr['email'] = 'sender_email';
    	    $return_arr['countryCode'] = 'sender_countrycode';
    	    $return_arr['province'] = 'sender_province';
    	    $return_arr['city'] = 'sender_city';
    	    $return_arr['area'] = 'sender_area';
    	    $return_arr['town'] = 'sender_town';
    	    $return_arr['detailAddress'] = 'sender_detail_address';
    	} elseif ($type == 'return_order_create_detail') {
    	    $return_arr['orderLineNo'] = 'line_no';
    	    $return_arr['sourceOrderCode'] = 'source_order_code';
    	    $return_arr['subSourceOrderCode'] = 'sub_source_order_code';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['planQty'] = 'expected_qty';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['batchCode'] = 'batch_code';
    	} elseif ($type == 'stock_out_create') {
    	    $return_arr['totalOrderLines'] = 'total_order_lines';
    	    $return_arr['deliveryOrderCode'] = 'order_no';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['createTime'] = 'order_time';
    	    $return_arr['scheduleDate'] = 'expected_shipment_time1';
    	    $return_arr['logisticsCode'] = 'carrier_id';
    	    $return_arr['logisticsName'] = 'carrier_name';
    	    $return_arr['transportMode'] = 'transport_mode';
    	    $return_arr['remark'] = 'remark';
            $return_arr['custmSource'] = 'fx_customer';
            $return_arr['branchCode'] = 'fx_branch_code';
            $return_arr['branchName'] = 'fx_branch_name';
            $return_arr['depositPay'] = 'fx_is_depositpay';
            $return_arr['distributionCode'] = 'fx_distbt_code';
            $return_arr['distributionName'] = 'fx_distbt_name';
            $return_arr['fx_flag'] = 'fx_flag';
    	} elseif ($type == 'stock_out_picker_create') {
    	    $return_arr['company'] = 'picker_company';
    	    $return_arr['name'] = 'picker_name';
    	    $return_arr['tel'] = 'picker_tel';
    	    $return_arr['mobile'] = 'picker_mobile';
    	    $return_arr['id'] = 'picker_id';
    	    $return_arr['carNo'] = 'picker_car_no';
    	} elseif ($type == 'stock_out_sender_create') {
    	    $return_arr['company'] = 'sender_company';
    	    $return_arr['name'] = 'sender_name';
    	    $return_arr['zipCode'] = 'sender_zip_code';
    	    $return_arr['tel'] = 'sender_tel';
    	    $return_arr['mobile'] = 'sender_mobile';
    	    $return_arr['id'] = 'sender_id';
    	    $return_arr['email'] = 'sender_email';
    	    $return_arr['countryCode'] = 'sender_country_code';
    	    $return_arr['province'] = 'sender_province';
    	    $return_arr['city'] = 'sender_city';
    	    $return_arr['area'] = 'sender_area';
    	    $return_arr['town'] = 'sender_town';
    	    $return_arr['detailAddress'] = 'sender_detail_address';
    	} elseif ($type == 'stock_out_receiver_create') {
    	    $return_arr['company'] = 'receiver_company';
    	    $return_arr['name'] = 'consignee_name';
    	    $return_arr['zipCode'] = 'c_zip';
    	    $return_arr['tel'] = 'c_tel2';
    	    $return_arr['mobile'] = 'c_tel1';
    	    $return_arr['id'] = 'receiver_id';
    	    $return_arr['email'] = 'c_mail';
    	    $return_arr['countryCode'] = 'c_country';
    	    $return_arr['province'] = 'c_province';
    	    $return_arr['city'] = 'c_city';
    	    $return_arr['area'] = 'c_address2';
    	    $return_arr['town'] = 'c_address3';
    	    $return_arr['detailAddress'] = 'c_address1';
    	} elseif ($type == 'stock_out_detail') {
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['orderLineNo'] = 'line_no';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['itemName'] = 'item_name';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['planQty'] = 'qty_ordered';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	} elseif ($type == 'order_cancel') {
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['orderCode'] = 'order_no';
    	    $return_arr['orderId'] = 'wms_order_id';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['cancelReason'] = 'reason';
    	} elseif ($type == 'insert_store_process_order') {
    	    $return_arr['processOrderCode'] = 'process_order_code';
    	    $return_arr['customer_id'] = 'customer_id';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['orderCreateTime'] = 'order_create_time';
    	    $return_arr['planTime'] = 'plan_time';
    	    $return_arr['serviceType'] = 'service_type';
    	    $return_arr['extendProps'] = 'extend_props';
    	    $return_arr['planQty'] = 'plan_qty';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'insert_store_process_material') {
    	    $return_arr['process_order_code'] = 'process_order_code';
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'insert_store_process_product') {
    	    $return_arr['process_order_code'] = 'process_order_code';
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'product_inventory') {
    	    $return_arr['customer_id'] = 'customer_id';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'qty';
    	    $return_arr['lockQuantity'] = 'occupy_qty';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['extendProps'] = 'extend_props';
    	}
    	return $return_arr;
    }

}