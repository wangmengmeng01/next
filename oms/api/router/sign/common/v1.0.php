<?php
/**
 * 标准接口签名算法
 * User: 独孤羽<123517746@qq.com>
 * Date: 15-4-27 下午12:39
 */
class sign
{

    /**
     * 签名校验
     * @param $customerId 客户ID
     * @param $sign 请求方签名
     * @param $requestApiSecret 请求方签名密钥
     * @param $request 请求参数
     * @return bool
     */
    public function check($sign, $requestApiSecret, &$request)
    {
        $checkSign = urlencode(strtoupper(base64_encode(md5($requestApiSecret . base64_decode($request['data']) . $requestApiSecret))));
        if ($sign != $checkSign) {
            return false;
        } else {
            return true;
        }
    }

}