<?php
/**
 * 菜鸟电子面单取消获取的电子面单接口类
 * @author Renee
 * 
 */
require API_ROOT . '/router/interface/erp/cainiao/cainiaoRequest.php';
class wlbICancel extends cainiaoRequest{
    public function cancel($params) {
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
            $sellerId = $tokenInfo['seller_id'];
        }
        
        //for recording log
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
            /*
            $c = new TopClient;
            $sessionKey = '6102907ea9e255d1bf8e6df1ab639c07372b0fe633533ee2054718218';
            $c->appkey = '1023229135';
            $c->secretKey = 'sandbox70e25bb970a1ffd3d4f411e1a';
            */
			try {
				$req = new WlbWaybillICancelRequest;
				$waybill_apply_cancel_request = new WaybillApplyCancelRequest;
				if (!empty($sellerId)) {
					$waybill_apply_cancel_request->real_user_id = $sellerId;
				}
				
				if (is_array($params['trade_order_list'])) {
					foreach ($params['trade_order_list'] as $t_v) {
						$tradeOrderList[] = $t_v;
					}
				} else {
					$tradeOrderList = $params['trade_order_list'];
				}
				$waybill_apply_cancel_request->trade_order_list = $tradeOrderList;
				
				$waybill_apply_cancel_request->cp_code = $params['cp_code'];
				$waybill_apply_cancel_request->waybill_code = $params['waybill_code'];
				if (!empty($params['package_id'])) {
					$waybill_apply_cancel_request->package_id = $params['package_id'];
				}
				$req->setWaybillApplyCancelRequest(json_encode($waybill_apply_cancel_request));
				cainiao_service::$_requstCainiaoTime = date("Y-m-d H:i:s");//记录请求菜鸟的时间
				$resp = $c->execute($req, $sessionKey);
				cainiao_service::$_responseOmsTime = date("Y-m-d H:i:s");//记录菜鸟响应OMS的时间
            } catch (Exception $e) {
				$logName = date('Ymd') . '_cainiao_execute_log.log';
                error_log(print_r($e->getMessage(),1).PHP_EOL, 3, LOG_PATH.$logName);
			}
			
            if (!empty($resp)) {
                $xmlObj = new xml();
                $respArr = $this->xmlToArr($resp);//将SimpleXML object格式数组转为普通数组
                cainiao_service::$_cnReturnStr = json_encode($respArr);
                if (!array_key_exists('error_response', $respArr)) {
                    $cancelResult = $respArr['wlb_waybill_i_cancel_response']['cancel_result'];
                    $xmlStr = '<?xml version="1.0" encoding="utf-8"?><wlb_waybill_i_cancel_response><cancel_result>';
                    $xmlStr .= $cancelResult ;
                    $xmlStr .= '</cancel_result></wlb_waybill_i_cancel_response>';
                    
                    $logTxt['return_msg'] = $xmlStr;
                    return $this->msgObj->outputCainiao(1, '0000', $xmlStr, $logTxt);
                } else {
                    $code = $respArr['error_response']['code'];
                    $msg = $respArr['error_response']['msg'];
                    $logTxt['return_msg'] = $xmlObj->array2xml($respArr);
                    return $this->msgObj->outputCainiao(4, $code, $msg, $logTxt);
                }
            } else {
                return $this->msgObj->outputCainiao(0, 'S003', '请求超时', $logTxt);
            }
        } else {
            return $this->msgObj->outputCainiao(0, 'S003', '请求数据为空', $logTxt);
        }
        
    }
}


?>