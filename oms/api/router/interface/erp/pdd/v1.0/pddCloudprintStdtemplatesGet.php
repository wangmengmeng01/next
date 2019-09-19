<?php
/**
 * Notes:拼多多获取所有的标准电子面单模板接口
 * Date: 2019/3/28
 * Time: 16:28
 */

require API_ROOT . 'router/interface/erp/pdd/erpRequest.php';

class pddCloudprintStdtemplatesGet extends erpRequest
{
    /***
     * Notes:获取所有的标准电子面单模板接口处理方法
     * Date: 2019/3/22
     * Time: 13:36
     * @param $params
     * @return array
     */
    public function get($params)
    {
        try {
            if (empty($params)) {
                return $this->outputPdd(0, 'S003', '请求数据不能为空');
            }
            //获取access_token
            $tokenInfo = $this->getTokenForSearch($params);
            if (!array_key_exists('access_token', $tokenInfo)) {//返回数组有键为access_token就是返回正确信息
                return $tokenInfo;
            } else {
                $accessToken = $tokenInfo['access_token'];
            }
            $response = $this->send($accessToken);
            if (!$response['error_response']) {
                return $this->msgObj->outputPdd(1, '0000', $response['addon']['return_msg'], $response['addon']);
            } else {
                $code = $response['error_response']['error_code'];
                $msg = $response['error_response']['error_msg'];
                return $this->msgObj->outputPdd(4, $code, $msg, $response['addon']);
            }
        } catch (Exception $e) {
            $logName = date('Ymd') . '_pdd_execute_log.log';
            error_log(print_r($e->getMessage(), 1) . PHP_EOL, 3, LOG_PATH . $logName);
            return $this->msgObj->outputPdd(0,$e->getCode(),$e->getMessage());
        }
    }
}







