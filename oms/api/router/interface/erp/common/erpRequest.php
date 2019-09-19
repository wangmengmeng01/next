<?php
/**
 * erp请求基类
 * User: 独孤羽<123517746@qq.com>
 * Date: 15-4-27 下午4:38
 */
class erpRequest
{

    public static $customerId = '';//客户ID
    public static $warehouseId = '';//仓库ID
    public static $erpBn = '';//erp接口编号
    public static $erpApi = '';//erp接口地址
    public static $erpApiSecret = '';//erp接口密钥
    public static $erpApiVer = '';//erp版本号
    public  $msgObj = null;
    public  $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }
    
    /**
     *
     * @param $method
     * @param $params
     */
    public function send($method, $params)
    {
    	$xml = new xml();
    	$xmlData = $this->utilObj->arrayToXml($params, $xml);  	
    	
    	$apiParams = array(
    			'method' => $method,
    			'customerid' => self::$customerId,
    			'warehouseid' => self::$warehouseId,
    			'messageid' => service::$_messageId,
    			'apptoken' => service::$_appToken,
    			'appkey' => service::$_appKey,
    			'sign' => urlencode(strtoupper(base64_encode(md5(self::$erpApiSecret.$xmlData.self::$erpApiSecret)))),
    			'timestamp' => service::$_timeStamp,
    			'data' => urlencode($xmlData)
    	);

    	$dataPost = '';
    	foreach ($apiParams as $key=>$val)
    	{
    		if ($dataPost == '') {
    			$dataPost = $key . '=' . $val;
    		} else {
    			$dataPost .= '&' . $key . '=' . $val;
    		}    		
    	} 	
    	$rs = $this->utilObj->curl(self::$erpApi, $dataPost);
    	if($rs == ''){
    		$msg = '请求超时';
    		$msgcode = 'E001';
    		$status = 0;
    	}else{
    		$response = $xml->xmlStr2array($rs);
    		$response = $response['return'];
    		$response = $this->utilObj->filter_null($response);
    		$response['resultInfo'] = $this->utilObj->getResultInfo($response['resultInfo']);  		

    		if ($response['returnFlag'] == '1') {   //全部成功
    			$status = 1;
    		} elseif ($response['returnFlag'] == '2') {  //部分成功，部分失败
    			$status = 2;
    		} else {   //失败
    			$status = 0;
    		}
    		$msg = $response['returnDesc'];
    		$msgcode = $response['returnCode'];
    		$data = $response['resultInfo'];
    	}
        
    	//接口层记录日志
        $logExt = array(
        		'api_url' => self::$erpApi,
        		'api_method' => $method,
        		'api_params' => $apiParams,
        		'return_msg' => $rs == '' ? '请求超时' : $rs
        );
   
        return $this->msgObj->output($status, $msg, $msgcode, $data, $logExt);
    }

    /**
     * 接口参数与数据库字段对应关系
     * @param  $type 类型（wareHouse:仓库  customer:货主  goods:商品  supplier:供应商  shop:店铺 ）
     * @return array
     */
    public function get_dataBase_relation($type)
    {
    	$return_arr = array();
    	if ($type == 'wareHouse') {
    		$return_arr['CustomerID'] = 'warehouse_code';
    		$return_arr['Descr_C'] = 'descr_c';
    		$return_arr['Descr_E'] = 'descr_e';
    		$return_arr['Address1'] = 'address1';
    		$return_arr['Address2'] = 'address2';
    		$return_arr['Address3'] = 'address3';
    		$return_arr['Country'] = 'country';
    		$return_arr['Province'] = 'province';
    		$return_arr['City'] = 'city';
    		$return_arr['Zip'] = 'zip';
    		$return_arr['Contact1'] = 'contact1';
    		$return_arr['Contact1_Tel1'] = 'contact1_tel1';
    		$return_arr['Contact1_Tel2'] = 'contact1_tel2';
    		$return_arr['Contact1_Fax'] = 'contact1_fax';
    		$return_arr['Contact1_Title'] = 'contact1_title';
    		$return_arr['Contact1_Email'] = 'contact1_email';
    		$return_arr['Contact2'] = 'contact2';
    		$return_arr['Contact2_Tel1'] = 'contact2_tel1';
    		$return_arr['Contact2_Tel2'] = 'contact2_tel2';
    		$return_arr['Contact2_Fax'] = 'contact2_fax';
    		$return_arr['Contact2_Title'] = 'contact2_title';
    		$return_arr['Contact3'] = 'contact3';
    		$return_arr['Contact3_Tel1'] = 'contact3_tel1';
    		$return_arr['Contact3_Tel2'] = 'contact3_tel2';
    		$return_arr['Contact3_Fax'] = 'contact3_fax';
    		$return_arr['Contact3_Title'] = 'contact3_title';
    		$return_arr['Currency'] = 'currency';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'stop';
    		$return_arr['R_Owner'] = 'r_owner';
    		$return_arr['UDF1'] = 'udf1';
    		$return_arr['UDF2'] = 'udf2';
    		$return_arr['UDF3'] = 'udf3';
    		$return_arr['UDF4'] = 'udf4';
    		$return_arr['UDF5'] = 'udf5';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['BankAccount'] = 'bank_account';
    		$return_arr['easycode'] = 'easy_code';
    		$return_arr['Active_Flag'] = 'active_flag';
    	} elseif ($type == 'customer') {
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['Descr_C'] = 'descr_c';
    		$return_arr['Descr_E'] = 'descr_e';
    		$return_arr['Address1'] = 'address1';
    		$return_arr['Address2'] = 'address2';
    		$return_arr['Address3'] = 'address3';
    		$return_arr['Country'] = 'country';
    		$return_arr['Province'] = 'province';
    		$return_arr['City'] = 'city';
    		$return_arr['Zip'] = 'zip';
    		$return_arr['Contact1'] = 'contact1';
    		$return_arr['Contact1_Tel1'] = 'contact1_tel1';
    		$return_arr['Contact1_Tel2'] = 'contact1_tel2';
    		$return_arr['Contact1_Fax'] = 'contact1_fax';
    		$return_arr['Contact1_Title'] = 'contact1_title';
    		$return_arr['Contact1_Email'] = 'contact1_email';
    		$return_arr['Contact2'] = 'contact2';
    		$return_arr['Contact2_Tel1'] = 'contact2_tel1';
    		$return_arr['Contact2_Tel2'] = 'contact2_tel2';
    		$return_arr['Contact2_Fax'] = 'contact2_fax';
    		$return_arr['Contact2_Title'] = 'contact2_title';
    		$return_arr['Contact3'] = 'contact3';
    		$return_arr['Contact3_Tel1'] = 'contact3_tel1';
    		$return_arr['Contact3_Tel2'] = 'contact3_tel2';
    		$return_arr['Contact3_Fax'] = 'contact3_fax';
    		$return_arr['Contact3_Title'] = 'contact3_title';
    		$return_arr['Currency'] = 'currency';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'stop';
    		$return_arr['R_Owner'] = 'r_owner';
    		$return_arr['UDF1'] = 'udf1';
    		$return_arr['UDF2'] = 'udf2';
    		$return_arr['UDF3'] = 'udf3';
    		$return_arr['UDF4'] = 'udf4';
    		$return_arr['UDF5'] = 'udf5';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['BankAccount'] = 'bank_account';
    		$return_arr['easycode'] = 'easy_code';
    		$return_arr['Active_Flag'] = 'active_flag';
    	} elseif ($type == 'supplier') {
    		$return_arr['CustomerID'] = 'supplier_code';
    		$return_arr['Descr_C'] = 'descr_c';
    		$return_arr['Descr_E'] = 'descr_e';
    		$return_arr['Address1'] = 'address1';
    		$return_arr['Address2'] = 'address2';
    		$return_arr['Address3'] = 'address3';
    		$return_arr['Country'] = 'country';
    		$return_arr['Province'] = 'province';
    		$return_arr['City'] = 'city';
    		$return_arr['Zip'] = 'zip';
    		$return_arr['Contact1'] = 'contact1';
    		$return_arr['Contact1_Tel1'] = 'contact1_tel1';
    		$return_arr['Contact1_Tel2'] = 'contact1_tel2';
    		$return_arr['Contact1_Fax'] = 'contact1_fax';
    		$return_arr['Contact1_Title'] = 'contact1_title';
    		$return_arr['Contact1_Email'] = 'contact1_email';
    		$return_arr['Contact2'] = 'contact2';
    		$return_arr['Contact2_Tel1'] = 'contact2_tel1';
    		$return_arr['Contact2_Tel2'] = 'contact2_tel2';
    		$return_arr['Contact2_Fax'] = 'contact2_fax';
    		$return_arr['Contact2_Title'] = 'contact2_title';
    		$return_arr['Contact3'] = 'contact3';
    		$return_arr['Contact3_Tel1'] = 'contact3_tel1';
    		$return_arr['Contact3_Tel2'] = 'contact3_tel2';
    		$return_arr['Contact3_Fax'] = 'contact3_fax';
    		$return_arr['Contact3_Title'] = 'contact3_title';
    		$return_arr['Currency'] = 'currency';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'stop';
    		$return_arr['R_Owner'] = 'r_owner';
    		$return_arr['UDF1'] = 'udf1';
    		$return_arr['UDF2'] = 'udf2';
    		$return_arr['UDF3'] = 'udf3';
    		$return_arr['UDF4'] = 'udf4';
    		$return_arr['UDF5'] = 'udf5';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['BankAccount'] = 'bank_account';
    		$return_arr['easycode'] = 'easy_code';
    		$return_arr['Active_Flag'] = 'active_flag';
    	}elseif ($type == 'inbound_info') {
    		$return_arr['OrderNo'] = 'order_no';
    		$return_arr['OrderType'] = 'order_type';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['WarehouseID'] = 'warehouse_code';
    		$return_arr['ExpectedArriveTime1'] = 'expected_arrive_time1';
    		$return_arr['ASNReference2'] = 'asn_reference2';
    		$return_arr['ASNReference3'] = 'asn_reference3';
    		$return_arr['ASNReference4'] = 'asn_reference4';
    		$return_arr['ASNReference5'] = 'asn_reference5';
    		$return_arr['PONO'] = 'pono';
    		$return_arr['I_Contact'] = 'i_contact';
    		$return_arr['IssuePartyName'] = 'issue_party_name';
    		$return_arr['CountryOfOrigin'] = 'country_of_origin';
    		$return_arr['CountryOfDestination'] = 'country_of_destination';
    		$return_arr['PlaceOfLoading'] = 'place_of_loading';
    		$return_arr['PlaceOfDischarge'] = 'place_of_discharge';
    		$return_arr['PlaceofDelivery'] = 'placeof_delivery';
    		$return_arr['UserDefine1'] = 'user_define1';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['SupplierID'] = 'supplier_code';
    		$return_arr['Supplier_Name'] = 'supplier_name';
    		$return_arr['CarrierID'] = 'carrier_id';
    		$return_arr['CarrierName'] = 'carrier_name';
    		$return_arr['H_EDI_02'] = 'h_edi_02';
    		$return_arr['H_EDI_03'] = 'h_edi_03';
    		$return_arr['H_EDI_04'] = 'h_edi_04';
    		$return_arr['H_EDI_05'] = 'h_edi_05';
    		$return_arr['H_EDI_06'] = 'h_edi_06';
    		$return_arr['H_EDI_07'] = 'h_edi_07';
    		$return_arr['H_EDI_08'] = 'h_edi_08';
    		$return_arr['H_EDI_09'] = 'h_edi_09';
    		$return_arr['H_EDI_10'] = 'h_edi_10';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['UserDefine7'] = 'user_define7';
    		$return_arr['UserDefine8'] = 'user_define8';
    		$return_arr['Priority'] = 'priority';
    		$return_arr['FollowUp'] = 'follow_up';
    	} elseif ($type == 'inbound_info_record') {
    		$return_arr['OMSOrderNo'] = 'oms_order_no';
    		$return_arr['WMSOrderNo'] = 'wms_order_no';
    		$return_arr['OrderType'] = 'order_type';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['WarehouseID'] = 'warehouse_code';
    		$return_arr['Status'] = 'order_status';
    		$return_arr['Desc'] = 'order_desc';
    		$return_arr['ASNCreationTime'] = 'asn_creation_time';
    		$return_arr['ExpectedArriveTime1'] = 'expected_arrive_time1';
    		$return_arr['ASNReference2'] = 'asn_reference2';
    		$return_arr['ASNReference3'] = 'asn_reference3';
    		$return_arr['ASNReference4'] = 'asn_reference4';
    		$return_arr['ASNReference5'] = 'asn_reference5';
    		$return_arr['PONO'] = 'pono';
    		$return_arr['I_Contact'] = 'i_contact';
    		$return_arr['IssuePartyName'] = 'issue_party_name';
    		$return_arr['CountryOfOrigin'] = 'country_of_origin';
    		$return_arr['CountryOfDestination'] = 'country_of_destination';
    		$return_arr['PlaceOfLoading'] = 'place_of_loading';
    		$return_arr['PlaceOfDischarge'] = 'place_of_discharge';
    		$return_arr['PlaceofDelivery'] = 'placeof_delivery';
    		$return_arr['UserDefine1'] = 'user_define1';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['SupplierID'] = 'supplier_code';
    		$return_arr['Supplier_Name'] = 'supplier_name';
    		$return_arr['CarrierID'] = 'carrier_id';
    		$return_arr['CarrierName'] = 'carrier_name';
    		$return_arr['H_EDI_02'] = 'h_edi_02';
    		$return_arr['H_EDI_03'] = 'h_edi_03';
    		$return_arr['H_EDI_04'] = 'h_edi_04';
    		$return_arr['H_EDI_05'] = 'h_edi_05';
    		$return_arr['H_EDI_06'] = 'h_edi_06';
    		$return_arr['H_EDI_07'] = 'h_edi_07';
    		$return_arr['H_EDI_08'] = 'h_edi_08';
    		$return_arr['H_EDI_09'] = 'h_edi_09';
    		$return_arr['H_EDI_10'] = 'h_edi_10';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['UserDefine7'] = 'user_define7';
    		$return_arr['UserDefine8'] = 'user_define8';
    		$return_arr['Priority'] = 'priority';
    		$return_arr['FollowUp'] = 'follow_up'; 		
    	} elseif ($type == 'inbound_detail') {
    		$return_arr['LineNo'] = 'line_no';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['SKU'] = 'sku';
    		$return_arr['ExpectedQty'] = 'expected_qty';
    		$return_arr['LotAtt01'] = 'lot_att01';
    		$return_arr['LotAtt02'] = 'lot_att02';
    		$return_arr['LotAtt03'] = 'lot_att03';
    		$return_arr['LotAtt04'] = 'lot_att04';
    		$return_arr['LotAtt05'] = 'lot_att05';
    		$return_arr['LotAtt06'] = 'lot_att06';
    		$return_arr['LotAtt07'] = 'lot_att07';
    		$return_arr['LotAtt08'] = 'lot_att08';
    		$return_arr['LotAtt09'] = 'lot_att09';
    		$return_arr['LotAtt10'] = 'lot_att10';
    		$return_arr['LotAtt11'] = 'lot_att11';
    		$return_arr['LotAtt12'] = 'lot_att12';
    		$return_arr['TotalPrice'] = 'total_price';
    		$return_arr['UserDefine1'] = 'user_define1';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['D_EDI_03'] = 'd_edi_03';
    		$return_arr['D_EDI_04'] = 'd_edi_04';
    		$return_arr['D_EDI_05'] = 'd_edi_05';
    		$return_arr['D_EDI_06'] = 'd_edi_06';
    		$return_arr['D_EDI_07'] = 'd_edi_07';
    		$return_arr['D_EDI_08'] = 'd_edi_08';
    		$return_arr['D_EDI_09'] = 'd_edi_09';
    		$return_arr['D_EDI_10'] = 'd_edi_10';
    		$return_arr['D_EDI_11'] = 'd_edi_11';
    		$return_arr['D_EDI_12'] = 'd_edi_12';
    		$return_arr['D_EDI_13'] = 'd_edi_13';
    		$return_arr['D_EDI_14'] = 'd_edi_14';
    		$return_arr['D_EDI_15'] = 'd_edi_15';
    		$return_arr['D_EDI_16'] = 'd_edi_16';
    	} elseif ($type == 'inbound_detail_record') {
    		$return_arr['LineNo'] = 'line_no';
    		$return_arr['SKU'] = 'sku';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['LineStatus'] = 'line_status';
    		$return_arr['LineDesc'] = 'line_desc';
    		$return_arr['ExpectedQty'] = 'expected_qty';
    		$return_arr['ReceivedQty'] = 'received_qty';
    		$return_arr['ReceivedTime'] = 'received_time';
    		$return_arr['LotAtt01'] = 'lot_att01';
    		$return_arr['LotAtt02'] = 'lot_att02';
    		$return_arr['LotAtt03'] = 'lot_att03';
    		$return_arr['LotAtt04'] = 'lot_att04';
    		$return_arr['LotAtt05'] = 'lot_att05';
    		$return_arr['LotAtt06'] = 'lot_att06';
    		$return_arr['LotAtt07'] = 'lot_att07';
    		$return_arr['LotAtt08'] = 'lot_att08';
    		$return_arr['LotAtt09'] = 'lot_att09';
    		$return_arr['LotAtt10'] = 'lot_att10';
    		$return_arr['LotAtt11'] = 'lot_att11';
    		$return_arr['LotAtt12'] = 'lot_att12';
    		$return_arr['TotalPrice'] = 'total_price';
    		$return_arr['UserDefine1'] = 'user_define1';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['D_EDI_03'] = 'd_edi_03';
    		$return_arr['D_EDI_04'] = 'd_edi_04';
    		$return_arr['D_EDI_05'] = 'd_edi_05';
    		$return_arr['D_EDI_06'] = 'd_edi_06';
    		$return_arr['D_EDI_07'] = 'd_edi_07';
    		$return_arr['D_EDI_08'] = 'd_edi_08';
    		$return_arr['D_EDI_09'] = 'd_edi_09';
    		$return_arr['D_EDI_10'] = 'd_edi_10';
    		$return_arr['D_EDI_11'] = 'd_edi_11';
    		$return_arr['D_EDI_12'] = 'd_edi_12';
    		$return_arr['D_EDI_13'] = 'd_edi_13';
    		$return_arr['D_EDI_14'] = 'd_edi_14';
    		$return_arr['D_EDI_15'] = 'd_edi_15';
    		$return_arr['D_EDI_16'] = 'd_edi_16';
    	} elseif($type == 'inventory'){
    		$return_arr['SKU'] = 'sku';
    		$return_arr['Qty'] = 'qty';
    		$return_arr['Lotatt01'] = 'lotatt01';
    		$return_arr['Lotatt02'] = 'lotatt02';
    		$return_arr['Lotatt03'] = 'lotatt03';
    		$return_arr['Lotatt04'] = 'lotatt04';
    		$return_arr['Lotatt05'] = 'lotatt05';
    		$return_arr['Lotatt06'] = 'lotatt06';
    		$return_arr['Lotatt07'] = 'lotatt07';
    		$return_arr['Lotatt08'] = 'lotatt08';
    		$return_arr['Lotatt09'] = 'lotatt09';
    		$return_arr['Lotatt10'] = 'lotatt10';
    		$return_arr['Lotatt11'] = 'lotatt11';
    		$return_arr['Lotatt12'] = 'lotatt12';
    		$return_arr['Udf1'] = 'udf1';
    		$return_arr['Udf2'] = 'udf2';
    		$return_arr['Udf3'] = 'udf3';
    		$return_arr['Udf4'] = 'udf4';
    		$return_arr['Udf5'] = 'udf5';
    		$return_arr['Udf6'] = 'udf6';
    		$return_arr['Udf7'] = 'udf7';
    		$return_arr['Udf8'] = 'udf8';
    		$return_arr['Udf9'] = 'udf9';
    		$return_arr['Udf10'] = 'udf10';
    	} elseif($type == 'outbound_detail_record'){
    		$return_arr['OrderNo'] = 'order_no';
    		$return_arr['LineNo'] = 'line_no';
    		$return_arr['SKU'] = 'sku';
    		$return_arr['QtyOrdered'] = 'qty_ordered';
    		$return_arr['QtyShipped'] = 'qty_shipped';
    		$return_arr['ShippedTime'] = 'shipped_time';
    		$return_arr['DeliveryNo'] = 'delivery_no';
    		$return_arr['Weight'] = 'weight';
    		$return_arr['Lotatt01'] = 'lot_att01';
    		$return_arr['Lotatt02'] = 'lot_att02';
    		$return_arr['Lotatt03'] = 'lot_att03';
    		$return_arr['Lotatt04'] = 'lot_att04';
    		$return_arr['Lotatt05'] = 'lot_att05';
    		$return_arr['Lotatt06'] = 'lot_att06';
    		$return_arr['Lotatt07'] = 'lot_att07';
    		$return_arr['Lotatt08'] = 'lot_att08';
    		$return_arr['Lotatt09'] = 'lot_att09';
    		$return_arr['Lotatt10'] = 'lot_att10';
    		$return_arr['Lotatt11'] = 'lot_att11';
    		$return_arr['Lotatt12'] = 'lot_att12';
    		$return_arr['UserDefine1'] = 'user_define1';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['Price'] = 'price';
    		$return_arr['inventoryType'] = 'inventory_type';
    		$return_arr['D_EDI_03'] = 'd_edi_03';
    		$return_arr['D_EDI_04'] = 'd_edi_04';
    		$return_arr['D_EDI_05'] = 'd_edi_05';
    		$return_arr['D_EDI_06'] = 'd_edi_06';
    		$return_arr['D_EDI_07'] = 'd_edi_07';
    		$return_arr['D_EDI_08'] = 'd_edi_08';
    		$return_arr['D_EDI_09'] = 'd_edi_09';
    		$return_arr['D_EDI_10'] = 'd_edi_10';
    		$return_arr['D_EDI_11'] = 'd_edi_11';
    		$return_arr['D_EDI_12'] = 'd_edi_12';
    		$return_arr['D_EDI_13'] = 'd_edi_13';
    		$return_arr['D_EDI_14'] = 'd_edi_14';
    		$return_arr['D_EDI_15'] = 'd_edi_15';
    		$return_arr['D_EDI_16'] = 'd_edi_16';
    	} elseif($type == 'outbound_status_record'){
    		$return_arr['OrderNo'] = 'order_no';
    		$return_arr['OrderType'] = 'order_type';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['WarehouseID'] = 'warehouse_code';
    		$return_arr['Status'] = 'order_status';
    		$return_arr['Desc'] = 'order_desc';
    		$return_arr['Time'] = 'operator_time';
    		$return_arr['Udf1'] = 'udf1';
    		$return_arr['Udf2'] = 'Udf2';
    		$return_arr['Udf3'] = 'Udf3';
    		$return_arr['Udf4'] = 'Udf4';
    		$return_arr['Udf5'] = 'Udf5';
    		$return_arr['Udf6'] = 'Udf6';
    		$return_arr['Udf7'] = 'Udf7';
    		$return_arr['Udf8'] = 'Udf8';
    		$return_arr['Udf9'] = 'Udf9';
    		$return_arr['Udf10'] = 'udf10';
    	} elseif ($type == 'outbound_info') {
    		$return_arr['OrderNo'] = 'order_no';
    		$return_arr['OrderType'] = 'order_type';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['WarehouseID'] = 'warehouse_code';
    		$return_arr['OrderTime'] = 'order_time';
    		$return_arr['ExpectedShipmentTime1'] = 'expected_shipment_time1';
    		$return_arr['RequiredDeliveryTime'] = 'required_delivery_time';
    		$return_arr['SOReference2'] = 'so_reference2';
    		$return_arr['SOReference3'] = 'so_reference3';
    		$return_arr['SOReference4'] = 'so_reference4';
    		$return_arr['SOReference5'] = 'so_reference5';
    		$return_arr['DeliveryNo'] = 'delivery_no';
    		$return_arr['ConsigneeID'] = 'consignee_id';
    		$return_arr['ConsigneeName'] = 'consignee_name';
    		$return_arr['C_Country'] = 'c_country';
    		$return_arr['C_Province'] = 'c_province';
    		$return_arr['C_City'] = 'c_city';
    		$return_arr['C_Tel1'] = 'c_tel1';
    		$return_arr['C_Tel2'] = 'c_tel2';
    		$return_arr['C_ZIP'] = 'c_zip';
    		$return_arr['C_Mail'] = 'c_mail';
    		$return_arr['C_Address1'] = 'c_address1';
    		$return_arr['C_Address2'] = 'c_address2';
    		$return_arr['C_Address3'] = 'c_address3';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['InvoicePrintFlag'] = 'invoice_print_flag';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['H_EDI_01'] = 'h_edi_01';
    		$return_arr['H_EDI_02'] = 'h_edi_02';
    		$return_arr['H_EDI_03'] = 'h_edi_03';
    		$return_arr['H_EDI_04'] = 'h_edi_04';
    		$return_arr['H_EDI_05'] = 'h_edi_05';
    		$return_arr['H_EDI_06'] = 'h_edi_06';
    		$return_arr['H_EDI_07'] = 'h_edi_07';
    		$return_arr['H_EDI_08'] = 'h_edi_08';
    		$return_arr['H_EDI_09'] = 'h_edi_09';
    		$return_arr['H_EDI_10'] = 'h_edi_10';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'route_stop';
    		$return_arr['CarrierMail'] = 'carrier_mail';
    		$return_arr['CarrierFax'] = 'carrier_fax';
    		$return_arr['Channel'] = 'channel';
    		$return_arr['CarrierId'] = 'carrier_id';
    		$return_arr['CarrierName'] = 'carrier_name';
    	} elseif ($type == 'outbound_detail') {
    		$return_arr['LineNo'] = 'line_no';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['SKU'] = 'sku';
    		$return_arr['QtyOrdered'] = 'qty_ordered';
    		$return_arr['LotAtt01'] = 'lot_att01';
    		$return_arr['LotAtt02'] = 'lot_att02';
    		$return_arr['LotAtt03'] = 'lot_att03';
    		$return_arr['LotAtt04'] = 'lot_att04';
    		$return_arr['LotAtt05'] = 'lot_att05';
    		$return_arr['LotAtt06'] = 'lot_att06';
    		$return_arr['LotAtt07'] = 'lot_att07';
    		$return_arr['LotAtt08'] = 'lot_att08';
    		$return_arr['LotAtt09'] = 'lot_att09';
    		$return_arr['LotAtt10'] = 'lot_att10';
    		$return_arr['LotAtt11'] = 'lot_att11';
    		$return_arr['LotAtt12'] = 'lot_att12';
    		$return_arr['UserDefine1'] = 'uer_define1';
    		$return_arr['UserDefine2'] = 'uer_define2';
    		$return_arr['UserDefine3'] = 'uer_define3';
    		$return_arr['UserDefine4'] = 'uer_define4';
    		$return_arr['UserDefine5'] = 'uer_define5';
    		$return_arr['UserDefine6'] = 'uer_define6';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['Price'] = 'price';
    		$return_arr['D_EDI_03'] = 'd_edi_03';
    		$return_arr['D_EDI_04'] = 'd_edi_04';
    		$return_arr['D_EDI_05'] = 'd_edi_05';
    		$return_arr['D_EDI_06'] = 'd_edi_06';
    		$return_arr['D_EDI_07'] = 'd_edi_07';
    		$return_arr['D_EDI_08'] = 'd_edi_08';
    		$return_arr['D_EDI_09'] = 'd_edi_09';
    		$return_arr['D_EDI_10'] = 'd_edi_10';
    		$return_arr['D_EDI_11'] = 'd_edi_11';
    		$return_arr['D_EDI_12'] = 'd_edi_12';
    		$return_arr['D_EDI_13'] = 'd_edi_13';
    		$return_arr['D_EDI_14'] = 'd_edi_14';
    		$return_arr['D_EDI_15'] = 'd_edi_15';
    		$return_arr['D_EDI_16'] = 'd_edi_16';
    	} elseif ($type == 'outbound_bill') {
    		$return_arr['OrderNo'] = 'order_no';
    		$return_arr['LineNumber'] = 'line_number';
    		$return_arr['Title'] = 'title';
    		$return_arr['Reference1'] = 'reference1';
    		$return_arr['SKU'] = 'sku';
    		$return_arr['UOM'] = 'uom';
    		$return_arr['QTY'] = 'qty';
    		$return_arr['UnitPrice'] = 'unit_price';
    		$return_arr['Amount'] = 'amount';
    		$return_arr['TAXRATE'] = 'taxrate';
    		$return_arr['TAXAMOUNT'] = 'taxamount';
    		$return_arr['SKUDESCR'] = 'skudescr';
    		$return_arr['DetailTitle'] = 'detail_title';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['UserDefine1'] = 'user_define1';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    	} elseif ($type == 'outbound_info_recode') {
    		$return_arr['OMSOrderNo'] = 'oms_order_no';
    		$return_arr['WMSOrderNo'] = 'wms_order_no';
    		$return_arr['OrderType'] = 'order_type';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['WarehouseID'] = 'warehouse_code';
    		$return_arr['OrderTime'] = 'order_time';
    		$return_arr['DeliveryNo'] = 'delivery_no';   		   		    		
    		$return_arr['SOReference2'] = 'so_reference2';
    		$return_arr['SOReference3'] = 'so_reference3';
    		$return_arr['SOReference4'] = 'so_reference4';
    		$return_arr['SOReference5'] = 'so_reference5';
    		$return_arr['Weight'] = 'weight';
    		$return_arr['C_Country'] = 'c_country';
    		$return_arr['ConsigneeID'] = 'consignee_id';
    		$return_arr['ConsigneeName'] = 'consignee_name';
    		$return_arr['ExpectedShipmentTime1'] = 'expected_shipment_time1';
    		$return_arr['RequiredDeliveryTime'] = 'required_delivery_time';
    		$return_arr['C_Province'] = 'c_province';
    		$return_arr['C_City'] = 'c_city';
    		$return_arr['C_Tel1'] = 'c_tel1';
    		$return_arr['C_Tel2'] = 'c_tel2';
    		$return_arr['C_ZIP'] = 'c_zip';
    		$return_arr['C_Mail'] = 'c_mail';
    		$return_arr['C_Address1'] = 'c_address1';
    		$return_arr['C_Address2'] = 'c_address2';
    		$return_arr['C_Address3'] = 'c_address3';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['InvoicePrintFlag'] = 'invoice_print_flag';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['H_EDI_01'] = 'h_edi_01';
    		$return_arr['H_EDI_02'] = 'h_edi_02';
    		$return_arr['H_EDI_03'] = 'h_edi_03';
    		$return_arr['H_EDI_04'] = 'h_edi_04';
    		$return_arr['H_EDI_05'] = 'h_edi_05';
    		$return_arr['H_EDI_06'] = 'h_edi_06';
    		$return_arr['H_EDI_07'] = 'h_edi_07';
    		$return_arr['H_EDI_08'] = 'h_edi_08';
    		$return_arr['H_EDI_09'] = 'h_edi_09';
    		$return_arr['H_EDI_10'] = 'h_edi_10';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'route_stop';
    		$return_arr['CarrierMail'] = 'carrier_mail';
    		$return_arr['CarrierFax'] = 'carrier_fax';
    		$return_arr['Channel'] = 'channel';
    		$return_arr['CarrierId'] = 'carrier_id';
    		$return_arr['CarrierName'] = 'carrier_name';
    	} elseif ($type == 'inventory_report') {
    	    $return_arr['WarehouseID'] = 'warehouse_code';
    	    $return_arr['checkOrderCode'] = 'check_order_code';
    	    $return_arr['checkOrderId'] = 'check_order_id';
    	    $return_arr['CustomerID'] = 'customer_id';
    	    $return_arr['checkTime'] = 'check_time';
    	    $return_arr['outBizCode'] = 'out_biz_code';
    	} elseif ($type == 'inventory_item_report') {
    	    $return_arr['itemCode'] = 'item_code';
    	    $return_arr['itemId'] = 'item_id';
    	    $return_arr['inventoryType'] = 'inventory_type';
    	    $return_arr['quantity'] = 'quantity';
    	}
    	return $return_arr;
    }
}