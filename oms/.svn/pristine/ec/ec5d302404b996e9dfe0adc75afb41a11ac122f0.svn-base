<?php
/**
 * Notes:唯品会标准接口签名校验类
 * Date: 2019/1/3
 * Time: 15:40
 */


class sign
{
    /***
     * Notes:校验
     * Date: 2019/1/24
     * Time: 11:10
     * @param $appSecret  秘钥
     * @param $appSign 签名
     * @param $data 请求报文
     * @return bool true 校验成功 false 校验失败
     */
    public function check($appSecret, $appSign, $data)
    {
        $sign = strtoupper(base64_encode(md5($appSecret . $data . $appSecret)));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }
}