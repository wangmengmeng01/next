<?php

/**
 * API请求处理
 * 基于韵达OMS与FLUX WMS接口方案v1.1方案开发
 */
class service
{
    private static $_systemParams = array('method', 'customerid', 'warehouseid', 'messageid', 'apptoken', 'appkey', 'sign', 'timestamp', 'data', 'v', 'format');
    public static $_customerId = '';            //货主id
    public static $_clientCustomerId = '';    //请求中系统级参数中的客户编码customerid
    public static $_method = '';                //请求的接口方法
    public static $_methodTo = '';            //转发时的接口方法
    public static $_cilentDb = '';            //wms数据库,系统级参数中的warehouseid参数
    public static $_messageId = '';            //接口方法的ID
    public static $_appToken = '';            //Token 号
    public static $_appKey = '';                //验签KEY
    public static $_appSecret = '';            //生成验签的appSecret
    private static $_sign = '';                //请求的签名
    public static $_timeStamp = '';            //时间
    private static $_data = '';                //数据
    private static $_format = 'xml';            //返回格式
    private static $_v = '1.0';                //接口版本
    private static $_from = '';                 //接口调用方
    public static $sysMsgCode = array(
        '0000' => '成功',
        '0001' => '失败',
        'S001' => '验签错误',
        'S002' => '系统异常',
        'S003' => '数据错误',
        'S004' => '货主错误',
        'S006' => '记录已存在且不允许修改',
        'S007' => '其他错误'
    );
    private static $_formatList = array('xml', 'json'); //支持的返回格式列表
    private static $_funcInstance = '';                //公共方法类实例
    private static $_requestUrl = OMS_API_URL;            //当前请求URL
    private static $_msgId = '';
    public static $_outputContent = '';                //标准输出内容
    public static $_toApiUrl = '';                    //转发目的地接口地址
    public static $_toApiMethod = '';                    //转发目的地接口方法
    public static $_toApiParams = '';                    //转发目的地参数
    public static $_toApiReturn = '';                    //转发目的地返回结果
    private static $_erroArr = array();                //校验时错误数据数组
    private static $_errFlag = 0;                        //校验错误标识
    public static $_methodErrorInfo = array(
        'putSKUData' => 'CustomerID,SKU,itemId,errorcode,errordescr',                        //商品资料
        'putCustData' => 'CustomerID,Customer_Type,errorcode,errordescr',                //客商档案（wms推送仓库信息给oms）
        'putCustData_ERP' => 'CustomerID,Customer_Type,errorcode,errordescr',            //客商档案（erp推送供应商和店铺信息给wms）
        'putCustData_OmsToErp' => 'CustomerID,Customer_Type,errorcode,errordescr',    //客商档案（oms推送货主和仓库信息给erp）
        'putCustData_OmsToWms' => 'CustomerID,Customer_Type,errorcode,errordescr',    //客商档案（oms推送货主信息给wms）
        'putASNData' => 'OrderNo,OrderType,CustomerID,WarehouseID,errorcode,errordescr',        //入库单下发
        'confirmASNData' => 'OrderNo,OrderType,CustomerID,WarehouseID,errorcode,errordescr',    //入库单状态明细回传
        'cancelASNData' => '',                                                                    //入库单取消
        'putSOData' => 'OrderNo,OrderType,CustomerID,WarehouseID,errorcode,errordescr',        //出库单下发
        'confirmSOStatus' => 'OrderNo,OrderType,CustomerID,WarehouseID,errorcode,errordescr',    //出库单状态回传
        'confirmSOData' => 'OrderNo,OrderType,CustomerID,WarehouseID,errorcode,errordescr',    //出库单明细回传
        'cancelSOData' => '',                                                                    //出库单取消
        'queryINVData' => '',                                                                    //库存查询
        'confirmINVADDData' => '',                                                                //库存推送
        'queryOrderProcess' => 'orderCode,errorcode,errordescr',                                //订单流水查询
        'inventoryReport' => 'checkOrderCode,errorcode,errordescr'                              //库存盘点通知接口
    );  //每个接口对应的错误信息resultInfo中的参数

    public function process()
    {
        //记录接收到的数据日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_oms_receive.log';
            error_log(print_r($_REQUEST, 1), 3, LOG_PATH . $logName);
            error_log(print_r(file_get_contents("php://input"), 1), 3, LOG_PATH . $logName);
        }

        try {
            //初始化公共方法类
            self::$_funcInstance = new func();

            //系统级参数解析
            self::__parseParams($_REQUEST);

            //系统级参数初始化校验
            $this->__filterParams($requestFrom, $requestTo, $async, $apiHandler, $callbackId);

            //校验api的类和方法是否存在
            if (!isset($apiHandler['api_handler_class']) || !$apiHandler['api_handler_class'] || !isset($apiHandler['api_handler_method']) || !$apiHandler['api_handler_method']) {
                $this->send_user_error('S002', '系统调用方法配置错误');
            }

            //应用级参数解析，把数据按照货主拆分开
            $customerData = $this->explodeParams(self::$_data);

            if (empty($customerData)) {
                $this->send_user_error('S003', 'data数据错误');
            }

            //生成日志ID
            self::$_msgId = self::$_funcInstance->makeMsgId();

            //循环处理每个货主的数据
            $msgIdArr = array();
            $apiUrlArr = array();
            $apiResponseArr = array();
            foreach ($customerData as $key => $val) {
                self::$_errFlag = 0;
                self::$_customerId = $key;
                //校验数据中货主是否和系统级参数中货主一致
                if (self::$_from == 'erp' && $_REQUEST['customerid'] != $key) {
                    $this->send_customer_error($key, 'S001', 'xml数据中货主' . $key . '与系统级参数中的货主customerid:' . $_REQUEST['customerid'] . '不一致', $val);
                    continue;
                }
                //获取货主、erp、wms关系
                $customer = self::$_funcInstance->getPartnerInfo($key);
                $customer_id = $customer['customer_id'];
                $erp_bn = $customer['erp_bn'];
                $erp_api = $customer['erp_api'];
                $erp_api_ver = $customer['erp_api_ver'];
                $erp_api_secert = $customer['erp_api_secret'];
                $wms_bn = $customer['wms_bn'];
                $wms_api = $customer['wms_api'];
                $wms_api_ver = $customer['wms_api_ver'];

                //系统级参数有效性校验
                $this->__validFilterParams($customer_id, $erp_bn, $wms_bn, $key, $val);
                if (self::$_errFlag == 1) {
                    continue;
                }
                //验证客户接口权限
                if (!self::$_funcInstance->checkRole($key, self::$_method)) {
                    $this->send_customer_error($key, 'S001', '该货主没有调用' . self::$_method . '接口的权限', $val);
                    continue;
                }

                //获取请求方与目的地接口信息:地址、密钥等
                if ($requestFrom == 'erp') {  //请求方为erp，即erp->wms
                    $requestSystemBn = $erp_bn;
                    $toSystemBn = $wms_bn;
                    $requestApiVer = $erp_api_ver;
                    $toApi = $wms_api;
                    $toApiVer = $wms_api_ver;
                    $toApiSecret = self::$_appSecret;
                } elseif ($requestFrom == 'wms') {  //请求方为wms，即wms->erp
                    $requestSystemBn = $wms_bn;
                    $toSystemBn = $erp_bn;
                    $requestApiVer = $wms_api_ver;
                    $toApi = $erp_api;
                    $toApiVer = $erp_api_ver;
                    $toApiSecret = $erp_api_secert;
                } else {
                    $this->send_customer_error($key, 'S002', '该货主绑定的ERP和WMS信息配置错误', $val);
                    continue;
                }
                //获取接口对应的类、方法
                $apiClass = $apiHandler['api_handler_class'];
                $apiMethod = $apiHandler['api_handler_method'];

                //参数转换:------------------返回数组格式
                $requestData = array();
                $formatClassFile = sprintf(API_ROOT . '/router/format/%s/v%s/%s.php', $requestSystemBn, $requestApiVer, 'format' . ucfirst($apiClass));
                if (!is_file($formatClassFile)) {
                    //取第一个版本
                    $formatClassFile = sprintf(API_ROOT . '/router/format/%s/v1.0/%s.php', $requestSystemBn, 'format' . ucfirst($apiClass));
                }
                if (is_file($formatClassFile)) {
                    include_once addslashes($formatClassFile);
                    $formatClassName = 'format' . ucfirst($apiClass);
                    if (class_exists($formatClassName, false)) {
                        $formatObj = new $formatClassName();
                        if (method_exists($formatObj, $apiMethod)) {
                            $requestData = $formatObj->$apiMethod($val);
                        }
                    } else {
                        $this->send_customer_error($key, 'S002', '该货主配置 的接口类不存在', $val);
                        continue;
                    }
                }
                if (!$requestData) {//无erp自定义参数转换，则默认使用标准
                    $formatClassFile = sprintf(API_ROOT . '/router/format/common/v%s.php', $requestApiVer);
                    if (!is_file($formatClassFile)) {
                        //取第一个版本
                        $formatClassFile = API_ROOT . '/router/format/common/v1.0.php';
                    }
                    if (is_file($formatClassFile)) {
                        include_once addslashes($formatClassFile);
                        $formatObj = new formatCommon();
                        if (method_exists($formatObj, 'request')) {
                            $requestData = $formatObj->request($val);
                        }
                    }
                }

                //应用参数过滤--------------即业务数据有效性过滤
                $filterClassFile = sprintf(API_ROOT . '/router/filter/%s/v%s/%s.php', $requestSystemBn, $requestApiVer, 'filter' . ucfirst($apiClass));
                if (!is_file($filterClassFile)) {
                    //取第一个版本
                    $filterClassFile = sprintf(API_ROOT . '/router/filter/%s/v1.0/%s.php', $requestSystemBn, 'filter' . ucfirst($apiClass));
                    if (!is_file($filterClassFile)) {
                        //获取标准文件
                        $filterClassFile = sprintf(API_ROOT . '/router/filter/common/v%s/%s.php', $requestApiVer, 'filter' . ucfirst($apiClass));
                        if (!is_file($filterClassFile)) {
                            $filterClassFile = sprintf(API_ROOT . '/router/filter/common/v1.0/%s.php', 'filter' . ucfirst($apiClass));
                        }
                    }
                }

                if (is_file($filterClassFile)) {
                    include_once addslashes($filterClassFile);
                    $filterClassName = 'filter' . ucfirst($apiClass);
                    if (class_exists($filterClassName, false)) {
                        $filterObj = new $filterClassName();
                        if (method_exists($filterObj, $apiMethod)) {
                            $filterRs = $filterObj->$apiMethod($requestData);
                            if ($filterRs['returnFlag'] == '0') {
                                $this->send_customer_error($key, $filterRs['returnCode'], $filterRs['returnDesc'], $filterRs);
                                continue;
                            }
                        } else {
                            $this->send_customer_error($key, 'S002', '该货主配置的过滤方法不存在', $val);
                            continue;
                        }
                    }
                }

                //接口请求----------------------------------------自定义接口优先，然后再标准
                $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/%s/v%s/%s.php', $requestTo, $toSystemBn, $toApiVer, $toSystemBn . ucfirst($apiClass));
                if (!is_file($interfaceClassFile)) {
                    //调用自定义实例
                    $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/%s/v1.0/%s.php', $requestTo, $toSystemBn, $toSystemBn . ucfirst($apiClass));
                    if (!is_file($interfaceClassFile)) {
                        //调用标准实例
                        $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/common/v%s/%s.php', $requestTo, $toApiVer, $requestTo . ucfirst($apiClass));
                        if (!is_file($interfaceClassFile)) {
                            $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/common/v1.0/%s.php', $requestTo, $requestTo . ucfirst($apiClass));
                            $interfaceClassName = $requestTo . ucfirst($apiClass);
                        } else {
                            $interfaceClassName = $requestTo . ucfirst($apiClass);
                        }
                    } else {
                        $interfaceClassName = $toSystemBn . ucfirst($apiClass);
                    }
                } else {
                    $interfaceClassName = $toSystemBn . ucfirst($apiClass);
                }

                //加载实例文件
                if (is_file($interfaceClassFile)) {
                    include_once addslashes($interfaceClassFile);
                    if (class_exists($interfaceClassName, false)) {
                        if ($async == 'false') {//同步接口
                            $instance = new $interfaceClassName();
                            if (method_exists($instance, $apiMethod)) {
                                if ($requestTo == 'erp') {
                                    $instance::$customerId = $customer_id;
                                    $instance::$warehouseId = service::$_cilentDb;
                                    $instance::$erpBn = $toSystemBn;
                                    $instance::$erpApi = $toApi;
                                    $instance::$erpApiSecret = $toApiSecret;
                                    $instance::$erpApiVer = $toApiVer;
                                } elseif ($requestTo == 'wms') {
                                    $instance::$customerId = service::$_clientCustomerId;
                                    $instance::$warehouseId = service::$_cilentDb;
                                    $instance::$wmsBn = $toSystemBn;
                                    $instance::$wmsApi = $toApi;
                                    $instance::$wmsApiSecret = $toApiSecret;
                                    $instance::$wmsApiVer = $toApiVer;
                                }
                                self::$_outputContent[$customer_id] = $instance->$apiMethod($requestData);
                                //addon由转发类返回，目的是为了记录日志----------
                                if (isset(self::$_outputContent[$customer_id]['addon'])) {
                                    self::$_toApiUrl = self::$_outputContent[$customer_id]['addon']['api_url'];
                                    self::$_toApiMethod = self::$_outputContent[$customer_id]['addon']['api_method'];
                                    self::$_toApiParams = self::$_outputContent[$customer_id]['addon']['api_params'];
                                    self::$_toApiReturn = self::$_outputContent[$customer_id]['addon']['return_msg'];
                                    unset(self::$_outputContent[$customer_id]['addon']);
                                }

                                self::$_outputContent[$customer_id]['msg_id'] = self::$_msgId;
                            } else {
                                $this->send_customer_error($key, 'S002', '该货主对应的接口中方法不存在', $val);
                                continue;
                            }
                        } elseif ($async == 'true') {  //异步，先存储到队列
                            $this->send_customer_error($key, 'S002', '暂不开放异步接口', $val);
                            continue;
                        } else {
                            $this->send_customer_error($key, 'S002', '接口推送方式配置错误', $val);
                            continue;
                        }
                    } else {
                        $this->send_customer_error($key, 'S002', '该货主对应的接口中类不存在', $val);
                        continue;
                    }
                } else {
                    $this->send_customer_error($key, 'S002', '该货主对应的接口文件不存在', $val);
                    continue;
                }

                //记录请求及返回日志:
                //请求者日志
                $msgId = self::$_funcInstance->writeLog(self::$_msgId, $key, self::$_requestUrl, self::$_method, $_REQUEST, $this->output(self::$_outputContent[$customer_id], false));
                if ($msgId) {
                    //转发目的地日志,$msgId作为此日志的父ID
                    self::$_funcInstance->writeLog('', $key, self::$_toApiUrl, self::$_toApiMethod, self::$_toApiParams, self::$_toApiReturn, $msgId);
                }
                $msgIdArr[$key] = $msgId;
                $apiUrlArr[$key] = self::$_toApiUrl;
                $apiResponseArr[$key] = self::$_toApiReturn;
            }
            //合并验证时的错误信息与接口返回的信息，返回标准输出格式的数据
            self::__merge_data();

            //记录接口日志
            if (YII_INTERFACE_LOG_FLAG) {
                self::$_funcInstance->writeInterfaceLog(self::$_method, self::$_data, $customerData, self::$_erroArr, self::$_outputContent, $apiResponseArr, $msgIdArr, $apiUrlArr);
            }

            //返回
            self::outPutXml(self::$_outputContent);
        } catch (Exception $e) {
            self::$_errorMsg = $e->getMessage() . 'in file ' . $e->getFile() . ' line ' . $e->getLine();
            $this->send_user_error('S007', self::$_errorMsg);
        }
    }

    /**
     * 分析请求参数:支持POST与GET
     * @param Array $request 请求参数
     * @return array
     */
    private static function __parseParams(&$request)
    {
        if (empty($request)) {
            self::send_user_error('S003', '接收到的数据为空');
            return false;
        }
        if (!isset($request['data']) || empty($request['data'])) {
            $request['data'] = file_get_contents("php://input");
        }

        $systemParamsData = array();
        foreach (self::$_systemParams as $v) {
            $systemParamsData[$v] = $request[$v];
        }

        self::$_clientCustomerId = $systemParamsData['customerid'];
        self::$_method = $systemParamsData['method'];
        self::$_cilentDb = $systemParamsData['warehouseid'];
        self::$_messageId = $systemParamsData['messageid'];
        self::$_appToken = $systemParamsData['apptoken'];
        self::$_appKey = $systemParamsData['appkey'];
        self::$_sign = $systemParamsData['sign'];
        self::$_timeStamp = $systemParamsData['timestamp'];
        self::$_data = $systemParamsData['data'];
        self::$_format = in_array($systemParamsData['format'], self::$_formatList) ? $systemParamsData['format'] : 'xml';
        self::$_v = $systemParamsData['v'];
    }

    /**
     * 系统级参数校验及初始化
     * @return array
     */
    private function __filterParams(&$from, &$to, &$async, &$apiHandler, &$callbackId = '')
    {
        if (!self::$_clientCustomerId || !self::$_method || !self::$_cilentDb || !self::$_messageId || !self::$_appKey || !self::$_appToken || !self::$_sign || !self::$_data || !self::$_timeStamp) {
            $this->send_user_error('S001', '系统级参数不完整');
            return false;
        }
        //校验method
        global $db;
        $sql = ' SELECT * FROM t_api_list WHERE api_id=:api_id AND is_valid=1 ';
        $model = $db->prepare($sql);
        $model->bindParam(':api_id', self::$_method);
        $model->execute();
        $apiInfo = $model->fetch(PDO::FETCH_ASSOC);
        if (empty($apiInfo)) {
            $this->send_user_error('S001', '系统级参数method:' . self::$_method . '不存在');
            return false;
        }
        //校验messageid
        if ($apiInfo['message_id'] != self::$_messageId) {
            $this->send_user_error('S001', '系统级参数中messageid:' . self::$_messageId . '与系统配置不匹配');
            return false;
        }
        if ($apiInfo['api_from'] == 'erp') {
            //校验货主是否存在
            $sql = "SELECT a.customer_id,a.app_secret,b.wms_code FROM t_base_customer a LEFT JOIN t_bind_relation b ON a.customer_id=b.customer_id WHERE a.customer_id=:customer_id AND a.active_flag='Y' AND a.is_valid=1 AND b.is_valid=1";
            $model = $db->prepare($sql);
            $model->bindParam(':customer_id', self::$_clientCustomerId);
            $model->execute();
            $rsCustomer = $model->fetch(PDO::FETCH_ASSOC);
            if (empty($rsCustomer)) {
                $this->send_user_error('S001', '系统级参数中货主customerid:' . self::$_clientCustomerId . '不存在或无效');
                return false;
            }
            //获取系统中wms维护的appToken和appKey
            $sql = "SELECT wms_code,app_token,app_key,cilent_customerid,app_secret FROM t_base_wms WHERE wms_code=:wms_code AND is_valid=1";
            $model = $db->prepare($sql);
            $model->bindParam(':wms_code', $rsCustomer['wms_code']);
            $model->execute();
            $rsWms = $model->fetch(PDO::FETCH_ASSOC);
            if (empty($rsWms) || $rsWms['cilent_customerid'] == '' || $rsWms['app_secret'] == '') {
                $this->send_user_error('S001', '系统wms配置错误');
                return false;
            } else {
                $appToken = $rsWms['app_token'];
                $appKey = $rsWms['app_key'];
            }
        } elseif ($apiInfo['api_from'] == 'wms') {
            //获取系统中wms维护的appToken和appKey
            $sql = "SELECT wms_code,app_token,app_key,cilent_customerid,app_secret FROM t_base_wms WHERE cilent_customerid=:cilent_customerid AND is_valid=1";
            $model = $db->prepare($sql);
            $model->bindParam(':cilent_customerid', self::$_clientCustomerId);
            $model->execute();
            $rsWms = $model->fetch(PDO::FETCH_ASSOC);
            if (empty($rsWms)) {
                $this->send_user_error('S001', '系统wms配置错误');
                return false;
            } else {
                $appToken = $rsWms['app_token'];
                $appKey = $rsWms['app_key'];
            }
        } else {
            $this->send_user_error('S001', '货主绑定关系配置错误');
            return false;
        }

        //获取系统中维护的appSecret和cilentCustomerid
        if ($apiInfo['api_from'] == 'erp') {
            $cilentCustomerid = $rsCustomer['customer_id'];
        } elseif ($apiInfo['api_from'] == 'wms') {
            $cilentCustomerid = $rsWms['cilent_customerid'];
        }
        //校验其他系统级参数
        if (self::$_clientCustomerId != $cilentCustomerid || self::$_cilentDb != $apiInfo['client_db'] || self::$_appKey != $appKey || self::$_appToken != $appToken) {
            $this->send_user_error('S001', '系统级参数错误');
            return false;
        }
//         //校验timestamp,该时间与服务端时间误差不得大于10分钟
//         if (self::$_timeStamp < date("Y-m-d H:i:s", strtotime("-10 minutes")) || self::$_timeStamp > date("Y-m-d H:i:s", strtotime("+10 minutes"))) {
//         	$this->send_user_error('S001', 'timestamp参数错误');
//             return false;
//         }
        //校验签名sign
//         $checkSign = strtoupper(base64_encode(md5($appSecret . self::$_data . $appSecret)));
//         if ($checkSign != self::$_sign) {
//         	$this->send_user_error('S001', '签名错误');
//         	return false;
//         }
        //返回参数赋值               
        $from = $apiInfo['api_from'];
        $to = $apiInfo['api_to'];
        $async = $apiInfo['async'] == 'true' ? 'true' : 'false';
        $apiHandler['api_handler_class'] = $apiInfo['api_class'];
        $apiHandler['api_handler_method'] = $apiInfo['api_method'];
        $callbackId = $apiInfo['callback_id'] != '' ? $apiInfo['callback_id'] : '';
        if ($apiInfo['api_from'] == 'erp') {
            self::$_clientCustomerId = $rsWms['cilent_customerid'];
        }
        self::$_appSecret = $rsWms['app_secret'];
        self::$_methodTo = $callbackId;
        self::$_from = $apiInfo['api_from'];
        return true;
    }

    /**
     * 系统级参数有效性验证
     * @return array
     */
    private function __validFilterParams($customerId, $erpBn, $wmsBn, $dataCustomer, $dataValue)
    {
        //货主ID校验
        if (!$customerId) {
            $this->send_customer_error($dataCustomer, 'S004', '货主错误', $dataValue);
            return false;
        }
        //erp和wms编码校验
        if (!$erpBn || !$wmsBn) {
            $this->send_customer_error($dataCustomer, 'S003', '货主对应的ERP或WMS维护错误', $dataValue);
            return false;
        }
        return true;
    }

    /**
     * 应用级参数按照货主进行拆分
     * @param  string
     * @return
     */
    private function explodeParams($requestData)
    {
        $xmlObj = new xml();
        //将xml转换为数组
        $requestData = $xmlObj->xmlStr2array($requestData);
        //过滤数组中的空数组
        $utilObj = new util();
        $requestData = $utilObj->filter_null($requestData);
        $customerData = array();
        if (!empty($requestData)) {
            if (preg_match("/^(putCustData)/", self::$_method)) {
                if (self::$_from == 'erp') {
                    $customerData[$_REQUEST['customerid']] = $requestData;
                } else {
                    $customerData[CUSTOMER_SYS] = $requestData;
                }
            } else {
                if (!empty($requestData['header'])) {
                    if (!empty($requestData['header'][0])) {
                        foreach ($requestData['header'] as $k => $v) {
                            $customerData[$v['CustomerID']]['header'][$k] = $v;
                        }
                    } else {
                        $customerData[$requestData['header']['CustomerID']] = array('header' => $requestData['header']);
                    }
                } elseif (!empty($requestData['data']['orderinfo'])) {
                    if (!empty($requestData['data']['orderinfo'][0])) {
                        foreach ($requestData['data']['orderinfo'] as $k => $v) {
                            $customerData[$v['CustomerID']]['data']['orderinfo'][$k] = $v;
                        }
                    } else {
                        $customerData[$requestData['data']['orderinfo']['CustomerID']] = array('data' => array('orderinfo' => $requestData['data']['orderinfo']));
                    }
                } elseif (!empty($requestData['data']['header'])) {
                    if (!empty($requestData['data']['header'][0])) {
                        foreach ($requestData['data']['header'] as $k => $v) {
                            $customerData[$v['CustomerID']]['data']['header'][$k] = $v;
                        }
                    } else {
                        $customerData[$requestData['data']['header']['CustomerID']] = array('data' => array('header' => $requestData['data']['header']));
                    }
                } elseif (!empty($requestData['data']['ordernos'])) {
                    if (!empty($requestData['data']['ordernos'][0])) {
                        foreach ($requestData['data']['ordernos'] as $k => $v) {
                            $customerData[$v['CustomerID']]['data']['ordernos'][$k] = $v;
                        }
                    } else {
                        $customerData[$requestData['data']['ordernos']['CustomerID']] = array('data' => array('ordernos' => $requestData['data']['ordernos']));
                    }
                }
            }
        }
        return $customerData;
    }

    public function send_user_error($error_code, $error_msg = '', $data = '')
    {
        $content = array(
            'returnFlag' => '0',
            'returnCode' => $error_code,
            'returnDesc' => $error_msg ?: self::$sysMsgCode[$error_code],
            'resultInfo' => $data
        );
        //记录错误日志
        self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_requestUrl, self::$_method, $_REQUEST, $error_msg);

        self::output($content);
    }

    /**
     * 按照接口标准返回格式组合验证的错误数据
     * @param string $customerId
     * @param string $error_code
     * @param string $error_msg
     * @param array $data
     * @return array
     */
    public function send_customer_error($customerId, $error_code, $error_msg = '', $data = '')
    {
        $content = array(
            'returnFlag' => '0',
            'returnCode' => $error_code,
            'returnDesc' => $error_msg ?: self::$sysMsgCode[$error_code]
        );
        if (!empty($data)) {
            //获取method方法需要返回的resultInfo参数信息
            $resultInfoStr = self::$_methodErrorInfo[self::$_method];
            $resultInfoArr = explode(",", $resultInfoStr);
            $resultArr = array();
            $utilObj = new util();
            $mutilFlag = $utilObj->isArrayMulti($data);
            if (!empty($data['header'])) {
                if ($mutilFlag) {
                    $resultArr = $data['header'];
                } else {
                    $resultArr = array($data['header']);
                }
            } elseif (!empty($data['data']['orderinfo'])) {
                if ($mutilFlag) {
                    $resultArr = $data['data']['orderinfo'];
                } else {
                    $resultArr = array($data['data']['orderinfo']);
                }
            } elseif (!empty($data['data']['header'])) {
                if ($mutilFlag) {
                    $resultArr = $data['data']['header'];
                } else {
                    $resultArr = array($data['data']['header']);
                }
            } elseif (!empty($data['data']['ordernos'])) {
                if ($mutilFlag) {
                    $resultArr = $data['data']['ordernos'];
                } else {
                    $resultArr = array($data['data']['ordernos']);
                }
            } else {
                if (!empty($data['resultInfo'])) {
                    $xmlObj = new xml();
                    $xmlStr = '<return><resultInfo>' . $data['resultInfo'] . '</resultInfo></return>';
                    $tmpArr = $xmlObj->xmlStr2array($xmlStr);

                    if (!empty($tmpArr['resultInfo'])) {
                        if (!empty($tmpArr['resultInfo'][0])) {
                            $resultArr = $tmpArr['resultInfo'];
                        } else {
                            $resultArr = array($tmpArr['resultInfo']);
                        }
                    }
                }
            }
            if (!empty($resultArr)) {
                foreach ($resultArr as $k => $v) {
                    foreach ($resultInfoArr as $a) {
                        $content['resultInfo'][$k][$a] = $v[$a];
                    }
                    $content['resultInfo'][$k]['errorcode'] = $content['resultInfo'][$k]['errorcode'] != '' ? $content['resultInfo'][$k]['errorcode'] : $content['returnCode'];
                    $content['resultInfo'][$k]['errordescr'] = $content['resultInfo'][$k]['errordescr'] != '' ? $content['resultInfo'][$k]['errordescr'] : $content['returnDesc'];
                }
            }
        }

        //把resultInfo中的数组转换为xml
        if (!empty($content['resultInfo'])) {
            $msgObj = new msg();
            $content['resultInfo'] = $msgObj->get_error_str($content['resultInfo']);
        }

        self::$_errFlag = 1;
        self::$_erroArr[$customerId] = $content;
        //记录错误日志
        self::$_funcInstance->writeLog(self::$_msgId, $customerId, self::$_requestUrl, self::$_method, $data, $this->output(self::$_erroArr[$customerId], false));
    }

    public static function output($content = '', $forceOutput = true)
    {
        if ($forceOutput === true) {//默认强制输出
            if (self::$_format == 'xml') {
                //xml格式输出
                if (!empty($content['xmldata'])) {
                    $xmlStr = $content['xmldata'];
                    unset($content['xmldata']);
                    if (in_array(self::$_method, array('putSKUData'))) {
                        $xmlReturn = '<Response><return>';
                        $xmlReturn .= util::array2xml($content);
                        $xmlReturn .= $xmlStr . '</return></Response>';
                    } else {
                        $xmlReturn = '<Response><return>';
                        $xmlReturn .= util::array2xml($content);
                        $xmlReturn .= '</return>' . $xmlStr . '</Response>';
                    }
                } else {
                    $xmlReturn = '<Response><return>';
                    $xmlReturn .= util::array2xml($content);
                    $xmlReturn .= '</return></Response>';
                }
                echo $xmlReturn;
            } else {
                $rs['responses']['return'] = $content;
                echo json_encode($rs);
            }
            exit;
        } else {//返回输出格式，不强制输出
            if (self::$_format == 'xml') {
                //xml格式输出
                if (!empty($content['xmldata'])) {
                    $xmlStr = $content['xmldata'];
                    unset($content['xmldata']);
                    if (in_array(self::$_method, array('putSKUData'))) {
                        $xmlReturn = '<Response><return>';
                        $xmlReturn .= util::array2xml($content);
                        $xmlReturn .= $xmlStr . '</return></Response>';
                    } else {
                        $xmlReturn = '<Response><return>';
                        $xmlReturn .= util::array2xml($content);
                        $xmlReturn .= '</return>' . $xmlStr . '</Response>';
                    }
                } else {
                    $xmlReturn = '<Response><return>';
                    $xmlReturn .= util::array2xml($content);
                    $xmlReturn .= '</return></Response>';
                }
            } else {
                $rs['responses']['return'] = $content;
                $xmlReturn = json_encode($rs);
            }
            return $xmlReturn;
        }
    }

    /**
     * 合并验证时的错误信息与接口返回的信息，返回标准输出格式的数据
     * @param null
     * @return null
     */
    private function __merge_data()
    {
        if (!empty(self::$_outputContent)) {
            $xmlObj = new xml();
            $msgObj = new msg();
            if (!empty(self::$_erroArr)) {
                foreach (self::$_outputContent as $k => $v) {
                    if (!empty(self::$_erroArr[$k])) {
                        $infoArr = array();
                        if (self::$_outputContent[$k]['returnFlag'] == 1) {
                            self::$_outputContent[$k]['returnFlag'] = 2;
                            self::$_outputContent[$k]['resultInfo'] = self::$_erroArr[$k]['resultInfo'];
                        } else {
                            if ($v['resultInfo'] != '') {
                                $xmlStr = '<resultInfo>' . $v['resultInfo'] . '</resultInfo>';
                                $tmpArr = $xmlObj->xmlStr2array($xmlStr);
                                if (!empty($tmpArr)) {
                                    if (!empty($tmpArr[0])) {
                                        $resultArr = $tmpArr;
                                    } else {
                                        $resultArr = array($tmpArr);
                                    }
                                }
                            }
                            $i = 0;
                            if (!empty($resultArr)) {
                                foreach ($resultArr as $b) {
                                    $infoArr[$i] = $b;
                                    $i++;
                                }
                            }
                            if (!empty(self::$_erroArr[$k]['resultInfo'])) {
                                $xmlStr = '<resultInfo>' . self::$_erroArr[$k]['resultInfo'] . '</resultInfo>';
                                $tmpArr = $xmlObj->xmlStr2array($xmlStr);
                                if (!empty($tmpArr)) {
                                    if (!empty($tmpArr[0])) {
                                        $resultErrorArr = $tmpArr;
                                    } else {
                                        $resultErrorArr = array($tmpArr);
                                    }
                                }
                                foreach ($resultErrorArr as $c) {
                                    $infoArr[$i] = $c;
                                    $i++;
                                }
                            }
                            self::$_outputContent[$k]['resultInfo'] = $msgObj->get_error_str($infoArr);
                        }
                    } else {
                        continue;
                    }
                }
            }
        } else {
            if (!empty(self::$_erroArr)) {
                self::$_outputContent = self::$_erroArr;
            }
        }
    }

    /**
     * 输出接口最终返回的xml格式数据
     */
    public static function outPutXml($content)
    {
        $returnArr = array();
        $returnFlagArr = array();
        $returnCodeArr = array();
        $returnDescArr = array();
        $resultInfo = '';
        if (!empty($content)) {
            foreach ($content as $v) {
                $returnFlagArr[$v['returnFlag']] = $v['returnFlag'];
                $returnCodeArr[] = $v['returnCode'];
                $returnDescArr[] = $v['returnDesc'];
                if ($resultInfo == '') {
                    $resultInfo = $v['resultInfo'];
                } else {
                    $resultInfo .= '</resultInfo><resultInfo>' . $v['resultInfo'];
                }
                if (!empty($v['xmldata'])) {
                    $returnArr['xmldata'] = $v['xmldata'];
                }
            }
            if (in_array(0, $returnFlagArr) && (in_array(1, $returnFlagArr) || in_array(2, $returnFlagArr))) {
                $returnArr['returnFlag'] = 2;
                $returnArr['returnCode'] = '0001';
                $returnArr['returnDesc'] = '部分成功部分失败';
            } elseif (in_array(0, $returnFlagArr) && !in_array(1, $returnFlagArr) && !in_array(2, $returnFlagArr)) {
                $returnArr['returnFlag'] = 0;
                $returnArr['returnCode'] = '0001';
                $returnArr['returnDesc'] = 'fail';
            } elseif (in_array(1, $returnFlagArr) && !in_array(0, $returnFlagArr) && !in_array(2, $returnFlagArr)) {
                $returnArr['returnFlag'] = 1;
                $returnArr['returnCode'] = '0000';
                $returnArr['returnDesc'] = 'ok';
            } elseif (in_array(2, $returnFlagArr)) {
                $returnArr['returnFlag'] = 2;
                $returnArr['returnCode'] = '0001';
                $returnArr['returnDesc'] = '部分成功部分失败';
            }
            //$returnArr['returnCode'] = implode(",", $returnCodeArr);
            $returnArr['returnDesc'] = implode(";", $returnDescArr);
            $returnArr['resultInfo'] = $resultInfo;
        }
        self::output($returnArr);
    }

}