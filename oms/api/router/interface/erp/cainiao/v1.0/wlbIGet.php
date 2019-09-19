<?php
/**
 * 菜鸟电子面单获取电子面单号接口类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/cainiao/cainiaoRequest.php';
class wlbIGet extends cainiaoRequest{
    public function get($params) {
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
            global $db;
            /*
             * 菜鸟封装的请求get的方法
             */
            /*
            $c = new TopClient;
            $c->appkey = CAINIAO_APP_KEY;
            $c->secretKey = CAINIAO_APP_SECRET;
            */
			try {
				$req = new WlbWaybillIGetRequest;
				$waybill_apply_new_request = new WaybillApplyNewRequest;
				$waybill_apply_new_request->cp_code=$params['cp_code'];
				$shipping_address = new WaybillAddress;
				if (!empty($shipCounty)) {
					$shipping_address->area = $shipCounty;
				}
				$shipping_address->province = $shipProv;
				if (!empty($shipTown)) {
					$shipping_address->town = $shipTown;
				}
				$shipping_address->address_detail = $shipDetailAddress;
				if (!empty($shipCity)) {
					$shipping_address->city = $shipCity;
				}
				$waybill_apply_new_request->shipping_address = $shipping_address;
				
				if (!empty($params['trade_order_info_cols'])) {
					if (empty($params['trade_order_info_cols'][0])) {
						$params['trade_order_info_cols'] = array($params['trade_order_info_cols']);
					}
					foreach ($params['trade_order_info_cols'] as $t_val) {
						$trade_order_info_cols = new TradeOrderInfo;
						$trade_order_info_cols->consignee_name = $t_val['consignee_name'];
						$trade_order_info_cols->order_channels_type = $t_val['order_channels_type'];
						$trade_order_info_cols->consignee_phone = $t_val['consignee_phone'];
					
						if (is_array($t_val['trade_order_list'])) {
							foreach ($t_val['trade_order_list'] as $val) {
								$trade_order_list[] =  $val;
							}
						} else {
							$trade_order_list = $t_val['trade_order_list'];
						}
						$trade_order_info_cols->trade_order_list = $trade_order_list;
						$consignee_address = new WaybillAddress;
						if (!empty($t_val['consignee_address']['area'])) {
							$consignee_address->area = $t_val['consignee_address']['area'];
						}
						$consignee_address->province = $t_val['consignee_address']['province'];
						if (!empty($t_val['consignee_address']['town'])) {
							$consignee_address->town = $t_val['consignee_address']['town'];
						}
						$consignee_address->address_detail = $t_val['consignee_address']['address_detail'];
						if (!empty($t_val['consignee_address']['city'])) {
							$consignee_address->city = $t_val['consignee_address']['city'];
						}
						$trade_order_info_cols->consignee_address = $consignee_address;
						if (!empty($t_val['send_phone'])) {
							$trade_order_info_cols->send_phone = $t_val['send_phone'];
						}
						if (!empty($t_val['weight'])) {
							$trade_order_info_cols->weight = $t_val['weight'];
						}
						if (!empty($t_val['send_name'])) {
							$trade_order_info_cols->send_name = $t_val['send_name'];
						}
					
						if (!empty($t_val['package_items'])) {
							if (empty($t_val['package_items'][0])) {
								$t_val['package_items'] = array($t_val['package_items']);
							}
							foreach ($t_val['package_items'] as $p_key=>$p_val) {
								$package_items = 'package_item_'.$p_key;
								$package_items = new PackageItem;
								$package_items->item_name = $p_val['item_name'];
								$package_items->count = $p_val['count'];
								$p_arr[] = $package_items;
							}
							$trade_order_info_cols->package_items = $p_arr;
						}
					
						if (!empty($t_val['logistics_service_list'])) {
							if (empty($t_val['logistics_service_list'][0])) {
								$t_val['logistics_service_list'] = array($t_val['logistics_service_list']);
							}
							foreach ($t_val['logistics_service_list'] as $l_key=>$l_val) {
								$logistics_service_list = 'logistics_service_list_'.$l_key;
								$logistics_service_list = new LogisticsService;
								$logistics_service_list->service_value4_json = $l_val['service_value4_json'];
								$logistics_service_list->service_code = $l_val['service_code'];
								$l_arr[] = $logistics_service_list;
							}
							$trade_order_info_cols->logistics_service_list = $l_arr;
						}
					
						$trade_order_info_cols->product_type = $productType;
						$trade_order_info_cols->real_user_id = $sellerId;
						if (!empty($t_val['volume'])) {
							$trade_order_info_cols->volume = $t_val['volume'];
						}
						if (!empty($t_val['package_id'])) {
							$trade_order_info_cols->package_id = $t_val['package_id'];
						}
						$t_arr[] = $trade_order_info_cols;
					}
				}
				
				
				$waybill_apply_new_request->trade_order_info_cols = $t_arr;
				$req->setWaybillApplyNewRequest(json_encode($waybill_apply_new_request));
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
                    //拼接返回数据
                    $xmlStr = '<?xml version="1.0" encoding="utf-8"?><wlb_waybill_i_get_response><waybill_apply_new_cols>';
                    //需要给YWMS那边返回该商家对应的发货地址
                    if (cainiao_service::$_appKey == 'YWMS') {
                        $xmlStr .= '<shipping_address>';
                        $xmlStr .= '<province>' . $shipProv . '</province>';
                        $xmlStr .= empty($shipCity) ? '' : '<city>' . $shipCity . '</city>';
                        $xmlStr .= empty($shipCounty) ? '' : '<area>' . $shipCounty . '</area>';
                        $xmlStr .= empty($shipTown) ? '' : '<town>' . $shipTown . '</town>';
                        $xmlStr .= '<address_detail>' . $shipDetailAddress . '</address_detail>';
                        $xmlStr .= '</shipping_address>';
                    }
                    if (!empty($respArr['wlb_waybill_i_get_response']['waybill_apply_new_cols']['waybill_apply_new_info'])) {
                        if (empty($respArr['wlb_waybill_i_get_response']['waybill_apply_new_cols']['waybill_apply_new_info'][0])) {
                            $respArr['wlb_waybill_i_get_response']['waybill_apply_new_cols']['waybill_apply_new_info'] = array($respArr['wlb_waybill_i_get_response']['waybill_apply_new_cols']['waybill_apply_new_info']);
                        }
                        $infoStr = '';
                        foreach ($respArr['wlb_waybill_i_get_response']['waybill_apply_new_cols']['waybill_apply_new_info'] as $r_v) {
                            $infoStr .= '<waybill_apply_new_info>';
                            $tradeOrderInfoCols = $r_v['trade_order_info'];
                            unset($r_v['trade_order_info']);
                        
                            //trade_order_info
                            $tradeOrderInfoStr = '<trade_order_info>';
                            //package_item
                            if (!empty($tradeOrderInfoCols['package_items'])) {
                                if (empty($tradeOrderInfoCols['package_items']['package_item'][0])) {
                                    $tradeOrderInfoCols['package_items']['package_item'] = array($tradeOrderInfoCols['package_items']['package_item']);
                                }
                                $packageStr = '<package_items>';
                                foreach ($tradeOrderInfoCols['package_items']['package_item'] as $p_v) {
                                    $packageStr .= '<package_item>';
                                    $packageStr .= $xmlObj->array2xml($p_v);
                                    $packageStr .= '</package_item>';
                                }
                                $packageStr .= '</package_items>';
                                $tradeOrderInfoStr .= $packageStr;
                                unset($tradeOrderInfoCols['package_items']);
                            }
                        
                            //logistic_service_list
                            if (!empty($tradeOrderInfoCols['logistics_service_list'])) {
                                if (empty($tradeOrderInfoCols['logistics_service_list']['logistics_service'][0])) {
                                    $tradeOrderInfoCols['logistics_service_list']['logistics_service'] = array($tradeOrderInfoCols['logistics_service_list']['logistics_service']);
                                }
                                $logisticsStr = '<logistics_service_list>';
                                foreach ($tradeOrderInfoCols['logistics_service_list']['logistics_service'] as $ls_v) {
                                    $logisticsStr .= '<logistics_service>';
                                    $logisticsStr .= $xmlObj->array2xml($ls_v);
                                    $logisticsStr .= '</logistics_service>';
                                }
                                $logisticsStr .= '</logistics_service_list>';
                                $tradeOrderInfoStr .= $logisticsStr;
                                unset($tradeOrderInfoCols['logistics_service_list']);
                            }
                        
                            //trade_order_list改造
                            $tradeOrderListStr = '<trade_order_list>';
                            if (is_array($tradeOrderInfoCols['trade_order_list']['string'])) {
                                foreach ($tradeOrderInfoCols['trade_order_list']['string'] as $list_v) {
                                    $tradeOrderListStr .= '<string>';
                                    $tradeOrderListStr .= $list_v;
                                    $tradeOrderListStr .= '</string>';
                                }
                            } else {
                                $listStr = '<string>' . $tradeOrderInfoCols['trade_order_list']['string'] . '</string>';
                                $tradeOrderListStr .= $listStr;
                            }
                            $tradeOrderListStr .= '</trade_order_list>';
                            $tradeOrderInfoStr .= $tradeOrderListStr;
                            unset($tradeOrderInfoCols['trade_order_list']);
                        
                            $tradeOrderInfoStr .= $xmlObj->array2xml($tradeOrderInfoCols);
                            $tradeOrderInfoStr .= '</trade_order_info>';
                        
                            $infoStr .= $tradeOrderInfoStr;
                            $infoStr .= $xmlObj->array2xml($r_v);
                            $infoStr .= '</waybill_apply_new_info>';
                        }
                    }
                    $xmlStr .= $infoStr;
                    $xmlStr .= '</waybill_apply_new_cols>';
                    if (array_key_exists('request_id', $respArr['wlb_waybill_i_get_response'])) {
                        $requestId = $respArr['wlb_waybill_i_get_response']['request_id'];
                    }
                    $xmlStr .= empty($requestId) ? '</wlb_waybill_i_get_response>': "<request_id>$requestId</request_id></wlb_waybill_i_get_response>";
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