<?php

/**
 * API接口函数
 * @author 独孤羽<123517746@qq.com>
 * @date 2015.04.24
 */
class func
{

    /**
     * 获取客户、仓库、ERP、WMS关系
     * @param String $customerId
     */
    public function getPartnerInfo($customerId)
    {
        global $db;
        $sql = " SELECT cus.customer_id,cus.app_secret,re.erp_api_url AS customer_erp_api,re.wms_api_url AS customer_wms_api,re.erp_api_ver AS customer_erp_api_ver,re.wms_api_ver AS customer_wms_api_ver,re.erp_code,re.wms_code FROM t_bind_relation as re "
            . " LEFT JOIN t_base_customer as cus ON re.customer_id=cus.customer_id "
            . " WHERE re.customer_id=:customer_id AND re.is_valid=1 AND cus.active_flag='Y' AND cus.is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':customer_id', $customerId);
        $model->execute();
        $customerInfo = $model->fetch(PDO::FETCH_ASSOC);

        $customer = array();
        $customer['customer_id'] = $customerInfo['customer_id'];
        $customer['erp_bn'] = $customerInfo['erp_code'];
        $customer['erp_api'] = $customerInfo['customer_erp_api'] ?: $customerInfo['erp_api'];
        $customer['erp_api_ver'] = $customerInfo['customer_erp_api_ver'] ?: '1.0';
        $customer['erp_api_secret'] = $customerInfo['app_secret'];
        $customer['wms_bn'] = $customerInfo['wms_code'];
        $customer['wms_api'] = $customerInfo['customer_wms_api'] ?: $customerInfo['wms_api'];
        $customer['wms_api_ver'] = $customerInfo['customer_wms_api_ver'] ?: '1.0';
        return $customer;
    }

    /**
     * 验证客户接口权限
     * @param String $customerId
     * @param String $method
     * @param String $from 请求方
     * @param String $to 目的地
     * @return bool
     */
    public function checkRole($customerId, $method)
    {
        //此处要读取客户是否拥有当前接口权限,因表结构还没有建立，所以没写

        return true;
    }
    /***
     * Notes:接口日志记录
     * Date: 2019/6/19
     * Time: 22:06
     * @param $msgId 日志ID
     * @param $customerId 货主编码
     * @param $requestUrl 请求url
     * @param $requestMethod 请求方法
     * @param $requestParams 请求参数
     * @param $msg 信息
     * @param int $parentMsgId 父日志ID
     * @return mixed
     */
    public function writeLog($msgId, $customerId, $requestUrl, $requestMethod, $requestParams, $msg, $parentMsgId = 0)
    {
        try {
            $insert_arr = array(
                'api_log_id' => $msgId,
                'parent_log_id' => $parentMsgId,
                'api_func' => $requestMethod,
                'customer_id' => $customerId,
                'api_url' => $requestUrl,
                'required_parameter' => $requestParams ? serialize($requestParams) : '',
                'return_msg' => $msg,
                'ip' => $_SERVER['REMOTE_ADDR'],
                'create_time' => date('Y-m-d H:i:s'),
            );
            $res= OmsDatabase::$oms_db->insert('t_api_log', $insert_arr);
            if ($res) {
                return $msgId;
            }
        } catch (Exception $e) {
            $this->errorLog($customerId, $requestUrl, $requestMethod, $requestParams, $msg);
        }
    }

    /**
     * 错误日志记录
     * @param $customerId
     * @param $requestUrl
     * @param $requestMethod
     * @param $requestParams
     * @param $msg
     * @param $msgId
     */
    public function errorLog($customerId, $requestUrl, $requestMethod, $requestParams, $msg = '')
    {
        file_put_contents(LOG_PATH . '/errorLog_' . $customerId . '.log', ' 日期:' . date('Y-m-d H:i:s:') . PHP_EOL . ' API: ' . $requestUrl . '  Method：' . $requestMethod . PHP_EOL . '  errorMsg:' . $msg . PHP_EOL . ' queryParmas:' . print_r($requestParams, 1));
    }

    /**
     * 生成日志ID
     * @return string
     */
    public function makeMsgId()
    {
        $microtime = util::microtime();
        $unique_key = str_replace('.', '', strval($microtime));
        $randval = uniqid('', true);
        $unique_key .= strval($randval);
        return md5($unique_key);
    }

    /**
     * 判断队列是否启动
     */
    public function pingQueue()
    {
        require_once API_ROOT . '/ext/queue.php';
        global $redisApiParam;
        $queueServer = $redisApiParam['queue_server'];
        $queueConfig = $redisApiParam[$queueServer];
        $queueObj = queue::instance($queueServer, $queueConfig);
        return $queueObj->ping();
    }

    /**
     * 入队列：异步接口使用
     * @param $taskName 任务名称
     * @param $taskParams 任务参数
     * @return bool
     */
    public function pushQueue($taskName, $taskParams)
    {
        //写入队列
        require_once API_ROOT . '/ext/queue.php';
        global $redisApiParam;
        $queueServer = $redisApiParam['queue_server'];
        $queueConfig = $redisApiParam[$queueServer];
        $queueObj = queue::instance($queueServer, $queueConfig);
        return $queueObj->push($taskName, json_encode($taskParams));
    }

    /**
     * 出队列：异步接口使用
     * @param $taskName 任务名称
     * @return bool
     */
    public function popQueue($taskName)
    {
        require_once API_ROOT . '/ext/queue.php';
        global $redisApiParam;
        $queueServer = $redisApiParam['queue_server'];
        $queueConfig = $redisApiParam[$queueServer];
        $queueObj = queue::instance($queueServer, $queueConfig);
        $queueData = $queueObj->pop($taskName);
        if ($queueData) {
            $queueData = json_decode($queueData);
        }
        return $queueData;
    }

    /**
     * 记录订单接口日志
     * @param $method  接口方法
     * @param $sourceParam  推送的原始xml数据
     * @param $requestParam  货主ID对应的订单数据
     * @param $errorParam  接口校验生成的错误数据
     * @param $resultParam  接口组合后的返回数据
     * @param $responseParam  ERP或WMS返回的数据
     * @param $msgIdArr  接口主msgid数组
     * @param $urlArr  ERP或WMS的接口地址数组
     */
    public function writeInterfaceLog($method, $sourceParam, $requestParam, $errorParam, $resultParam, $responseParam, $msgIdArr, $urlArr)
    {
        $orderArr = array(); //订单数组
        $errorOrderArr = array();
        $skuArr = array(); //SKU数组
        $errorSkuArr = array();
        $customerArr = array();  //客商档案数组
        $errorCustomerArr = array();
        $dataArr = array();
        //数据处理
        if (!empty($requestParam)) {
            global $db;
            foreach ($requestParam as $customerid => $data) {
                //统一数据格式
                if (!empty($data['header'])) {
                    $dataArr = $data['header'];
                } elseif (!empty($data['data']['orderinfo'])) {
                    $dataArr = $data['data']['orderinfo'];
                } elseif (!empty($data['data']['header'])) {
                    $dataArr = $data['data']['header'];
                } elseif (!empty($data['data']['ordernos'])) {
                    $dataArr = $data['data']['ordernos'];
                }
                if (!empty($dataArr) && empty($dataArr[0])) {
                    $dataArr = array($dataArr);
                }
                //获取请求参数中的数据
                if (!empty($dataArr)) {
                    if (in_array($method, array('putCustData'))) {
                        foreach ($dataArr as $key => $val) {
                            $customerArr[$key]['customerId'] = $val['CustomerID'];
                            $customerArr[$key]['customerType'] = $val['Customer_Type'];
                        }
                    } elseif (in_array($method, array('putSKUData'))) {
                        foreach ($dataArr as $key => $val) {
                            $skuArr[$key]['sku'] = $val['SKU'];
                        }
                    } elseif (in_array($method, array('putASNData', 'cancelASNData', 'putSOData', 'cancelSOData', 'confirmSOStatus'))) {
                        foreach ($dataArr as $key => $val) {
                            $orderArr[$key]['orderNo'] = $val['OrderNo'];
                            $orderArr[$key]['orderType'] = $val['OrderType'];
                            $orderArr[$key]['warehouseID'] = $val['WarehouseID'];
                        }
                    } elseif (in_array($method, array('confirmASNData', 'confirmSOData'))) {
                        foreach ($dataArr as $key => $val) {
                            $orderArr[$key]['orderNo'] = empty($val['OMSOrderNo']) ? $val['WMSOrderNo'] : $val['OMSOrderNo'];
                            $orderArr[$key]['orderType'] = $val['OrderType'];
                            $orderArr[$key]['warehouseID'] = $val['WarehouseID'];
                        }
                    } elseif (in_array($method, array('inventoryReport'))) {
                        foreach ($dataArr as $key => $val) {
                            $orderArr[$key]['orderNo'] = $val['checkOrderCode'];
                            $orderArr[$key]['orderType'] = '';
                            $orderArr[$key]['warehouseID'] = $val['WarehouseID'];
                        }
                    }
                }
                //解析发送接口之前的错误信息
                $n = 0;
                if (!empty($errorParam[$customerid])) {
                    $xmlStr = '';
                    $tmpArr = array();
                    if (!empty($errorParam[$customerid]['resultInfo'])) {
                        $xmlStr = '<resultInfo>' . $errorParam[$customerid]['resultInfo'] . '</resultInfo>';
                        $xmlObj = new xml();
                        $tmpArr = $xmlObj->xmlStr2array($xmlStr);
                        if (!empty($tmpArr)) {
                            if (empty($tmpArr[0])) {
                                $tmpArr = array($tmpArr);
                            }
                            foreach ($tmpArr as $v) {
                                if (in_array($method, array('putCustData'))) {
                                    $errorCustomerArr[$n]['customerId'] = $v['CustomerID'];
                                    $errorCustomerArr[$n]['customerType'] = $v['Customer_Type'];
                                    $n++;
                                } elseif (in_array($method, array('putSKUData'))) {
                                    $errorSkuArr[$n]['sku'] = $v['SKU'];
                                    $n++;
                                } elseif (in_array($method, array('putASNData', 'cancelASNData', 'putSOData', 'cancelSOData', 'confirmSOStatus', 'confirmASNData', 'confirmSOData'))) {
                                    $errorOrderArr[$n]['orderNo'] = $v['OrderNo'];
                                    $errorOrderArr[$n]['orderType'] = $v['OrderType'];
                                    $errorOrderArr[$n]['warehouseID'] = $v['WarehouseID'];
                                    $n++;
                                }
                            }
                        }
                    }
                }
                //解析接口返回状态中错误的数据
                if (!empty($resultParam[$customerid])) {
                    $xmlStr = '';
                    $tmpArr = array();
                    if (!empty($resultParam[$customerid]['resultInfo'])) {
                        $xmlStr = '<resultInfo>' . $resultParam[$customerid]['resultInfo'] . '</resultInfo>';
                        $xmlObj = new xml();
                        $tmpArr = $xmlObj->xmlStr2array($xmlStr);
                        if (!empty($tmpArr)) {
                            if (empty($tmpArr[0])) {
                                $tmpArr = array($tmpArr);
                            }
                            foreach ($tmpArr as $v) {
                                if (in_array($method, array('putCustData'))) {   //客商档案数据处理
                                    $errorCustomerArr[$n]['customerId'] = $v['CustomerID'];
                                    $errorCustomerArr[$n]['customerType'] = $v['Customer_Type'];
                                    $n++;
                                } elseif (in_array($method, array('putSKUData'))) {   //商品资料数据处理
                                    $errorSkuArr[$n]['sku'] = $v['SKU'];
                                    $n++;
                                } elseif (in_array($method, array('putASNData', 'cancelASNData', 'putSOData', 'cancelSOData', 'confirmSOStatus', 'confirmASNData', 'confirmSOData'))) {   //订单数据处理
                                    $errorOrderArr[$n]['orderNo'] = $v['OrderNo'];
                                    $errorOrderArr[$n]['orderType'] = $v['OrderType'];
                                    $errorOrderArr[$n]['warehouseID'] = $v['WarehouseID'];
                                    $n++;
                                }
                            }
                        }
                    }
                }
                //判断成功与失败状态
                if (!empty($resultParam[$customerid]) && in_array($resultParam[$customerid]['returnFlag'], array(1, 2))) {
                    //客商档案数据处理
                    if (!empty($customerArr)) {
                        foreach ($customerArr as $key => $val) {
                            $m = 0;
                            if (!empty($errorCustomerArr)) {
                                foreach ($errorCustomerArr as $v) {
                                    if ($val['customerId'] == $v['customerId'] && $val['customerType'] == $v['customerType']) {
                                        $customerArr[$key]['status'] = 0;
                                        $m++;
                                    }
                                }
                            }
                            if ($m == 0) {
                                $customerArr[$key]['status'] = 1;
                            }
                        }
                    }
                    //商品资料数据处理
                    if (!empty($skuArr)) {
                        foreach ($skuArr as $key => $val) {
                            $m = 0;
                            if (!empty($errorSkuArr)) {
                                foreach ($errorSkuArr as $v) {
                                    if ($val['sku'] == $v['sku']) {
                                        $skuArr[$key]['status'] = 0;
                                        $m++;
                                    }
                                }
                            }
                            if ($m == 0) {
                                $skuArr[$key]['status'] = 1;
                            }
                        }
                    }
                    //订单数据处理
                    if (!empty($orderArr)) {
                        foreach ($orderArr as $key => $val) {
                            $m = 0;
                            if (!empty($errorOrderArr)) {
                                foreach ($errorOrderArr as $v) {
                                    if ($val['orderNo'] == $v['orderNo'] && $val['orderType'] == $v['orderType']) {
                                        $orderArr[$key]['status'] = 0;
                                        $m++;
                                    }
                                }
                            }
                            if ($m == 0) {
                                $orderArr[$key]['status'] = 1;
                            }
                        }
                    }
                } else {
                    //客商档案数据处理
                    if (!empty($customerArr)) {
                        foreach ($customerArr as $key => $val) {
                            $customerArr[$key]['status'] = 0;
                        }
                    }
                    //商品资料数据处理
                    if (!empty($skuArr)) {
                        foreach ($skuArr as $key => $val) {
                            $skuArr[$key]['status'] = 0;
                        }
                    }
                    //订单数据处理
                    if (!empty($orderArr)) {
                        foreach ($orderArr as $key => $val) {
                            $orderArr[$key]['status'] = 0;
                        }
                    }
                }
                //写入订单接口操作日志数据
                if (!empty($orderArr)) {
                    $sql = "INSERT INTO t_order_interface_log(order_no, order_type, customer_id, warehouse_code, method, msg_id, api_url, request_param, filter_result, response_param, return_status, create_time) VALUES(:order_no, :order_type, :customer_id, :warehouse_code, :method, :msg_id, :api_url, :request_param, :filter_result, :response_param, :return_status, now())";
                    $Command = $db->prepare($sql);
                    foreach ($orderArr as $val) {
                        if ($val['status'] == 1) {
                            $filterStr = '';
                        } else {
                            $filterStr = '<resultInfo>' . $resultParam[$customerid]['resultInfo'] . '</resultInfo>';
                        }
                        $Command->bindParam(':order_no', $val['orderNo']);
                        $Command->bindParam(':order_type', $val['orderType']);
                        $Command->bindParam(':customer_id', $customerid);
                        $Command->bindParam(':warehouse_code', $val['warehouseID']);
                        $Command->bindParam(':method', $method);
                        $Command->bindParam(':msg_id', $msgIdArr[$customerid]);
                        $Command->bindParam(':api_url', $urlArr[$customerid]);
                        $Command->bindParam(':request_param', $sourceParam);
                        $Command->bindParam(':filter_result', $filterStr);
                        $Command->bindParam(':response_param', $responseParam[$customerid]);
                        $Command->bindParam(':return_status', $val['status']);
                        $Command->execute();
                    }
                }
                //写入商品资料接口日志
                if (!empty($skuArr)) {
                    $sql = "INSERT INTO t_product_interface_log(sku, customer_id, method, msg_id, api_url, request_param, filter_result, response_param, return_status, create_time) VALUES(:sku, :customer_id, :method, :msg_id, :api_url, :request_param, :filter_result, :response_param, :return_status, now())";
                    $Command = $db->prepare($sql);
                    foreach ($skuArr as $val) {
                        if ($val['status'] == 1) {
                            $filterStr = '';
                        } else {
                            $filterStr = '<resultInfo>' . $resultParam[$customerid]['resultInfo'] . '</resultInfo>';
                        }
                        $Command->bindParam(':sku', $val['sku']);
                        $Command->bindParam(':customer_id', $customerid);
                        $Command->bindParam(':method', $method);
                        $Command->bindParam(':msg_id', $msgIdArr[$customerid]);
                        $Command->bindParam(':api_url', $urlArr[$customerid]);
                        $Command->bindParam(':request_param', $sourceParam);
                        $Command->bindParam(':filter_result', $filterStr);
                        $Command->bindParam(':response_param', $responseParam[$customerid]);
                        $Command->bindParam(':return_status', $val['status']);
                        $Command->execute();
                    }
                }
                //写入客商档案接口日志
                if (!empty($customerArr)) {
                    $sql = "INSERT INTO t_customer_interface_log(customer_id, customer_type, method, msg_id, api_url, request_param, filter_result, response_param, return_status, create_time) VALUES(:customer_id, :customer_type, :method, :msg_id, :api_url, :request_param, :filter_result, :response_param, :return_status, now())";
                    $Command = $db->prepare($sql);
                    foreach ($customerArr as $val) {
                        if ($val['status'] == 1) {
                            $filterStr = '';
                        } else {
                            $filterStr = '<resultInfo>' . $resultParam[$customerid]['resultInfo'] . '</resultInfo>';
                        }
                        $Command->bindParam(':customer_id', $val['customerId']);
                        $Command->bindParam(':customer_type', $val['customerType']);
                        $Command->bindParam(':method', $method);
                        $Command->bindParam(':msg_id', $msgIdArr[$customerid]);
                        $Command->bindParam(':api_url', $urlArr[$customerid]);
                        $Command->bindParam(':request_param', $sourceParam);
                        $Command->bindParam(':filter_result', $filterStr);
                        $Command->bindParam(':response_param', $responseParam[$customerid]);
                        $Command->bindParam(':return_status', $val['status']);
                        $Command->execute();
                    }
                }
            }
        }
    }

    /**
     * 记录奇门接口日志
     * @param $method 接口方法
     * @param $sourceData 请求的xml数据
     * @param $requestData 请求的xml转换后的数组数据
     * @param $responseParam ERP或WMS返回的数据
     * @param $msgId 接口主msgid
     * @param $toApiUrl 请求的ERP或WMS接口地址
     * @param $status 接口返回状态
     */
    public function writeQimenInterfaceLog($method, $sourceData, $errorMsg, $requestData, $responseParam, $msgId, $toApiUrl, $status)
    {
        $skuArr = array();
        $itemsArr = array();
        $orderArr = array();
        $batchOrderArr = array();
        $inventoryArr = array();
        $customerInfoArr = array();
        $deliveryOrderWaveArr = array();
        $deliveryBatchCreateAnswerArr = array();
        //单一商品method数组
        $methodSkuArr = array(
            'taobao.qimen.singleitem.synchronize', 'singleitem.synchronize'

        );
        //组合商品method数组
        $methodCombineSkuArr = array(
            'taobao.qimen.combineitem.synchronize', 'combineitem.synchronize'
        );
        //入库单method数组
        $methodEntryOrderArr = array(
            'taobao.qimen.entryorder.create', 'entryorder.create',
            'taobao.qimen.entryorder.confirm', 'entryorder.confirm'
        );
        //退货入库单method数组
        $methodReturnOrderArr = array(
            'taobao.qimen.returnorder.create', 'returnorder.create',
            'taobao.qimen.returnorder.confirm', 'returnorder.confirm'
        );
        //出库单、发货单method数组
        $methodDeliveryOrderArr = array(
            'taobao.qimen.stockout.create', 'stockout.create',
            'taobao.qimen.stockout.confirm', 'stockout.confirm',
            'taobao.qimen.deliveryorder.create', 'deliveryorder.create',
            'taobao.qimen.deliveryorder.confirm', 'deliveryorder.confirm',
            'taobao.qimen.deliveryorder.shortage', 'deliveryorder.shortage',
        );
        //订单流水method数组
        $methodOrderProcessArr = array(
            'taobao.qimen.orderprocess.report', 'orderprocess.report'
        );
        //发货单sn通知method数组
        $methodSnReportArr = array(
            'taobao.qimen.sn.report', 'sn.report'
        );
        //发货单缺货通知method数组
        $methodItemLackArr = array(
            'taobao.qimen.itemlack.report', 'itemlack.report'
        );
        //单据取消method数组
        $methodCancelOrderArr = array(
            'taobao.qimen.order.cancel', 'order.cancel'
        );
        //库存盘点method数组
        $methodInventoryReportArr = array(
            'taobao.qimen.inventory.report', 'inventory.report'
        );
        //仓内加工单method数组
        $methodStoreProcessOrderArr = array(
            'taobao.qimen.storeprocess.create', 'storeprocess.create',
            'taobao.qimen.storeprocess.confirm', 'storeprocess.confirm'
        );
        //库存异动method数组
        $methodInventoryChangeArr = array(
            'taobao.qimen.stockchange.report', 'stockchange.report'
        );
        //批量接口
        //批量商品method数组
        $methodItemsArr = array(
            'taobao.qimen.items.synchronize', 'items.synchronize'
        );
        //批量发货单创建，发货单确认接口method数组
        $methodDeliveryBatchCreate = array(
            'taobao.qimen.deliveryorder.batchcreate', 'deliveryorder.batchcreate',
            'taobao.qimen.deliveryorder.batchconfirm', 'deliveryorder.batchconfirm'
        );
        //仓库注册接口，仓库更新接口，仓库查询接口，用户注册接口
        $methodWarehouseReg = array(
            'taobao.qimen.warehouse.reg', 'taobao.qimen.warehouse.update'
        );
        //用户注册接口
        $methodCustomerReg = array(
            'taobao.qimen.customer.reg'
        );
        //店铺同步接口
        $methodShopSynchronize = array(
            'taobao.qimen.shop.synchronize', 'shop.synchronize'
        );
        //单据挂起，恢复接口
        $methodOrderPending = array(
            'taobao.qimen.order.pending', 'order.pending'
        );
        //发货单波次通知接口
        $methodWaveNum = array(
            'taobao.qimen.wavenum.report', 'wavenum.report'
        );
        //发货单创建结果通知接口 （批量）
        $methodBatchCreateAnswer = array(
            'taobao.qimen.deliveryorder.batchcreate.answer', 'deliveryorder.batchcreate.answer'
        );
        //家乐福接口
        $methodJlf = array(
            'cf.deliveryorder.picked.confirm', 'cf.inventory.sync'
        );
        //请求数据处理
        if (!empty($requestData)) {
            global $db;

            $xmlObj = new xml();
            $returnMsgArr = $xmlObj->xmlStr2array($responseParam);

            if (in_array($method, $methodSkuArr)) {    //单一商品数据处理
                $skuArr['sku'] = $requestData['item']['itemCode'];
                $skuArr['customer_id'] = $requestData['ownerCode'];
            } elseif (in_array($method, $methodCombineSkuArr)) {  //组合商品数据处理
                $skuArr['sku'] = $requestData['itemCode'];
                $skuArr['customer_id'] = $requestData['ownerCode'];
            } elseif (in_array($method, $methodEntryOrderArr)) {  //入库单数据处理
                $orderArr['order_no'] = $requestData['entryOrder']['entryOrderCode'];
                $orderArr['order_type'] = empty($requestData['entryOrder']['orderType']) ? (empty($requestData['entryOrder']['entryOrderType']) ?: $requestData['entryOrder']['entryOrderType']) : $requestData['entryOrder']['orderType'];
                if (!empty($requestData['orderLines']['orderLine'])) {
                    if (empty($requestData['orderLines']['orderLine'][0])) {
                        $requestData['orderLines']['orderLine'] = array($requestData['orderLines']['orderLine']);
                    }
                    $orderArr['customer_id'] = $requestData['orderLines']['orderLine'][0]['ownerCode'];
                } else {
                    $orderArr['customer_id'] = qimen_service::$_customerId;
                }
                $orderArr['warehouse_code'] = $requestData['entryOrder']['warehouseCode'];
            } elseif (in_array($method, $methodReturnOrderArr)) {  //退货入库单数据处理
                $orderArr['order_no'] = $requestData['returnOrder']['returnOrderCode'];
                $orderArr['order_type'] = $requestData['returnOrder']['orderType'];
                if (!empty($requestData['orderLines']['orderLine'])) {
                    if (empty($requestData['orderLines']['orderLine'][0])) {
                        $requestData['orderLines']['orderLine'] = array($requestData['orderLines']['orderLine']);
                    }
                    $orderArr['customer_id'] = $requestData['orderLines']['orderLine'][0]['ownerCode'];
                } else {
                    $orderArr['customer_id'] = qimen_service::$_customerId;
                }
                $orderArr['warehouse_code'] = $requestData['returnOrder']['warehouseCode'];
            } elseif (in_array($method, $methodDeliveryOrderArr)) {  //出库单、发货单数据处理
                $orderArr['order_no'] = $requestData['deliveryOrder']['deliveryOrderCode'];
                $orderArr['order_type'] = $requestData['deliveryOrder']['orderType'];
                if (!empty($requestData['orderLines']['orderLine'])) {
                    if (empty($requestData['orderLines']['orderLine'][0])) {
                        $requestData['orderLines']['orderLine'] = array($requestData['orderLines']['orderLine']);
                    }
                    $orderArr['customer_id'] = !empty($requestData['orderLines']['orderLine'][0]['ownerCode']) ? $requestData['orderLines']['orderLine'][0]['ownerCode'] : qimen_service::$_customerId;
                } else {
                    $orderArr['customer_id'] = qimen_service::$_customerId;
                }
                $orderArr['warehouse_code'] = $requestData['deliveryOrder']['warehouseCode'];
            } elseif (in_array($method, $methodOrderProcessArr)) {  //订单流水数据处理
                $orderArr['order_no'] = $requestData['order']['orderCode'];
                $orderArr['order_type'] = $requestData['order']['orderType'];
                $orderArr['customer_id'] = qimen_service::$_customerId;
                $orderArr['warehouse_code'] = $requestData['order']['warehouseCode'];
            } elseif (in_array($method, $methodSnReportArr)) {  //发货单sn通知数据处理
                $orderArr['order_no'] = $requestData['deliveryOrder']['deliveryOrderCode'];
                $orderArr['order_type'] = $requestData['deliveryOrder']['orderType'];
                $orderArr['customer_id'] = empty($requestData['deliveryOrder']['ownerCode']) ? qimen_service::$_customerId : $requestData['deliveryOrder']['ownerCode'];
                $orderArr['warehouse_code'] = $requestData['deliveryOrder']['warehouseCode'];
            } elseif (in_array($method, $methodItemLackArr)) {  //发货单缺货通知数据处理
                $orderArr['order_no'] = $requestData['deliveryOrder']['deliveryOrderCode'];
                $orderArr['order_type'] = '';
                $orderArr['customer_id'] = qimen_service::$_customerId;
                $orderArr['warehouse_code'] = $requestData['deliveryOrder']['warehouseCode'];
            } elseif (in_array($method, $methodCancelOrderArr)) {  //单据取消数据处理
                $orderArr['order_no'] = $requestData['orderCode'];
                $orderArr['order_type'] = $requestData['orderType'];
                $orderArr['customer_id'] = $requestData['ownerCode'];
                $orderArr['warehouse_code'] = empty($requestData['warehouseCode']) ? '' : $requestData['warehouseCode'];
            } elseif (in_array($method, $methodInventoryReportArr)) {  //库存盘点数据处理
                $orderArr['order_no'] = $requestData['checkOrderCode'];
                $orderArr['order_type'] = '';
                $orderArr['customer_id'] = $requestData['ownerCode'];
                $orderArr['warehouse_code'] = $requestData['warehouseCode'];
            } elseif (in_array($method, $methodStoreProcessOrderArr)) {  //仓内加工单数据处理
                $orderArr['order_no'] = $requestData['processOrderCode'];
                $orderArr['order_type'] = $requestData['orderType'];
                $orderArr['customer_id'] = qimen_service::$_customerId;
                $orderArr['warehouse_code'] = $requestData['warehouseCode'];
            } elseif (in_array($method, $methodInventoryChangeArr)) {  //库存异动数据处理
                foreach ($requestData['items']['item'] as $k => $v) {
                    $inventoryArr[$k]['order_no'] = $v['orderCode'];
                    $inventoryArr[$k]['order_type'] = empty($v['orderType']) ? '' : $v['orderType'];
                    $inventoryArr[$k]['customer_id'] = $v['ownerCode'];
                    $inventoryArr[$k]['warehouse_code'] = $v['warehouseCode'];
                }
            } elseif (in_array($method, $methodItemsArr)) {  //商品同步(批量)接口
                if (!empty($requestData['items']['item']) && empty($requestData['items']['item'][0])) {
                    $requestData['items']['item'] = array($requestData['items']['item']);
                }
                foreach ($requestData['items']['item'] as $k => $v) {
                    if ($status == 'success' || $status == 1) {
                        $itemsArr[$k]['status'] = 1;
                    } else {
                        if (!empty($returnMsgArr['items']['item'])) {
                            if (empty($returnMsgArr['items']['item'][0])) {
                                $returnMsgArr['items']['item'] = array($returnMsgArr['items']['item']);
                            }
                            $itemsArr[$k]['status'] = 1;
                            foreach ($returnMsgArr['items']['item'] as $i_v) {
                                if ($v['itemCode'] == $i_v['itemCode']) {
                                    $itemsArr[$k]['status'] = 0;
                                }
                            }
                        } else {
                            $itemsArr[$k]['status'] = 0;
                        }
                    }
                    $itemsArr[$k]['sku'] = $v['itemCode'];
                    $itemsArr[$k]['customer_id'] = $requestData['ownerCode'];
                }
            } elseif (in_array($method, $methodDeliveryBatchCreate)) {  //发货单创建,确认(批量)接口
                if (!empty($requestData['orders']['order']) && empty($requestData['orders']['order'][0])) {
                    $requestData['orders']['order'] = array($requestData['orders']['order']);
                }
                if (!empty($requestData['orderLines']['orderLine']) && empty($requestData['orderLines']['orderLine'][0])) {
                    $requestData['orderLines']['orderLine'] = array($requestData['orderLines']['orderLine']);
                }
                foreach ($requestData['orders']['order'] as $k => $v) {
                    if ($status == 'success' || $status == 1) {
                        $batchOrderArr[$k]['status'] = 1;
                    } else {
                        if (!empty($returnMsgArr['orders']['order'])) {
                            $batchOrderArr[$k]['status'] = 1;
                            if (empty($returnMsgArr['orders']['order'][0])) {
                                $returnMsgArr['orders']['order'] = array($returnMsgArr['orders']['order']);
                            }
                            foreach ($returnMsgArr['orders']['order'] as $o_v) {
                                if ($v['deliveryOrder']['deliveryOrderCode'] == $o_v['deliveryOrderCode']) {
                                    $batchOrderArr[$k]['status'] = 0;
                                }
                            }
                        } else {
                            $batchOrderArr[$k]['status'] = 0;
                        }
                    }
                    $batchOrderArr[$k]['order_no'] = $v['deliveryOrder']['deliveryOrderCode'];
                    $batchOrderArr[$k]['order_type'] = $v['deliveryOrder']['orderType'];
                    if (!empty($v['orderLines']['orderLine'])) {
                        if (empty($v['orderLines']['orderLine'][0])) {
                            $v['orderLines']['orderLine'] = array($v['orderLines']['orderLine']);
                        }
                        $batchOrderArr[$k]['customer_id'] = $v['orderLines']['orderLine'][0]['ownerCode'];
                    } else {
                        $batchOrderArr[$k]['customer_id'] = qimen_service::$_customerId;
                    }
                    $batchOrderArr[$k]['warehouse_code'] = $v['deliveryOrder']['warehouseCode'];
                }
            } elseif (in_array($method, $methodWarehouseReg)) {//奇门仓库相关操作接口
                $customerInfoArr['customer_id'] = empty($requestData['warehouseCode']) ? (empty($requestData['qmWarehouseCode']) ? '' : $requestData['qmWarehouseCode']) : $requestData['warehouseCode'];
                $customerInfoArr['customer_type'] = 'WH';
            } elseif (in_array($method, $methodWaveNum)) {  //奇门发货单波次通知接口
                if (!empty($requestData['orders']['order']) && empty($requestData['orders']['order'][0])) {
                    $requestData['orders']['order'] = array($requestData['orders']['order']);
                }
                foreach ($requestData['orders']['order'] as $k => $v) {
                    $deliveryOrderWaveArr[$k]['order_no'] = $v['deliveryOrderCode'];
                    $deliveryOrderWaveArr[$k]['customer_id'] = qimen_service::$_customerId;
                }
            } elseif (in_array($method, $methodBatchCreateAnswer)) { //发货单创建结果通知接口 （批量）
                if (!empty($requestData['orders']['order']) && empty($requestData['orders']['order'][0])) {
                    $requestData['orders']['order'] = array($requestData['orders']['order']);
                }
                foreach ($requestData['orders']['order'] as $k => $v) {
                    $deliveryBatchCreateAnswerArr[$k]['order_no'] = $v['deliveryOrderCode'];
                    $deliveryBatchCreateAnswerArr[$k]['warehouse_code'] = $v['orderInfo']['warehouseCode'];
                }
            } elseif (in_array($method, $methodOrderPending)) {  //单据挂起（恢复接口）
                $orderArr['order_no'] = $requestData['orderCode'];
                $orderArr['order_type'] = empty($requestData['orderType']) ? '' : $requestData['orderType'];
                $orderArr['customer_id'] = empty($requestData['ownerCode']) ? '' : $requestData['ownerCode'];
                $orderArr['warehouse_code'] = $requestData['warehouseCode'];
            } elseif (in_array($method, $methodCustomerReg)) {  //用户注册接口
                $customerInfoArr['customer_id'] = $requestData['customerInfo']['customerid'];
                $customerInfoArr['customer_type'] = 'OW';
            } elseif (in_array($method, $methodShopSynchronize)) {  //店铺同步接口
                $customerInfoArr['customer_id'] = $requestData['shop']['shopCode'];
                $customerInfoArr['customer_type'] = 'OT';
            } elseif (in_array($method, $methodJlf)) {//家乐福接口
                $orderArr['order_no'] = $requestData['deliveryOrderCode'];
                $orderArr['order_type'] = '';
                $orderArr['customer_id'] = $requestData['ownerCode'];
                $orderArr['warehouse_code'] = $requestData['warehouseCode'];
            }
            //状态处理
            if ($status == 1 || $status == 'success') {
                $status = 1;
            } else {
                $status = 0;
            }
            //写入商品日志
            if (!empty($skuArr)) {
                $sql = "INSERT INTO t_product_interface_log(sku, customer_id, method, msg_id, api_url, request_param, response_param, return_status, create_time) VALUES(:sku, :customer_id, :method, :msg_id, :api_url, :request_param, :response_param, :return_status, now())";
                $Command = $db->prepare($sql);
                $Command->bindParam(':sku', $skuArr['sku']);
                $Command->bindParam(':customer_id', $skuArr['customer_id']);
                $Command->bindParam(':method', $method);
                $Command->bindParam(':msg_id', $msgId);
                $Command->bindParam(':api_url', $toApiUrl);
                $Command->bindParam(':request_param', $sourceData);
                $Command->bindParam(':response_param', $responseParam);
                $Command->bindParam(':return_status', $status);
                $Command->execute();
            }
            //写入订单日志
            if (!empty($orderArr)) {
                $sql = "INSERT INTO t_order_interface_log(order_no, order_type, customer_id, warehouse_code, method, msg_id, api_url, request_param, filter_result, response_param, return_status, create_time) VALUES(:order_no, :order_type, :customer_id, :warehouse_code, :method, :msg_id, :api_url, :request_param, :filter_result, :response_param, :return_status, now())";
                $Command = $db->prepare($sql);
                $Command->bindParam(':order_no', $orderArr['order_no']);
                $Command->bindParam(':order_type', $orderArr['order_type']);
                $Command->bindParam(':customer_id', $orderArr['customer_id']);
                $Command->bindParam(':warehouse_code', $orderArr['warehouse_code']);
                $Command->bindParam(':method', $method);
                $Command->bindParam(':msg_id', $msgId);
                $Command->bindParam(':api_url', $toApiUrl);
                $Command->bindParam(':request_param', $sourceData);
                $Command->bindParam(':filter_result', $errorMsg);
                $Command->bindParam(':response_param', $responseParam);
                $Command->bindParam(':return_status', $status);
                $Command->execute();
            }

            //单独将库存异动通知写入订单日志
            if (!empty($inventoryArr)) {
                global $db;
                foreach ($inventoryArr as $k => $v) {
                    $sql = "INSERT INTO t_order_interface_log(order_no, order_type, customer_id, warehouse_code, method, msg_id, api_url, request_param, filter_result, response_param, return_status, create_time) VALUES(:order_no, :order_type, :customer_id, :warehouse_code, :method, :msg_id, :api_url, :request_param, :filter_result, :response_param, :return_status, now())";
                    $Command = $db->prepare($sql);
                    $Command->bindParam(':order_no', $v['order_no']);
                    $Command->bindParam(':order_type', $v['order_type']);
                    $Command->bindParam(':customer_id', $v['customer_id']);
                    $Command->bindParam(':warehouse_code', $v['warehouse_code']);
                    $Command->bindParam(':method', $method);
                    $Command->bindParam(':msg_id', $msgId);
                    $Command->bindParam(':api_url', $toApiUrl);
                    $Command->bindParam(':request_param', $sourceData);
                    $Command->bindParam(':filter_result', $errorMsg);
                    $Command->bindParam(':response_param', $responseParam);
                    $Command->bindParam(':return_status', $status);
                    $Command->execute();
                }
            }

            //奇门商品批量同步接口
            if (!empty($itemsArr)) {
                foreach ($itemsArr as $a_v) {
                    $this->writeProductSql($a_v['sku'], $a_v['customer_id'], $method, $msgId, $toApiUrl, $sourceData, $errorMsg, $responseParam, $a_v['status']);
                }
            }

            //奇门发货单创建(确认)批量接口
            if (!empty($batchOrderArr)) {
                global $db;
                foreach ($batchOrderArr as $b_v) {
                    $this->writeOrderSql($b_v['order_no'], $b_v['order_type'], $b_v['customer_id'], $b_v['warehouse_code'], $method, $msgId, $toApiUrl, $sourceData, $errorMsg, $responseParam, $b_v['status']);
                }
            }

            //奇门发货单波次通知接口日志
            if (!empty($deliveryOrderWaveArr)) {
                foreach ($deliveryOrderWaveArr as $d_v) {
                    $this->writeOrderSql($d_v['order_no'], '', $d_v['customer_id'], '', $method, $msgId, $toApiUrl, $sourceData, $errorMsg, $responseParam, $status);
                }
            }

            //奇门发货单创建结果通知接口
            if (!empty($deliveryBatchCreateAnswerArr)) {
                foreach ($deliveryBatchCreateAnswerArr as $d_v) {
                    $this->writeOrderSql($d_v['order_no'], '', qimen_service::$_customerId, $d_v['warehouse_code'], $method, $msgId, $toApiUrl, $sourceData, $errorMsg, $responseParam, $status);
                }
            }

            //奇门仓库操作接口、用户注册接口、店铺同步接口日志记录
            if (!empty($customerInfoArr)) {
                global $db;
                $sql = "INSERT INTO t_customer_interface_log(customer_id,customer_type,method,msg_id,api_url,request_param,filter_result,response_param,return_status,create_time) VALUES(:customer_id,:customer_type,:method,:msg_id,:api_url,:request_param,:filter_result,:response_param,:return_status,now())";
                $Command = $db->prepare($sql);
                $Command->bindParam(':customer_id', $customerInfoArr['customer_id']);
                $Command->bindParam(':customer_type', $customerInfoArr['customer_type']);
                $Command->bindParam(':method', $method);
                $Command->bindParam(':msg_id', $msgId);
                $Command->bindParam(':api_url', $toApiUrl);
                $Command->bindParam(':request_param', $sourceData);
                $Command->bindParam(':filter_result', $errorMsg);
                $Command->bindParam(':response_param', $responseParam);
                $Command->bindParam(':return_status', $status);
                $Command->execute();
            }

        }
    }

    /**
     * 批量商品日志sql调用方法
     * @param $itemCode       商品编码
     * @param $customerId     客户ID
     * @param $method         接口方法
     * @param $msgId          日志id
     * @param $toApiUrl       转发接口地址
     * @param $sourceData     请求数据来源
     * @param $errorMsg       过滤错误
     * @param $responseParam  响应数据
     * @param $status         接口返回状态
     */
    public function writeProductSql($itemCode, $customerId, $method, $msgId, $toApiUrl, $sourceData, $errorMsg = '', $responseParam, $status)
    {
        global $db;
        $sql = "INSERT INTO t_product_interface_log(sku, customer_id, method, msg_id, api_url, request_param, filter_result, response_param, return_status, create_time) VALUES(:sku, :customer_id, :method, :msg_id, :api_url, :request_param, :filter_result, :response_param, :return_status, now())";
        $Command = $db->prepare($sql);
        $Command->bindParam(':sku', $itemCode);
        $Command->bindParam(':customer_id', $customerId);
        $Command->bindParam(':method', $method);
        $Command->bindParam(':msg_id', $msgId);
        $Command->bindParam(':api_url', $toApiUrl);
        $Command->bindParam(':request_param', $sourceData);
        $Command->bindParam(':filter_result', $errorMsg);
        $Command->bindParam(':response_param', $responseParam);
        $Command->bindParam(':return_status', $status);
        $Command->execute();
    }

    /**
     * 批量订单日志记录调用方法
     * @param $orderNo        订单号
     * @param $orderType      订单类型
     * @param $customerId     客户ID
     * @param $warehouseCode  仓库编码
     * @param $method         接口方法
     * @param $msgId          日志id
     * @param $toApiUrl       转发接口地址
     * @param $sourceData     请求数据来源
     * @param $errorMsg       过滤错误
     * @param $responseParam  响应数据
     * @param $status         接口返回状态
     */
    public function writeOrderSql($orderNo, $orderType, $customerId, $warehouseCode, $method, $msgId, $toApiUrl, $sourceData, $errorMsg, $responseParam, $status)
    {
        global $db;
        $sql = "INSERT INTO t_order_interface_log(order_no, order_type, customer_id, warehouse_code, method, msg_id, api_url, request_param, filter_result, response_param, return_status, create_time) VALUES(:order_no, :order_type, :customer_id, :warehouse_code, :method, :msg_id, :api_url, :request_param, :filter_result, :response_param, :return_status, now())";
        $Command = $db->prepare($sql);
        $Command->bindParam(':order_no', $orderNo);
        $Command->bindParam(':order_type', $orderType);
        $Command->bindParam(':customer_id', $customerId);
        $Command->bindParam(':warehouse_code', $warehouseCode);
        $Command->bindParam(':method', $method);
        $Command->bindParam(':msg_id', $msgId);
        $Command->bindParam(':api_url', $toApiUrl);
        $Command->bindParam(':request_param', $sourceData);
        $Command->bindParam(':filter_result', $errorMsg);
        $Command->bindParam(':response_param', $responseParam);
        $Command->bindParam(':return_status', $status);
        $Command->execute();
    }

    /**
     *
     * @param  $customerId    业务级货主id
     * @param  $appCode       应用编码
     * @param  $method        接口名称
     * @param  $sourceData    数据来源
     * @param  $authorUrl     地址
     * @param  $requestData   请求数据
     * @param  $responseData  响应数据
     * @param  $msgId         日志id
     * @param  $toApiUrl      请求接口地址
     */
    public function writeCainiaoInterfaceLog($customerId, $appCode, $method, $sourceData, $authorUrl, $requestData, $responseData, $requestTime, $responseTime, $msgId, $parentLogId = 0, $toApiUrl)
    {
        global $db;
        if (preg_match("/<error_response>(.*)<\/error_response>/", $responseData) || preg_match("/error_response/i", $responseData)) {
            $flag = 0;
        } else {
            $flag = 1;
        }
        //$singleMethodArr = array();
        $sql = "INSERT INTO csk_interface_log(customer_id,app_code,method,order_list,msg_id,parent_log_id,api_url,request_param,response_param,request_time,response_time,return_status,create_time) VALUES(:customer_id,:app_code,:method,:trade_order_list,:msg_id,:parent_log_id,:api_url,:request_param,:response_param,:request_time,:response_time,:return_status,now())";

        if ($method == 'taobao.wlb.waybill.i.get') {
            if (empty($requestData['trade_order_info_cols'][0])) {
                $requestData['trade_order_info_cols'] = array($requestData['trade_order_info_cols']);
            }
            foreach ($requestData['trade_order_info_cols'] as $infoVal) {
                $cainiaoModel = $db->prepare($sql);
                $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
                $cainiaoModel->bindParam(':customer_id', $customerId);
                $cainiaoModel->bindParam(':app_code', $appCode);
                $cainiaoModel->bindParam(':method', $method);
                $cainiaoModel->bindParam(':trade_order_list', $infoVal['trade_order_list']);
                $cainiaoModel->bindParam(':msg_id', $msgId);
                $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
                $cainiaoModel->bindParam(':api_url', $toApiUrl);
                $cainiaoModel->bindParam(':request_param', $sourceData);
                $cainiaoModel->bindParam(':response_param', $responseData);
                $cainiaoModel->bindParam(':request_time', $requestTime);
                $cainiaoModel->bindParam(':response_time', $responseTime);
                $cainiaoModel->bindParam(':return_status', $flag);
                $cainiaoModel->execute();
            }
        } elseif ($method == 'taobao.wlb.waybill.i.print') {
            if (empty($requestData['print_check_info_cols'][0])) {
                $requestData['print_check_info_cols'] = array($requestData['print_check_info_cols']);
            }
            foreach ($requestData['print_check_info_cols'] as $printVal) {
                $cainiaoModel = $db->prepare($sql);
                $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
                $cainiaoModel->bindParam(':customer_id', $customerId);
                $cainiaoModel->bindParam(':app_code', $appCode);
                $cainiaoModel->bindParam(':method', $method);
                $cainiaoModel->bindParam(':trade_order_list', $printVal['waybill_code']);
                $cainiaoModel->bindParam(':msg_id', $msgId);
                $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
                $cainiaoModel->bindParam(':api_url', $toApiUrl);
                $cainiaoModel->bindParam(':request_param', $sourceData);
                $cainiaoModel->bindParam(':response_param', $responseData);
                $cainiaoModel->bindParam(':request_time', $requestTime);
                $cainiaoModel->bindParam(':response_time', $responseTime);
                $cainiaoModel->bindParam(':return_status', $flag);
                $cainiaoModel->execute();
            }
        } elseif (in_array($method, array('taobao.wlb.waybill.i.fullupdate', 'taobao.wlb.waybill.i.cancel'))) {
            $cainiaoModel = $db->prepare($sql);
            $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
            $cainiaoModel->bindParam(':customer_id', $customerId);
            $cainiaoModel->bindParam(':app_code', $appCode);
            $cainiaoModel->bindParam(':method', $method);
            $cainiaoModel->bindParam(':trade_order_list', $requestData['trade_order_list']);
            $cainiaoModel->bindParam(':msg_id', $msgId);
            $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
            $cainiaoModel->bindParam(':api_url', $toApiUrl);
            $cainiaoModel->bindParam(':request_param', $sourceData);
            $cainiaoModel->bindParam(':response_param', $responseData);
            $cainiaoModel->bindParam(':request_time', $requestTime);
            $cainiaoModel->bindParam(':response_time', $responseTime);
            $cainiaoModel->bindParam(':return_status', $flag);
            $cainiaoModel->execute();
        } elseif ($method == 'taobao.wlb.waybill.i.search') {
            $cainiaoModel = $db->prepare($sql);
            $orderList = '';
            $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
            $cainiaoModel->bindParam(':customer_id', $customerId);
            $cainiaoModel->bindParam(':app_code', $appCode);
            $cainiaoModel->bindParam(':method', $method);
            $cainiaoModel->bindParam(':trade_order_list', $orderList);
            $cainiaoModel->bindParam(':msg_id', $msgId);
            $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
            $cainiaoModel->bindParam(':api_url', $toApiUrl);
            $cainiaoModel->bindParam(':request_param', $sourceData);
            $cainiaoModel->bindParam(':response_param', $responseData);
            $cainiaoModel->bindParam(':request_time', $requestTime);
            $cainiaoModel->bindParam(':response_time', $responseTime);
            $cainiaoModel->bindParam(':return_status', $flag);
            $cainiaoModel->execute();
        } elseif ($method == 'taobao.wlb.waybill.i.seller.authorization') {
            $cainiaoModel = $db->prepare($sql);
            $orderList = '';
            $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
            $cainiaoModel->bindParam(':customer_id', $customerId);
            $cainiaoModel->bindParam(':app_code', $appCode);
            $cainiaoModel->bindParam(':method', $method);
            $cainiaoModel->bindParam(':trade_order_list', $orderList);
            $cainiaoModel->bindParam(':msg_id', $msgId);
            $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
            $cainiaoModel->bindParam(':api_url', $authorUrl);
            $cainiaoModel->bindParam(':request_param', $sourceData);
            $cainiaoModel->bindParam(':response_param', $responseData);
            $cainiaoModel->bindParam(':request_time', $requestTime);
            $cainiaoModel->bindParam(':response_time', $responseTime);
            $cainiaoModel->bindParam(':return_status', $flag);
            $cainiaoModel->execute();
        } elseif ($method == 'cainiao.waybill.ii.get') {//新菜鸟接口 下同
            if (empty($requestData['trade_order_info_dtos']['trade_order_info_dto'][0])) {
                $requestData['trade_order_info_dtos']['trade_order_info_dto'] = array($requestData['trade_order_info_dtos']['trade_order_info_dto']);
            }
            foreach ($requestData['trade_order_info_dtos']['trade_order_info_dto'] as $dtoVal) {
                $cainiaoModel = $db->prepare($sql);
                $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
                $cainiaoModel->bindParam(':customer_id', $customerId);
                $cainiaoModel->bindParam(':app_code', $appCode);
                $cainiaoModel->bindParam(':method', $method);
                $cainiaoModel->bindParam(':trade_order_list', $dtoVal['order_info']['trade_order_list']);
                $cainiaoModel->bindParam(':msg_id', $msgId);
                $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
                $cainiaoModel->bindParam(':api_url', $toApiUrl);
                $cainiaoModel->bindParam(':request_param', $sourceData);
                $cainiaoModel->bindParam(':response_param', $responseData);
                $cainiaoModel->bindParam(':request_time', $requestTime);
                $cainiaoModel->bindParam(':response_time', $responseTime);
                $cainiaoModel->bindParam(':return_status', $flag);
                $cainiaoModel->execute();
            }
        } elseif ($method == 'cainiao.waybill.ii.cancel') {//order_list记录的是面单号
            $cainiaoModel = $db->prepare($sql);
            $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
            $cainiaoModel->bindParam(':customer_id', $customerId);
            $cainiaoModel->bindParam(':app_code', $appCode);
            $cainiaoModel->bindParam(':method', $method);
            $cainiaoModel->bindParam(':trade_order_list', $requestData['waybill_code']);
            $cainiaoModel->bindParam(':msg_id', $msgId);
            $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
            $cainiaoModel->bindParam(':api_url', $toApiUrl);
            $cainiaoModel->bindParam(':request_param', $sourceData);
            $cainiaoModel->bindParam(':response_param', $responseData);
            $cainiaoModel->bindParam(':request_time', $requestTime);
            $cainiaoModel->bindParam(':response_time', $responseTime);
            $cainiaoModel->bindParam(':return_status', $flag);
            $cainiaoModel->execute();
        } elseif ($method == 'cainiao.waybill.ii.search') {
            $cainiaoModel = $db->prepare($sql);
            $orderList = '';
            $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
            $cainiaoModel->bindParam(':customer_id', $customerId);
            $cainiaoModel->bindParam(':app_code', $appCode);
            $cainiaoModel->bindParam(':method', $method);
            $cainiaoModel->bindParam(':trade_order_list', $orderList);
            $cainiaoModel->bindParam(':msg_id', $msgId);
            $cainiaoModel->bindParam(':parent_log_id', $parentLogId);
            $cainiaoModel->bindParam(':api_url', $toApiUrl);
            $cainiaoModel->bindParam(':request_param', $sourceData);
            $cainiaoModel->bindParam(':response_param', $responseData);
            $cainiaoModel->bindParam(':request_time', $requestTime);
            $cainiaoModel->bindParam(':response_time', $responseTime);
            $cainiaoModel->bindParam(':return_status', $flag);
            $cainiaoModel->execute();
        }
    }

    /**
     * 获取接口地址
     * @param $requestData 请求数据
     * @param $method      接口名称
     * @return bool        状态
     */
    public function getUrlByWarehouse($requestData, $method)
    {
        global $db;

        if (in_array($method, array('singleitem.synchronize', 'entryorder.query', 'returnorder.query', 'stockout.query', 'deliveryorder.query', 'orderprocess.query', 'order.cancel', 'stock.query', 'settlement.query'))) {
            qimen_service::$_warehouseCode = $requestData['warehouseCode'];
        } elseif ($method == 'entryorder.create') {
            qimen_service::$_warehouseCode = $requestData['entryOrder']['warehouseCode'];
        } elseif ($method == 'returnorder.create') {
            qimen_service::$_warehouseCode = $requestData['returnOrder']['warehouseCode'];
        } elseif ($method == 'stockout.create') {
            qimen_service::$_warehouseCode = $requestData['deliveryOrder']['warehouseCode'];
        } elseif ($method == 'deliveryorder.create') {
            qimen_service::$_warehouseCode = $requestData['deliveryOrder']['warehouseCode'];
        } elseif ($method == 'deliveryorder.batchcreate') {
            if (empty($requestData['orders']['order'][0])) {
                $requestData['orders']['order'] = array($requestData['orders']['order']);
            }
            qimen_service::$_warehouseCode = $requestData['orders']['order'][0]['deliveryOrder']['warehouseCode'];
        } elseif ($method == 'inventory.query') {
            if (empty($requestData['criteriaList']['criteria'][0])) {
                $requestData['criteriaList']['criteria'] = array($requestData['criteriaList']['criteria']);
            }
            qimen_service::$_warehouseCode = $requestData['criteriaList']['criteria'][0]['warehouseCode'];
        } else {
            return true;
        }

        if (qimen_service::$_warehouseCode != '') {
            global $db;

            $sql = "SELECT wms_url FROM t_base_warehouse WHERE warehouse_code=:warehouse_code AND is_valid=1";
            $model = $db->prepare($sql);
            $model->bindParam(':warehouse_code', qimen_service::$_warehouseCode);
            $model->execute();
            $whInfo = $model->fetch(PDO::FETCH_ASSOC);
            if (!empty($whInfo['wms_url'])) {
                qimen_service::$_toApiUrl = $whInfo['wms_url'];
            }
        }
        return true;
    }

    /**
     *
     * @param 接口名称 $method
     * @param 货主编码 $ownerUserId
     * @param 请求数据 $requestData
     * @param 返回状态 $success
     * @param 响应数据 $responseData
     * @param 校验错误 $filterRs
     */
    public function writeStorageLog($method, $ownerUserId, $requestData, $success, $responseData = '', $filterRs = '')
    {
        global $db;
        $sql1 = "INSERT INTO t_product_interface_log(
                    sku,
                    customer_id,
                    method,
                    msg_id,
                    api_url,
                    request_param,
                    filter_result,
                    response_param,
                    return_status,
                    create_time 
                 ) VALUES(
                    :sku,
                    :customer_id,
                    :method,
                    :msg_id,
                    :api_url,
                    :request_param,
                    :filter_result,
                    :response_param,
                    :return_status,
                    :create_time
                 )";
        $sql2 = 'INSERT INTO t_order_interface_log(
                         order_no,
                         order_type,
                         customer_id,
                         warehouse_code,
                         method,
                         msg_id,
                         api_url,
                         request_param,
                         filter_result,
                         response_param,
                         return_status,
                         create_time
                    ) VALUES(
                         :order_no,
                         :order_type,
                         :customer_id,
                         :warehouse_code,
                         :method,
                         :msg_id,
                         :api_url,
                         :request_param,
                         :filter_result,
                         :response_param,
                         :return_status,
                         :create_time
                    )';
        $values = array();
        $flag = $success == 'true' && $success ? 1 : 0;
        if ($method == 'WMS_SKU_INFO_NOTIFY') {
            $values = array(
                ':sku' => $requestData['itemCode'],
                ':customer_id' => $ownerUserId,
                ':method' => $method,
                ':msg_id' => cn_storage_service::$_msg_id,
                ':api_url' => STORAGE_CN_URL,
                ':request_param' => cn_storage_service::$_logistics_interface,
                ':filter_result' => $filterRs,
                ':response_param' => $responseData,
                ':return_status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            $model = $db->prepare($sql1);
            $model->execute($values);
        } elseif ($method == 'WMS_ITEM_QUERY') {
            $values = array(
                ':sku' => '',
                ':customer_id' => $ownerUserId,
                ':method' => $method,
                ':msg_id' => cn_storage_service::$_msgId,
                ':api_url' => STORAGE_WMS_API_URL,
                ':request_param' => cn_storage_service::$_data,
                ':filter_result' => $filterRs,
                ':response_param' => $responseData,
                ':return_status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            $model = $db->prepare($sql1);
            $model->execute($values);
        } elseif (in_array($method, array('WMS_INVENTORY_COUNT', 'WMS_INVENTORY_ADJUST_UPLOAD'))) {
            $values = array(
                ':order_no' => $requestData['imbalanceOrderCode'],
                ':order_type' => $requestData['orderType'],
                ':customer_id' => $ownerUserId,
                ':warehouse_code' => cn_storage_service::$_warehouseid,
                ':method' => $method,
                ':msg_id' => cn_storage_service::$_msgId,
                ':api_url' => STORAGE_WMS_API_URL,
                ':request_param' => cn_storage_service::$_data,
                ':filter_result' => $filterRs,
                ':response_param' => $responseData,
                ':return_status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            $model = $db->prepare($sql2);
            $model->execute($values);
        } elseif (in_array($method, array('WMS_STOCK_OUT_ORDER_NOTIFY', 'WMS_CONSIGN_ORDER_NOTIFY'))) {
            $values = array(
                ':order_no' => $requestData['orderCode'],
                ':order_type' => $requestData['orderType'],
                ':customer_id' => $ownerUserId,
                ':warehouse_code' => $requestData['storeCode'],
                ':method' => $method,
                ':msg_id' => cn_storage_service::$_msg_id,
                ':api_url' => STORAGE_CN_URL,
                ':request_param' => cn_storage_service::$_logistics_interface,
                ':filter_result' => $filterRs,
                ':response_param' => $responseData,
                ':return_status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            $model = $db->prepare($sql2);
            $model->execute($values);
        } elseif (in_array($method, array('WMS_STOCK_OUT_ORDER_CONFIRM', 'WMS_CONSIGN_ORDER_CONFIRM'))) {
            $values = array(
                ':order_no' => $requestData['orderCode'],
                ':order_type' => $requestData['orderType'],
                ':customer_id' => $ownerUserId,
                ':warehouse_code' => cn_storage_service::$_warehouseid,
                ':method' => $method,
                ':msg_id' => cn_storage_service::$_msgId,
                ':api_url' => STORAGE_WMS_API_URL,
                ':request_param' => cn_storage_service::$_data,
                ':filter_result' => $filterRs,
                ':response_param' => $responseData,
                ':return_status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            $model = $db->prepare($sql2);
            $model->execute($values);
        } elseif (in_array($method, array('WMS_STOCK_IN_ORDER_NOTIFY', 'WMS_ORDER_CANCEL_NOTIFY'))) {
            $values = array(
                ':order_no' => $requestData['orderCode'],
                ':order_type' => $requestData['orderType'],
                ':customer_id' => $ownerUserId,
                ':warehouse_code' => $requestData['storeCode'],
                ':method' => $method,
                ':msg_id' => cn_storage_service::$_msg_id,
                ':api_url' => STORAGE_CN_URL,
                ':request_param' => cn_storage_service::$_logistics_interface,
                ':filter_result' => $filterRs,
                ':response_param' => $responseData,
                ':return_status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            $model = $db->prepare($sql2);
            $model->execute($values);
        } elseif (in_array($method, array('WMS_STOCK_IN_ORDER_CONFIRM', 'WMS_ORDER_STATUS_UPLOAD'))) {
            $values = array(
                ':order_no' => $requestData['orderCode'],
                ':order_type' => $requestData['orderType'],
                ':customer_id' => $ownerUserId,
                ':warehouse_code' => cn_storage_service::$_warehouseid,
                ':method' => $method,
                ':msg_id' => cn_storage_service::$_msgId,
                ':api_url' => STORAGE_WMS_API_URL,
                ':request_param' => cn_storage_service::$_data,
                ':filter_result' => $filterRs,
                ':response_param' => $responseData,
                ':return_status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            $model = $db->prepare($sql2);
            $model->execute($values);
        }
    }


    /**
     * @param $msgType
     * @param $status
     * @param $requestData
     * @param string $filterMsg
     * @param $respMsg
     */
    public function addCustomLog($msgType, $requestData, $filterMsg = '', $respMsg)
    {
        if (!empty($respMsg)) {
            $xmlObj = new xml();
            $respMsgArr = $xmlObj->xmlStr2array($respMsg);
            //获取返回状态
            if ($msgType != 'cnec_wh_7') {
                $flag = $respMsgArr['success'] == 'true' || $respMsgArr['success'] === true ? 1 : 0;
            } else {
                $flag = !empty($respMsgArr) && ($respMsgArr['success'] != 'false' || $respMsgArr['success'] === false) ? 1 : 0;
            }

            $sql = "INSERT INTO t_kj_interface_log(key_no,storer,wmwhseid,msg_type,req_param,filter_param,resp_param,status,create_time) ";
            $sql .= "VALUES(:key_no,:storer,:wmwhseid,:msg_type,:req_param,:filter_param,:resp_param,:status,:create_time)";

            $storer = isset($requestData['storer']) ? $requestData['storer'] : '';
            $wmwhseid = isset($requestData['wmwhseid']) ? $requestData['wmwhseid'] : '';

            $keyNo = '';
            switch ($msgType) {
                case 'cnec_wh_2':
                    $keyNo = $requestData['carrier'];//承运商编码
                    break;
                case 'cnec_wh_3':
                    $keyNo = $requestData['skuKey'];//商品编码
                    break;
                case 'cnec_wh_4':
                case 'cnec_wh_5':
                case 'cnec_wh_5':
                case 'cnec_wh_6':
                case 'cnec_wh_9':
                case 'cnec_wh_11':
                case 'cnec_im_1':
                case 'cnec_im_3':
                    $keyNo = $requestData['externalNo'];
                    break;
                case 'cnec_wh_7':
                    $keyNo = $requestData['declNo'];
                    break;
                case 'cnec_im_6':
                    $keyNo = $requestData['mailNo'];
                    break;
                default:
                    break;
            }

            $params = array(
                ':key_no' => $keyNo,
                ':storer' => $storer,
                ':wmwhseid' => $wmwhseid,
                ':msg_type' => $msgType,
                ':req_param' => custom_service::$_msg,
                ':filter_param' => $filterMsg,
                ':resp_param' => $respMsg,
                ':status' => $flag,
                ':create_time' => date("Y-m-d H:i:s"),
            );
            global $db;
            $model = $db->prepare($sql);
            $model->execute($params);
        }
    }

    /**
     * 添加考拉接口日志
     * @param $requestData
     * @param $method
     * @param $reqUrl
     * @param $data
     * @param $filterRs
     * @param $rs
     * @param $status
     */
    public function addKlLog($requestData, $method, $reqUrl, $data, $filterRs, $rs, $status)
    {
        $flag = $status == 'true' ? 1 : 0;
        if ($requestData == '') {
            $orderNo = '';
            $orderType = '';
        } else {
            switch ($method) {
                case '100':
                case '101':
                case '102':
                case '103':
                    $orderNo = $requestData['outbound_id'];
                    $orderType = $requestData['type'];
                    break;

                case '104':
                case '105':
                case '106':
                    $orderNo = $requestData['inbound_id'];
                    $orderType = $requestData['type'];
                    break;

                case '10':
                case '11':
                case '30':
                    $orderNo = $requestData['purchase_id'];
                    $orderType = '';
                    break;

                case '20':
                case '21':
                case '51':
                    $orderNo = $requestData['order_id'];
                    $orderType = '';
                    break;

                case '31':
                    $orderNo = $requestData['order_id'];
                    $orderType = $requestData['order_status'];
                    break;

                case '60':
                case '150':
                    $orderNo = '';
                    $orderType = '';
                    break;

                case '107':
                case '108':
                case '109':
                    $orderNo = $requestData['storage_tally_num'];
                    $orderType = '';
                    break;

                case '115':
                    $orderNo = $requestData['check_id'];
                    $orderType = $requestData['type'];
                    break;

                default:
                    break;
            }
        }


        if ($method != '119') {
            $values = array(
                'order_no' => $orderNo,
                'order_type' => $orderType,
                'customer_id' => kaola_service::$_ownerId,
                'warehouse_code' => kaola_service::$_stockId,
                'method' => $method,
                'api_url' => $reqUrl,
                'request_param' => $data,
                'filter_result' => $filterRs,
                'response_param' => $rs,
                'return_status' => $flag,
                'create_time' => date("Y-m-d H:i:s"),
            );
            $table = 't_order_interface_log';
        } else {
            //商品同步接口日志
            $values = array(
                'sku' => $requestData[0]['sku_id'],
                'customer_id' => kaola_service::$_ownerId,
                'method' => $method,
                'api_url' => $reqUrl,
                'request_param' => $data,
                'filter_result' => $filterRs,
                'response_param' => $rs,
                'return_status' => $flag,
                'create_time' => date("Y-m-d H:i:s"),
            );
            $table = 't_product_interface_log';
        }
        OmsDatabase::$oms_db->insert($table, $values);
    }

    /**
     * 记录京东无界接口日志
     * @param $customerCode  货主编码
     * @param $method        方法名
     * @param $orderList     订单号或运单号
     * @param $msgId         日志id
     * @param $parentId      父日志id
     * @param $apiUrl        请求的接口地址
     * @param $requestParam  请求数据
     * @param $responseParam 返回数据
     * @param $requestTime   请求时间
     * @param $responseTime  返回时间
     * @param $status        响应状态
     * @throws Exception     异常
     */
    public function addJdWaybillLog($customerCode, $method, $orderList, $msgId, $parentId, $apiUrl, $requestParam, $responseParam, $requestTime, $responseTime, $status)
    {
        $jdDb = new DbAction();

        if ($status != 0) {
            $flag = 0;
        } else {
            $flag = 1;
        }

        $values = array(
            'customer_id' => $customerCode,
            'app_code' => jd_service::$_appKey,
            'method' => $method,
            'order_list' => $orderList,
            'msg_id' => $msgId,
            'parent_log_id' => $parentId,
            'api_url' => $apiUrl,
            'request_param' => $requestParam,
            'response_param' => $responseParam,
            'request_time' => $requestTime,
            'response_time' => $responseTime,
            'return_status' => $flag,
            'create_time' => date("Y-m-d H:i:s"),
        );

        try {
            $jdDb->insert('csk_interface_log', $values);
        } catch (Exception $e) {
            throw new Exception("MySQL Error: " . $e->getMessage());
        }
    }

    //判断是否是多维数组
    public function qimenArrayMulti($arr)
    {
        if (!is_array($arr)) {
            return false;
        }
        if (empty($arr)) {
            return false;
        } else {
            if (!empty($arr['request'])) {
                $arr = $arr['request'];
            }
        }
        if (!empty($arr['items'])) {
            $KeyArr = array_keys($arr['items']);
        } else {
            return false;
        }
        foreach ($KeyArr as $val) {
            if (!is_numeric($val)) {
                return false;
            }
        }
        return true;
    }

    /***
     * Notes:拼多多接口日志
     * Date: 2019/3/28
     * Time: 14:20
     * @param $appCode              应用编码
     * @param $method               接口名称
     * @param $sourceData           数据来源
     * @param $requestData          请求数据
     * @param $responseData         响应数据
     * @param $requestTime          请求时间
     * @param $responseTime         响应时间
     * @param $msgId                日志id
     * @param string $parentLogId 父日志ID
     * @param $toApiUrl             请求接口地址
     */
    public function pddLogWrite($appCode, $method, $sourceData, $requestData, $responseData, $requestTime, $responseTime, $msgId, $parentLogId = '', $toApiUrl)
    {
        # 1. pdd.waybill.get( 电子面单云打印接口 )
        # //2. pdd.waybill.query.by.waybillcode( 通过面单号查询面单信息 )
        # //3. pdd.waybill.update(更新接口)
        # 4. ppp.waybill.search(查询面单服务订购及面单使用情况)
        # 5. pdd.waybill.cancel(商家取消接口)
        # 6. sellerAuthorization(授权信息同步接口 erp->oms)
        $db = OmsDatabase::$oms_db;
        if (preg_match("/<error_response>(.*)<\/error_response>/", $responseData) || preg_match("/error_response/i", $responseData)) {
            $flag = 0;
        } else {
            $flag = 1;
        }
        $customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];
        if ($method == 'pdd.waybill.cancel' || $method == 'pdd.waybill.search') {
            $orderList = isset($requestData['waybill_code']) ? $requestData['waybill_code'] : '';
            $insertData = array(
                'customer_id' => $customerId,
                'app_code' => $appCode,
                'method' => $method,
                'order_list' => $orderList,
                'msg_id' => $msgId,
                'parent_log_id' => $parentLogId,
                'api_url' => $toApiUrl,
                'request_param' => serialize($sourceData),
                'response_param' => $responseData,
                'request_time' => $requestTime,
                'response_time' => $responseTime,
                'return_status' => $flag,
                'create_time' => date('Y-m-d H:i:s'),
            );
            $db->insert('pdd_interface_log', $insertData);
        } elseif ($method == 'pdd.waybill.get') {
            $responseArr = json_decode($responseData, true);
            if (empty($responseArr['pdd_waybill_get_response'])) {
                $insertData = array(
                    'customer_id' => $customerId,
                    'app_code' => $appCode,
                    'method' => $method,
                    'order_list' => '',
                    'msg_id' => $msgId,
                    'parent_log_id' => $parentLogId,
                    'api_url' => $toApiUrl,
                    'request_param' => serialize($sourceData),
                    'response_param' => $responseData,
                    'request_time' => $requestTime,
                    'response_time' => $responseTime,
                    'return_status' => $flag,
                    'create_time' => date('Y-m-d H:i:s'),
                );
                $db->insert('pdd_interface_log', $insertData);
            } else {
                $tmpData = empty($responseArr['pdd_waybill_get_response']['modules'][0]) ? array($responseArr['pdd_waybill_get_response']['modules']) : $responseArr['pdd_waybill_get_response']['modules'];
                foreach ($tmpData as $tmpVal) {
                    $insertData = array(
                        'customer_id' => $customerId,
                        'app_code' => $appCode,
                        'method' => $method,
                        'order_list' => $tmpVal['waybill_code'],
                        'msg_id' => $msgId,
                        'parent_log_id' => $parentLogId,
                        'api_url' => $toApiUrl,
                        'request_param' => serialize($sourceData),
                        'response_param' => $responseData,
                        'request_time' => $requestTime,
                        'response_time' => $responseTime,
                        'return_status' => $flag,
                        'create_time' => date('Y-m-d H:i:s'),
                    );
                    $db->insert('pdd_interface_log', $insertData);
                }
            }
        } else {
            $insertData = array(
                'customer_id' => $customerId,
                'app_code' => $appCode,
                'method' => $method,
                'order_list' => '',
                'msg_id' => $msgId,
                'parent_log_id' => $parentLogId,
                'api_url' => $toApiUrl,
                'request_param' => serialize($sourceData),
                'response_param' => $responseData,
                'request_time' => $requestTime,
                'response_time' => $responseTime,
                'return_status' => $flag,
                'create_time' => date('Y-m-d H:i:s'),
            );
            $db->insert('pdd_interface_log', $insertData);
        }
    }
    /***
     * Notes:记录贝贝接口日志
     * Date: 2019/6/18
     * Time: 22:32
     * @param $method 接口方法
     * @param $sourceData 请求的json数据
     * @param $errorMsg oms报错信息
     * @param $requestData 请求的json转换后的数组数据
     * @param $responseParam ERP或WMS返回的数据
     * @param $msgId 接口主msgid
     * @param $toApiUrl 请求的ERP或WMS接口地址
     * @param $status 接口返回状态
     */
    public function writeBeibeiInterfaceLog($method, $sourceData, $errorMsg, $requestData, $responseParam, $msgId, $toApiUrl, $status)
    {
        $skuArr = array();
        $orderArr = array();
        //单一商品method数组
        $methodSkuArr = array(
            'beibei.outer.product.sync'
        );
        //入库单、出库单、发货单method数组
        $methodOrderArr = array(
            'beibei.outer.entryorder.create', 'beibei.outer.stockout.create',
            'beibei.outer.deliveryorder.create', 'beibei.outer.bill.cancel'
        );
        //入库单回传接口、出库单回传接口
        $methodConfirmArr = array(
            'beibei.outer.entryorder.confirm','beibei.outer.stockout.confirm'
        );
        //库存异动接口
        $methodStockchangeArr = array(
            'beibei.outer.stockchange.report'
        );
        //退货入库单method数组
        $methodReturnOrderArr = array(
            'beibei.outer.rma.create'
        );
        //请求数据处理
        if (!empty($requestData)) {
            if (in_array($method, $methodSkuArr)) {    //单一商品数据处理
                if (empty($requestData[0])) {
                    $requestData = array($requestData);
                }
                foreach ($requestData as $k => $v) {
                    $skuArr[$k]['sku'] = $v['sku'];
                    $skuArr[$k]['customer_id'] = $v['company'];
                }
            } elseif (in_array($method, $methodOrderArr)) {  //入库、出库、发货订单数据处理
                $orderArr['order_no'] = isset($requestData['billId'])? $requestData['billId'] : $requestData['orderNo'] ;
                $orderArr['order_type'] = isset($requestData['billType'])? $requestData['billType'] : $requestData['orderType'] ;
                $orderArr['customer_id'] = isset($requestData['company'])? $requestData['company'] : $requestData['companyId'] ;
                $orderArr['warehouse_code'] = isset($requestData['warehouse'])? $requestData['warehouse'] : $requestData['warehouseNo'] ;
            } elseif (in_array($method, $methodConfirmArr)) {  //回传数据处理
                $orderArr['order_no'] = isset($requestData['entryOrder']['billId']) ? $requestData['entryOrder']['billId']: $requestData['stockout']['billId'];
                $orderArr['order_type'] = isset($requestData['entryOrder']['billType']) ? $requestData['entryOrder']['billType']: $requestData['stockout']['billType'];
                $orderArr['customer_id'] = isset($requestData['entryOrder']['details'][0]['company']) ? $requestData['entryOrder']['details'][0]['company'] : $requestData['stockout']['details'][0]['company'];
                $orderArr['warehouse_code'] = isset($requestData['entryOrder']['warehouse']) ? $requestData['entryOrder']['warehouse'] : $requestData['stockout']['warehouse'];
            }elseif (in_array($method, $methodStockchangeArr)) {  //库存异动数据处理
                $orderArr['order_no'] = $requestData['stockChangeReport']['billId'];
                $orderArr['order_type'] = $requestData['stockChangeReport']['company'];
                $orderArr['customer_id'] = $requestData['stockChangeReport']['warehouse'];
                $orderArr['warehouse_code'] = $requestData['stockChangeReport']['warehouse'];
            } elseif (in_array($method, $methodReturnOrderArr)) {  //销售退货下传数据处理
                $orderArr['order_no'] = $requestData['header']['returnId'];
                $orderArr['order_type'] = $requestData['header']['orderType'];
                $orderArr['customer_id'] = $requestData['header']['company'];
                $orderArr['warehouse_code'] = $requestData['header']['refundWarehouse'];
            }
            //状态处理
            if ($status) {
                $status = 1;
            } else {
                $status = 0;
            }
            //写入商品日志
            if (!empty($skuArr)) {
                $insert_arr =array();
                foreach ($skuArr as $k => $v) {
                    $insert_arr[] = array(
                        'sku' => $v['sku'],
                        'customer_id' => $v['customer_id'],
                        'method' => $method,
                        'msg_id' => $msgId,
                        'api_url' => $toApiUrl,
                        'request_param' => $sourceData,
                        'response_param' => $responseParam,
                        'return_status' => $status,
                        'create_time' => date('Y-m-d H:i:s')
                    );
                }
                OmsDatabase::$oms_db->insertAll('t_product_interface_log', $insert_arr);
            }
            //写入订单日志
            if (!empty($orderArr)) {
                $insert_arr = array(
                    'order_no' => $orderArr['order_no'],
                    'order_type' => $orderArr['order_type'],
                    'customer_id' => $orderArr['customer_id'],
                    'warehouse_code' => $orderArr['warehouse_code'],
                    'method' => $method,
                    'msg_id' => $msgId,
                    'api_url' => $toApiUrl,
                    'request_param' => $sourceData,
                    'filter_result' => $errorMsg,
                    'response_param' => $responseParam,
                    'return_status' => $status,
                    'create_time' => date('Y-m-d H:i:s')
                );
                OmsDatabase::$oms_db->insert('t_order_interface_log', $insert_arr);
            }
        }
    }
}