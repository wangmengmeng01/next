<?php
/**
 * YWMS系统菜鸟电子面单接口签名算法
 * 
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
        $sign = strtoupper(base64_encode(md5($secret . $data . $secret)));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }

}
?>