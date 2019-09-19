<?php
require API_ROOT . '/router/interface/wms/storage/common/wmsRequest.php';
/**
 * 仓储交易出库单下发接口处理类(WMS_CONSIGN_ORDER_NOTIFY)
 * @author Renee
 *
 */
class wmsConsignOrderNotify extends wmsRequest{
    public function notify($params){
        if (empty($params)) {
            return $this->msgObj->outputCnStorage(false, '失败：请求的数据为空！', 'S003');
        } else {
            if (empty($params['orderItemList']['orderItem'])) {
                return $this->msgObj->outputCnStorage(false, '失败：订单明细为空！', 'S003');
            } else {
                global $db;

                $orderTypeRelation = array(
                    '201'=>'JYCK',
                    '502'=>'HHCK',
                    '503'=>'BFCK'
                );

                $orderSourceRelation = array(
                    '201'=>'TB',
                    '214'=>'JD',
                    '203'=>'SN',
                    '204'=>'AMAZON',
                    '205'=>'DD',
                    '206'=>'EBAY',
                    '207'=>'OTHER',
                    '208'=>'YHD',
                    '209'=>'GM',
                    '210'=>'PP',
                    '211'=>'JM',
                    '212'=>'LF',
                    '202'=>'1688',
                    '301'=>'OTHERS',
                    '213'=>'TM',
                    '219'=>'YX',
                    '222'=>'MGJ',
                    '221'=>'JS',
                    '217'=>'YG',
                    '218'=>'YT',
                    '220'=>'MIA',
                );

                $xmlStr = '';
                $xmlStr .= '<?xml version="1.0" encoding="utf-8"?><request><deliveryOrder>';
                $xmlStr .= '<deliveryOrderCode>' . $params['erpOrderCode']        . '</deliveryOrderCode>';
                $xmlStr .= '<cnOrderId>'         . $params['orderCode']           . '</cnOrderId>';
                $xmlStr .= '<warehouseCode>'     . $params['storeCode']           . '</warehouseCode>';
                $xmlStr .= '<orderType>'         . $orderTypeRelation[$params['orderType']] . '</orderType>';
                $xmlStr .= '<sourcePlatformCode>'. $orderSourceRelation[$params['orderSource']].'</sourcePlatformCode>';
                $xmlStr .= '<createTime>'        . $params['orderCreateTime']     . '</createTime>';
                $xmlStr .= '<placeOrderTime>'    . $params['orderShopCreateTime'] . '</placeOrderTime>';
                $xmlStr .= '<operateTime>'       . $params['orderExaminationTime']. '</operateTime>';
                $xmlStr .= '<shopNick>'          . $params['userName']            . '</shopNick>';
                $xmlStr .= '<logisticsCode>'     . $params['tmsServiceCode']      . '</logisticsCode>';
                $xmlStr .= '<buyerMessage>'      . $params['buyerMessage']        . '</buyerMessage>';
                $xmlStr .= '<sellerMessage>'     . $params['sellerMessage']       . '</sellerMessage>';
                $xmlStr .= '<remark>'            . $params['remark']              . '</remark>';

                $xmlStr .= '<senderInfo>';
                $xmlStr .= '<name>'              . $params['senderInfo']['senderName']        . '</name>';
                $xmlStr .= '<mobile>'            . $params['senderInfo']['senderMobile']      . '</mobile>';
                $xmlStr .= '<tel>'               . $params['senderInfo']['senderPhone']       . '</tel>';
                $xmlStr .= '<province>'          . $params['senderInfo']['senderProvince']    . '</province>';
                $xmlStr .= '<city>'              . $params['senderInfo']['senderCity']        . '</city>';
                $xmlStr .= '<area>'              . $params['senderInfo']['senderArea']        . '</area>';
                $xmlStr .= '<town>'              . $params['senderInfo']['senderTown']        . '</town>';
                $xmlStr .= '<detailAddress>'     . $params['senderInfo']['senderAddress']     . '</detailAddress>';
                $xmlStr .= '<zipCode>'           . $params['senderInfo']['senderZipCode']     . '</zipCode>';
                $xmlStr .= '<email>'             . $params['senderInfo']['senderEmail']       . '</email>';
                $xmlStr .= '</senderInfo>';

                $xmlStr .= '<receiverInfo>';
                $xmlStr .= '<name>'              . $params['receiverInfo']['receiverName']        . '</name>';
                $xmlStr .= '<zipCode>'           . $params['receiverInfo']['receiverZipCode']     . '</zipCode>';
                $xmlStr .= '<tel>'               . $params['receiverInfo']['receiverPhone']       . '</tel>';
                $xmlStr .= '<mobile>'            . $params['receiverInfo']['receiverMobile']      . '</mobile>';
                $xmlStr .= '<email>'             . $params['receiverInfo']['receiverEmail']       . '</email>';
                $xmlStr .= '<province>'          . $params['receiverInfo']['receiverProvince']    . '</province>';
                $xmlStr .= '<city>'              . $params['receiverInfo']['receiverCity']        . '</city>';
                $xmlStr .= '<area>'              . $params['receiverInfo']['receiverArea']        . '</area>';
                $xmlStr .= '<town>'              . $params['receiverInfo']['receiveTown']         . '</town>';
                $xmlStr .= '<detailAddress>'     . $params['receiverInfo']['receiverAddress']     . '</detailAddress>';
                $xmlStr .= '</receiverInfo>';
                $xmlStr .= '</deliveryOrder>';

                $xmlStr .= '<orderLines>';
                foreach ($params['orderItemList']['orderItem'] as $o_k=>$o_v) {
                    $xmlStr .= '<orderLine>';
                    $xmlStr .= '<sourceOrderCode>'.$o_v['orderSourceCode'].'</sourceOrderCode>';
                    $xmlStr .= '<cnOrderItemId>'.$o_v['orderItemId']     .'</cnOrderItemId>';
                    $xmlStr .= '<ownerCode>'    .$params['ownerUserId']  .'</ownerCode>';
                    $xmlStr .= '<itemCode>'     .$o_v['itemCode']        .'</itemCode>';
                    $xmlStr .= '<planQty>'      .$o_v['itemQuantity']    .'</planQty>';
                    $xmlStr .= '<actualPrice>'  .$o_v['actualPrice']     .'</actualPrice>';
                    $xmlStr .= '</orderLine>';
                }
                $xmlStr .= '</orderLines>';
                $xmlStr .= '</request>';

                //推送给wms
                $response = $this->send($xmlStr,'deliveryorder.create');
                //解析返回的数据
                if (!empty($response)) {
                    if ($response['success']) {
                        $wmsOrderStatusUploadStr  = "<request>";
                        $wmsOrderStatusUploadStr .= '<orderType>' . $params['orderType'] . '</orderType>';
                        $wmsOrderStatusUploadStr .= '<orderCode>' . $params['orderCode'] . '</orderCode>';
                        $wmsOrderStatusUploadStr .= '<status>WMS_ACCEPT</status>';
                        $wmsOrderStatusUploadStr .= '<operator>张三</operator>';
                        $wmsOrderStatusUploadStr .= '<operatorContact>13514516577</operatorContact>';
                        $wmsOrderStatusUploadStr .= '<operateDate>'.date("Y-m-d H:i:s").'</operateDate>';
                        $wmsOrderStatusUploadStr .= '</request>';

                        $orderStatusUploadRs = $this->requestOmsToCn($params, 'WMS_ORDER_STATUS_UPLOAD', $wmsOrderStatusUploadStr);

                        if ($orderStatusUploadRs['success']) {
                            //$orderInfo = $this->check_delivery_order($params);
                            $this->insertOrderInfo($params);
                            /*if (empty($orderInfo)) {
                                //写入出库单数据
                                $this->insertOrderInfo($params);
                            } else {
                                $columnsArr = array();
                                $updateInfoArr = array();
                                if ($orderInfo['receiver_province'] != $params['receiverInfo']['receiverProvince']) {
                                    $columnsArr[] = 'receiver_province';
                                    $updateInfoArr[':receiver_province'] = $params['receiverInfo']['receiverProvince'];
                                }
                                if ($orderInfo['receiver_city'] != $params['receiverInfo']['receiverCity']) {
                                    $columnsArr[] = 'receiver_city';
                                    $updateInfoArr[':receiver_city'] = $params['receiverInfo']['receiverCity'];
                                }
                                if (!empty($params['receiverInfo']['receiverArea'])) {
                                    if ($orderInfo['receiver_area'] != $params['receiverInfo']['receiverArea']) {
                                        $columnsArr[] = 'receiver_area';
                                        $updateInfoArr[':receiver_area'] = $params['receiverInfo']['receiverArea'];
                                    }
                                }
                                if (!empty($params['receiverInfo']['receiverTown'])) {
                                    if ($orderInfo['receiver_town'] != $params['receiverInfo']['receiverTown']) {
                                        $columnsArr[] = 'receiver_town';
                                        $updateInfoArr[':receiver_town'] = $params['receiverInfo']['receiverTown'];
                                    }
                                }
                                if ($orderInfo['receiver_detail_address'] != $params['receiverInfo']['receiverAddress']) {
                                    $columnsArr[] = 'receiver_detail_address';
                                    $updateInfoArr[':receiver_detail_address'] = $params['receiverInfo']['receiverAddress'];
                                }
                                if ($orderInfo['receiver_name'] != $params['receiverInfo']['receiverName']) {
                                    $columnsArr[] = 'receiver_name';
                                    $updateInfoArr[':receiver_name'] = $params['receiverInfo']['receiverName'];
                                }
                                if ($orderInfo['receiver_mobile'] != $params['receiverInfo']['receiverMobile']) {
                                    $columnsArr[] = 'receiver_mobile';
                                    $updateInfoArr[':receiver_mobile'] = $params['receiverInfo']['receiverMobile'];
                                }
                                if (!empty($params['receiverInfo']['receiverPhone'])){
                                    if ($orderInfo['receiver_tel'] != $params['receiverInfo']['receiverPhone']) {
                                        $columnsArr[] = 'receiver_tel';
                                        $updateInfoArr[':receiver_tel'] = $params['receiverInfo']['receiverPhone'];
                                    }
                                }
                                if (!empty($columnsArr)) {
                                    $setSqlStr = '';
                                    foreach ($columnsArr as $cval) {
                                        $setSqlStr .= $cval . "=:" . $cval . ',';
                                    }
                                    $setSqlStr = substr($setSqlStr, 0, -1);
                                    $updateSql = "UPDATE t_delivery_order_info SET " . $setSqlStr . " WHERE delivery_id=" . $orderInfo['delivery_id'];
                                    $model = $db->prepare($updateSql);
                                    $model->execute($updateInfoArr);
                                }
                            }*/
                            return $this->msgObj->outputCnStorage(true,'','',$response['addon']);
                        } else {
                            //调用取消接口
                            $cancelStr  = '<?xml version="1.0" encoding="utf-8"?>';
                            $cancelStr .= '<request>';
                            $cancelStr .= '<ownerUserId>'. $params['ownerUserId'] . '</ownerUserId>';
                            $cancelStr .= '<orderCode>'  . $params['orderCode']   . '</orderCode>';
                            $cancelStr .= '<storeCode>'  . $params['storeCode']   . '</storeCode>';
                            $cancelStr .= '<orderType>'  . $params['orderType']   . '</orderType>';
                            $cancelStr .= '</request>';

                            $cancelRs = $this->requestOmsToWms($cancelStr, 'WMS_ORDER_CANCEL_NOTIFY');
                            if ($cancelRs['success']) {
                                return $this->msgObj->outputCnStorage(false,'下发失败：调用订单流水接口失败['.$orderStatusUploadRs['errorMsg'].']，取消订单成功！','S004');
                            } else {
                                return $this->msgObj->outputCnStorage(false,'下发失败：调用订单流水接口失败['.$orderStatusUploadRs['errorMsg'].']，取消订单失败['.$cancelRs['errorMsg'].']！','S004');
                            }
                        }
                    } else {
                        return $this->msgObj->outputCnStorage(false, $response['errorMsg'], $response['errorCode'], $response['addon']);
                    }
                } else {
                    return $this->msgObj->outputQimen(false, 'wms接口调用失败', 'S007');
                }
            }
        }
    }

    /*public function check_delivery_order($params){
        global $db;
        $sql = "SELECT * FROM t_delivery_order_info 
                WHERE cn_order_code=:cn_order_code
                AND customer_id=:customer_id 
                AND warehouse_code=:warehouse_code;";
        $model = $db->prepare($sql);
        $values = array();
        $values[':cn_order_code'] = $params['orderCode'];
        $values[':customer_id'] = $params['ownerUserId'];
        $values[':warehouse_code'] = $params['storeCode'];
        $model->execute($values);
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        return $rs;
    }*/

    public function insertOrderInfo($params){
        $orderItemList = $params['orderItemList']['orderItem'];

        $column_order_info_arr = $this->get_dataBase_relation('delivery_order_info');
        $column_order_info_arr1 = $column_order_info_arr;
        unset($column_order_info_arr['receiverInfo']);
        unset($column_order_info_arr['senderInfo']);
        $column_key_orderInfo = implode(',', array_values($column_order_info_arr)).',';
        $column_key_receiverInfo = implode(',', array_values($column_order_info_arr1['receiverInfo'])) . ',';
        $column_key_senderInfo = implode(',', array_values($column_order_info_arr1['senderInfo'])) . ',create_time';
        $column_key_orderInfo .= $column_key_receiverInfo.$column_key_senderInfo;

        $column_order_detail_arr = $this->get_dataBase_relation('delivery_order_detail');
        $column_key_orderDetail = implode(',', array_values($column_order_detail_arr)) . ',delivery_id,create_time';

        global $db;
        if (!empty($params)) {
            $column_value_orderInfo = ":" . implode(',:', array_values($column_order_info_arr)) . ',';
            $column_value_receiverInfo = ":" . implode(',:', array_values($column_order_info_arr1['receiverInfo'])) . ',';
            $column_value_senderInfo = ":" . implode(',:', array_values($column_order_info_arr1['senderInfo'])) . ',now()';
            $column_value_orderInfo .= $column_value_receiverInfo.$column_value_senderInfo;

            $sql = "INSERT INTO t_delivery_order_info(" . $column_key_orderInfo .') VALUES(' . $column_value_orderInfo . ')';
            $model = $db->prepare($sql);
            $values = array();
            foreach ($column_order_info_arr1 as $k=>$v) {
                if (in_array($k, array('receiverInfo','senderInfo'))) {
                    foreach ($v as $i=>$j) {
                        $values[':'.$j] = empty($params[$k][$i]) ? '' : $params[$k][$i];
                    }
                } else {
                    $values[':'.$v] = empty($params[$k]) ? '' : $params[$k];
                }
            }
            $model->execute($values);
            $orderId = $db->lastInsertId();

            if (!empty($orderItemList)) {
                foreach ($orderItemList as $o_d_k=>$o_d_v) {
                    $column_value_orderDetail = ':' . implode(',:', $column_order_detail_arr) . ',:delivery_id,now()';
                    $detail_sql = "INSERT IGNORE INTO t_delivery_order_detail(" . $column_key_orderDetail . ') VALUES(' . $column_value_orderDetail . ')';
                    $dValues = array();
                    $dValues[':delivery_id'] = $orderId;
                    foreach ($column_order_detail_arr as $d_k=>$d_v) {
                        $dValues[':'.$d_v] =  empty($o_d_v[$d_k]) ? '' : $o_d_v[$d_k] ;
                    }
                    $dModel = $db->prepare($detail_sql);
                    $dModel->execute($dValues);
                }
            }
        }
        return true;
    }
}