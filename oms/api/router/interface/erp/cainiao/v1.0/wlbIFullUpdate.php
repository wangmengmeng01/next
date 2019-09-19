<?php
/**
 * 菜鸟电子面单获取电子面单号接口类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/cainiao/cainiaoRequest.php';
class wlbIFullUpdate extends cainiaoRequest{
    public function update($params) {
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
				$req = new WlbWaybillIFullupdateRequest;
				$waybill_apply_full_update_request = new WaybillApplyFullUpdateRequest;
				if (!empty($params['send_phone'])) {
					$waybill_apply_full_update_request->send_phone = $params['send_phone'];
				}
				$waybill_apply_full_update_request->consignee_name = $params['consignee_name'];
				if (!empty($params['weight'])) {
					$waybill_apply_full_update_request->weight = $params['weight'];
				}
				if (!empty($params['send_name'])) {
					$waybill_apply_full_update_request->send_name = $params['send_name'];
				}
				if (!empty($params['trade_order_list'])) {
					if (empty($params['trade_order_list'][0])) {
						$params['trade_order_list'] = array($params['trade_order_list']);
					}
					foreach ($params['trade_order_list'] as $t_o_v) {
						$tradeOrderList[] =  $t_o_v;
					}
				}
				$waybill_apply_full_update_request->trade_order_list = $tradeOrderList;
				
				$waybill_apply_full_update_request->cp_code = $params['cp_code'];
				$waybill_apply_full_update_request->waybill_code = $params['waybill_code'];
				$waybill_apply_full_update_request->product_type = $productType;
				$waybill_apply_full_update_request->order_channels_type = $params['order_channels_type'];
				$waybill_apply_full_update_request->real_user_id = $sellerId;
				if (!empty($params['volume'])) {
					$waybill_apply_full_update_request->volume = $params['volume'];
				}
				
				if (!empty($params['package_items'])) {
					if (empty($params['package_items'][0])) {
						$params['package_items'] = array($params['package_items']);
					}
					foreach ($params['package_items'] as $pack_k=>$pack_v) {
						$package_items = 'package_item_' . $pack_k;
						$package_items = new PackageItem;
						$package_items->item_name = $pack_v['item_name'];
						$package_items->count = $pack_v['count'];
						$pack[] = $package_items;
					}
				}
				$waybill_apply_full_update_request->package_items = $pack;
				
				if (!empty($params['logistics_service_list'])) {
					if (empty($params['logistics_service_list'][0])) {
						$params['logistics_service_list'] = array($params['logistics_service_list']);
					}
					foreach ($params['logistics_service_list'] as $list_k=>$list_v) {
						$logistics_service_list = 'logistics_service_list_'.$list_k;
						$logistics_service_list = new LogisticsService;
						$logistics_service_list->service_value4_json = $list_v['service_value4_json'];
						$logistics_service_list->service_code = $list_v['service_code'];
						$serviceArr[] = $logistics_service_list;
					}
					$waybill_apply_full_update_request->LogisticsServiceList = $serviceArr;
				}
				
				$consignee_address = new WaybillAddress;
				if (!empty($params['consignee_address']['area'])) {
					$consignee_address->area = $params['consignee_address']['area'];
				}
				$consignee_address->province = $params['consignee_address']['province'];
				if (!empty($params['consignee_address']['town'])) {
					$consignee_address->town = $params['consignee_address']['town'];
				}
				$consignee_address->address_detail = $params['consignee_address']['address_detail'];
				if (!empty($params['consignee_address']['city'])) {
					$consignee_address->city = $params['consignee_address']['city'];
				}
				$waybill_apply_full_update_request->consignee_address = $consignee_address;
				
				$waybill_apply_full_update_request->consignee_phone = $params['consignee_phone'];
				if (!empty($params['package_id'])) {
					$waybill_apply_full_update_request->package_id = $params['package_id'];
				}
				$req->setWaybillApplyFullUpdateRequest(json_encode($waybill_apply_full_update_request));
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
                    $xmlStr = '<?xml version="1.0" encoding="utf-8"?><wlb_waybill_i_fullupdate_response><waybill_apply_update_info>';
                    
                    $tradeOrderInfoStr = '<trade_order_info>';
                    
                    if (!empty($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['package_items'])) {
                        if (empty($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['package_items']['package_item'][0])) {
                            $respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['package_items']['package_item'] = array($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['package_items']['package_item']);
                        }
                        $packageItemsStr = '<package_items>';
                        foreach ($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['package_items']['package_item'] as $pack_val) {
                            $packageItemsStr .= '<package_item>';
                            $packageItemsStr .= $xmlObj->array2xml($pack_val);
                            $packageItemsStr .= '</package_item>';
                        }
                        $packageItemsStr .= '</package_items>';
                        $tradeOrderInfoStr .= $packageItemsStr;
                        unset($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['package_items']);
                    }
                    
                    if (!empty($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['logistics_service_list'])) {
                        if (empty($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['logistics_service_list']['logistics_service'][0])) {
                            $respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['logistics_service_list']['logistics_service'] = array($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['logistics_service_list']['logistics_service']);
                        }
                        $logisticsStr = '<logistics_service_list>';
                        foreach ($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['logistics_service_list']['logistics_service'] as $logistics_val) {
                            $logisticsStr .= '<logistics_service>';
                            $logisticsStr .= $xmlObj->array2xml($logistics_val);
                            $logisticsStr .= '</logistics_service>';
                        }
                        $logisticsStr .= '</logistics_service_list>';
                        $tradeOrderInfoStr .= $logisticsStr;
                        unset($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['logistics_service_list']);
                    }
                    
                    $tradeListStr = '';
                    if (!empty($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['trade_order_list'])) {
                        if (empty($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['trade_order_list'][0])) {
                            $respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['trade_order_list'] = array($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['trade_order_list']);
                        }
                        $tradeListStr = '<trade_order_list>';
                        foreach ($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['trade_order_list'] as $tra_val) {
                            $orderStr = $tra_val['string'];
                            $orderArr = explode(',', $orderStr);
                            foreach ($orderArr as $orderVal) {
                                $tradeListStr .= '<string>';
                                $tradeListStr .= $orderVal;
                                $tradeListStr .= '</string>';
                            }
                        }
                        $tradeListStr .= '</trade_order_list>';
                        $tradeOrderInfoStr .= $tradeListStr;
                        unset($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']['trade_order_list']);
                    }
                    
                    $tradeOrderInfoStr .= $xmlObj->array2xml($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']);
                    $tradeOrderInfoStr .= '</trade_order_info>';
                    unset($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']['trade_order_info']);                    
                    
                    $xmlStr .= $tradeOrderInfoStr;
                    $xmlStr .= $xmlObj->array2xml($respArr['wlb_waybill_i_fullupdate_response']['waybill_apply_update_info']);
                    if (array_key_exists('request_id', $respArr['wlb_waybill_i_fullupdate_response'])) {
                        $requestId = $respArr['wlb_waybill_i_fullupdate_response']['request_id'];
                    }
                    $xmlStr .= !empty($requestId) ? "</waybill_apply_update_info><request_id>$requestId</request_id></wlb_waybill_i_fullupdate_response>" : '</waybill_apply_update_info></wlb_waybill_i_fullupdate_response>';
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