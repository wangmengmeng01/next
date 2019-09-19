<?php

/**
 * 菜鸟接口入口类
 */
class cainiao_service
{
    const TIMEOUT = '30'; //默认预警超时时间
    public static $_systemParams = array('method', 'customer_id', 'timestamp', 'format', 'v', 'sign_method', 'sign');
    public static $_customerId = '';            //请求的货主
    public static $_accessToken = '';           //授权口令
    public static $_method = '';                //请求的接口方法
    private static $_sign = '';                //请求的签名
    public static $_signMethod = '';            //签名方法
    public static $_timeStamp = '';            //时间
    public static $_data = '';                    //数据
    public static $_appKey = '';                //应用名称
    public static $_secret = '';                //请求秘钥
    public static $_format = 'xml';            //返回格式
    public static $_v = '';                    //接口版本
    public static $_platFormElec = '';          //电子面单平台
    private static $_fromApiVer = '';           //调用方API版本号（ERP或WMS版本号）
    private static $_to = '';                   //目的方（erp或wms）
    private static $_apiClass = '';             //接口调用类名
    private static $_apiMethod = '';            //接口调用方法名
    private static $_start_time;                //API执行开始时间
    public static $_finish = false;            //完成标识
    private static $_funcInstance = '';        //公共方法类实例
    private static $_requestUrl = CAINIAO_API_URL;    //当前请求URL
    private static $_msgId = '';                //日志ID
    public static $_outputContent = '';        //标准输出内容
    public static $_errorFlag = false;             //错误标志
    public static $_errorMsg = '';              //错误信息
    public static $_toApiUrl = '';                //目的地接口地址
    public static $_toApiParams = '';            //目的地接口发送参数
    public static $_toApiReturn = '';            //目的地返回结果
    public static $_queryFlag = false;          //是否是查询接口标志
    public static $_cnReturnStr = '';           //菜鸟原始返回数据   
    public static $_requstCainiaoTime = '';     //OMS请求菜鸟的时间
    public static $_responseOmsTime = '';       //菜鸟响应OMS时间
    public static $_responseWmsTime = '';       //OMS响应WMS时间
    public static $_innerErrorFlag = false;     //OMS内部报错

    public function process()
    {
        //记录接收到的数据日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_cainiao_receive.log';
            error_log(print_r($_REQUEST, 1), 3, LOG_PATH . $logName);
            error_log(print_r(file_get_contents("php://input"), 1), 3, LOG_PATH . $logName);
        }

        ignore_user_abort();                        //设置与客户机断开是否会终止脚本的执行。
        set_time_limit(0);                            //设置程序执行时间
        header('Connection: close');                //关闭持久连接

        //判断magic_quotes_gpc是否开启。开启则对单引号等字符进行转义
        if (get_magic_quotes_gpc()) {
            self::strip_magic_quotes($_REQUEST);
        }

        self::$_funcInstance = new func();

        try {
            self::$_start_time = $_SERVER['REQUEST_TIME'] ? $_SERVER['REQUEST_TIME'] : time();

            //系统级参数解析
            self::__parseParams($_REQUEST);

            //系统级参数初始化校验
            $this->__filterParams();

            //获取接口对应的类、方法
            $apiMethod = self::$_apiMethod;
            self::$_fromApiVer = self::$_v;

            //生成日志ID
            self::$_msgId = self::$_funcInstance->makeMsgId();

            //签名校验
            $signClassFile = sprintf(API_ROOT . '/router/sign/cainiao/v%s.php', self::$_fromApiVer);
            if (!file_exists($signClassFile)) {
                //获取标准签名文件
                $signClassFile = API_ROOT . '/router/sign/cainiao/v1.0.php';
            }
            if (file_exists($signClassFile)) {
                include addslashes($signClassFile);
                if (class_exists('sign')) {
                    $signObj = new sign();
                    if (!$signObj->check(self::$_secret, self::$_data, self::$_sign)) {
                        $this->send_user_error('S001', '签名错误');
                    }
                } else {
                    $this->send_user_error('S002', '没有找到签名方法');
                }
            } else {
                $this->send_user_error('S002', '没有找到签名文件');
            }

            //数据转换
            $requestData = array();
            $formatClassFile = sprintf(API_ROOT . '/router/format/cainiao/v%s.php', self::$_fromApiVer);
            if (!file_exists($formatClassFile)) {
                if (self::$_platFormElec == 'JD') {
                    //取第三个版本
                    $formatClassFile = API_ROOT . '/router/format/cainiao/v3.0.php';
                } else {
                    //取第一个版本
                    $formatClassFile = API_ROOT . '/router/format/cainiao/v1.0.php';
                }
            }
            if (file_exists($formatClassFile)) {
                include_once addslashes($formatClassFile);
                if (class_exists('format')) {
                    $formatObj = new format();
                    if (method_exists($formatObj, $apiMethod)) {
                        $requestData = $formatObj->$apiMethod(self::$_data);
                    } else {
                        $requestData = $formatObj->request(self::$_data);
                    }
                } else {
                    $this->send_user_error('S002', '找不到数据格式化的方法');
                }
            } else {
                $this->send_user_error('S002', '找不到数据格式化的文件');
            }

            //接口请求处理
            $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/cainiao/v%s/%s.php', self::$_to, self::$_v, self::$_apiClass);
            if (!file_exists($interfaceClassFile)) {
                //调用标准路径下实例
                $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/cainiao/v1.0/%s.php', self::$_to, self::$_apiClass);
                if (!file_exists($interfaceClassFile)) {
                    $this->send_user_error('S002', '找不到接口调用文件', $requestData);
                }
            }

            if (file_exists($interfaceClassFile)) {
                $interfaceClassName = self::$_apiClass;
                include_once addslashes($interfaceClassFile);
                if (class_exists($interfaceClassName)) {
                    $instance = new $interfaceClassName();
                    if (method_exists($instance, $apiMethod)) {
                        self::$_outputContent = $instance->$apiMethod($requestData);

                        //addon由转发类返回，目的是为了记录日志
                        if (isset(self::$_outputContent['addon'])) {
                            //商家授权方法就返回我们自己返回给RYD的数据
                            if (self::$_method != 'taobao.wlb.waybill.i.seller.authorization') {
                                self::$_toApiUrl = self::$_outputContent['addon']['api_url'];
                                self::$_toApiParams = self::$_outputContent['addon']['api_params'];
                            }
                            self::$_toApiReturn = self::$_outputContent['addon']['return_msg'];
                            self::$_toApiReturn = str_replace('&','',self::$_toApiReturn);
                            unset(self::$_outputContent['addon']);
                        }
                    } else {
                        $this->send_user_error('S002', '没有找到接口请求的方法', $requestData);
                    }
                } else {
                    $this->send_user_error('S002', '没有找到接口请求的文件', $requestData);
                }
            }
            self::$_customerId = empty($requestData['customerCode']) ? '' : $requestData['customerCode'];

            //记录请求日志
            self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_requestUrl, self::$_method, $_REQUEST, self::$_toApiReturn);

            if (self::$_method != 'taobao.wlb.waybill.i.seller.authorization') {
                //记录转发目的地返回日志,self::$_msgId作为此日志的父ID         
                self::$_funcInstance->writeLog('', self::$_customerId, self::$_toApiUrl, self::$_method, self::$_toApiParams, self::$_toApiReturn, self::$_msgId);
            } else {
                self::$_funcInstance->writeLog('', self::$_customerId, self::$_requestUrl, self::$_method, '', self::$_toApiReturn, self::$_msgId);
            }

            //记录菜鸟电子面单日志（页面查看）
            if (YII_INTERFACE_LOG_FLAG) {
                //wms请求oms日志
                self::$_funcInstance->writeCainiaoInterfaceLog(self::$_customerId, self::$_appKey, self::$_method, self::$_data, self::$_requestUrl, $requestData, self::$_toApiReturn, self::$_timeStamp, date("Y-m-d H:i:s"), self::$_msgId, '', self::$_requestUrl);

                //oms请求cainiao日志
                if (self::$_method != 'taobao.wlb.waybill.i.seller.authorization' && !self::$_innerErrorFlag) {
                    self::$_funcInstance->writeCainiaoInterfaceLog(self::$_customerId, self::$_appKey, self::$_method, self::$_data, self::$_requestUrl, $requestData, self::$_cnReturnStr, self::$_requstCainiaoTime, self::$_responseOmsTime, '', self::$_msgId, self::$_toApiUrl);
                }
            }
            //接口执行完成标识置为true
            self::$_finish = true;
            /*
            //返回结果前，记录天蚕日志
            if (CHECK_TIANCAN == 1){
                global $check_tc;
                $check_tc->write_system_log(array(API_RETURN_DATA_CODE_FIELD=>'success', API_RETURN_DATA_MESSAGE_FIELD=>'成功！'));
            }*/

            //返回结果
            if (self::$_platFormElec != 'JD') {
                self::output(self::$_outputContent['msg']);
            } else {
                self::output(self::$_toApiReturn);
            }
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
        } else {
            if (!isset($request['data'])) {
                $request['data'] = file_get_contents("php://input");
            }
        }

        self::$_method = $request['method'];
        self::$_sign = $request['sign'];
        self::$_timeStamp = $request['timestamp'];
        self::$_data = isset($request['data']) ? $request['data'] : '';
        self::$_signMethod = isset($request['sign_method']) ? $request['sign_method'] : 'md5';
        self::$_format = isset($request['format']) ? $request['format'] : 'xml';
        self::$_v = isset($request['v']) ? $request['v'] : '2.0';
        self::$_appKey = $request['appkey'];
        self::$_platFormElec = empty($request['platform_elec']) ? 'CAINIAO' : $request['platform_elec'];
    }

    /**
     * 系统级参数校验及初始化
     * @return bool
     */
    private function __filterParams()
    {
        //校验系统级参数完整性
        if (!self::$_method || !self::$_sign || !self::$_timeStamp || !self::$_signMethod || !self::$_format || !self::$_v || !self::$_appKey || !self::$_platFormElec) {
            $this->send_user_error('S003', '系统级参数不完整');
            return false;
        }
        //校验xml数据
        if (!self::$_data) {
            $this->send_user_error('S003', '发送的xml数据为空');
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
            $this->send_user_error('S003', '系统级参数method:' . self::$_method . '不存在');
            return false;
        } elseif (empty($apiInfo['api_class']) || empty($apiInfo['api_method']) || empty($apiInfo['api_from']) || empty($apiInfo['api_to'])) {
            $this->send_user_error('S005', '系统中系统级参数method:' . self::$_method . '信息配置不完整');
            return false;
        }
        //获取不同接口调用的方法
        self::$_to = $apiInfo['api_to'];
        self::$_apiClass = $apiInfo['api_class'];
        self::$_apiMethod = $apiInfo['api_method'];

        $findSecretSql = "SELECT app_secret FROM csk_app_setting WHERE app_key = :app_key";
        $findSecretModel = $db->prepare($findSecretSql);
        $findSecretModel->bindParam(':app_key', self::$_appKey);
        $findSecretModel->execute();
        $appSecretInfo = $findSecretModel->fetch(PDO::FETCH_ASSOC);
        if (!empty($appSecretInfo)) {
            self::$_secret = $appSecretInfo['app_secret'];
        } else {
            $this->send_user_error('S005', '尚未有该' . self::$_appKey . '应用的配置信息');
        }
        return true;
    }

    public function send_user_error($error_code, $error_msg = '', $requestData = array())
    {
        self::$_errorFlag = true;

        if (self::$_platFormElec != 'JD') {
            $content = array(
                'code' => $error_code,
                'msg' => $error_msg,
                'sub_code' => '',
                'sub_msg' => ''
            );
        } else {
            $content = array(
                'statusCode' => $error_code,
                'statusMessage' => $error_msg,
            );
        }

        self::$_toApiReturn = self::output($content, false);
        //记录错误日志
        self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_requestUrl, self::$_method, $_REQUEST, self::$_toApiReturn);
        //记录详细接口日志
        if (YII_INTERFACE_LOG_FLAG) {
            self::$_funcInstance->writeCainiaoInterfaceLog(self::$_customerId, self::$_appKey, self::$_method, self::$_data, self::$_requestUrl, $requestData, self::$_toApiReturn, self::$_timeStamp, date("Y-m-d H:i:s"), self::$_msgId, '', self::$_requestUrl);
        }

        //返回结果
        self::output($content);
    }

    //接口标准输出函数
    public static function output($content = '', $forceOutput = true)
    {
        if (self::$_platFormElec != 'JD') {
            if ($forceOutput === true) {//默认强制输出
                if (self::$_format == 'xml') {
                    //判断在OMS中是否报错
                    if (!self::$_errorFlag) {
                        if (self::$_toApiReturn != '' && xml::isXml(self::$_toApiReturn)) {
                            $xmlReturn = self::$_toApiReturn;
                        }
                    } else {
                        //异常返回格式<error_response>
                        $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                        $xmlReturn .= '<error_response>';
                        $xmlReturn .= util::array2xml($content);
                        $xmlReturn .= '</error_response>';
                    }
                    echo $xmlReturn;
                } else {
                    if (self::$_toApiReturn != '') {
                        echo self::$_toApiReturn;
                    } else {//返回的json格式为空，以异常格式返回
                        $rs['error_response'] = $content;
                        echo json_encode($rs);
                    }
                }
                exit;
            } else {
                if (self::$_method != 'taobao.wlb.waybill.i.seller.authorization') {
                    if (self::$_format == 'xml') {
                        //判断在OMS中是否报错
                        if (!self::$_errorFlag) {
                            if (self::$_toApiReturn != '' && xml::isXml(self::$_toApiReturn)) {
                                $xmlReturn = self::$_toApiReturn;
                            }
                        } else {
                            //异常返回格式<error_response>
                            $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                            $xmlReturn .= '<error_response>';
                            $xmlReturn .= util::array2xml($content);
                            $xmlReturn .= '</error_response>';
                        }
                    } else {
                        if (self::$_toApiReturn != '') {
                            $xmlReturn = self::$_toApiReturn;
                        } else {//返回的json格式为空，以异常格式返回
                            $rs['error_response'] = $content;
                            $xmlReturn = json_encode($rs);
                        }
                    }
                    return $xmlReturn;
                } else {
                    if (!self::$_errorFlag) {//未报错
                        if (self::$_format == 'xml') {
                            $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                            $xmlReturn .= '<response>';
                            $xmlReturn .= util::array2xml($content);
                            $xmlReturn .= '</response>';
                        } else {
                            if (self::$_toApiReturn != '') {
                                $xmlReturn = self::$_toApiReturn;
                            } else {//返回的json格式为空，以异常格式返回
                                $rs['error_response'] = $content;
                                $xmlReturn = json_encode($rs);
                            }
                        }
                    } else {
                        if (self::$_format == 'xml') {
                            $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                            $xmlReturn .= '<error_response>';
                            $xmlReturn .= util::array2xml($content);
                            $xmlReturn .= '</error_response>';
                        } else {
                            if (self::$_toApiReturn != '') {
                                $xmlReturn = self::$_toApiReturn;
                            } else {//返回的json格式为空，以异常格式返回
                                $rs['error_response'] = $content;
                                $xmlReturn = json_encode($rs);
                            }
                        }
                    }
                    return $xmlReturn;
                }
            }
        } else {
            if ($forceOutput === true) {//默认强制输出
                echo $content;
                exit;
            } else {
                return $content;
            }
        }
    }

    //去除多余的反斜线
    private function strip_magic_quotes(&$var)
    {
        foreach ($var as $k => $v) {
            if (is_array($v)) {
                self::strip_magic_quotes($var[$k]);
            } else {
                $var[$k] = stripcslashes($v);
            }
        }
    }

} ?>