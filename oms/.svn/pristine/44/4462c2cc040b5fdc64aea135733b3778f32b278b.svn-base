<?php

/**
 * OMS发起请求接口路由
 * @author 独孤羽<123517746@qq.com>
 * @date 2015-05-07
 */
class inner_service
{
    private static $_systemParams = array('method', 'customerid', 'warehouseid', 'messageid', 'apptoken', 'appkey', 'sign', 'timestamp', 'data', 'msg_id', 'customer_id');
    private static $_customerId = '';#请求的客户ID
    private static $_clientCustomerId = '';
    public static $_method = '';#请求的接口方法
    public static $_methodTo = ''; //转发时的接口方法
    public static $_cilentDb = ''; //wms数据库
    public static $_appToken = ''; //Token 号
    public static $_appKey = ''; //验签KEY
    public static $_appSecret = ''; //生成验签的appSecret
    public static $_timeStamp = ''; //时间
    private static $_data = '';#请求的接口参数
    private static $_parentMsgId = '';#父日志ID
    public static $_messageId = '';
    private static $_funcInstance = '';#公共方法类实例
    private static $_msgId = '';
    public static $_outputContent = '';#标准输出内容
    public static $_toApiUrl = '';//转发目的地接口地址
    public static $_toApiMethod = '';//转发目的地接口方法
    public static $_toApiParams = '';//转发目的地参数
    public static $_toApiReturn = '';//转发目的地返回结果


    /**
     * 接口请求，由OMS主动发起
     * @param $customerId
     * @param $method
     * @param $params
     * @param int $parentMsgId 父日志ID，异步接口需要传递
     * @return array
     */
    public function process()
    {
        //系统级参数解析
        self::__parseParams($_REQUEST);
        self::$_outputContent = array('returnFlag' => '0', 'returnCode' => '0001', 'returnDesc' => 'fail', 'resultInfo' => '');

        //系统级参数初始化校验
        $this->__filterParams($requestFrom, $requestTo, $async, $apiHandler, $callbackId);

        //校验api的类和方法是否存在
        if (!isset($apiHandler['api_handler_class']) || !$apiHandler['api_handler_class'] || !isset($apiHandler['api_handler_method']) || !$apiHandler['api_handler_method']) {
            self::$_outputContent['returnDesc'] = '接口转发类与方法不存在';
            self::output(self::$_outputContent);
        }

        self::$_funcInstance = new func();

        try {
            //获取客户、erp、wms关系
            $customer = self::$_funcInstance->getPartnerInfo(self::$_customerId);
            $customer_id = $customer['customer_id'];
            $erp_bn = $customer['erp_bn'];
            $erp_api = $customer['erp_api'];
            $erp_secret = $customer['erp_api_secret'];
            $erp_api_ver = $customer['erp_api_ver'];
            $wms_bn = $customer['wms_bn'];
            $wms_api = $customer['wms_api'];
            $wms_api_ver = $customer['wms_api_ver'];

            //客户ID
            if (!$customer_id) {
                self::$_outputContent['returnDesc'] = '客户ID不正确';
                self::output(self::$_outputContent);
            }

            //客户关联的仓库、erp、wms编码
            if (!$erp_bn || !$wms_bn) {
                self::$_outputContent['returnDesc'] = 'erp或wms编码不存在';
                self::output(self::$_outputContent);
            }

            //验证客户接口权限
            if (!self::$_funcInstance->checkRole(self::$_customerId)) {
                self::$_outputContent['returnDesc'] = '客户无此接口权限';
                self::output(self::$_outputContent);
            }

            //获取目的地接口信息:地址、密钥等
            if ($requestTo == 'erp') { //oms->erp
                $toSystemBn = $erp_bn;
                $toApi = $erp_api;
                $toApiSecret = $erp_secret;
                $toApiVer = $erp_api_ver;
            } elseif ($requestTo == 'wms') { //oms->wms
                $toSystemBn = $wms_bn;
                $toApi = $wms_api;
                $toApiSecret = self::$_appSecret;
                $toApiVer = $wms_api_ver;
            } else {
                self::$_outputContent['returnDesc'] = '请求目标不存在';
                self::output(self::$_outputContent);
            }

            //获取接口转发对应的类、方法
            $apiClass = $apiHandler['api_handler_class'];
            $apiMethod = $apiHandler['api_handler_method'];

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
                    //生成日志ID
                    self::$_msgId = self::$_funcInstance->makeMsgId();

                    $instance = new $interfaceClassName();
                    if (method_exists($instance, $apiMethod)) {
                        $instance::$warehouseId = inner_service::$_cilentDb;
                        if ($requestTo == 'erp') {
                            $instance::$customerId = $customer_id;
                            $instance::$erpBn = $toSystemBn;
                            $instance::$erpApi = $toApi;
                            $instance::$erpApiSecret = $toApiSecret;
                            $instance::$erpApiVer = $toApiVer;
                        } else {
                            $instance::$customerId = self::$_clientCustomerId;
                            $instance::$wmsBn = $toSystemBn;
                            $instance::$wmsApi = $toApi;
                            $instance::$wmsApiSecret = $toApiSecret;
                            $instance::$wmsApiVer = $toApiVer;
                        }
                        self::$_outputContent = $instance->$apiMethod(self::$_data);

                        //addon由转发类返回，目的是为了记录日志----------
                        if (isset(self::$_outputContent['addon'])) {
                            self::$_toApiUrl = self::$_outputContent['addon']['api_url'];
                            self::$_toApiMethod = self::$_outputContent['addon']['api_method'];
                            self::$_toApiParams = self::$_outputContent['addon']['api_params'];
                            self::$_toApiReturn = self::$_outputContent['addon']['return_msg'];
                            unset(self::$_outputContent['addon']);
                        }

                        self::$_outputContent['msg_id'] = self::$_msgId;
                    } else {
                        self::$_outputContent['returnDesc'] = '无法找到接口实例方法';
                        self::$_outputContent['msg_id'] = self::$_msgId;
                    }
                } else {
                    self::$_outputContent['returnDesc'] = '无法找到接口实例';
                }
            } else {
                self::$_outputContent['returnDesc'] = '无法找到请求的接口文件';
            }

            //---接口日志记录
            self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_toApiUrl, self::$_toApiMethod, self::$_toApiParams, self::$_toApiReturn, self::$_parentMsgId);

            //记录订单接口日志
            if (YII_INTERFACE_LOG_FLAG) {
                $utilObj = new util();
                $xmlObj = new xml();
                $sendArr = array('header' => self::$_data);
                $sendXml = $utilObj->arrayToXml($sendArr, $xmlObj);
                self::$_funcInstance->writeInterfaceLog(self::$_toApiMethod, $sendXml, array($customer_id => $sendArr), '', array($customer_id => self::$_outputContent), array($customer_id => self::$_toApiReturn), array($customer_id => self::$_msgId), array($customer_id => $toApi));
            }

            //返回
            self::output(self::$_outputContent);
        } catch (Exception $e) {
            $this->send_user_error('S007', '接口异常');
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
            self::$_outputContent['returnDesc'] = '数据为空';
            self::output(self::$_outputContent);
        }

        $systemParamsData = array();
        foreach (self::$_systemParams as $v) {
            $systemParamsData[$v] = isset($request[$v]) ? $request[$v] : '';
        }

        self::$_customerId = isset($systemParamsData['customer_id']) ? $systemParamsData['customer_id'] : '';
        self::$_method = isset($systemParamsData['method']) ? $systemParamsData['method'] : '';
        self::$_data = isset($systemParamsData['data']) ? json_decode($systemParamsData['data'], true) : '';
        self::$_messageId = isset($systemParamsData['messageid']) ? $systemParamsData['messageid'] : '';
        self::$_parentMsgId = isset($systemParamsData['msg_id']) ? $systemParamsData['msg_id'] : '';
    }

    /**
     * 系统级参数初始化验证
     * @return array
     */
    private function __filterParams(&$from, &$to, &$async, &$apiHandler, &$callbackId = '')
    {
        //校验系统级参数是否完整
        if (!self::$_method || !self::$_messageId || !self::$_customerId || !self::$_data) {
            self::$_outputContent['returnDesc'] = '系统级参数不完整';
            self::output(self::$_outputContent);
            return false;
        }
        //校验method
        global $db;
        $sql = ' SELECT api_id,message_id,callback_id,api_class,api_method,api_from,api_to,async,client_db FROM t_api_list WHERE api_id=:api_id AND is_valid=1 ';
        $model = $db->prepare($sql);
        $model->bindParam(':api_id', self::$_method);
        $model->execute();
        $apiInfo = $model->fetch(PDO::FETCH_ASSOC);
        if (empty($apiInfo)) {
            self::$_outputContent['returnDesc'] = '系统级参数method:' . self::$_method . '不存在';
            self::output(self::$_outputContent);
            return false;
        } elseif ($apiInfo['message_id'] == '' || $apiInfo['callback_id'] == '' || $apiInfo['api_class'] == '' || $apiInfo['api_method'] == '' || $apiInfo['api_from'] == '' || $apiInfo['api_to'] == '' || $apiInfo['async'] == '' || $apiInfo['client_db'] == '') {
            self::$_outputContent['returnDesc'] = '系统级参数method:' . self::$_method . '信息配置不完整';
            self::output(self::$_outputContent);
            return false;
        }
        //校验messageid
        if ($apiInfo['message_id'] != self::$_messageId) {
            self::$_outputContent['returnDesc'] = '系统级参数中messageid:' . self::$_messageId . '与系统配置不匹配';
            self::output(self::$_outputContent);
            return false;
        }
        //校验货主，获取货主对应的wms编码
        $sql = "SELECT a.customer_id,a.app_secret,b.wms_code FROM t_base_customer a LEFT JOIN t_bind_relation b ON a.customer_id=b.customer_id WHERE a.customer_id=:customer_id AND a.active_flag='Y' AND a.is_valid=1 AND b.is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':customer_id', self::$_customerId);
        $model->execute();
        $rsCustomer = $model->fetch(PDO::FETCH_ASSOC);
        if (empty($rsCustomer)) {
            self::$_outputContent['returnDesc'] = '系统级参数中货主customerid:' . self::$_customerId . '不存在';
            self::output(self::$_outputContent);
            return false;
        } elseif ($rsCustomer['app_secret'] == '' || $rsCustomer['wms_code'] == '') {
            self::$_outputContent['returnDesc'] = '货主信息和绑定关系配置错误';
            self::output(self::$_outputContent);
            return false;
        }
        //获取生成验签的appSecret
        $sql = "SELECT wms_code,app_token,app_key,cilent_customerid,app_secret FROM t_base_wms WHERE wms_code=:wms_code AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':wms_code', $rsCustomer['wms_code']);
        $model->execute();
        $rsWms = $model->fetch(PDO::FETCH_ASSOC);
        if (empty($rsWms) || $rsWms['cilent_customerid'] == '' || $rsWms['app_secret'] == '' || $rsWms['app_token'] == '' || $rsWms['app_key'] == '') {
            self::$_outputContent['returnDesc'] = '系统wms配置错误';
            self::output(self::$_outputContent);
            return false;
        }

        //系统级参数初始化
        if ($apiInfo['api_to'] == 'erp') {
            self::$_appSecret = $rsCustomer['app_secret'];
        } elseif ($apiInfo['api_to'] == 'wms') {
            self::$_appSecret = $rsWms['app_secret'];
        }
        self::$_appToken = $rsWms['app_token'];
        self::$_appKey = $rsWms['app_key'];
        self::$_cilentDb = $apiInfo['client_db'];
        self::$_clientCustomerId = $rsWms['cilent_customerid'];
        self::$_methodTo = $apiInfo['callback_id'];

        $from = $apiInfo['api_from'];
        $to = $apiInfo['api_to'];
        $async = $apiInfo['async'] == 'true' ? 'true' : 'false';
        $apiHandler['api_handler_class'] = $apiInfo['api_class'];
        $apiHandler['api_handler_method'] = $apiInfo['api_method'];
        $callbackId = $apiInfo['callback_id'] != '' ? $apiInfo['callback_id'] : '';

        return true;
    }

    public function send_user_error($error_code, $error_msg = '', $data = '')
    {
        $content = array(
            'returnFlag' => '0',
            'returnCode' => $error_code,
            'returnDesc' => $error_msg ?: self::$sysMsgCode[$error_code],
            'resultInfo' => $data
        );
        self::output($content);
    }

    public static function output($content = '')
    {
        echo json_encode($content);
        exit;
    }
}
