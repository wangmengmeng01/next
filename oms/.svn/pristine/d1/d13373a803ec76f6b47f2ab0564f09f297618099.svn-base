<?php
/**
 * 查询面单服务订购及面单使用情况接口
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/cainiao/cnRequest.php';
class CnWlbIiSearch extends cnRequest{
      public function search($params) {
          //获取sessionKey
          if (cainiao_service::$_appKey == 'RYD') {//判断订单来源于 RYD or YWMS
              $tokenInfo = $this->getTokenBySellerId($params);
          } else {
              $tokenInfo = $this->getTokenForSearch($params);
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
              
              try {
                  $req = new CainiaoWaybillIiSearchRequest;
                  if (!empty($params['cp_code'])) {
                      $req->setCpCode($params['cp_code']);
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
                  $respArr = json_decode(json_encode($resp),true);
                  cainiao_service::$_cnReturnStr = json_encode($respArr);

                  $emptyWaybill = 0;
                  if (!array_key_exists('error_response', $respArr)) {
                      if (!empty($respArr['waybill_apply_subscription_cols']['waybill_apply_subscription_info'])) {
                          if (empty($respArr['waybill_apply_subscription_cols']['waybill_apply_subscription_info'][0])) {
                              $respArr['waybill_apply_subscription_cols']['waybill_apply_subscription_info'] = array($respArr['waybill_apply_subscription_cols']['waybill_apply_subscription_info']);
                          }
                          $columnsArr = $this->get_database_relation('seller_waybill_info');
                          $columns = implode(',', array_values($columnsArr)) . ',create_time';
                          $columns_value = ':' . implode(",:", $columnsArr) . ',now()';
                          $insertSql = "INSERT INTO csk_seller_waybill_info({$columns}) VALUES({$columns_value})";
                          $model = $db->prepare($insertSql);
                          
                          foreach ($respArr['waybill_apply_subscription_cols']['waybill_apply_subscription_info'] as $cn_search_v) {
                              $i = 0;
                              $values = array();
                              $branchColumns = $this->get_database_relation('seller_waybill_branch');//获取网点相关字段
                              $addrColumns = $this->get_database_relation('seller_waybill_addr');//获取地址相关字段
                              
                              if (!empty($cn_search_v['branch_account_cols']['waybill_branch_account']) && empty($cn_search_v['branch_account_cols']['waybill_branch_account'][0])) {
                                  $cn_search_v['branch_account_cols']['waybill_branch_account'] = array($cn_search_v['branch_account_cols']['waybill_branch_account']);
                              }
                              foreach ($cn_search_v['branch_account_cols']['waybill_branch_account'] as $branch_account_v) {
                                  if (!empty($branch_account_v['shipp_address_cols']['address_dto'])) {
                                      if (empty($branch_account_v['shipp_address_cols']['address_dto'][0])) {
                                          $branch_account_v['shipp_address_cols']['address_dto'] = array($branch_account_v['shipp_address_cols']['address_dto']);
                                      }
                                      foreach ($branch_account_v['shipp_address_cols']['address_dto'] as $addr_v) {
                                          //$uniqueAddr = $addr_v['province'].$addr_v['detail'];
                                          $selectServicesSql = "SELECT * FROM csk_seller_waybill_info WHERE seller_id=:seller_id AND branch_code=:branch_code AND cp_code=:cp_code AND ship_detail_address=:ship_detail_address";
                                          $selectServiceModel = $db->prepare($selectServicesSql);
                                          $selectServiceModel->bindParam(':seller_id', $sellerId);
                                          $selectServiceModel->bindParam(':branch_code', $branch_account_v['branch_code']);
                                          $selectServiceModel->bindParam(':ship_detail_address', $addr_v['detail']);
                                          $selectServiceModel->bindParam(':cp_code', $cn_search_v['cp_code']);
                                          $selectServiceModel->execute();
                                          $waybillInfo = $selectServiceModel->fetch(PDO::FETCH_ASSOC);
                                          
                                          if (empty($waybillInfo)) {
                                              $emptyWaybill = 1;
                                              //新增
                                              foreach ($branchColumns as $b_c_k=>$b_c_v) {
                                                  $values[$i][':'.$b_c_v] = empty($branch_account_v[$b_c_k]) ? '' : $branch_account_v[$b_c_k] ;
                                              }
                                              $values[$i][':seller_id'] = $sellerId;
                                              foreach ($addrColumns as $a_c_k=>$a_c_v) {
                                                  $values[$i][':'.$a_c_v] = empty($addr_v[$a_c_k]) ? '' : $addr_v[$a_c_k] ;
                                              }
                                              $i++;
                                          } 
                                      }
                                  }
                              }
                              if (!empty($values) && $emptyWaybill == 1) {
                                  $cpType = $cn_search_v['cp_type'];
                                  $cpCode = $cn_search_v['cp_code'];
                                  foreach ($values as $val) {
                                      $val['cp_type'] = $cpType;
                                      $val['cp_code'] = $cpCode;
                                      $model->execute($val);
                                  }
                              }
                          }
                          
                          $xmlStr = '<?xml version="1.0" encoding="utf-8"?><cainiao_waybill_ii_search_response><waybill_apply_subscription_cols>';
                          $subInfoStr = '';
                          foreach ($respArr['waybill_apply_subscription_cols']['waybill_apply_subscription_info'] as $wb_sub_v) {
                              $subInfoStr .= '<waybill_apply_subscription_info>';
                              if (!empty($wb_sub_v['branch_account_cols']['waybill_branch_account'])) {
                                  if (empty($wb_sub_v['branch_account_cols']['waybill_branch_account'][0])) {
                                      $wb_sub_v['branch_account_cols']['waybill_branch_account'] = array($wb_sub_v['branch_account_cols']['waybill_branch_account']);
                                  }
                                  $branchColsStr = '<branch_account_cols>';
                                  foreach ($wb_sub_v['branch_account_cols']['waybill_branch_account'] as $w_branch_v) {
                                      $branchColsStr .= '<waybill_branch_account>';
                                      if (!empty($w_branch_v['shipp_address_cols']['address_dto'])) {
                                          if (empty($w_branch_v['shipp_address_cols']['address_dto'][0])) {
                                              $w_branch_v['shipp_address_cols']['address_dto'] = array($w_branch_v['shipp_address_cols']['address_dto']);
                                          }      
                                          $addrColsStr = '<shipp_address_cols>';
                                          foreach ($w_branch_v['shipp_address_cols']['address_dto'] as $w_addr_v) {
                                              $addrColsStr .= '<address_dto>';
                                              $addrColsStr .= $xmlObj->array2xml($w_addr_v);
                                              $addrColsStr .= '</address_dto>';
                                          }
                                          $addrColsStr .= '</shipp_address_cols>';
                                          unset($w_branch_v['shipp_address_cols']);
                                      }
                                      $addrColsStr .= $xmlObj->array2xml($w_branch_v);
                                      $branchColsStr .= $addrColsStr;
                                      $branchColsStr .= '</waybill_branch_account>';
                                  }
                                  $branchColsStr .= '</branch_account_cols>';
                                  unset($wb_sub_v['branch_account_cols']);
                              }
                              $branchColsStr .= $xmlObj->array2xml($wb_sub_v);
                              $subInfoStr .= $branchColsStr;
                              $subInfoStr .= '</waybill_apply_subscription_info>';
                          }
                          $xmlStr .= $subInfoStr . '</waybill_apply_subscription_cols></cainiao_waybill_ii_search_response>';
                          $logTxt['return_msg'] = $xmlStr;
                          return $this->msgObj->outputCainiao(1, '0000', $xmlStr, $logTxt);
                      }  
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