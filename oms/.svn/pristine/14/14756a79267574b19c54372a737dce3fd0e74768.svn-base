<?php
/**
 * Notes:YWMS系统贝贝接口签名算法 （wms->oms）
 * Date: 2019/6/14
 * Time: 19:02
 */
class sign
{
    /***
     * Notes:签名校验
     * Date: 2019/6/14
     * Time: 20:40
     * @param $appSecretMS系统中秘钥
     * @param $appSign 签名
     * @param $data   请求报文
     * @return bool
     */
    public function check($appSecret,$appSign,$data)
    {
        $sign = strtoupper(base64_encode(md5($appSecret . $data . $appSecret)));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }

}