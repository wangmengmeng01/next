<?php
/**
 * 获取所有的菜鸟标准电子面单模板接口类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/cainiao/cnRequest.php';
class CnCloudPrintStdtemplatesGet extends cnRequest{
    public function getTemplates($params) {
        //获取sessionKey
        if (cainiao_service::$_appKey == 'RYD') {//判断订单来源于 RYD or YWMS
            $tokenInfo = $this->getTokenBySellerId($params);
        } else {
            $tokenInfo = $this->getTokenByCustomerCode($params);
        }
        if (!array_key_exists('access_token', $tokenInfo)) {//返回数组有键为access_token就是返回正确信息
            cainiao_service::$_innerErrorFlag = true;
            return $tokenInfo;
        } else {
            $sessionKey = $tokenInfo['access_token'];
        }
        
        $c = new TopClient;
        $c->appkey = CAINIAO_APP_KEY;
        $c->secretKey = CAINIAO_APP_SECRET;
        $apiParams = array(
            'method' => cainiao_service::$_method,
            'app_key' => $c->appkey,
            'session' => $sessionKey
        );
        $logTxt = array(
            'api_url' => $c->gatewayUrl,
            'api_method' => cainiao_service::$_method,
            'api_params' => $apiParams
        );
        
        if (!empty($params)) {
            try {
                $req = new CainiaoCloudprintStdtemplatesGetRequest;
                cainiao_service::$_requstCainiaoTime = date("Y-m-d H:i:s");//记录请求菜鸟的时间
                $resp = $c->execute($req, $sessionKey);
                cainiao_service::$_responseOmsTime = date("Y-m-d H:i:s");//记录菜鸟响应OMS的时间
            }catch(Exception $e){
                $logName = date('Ymd') . '_cainiao_execute_log.log';
                error_log(print_r($e->getMessage(),1).PHP_EOL, 3, LOG_PATH.$logName);
            }
            
            if (!empty($resp)) {
                $xmlObj = new xml();
                $respArr = $this->xmlToArr($resp);//将SimpleXML object格式数组转为普通数组
                cainiao_service::$_cnReturnStr = json_encode($respArr);
                
                if (!array_key_exists('error_response', $respArr)) {
                    if ($resp['cainiao_cloudprint_stdtemplates_get_response']['result']['success'] == 'true') {
                        $xmlStr = '<cainiao_cloudprint_stdtemplates_get_response><result>';
                        if (!empty($resp['cainiao_cloudprint_stdtemplates_get_response']['result']['datas']['standard_template_result'])) {
                            $dataStr = '<datas>';
                            $standard_template_result = 
                                $resp['cainiao_cloudprint_stdtemplates_get_response']['result']['datas']['standard_template_result'];
                            if (empty($standard_template_result[0])) {
                                $standard_template_result = array($standard_template_result);
                            }
                            $standardStr = '';
                            foreach ($standard_template_result as $standard_v) {
                                $standardStr .= '<standard_template_result>';
                                $templatesStr = '<standard_templates>';
                                $templates = '';
                                foreach ($standard_v['standard_templates']['standard_template_do'] as $templates_v) {
                                    $templates .= '<standard_template_do>';
                                    $templates .= $xmlObj->array2xml($templates_v);
                                    $templates .= '</standard_template_do>';
                                }
                                $templatesStr .= $templates;
                                $templatesStr .= '</standard_templates>';
                                unset($standard_v['standard_templates']);

                                $standardStr .= $templatesStr;
                                $standardStr .= $xmlObj->array2xml($standard_v);
                                $standardStr .= '</standard_template_result>';
                            }
                            $dataStr .= $standardStr;
                            $dataStr .= '</datas>';
                            unset($resp['cainiao_cloudprint_stdtemplates_get_response']['result']['datas']);
                            
                            $xmlStr .= $dataStr;
                            $xmlStr .= $xmlObj->array2xml($resp['cainiao_cloudprint_stdtemplates_get_response']['result']);
                            $xmlStr .= '</result></cainiao_cloudprint_stdtemplates_get_response>';
                            $logTxt['return_msg'] = $xmlStr;
                            return $this->msgObj->outputCainiao(1, '0000', $xmlStr, $logTxt);
                        }
                    } else {
                        $logTxt['return_msg'] = $xmlObj->array2xml($respArr);
                        return $this->msgObj->outputCainiao(4, $code, $msg, $logTxt);
                    }
                } else {
                    $code = $respArr['error_response']['code'];
                    $msg = $respArr['error_response']['msg'];
                    $logTxt['return_msg'] = $xmlObj->array2xml($respArr);
                    return $this->msgObj->outputCainiao(4, $code, $msg, $logTxt);
                }
            } else {
                
            }
        } else {
            return $this->msgObj->outputCainiao(0, 'S003', '请求数据为空', $logTxt);
        }
        
    }
}
?>