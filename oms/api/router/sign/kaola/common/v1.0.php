<?php
/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/5/10
 * Time: 9:15
 */
class sign
{
    /**
     * 网易签名验证
     *
     * @param string $sign       接收的签名数据(16进制)
     * @param string $data       接受的数据信息
     * @param static $public_key 公钥信息(16进制)
     *
     * @return bool true/false
     */
    public function check($sign, $data, $public_key)
    {
        $hexStr = $this->hex2binnn($public_key);
        $public_key = base64_encode($hexStr);
        //$public_key = base64_encode(hex2bin($public_key));
        $pubkeyid = openssl_pkey_get_public("-----BEGIN PUBLIC KEY-----\n" . $this->yd_check_split($public_key, 64) . "\n-----END PUBLIC KEY-----");

        $binSign = $this->hex2binnn($sign);
        return (bool) openssl_verify($data, $binSign, $pubkeyid);
    }

    public function yd_check_split($str, $len = 76, $end = "\n") {
        $pattern = '~.{1,' . $len . '}~u';
        $str = preg_replace($pattern, '$0' . $end, $str);

        return rtrim($str, $end);
    }

    //十六进制转二进制
    public function hex2binnn($h){
        if (!is_string($h)) return null;
        $r='';
        for ($a=0; $a<strlen($h); $a+=2) { $r.=chr(hexdec($h{$a}.$h{($a+1)})); }
        return $r;
    }
}