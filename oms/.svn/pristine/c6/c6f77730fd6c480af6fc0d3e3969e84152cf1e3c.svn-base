<?php

/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/5/3
 * Time: 10:33
 */
class kaola_service
{
    public static $_systemParams = array('wms_id', 'owner_id', 'stock_id', 'notify_type', 'notify_id', 'notify_time', 'sign', 'data');
    public static $_wmsId = '';  //应用ID
    public static $_ownerId = '';//货主编码
    public static $_stockId = '';//仓库编码
    public static $_notifyType = '';//接口名称
    public static $_notifyId = '';//请求流水号
    public static $_notifyTime = '';//请求时间
    public static $_sign = '';//签名
    public static $_data = '';//请求报文

    public static $_pulicKey = '';
    public static $_privateKey = '';

    private static $_from = '';
    private static $_to = '';
    private static $_apiClass = '';
    private static $_apiMethod = '';

    public static $_reqUrl = '';
    public static $_respUrl = '';

    public static $_rsMsg = '';

    public static $_outputContent = '';        //标准输出内容
    public static $_toApiUrl = '';                //目的地接口地址
    public static $_toApiParams = '';            //目的地接口发送参数
    public static $_toApiReturn = '';            //目的地返回结果

    public function process()
    {
        if (1) {
            $logName = date("Ymd") . '_kaola_receive.log';
            error_log(print_r($_REQUEST, 1), 3, '/yd/oms/1.0.1/log/' . $logName);
        }

        ignore_user_abort();                        //设置与客户机断开是否会终止脚本的执行。
        set_time_limit(0);                            //设置程序执行时间
        header('Connection: close');                //关闭持久连接

        //判断magic_quotes_gpc是否开启。开启则对单引号等字符进行转义
        if (get_magic_quotes_gpc()) {
            self::strip_magic_quotes($_REQUEST);
        }


        try {
            //系统级参数解析
            self::__parseParams($_REQUEST);

            //系统级参数初始化校验
            $this->__filterParams();

            $apiMethod = self::$_apiMethod;


            //正向
            if (self::$_to == 'wms') {
                include_once addslashes(API_ROOT . 'router/sign/kaola/common/v1.0.php');
                if (class_exists('sign')) {
                    $signObj = new sign();
                    $klPubKey = KAOLA_WY_PUB_KEY;
                    if (!$signObj->check(self::$_sign, self::$_data, $klPubKey)) {
                        $this->generateError('签名错误');
                    }
                } else {
                    $this->generateError('没有找到签名方法');
                }
            }

            //数据转换
            $requestData = array();
            $formatClassFile = API_ROOT . 'router/format/kaola/v1.0.php';
            if (file_exists($formatClassFile)) {
                include_once addslashes($formatClassFile);
                if (class_exists('format')) {
                    $formatObj = new format();
                    $requestData = $formatObj->request(self::$_data);
                } else {
                    $this->generateError('找不到数据格式化的方法！');
                }
            } else {
                $this->generateError('找不到数据格式化的文件！');
            }

            $filterClassFile = sprintf(API_ROOT . 'router/filter/kaola/common/v1.0/%s.php', 'filter' . ucfirst(self::$_apiClass));
            if (file_exists($filterClassFile)) {
                include_once addslashes($filterClassFile);
                $filterClassName = 'filter' . ucfirst(self::$_apiClass);
                if (class_exists($filterClassName)) {
                    $filterObj = new $filterClassName();
                    if (method_exists($filterObj, $apiMethod)) {
                        $filterRs = $filterObj->$apiMethod($requestData);
                        if (!$filterRs['success']) {
                            $this->generateError($filterRs['error_msg']);
                        }
                    } else {
                        $this->generateError('找不到数据过滤的方法!');
                    }
                }
            }

            //接口请求处理
            $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/kaola/common/v1.0/%s.php', self::$_to, 'Kl' . ucfirst(self::$_apiClass));
            if (!file_exists($interfaceClassFile)) {
                $this->generateError('找不到接口调用文件!');
            }

            if (file_exists($interfaceClassFile)) {
                $interfaceClassName = 'Kl' . ucfirst(self::$_apiClass);
                include_once addslashes($interfaceClassFile);
                if (class_exists($interfaceClassName)) {
                    $instance = new $interfaceClassName();
                    if (method_exists($instance, $apiMethod)) {
                        self::$_outputContent = $instance->$apiMethod($requestData);

                        //记录日志
                        self::$_toApiUrl = self::$_reqUrl;
                        self::$_toApiParams = kaola_service::$_systemParams;
                        self::$_toApiReturn = self::$_rsMsg;
                    } else {
                        $this->generateError('没有找到接口请求的方法!');
                    }
                } else {
                    $this->generateError('没有找到接口请求的文件!');
                }
            }
        } catch (Exception $e) {
            $this->generateError('Exception:' . $e->getMessage());
        }

        $rsArr = self::$_outputContent;

        $funcObj = new func();
        $funcObj->addKlLog($requestData, self::$_notifyType, self::$_reqUrl, self::$_data, '', self::$_rsMsg, $rsArr['success']);

        if (!in_array(kaola_service::$_notifyType, array('60', '107'))) {
            self::output(self::$_outputContent);
        } else {
            self::output(kaola_service::$_rsMsg);
        }
    }

    private static function __parseParams(&$request)
    {
        if (empty($request)) {
            self::generateError("请求数据不能为空！");
        } else {
            if ($request['notify_type'] != 'kaola_getBillNo') {//非子母件接口
                self::$_wmsId = isset($request['wms_id']) ? urldecode($request['wms_id']) : '';
                self::$_ownerId = isset($request['owner_id']) ? urldecode($request['owner_id']) : '';
                self::$_stockId = isset($request['stock_id']) ? urldecode($request['stock_id']) : '';
                self::$_notifyType = isset($request['notify_type']) ? urldecode($request['notify_type']) : '';
                self::$_notifyId = isset($request['notify_id']) ? urldecode($request['notify_id']) : '';
                self::$_notifyTime = isset($request['notify_time']) ? urldecode($request['notify_time']) : date("Y-m-d H:i:s");
                self::$_sign = isset($request['sign']) ? urldecode($request['sign']) : '';

                $method = self::$_notifyType;
                if (!in_array($method, array('10', '11', '20', '21', '60', '109', '119', '100', '101', '103', '104', '106'))) {//回调
                    self::$_data = isset($request['data']) ? urldecode($request['data']) : '';
                } else {//正向请求
                    self::$_data = isset($request['data']) ? $request['data'] : '';
                }
            } else {
                if (isset($request['data'])) {
                    $dealFile = API_ROOT . "router/interface/erp/kaola/common/v1.0/KlGetBillNo.php";
                    include_once addslashes($dealFile);
                    $klGetBillNoObj = new KlGetBillNo();
                    self::$_rsMsg = $klGetBillNoObj->get($request['data']);
                } else {
                    self::$_rsMsg = '{"code": 400,"message": "子母件获取运单号接口：data参数不能为空！"}';

                }
                self::output(self::$_rsMsg);
            }
        }
    }

    public function __filterParams()
    {
        if (!self::$_ownerId || !self::$_stockId || !self::$_notifyType || !self::$_notifyId || !self::$_data) {
            $this->generateError('系统级参数不完整！');
        } else {
            //查询接口配置
            $params1 = array(
                ':api_id' => 'kaola_' . self::$_notifyType,
                ':is_valid' => '1'
            );
            $methodInfo = OmsDatabase::$oms_db->fetchOne('api_class,api_method,api_from,api_to', 't_api_list', "api_id=:api_id AND is_valid=:is_valid", $params1);

            if (empty($methodInfo)) {
                $this->generateError("无此接口的配置信息！");
            } else {
                self::$_from = $methodInfo['api_from'];
                self::$_to = $methodInfo['api_to'];
                self::$_apiClass = $methodInfo['api_class'];
                self::$_apiMethod = $methodInfo['api_method'];
            }

            //查询货主仓库配置
            $params = array(
                ':storer' => self::$_ownerId,
                ':wmwhseid' => self::$_stockId,
                ':is_auto' => 0
            );
            $storerInfo = OmsDatabase::$oms_db->fetchOne('erp_url,wms_url', 't_kj_storer', "storer=:storer AND wmwhseid=:wmwhseid AND is_auto=:is_auto", $params);
            if (empty($storerInfo)) {
                $this->generateError('货主和仓库配置关系不存在！');
            } else {
                if (empty($storerInfo['erp_url']) || empty($storerInfo['wms_url'])) {
                    $this->generateError("网易货主url配置不完整！");
                }
                if (self::$_to == 'wms') {
                    self::$_reqUrl = $storerInfo['wms_url'];
                    self::$_respUrl = $storerInfo['erp_url'];
                } else {
                    self::$_reqUrl = $storerInfo['erp_url'];
                    self::$_respUrl = $storerInfo['wms_url'];
                }
            }
        }
    }

    /**
     * 处理错误信息
     * @param 错误描述
     */
    public function generateError($errorMsg)
    {
        $params = array(
            'success' => false,
            'error_msg' => $errorMsg,
        );

        $funcObj = new func();
        $requestData = json_decode(self::$_data, true);
        if (!is_array($requestData)) {
            $requestData = '';
        }
        $funcObj->addKlLog($requestData, self::$_notifyType, self::$_reqUrl, self::$_data, $errorMsg, self::output($params, false), false);

        self::output($params);
    }

    /**
     * 标准输出函数
     * @param  $content     请求报文
     * @param  $forceOutput 强制输出标志
     * @return 响应报文
     */
    public static function output($content = '', $forceOutput = true)
    {
        $data = array();
        if (is_array($content)) {
            foreach ($content as $key => $item) {
                if ($key != 'success') {
                    $data[$key] = urlencode($item);
                } else {
                    $data[$key] = $item;
                }
            }
        } else {
            $data = $content;
        }
        error_log(print_r($_REQUEST, 1), 3, '/yd/oms/1.0.1/log/reqrs.log');
        error_log(print_r(self::$_data . PHP_EOL, 1), 3, '/yd/oms/1.0.1/log/req_data.log');
        error_log(print_r(urldecode(json_encode($data)), 1), 3, '/yd/oms/1.0.1/log/reqrs.log');


        if ($forceOutput === true) {
            if (is_array($data)) {
                echo urldecode(json_encode($data));
            } else {
                echo $data;
            }
            exit();
        } else {
            if (is_array($data)) {
                return urldecode(json_encode($data));
            } else {
                return $data;
            }
        }
    }
}