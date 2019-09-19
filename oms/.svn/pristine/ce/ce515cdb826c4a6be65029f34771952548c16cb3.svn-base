<?php
/**
 * Notes:唯品会 创建拣货单、获取捡货单列表、获取拣货单明细
 * Date: 2018/12/18
 * Time: 13:27
 */

class VipPickCommand extends CConsoleCommand
{

    /***
     * Notes:生成签名
     * Date: 2019/1/4
     * Time: 17:02
     * @param $appSecret   请求秘钥
     * @param $request     请求参数
     * @return string      签名
     */
//    public function sign($appSecret, $request)
//    {
//        //生成签名
//        ksort($request);
//        $str = $appSecret;
//        foreach ($request as $key => $val)
//        {
//            $str .= $key . $val;
//        }
//        $str .= $appSecret;
//        $sign = strtoupper(md5($str));
//        return $sign;
//    }
    /***
     * Notes:调用接口
     * Date: 2018/12/18
     * Time: 18:04
     */
    public function actionCreate()
    {
        $filePath = dirname(realpath(__FILE__));
        $root_dir = substr($filePath,0,strpos($filePath, '\protected\commands'));
//        $appSecret = VIP_APPSECRET;
//        $service = VIP_SERVICE;
        $method = VIP_CREATEPICK;
//        $version = VIP_VERSION;
//        $timestamp = date('Y-m-d H:i:s');
//        $format = VIP_FORMAT;
//        $appKey = VIP_APPKEY;
//        $request = array(
//            'service' => $service,
//            'method' => $method,
//            'version' => $version,
//            'timestamp' => $timestamp,
//            'format' => $format,
//            'appKey' => $appKey,
//        );
//        $sign = $this->sign($appSecret,$request);
//        $request['sign'] = $sign;
        require_once $root_dir.'/vip_api.php';
        echo 'OK';
    }
}