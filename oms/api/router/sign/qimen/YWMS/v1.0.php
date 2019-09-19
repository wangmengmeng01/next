<?php
/**
 * YWMS系统奇门接口签名算法
 * 
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