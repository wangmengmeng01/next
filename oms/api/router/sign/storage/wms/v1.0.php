<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/13 11:32
 * @description:  wms->菜鸟 签名验签
 */

class sign
{

    /**
     * 签名校验
     * @param $appSecret WMS系统中客户密码
     * @param $data body中的xml数据
     * @param $appSign 签名
     * @return bool
     */
    public function check($appSecret, $data, $appSign)
    {
        $sign = strtoupper(base64_encode(md5($appSecret . $data . $appSecret)));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }

}