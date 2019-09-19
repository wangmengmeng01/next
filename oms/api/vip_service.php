<?php

/**
 * Notes:唯品会接口入口类
 * Date: 2019/1/2
 * Time: 9:05
 */
class vip_service
{
    public static $_method;
    public static $_vendorid;
    public static $_warehouseid;
    public static $_format;
    public static $_timestamp;
    public static $_sign;
    public static $_data;
    public static $_selfreq = 0;//本系统请求标记
    protected static $_toApiReturn;

    protected $apiFunc = array(
        'getPoList' => array('class' => 'VipImpl', 'fct' => 'getPoList', 'to' => 'erp'),
        'createPick' => array('class' => 'createPick', 'fct' => 'create', 'to' => 'erp'),
        'createDelivery' => array('class' => 'createDelivery', 'fct' => 'create', 'to' => 'erp'),
        'createMultiPoDelivery' => array('class' => 'createMultiPoDelivery', 'fct' => 'create', 'to' => 'erp'),
        'importDeliveryDetail' => array('class' => 'importDeliveryDetail', 'fct' => 'import', 'to' => 'erp'),
        'importMultiPoDeliveryDetail' => array('class' => 'importMultiPoDeliveryDetail', 'fct' => 'import', 'to' => 'erp'),
        'vip.pickorder.sync' => array('class' => 'pickOrder', 'fct' => 'create', 'to' => 'wms'),
        'confirmDelivery' => array('class' => 'confirmDelivery', 'fct' => 'confirm', 'to' => 'erp'),
        'deleteDeliveryDetail' => array('class' => 'deleteDeliveryDetail', 'fct' => 'delete', 'to' => 'erp'),
        'editDelivery' => array('class' => 'editDelivery', 'fct' => 'edit', 'to' => 'erp'),
        'editMultiPoDelivery' => array('class' => 'editMultiPoDelivery', 'fct' => 'editMulti', 'to' => 'erp'),
    );

    /**
     * 处理逻辑
     */
    public function process()
    {
        //记录接收到的数据日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_vip_service.log';
            error_log(print_r($_REQUEST,1) . PHP_EOL,3,LOG_PATH . $logName);
        }

        ignore_user_abort();                        //设置与客户机断开是否会终止脚本的执行。
        set_time_limit(0);                          //设置程序执行时间
        header('Connection: close');                //关闭持久连接

        //判断magic_quotes_gpc是否开启。开启则对单引号等字符进行转义
        if (get_magic_quotes_gpc()) {
            self::strip_magic_quotes($_REQUEST);
        }

        try {
            //解析系统级参数
            self::__parseParams($_REQUEST);
            //系统级参数初始化校验
            $this->__filterParams();
            //匹配接口处理类，方法
            $apiClass = $this->apiFunc[self::$_method]['class'];
            $apiMethod = $this->apiFunc[self::$_method]['fct'];
            $apiTo = $this->apiFunc[self::$_method]['to'];

            if (!isset($apiClass) || !isset($apiMethod)) {
                $this->send_user_error('S001', '接口信息不匹配！');
            }
            //校验签名
            if (self::$_selfreq == 0) {
                $signClassFile = API_ROOT . 'router/sign/vip/v1.0.php';
                if (!file_exists($signClassFile)) {
                    $this->send_user_error('S001', '签名校验文件不存在！');
                }
                include_once addslashes($signClassFile);
                $signObj = new sign();
                if (!$signObj->check(VIP_WMS_SECRET, self::$_sign, self::$_data)) {
                    $this->send_user_error('S001', '签名校验不通过！');
                }
            }
            //接口请求处理
            $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/vip/v1.0/%s.php', $apiTo, $apiClass);
            if (!file_exists($interfaceClassFile)) {
                $this->send_user_error('S001', '找不到接口调用文件');
            }

            include_once addslashes($interfaceClassFile);
            if (!class_exists($apiClass)) {
                $this->send_user_error('S001', '接口处理类不存在！');
            }
            $interfaceClassObj = new $apiClass;
            if (!method_exists($interfaceClassObj, $apiMethod)) {
                $this->send_user_error('S001', '接口处理方法不存在！');
            }
            $rs = $interfaceClassObj->$apiMethod(self::$_data);//echo '999';die;

            self::output($rs);
        } catch (Exception $e) {
            $this->send_user_error('S003', $e->getMessage());
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
            self::send_user_error('S001', '接收到的数据为空');
        }
        //用于本系统自己调用
        if (isset($request['selfreq']) && $request['selfreq'] == VIP_OMS_SELF_REQ_SECRET) {
            self::$_data = isset($request['data']) ? $request['data'] : '';
            self::$_method = isset($request['method']) ? $request['method'] : '';
            self::$_vendorid = isset($request['vendorid']) ? $request['vendorid'] : '';
            self::$_warehouseid = isset($request['warehouseid']) ? $request['warehouseid'] : '';
            self::$_selfreq = 1;
        } else {
            self::$_method = isset($request['method']) ? $request['method'] : '';
            self::$_vendorid = isset($request['vendorid']) ? $request['vendorid'] : '';
            self::$_warehouseid = isset($request['warehouseid']) ? $request['warehouseid'] : '';
            self::$_format = isset($request['format']) ? $request['format'] : 'json';
            self::$_timestamp = isset($request['timestamp']) ? $request['timestamp'] : '';
            self::$_sign = isset($request['sign']) ? $request['sign'] : '';
            self::$_data = isset($request['data']) ? $request['data'] : '';
        }
    }

    /**
     * 系统级参数校验及初始化
     * @return bool
     */
    private function __filterParams()
    {
        //校验系统级参数完整性
        if (self::$_selfreq == 1) {
            if (!self::$_method || !self::$_data) {
                $this->send_user_error('S001', '请求参数不完整[self]！');
            }
            if ($this->apiFunc[self::$_method]['to'] == 'wms') {
                if (!self::$_method || !self::$_data || !self::$_vendorid || !self::$_warehouseid) {
                    $this->send_user_error('S001', '请求参数不完整[self]！');
                }
            }
        } else {
            if (!self::$_method || !self::$_data || !self::$_vendorid || !self::$_warehouseid || !self::$_format || !self::$_timestamp || !self::$_sign) {
                $this->send_user_error('S001', '请求参数不完整[all]！');
            }
        }
    }

    /***
     * Notes: 返回的错误信息
     * @param $flag 成功标记
     * @param $error_code 错误码
     * @param string $error_msg 错误信息
     */
    public function send_user_error($error_code, $error_msg)
    {
        $content = array(
            'flag' => 'failure',
            'code' => $error_code,
            'message' => $error_msg,
        );
        $rsp = json_encode($content, JSON_UNESCAPED_UNICODE);
        self::$_toApiReturn = self::output($rsp, false);
        //记录错误日志
        //self::$_funcInstance->addVipLog(self::$_method,self::$_logId, self::$_appUrl,json_encode(self::$_request), self::$_toApiReturn, self::$_requestOmsTime, date("Y-m-d H:i:s"), 0);
        //返回结果
        self::output($rsp);
    }

    //接口标准输出函数
    public static function output($content = '', $forceOutput = true)
    {
        if ($forceOutput === true) {//默认强制输出
            echo $content;
            exit;
        } else {
            return $content;
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