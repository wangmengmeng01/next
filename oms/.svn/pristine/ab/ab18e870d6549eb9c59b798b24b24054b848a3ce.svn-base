<?php
/**
 * 签名类
 * User: Renee
 * Date: 2018/1/4
 * Time: 9:39
 */
class sign
{

    /**
     * 签名校验
     * @param $appKey WMS系统APP
     * @param $appSecret WMS系统中客户密码
     * @param $data body中的xml数据
     * @param $appSign 签名
     * @param $request 请求参数
     * @return bool
     */
    public function check($userId, $secret, $appSign)
    {
        //校验签名
        $sign = md5($userId.$secret);
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }
}