<?php
/**
 * Notes:
 * Date: 2019/1/7
 * Time: 11:08
 */
/**
 * 唯品会wms请求基类
 */
class wmsRequest
{


    public static $wmsBn = '';//wms接口编号
    public static $wmsApi = '';//wms接口地址
    public static $wmsApiSecret = '';//wms接口密钥
    public static $wmsApiVer = '';//wms接口密钥
    public $msgObj = null;
    public $utilObj = null;
    public $xmlObj = null;
    public function __construct()
    {
        $this->utilObj = new util();
        $this->xmlObj = new xml();
    }

    /**
     *
     * @param $method
     * @param $params
     */
    public function send($method, $params)
    {
        $xml = new xml();
        $xmlData = $this->utilObj->array2xml($params, $this->xmlObj);

        $apiParams = array(
            'method' => $method,
            'vendorid' => vip_service::$_vendorid,
            'warehouseid' => vip_service::$_warehouseid,
            'format' => 'xml',
            'sign' => strtoupper(base64_encode(md5(self::$wmsApiSecret.$xmlData.self::$wmsApiSecret))),
            'timestamp' => round($this->utilObj->currentTimeMillis()/1000),
            'data' => $xmlData
        );

        $httpObj = new httpclient();
//        $rs = $httpObj->post(self::$wmsApi, $apiParams);
        $rs = urlencode('<?xml version="1.0" encoding="utf-8"?>
<response>
<flag>success</flag>
<code>响应码</code>
<message>响应信息</message>
<orderId>仓储系统拣货单Id</orderId>
</response>');
        return urldecode($rs);
    }


}