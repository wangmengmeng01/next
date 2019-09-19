<?php

/**
 * Notes:拼多多接口入口类
 * Date: 2019/3/21
 * Time: 10:27
 */
class pdd_service
{
    public static $_method;                     //请求的接口方法
    public static $_timeStamp;                  //时间
    private static $_sign = '';                //请求的签名
    public static $_v = '';                    //接口版本
    public static $_data;                       //数据
    public static $_appKey = '';                //应用名称
    public static $_secret = '';                //请求秘钥
    public static $_platFormElec = '';          //电子面单平台
    protected static $_toApiReturn;            //返回数据
    public static $_pddReturnStr = '';           //拼多多原始返回数据
    private static $_funcInstance = '';         //公共方法类实例
    private static $_to = '';                   //目的方（erp或wms）
    private static $_apiClass = '';             //接口调用类名
    public static $_toApiParams = '';          //访问参数
    public static $_outputContent = '';        //标准输出内容
    private static $_msgId = '';                //日志ID
    public static $_requestUrl = PDD_API_URL;   //当前请求URL
    private static $_toApiUrl = PDD_URL;        //拼多多请求url
    public static $_client_secret = CLIENT_SECRET;       //拼多多开发平台client_secret
    public static $_client_id = CLIENT_ID;              //POP分配给应用的client_id
    public static $_errorMsg = '';              //错误信息
    public static $_requstPddTime = '';         //OMS请求拼多多的时间
    public static $_responseOmsTime = '';       //拼多多响应OMS时间
    protected $apiFunc = array(
        'pdd.waybill.authorization' => array('class' => 'pddWaybillAuthorization', 'fct' => 'authorize', 'to' => 'erp'),
        'pdd.waybill.get' => array('class' => 'pddWaybillGet', 'fct' => 'get', 'to' => 'erp'),
        'pdd.waybill.search' => array('class' => 'pddWaybillSearch', 'fct' => 'search', 'to' => 'erp'),
        'pdd.waybill.cancel' => array('class' => 'pddWaybillCancel', 'fct' => 'cancel', 'to' => 'erp'),
        'pdd.cloudprint.stdtemplates.get' => array('class' => 'pddCloudprintStdtemplatesGet', 'fct' => 'get', 'to' => 'erp'),
    );

    /**
     * 处理逻辑
     */
    public function process()
    {
        //记录接收到的数据日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_pdd_service.log';
            error_log(print_r($_REQUEST, 1) . PHP_EOL, 3, LOG_PATH . $logName);
        }
        ignore_user_abort();                        //设置与客户机断开是否会终止脚本的执行。
        set_time_limit(0);                          //设置程序执行时间
        header('Connection: close');                //关闭持久连接

        //判断magic_quotes_gpc是否开启。开启则对单引号等字符进行转义
        if (get_magic_quotes_gpc()) {
            self::strip_magic_quotes($_REQUEST);
        }
        self::$_funcInstance = new func();
        try {
            //生成日志ID
            self::$_msgId = self::$_funcInstance->makeMsgId();
            if (isset($_REQUEST['auth_session'])) {
                self::$_method = 'pdd.waybill.authorization';
                self::$_apiClass = self::$_apiClass = $this->apiFunc[self::$_method]['class'];
                $requestData = $_REQUEST;
                self::$_timeStamp = date("Y-m-d H:i:s");
                self::$_to = 'erp';
                $apiMethod = $this->apiFunc[self::$_method]['fct'];
                if (!isset(self::$_apiClass) || !isset($apiMethod)) {
                    $this->send_user_error('S002', '没有找到对应的接口信息');
                }
            } else {

                //解析系统级参数
                self::__parseParams($_REQUEST);
                //系统级参数初始化校验
                $this->__filterParams();
                //匹配接口处理类，方法
                self::$_apiClass = $this->apiFunc[self::$_method]['class'];
                $apiMethod = $this->apiFunc[self::$_method]['fct'];
                self::$_to = $this->apiFunc[self::$_method]['to'];
                self::$_v = '1.0';

                if (!isset(self::$_apiClass) || !isset($apiMethod)) {
                    $this->send_user_error('S002', '没有找到对应的接口信息');
                }

                //校验签名
                $signClassFile = sprintf(API_ROOT . 'router/sign/pdd/v%s.php', self::$_v);
                if (!file_exists($signClassFile)) {
                    //获取标准签名文件
                    $signClassFile = API_ROOT . 'router/sign/pdd/v1.0.php';
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
            $formatClassFile = API_ROOT . '/router/format/pdd/v1.0.php';
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
                    $this->send_user_error('S002', '没有找到数据格式化的方法');
                }
            } else {
                $this->send_user_error('S002', '没有找到数据格式化的文件');
            }
            }
            //数据过滤
            $filterClassFile = sprintf(API_ROOT . 'router/filter/pdd/v%s/%s.php', self::$_v, 'filter' . ucfirst(self::$_apiClass));
            if (!file_exists($filterClassFile)) {
                //取第一个版本
                $filterClassFile = sprintf(API_ROOT . 'router/filter/pdd/v1.0/%s.php', 'filter' . ucfirst(self::$_apiClass));
            }
            if (file_exists($filterClassFile)) {
                include_once(addslashes($filterClassFile));
                $filterClassName = 'filter' . ucfirst(self::$_apiClass);
                if (class_exists($filterClassName)) {
                    $filterObj = new $filterClassName();
                    if (method_exists($filterObj, $apiMethod)) {
                        $filterRs = $filterObj->$apiMethod($requestData);
                        if ($filterRs['error_response']['error_code'] == 'S003') {
                            $this->send_user_error('S001', $filterRs['error_response']['error_msg'], $requestData);
                        }
                    } else {
                        $this->send_user_error('S002', '没有找到数据过滤的方法', $requestData);
                    }
                }
            }
            //接口请求处理
            $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/pdd/v%s/%s.php', self::$_to, self::$_v, self::$_apiClass);
            if (!file_exists($interfaceClassFile)) {
                //调用标准路径下实例
                $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/pdd/v1.0/%s.php', self::$_to, self::$_apiClass);
                if (!file_exists($interfaceClassFile)) {
                    $this->send_user_error('S002', '没有找到接口调用文件', $requestData);
                }
            }
            $interfaceClassName = self::$_apiClass;
            include_once addslashes($interfaceClassFile);
            if (class_exists($interfaceClassName)) {
                $instance = new $interfaceClassName();
                if (method_exists($instance, $apiMethod)) {
                    self::$_outputContent = $instance->$apiMethod($requestData);
                    //addon由转发类返回，目的是为了记录日志
                    //商家授权方法就返回我们自己返回给RYD的数据
                    if (self::$_method != 'pdd.waybill.authorization') {
                        self::$_toApiUrl = self::$_outputContent['addon']['api_url'];
                        self::$_toApiParams = self::$_outputContent['addon']['api_params'];
                    }
                    self::$_toApiReturn = self::$_outputContent['addon']['return_msg'];
                    unset(self::$_outputContent['addon']);
                } else {
                    $this->send_user_error('S002', '没有找到接口请求的方法', $requestData);
                }
            } else {
                $this->send_user_error('S002', '没有找到接口请求的文件', $requestData);
            }
            //记录拼多多电子面单日志
            //wms请求oms日志
            self::$_funcInstance->pddLogWrite(self::$_appKey, self::$_method, $_REQUEST, $requestData, self::$_toApiReturn, self::$_timeStamp, date("Y-m-d H:i:s"), self::$_msgId, '', self::$_requestUrl);
            //oms请求pdd日志
            if (self::$_method != 'pdd.waybill.authorization') {
                self::$_funcInstance->pddLogWrite(self::$_appKey, self::$_method, self::$_toApiParams, $requestData, self::$_pddReturnStr, self::$_requstPddTime, self::$_responseOmsTime, '', self::$_msgId, self::$_toApiUrl);
            }
            //返回结果
            self::output(self::$_outputContent['msg']);
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
        }
        self::$_method = $request['method'];
        self::$_sign = $request['sign'];
        self::$_timeStamp = $request['timestamp'];
        self::$_data = $request['data'];
        self::$_v = isset($request['v']) ? $request['v'] : '1.0';
        self::$_appKey = $request['appkey'];
        self::$_platFormElec = empty($request['platform_elec']) ? 'PDD' : $request['platform_elec'];
    }

    /**
     * 系统级参数校验及初始化
     * @return bool
     */
    private function __filterParams()
    {
        if (!self::$_method || !self::$_sign || !self::$_timeStamp || !self::$_v || !self::$_appKey || !self::$_data || !self::$_platFormElec) {
            self::send_user_error('S003', '系统级参数不完整');
            return false;
        }
        $appSecretInfo = OmsDatabase::$oms_db->fetchOne('app_secret', 'csk_app_setting', 'app_key = :app_key', array(':app_key' => self::$_appKey));
        if (!empty($appSecretInfo)) {
            self::$_secret = $appSecretInfo['app_secret'];
        } else {
            $this->send_user_error('S005', '尚未有该' . self::$_appKey . '应用的配置信息');
        }


    }

    /***
     * Notes: 接口前置错误输出
     * Date: 2019/4/4
     * Time: 13:21
     * @param $error_code 错误码
     * @param string $error_msg 错误信息
     * @param array $requestData 请求数据
     */
    public function send_user_error($error_code, $error_msg = '', $requestData = array())
    {
        $content = array(
            'error_msg' => $error_msg,
            'sub_msg' => '',
            'sub_code' => '',
            'error_code' => $error_code,
            'request_id' => '',
        );
        self::$_toApiReturn = self::output($content, false);
        self::$_funcInstance->pddLogWrite(self::$_appKey, self::$_method, $_REQUEST, $requestData, self::$_toApiReturn, self::$_timeStamp, date("Y-m-d H:i:s"), self::$_msgId, '', self::$_requestUrl);
        self::output($content);
    }

    /***
     * Notes: 接口标准输出函数
     * Date: 2019/4/4
     * Time: 13:20
     * @param string $content 输出内容
     * @param bool $forceOutput 是否直接输出
     * @return false|string
     */
    public static function output($content = '', $forceOutput = true)
    {
        if (self::$_toApiReturn != '') {
            $jsonReturn = self::$_toApiReturn;
        } else {//返回的json格式为空，以异常格式返回
            $rs['error_response'] = $content;
            $jsonReturn = json_encode($rs);
        }
        if ($forceOutput === true) {//默认强制输出
            echo $jsonReturn;
            exit();
        } else {
            return $jsonReturn;
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
}