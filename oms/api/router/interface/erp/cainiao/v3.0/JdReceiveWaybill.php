<?php
/**
 * 京东接单接口
 * User: 20171012
 * Date: 2018/3/21
 * Time: 15:12
 */
require API_ROOT . '/router/interface/erp/cainiao/JdRequest.php';
class JdReceiveWaybill extends JdRequest {
    public function receive($params) {
        try {
            $infoArr = $this->getSenderInfo($params);

            if (array_key_exists('access_token',$infoArr)) {
                $accessToken = $infoArr['access_token'];
            } else {
                return $infoArr;
            }

            //获取空运单号的pclId
            $pclIds = explode(',',$params['pclIds']);

            if ($params['waybillCodes']=='' && !strpos($params['waybillCodes'],',')) {
                $waybillCodes = array();
                foreach ($pclIds as $p_k=>$p_v) {
                    $waybillCodes[$p_k] = '';
                }
            } else {
                $waybillCodes = explode(',',$params['waybillCodes']);
            }

            $setCodes = array_combine($pclIds,$waybillCodes);
            //空运单号的pclIds
            $pclIdArr = array();
            //获取到的运单号
            //$wbCodeArr = array();
            //只需要获取打印信息的pcl-wb
            $bsPclWbArr = array();
            //是否需要调用下单接口
            $getFlag = 0;
            foreach ($setCodes as $key=>$val) {
                //无运单号，需要调用下单接口
                if (empty($val)) {
                    $getFlag = 1;
                    array_push($pclIdArr,$key);
                } else {
                    $bsPclWbArr[$key] = $val;
                }
            }

            //匹配jd承运商编码
            $providerInfo = $this->getJdProvider($params,$infoArr['seller_id']);
            if (empty($providerInfo['provider_code'])) {
                return $this->msgObj->outputJd(1, '该商家承运商配置不完整，请联系实施人员', '', $this->logTxt);
            }

            $func = new func();
            if ($getFlag == 1) {
                //改造请求数据
                $contentArr = $this->modifyContent($params,$infoArr,$providerInfo);
                $pclWbArr = array();
                //如果请求有多包裹，需要替换请求数据中的商家自由单号,所以多包裹得一个一个请求
                foreach ($pclIdArr as $pvv) {
                    $contentArr['vendorOrderCode'] = $pvv;
                    $contentArr['waybillCount'] = 1;

                    //调用获取单号接口
                    $req = new LdopAlphaWaybillReceiveRequest();
                    $content_js = json_encode($contentArr);
                    $req->setContent($content_js);

                    jd_service::$_requestJdTime = date("Y-m-d H:i:s");
                    $resp1_js = $this->c->execute($req, $accessToken);
                    jd_service::$_responseOmsTime = date("Y-m-d H:i:s");

//                    $resp1_js = json_encode($resp1);
                    $respArr1 = json_decode($resp1_js,true);
                    $msgId = $func->makeMsgId();

                    //系统级参数错误
                    if (isset($respArr1['error_response'])) {
                        $func->addJdWaybillLog($params['customerCode'],jd_service::$_method,$pvv,$msgId,jd_service::$_msgId,JD_YD_URL,$content_js,$resp1_js,jd_service::$_requestJdTime,jd_service::$_responseOmsTime,$respArr1['resultInfo']['statusCode']);
                        return $this->msgObj->outputJd(1, $respArr1['error_response']['en_desc'].'('.$respArr1['error_response']['code'].'):'.$respArr1['error_response']['zh_desc'], '', $this->logTxt);
                    }

                    //判断下单成功与否
                    if ($respArr1['statusCode'] != 0) {//失败
                        $func->addJdWaybillLog($params['customerCode'],jd_service::$_method,$pvv,$msgId,jd_service::$_msgId,JD_YD_URL,$content_js,$resp1_js,jd_service::$_requestJdTime,jd_service::$_responseOmsTime,$respArr1['resultInfo']['statusCode']);
                        return $this->msgObj->outputJd($respArr1['statusCode'], $respArr1['statusMessage'], '', $this->logTxt);
                    } else {
                        $pclWbArr[$pvv] = $respArr1['data']['waybillCodeList'][0];
                    }
                    $func->addJdWaybillLog($params['customerCode'],jd_service::$_method,$pvv,$msgId,jd_service::$_msgId,JD_YD_URL,$content_js,$resp1_js,jd_service::$_requestJdTime,jd_service::$_responseOmsTime,$respArr1['resultInfo']['statusCode']);
                }
            }

            //pclId=>waybillCode
            if (!empty($pclWbArr) && !empty($bsPclWbArr)) {
                $rspPclWbArr = $pclWbArr + $bsPclWbArr;//保留键值
            } else {
                if (empty($bsPclWbArr) && empty($pclWbArr)) {
                    return $this->msgObj->outputJd(1, '数据处理出错！', '', $this->logTxt);
                }
                if (empty($bsPclWbArr)) {
                    $rspPclWbArr = $pclWbArr;
                }
                if (empty($pclWbArr)) {
                    $rspPclWbArr = $bsPclWbArr;
                }
            }
            $wbCodes = array_values($rspPclWbArr);

            //调用大头笔接口
            $req2 = new LdopAlphaVendorBigshotQueryRequest();
            $req2->setWaybillCode($wbCodes[0]);
            $req2->setProviderId($providerInfo['provider_id']);
            $req2->setProviderCode($providerInfo['provider_code']);

            jd_service::$_requestJdTime = date("Y-m-d H:i:s");
            $resp2_js = $this->c->execute($req2, $accessToken);
            jd_service::$_responseOmsTime = date("Y-m-d H:i:s");
            //$resp2_js = json_encode($resp2);
            $respArr2 = json_decode($resp2_js,true);

            //大头笔接口日志id
            $msgId2 = $func->makeMsgId();
            //大头笔接口请求数据
            $req2_arr = array(
                'waybillCode'=>$wbCodes[0],
                'providerId'=>$providerInfo['provider_id'],
                'providerCode'=>$providerInfo['provider_code']
            );
            $req2_js = json_encode($req2_arr);

            if (!isset($respArr2['jingdong_ldop_alpha_vendor_bigshot_query_responce']['resultInfo'])) {
                $func->addJdWaybillLog($params['customerCode'],'jingdong.ldop.alpha.vendor.bigshot.query',$params['pclIds'],$msgId2,jd_service::$_msgId,JD_YD_URL,$req2_js,$resp2_js,jd_service::$_requestJdTime,jd_service::$_responseOmsTime,$respArr2['code']);
                return $this->msgObj->outputJd(1, $respArr2['en_desc'].'('.$respArr2['code'].'):'.$respArr2['zh_desc'], '', $this->logTxt);
            }

            //记录调用大头笔接口日志
            $func->addJdWaybillLog($params['customerCode'],'jingdong.ldop.alpha.vendor.bigshot.query',$params['pclIds'],$msgId2,jd_service::$_msgId,JD_YD_URL,$req2_js,$resp2_js,jd_service::$_requestJdTime,jd_service::$_responseOmsTime,$respArr2['resultInfo']['statusCode']);

            //判断调用大头笔接口成功与否
            if (!empty($respArr2['jingdong_ldop_alpha_vendor_bigshot_query_responce']['resultInfo']['data'])) {
                $rsArr = array();
                $rsArr['statusCode'] = $respArr2['jingdong_ldop_alpha_vendor_bigshot_query_responce']['resultInfo']['statusCode'];
                $rsArr['statusMessage'] = $respArr2['jingdong_ldop_alpha_vendor_bigshot_query_responce']['resultInfo']['statusMessage'];
                $i = 0;
                foreach ($rspPclWbArr as $pk=>$wv) {
                    $rsArr['data'][$i] =$respArr2['jingdong_ldop_alpha_vendor_bigshot_query_responce']['resultInfo']['data'];
                    $rsArr['data'][$i]['pclId'] = $pk;
                    $rsArr['data'][$i]['waybillCode'] = $wv;
                    $i++;
                }
                $endRs = json_encode($rsArr);
            } else {
                $endRs = '';
            }

            $func->addJdWaybillLog($params['customerCode'],jd_service::$_method,$params['pclIds'],jd_service::$_msgId,'',JD_YD_URL,jd_service::$_data,$endRs,jd_service::$_requestOmsTime,date("Y-m-d H:i:s"),$respArr2['resultInfo']['statusCode']);
            return $this->msgObj->outputJd($respArr2['jingdong_ldop_alpha_vendor_bigshot_query_responce']['resultInfo']['statusCode'], $respArr2['jingdong_ldop_alpha_vendor_bigshot_query_responce']['resultInfo']['statusMessage'], $endRs, $this->logTxt);
        } catch (Exception $e) {
            return $this->msgObj->outputJd($e->getCode(), $e->getMessage(), '', $this->logTxt);
        }
    }

    /**
     * 编辑下单请求数据
     * @param $params  wms请求数据
     * @param $infoArr 匹配到的商家信息
     * @return json    请求的json数据
     */
    public function modifyContent($params,$infoArr,$providerInfo) {
        //$params = array_diff_key($params,['customerCode'=>'11','warehouseCode'=>'11','shopName'=>'11','waybillCodes'=>'11','pclIds'=>'11']);

        unset($params['customerCode']);
        unset($params['warehouseCode']);
        unset($params['shopName']);
        unset($params['waybillCodes']);
        unset($params['pclIds']);

        //添加商家编码
        $params['vendorCode'] = $infoArr['seller_id'];
        //添加jd承运商编码
        $params['providerCode'] = $providerInfo['provider_code'];
        //结算编码
        if (!empty($infoArr['ship_addr_info']['settlement_code'])) {
            $params['settlementCode'] = $infoArr['ship_addr_info']['settlement_code'];
        }

        //添加网点编码以及发货地址
        $params['branchCode'] = $infoArr['ship_addr_info']['branch_code'];
        $params['fromAddress']['provinceId']   = $infoArr['ship_addr_info']['ship_prov_id'];
        $params['fromAddress']['provinceName'] = $infoArr['ship_addr_info']['ship_prov'];
        $params['fromAddress']['cityId']       = $infoArr['ship_addr_info']['ship_city_id'];
        $params['fromAddress']['cityName']     = $infoArr['ship_addr_info']['ship_city'];
        $params['fromAddress']['countryId']    = $infoArr['ship_addr_info']['ship_county_id'];
        $params['fromAddress']['countryName']  = $infoArr['ship_addr_info']['ship_county'];
        $params['fromAddress']['address']      = $infoArr['ship_addr_info']['ship_detail_address'];

        return $params;
    }
}