<?php
/**
 * Notes:
 * Date: 2019/1/7
 * Time: 11:08
 */
/**
 * 唯品会erp请求基类
 */
class erpRequest
{
    public function send($params)
    {
        $reqParams = array(
            'service'  => 'vipapis.delivery.JitDeliveryService',
            'method'   => $params['method'],
            'version'  => '1.0.0',
            'timestamp'=> round($this->currentTimeMillis()/1000),
            'format'   => 'json',
            'appKey'   => VIP_APPKEY
        );

        ksort($reqParams);
        $signStr = '';
        foreach ($reqParams as $key => $val)
        {
            $signStr .= $key . $val;
        }
        $signStr .= $params['data'];
        $sign = strtoupper(hash_hmac('md5',$signStr,VIP_APPSECRET));

        $reqParams['sign'] = $sign;
        $reqUrl = VIP_APPURL.'?'.$this->getQueryString($reqParams);

        $utilObj = new util();
        $resp = $utilObj->post_data_json($reqUrl,$params['data'],60);
        return $resp;
    }

    /**
     * 组装请求参数
     * @param unknown $request
     * @return string
     */
    public function getQueryString($request){
        $params = '';
        $params = $params . "appKey=" . $request["appKey"];
        $params = $params . "&format=" . $request["format"];
        $params = $params . "&method=" . $request["method"];
        $params = $params . "&service=" . $request["service"];
        $params = $params . "&sign=" . $request["sign"];
        $params = $params . "&timestamp=" . $request["timestamp"];
        $params = $params . "&version=" . $request["version"];
        if(!empty($request["accessToken"])){
            $params = $params . "&accessToken=" . $request["accessToken"];
        }
        if(!empty($request["language"])){
            $params = $params . "&language=" . $request["language"];
        }

        return $params;
    }

    protected function currentTimeMillis()
    {
        list($t1, $t2) = explode(' ', microtime());
        return (float) (floatval($t1) + floatval($t2)) * 1000;
    }
}