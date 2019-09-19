<?php
/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/5/9
 * Time: 16:11
 */
class erpRequest
{
    public  $msgObj = null;
    public  $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }

    public function send ()
    {
        $reqParams = array(
            'notify_type'=> kaola_service::$_notifyType,
            'notify_time'=> date('Y-m-d H:i:s'),
            'notify_id'  => kaola_service::$_notifyId,
            'wms_id'     => KAOLA_WMS_ID,
            'stock_id'   => kaola_service::$_stockId,
            'owner_id'   => kaola_service::$_ownerId,
            'data'       => kaola_service::$_data
        );

        $reqParams['sign'] = $this->makeSign(kaola_service::$_data);

        $httpObj = new httpclient();
        $rsp = $httpObj->post(kaola_service::$_reqUrl, $reqParams);

        if (empty($rsp) || $rsp == '-3' || $rsp == NULL) {
            $rsArr = array(
                'success'   => false,
                'error_msg' => '请求超时！',
            );
            $rsp = json_encode($rsArr);
        } else {
            $rsArr = json_decode($rsp,true);
        }
        kaola_service::$_rsMsg = $rsp;

        return $rsArr;
    }

    public function makeSign ($data)
    {
        $priContent = file_get_contents("/yd/oms/1.0.1/api/config/rsa_private_key.pem");

        $prikeyid = openssl_pkey_get_private($priContent);

        openssl_sign($data,$sign,$prikeyid,OPENSSL_ALGO_SHA1);

        openssl_free_key($prikeyid);

        return bin2hex($sign);
    }
}