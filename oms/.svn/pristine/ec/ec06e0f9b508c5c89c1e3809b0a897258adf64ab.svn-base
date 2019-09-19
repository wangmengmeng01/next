<?php
/**
 * Created by PhpStorm.
 * User: 20171012
 * Date: 2018/1/17
 * Time: 11:04
 */
class wmsRequest {
    public static $userId = ''; //客户ID
    public static $wmsApiUrl = ''; //wms接口地址
    public static $wmsApiSecret = '';//wms接口密钥
    public $msgObj = null;
    public $utilObj = null;
    public $funcObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }

    /**
     * 转发数据给WMS
     */
    public function send()
    {
        $apiParams = array(
            'msgType' => custom_service::$_msgtype,
            'userId'  => custom_service::$_userid,
            'sign'    => md5(self::$wmsApiSecret . custom_service::$_msg . self::$wmsApiSecret),
            'msg'     => custom_service::$_msg
        );

        //推送数据到wms接口
        $httpObj = new httpclient();
        $rs = $httpObj->post(self::$wmsApiUrl, $apiParams);

        //接口层记录日志
        $logExt = array(
            'api_url' => self::$wmsApiUrl,
            'api_method' => custom_service::$_msgtype,
            'api_params' => $apiParams
        );

        if($rs == '' || $rs == '-3'){
            $msg = '请求超时';
            $success = 'false';
            $logExt['return_msg'] = $msg;
        }else{
            $xmlObj = new xml();
            $response = $xmlObj->xmlStr2array($rs);
            $response = $this->utilObj->filter_null($response);
            $success = $response['success'];

            //cnec_wh_7
            if (empty($response[0]) && custom_service::$_msgtype=='cnec_wh_7') {
                $success = 'true';
            }
            //部分接口响应报文无reasons字段
            if (empty($response['reasons']) && $success == 'false') {
                $msg = '失败【S005】';
            } else {
                $msg = $response['reasons'];
            }
            $logExt['return_msg'] = $rs;
        }
        return $this->msgObj->outputCustom($success, $msg, $logExt);
    }

    public function getDbRelation($msgType){
        $returnArr = array();
        if ($msgType == 'cnec_wh_6') {
            $returnArr['storer']         = 'customer_id';
            $returnArr['wmwhseid']       = 'warehouse_code';
            $returnArr['externalNo']     = 'delivery_order_code';
            $returnArr['externalNo2']    = 'external_no2';
            $returnArr['shipToName']     = 'sender_name';
            $returnArr['shipToPhone']    = 'sender_tel';
            $returnArr['userName']       = 'receiver_name';
            $returnArr['billDate']       = 'place_order_time';
            $returnArr['paymentDateTime']= 'pay_time';
            $returnArr['receipType']     = 'order_type';
            $returnArr['provinceName']   = 'receiver_province';
            $returnArr['cityName']       = 'receiver_city';
            $returnArr['regionName']     = 'receiver_area';
            $returnArr['shipToAddr']     = 'receiver_detail_address';
            $returnArr['shipToPassCode'] = 'ship_to_pass_code';
            $returnArr['zipCode']        = 'receiver_zipcode';
            $returnArr['dsPlatform']     = 'source_platform_code';
            $returnArr['dsStorer']       = 'shop_nick';
            $returnArr['carrierKey']     = 'logistics_code';
            $returnArr['expressID']      = 'express_code';
            $returnArr['expressType']    = 'express_type';
            $returnArr['payment']        = 'got_amount';
            $returnArr['flag']           = 'flag';
            $returnArr['default01']      = 'default01';
            $returnArr['tdq']            = 'tdq';
            $returnArr['order_flag']     = 'order_flag';
        } elseif ($msgType == 'cnec_wh_6_item') {
            $returnArr['delivery_id']= 'delivery_id';
            $returnArr['sku']        = 'item_code';
            $returnArr['uom']        = 'uom';
            $returnArr['taxPrice']   = 'actual_price';
            $returnArr['expectedQty']= 'plan_qty';
        } elseif ($msgType == 'cnec_wh_1') {
            $returnArr['storer']      = 'storer';
            $returnArr['wmwhseid']    = 'wmwhseid';
            $returnArr['company']     = 'company';
            $returnArr['desce']       = 'desce';
            $returnArr['address1']    = 'address1';
            $returnArr['address2']    = 'address2';
            $returnArr['city']        = 'city';
            $returnArr['province']    = 'province';
            $returnArr['postcode']    = 'postcode';
            $returnArr['contact']     = 'contact';
            $returnArr['contactPhone']= 'contact_phone';
            $returnArr['contactFax']  = 'contact_fax';
            $returnArr['notes']       = 'notes';
        }
        return $returnArr;
    }
}