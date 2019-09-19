<?php
/**
 * 获取发货地&CP开通状态&账户的使用情况处理类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/cainiao/cainiaoRequest.php';
class wlbISearch extends cainiaoRequest {
    //转发给菜鸟
    public function search($params) {
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
        
        //for recording log
        $c = new TopClient;
        cainiao_service::$_toApiUrl = $c->gatewayUrl;
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
             * 菜鸟封装请求search接口的方法
             */
            /*
            $c = new TopClient;
            cainiao_service::$_toApiUrl = $c->gatewayUrl;
            $c->appkey = CAINIAO_APP_KEY;
            $c->secretKey = CAINIAO_APP_SECRET;
            */
			try{
				$req = new WlbWaybillISearchRequest;
				$waybill_apply_request = new WaybillApplyRequest;
				if (!empty($params['cp_code'])) {
					$waybill_apply_request->cp_code=$params['cp_code'];
				}
				$req->setWaybillApplyRequest(json_encode($waybill_apply_request));
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
                if (!array_key_exists('error_response', $respArr)) {//成功
                    if (!empty($respArr['wlb_waybill_i_search_response']['subscribtions']['waybill_apply_subscription_info'])) {
                        if (empty($respArr['wlb_waybill_i_search_response']['subscribtions']['waybill_apply_subscription_info'][0])) {
                            $respArr['wlb_waybill_i_search_response']['subscribtions']['waybill_apply_subscription_info'] = array($respArr['wlb_waybill_i_search_response']['subscribtions']['waybill_apply_subscription_info']);
                        }
                        $columnsArr = $this->get_database_relation('search_all');
                        $columns = implode(',', array_values($columnsArr)) . ',create_time';
                        $columns_value = ':' . implode(",:", $columnsArr) . ',now()';
                        $insertSql = "INSERT INTO csk_seller_waybill_info({$columns}) VALUES({$columns_value})";
                        $model = $db->prepare($insertSql);
                        foreach ($respArr['wlb_waybill_i_search_response']['subscribtions']['waybill_apply_subscription_info'] as $info_val) {
                            $i = 0;
                            $values = array();
                            $branchColumns = $this->get_database_relation('search_branch');//获取网点相关字段
                            $addrColumns = $this->get_database_relation('search_addr');//获取地址相关字段
                            if (!empty($info_val['branch_account_cols']['waybill_branch_account'])) {
                                if (empty($info_val['branch_account_cols']['waybill_branch_account'][0])) {
                                    $info_val['branch_account_cols']['waybill_branch_account'] = array($info_val['branch_account_cols']['waybill_branch_account']);
                                }
                                foreach ($info_val['branch_account_cols']['waybill_branch_account'] as $branch_val) {
                                    if (!empty($branch_val['shipp_address_cols']['waybill_address'])) {
                                        if (empty($branch_val['shipp_address_cols']['waybill_address'][0])) {
                                            $branch_val['shipp_address_cols']['waybill_address'] = array($branch_val['shipp_address_cols']['waybill_address']);
                                        }
                                        foreach ($branch_val['shipp_address_cols']['waybill_address'] as $add_val) {
                                            $selectSql = "SELECT * FROM csk_seller_waybill_info WHERE seller_id=:seller_id AND waybill_address_id = :waybill_address_id";
                                            $selectModel = $db->prepare($selectSql);
                                            $selectModel->bindParam(':seller_id', $branch_val['seller_id']);
                                            $selectModel->bindParam(':waybill_address_id', $add_val['waybill_address_id']);
                                            $selectModel->execute();
                                            $waybillInfo = $selectModel->fetch(PDO::FETCH_ASSOC);
                                            //如果是已经存在该商家的发货地址信息，就更新相关数据
                                            if (empty($waybillInfo)) {
                                                foreach ($branchColumns as $b_c_k=>$b_c_v) {
                                                    $values[$i][':'.$b_c_v] = empty($branch_val[$b_c_k]) ? '' : $branch_val[$b_c_k] ;
                                                }
                                                foreach ($addrColumns as $a_c_k=>$a_c_v) {
                                                    $values[$i][':'.$a_c_v] = empty($add_val[$a_c_k]) ? '' : $add_val[$a_c_k] ;
                                                }
                                                $i++;
                                            } else {
                                                $setSql = " ship_town='{$add_val['town']}', ship_county='{$add_val['area']}', ship_city='{$add_val['city']}', ship_prov='{$add_val['province']}', ship_detail_address='{$add_val['address_detail']}', waybill_address_id='{$add_val['waybill_address_id']}',"
                                                . "seller_id='{$branch_val['seller_id']}', quantity='{$branch_val['quantity']}', allocated_quantity='{$branch_val['allocated_quantity']}', branch_code='{$branch_val['branch_code']}', branch_name='{$branch_val['branch_name']}', print_quantity='{$branch_val['print_quantity']}', cancel_quantity='{$branch_val['cancel_quantity']}',"
                                                . "cp_code='{$info_val['cp_code']}', cp_type='{$info_val['cp_type']}',create_time=now() ";
                                                $updateSql = "UPDATE csk_seller_waybill_info SET {$setSql} WHERE seller_id=:seller_id AND waybill_address_id=:waybill_address_id";
                                                $updateModel = $db->prepare($updateSql);
                                                $updateModel->bindParam(':seller_id', $branch_val['seller_id']);
                                                $updateModel->bindParam(':waybill_address_id', $add_val['waybill_address_id']);
                                                $updateModel->execute();
                                            }
                                        }
                                    }
                                }
                            }
                            $requestId = empty($respArr['wlb_waybill_i_search_response']['request_id']) ? '' : $respArr['wlb_waybill_i_search_response']['request_id'] ;
                            if (!empty($values) && empty($waybillInfo)) {
                                $cpType = $info_val['cp_type'];
                                $cpCode = $info_val['cp_code'];
                                foreach ($values as $val) {
                                    $val['cp_type'] = $cpType;
                                    $val['cp_code'] = $cpCode;
                                    $model->execute($val);
                                }
                            }
                        }
                        $xmlStr = '<?xml version="1.0" encoding="utf-8"?><wlb_waybill_i_search_response><subscribtions>';

                        $subInfoStr = '';
                        foreach ($respArr['wlb_waybill_i_search_response']['subscribtions']['waybill_apply_subscription_info'] as $sub_v) {
                            $subInfoStr .= '<waybill_apply_subscription_info>';
                            if (!empty($sub_v['branch_account_cols']['waybill_branch_account'])) {
                                //waybill_branch_account
                                if (empty($sub_v['branch_account_cols']['waybill_branch_account'][0])) {
                                    $sub_v['branch_account_cols']['waybill_branch_account'] = array($sub_v['branch_account_cols']['waybill_branch_account']);
                                }
                                $branchColsStr = '<branch_account_cols>';
                                $branchAccountStr = '';
                                foreach ($sub_v['branch_account_cols']['waybill_branch_account'] as $acc_v) {
                                    $branchAccountStr .= '<waybill_branch_account>';
                                    //waybill_address
                                    if (!empty($acc_v['shipp_address_cols']['waybill_address'])) {
                                        if (empty($acc_v['shipp_address_cols']['waybill_address'][0])) {
                                            $acc_v['shipp_address_cols']['waybill_address'] = array($acc_v['shipp_address_cols']['waybill_address']);
                                        }
                                        $shipAddrStr = '<shipp_address_cols>';
                                        foreach ($acc_v['shipp_address_cols']['waybill_address'] as $add_v) {
                                            $shipAddrStr .= '<waybill_address>';
                                            $shipAddrStr .= $xmlObj->array2xml($add_v);
                                            $shipAddrStr .= '</waybill_address>';
                                        }
                                        $shipAddrStr .= '</shipp_address_cols>';
                                        unset($acc_v['shipp_address_cols']);
                                        
                                        $branchAccountStr .= $shipAddrStr;
                                        $branchAccountStr .= $xmlObj->array2xml($acc_v);
                                        $branchAccountStr .= '</waybill_branch_account>';
                                    }
                                }
                                $branchColsStr .= $branchAccountStr;
                                $branchColsStr .= '</branch_account_cols>';
                                
                                unset($sub_v['branch_account_cols']);
                                
                                $subInfoStr .= $branchColsStr;
                                $subInfoStr .= $xmlObj->array2xml($sub_v);
                                $subInfoStr .= '</waybill_apply_subscription_info>';
                            }
                        }
                        $xmlStr .= $subInfoStr;
                        $xmlStr .= '</subscribtions>';
                        
                        if (array_key_exists('request_id', $respArr['wlb_waybill_i_search_response'])) {
                            $xmlStr .= "<request_id>$requestId</request_id>";
                        }
                        $xmlStr .= '</wlb_waybill_i_search_response>';
                        $logTxt['return_msg'] = $xmlStr;
                        return $this->msgObj->outputCainiao(1, '0000', $xmlStr, $logTxt);
                    }
                } else {//菜鸟返回的错误信息
                    //$xmlStr = $resp;
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