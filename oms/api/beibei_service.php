<?php
/**
 * Notes:贝贝接口入口类
 * Date: 2019/6/24
 * Time: 13:32
 */

class beibei_service
{
    public static $_method = '';                //请求的接口方法
    private static $_apiConfig = array();            //接口配置相关参数
    private static $_to = '';                   //目的方（erp或wms）
    private static $_apiClass = '';             //接口调用类名
    private static $_apiMethod = '';            //接口调用方法名
    public static $_toApiCode = '';             //目的方API编码（ERP或WMS编码
    private static $_sign = '';                 //请求的签名
    public static $_appId = '';                //贝贝给出appId
    public static $_session = BEIBEI_SESSION;            //贝贝给出session
    public static $_systemParams = array('method', 'format', 'timestamp', 'appId', 'session', 'version', 'data');
    public static $_customerId = '';            //客户id  即货主编码
    public static $_appSecret = '';             //客户在WMS系统中的appSecret
    public static $_timeStamp = '';            //时间
    public static $_data = '';                    //数据
    public static $_v = '';                    //接口版本
    public static $_format = '';            //返回格式
    private static $_fromApiCode = '';          //调用方API编码（ERP或WMS编码）
    private static $_fromApiVer = '';           //调用方API版本号（ERP或WMS版本号）
    private static $_toApiVer = '';             //目的方API版本号（ERP或WMS版本号）
    private static $_requestUrl = BEIBEI_API_URL;    //当前请求URL
    private static $_msgId = '';                //日志ID
    private static $_funcInstance = '';        //公共方法类实例
    public static $_outputContent = '';        //标准输出内容
    public static $_toApiUrl = '';                //目的地接口地址
    public static $_toApiParams = '';            //目的地接口发送参数
    public static $_toApiReturn = '';            //目的地返回结果
    public static $_req_time = '';              //erp|wms请求oms时间
    public static $_mid_req_time = '';          //oms请求wms|erp时间
    public static $_mid_resp_time = '';         //wms|erp响应oms时间
    public static $_resp_time = '';             //oms响应wms|erp时间

    public function process()
    {
        self::$_apiConfig = (new api_config())->getApiConfig('beibei');
        self::$_req_time = util::microtime_float();

        //记录接收到的数据日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_oms_beibei_receive.log';
            error_log(print_r($_REQUEST, 1) . PHP_EOL, 3, LOG_PATH . $logName);
            error_log(print_r(file_get_contents("php://input"), 1) . PHP_EOL, 3, LOG_PATH . $logName);
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
            //系统级参数解析
            self::__parseParams($_REQUEST);

            //系统级参数初始化校验
            $this->__filterParams();
            //获取接口对应的类、方法
            $apiMethod = self::$_apiMethod;

            //生成日志ID
            self::$_msgId = self::$_funcInstance->makeMsgId();
            //签名校验
            $signClassFile = sprintf(API_ROOT . 'router/sign/beibei/%s/v%s.php', self::$_fromApiCode, self::$_fromApiVer);
            if (!file_exists($signClassFile)) {
                //取第一个版本
                $signClassFile = sprintf(API_ROOT . 'router/sign/beibei/%s/v1.0.php', self::$_fromApiCode);
                if (!file_exists($signClassFile)) {
                    //获取标准签名文件
                    $signClassFile = API_ROOT . 'router/sign/beibei/common/v1.0.php';
                }
            }
            if (file_exists($signClassFile)) {
                include addslashes($signClassFile);
                if (class_exists('sign')) {
                    $signObj = new sign();
                    if (self::$_to == 'wms') {
                        $check_res = $signObj->check(self::$_appSecret, self::$_sign, $_REQUEST);
                    } else {
                        $check_res = $signObj->check(self::$_appSecret, self::$_sign, self::$_data);
                    }
                    if (!$check_res) {
                        $this->send_user_error('签名错误');
                    }
                } else {
                    $this->send_user_error('没有找到签名方法');
                }
            } else {
                $this->send_user_error('没有找到签名文件');
            }
            //数据转换
            $requestData = array();
            $formatClassFile = sprintf(API_ROOT . 'router/format/beibei/%s/v%s.php', self::$_fromApiCode, self::$_fromApiVer);
            if (!file_exists($formatClassFile)) {
                //取第一个版本
                $formatClassFile = sprintf(API_ROOT . 'router/format/beibei/%s/v1.0.php', self::$_fromApiCode);
                if (!file_exists($formatClassFile)) {
                    //获取标准转换文件
                    $formatClassFile = sprintf(API_ROOT . 'router/format/beibei/common/v%s.php', self::$_fromApiVer);
                    if (!file_exists($formatClassFile)) {
                        //取第一个版本
                        $formatClassFile = API_ROOT . 'router/format/beibei/common/v1.0.php';
                    }
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
                    $this->send_user_error('找不到数据格式化的方法');
                }
            } else {
                $this->send_user_error('找不到数据格式化的文件');
            }
            //数据过滤
            $filterClassFile = sprintf(API_ROOT . 'router/filter/beibei/%s/v%s/%s.php', self::$_fromApiCode, self::$_fromApiVer, 'filter' . ucfirst(self::$_apiClass));
            if (!file_exists($filterClassFile)) {
                //取第一个版本
                $filterClassFile = sprintf(API_ROOT . 'router/filter/beibei/%s/v1.0/%s.php', self::$_fromApiCode, 'filter' . ucfirst(self::$_apiClass));
                if (!file_exists($filterClassFile)) {
                    //获取标准文件
                    $filterClassFile = sprintf(API_ROOT . 'router/filter/beibei/common/v%s/%s.php', self::$_fromApiVer, 'filter' . ucfirst(self::$_apiClass));
                    if (!file_exists($filterClassFile)) {
                        $filterClassFile = sprintf(API_ROOT . 'router/filter/beibei/common/v1.0/%s.php', 'filter' . ucfirst(self::$_apiClass));
                    }
                }
            }
            if (file_exists($filterClassFile)) {
                include_once addslashes($filterClassFile);
                $filterClassName = 'filter' . ucfirst(self::$_apiClass);
                if (class_exists($filterClassName)) {
                    $filterObj = new $filterClassName();
                    if (method_exists($filterObj, $apiMethod)) {
                        $filterRs = $filterObj->$apiMethod($requestData);
                        if ($filterRs['success'] == false) {
                            $this->send_user_error($filterRs['message'], $requestData);
                        }
                    } else {
                        $this->send_user_error('找不到数据过滤的方法', $requestData);
                    }
                }
            }
            //接口请求处理
            $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/beibei/%s/v%s/%s.php', self::$_to, self::$_toApiCode, self::$_toApiVer, self::$_to . ucfirst(self::$_apiClass));
            if (!file_exists($interfaceClassFile)) {
                //调用自定义实例
                $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/beibei/%s/v1.0/%s.php', self::$_to, self::$_toApiCode, self::$_to . ucfirst(self::$_apiClass));
                if (!file_exists($interfaceClassFile)) {
                    //调用标准实例
                    $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/beibei/common/v%s/%s.php', self::$_to, self::$_toApiVer, self::$_to . ucfirst(self::$_apiClass));
                    if (!file_exists($interfaceClassFile)) {
                        $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/beibei/common/v1.0/%s.php', self::$_to, self::$_to . ucfirst(self::$_apiClass));
                        if (!file_exists($interfaceClassFile)) {
                            $this->send_user_error('找不到接口调用文件', $requestData);
                        }
                    }
                }
            }
            $interfaceClassName = self::$_to . ucfirst(self::$_apiClass);
            include_once addslashes($interfaceClassFile);
            if (class_exists($interfaceClassName)) {
                $instance = new $interfaceClassName();
                if (method_exists($instance, $apiMethod)) {
                    if (self::$_to == 'erp') {
                        $instance::$erpApi = self::$_toApiUrl;
                        $instance::$erpApiSecret = self::$_appSecret;
                        $instance::$erpApiVer = self::$_toApiVer;
                    } elseif (self::$_to == 'wms') {
                        $instance::$customerId = self::$_customerId;
                        $instance::$wmsApi = self::$_toApiUrl;
                        $instance::$wmsApiSecret = self::$_appSecret;
                        $instance::$wmsApiVer = self::$_toApiVer;
                    }
                    self::$_outputContent = $instance->$apiMethod($requestData);
                    //addon由转发类返回，目的是为了记录日志
                    if (isset(self::$_outputContent['addon'])) {
                        self::$_toApiUrl = self::$_outputContent['addon']['api_url'];
                        self::$_toApiParams = self::$_outputContent['addon']['api_params'];
                        self::$_toApiReturn = self::$_outputContent['addon']['return_msg'];
                        unset(self::$_outputContent['addon']);
                    }
                } else {
                    $this->send_user_error('没有找到接口请求的方法', $requestData);
                }
            } else {
                $this->send_user_error('没有找到接口请求的文件', $requestData);
            }
            //记录请求日志
            self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_requestUrl, self::$_method, $_REQUEST, $this->output(self::$_outputContent, false));
            //记录转发目的地返回日志,self::$_msgId作为此日志的父ID
            self::$_funcInstance->writeLog('', self::$_customerId, self::$_toApiUrl, self::$_method, self::$_toApiParams, self::$_toApiReturn, self::$_msgId);
            //记录订单接口日志(待修改)
            if (YII_INTERFACE_LOG_FLAG) {
                self::$_funcInstance->writeBeibeiInterfaceLog(self::$_method, self::$_data, '', $requestData, self::$_toApiReturn, self::$_msgId, self::$_toApiUrl, self::$_outputContent['success']);
            }

            self::$_resp_time = util::microtime_float();
            $timeRs = self::getTimeLength(self::$_req_time, self::$_mid_req_time, self::$_mid_resp_time, self::$_resp_time);
            self::addTimeLog($timeRs['api_time'], $timeRs['all_time'], self::$_req_time, self::$_resp_time, self::$_mid_req_time, self::$_mid_resp_time, self::$_method, self::$_customerId);
            //返回结果
            self::output(self::$_outputContent);

        } catch (Exception $e) {
            $logName = date("Ymd") . '_oms_beibei_receive.log';
            error_log(print_r($e->getMessage(), 1) . PHP_EOL, 3, LOG_PATH . $logName);
            $this->send_user_error($e->getMessage());
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
            self::send_user_error('接收到的数据为空');
        } else {
            if (!isset($request['data'])) {
                $request['data'] = file_get_contents("php://input");
            }
        }
        self::$_method = $request['method'];
        //访问相关配置
        self::$_to = self::$_apiConfig[self::$_method]['to'];
        self::$_apiClass = self::$_apiConfig[self::$_method]['class'];
        self::$_apiMethod = self::$_apiConfig[self::$_method]['fct'];
        if (empty(self::$_to) || empty(self::$_apiClass) || empty(self::$_apiMethod)) {
            self::send_user_error('系统中系统级参数method:' . self::$_method . '信息配置不完整');
        }
        self::$_timeStamp = $request['timestamp'];
        self::$_sign = $request['sign'];
        self::$_data = $request['data'];
        if (self::$_to == 'erp') {
            self::$_v = isset($request['v']) ? $request['v'] : '1.0';
            self::$_customerId = $request['customerId'];
        } else {
            self::$_format = isset($request['format']) ? $request['format'] : 'json';
            self::$_appId = $request['appId'];
            self::$_session = $request['session'];
            self::$_v = isset($request['version']) ? $request['version'] : '1.0';
            //获取货主id（贝贝的货主id放在业务级参数中）
            self::$_customerId = self::getCustomerId();
        }
    }

    /**
     * 系统级参数校验及初始化
     * @return bool
     */
    private function __filterParams()
    {
        //校验系统级参数完整性
        if (self::$_to == 'erp') {
            if (!self::$_customerId || !self::$_method || !self::$_sign || !self::$_timeStamp || !self::$_v) {
                $this->send_user_error('系统级参数不完整');
            }
        } else {
            if (!self::$_method || !self::$_format || !self::$_timeStamp || !self::$_customerId || !self::$_session || !self::$_sign || !self::$_v) {
                $this->send_user_error('系统级参数不完整');
            }
        }
        //校验json数据
        if (!self::$_data) {
            $this->send_user_error('发送的业务字段data数据为空');
        }
        //校验customerId，并且获取customerId对应的ERP和WMS信息
        if (self::$_to == 'wms') {
            $rsCustomer = OmsDatabase::$oms_db->fetchOne('*', 't_qimen_customer', 'customer_id = :customer_id', array(':customer_id' => self::$_appId));
            if (empty($rsCustomer)) {
                $this->send_user_error('系统级参数appId:' . self::$_appId . '不存在或无效');
            } elseif (empty($rsCustomer['wms_app_key']) || empty($rsCustomer['wms_secret']) || empty($rsCustomer['erp_code']) || empty($rsCustomer['erp_api_ver']) || empty($rsCustomer['erp_api_url']) || empty($rsCustomer['wms_code']) || empty($rsCustomer['wms_api_ver']) || empty($rsCustomer['wms_api_url'])) {
                $this->send_user_error('系统中系统级参数appId:' . self::$_appId . '信息配置不完整');
            }
        } elseif (self::$_to == 'erp') {
            $rsBind = OmsDatabase::$oms_db->fetchOne('qimen_customer_id,customer_id', 't_qimen_customer_bind', 'customer_id = :customer_id AND is_valid=1 ', array(':customer_id' => self::$_customerId));
            if (empty($rsBind)) {
                $this->send_user_error('系统级参数customerId:' . self::$_customerId . '不存在或无效');
            } else {
                $rsCustomer = OmsDatabase::$oms_db->fetchOne('*', 't_qimen_customer', 'customer_id = :customer_id AND is_valid=1', array(':customer_id' => $rsBind['qimen_customer_id']));
                if (empty($rsCustomer)) {
                    $this->send_user_error('系统级参数customerId:' . self::$_customerId . '不存在或无效');
                } elseif (empty($rsCustomer['wms_app_key']) || empty($rsCustomer['wms_secret']) || empty($rsCustomer['erp_code']) || empty($rsCustomer['erp_api_ver']) || empty($rsCustomer['erp_api_url']) || empty($rsCustomer['wms_code']) || empty($rsCustomer['wms_api_ver']) || empty($rsCustomer['wms_api_url'])) {
                    $this->send_user_error('系统中系统级参数customerId:' . self::$_customerId . '信息配置不完整');
                }
            }
        }
        //返回参数赋值
        if (self::$_to == 'wms') {
            self::$_fromApiCode = $rsCustomer['erp_code'];
            self::$_fromApiVer = $rsCustomer['erp_api_ver'];
            self::$_toApiCode = $rsCustomer['wms_code'];
            self::$_toApiVer = $rsCustomer['wms_api_ver'];
            self::$_toApiUrl = $rsCustomer['wms_api_url'];
        } else {
            self::$_appId = $rsBind['qimen_customer_id'];
            self::$_fromApiCode = $rsCustomer['wms_code'];
            self::$_fromApiVer = $rsCustomer['wms_api_ver'];
            self::$_toApiCode = $rsCustomer['erp_code'];
            self::$_toApiVer = $rsCustomer['erp_api_ver'];
            self::$_toApiUrl = $rsCustomer['erp_api_url'];
        }
        self::$_appSecret = $rsCustomer['wms_secret'];
    }

    /***
     * Notes: oms内部报错
     * Date: 2019/6/18
     * Time: 23:44
     * @param string $error_msg 错误信息
     * @param array $requestData 请求数据
     */
    public function send_user_error($error_msg = '', $requestData = array())
    {
        $content = array(
            'success' => false,
            'data' => '',
            'message' => $error_msg,
        );
        //记录错误日志
        self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_requestUrl, self::$_method, $_REQUEST, $error_msg);
        //记录详细接口日志
        if (YII_INTERFACE_LOG_FLAG) {
            self::$_funcInstance->writeBeibeiInterfaceLog(self::$_method, self::$_data, $error_msg, $requestData, '', self::$_msgId, self::$_toApiUrl, false);
        }
        //返回结果
        self::output($content);
    }

    /***
     * Notes: erp请求获取货主编码
     * Date: 2019/6/18
     * Time: 23:42
     * @return string
     */
    public function getCustomerId()
    {
        $data = json_decode(self::$_data, true);
        switch (self::$_method) {
            case 'beibei.outer.product.sync' :
                $customerId = $data[0]['company'];
                break;
            case 'beibei.outer.entryorder.create' :
            case 'beibei.outer.stockout.create' :
            case 'beibei.outer.bill.cancel' :
                $customerId = $data['company'];
                break;
            case 'beibei.outer.entryorder.query' :
            case 'beibei.outer.deliveryorder.query' :
            case 'beibei.outer.inventory.pagequery' :
                $customerId = $data['company'] ? $data['company'] : 'R1002';
                break;
            case 'beibei.outer.deliveryorder.create':
                $customerId = $data['companyId'];
                break;
            case 'beibei.outer.rma.create':
                $customerId = $data['header']['company'];
                break;
            default:
                $customerId = 'R1002';
        }
        return $customerId;
    }

    /***
     * Notes: 接口标准输出函数
     * Date: 2019/6/14
     * Time: 19:58
     * @param string $content 输出内容
     * @param bool $forceOutput 是否直接输出
     * @return false|string
     */
    public static function output($content = '', $forceOutput = true)
    {
        if (self::$_toApiReturn != '') {
            $jsonReturn = self::$_toApiReturn;
        } else {
            //目的地返回结果返回的数据为空
            $jsonReturn = json_encode($content, JSON_UNESCAPED_UNICODE);
        }
        if ($forceOutput === true) {//默认强制输出
            echo $jsonReturn;
            exit();
        } else {
            return $jsonReturn;
        }
    }

    /***
     * Notes:去除多余的反斜线
     * Date: 2019/6/19
     * Time: 14:13
     * @param $var 数组
     */
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

    /***
     * Notes:获取耗时时长
     * Date: 2019/6/19
     * Time: 14:01
     * @param $reqTime 请求oms
     * @param $midReqTime oms请求wms/erp接口
     * @param $midRespTime wms/erp接口返回到 oms
     * @param $respTime  oms返回
     * @return array  api_time oms访问wms/erp 时长   wms/erp访问到返回总时长
     */
    private function getTimeLength($reqTime, $midReqTime, $midRespTime, $respTime)
    {
        $apiTimeLength = $midRespTime - $midReqTime;
        $allTimeLength = $respTime - $reqTime;
        return array('api_time' => $apiTimeLength, 'all_time' => $allTimeLength);
    }

    /***
     * Notes: 添加耗时日志
     * Date: 2019/6/19
     * Time: 14:10
     * @param $apiTime oms访问wms/erp接口所用时长
     * @param $allTime wms访问erp 或者 erp访问wms 所用时长
     * @param $reqTime 请求oms时
     * @param $respTime  oms返回
     * @param $midReqTime oms请求wms/erp接口
     * @param $midRespTime wms/erp接口返回到 oms
     * @param $method 接口名称
     * @param $customerId 货主id
     */
    private function addTimeLog($apiTime, $allTime, $reqTime, $respTime, $midReqTime, $midRespTime, $method, $customerId)
    {
        $insert_arr = array(
            'customer_id' => $customerId,
            'method' => $method,
            'req_time' => $reqTime,
            'mid_req_time' => $midReqTime,
            'mid_resp_time' => $midRespTime,
            'resp_time' => $respTime,
            'wms_time_length' => $apiTime,
            'all_time_length' => $allTime,
            'create_time' => date('Y-m-d H:i:s')
        );
        OmsDatabase::$oms_db->insert('t_api_time_log', $insert_arr);
    }
}