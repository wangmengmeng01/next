<?php
/**
 *wms->菜鸟
 */
class cnRequest
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

    /**
     * 转发数据给菜鸟
     */
    public function send($data)
    {
        $apiParams = array(
            'msg_type'              => cn_storage_service::$_api_id,
            'logistic_provider_id'  => cn_storage_service::$_logistic_provider_id,
            'data_digest'           => base64_encode(md5($data .STORAGE_APP_SECRET ,true)),
            'logistics_interface'   => $data
        );
        
        //推送数据到菜鸟接口
        $httpObj = new httpclient();
        cn_storage_service::$_mid_req_time = util::microtime_float();
        try{
            $rs = $httpObj->post(STORAGE_CN_URL, $apiParams);
        }catch(Exception $e){
            echo $e->getMessage();  
        }
        cn_storage_service::$_mid_resp_time = util::microtime_float();
        
        if($rs == '' || $rs == '-3'){
            $msg = '请求超时';
            $msgcode = 'E001';
            $status = false;
        } else {
            $xmlObj = new xml();
            $response = $xmlObj->xmlStr2array($rs);
            $response = $this->utilObj->filter_null($response);    

            if($response['success'] == 'true'){
                $status = true;
                $msg = '';
                $msgcode = '';
            }else{
                $status = false;
                
                $msg = $response['errorMsg'];
                $msgcode = $response['errorCode'];
            }
        }
        
        //接口层记录日志
        $logExt = array(
            'api_params' => $apiParams,
            'rs_flag'    => $status == 1 ? 'true' : 'false',
            'return_msg' => $rs == '' ? '请求超时' : $rs
        );
        return $this->msgObj->outputCnStorage($status, $msg, $msgcode, $logExt);
    }

    public function requestOmsToWms($data,$method){
        $requestParams = array(
            'msg_type'              => $method,
            'from_code'             => STORAGE_APP_KEY,
            'partner_code'          => cn_storage_service::$_partner_code,
            'data_digest'           => base64_encode(md5( $data . STORAGE_APP_SECRET, true)),
            'logistics_interface'   => $data,
            'msg_id'                => rand(10000001,99999999)
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
    
            if($response['success'] == 'true'){
                $status = true;
                $msg = '';
                $msgcode = '';
            }else{
                $status = false;
                $msg = $response['errMsg'];
                $msgcode = $response['errorCode'];
            }
        }
        $logExt = array(
            'return_msg' => $rs
        );
        return $this->msgObj->outputCnStorage($status,$msg,$msgcode,$logExt);
    }

    /**
     * 接口参数与数据库字段对应关系
     * @param  $type 类型
     * @return array
     */
    public function get_dataBase_relation($type)
    {
        switch($type) {
            case 'wms_stock_in_order_confirm':
                $return_arr['orderCode']    = 'cn_order_id';
                $return_arr['orderType']    = 'order_type';
                $return_arr['outBizCode']   = 'out_biz_code';
                $return_arr['confirmType']  = 'confirm_type';
                $return_arr['orderConfirmTime'] = 'order_confirm_time';
                break;
            case 'wms_stock_in_order_confirm_order_item':
                $return_arr['orderItemId']  = 'line_no';
                break;
            case 'wms_stock_in_order_confirm_items':
                $return_arr['inventoryType'] = 'inventory_type';
                $return_arr['quantity']      = 'received_qty';
                break;
            case 'outbound_info_record':
                $return_arr['orderCode']        = 'cn_order_code';
                $return_arr['orderType']        = 'order_type';
                $return_arr['outBizCode']       = 'out_biz_code';
                $return_arr['confirmType']      = 'confirm_type';
                $return_arr['orderConfirmTime'] = 'required_delivery_time';
                break;
            case 'outbound_info_detail_record':
                $return_arr['orderItemId']      = 'line_no';
                $return_arr['inventoryType']    = 'inventory_type';
                $return_arr['quantity']         = 'qty_shipped';
                $return_arr['batchCode']        = 'batch_code';
                break;
            case 'outbound_package_info_record':
                $return_arr['tmsCode']          = 'tms_code';
                $return_arr['tmsOrderCode']     = 'express_code';
                $return_arr['tmsServiceName']   = 'logistics_name';
                $return_arr['packageCode']      = 'package_code';
                $return_arr['packageWeight']    = 'weight';
                $return_arr['packageLength']    = 'length';
                $return_arr['packageWidth']     = 'width';
                $return_arr['packageHeight']    = 'height';
                $return_arr['packageVolumn']    = 'volume';
                break;
            case 'outbound_package_product_record':
                $return_arr['itemId']           = 'cn_item_id';
                $return_arr['itemCode']         = 'item_code';
                $return_arr['itemQuantity']     = 'quantity';
                break;
            case 'delivery_info_record':
                $return_arr['orderConfirmTime'] = 'order_confirm_time';
                $return_arr['orderCode']        = 'cn_order_code';
                $return_arr['orderType']        = 'order_type';
                $return_arr['outBizCode']       = 'out_biz_code';
                $return_arr['confirmType']      = 'confirm_type';
                break;
            case 'delivery_detail_record':
                $return_arr['inventoryType'] = 'inventory_type';
                $return_arr['quantity']      = 'actual_qty';
                $return_arr['batchCode']     = 'batch_code';   
                $return_arr['qrCode']        = 'qr_code';
                break;
            case 'delivery_package_record':
                $return_arr['tmsCode']       = 'logistics_code';
                $return_arr['tmsOrderCode']  = 'express_code';
                $return_arr['packageCode']   = 'package_code';
                $return_arr['packageWeight'] = 'weight';
                $return_arr['packageLength'] = 'length';
                $return_arr['packageWidth']  = 'width';
                $return_arr['packageHeight'] = 'height';
                $return_arr['packageVolume'] = 'volume';
                break;
            case 'delivery_package_material_record':
                $return_arr['materialType']     = 'type';
                $return_arr['materialQuantity'] = 'quantity';
                break;
            case 'inventory_check_record':
                $return_arr['storeCode'] = 'warehouse_code';
                $return_arr['responsibilityCode'] = 'check_order_code';
                $return_arr['checkOrderId'] = 'check_order_id';
                $return_arr['ownerUserId'] = 'customer_id';
                $return_arr['operateTime'] = 'check_time';
                $return_arr['outBizCode'] = 'out_biz_code';
                $return_arr['remark'] = 'remark';
                break;
            case 'inventory_product_check_record':
                $return_arr['itemCode'] = 'item_code'; //商品编码
                $return_arr['itemId'] = 'item_id';    //商品id
                $return_arr['inventoryType'] = 'inventory_type';
                $return_arr['quantity'] = 'quantity';
                $return_arr['batchCode'] = 'batch_code';
                $return_arr['produceDate'] = 'product_date';
                $return_arr['dueDate'] = 'expire_date';
                $return_arr['produceCode'] = 'produce_code';
                $return_arr['snCode'] = 'sn_code';
                $return_arr['remark'] = 'remark';
                break;
            default:
                break;
        }
        return $return_arr;
    }
}