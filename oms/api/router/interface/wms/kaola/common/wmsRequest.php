<?php
/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/5/8
 * Time: 17:02
 */
class wmsRequest
{
    public $msgObj = null;
    public $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }

    public function send ()
    {
        $reqParams = array(
            'notify_type'=> kaola_service::$_notifyType,
            'notify_id'  => kaola_service::$_notifyId,
            'stock_id'   => kaola_service::$_stockId,
            'owner_id'   => kaola_service::$_ownerId,
            'data'       => kaola_service::$_data
        );

        $httpObj = new httpclient();
        error_log(print_r(kaola_service::$_reqUrl.PHP_EOL.$reqParams,1),3,'/yd/oms/1.0.1/log/wmsRequest.log');
        $rsp = $httpObj->post(kaola_service::$_reqUrl, $reqParams);

        if (empty($rsp) || $rsp == '-3') {
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
}