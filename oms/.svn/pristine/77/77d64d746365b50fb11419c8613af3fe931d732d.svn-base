<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/13 11:31
 * @description:  菜鸟->wms 签名方式
 */

class sign
{

    /**
     * 签名校验
     * @param $secret WMS系统中客户密码
     * @param $data body中的xml数据
     * @param $appSign 签名
     * @return bool
     */
    public function check($secret, $data, $appSign)
    {
        $sign = base64_encode(md5( $data . $secret,true));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }

}