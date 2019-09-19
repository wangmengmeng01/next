<?php
/**
 * 奇门标准接口签名校验类
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
    public function check($appSecret, $data, $appSign, $request)
    {
    	//校验签名
    	unset($request['sign']);
    	ksort($request);
    	$str = $appSecret;
    	foreach ($request as $key => $val)
    	{
    		if (in_array($key, qimen_service::$_systemParams)) {
    			$str .= $key . $val;
    		}		
    	}
    	$str .= $data . $appSecret;
    	$sign = strtoupper(md5($str));
        if ($sign != $appSign) {
            return false;
        } else {
            return true;
        }
    }

}