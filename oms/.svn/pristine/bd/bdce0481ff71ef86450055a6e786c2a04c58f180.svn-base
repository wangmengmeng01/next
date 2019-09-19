<?php
/**
 * 菜鸟电子面单打印校验接口类
 * @author Renee
 * 
 */
require API_ROOT . '/router/interface/erp/cainiao/cainiaoRequest.php';
class wlbIPrint extends cainiaoRequest{
    public function printFilter($params) {
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
            $productType = $tokenInfo['product_type'];
            $shipProv = $tokenInfo['ship_addr_info']['ship_prov'];
            $shipCity = $tokenInfo['ship_addr_info']['ship_city'];
            $shipCounty = $tokenInfo['ship_addr_info']['ship_county'];
            $shipTown = $tokenInfo['ship_addr_info']['ship_town'];
            $shipDetailAddress = $tokenInfo['ship_addr_info']['ship_detail_address'];
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
			try {
				$req = new WlbWaybillIPrintRequest;
				$waybill_apply_print_check_request = new WaybillApplyPrintCheckRequest;
				if (!empty($params['print_check_info_cols'])) {
					if (empty($params['print_check_info_cols'][0])) {
						$params['print_check_info_cols'] = array($params['print_check_info_cols']);
					}
					foreach ($params['print_check_info_cols'] as $check_key=>$check_val) {
						$print_check_info_cols = 'print_check_info_' . $check_key;
						$print_check_info_cols = new PrintCheckInfo;
						if (!empty($check_val['shipping_branch_code'])) {
							$print_check_info_cols->shipping_branch_code = $check_val['shipping_branch_code'];
						}
						$print_check_info_cols->consignee_name = $check_val['consignee_name'];
						if (!empty($check_val['send_phone'])) {
							$print_check_info_cols->send_phone = $check_val['send_phone'];
						}
						if (!empty($check_val['weight'])) {
							$print_check_info_cols->weight = $check_val['weight'];
						}
						$print_check_info_cols->waybill_code = $check_val['waybill_code'];
					
						$consignee_address = new WaybillAddress;
						if (!empty($check_val['consignee_address']['area'])) {
							$consignee_address->area = $check_val['consignee_address']['area'];
						}
						if (!empty($check_val['consignee_address']['division_id'])) {
							$consignee_address->division_id = $check_val['consignee_address']['division_id'];
						}
						if (!empty($check_val['consignee_address']['town'])) {
							$consignee_address->town = $check_val['consignee_address']['town'];
						}
						if (!empty($check_val['consignee_address']['city'])) {
							$consignee_address->city = $check_val['consignee_address']['city'];
						}
						$consignee_address->province = $check_val['consignee_address']['province'];
						$consignee_address->address_detail = $check_val['consignee_address']['address_detail'];
						$print_check_info_cols->consignee_address = $consignee_address;
					
						if (!empty($productType)) {
							$print_check_info_cols->product_type = $productType;
						}
						if (!empty($check_val['send_name'])) {
							$print_check_info_cols->send_name = $check_val['send_name'];
						}
						if (!empty($check_val['consignee_branch_code'])) {
							$print_check_info_cols->consignee_branch_code = $check_val['consignee_branch_code'];
						}
					
						if (!empty($check_val['logistics_service_list'])) {
							if (empty($check_val['logistics_service_list'][0])) {
								$check_val['logistics_service_list'] = array($check_val['logistics_service_list']);
							}
							foreach ($check_val['logistics_service_list'] as $list_key=>$list_val) {
								$logistics_service_list = 'logistics_service_list_' . $list_key;
								$logistics_service_list = new LogisticsService;
								$logistics_service_list->service_value4_json = $list_val['service_value4_json'];
								$logistics_service_list->service_code = $list_val['service_code'];
								$serviceListArr[] = $logistics_service_list;
							}
							$print_check_info_cols->LogisticsServiceList = $serviceListArr;
						}
					
						if (!empty($check_val['consignee_branch_name'])) {
							$print_check_info_cols->consignee_branch_name = $check_val['consignee_branch_name'];
						}
						if (!empty($check_val['shipping_branch_name'])) {
							$print_check_info_cols->shipping_branch_name = $check_val['shipping_branch_name'];
						}
						if (!empty($check_val['short_address'])) {
							$print_check_info_cols->short_address = $check_val['short_address'];
						}
						if (!empty($check_val['volume'])) {
							$print_check_info_cols->volume = $check_val['volume'];
						}
						$print_check_info_cols->consignee_phone = $check_val['consignee_phone'];
					
						$shipping_address = new WaybillAddress;
						if (cainiao_service::$_appKey != 'RYD') {
							$shipping_address->area = $shipCounty;
							$shipping_address->division_id = $check_val['shipping_address']['division_id'];
							$shipping_address->province = $shipProv;
							$shipping_address->town = $shipTown;
							$shipping_address->address_detail = $shipDetailAddress;
							$shipping_address->city = $shipCity;
						} else {
							$shipping_address->area = $check_val['shipping_address']['area'];
							$shipping_address->division_id = $check_val['shipping_address']['division_id'];
							$shipping_address->province = $check_val['shipping_address']['province'];
							$shipping_address->town = $check_val['shipping_address']['town'];
							$shipping_address->address_detail = $check_val['shipping_address']['address_detail'];
							$shipping_address->city = $check_val['shipping_address']['city'];
						}
						$print_check_info_cols->shipping_address = $shipping_address;
					
						if (!empty($sellerId)) {
							$print_check_info_cols->real_user_id = $sellerId;
						}
						if (!empty($check_val['package_center_code'])) {
							$print_check_info_cols->package_center_code = $check_val['package_center_code'];
						}
						if (!empty($check_val['package_center_name'])) {
							$print_check_info_cols->package_center_name = $check_val['package_center_name'];
						}
						if (!empty($check_val['print_config'])) {
							$print_check_info_cols->print_config = $check_val['print_config'];
						}
						$printCheckInfoArr[] = $print_check_info_cols;
					}
				}
				$waybill_apply_print_check_request->print_check_info_cols = $printCheckInfoArr;
				$waybill_apply_print_check_request->cp_code = $params['cp_code'];
				$req->setWaybillApplyPrintCheckRequest(json_encode($waybill_apply_print_check_request));
				cainiao_service::$_requstCainiaoTime = date("Y-m-d H:i:s");//记录请求菜鸟的时间
				$resp = $c->execute($req, $sessionKey);
				cainiao_service::$_responseOmsTime = date("Y-m-d H:i:s");//记录菜鸟响应OMS的时间
            } catch(Exception $e) {
				$logName = date('Ymd') . '_cainiao_execute_log.log';
                error_log(print_r($e->getMessage(),1).PHP_EOL, 3, LOG_PATH.$logName);
			}
            if (!empty($resp)) {
                $xmlObj = new xml();
                $respArr = $this->xmlToArr($resp);//将SimpleXML object格式数组转为普通数组
                cainiao_service::$_cnReturnStr = json_encode($respArr);
                if (!array_key_exists('error_response', $respArr)) {
                    $xmlStr = '<?xml version="1.0" encoding="utf-8"?><wlb_waybill_i_print_response>';
                    $checkInfoStr = '<waybill_apply_print_check_infos>';
                    if (!empty($respArr['wlb_waybill_i_print_response']['waybill_apply_print_check_infos']['waybill_apply_print_check_info'])) {
                        if (empty($respArr['wlb_waybill_i_print_response']['waybill_apply_print_check_infos']['waybill_apply_print_check_info'][0])) {
                            $respArr['wlb_waybill_i_print_response']['waybill_apply_print_check_infos']['waybill_apply_print_check_info'] = array($respArr['wlb_waybill_i_print_response']['waybill_apply_print_check_infos']['waybill_apply_print_check_info']);
                        }
                        foreach ($respArr['wlb_waybill_i_print_response']['waybill_apply_print_check_infos']['waybill_apply_print_check_info'] as $val) {
                            $checkInfoStr .= '<waybill_apply_print_check_info>';
                            $checkInfoStr .= $xmlObj->array2xml($val);
                            $checkInfoStr .= '</waybill_apply_print_check_info>';
                        }
                    }
                    $checkInfoStr .= '</waybill_apply_print_check_infos>';
                    $xmlStr .= $checkInfoStr;
                    $xmlStr .= '</wlb_waybill_i_print_response>';
                    $logTxt['return_msg'] = $xmlStr;
                    return $this->msgObj->outputCainiao(1, '0000', $xmlStr, $logTxt);
                } else {//菜鸟返回失败消息
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