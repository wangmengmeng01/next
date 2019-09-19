<?php
/**
 * cainiao.waybill.ii.get  电子面单云打印接口类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/cainiao/cnRequest.php';
class CnWlbIiGet extends cnRequest{
    public function get ($params) {
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
            //$productType = $tokenInfo['product_type'];
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
            try {
                $req = new CainiaoWaybillIiGetRequest;
        
                $param_waybill_cloud_print_apply_new_request = new WaybillCloudPrintApplyNewRequest;
                $param_waybill_cloud_print_apply_new_request->cp_code=$params['cp_code'];
                if (!empty($params['product_code'])) {
                    $param_waybill_cloud_print_apply_new_request->product_code=$params['product_code'];
                }
                
                $sender = new UserInfoDto;
                $address = new AddressDto;
                $address->city=$shipCity;
                $address->detail=$shipDetailAddress;
                $address->district=$shipCounty;
                $address->province=$shipProv;
                $address->town=$shipTown;
                $sender->address = $address;
                if (!empty($params['sender']['mobile'])) {
                    $sender->mobile=$params['sender']['mobile'];
                }
                $sender->name=$params['sender']['name'];
                if (!empty($params['sender']['phone'])) {
                    $sender->phone=$params['sender']['phone'];
                }
                $param_waybill_cloud_print_apply_new_request->sender = $sender;
                
                if (!empty($params['trade_order_info_dtos']['trade_order_info_dto'])) {
                    $tradeOrderInfoDtosArr = $params['trade_order_info_dtos']['trade_order_info_dto'];
                    if (empty($tradeOrderInfoDtosArr[0])) {
                        $tradeOrderInfoDtosArr = array($tradeOrderInfoDtosArr);
                    }
                    foreach ($tradeOrderInfoDtosArr as $dto_v) {
                        $trade_order_info_dto = new TradeOrderInfoDto;
                        if (!empty($dto_v['logistics_services'])) {
                            $trade_order_info_dto->logistics_services=$dto_v['logistics_services'];
                        }
                        $trade_order_info_dto->object_id=$dto_v['object_id'];
                    
                        $order_info = new OrderInfoDto;
                        /*if (in_array($dto_v['order_info']['order_channels_type'],array('OTHER','POS','MIA'))) {
                            $dto_v['order_info']['order_channels_type'] = 'OTHERS';
                        }*/
                        $order_info->order_channels_type=$dto_v['order_info']['order_channels_type'];
                        $order_info->trade_order_list=$dto_v['order_info']['trade_order_list'];
                        $trade_order_info_dto->order_info = $order_info;
                    
                        $package_info = new PackageInfoDto;
                        if (empty($dto_v['package_info']['items']['item'][0])) {
                            $dto_v['package_info']['items']['item'] = array($dto_v['package_info']['items']['item']);
                        }
                        foreach ($dto_v['package_info']['items']['item'] as $item_v) {
                            $item = new Item;
                            $item->count=$item_v['count'];
                            $item->name=$item_v['name'];
                            $items[] = $item;
                        }
                        
                        $package_info->items = $items;
                        if (!empty($dto_v['package_info']['id'])) {
                            $package_info->id=$dto_v['package_info']['id'];
                        }
                        if (!empty($dto_v['package_info']['volume'])) {
                            $package_info->volume=$dto_v['package_info']['volume'];
                        }
                        if (!empty($dto_v['package_info']['weight'])) {
                            $package_info->weight=$dto_v['package_info']['weight'];
                        }
                        $trade_order_info_dto->package_info = $package_info;
                    
                        $recipient = new UserInfoDto;
                        $address = new AddressDto;
                        $address->city=$dto_v['recipient']['address']['city'];
                        $address->detail=$dto_v['recipient']['address']['detail'];
                        $address->district=$dto_v['recipient']['address']['district'];
                        $address->province=$dto_v['recipient']['address']['province'];
                        $address->town=$dto_v['recipient']['address']['town'];
                        $recipient->address = $address;
                        $recipient->mobile=$dto_v['recipient']['mobile'];
                        $recipient->name=$dto_v['recipient']['name'];
                        $recipient->phone=$dto_v['recipient']['phone'];
                        $trade_order_info_dto->recipient = $recipient;
                        if (cainiao_service::$_appKey == 'RYD') {
                            $trade_order_info_dto->template_url=$dto_v['template_url'];
                        } else {
                            $trade_order_info_dto->template_url = 'http://cloudprint.daily.taobao.net/template/standard/137405/10';
                        }                                          
                        $trade_order_info_dto->user_id=$sellerId;
                        $trade_order_info_dtos[] = $trade_order_info_dto;
                    }
                    $param_waybill_cloud_print_apply_new_request->trade_order_info_dtos = $trade_order_info_dtos;

                    $req->setParamWaybillCloudPrintApplyNewRequest(json_encode($param_waybill_cloud_print_apply_new_request));
                }
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
                    $xmlStr = '<?xml version="1.0" encoding="utf-8"?><cainiao_waybill_ii_get_response>';
                    if (!empty($respArr['cainiao_waybill_ii_get_response']['modules'])) {
                        if (empty($respArr['cainiao_waybill_ii_get_response']['modules']['waybill_cloud_print_response'][0])) {
                            $respArr['cainiao_waybill_ii_get_response']['modules']['waybill_cloud_print_response'] = array($respArr['cainiao_waybill_ii_get_response']['modules']['waybill_cloud_print_response']);
                        }
                        $moduleStr = '<modules>';
                        $printResStr = '';
                        foreach ($respArr['cainiao_waybill_ii_get_response']['modules']['waybill_cloud_print_response'] as $mod_v) {
                            $printResStr .= '<waybill_cloud_print_response>';
                            $printResStr .= $xmlObj->array2xml($mod_v);
                            $printResStr .= '</waybill_cloud_print_response>';
                        }
                        $moduleStr .= $printResStr;
                        $moduleStr .= '</modules>';
                    }
                    $xmlStr .= $moduleStr;
                    //给wms加一个商家id的字段
                    $xmlStr .= '<cainiao_seller_id>'.$sellerId.'</cainiao_seller_id>';

                    $xmlStr .= '</cainiao_waybill_ii_get_response>';
                    
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
