<?php
/**
 * Notes:拼多多电子面单接口签名算法
 * Date: 2019/3/21
 * Time: 10:52
 */
class sign
{
    /**
     * 签名校验
     * @param $secret OMS系统中拼多多秘钥
     * @param $data body中的json数据
     * @param $appSign 签名
     * @return bool
     */
    public function check($secret, $data, $appSign)
    {
        $sign = strtoupper(base64_encode(md5($secret . $data . $secret)));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }
}
