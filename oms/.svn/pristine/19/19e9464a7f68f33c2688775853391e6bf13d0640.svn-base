<?php

class DeliveryOrderShortageController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = DeliveryOrderShortage::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('出库管理', '发货单缺货转仓报表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }


    /**
     * 分配仓库
     * @param 查询参数 $_POST
     */
    public function actionAllocateWarehouse()
    {
        $connection = Yii::app()->db;
        $batchFlag = false;
        $rowData = $_REQUEST['row_data'];
        $requestData = json_decode($rowData);

        $rCustomerId = $requestData[0]->customer_id;
        $rWarehouseCode = $requestData[0]->warehouse_code;
        $rOrderType = $requestData[0]->order_type;
        $orderNoList = '';
        $orderNoSqlStr = '';
        //合并的商品编码
        $mergeSkuList = '';
        //判断是否是批量处理
        if (count($requestData) > 1) {
            $batchFlag = true;
        }
        foreach ($requestData as $request) {
            $param = (array)$request;
            if ($param['customer_id'] != $rCustomerId) {
                die(json_encode(array(
                    'rows' => array(),
                    'status' => 'error',
                    'msg' => '批量处理的订单货主必须相同！'
                )));
            }
            if ($param['warehouse_code'] != $rWarehouseCode) {
                die(json_encode(array(
                    'rows' => array(),
                    'status' => 'error',
                    'msg' => '批量处理的订单仓库必须相同！'
                )));
            }
            if ($param['order_type'] != $rOrderType) {
                die(json_encode(array(
                    'rows' => array(),
                    'status' => 'error',
                    'msg' => '批量处理的订单类型必须相同！'
                )));
            }

            if ($batchFlag) {
                $orderNoList .= $param['delivery_order_code'] . '/';
                $orderNoSqlStr .= "'" . $param['delivery_order_code'] . "',";
                $mergeSkuList .= $param['all_sku'] . ',';
            } else {
                $orderNoList .= $param['delivery_order_code'];
                $orderNoSqlStr .= "'" . $param['delivery_order_code'] . "'";
                $mergeSkuList .= $param['all_sku'];
            }
        }

        //订单号和商品信息的合并
        if ($batchFlag) {
            $orderNoList = substr($orderNoList, 0, -1);
            $orderNoSqlStr = substr($orderNoSqlStr, 0, -1);
            $mergeSkuList = substr($mergeSkuList, 0, -1);
        }

        $mergePlanQtySql = "SELECT b.item_code code,SUM(b.plan_qty) qty,c.descr_c name
                        FROM t_delivery_order_shortage a
                        LEFT JOIN t_delivery_order_shortage_detail b
                        ON a.order_id=b.order_id
                        LEFT JOIN t_base_product c
                        ON b.item_code=c.sku
                        WHERE a.delivery_order_code IN (" . $orderNoSqlStr . ") and b.is_valid=1
                        GROUP BY b.item_code";
        $mergePlanQtyCommand = $connection->createCommand($mergePlanQtySql);
        $mergePlanQtyRs = $mergePlanQtyCommand->queryAll();

        $itemStr = '';//商品报文拼接
        $itemPlanQty = array();//array(商品编码=>array(商品信息))
        foreach ($mergePlanQtyRs as $i_v) {
            $code = $i_v['code'];
            unset($i_v['code']);
            $itemPlanQty[$code] = $i_v;
            $itemStr .= "<itemCode>$code</itemCode>";
        }

        $rsCustomer = QimenCustomerBind::model()->find('customer_id=:customer_id', array(':customer_id' => $rCustomerId));
        $customerInfo = QimenCustomer::model()->findByPk($rsCustomer['qimen_customer_id']);

        $params = array(
            'method' => 'inventory.sum.available',
            'customerid' => $rCustomerId,
            'appkey' => $customerInfo['wms_app_key'],
            'timestamp' => date('Y-m-d H:i:s'),
        );
        //排除已经下发的仓库
        $remSql = "SELECT distinct warehouse_code FROM t_delivery_order_shortage WHERE delivery_order_code IN ({$orderNoSqlStr}) and customer_id = '{$rCustomerId}'" ;
        $remCommand = $connection->createCommand($remSql);
        $remWarehouse = $remCommand->queryAll();
        $remStr = '';
        foreach ($remWarehouse as $k => $r_v) {
            $remStr .= "'" . $r_v['warehouse_code'] . "',";
        }
        $param['not_warehouse_code'] = substr($remStr, 0, -1);
        $warehouseData = DeliveryOrderShortage::model()->allocateWarehouse($param);//备选仓库的仓库编码以及优先级
        $alternativeWhInfo = array();
        include_once Yii::app()->basePath . '/ext/httpclient.php';
        include_once Yii::app()->basePath . '/ext/xml.php';
        foreach ($warehouseData as $wh_v) {
            $alternativeWhCode = $wh_v['alternative_wh'];
            $requestXml = "<?xml version='1.0' encoding='utf-8'?><request><criteria><ownerCode>$rCustomerId</ownerCode><deliveryOrderType>$rOrderType</deliveryOrderType>";
            $requestXml .= "<warehouseCode>$alternativeWhCode</warehouseCode><items>";
            $requestXml .= $itemStr;
            $requestXml .= '</items></criteria></request>';
            $params['data'] = $requestXml;
            $params['sign'] = strtoupper(base64_encode(md5($customerInfo['wms_secret'] . $requestXml . $customerInfo['wms_secret'])));
            $httpObj = new httpclient();
            $response = $httpObj->post($customerInfo['wms_api_url'], $params);
            if ($response == '') {
                die(json_encode(array(
                    'rows' => array(),
                    'status' => 'error',
                    'msg' => '数据推送失败，请检查YWMS接口是否能正常访问'
                )));
            } else {
                $xmlObj = new xml();
                $responseArr = $xmlObj->xmlStr2array($response);
                if ($responseArr['flag'] == 'success') {
                    $alternativeWhInfo[$wh_v['alternative_wh']]['level'] = $wh_v['level'];
                    if (!empty($responseArr['items']['item'])) {
                        if (empty($responseArr['items']['item'][0])) {
                            $responseArr['items']['item'] = array($responseArr['items']['item']);
                        }
                        $alternativeWhInfo[$wh_v['alternative_wh']]['items']['item'] = $responseArr['items']['item'];
                    } else {
                        die(json_encode(array(
                            'rows' => array(),
                            'status' => 'error',
                            'msg' => 'YWMS接口返回数据有误'
                        )));
                    }
                } else {
                    die(json_encode(array(
                        'rows' => array(),
                        'status' => 'error',
                        'msg' => $responseArr['message']
                    )));
                }
            }
        }
        //按照优先级level排序
        $arr = array_map(create_function('$n', 'return $n["level"];'), $alternativeWhInfo);
        array_multisort($arr, SORT_ASC, $alternativeWhInfo);
        $anArr = array();
        $i = 0;
        foreach ($alternativeWhInfo as $at_k => $at_v) {
            foreach ($at_v['items']['item'] as $item_v) {
                $itemCode = $item_v['itemCode'];
                $anArr[$i] = array(
                    'order_no' => $orderNoList,
                    'customer_id' => $rCustomerId,
                    'delivery_warehouse_code' => $at_k,
                    'item_code' => $itemCode,
                    'item_name' => $itemPlanQty[$itemCode]['name'],
                    'quantity' => $itemPlanQty[$itemCode]['qty'],
                    'available_quantity' => $item_v['onhandQuantity'] - $item_v['occupiedQuantity'],
                    'occupied_quantity' => $item_v['occupiedQuantity'],
                    'onhand_quantity' => $item_v['onhandQuantity'],
                );
                $i++;
            }
        }

        echo '{"total":' . $i . ',"rows":' . CJSON::encode($anArr) . '}';
    }

    /**
     * 确认缺货订单发货仓库
     */
    public function actionConfirmWarehouse()
    {
        $param = $_REQUEST;
        $warehouseCode = $param['warehouse_code'];
        $orderType = $param['order_type'];
        $recommendedWarehouse = $param['recommended_wh_code'];//推荐仓库编码
        $confirmWarehouse = $param['confirm_wh_code'];//确认仓库编码
        $customerId = $param['customer_id'];
        $orderNos = $param['order_code'];

        $orderNoStr = strstr($orderNos, '/') ? "'" . str_replace("/", "','", $orderNos) . "'" : "'" . $orderNos . "'";
        $updateOrderSql = "UPDATE t_delivery_order_shortage 
                            SET mid_warehouse_code='$recommendedWarehouse',af_warehouse_code='$confirmWarehouse'
                            WHERE customer_id='$customerId' 
                            AND warehouse_code='$warehouseCode' 
                            AND order_type='$orderType' 
                            AND delivery_order_code IN ($orderNoStr)
                            AND done_flag=0";
        $connection = Yii::app()->db;
        $updateOrderCommand = $connection->createCommand($updateOrderSql);
        $rowCount = $updateOrderCommand->execute();
        die(json_encode(array(
            'rows' => array(),
            'status' => 'error',
            'msg' => '成功更新' . $rowCount . '条记录！'
        )));
    }

    /**
     * 重新下发缺货订单
     */
    public function actionSendOrderInfo()
    {
        $rowsDataArr = json_decode($_REQUEST['rows_data'], true);

        include_once Yii::app()->basePath . '/ext/httpclient.php';
        include_once Yii::app()->basePath . '/ext/xml.php';

        $connection = Yii::app()->db;
        $deliType = array('JYCK', 'HHCK', 'BFCK', 'QTCK', 'jit', 'jit_4a');//发货单订单类型
        $vipType = array('jit', 'jit_4a');//唯品会订单类型
        $sendSucOrderArr = array();
        //接口请求参数
        $requestParams = array(
            'timestamp' => date("Y-m-d H:i:s"),
            'format' => 'xml',
            'sign_method' => 'md5',
        );

        $num = count($rowsDataArr);

        if ($num < 1) {
            die(json_encode(array(
                'rows' => array(),
                'status' => 'error',
                'msg' => '请选择至少一条订单！'
            )));
        }
        if ($num == 1 && (empty($rowsDataArr[0]['mid_warehouse_code']) || empty($rowsDataArr[0]['af_warehouse_code']))) {
            die(json_encode(array(
                'rows' => array(),
                'status' => 'error',
                'msg' => '请先选择发货仓库！'
            )));
        }
        $succ_i = 0;//处理成功订单数
        foreach ($rowsDataArr as $key => $order) {
            $httpObj = new httpclient();
            if (empty($order['mid_warehouse_code']) || empty($order['af_warehouse_code'])) {
                continue;
            }
            //判断是否在订单相关类型中
            if (in_array($order['order_type'], $deliType)) {
                $nowShortageOrderInfoSql = "SELECT done_flag 
                                            FROM t_delivery_order_shortage 
                                            WHERE delivery_order_code='" . $order['delivery_order_code'] .
                    "' AND customer_id='" . $order['customer_id'] .
                    "' ORDER BY create_time DESC LIMIT 1;";
                $nowCommand = $connection->createCommand($nowShortageOrderInfoSql);
                $nowShortageOrderInfo = $nowCommand->queryRow();
                if (!empty($nowShortageOrderInfo) && in_array($nowShortageOrderInfo['done_flag'], array(1, 2, 3))) {
                    if ($num == 1) {
                        die(json_encode(array(
                            'rows' => array(),
                            'status' => 'error',
                            'msg' => '失败：此订单状态已改变，请重新刷新页面！'
                        )));
                    } else {
                        continue;
                    }
                }
                if (in_array($order['order_type'], $vipType)) {
                    //查看原订单是否存在
                    $oldPickInfo = VipJitPickList::model()->checkOne($order['customer_id'], $order['delivery_order_code'], $order['warehouse_code']);
                    $warehouse = !empty($order['af_warehouse_code']) ? $order['af_warehouse_code'] : $order['mid_warehouse_code'];
                    if (empty($oldPickInfo)) {
                        if ($num == 1 ) {
                            die(json_encode(array(
                                'rows' => array(),
                                'status' => 'error',
                                'msg' => '订单异常：找不到原单信息！'
                            )));
                        } else {
                            continue;
                        }
                    }
                    $jsonData = '{"vendor_id":"' . $order['customer_id'] . '","pick_no":"' . $order['delivery_order_code'] . '","warehouse":"' . $warehouse . '","is_lack":1}';
                    $vip_params = array(
                        'method' => 'vip.pickorder.sync',
                        'vendorid' => $order['customer_id'],
                        'warehouseid' => $order['warehouse_code'],
                        'format' => 'json',
                        'timestamp' => date("Y-m-d H:i:s"),
                        'selfreq' => '96c76c3f40b00bd9b24602ba96da18e1',
                        'data' => $jsonData,
                    );
                    //调用下发拣货单接口
                    $response = $httpObj->post('http://www.oms.com/vip_api.php', $vip_params);
                    if (!empty($response)) {
                        $returnArrr = json_decode($response, true);
                        if ($returnArrr['flag'] == 'success') {
                            //修改缺货单状态
                            $updateShortageOrderSql = "UPDATE t_delivery_order_shortage
                                                    SET done_flag=1
                                                    WHERE order_id ={$order['order_id']}";
                            $updateShortageOrderCommand = $connection->createCommand($updateShortageOrderSql);
                            $updateShortageOrderCommand->execute();

                            //拣货单明细
                            $detailSql = " select d.lack_qty,pp.* from t_delivery_order_shortage_detail d 
                                          join t_vip_pick_product as pp on d.item_code = pp.barcode 
                                          where d.is_valid = 1 and d.is_short =1 and d.order_id = '{$order['order_id']}' and pp.pick_id = {$oldPickInfo['id']} ";
                            $detailCommand = $connection->createCommand($detailSql);
                            $detailInfo = $detailCommand->queryAll();
                            $total = count($detailInfo);

                            $transaction = $connection->beginTransaction();
                            //插入数据到t_vip_pick_list
                            $pickListArr = array(
                                'vendor_id' => $order['customer_id'],
                                'po_no' => $oldPickInfo['po_no'],
                                'pick_no' => $order['delivery_order_code'],
                                'po_id' => $oldPickInfo['po_id'],
                                'pick_type' => $oldPickInfo['pick_type'],
                                'warehouse' => $warehouse,
                                'sell_site' => $oldPickInfo['sell_site'],
                                'store_sn' => $oldPickInfo['store_sn'],
                                'jit_type' => $oldPickInfo['jit_type'],
                                'co_mode' => $oldPickInfo['co_mode'],
                                'order_cate' => $oldPickInfo['order_cate'],
                                'pick_num' => $order['short_sku_num'],
                                'status' => '已下发',
                                'is_get_detail' => 1,
                                'total' => $total,
                                'is_send' => 1,
                                'send_time' => date("Y-m-d H:i:s"),
                                'is_lack' => 1,
                                'create_time' => $order['create_time'],//记录为缺货回传单的创建时间
                                'record_time' => date('Y-m-d H:i:s'),
                            );
                            $pickResult = $connection->createCommand()->insert('t_vip_pick_list', $pickListArr);
                            $last_id = $connection->getLastInsertID();
                            //t_vip_pick_product
                            $proResult = 0;
                            foreach ($detailInfo as $d) {
                                $productArr = array(
                                    'pick_id' => $last_id,
                                    'pick_no' => $d['pick_no'],
                                    'stock' => $d['lack_qty'],
                                    'barcode' => $d['barcode'],
                                    'art_no' => $d['art_no'],
                                    'product_name' => $d['product_name'],
                                    'size' => $d['size'],
                                    'actual_unit_price' => $d['actual_unit_price'],
                                    'actual_market_price' => $d['actual_market_price'],
                                    'sell_site' => $d['sell_site'],
                                    'store_sn' => $d['store_sn'],
                                    'jit_type' => $d['jit_type'],
                                    'create_time' => date('Y-m-d H:i:s'),
                                );
                                $proResult = $connection->createCommand()->insert('t_vip_pick_product', $productArr);
                            }
                            if ($pickResult && $proResult) {
                                $transaction->commit();
                            } else {
                                $transaction->rollBack();
                            }
                            $succ_i++;
                        } else {
                            if ($num == 1) {
                                die(json_encode(array(
                                    'rows' => array(),
                                    'status' => 'error',
                                    'msg' => '重新下单失败！'
                                )));
                            } else {
                                continue;
                            }
                        }
                    } else {
                        if ($num == 1) {
                            die(json_encode(array(
                                'rows' => array(),
                                'status' => 'error',
                                'msg' => '下单接口无返回，查看接口是否访问正常！'
                            )));
                        } else {
                            continue;
                        }
                    }
                } else {
                    $rsCustomer = QimenCustomerBind::model()->find('customer_id=:customer_id', array(':customer_id' => $order['customer_id']));
                    $customerInfo = QimenCustomer::model()->findByPk($rsCustomer['qimen_customer_id']);
                    $requestParams['app_key'] = $customerInfo['wms_app_key'];
                    $requestParams['v'] = '2.4';
                    $requestParams['customerId'] = $rsCustomer['qimen_customer_id'];
                    $appSecret = $customerInfo['wms_secret'];
                    //拼装报文
                    $cancelRequestStr = "<?xml version='1.0' encoding='utf-8'?><request>";
                    $cancelRequestStr .= "<warehouseCode>" . $order['warehouse_code'] . "</warehouseCode>";
                    $cancelRequestStr .= "<ownerCode>" . $order['customer_id'] . "</ownerCode>";
                    $cancelRequestStr .= "<orderCode>" . $order['delivery_order_code'] . "</orderCode>";
                    $cancelRequestStr .= "<orderType>" . $order['order_type'] . "</orderType>";
                    $cancelRequestStr .= "<cancelReason>YDCPOMS</cancelReason></request>";

                    $requestParams['method'] = 'order.cancel';
                    $requestParams['sign'] = $this->makeSign($requestParams, $appSecret, $cancelRequestStr);
                    $requestParams['data'] = $cancelRequestStr;

                    //查询原订单相关信息

                    $whereConditions = " WHERE customer_id='" . $order['customer_id'] . "' AND
                                      warehouse_code='" . $order['warehouse_code'] . "' AND
                                      delivery_order_code='" . $order['delivery_order_code'] . "' AND
                                      order_type='" . $order['order_type'] . "' ";
                    $findOrderSql = "SELECT
                                        delivery_id,
                                        source_platform_code,
                                        place_order_time,
                                        operate_time,
                                        shop_nick,
                                        logistics_code,
                                        receiver_company,
                                        receiver_name,
                                        receiver_zipcode,
                                        receiver_tel,
                                        receiver_mobile,
                                        receiver_email,
                                        receiver_countrycode,
                                        receiver_province,
                                        receiver_city,
                                        receiver_area,
                                        receiver_town,
                                        receiver_detail_address,
                                        create_time
                                    FROM t_delivery_order_info " . $whereConditions . " AND is_valid=1 ORDER BY create_time DESC";
                    $findOrderCommand = $connection->createCommand($findOrderSql);
                    $originOrderInfo = $findOrderCommand->queryRow();

                    //调用接口取消订单
                    $cancelResponse = $httpObj->post('http://127.0.0.1/oms/api.php', $requestParams);

                    $xmlObj = new xml();
                    $cancelResponseArr = $xmlObj->xmlStr2array($cancelResponse);
                    if ($cancelResponseArr['flag'] == 'success') {
                        //将OMS系统中订单状态改为无效
                        $cancelOrderSql = "UPDATE t_delivery_order_info 
                                       SET is_valid=0,order_status='CANCELED' " . $whereConditions . " AND is_valid=1;";
                        $cancelOrderCommand = $connection->createCommand($cancelOrderSql);
                        $cancelOrderCommand->execute();
                    } else {
                        if ($num == 1) {
                            die(json_encode(array(
                                'rows' => array(),
                                'status' => 'error',
                                'msg' => '接口：订单取消失败！' . $cancelResponseArr['message']
                            )));
                        }
                    }
                    unset($cancelResponseArr['flag']);
                    unset($requestParams['data']);
                    unset($requestParams['sign']);
                    unset($requestParams['method']);

                    //判断此订单是否存在请求报文,存在就取原报文，不存在自己拼接
                    $findOrderLogSql = "SELECT request_param 
                                    FROM t_order_interface_log 
                                    WHERE customer_id='" . $order['customer_id'] . "' AND 
                                          order_no='" . $order['delivery_order_code'] . "' AND
                                          method='deliveryorder.create' AND
                                          return_status = 1
                                    ORDER BY create_time DESC;";
                    $findOrderLogCommand = $connection->createCommand($findOrderLogSql);
                    $requestDataArr = $findOrderLogCommand->queryRow();

                    if (empty($requestDataArr) || empty($requestDataArr['request_param'])) {
                        if (empty($originOrderInfo)) {
                            if ($num == 1) {
                                $reSelectSql = "SELECT
                                			MAX(order_id) order_id
                                		FROM
                                			t_delivery_order_shortage " . $whereConditions;
                                $reSelectCommand = $connection->createCommand($reSelectSql);
                                $reSelectData = $reSelectCommand->queryRow();

                                $reUpdateSql = "UPDATE t_delivery_order_shortage
                                        SET done_flag=0
                                        WHERE order_id='" . $reSelectData['order_id'] . "'";
                                $reUpdateCommand = $connection->createCommand($reUpdateSql);
                                $reUpdateCommand->execute();
                                die(json_encode(array(
                                    'rows' => array(),
                                    'status' => 'error',
                                    'msg' => '订单异常：找不到原单信息！'
                                )));
                            } else {
                                continue;
                            }
                        } else {
                            $requestStr = "<?xml version='1.0' encoding='utf-8'?><request><deliveryOrder>";
                            $requestStr .= "<deliveryOrderCode>{$order['delivery_order_code']}</deliveryOrderCode>";
                            $requestStr .= "<orderType>{$order['order_type']}</orderType>";
                            $requestStr .= "<warehouseCode>{$order['af_warehouse_code']}</warehouseCode>";
                            $requestStr .= "<createTime>{$order['erp_create_time']}</createTime>";
                            $requestStr .= "<placeOrderTime>{$originOrderInfo['place_order_time']}</placeOrderTime>";
                            $requestStr .= "<operateTime>{$originOrderInfo['operate_time']}</operateTime>";
                            $requestStr .= "<shopNick>{$originOrderInfo['shop_nick']}</shopNick>";
                            $requestStr .= "<logisticsCode>{$originOrderInfo['logistics_code']}</logisticsCode><receiverInfo>";
                            $requestStr .= !empty($originOrderInfo['receiver_company']) ? "<company>" . $originOrderInfo['receiver_company'] . "</company>" : '';
                            $requestStr .= !empty($originOrderInfo['receiver_zipcode']) ? "<zipCode>" . $originOrderInfo['receiver_zipcode'] . "</zipCode>" : '';
                            $requestStr .= !empty($originOrderInfo['receiver_tel']) ? "<tel>" . $originOrderInfo['receiver_tel'] . "</tel>" : '';
                            $requestStr .= !empty($originOrderInfo['receiver_email']) ? "<email>" . $originOrderInfo['receiver_email'] . "</email>" : '';
                            $requestStr .= !empty($originOrderInfo['receiver_countrycode']) ? "<countryCode>" . $originOrderInfo['receiver_countrycode'] . "</countryCode>" : '';
                            $requestStr .= !empty($originOrderInfo['receiver_area']) ? "<area>" . $originOrderInfo['receiver_area'] . "</area>" : '';
                            $requestStr .= !empty($originOrderInfo['receiver_town']) ? "<town>" . $originOrderInfo['receiver_town'] . "</town>" : '';
                            $requestStr .= "<name>{$originOrderInfo['receiver_name']}</name>";
                            $requestStr .= "<mobile>{$originOrderInfo['receiver_mobile']}</mobile>";
                            $requestStr .= "<province>{$originOrderInfo['receiver_province']}</province>";
                            $requestStr .= "<city>{$originOrderInfo['receiver_city']}</city>";
                            $requestStr .= "<detailAddress>{$originOrderInfo['receiver_detail_address']}</detailAddress></receiverInfo></deliveryOrder>";

                            //查询订单明细信息
                            $findOrderDetailInfoSql = "SELECT order_line_no,customer_id,item_code,inventory_type,item_name,plan_qty,actual_price
                                        FROM t_delivery_order_detail WHERE delivery_id='" . $originOrderInfo['delivery_id'] . "'";
                            $findOrderDetailCommand = $connection->createCommand($findOrderDetailInfoSql);
                            $originOrderDetailInfo = $findOrderDetailCommand->queryAll();
                            if (empty($originOrderDetailInfo)) {
                                continue;
                            }
                            $orderDetailStr = "<orderLines>";
                            foreach ($originOrderDetailInfo as $detail_v) {
                                $orderDetailStr .= "<orderLine>";
                                $orderDetailStr .= "<orderLineNo>" . $detail_v['order_line_no'] . "</orderLineNo>";
                                $orderDetailStr .= "<ownerCode>" . $order['customer_id'] . "</ownerCode>";
                                $orderDetailStr .= "<itemCode>" . $detail_v['item_code'] . "</itemCode>";
                                $orderDetailStr .= "<itemName>" . $detail_v['item_name'] . "</itemName>";
                                $orderDetailStr .= "<planQty>" . $detail_v['plan_qty'] . "</planQty>";
                                $orderDetailStr .= "<actualPrice>" . $detail_v['actual_price'] . "</actualPrice>";
                                $orderDetailStr .= !empty($detail_v['inventory_type']) ? "<inventoryType>" . $detail_v['inventory_type'] . "</inventoryType>" : '';
                                $orderDetailStr .= "</orderLine>";
                            }
                            $orderDetailStr .= "</orderLines>";
                            $requestStr .= $orderDetailStr . "</request>";
                        }
                    } else {
                        $requestParam = $requestDataArr['request_param'];
                        $requestStr = preg_replace("/<warehouseCode>(.*)<\/warehouseCode>/s", '<warehouseCode>' . $order['af_warehouse_code'] . '</warehouseCode>', $requestParam);
                    }
                    $requestParams['method'] = 'deliveryorder.create';
                    $requestParams['sign'] = $this->makeSign($requestParams, $appSecret, $requestStr);
                    $requestParams['data'] = $requestStr;

                    //下发订单成功标记
                    $orderCreateFlag = 0;
                    //重新下发订单
                    $response = $httpObj->post('http://127.0.0.1/oms/api.php', $requestParams);

                    if (!empty($response)) {
                        if ($response == -3) {
                            $reSelectSql = "SELECT
                                			MAX(order_id) order_id
                                		FROM
                                			t_delivery_order_shortage " . $whereConditions;
                            $reSelectCommand = $connection->createCommand($reSelectSql);
                            $reSelectData = $reSelectCommand->queryRow();

                            $reUpdateSql = "UPDATE t_delivery_order_shortage
                                        SET done_flag=0 
                                        WHERE order_id='" . $reSelectData['order_id'] . "'";
                            $reUpdateCommand = $connection->createCommand($reUpdateSql);
                            $reUpdateCommand->execute();
                            die(json_encode(array(
                                'rows' => array(),
                                'status' => 'error',
                                'msg' => '重新下单超时！'
                            )));
                        }
                        $xmlObj = new xml();
                        $responseArr = $xmlObj->xmlStr2array($response);
                        if ($responseArr['flag'] == 'success') {
                            $orderCreateFlag = 1;
                            $sendSucOrderArr[$key] = $order['order_id'];

                            //更新缺货订单列表中的处理标记
                            $sendSucOrderStr = '';
                            foreach ($sendSucOrderArr as $suc_v) {
                                $sendSucOrderStr .= "'" . $suc_v . "',";
                            }
                            $sendSucOrderStr = substr($sendSucOrderStr, 0, -1);
                            $updateShortageOrderSql = "UPDATE t_delivery_order_shortage
                                                    SET done_flag=1
                                                    WHERE order_id IN (" . $sendSucOrderStr . ")";
                            $updateShortageOrderCommand = $connection->createCommand($updateShortageOrderSql);
                            $updateShortageOrderCommand->execute();

                            $transWhRequestStr = '<?xml version="1.0" encoding="utf-8"?><request>';
                            $transWhRequestStr .= '<warehouseCode>' . $order['af_warehouse_code'] . '</warehouseCode>';
                            $transWhRequestStr .= '<originalWarehouseCode>' . $order['warehouse_code'] . '</originalWarehouseCode>';
                            $transWhRequestStr .= '<ownerCode>' . $order['customer_id'] . '</ownerCode>';
                            $transWhRequestStr .= '<orderCode>' . $order['delivery_order_code'] . '</orderCode>';
                            $transWhRequestStr .= '<orderType>' . $order['order_type'] . '</orderType>';
                            $transWhRequestStr .= '<transferTime>' . date("Y-m-d H:i:s") . '</transferTime>';
                            $transWhRequestStr .= '</request>';

                            $params = array(
                                'method' => 'taobao.qimen.transwarehouse.report',
                                'customerid' => $order['customer_id'],
                                'appkey' => $customerInfo['wms_app_key'],
                                'timestamp' => date('Y-m-d H:i:s'),
                            );
                            $params['data'] = $transWhRequestStr;
                            $params['sign'] = strtoupper(base64_encode(md5($customerInfo['wms_secret'] . $transWhRequestStr . $customerInfo['wms_secret'])));

                            $transResponse = $httpObj->post('http://127.0.0.1/oms/api.php', $params);

                            if (empty($transResponse) && $num == 1) {
                                die(json_encode(array(
                                    'rows' => array(),
                                    'status' => 'error',
                                    'msg' => '转仓成功，告知ERP转仓失败！'
                                )));
                            } else {
                                if ($response == -3) {
                                    die(json_encode(array(
                                        'rows' => array(),
                                        'status' => 'error',
                                        'msg' => '转仓通知请求超时！'
                                    )));
                                }
                                $xmlObj = new xml();
                                $responseTransArr = $xmlObj->xmlStr2array($transResponse);
                                if ($responseTransArr['flag'] == 'failure') {
                                    if ($num == 1) {
                                        die(json_encode(array(
                                            'rows' => array(),
                                            'status' => 'error',
                                            'msg' => '转仓成功，' . $responseTransArr['message']
                                        )));
                                    }
                                }
                            }
                            $succ_i++;
                        } else {
                            $reSelectSql = "SELECT
                                			MAX(order_id) order_id
                                		FROM
                                			t_delivery_order_shortage " . $whereConditions;
                            $reSelectCommand = $connection->createCommand($reSelectSql);
                            $reSelectData = $reSelectCommand->queryRow();

                            $reUpdateSql = "UPDATE t_delivery_order_shortage
                                            SET done_flag=0
                                            WHERE order_id='" . $reSelectData['order_id'] . "'";
                            $reUpdateCommand = $connection->createCommand($reUpdateSql);
                            $reUpdateCommand->execute();
                            if ($num == 1) {
                                die(json_encode(array(
                                    'rows' => array(),
                                    'status' => 'error',
                                    'msg' => $responseArr['message']
                                )));
                            } else {
                                continue;
                            }
                        }
                    } else {
                        $reSelectSql = "SELECT
                                			MAX(order_id) order_id
                                		FROM
                                			t_delivery_order_shortage " . $whereConditions;
                        $reSelectCommand = $connection->createCommand($reSelectSql);
                        $reSelectData = $reSelectCommand->queryRow();

                        $reUpdateSql = "UPDATE t_delivery_order_shortage
                                        SET done_flag=0
                                        WHERE order_id='" . $reSelectData['order_id'] . "'";
                        $reUpdateCommand = $connection->createCommand($reUpdateSql);
                        $reUpdateCommand->execute();
                        if ($num == 1) {
                            die(json_encode(array(
                                'rows' => array(),
                                'status' => 'error',
                                'msg' => '下单接口无返回，查看接口是否访问正常！'
                            )));
                        } else {
                            continue;
                        }
                    }
                    if ($orderCreateFlag == 0) {
                        $reSelectSql = "SELECT
                                			MAX(order_id) order_id
                            		FROM
                            			t_delivery_order_shortage" . $whereConditions;
                        $reSelectCommand = $connection->createCommand($reSelectSql);
                        $reSelectData = $reSelectCommand->queryRow();

                        $reUpdateSql = "UPDATE t_delivery_order_shortage
                                    SET done_flag=0 
                                    WHERE order_id='" . $reSelectData['order_id'] . "'";
                        $reUpdateCommand = $connection->createCommand($reUpdateSql);
                        $reUpdateCommand->execute();
                    }
                    unset($responseArr['flag']);
                    unset($requestParams['data']);
                    unset($requestParams['sign']);
                    unset($requestParams['method']);
                }
            } else {
                if ($num == 1) {
                    die(json_encode(array(
                        'rows' => array(),
                        'status' => 'error',
                        'msg' => '订单类型不存在！'
                    )));
                }
            }
        }

        die(json_encode(array(
            'rows' => array(),
            'status' => 'error',
            'msg' => '成功处理' . $succ_i . '条订单！'
        )));
    }

    /**
     * 生成签名的方法
     * @param  请求参数 $params
     * @param  应用秘钥 $appSecret
     * @param  业务级参数 $data
     * @return 签名
     */
    public function makeSign($params, $appSecret, $data)
    {
        ksort($params);
        $str = $appSecret;
        foreach ($params as $key => $val) {
            $str .= $key . $val;
        }
        $str .= $data . $appSecret;

        $sign = strtoupper(md5($str));
        return $sign;
    }

    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = DeliveryOrderShortage::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}