<?php
class cainiao_wms_base {
    /*
     * 检查加密
     */
    public function checkSign($secret, $data, $appSign) {

        $sign = base64_encode(md5($data.$secret, true));

        return $sign != $appSign ? false : true;
    }

    public function curl_post ($url, $body, array $options = array())
    {
        $defaults = array(
            CURLOPT_SSL_VERIFYHOST => false,
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_POST           => 1,
            CURLOPT_HEADER         => 0,
            CURLOPT_URL            => $url,
            CURLOPT_FRESH_CONNECT  => 1,
            CURLOPT_RETURNTRANSFER => 1,
            CURLOPT_FORBID_REUSE   => 1,
            CURLOPT_TIMEOUT        => 30,
            CURLOPT_POSTFIELDS     => http_build_query($body)
        );

        $ch = curl_init();

        curl_setopt_array($ch, ($options + $defaults));
        if ( ! $result = curl_exec($ch)) {
            return $this->sendError('S006' , curl_error($ch));
        }
        curl_close($ch);

        $result = json_encode(simplexml_load_string($result));//将对象转换个JSON

        return json_decode($result , true);
    }

    public function sendError ($errorCode = '',$errorMsg = '')
    {
        return "<response>
                  <flag>failure</flag>
                  <code>{$errorCode}</code>
                  <message>{$errorMsg}</message>
                </response>";
    }

    public function sendSucc($code = '000',$msg = '成功')
    {
        return "<response>
                  <flag>success</flag>
                  <code>{$code}</code>
                  <message>{$msg}</message>
                </response>";
    }

    public function sign($app_sectet , $data)
    {
        return base64_encode(md5($data.$app_sectet, true));
    }

}