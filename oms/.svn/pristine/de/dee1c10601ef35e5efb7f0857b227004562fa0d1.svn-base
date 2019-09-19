<?php
/**
 * Created by PhpStorm.
 * User: 20171012
 * Date: 2019/1/7
 * Time: 19:26
 */
require_once API_ROOT . 'router/interface/erp/vip/erpRequest.php';
class VipImpl extends erpRequest {

    private static $service;

    function __construct()
    {
        //self::$service = \vipapis\delivery\JitDeliveryServiceClient::getService();
    }

    public function getPoList($jsonData)
    {
        try{
            /*$service = \vipapis\delivery\JitDeliveryServiceClient::getService();

            $ctx = \Osp\Context\InvocationContextFactory::getInstance();
            $ctx->setAppKey(VIP_APPKEY);
            $ctx->setAppSecret(VIP_APPSECRET);
            $ctx->setAppURL(VIP_APPURL);

            $dataArr = json_decode($jsonData,1);
            $vendorId = $dataArr['vendor_id'];
            $sTime = $dataArr['st_sell_st_time'];
            $eTime = $dataArr['et_sell_st_time'];
            $page = $dataArr['page'];
            $limit = $dataArr['limit'];

            echo $rs =  $service->getPoList('', '', '', '', '', $vendorId, $sTime, $eTime, $page, $limit,  '', '', '', '');die;*/

            $params = array(
                'method' => 'getPoList',
                'data'   => $jsonData
            );

            $rs = $this->send($params);
            return $rs;
        }catch (\Exception $e){
            $e->getMessage();
        }
    }
}