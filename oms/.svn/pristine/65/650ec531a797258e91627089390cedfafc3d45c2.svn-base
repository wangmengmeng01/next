<?php
/**
 * 奇门erp请求基类
 */
class erpRequest
{

    public static $customerId = ''; //客户ID
    public static $erpApi = ''; //erp接口地址
    public static $erpApiSecret = ''; //erp接口密钥
    public static $erpApiVer = ''; //erp版本号
    public  $msgObj = null;
    public  $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }
    
    /**
     * 生成签名
     */
    public function makeSign()
    {
    	/*
        unset($_REQUEST['warehouseid']);
    	unset($_REQUEST['sign']);
    	unset($_REQUEST['data']);
    	ksort($_REQUEST);
    	$signStr = self::$erpApiSecret;
    	foreach ($_REQUEST as $key => $val)
    	{
    		$signStr .= $key . $val;
    	}
    	*/
    	$signStr = self::$erpApiSecret;
    	foreach (qimen_service::$_systemParams as $val)
    	{
    	    if ($val != 'v') {
    	        if ($val == 'customerId') {
    	            $signStr .= $val . qimen_service::$_customerId;
    	        } else {
    	            $signStr .= $val . $_REQUEST[$val];
    	        }
    	    }
    	    //$signStr .= $val . $_REQUEST[$val];
    	}
    	$signStr .= 'v' . self::$erpApiVer . qimen_service::$_data . self::$erpApiSecret;
    	$sign = strtoupper(md5($signStr));
    	return $sign;
    }
    
    /**
     * 转发数据给ERP
     */
    public function send()
    {   	
    	//生成签名    	
    	$sign = self::makeSign();
        //$apiUrl = self::$erpApi . '?method=' . qimen_service::$_method . '&timestamp=' . urlencode(qimen_service::$_timeStamp) . '&format=' . qimen_service::$_format . '&app_key=' . qimen_service::$_appKey . '&v=' . self::$erpApiVer . '&sign=' . $sign . '&sign_method=' . qimen_service::$_signMethod . '&customerId=' . self::$customerId;
        $apiArr = array(
            'method'=>qimen_service::$_method,
            'timestamp'=>urlencode(qimen_service::$_timeStamp),
            'format'=>qimen_service::$_format,
            'app_key'=>qimen_service::$_appKey,
            'v'=>self::$erpApiVer,
            'sign'=>$sign,
            'sign_method'=>qimen_service::$_signMethod,
            'customerId'=>self::$customerId,
            'data'=>qimen_service::$_data
        );
		error_log(print_r($apiArr,1),3,'/yd/oms/log/erpRequest.log');
    	//推送数据到ERP接口
        qimen_service::$_mid_req_time = util::microtime_float();
    	$rs = $this->utilObj->post(self::$erpApi, $apiArr);    	
    	qimen_service::$_mid_resp_time = util::microtime_float();
    	//$httpObj = new httpclient();
    	//$rs = $httpObj->post(self::$erpApi, $apiParams);
    	
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
    	$apiParams = array(
    			'method' => qimen_service::$_method,
    			'timestamp' => qimen_service::$_timeStamp,
    			'format' => qimen_service::$_format,
    	        'app_key' => qimen_service::$_appKey,
    			'app_secret' => self::$erpApiSecret,
    	        'v' => qimen_service::$_v,
    	        'sign' => $sign,
    	        'sign_method' => qimen_service::$_signMethod,
    			'customerid' => self::$customerId,
    			'data' => qimen_service::$_data
    	);
        $logExt = array(
            'api_url' => self::$erpApi,
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
        if ($type == 'DeliveryOrderConfirmInfo') {
    		$return_arr['deliveryOrderCode'] = 'delivery_order_code';
    		$return_arr['deliveryOrderId'] = 'delivery_order_id';
    		$return_arr['warehouseCode'] = 'warehouse_code';
    		$return_arr['orderType'] = 'order_type';
    		$return_arr['status'] = 'order_status';
    		$return_arr['outBizCode'] = 'out_biz_code';
    		$return_arr['confirmType'] = 'confirm_type';
    		$return_arr['orderConfirmTime'] = 'order_confirm_time';
    		$return_arr['orignOrders'] = 'orign_orders';
    		$return_arr['operatorCode'] = 'operator_code';
    		$return_arr['operatorName'] = 'operator_name';
    		$return_arr['operateTime'] = 'operate_time';
    	} elseif ($type == 'DeliveryOrderConfirmBill') {
    		$return_arr['header'] = 'header';
    		$return_arr['amount'] = 'amount';
    		$return_arr['content'] = 'content';
    		$return_arr['code'] = 'code';
    		$return_arr['number'] = 'number';
    	} elseif ($type == 'DeliveryOrderConfirmBillProduct') {
    		$return_arr['itemName'] = 'item_name';
    		$return_arr['unit'] = 'unit';
    		$return_arr['price'] = 'price';
    		$return_arr['quantity'] = 'quantity';
    		$return_arr['amount'] = 'amount';   		
    	} elseif ($type == 'DeliveryOrderConfirmPackage') {
    		$return_arr['logisticsCode'] = 'logistics_code';
    		$return_arr['logisticsName'] = 'logistics_name';
    		$return_arr['expressCode'] = 'express_code';
    		$return_arr['packageCode'] = 'package_code';
    		$return_arr['length'] = 'length';
    		$return_arr['width'] = 'width';
    		$return_arr['height'] = 'height';
    		$return_arr['theoreticalWeight'] = 'theoretical_weight';
    		$return_arr['weight'] = 'weight';
    		$return_arr['volume'] = 'volume';
    		$return_arr['invoiceNo'] = 'invoice_no';    		
    	} elseif ($type == 'DeliveryOrderConfirmPackageMaterial') {
    		$return_arr['type'] = 'type';
    		$return_arr['quantity'] = 'quantity';   		
    	} elseif ($type == 'DeliveryOrderConfirmPackageProduct') {
    		$return_arr['itemCode'] = 'item_code';
    		$return_arr['itemId'] = 'item_id';
    		$return_arr['quantity'] = 'quantity';   		
    	} elseif ($type == 'DeliveryOrderConfirmDetail') {
    		$return_arr['orderLineNo'] = 'order_line_no';
    		$return_arr['orderSourceCode'] = 'order_source_code';
    		$return_arr['subSourceCode'] = 'sub_source_order_code';
    		$return_arr['itemCode'] = 'item_code';
    		$return_arr['itemId'] = 'item_id';
    		$return_arr['inventoryType'] = 'inventory_type';
    		$return_arr['ownerCode'] = 'customer_id';
    		$return_arr['itemName'] = 'item_name';
    		$return_arr['extCode'] = 'ext_code';
    		$return_arr['planQty'] = 'plan_qty';
    		$return_arr['actualQty'] = 'actual_qty';
    		$return_arr['batchCode'] = 'batch_code';
    		$return_arr['productDate'] = 'product_date';
    		$return_arr['expireDate'] = 'expire_date';
    		$return_arr['produceCode'] = 'produce_code';
    		$return_arr['qrCode'] = 'qr_code';
    	} elseif ($type == 'DeliveryOrderConfirmDetailBath') {
    		$return_arr['batchCode'] = 'batch_code';
    		$return_arr['productDate'] = 'product_date';
    		$return_arr['expireDate'] = 'expire_date';
    		$return_arr['produceCode'] = 'produce_code';
    		$return_arr['inventoryType'] = 'inventory_type';
    		$return_arr['actualQty'] = 'actual_qty';
    	} elseif ($type == 'inbound_info_record') {
    	    $return_arr['totalOrderLines'] = 'total_order_lines';
    	    $return_arr['entryOrderCode'] = 'oms_order_no';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['entryOrderId'] = 'wms_order_no';
    	    $return_arr['entryOrderType'] = 'order_type';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['confirmType'] = 'confirm_type';
    	    $return_arr['status'] = 'order_status';
    	    $return_arr['operateTime'] = 'operate_time';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'inbound_detail_record') {
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['orderLineNo'] = 'line_no';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['itemName'] = 'item_name';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['planQty'] = 'expected_qty';
    	    $return_arr['actualQty'] = 'received_qty';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 't_inbound_detail_batch_record') {
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['actualQty'] = 'actual_qty';
    	} elseif ($type == 'return_order_inbound_info_record') {
    	    $return_arr['returnOrderCode'] = 'oms_order_no';
    	    $return_arr['returnOrderId'] = 'wms_order_no';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['orderConfirmTime'] = 'order_confirm_time';
    	    $return_arr['returnReason '] = 'user_define3';
    	    $return_arr['logisticsCode'] = 'carrier_id';
    	    $return_arr['logisticsName'] = 'carrier_name';
    	    $return_arr['expressCode'] = 'user_define2';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'return_order_sender_record') {
    	    $return_arr['company'] = 'sender_company';
    	    $return_arr['name'] = 'sender_name';
    	    $return_arr['zipCode'] = 'sender_zip_code';
    	    $return_arr['tel'] = 'sender_tel';
    	    $return_arr['mobile'] = 'sender_mobile';
    	    $return_arr['email'] = 'sender_email';
    	    $return_arr['countryCode'] = 'sender_country_code';
    	    $return_arr['province'] = 'sender_province';
    	    $return_arr['city'] = 'sender_city';
    	    $return_arr['area'] = 'sender_area';
    	    $return_arr['town'] = 'sender_town';
    	    $return_arr['detailAddress'] = 'sender_detail_address';
    	} elseif ($type == 'return_order_inbound_detail_record') {
    	    $return_arr['customerId'] = 'customer_id';
    	    $return_arr['orderLineNo'] = 'line_no';
    	    $return_arr['sourceOrderCode'] = 'source_order_code';
    	    $return_arr['subSourceOrderCode'] = 'sub_source_order_code';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['planQty'] = 'expected_qty';
    	    $return_arr['actualQty'] = 'received_qty';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['qrCode'] = 'qr_code';
    	} elseif ($type == 'stock_out_confirm') {
    	    $return_arr['customer_id'] = 'customer_id';
    	    $return_arr['totalOrderLines'] = 'total_order_lines';
    	    $return_arr['deliveryOrderCode'] = 'oms_order_no';
    	    $return_arr['deliveryOrderId'] = 'wms_order_no';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['status'] = 'order_status';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['confirmType'] = 'confirm_type';
    	    $return_arr['logisticsCode'] = 'carrier_id';
    	    $return_arr['logisticsName'] = 'carrier_name';
    	    $return_arr['expressCode'] = 'delivery_no';
    	    $return_arr['orderConfirmTime'] = 'required_delivery_time';
    	} elseif ($type == 'stock_out_package_confirm') {
    	    $return_arr['logisticsName'] = 'logistics_name';
    	    $return_arr['expressCode'] = 'express_code';
    	    $return_arr['packageCode'] = 'package_code';
    	    $return_arr['length'] = 'length';
    	    $return_arr['width'] = 'width';
    	    $return_arr['height'] = 'height';
    	    $return_arr['weight'] = 'weight';
    	    $return_arr['volume'] = 'volume';
    	} elseif ($type == 'stock_out_package_material_confirm') {
    	    $return_arr['type'] = 'type';
    	    $return_arr['quantity'] = 'quantity';
    	} elseif ($type == 'stock_out_package_product_confirm') {
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['quantity'] = 'quantity';
    	} elseif ($type == 'stock_out_detail_confirm') {
    	    $return_arr['order_no'] = 'order_no';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['orderLineNo'] = 'line_no';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['itemCode'] = 'sku';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['itemName'] = 'item_name';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['actualQty'] = 'qty_shipped';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	} elseif ($type == 'stock_out_detail_batch_confirm') {
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['actualQty'] = 'actual_qty';
    	} elseif ($type == 'order_process_report') {
    	    $return_arr['orderCode'] = 'order_code';
    	    $return_arr['orderId'] = 'order_id';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['processStatus'] = 'process_status';
    	    $return_arr['operatorCode'] = 'operator_code';
    	    $return_arr['operatorName'] = 'operator_name';
    	    $return_arr['operateTime'] = 'operate_time';
    	    $return_arr['operateInfo'] = 'operate_info';
    	    $return_arr['remark'] = 'remark';
    	    $return_arr['extendProps'] = 'extend_props';
    	} elseif($type == 'inventory_check_record'){
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['checkOrderCode'] = 'check_order_code';
    	    $return_arr['checkOrderId'] = 'check_order_id';
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['checkTime'] = 'check_time';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif($type == 'inventory_product_check_record'){
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['snCode'] = 'sn_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'store_process_confirm') {
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['processOrderCode'] = 'process_order_code';
    	    $return_arr['processOrderId'] = 'process_order_id';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['orderCompleteTime'] = 'order_complete_time';
    	    $return_arr['actualQty'] = 'actual_qty';
    	    $return_arr['extendProps'] = 'extend_props';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'store_process_material_confirm') {
    	    $return_arr['process_order_code'] = 'process_order_code';
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'store_process_product_confirm') {
    	    $return_arr['process_order_code'] = 'process_order_code';
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'stock_change_record') {
    	    $return_arr['ownerCode'] = 'customer_id';
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['orderCode'] = 'order_code';
    	    $return_arr['orderType'] = 'order_type';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['changeTime'] = 'change_time';
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['remark'] = 'remark';
    	} elseif ($type == 'stock_change_batch_report') {
    	    $return_arr['batchCode'] = 'batch_code';
    	    $return_arr['productDate'] = 'product_date';
    	    $return_arr['expireDate'] = 'expire_date';
    	    $return_arr['produceCode'] = 'produce_code';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	} elseif ($type == 'SnReportInfo') {
    		$return_arr['deliveryOrderCode'] = 'delivery_order_code';
    		$return_arr['deliveryOrderId'] = 'delivery_order_id';
    		$return_arr['warehouseCode'] = 'warehouse_code';
    		$return_arr['ownerCode'] = 'customer_id';
    		$return_arr['orderType'] = 'order_type';
    		$return_arr['outBizCode'] = 'out_biz_code';
    	} elseif ($type == 'SnReportDetail') {
    		$return_arr['itemCode'] = 'item_code';
    		$return_arr['itemId'] = 'item_id';
    		$return_arr['sn'] = 'sn';
    	} elseif ($type == 'item_lack_record') {
    	    $return_arr['customer_id'] = 'customer_id';
    		$return_arr['warehouseCode'] = 'warehouse_code';
    		$return_arr['deliveryOrderCode'] = 'delivery_order_code';
    		$return_arr['deliveryOrderId'] = 'delivery_order_id';
    		$return_arr['createTime'] = 'lack_create_time';
    		$return_arr['outBizCode'] = 'out_biz_code';
    	} elseif ($type == 'item_lack_product_record') {
    	    $return_arr['itemCode'] = 'item_code';
    		$return_arr['itemId'] = 'item_id';
    		$return_arr['inventoryType'] = 'inventory_type';
    		$return_arr['batchCode'] = 'batch_code';
    		$return_arr['productDate'] = 'product_date';
    		$return_arr['expireDate'] = 'expire_date';
    		$return_arr['produceCode'] = 'produce_code';
    		$return_arr['planQty'] = 'plan_aty';
    		$return_arr['lackQty'] = 'lack_aty';
    		$return_arr['reason'] = 'reason';
    	} elseif ($type == 'warehouse_reg') {
    	    $return_arr['warehouseCode'] = 'warehouse_code';
    	    $return_arr['warehouseName'] = 'descr_c';
    	    $return_arr['wmsURL'] = 'wms_url';
    	} elseif ($type == 'warehouse_reg_contact_info') {
    	    $return_arr['name'] = 'contact1';
    	    $return_arr['tel'] = 'contact1_tel2';
    	    $return_arr['mobile'] = 'contact1_tel1';
    	} elseif ($type == 'warehouse_reg_addr_info') {
    	    $return_arr['zipCode'] = 'zip';
    	    $return_arr['province'] = 'wh_province';
    	    $return_arr['city'] = 'wh_city';
    	    $return_arr['area'] = 'wh_area';
    	    $return_arr['town'] = 'wh_town';
    	    $return_arr['detailAddress'] = 'address1';
    	} elseif ($type == 'warehouse_update') {
    	    $return_arr['qmWarehouseCode'] = 'warehouse_code';
    	    $return_arr['warehouseName'] = 'descr_c';
    	    $return_arr['wmsURL'] = 'wms_url';
    	} elseif ($type == 'warehouse_update_contact_info') {
    	    $return_arr['name'] = 'contact1';
    	    $return_arr['tel'] = 'contact1_tel2';
    	    $return_arr['mobile'] = 'contact1_tel1';
    	} elseif ($type == 'warehouse_update_addr_info') {
    	    $return_arr['zipCode'] = 'zip';
    	    $return_arr['province'] = 'wh_province';
    	    $return_arr['city'] = 'wh_city';
    	    $return_arr['area'] = 'wh_area';
    	    $return_arr['town'] = 'wh_town';
    	    $return_arr['detailAddress'] = 'address1';
    	} elseif ($type == 'delivery_wave_info') {
    	    $return_arr['customer_id'] = 'customer_id';
    	    $return_arr['waveNum'] = 'wave_num';
    	    $return_arr['deliveryOrderCode'] = 'delivery_order_code';
    	    $return_arr['deliveryOrderId'] = 'delivery_order_id';
    	    $return_arr['num'] = 'num';
    	}
    	return $return_arr;
    }
}