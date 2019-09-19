<?php
/**
 * Notes:贝贝标准接口签名校验类 （beibei->oms）
 * Date: 2019/6/14
 * Time: 19:10
 */

class sign
{
    /***
     * Notes:签名校验
     * Date: 2019/6/14
     * Time: 20:42
     * @param $appSecret 贝贝给出的秘钥
     * @param $appSign 签名
     * @param $request 请求参数
     * @return bool
     */
    public function check($appSecret,$appSign,$request)
    {
        //校验签名
        unset($request['sign']);
        ksort($request);
        $str = $appSecret;
        foreach ($request as $key => $val)
        {
            if (in_array($key, beibei_service::$_systemParams)) {
                $str .= $key . $val;
            }
        }
        $str .= $appSecret;
        $sign = strtoupper(md5($str));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }

}