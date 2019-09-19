<?php

/**
 * 菜鸟仓储API接入
 * @author Renee
 *
 */
class cn_storage_service
{
    const TIMEOUT = '30'; //默认预警超时时间

    /*
     * 菜鸟->wms  被动模式系统级参数
     */
    public static $_logistics_interface = '';             //请求报文内容
    public static $_data_digest = '';                     //签名
    public static $_msg_type = '';                        //消息类型API
    public static $_msg_id = '';                          //消息id
    public static $_partner_code = '';                    //合作伙伴编码
    public static $_logistic_provider_id = '';            //合作伙伴编码
    public static $_from_code = '';                       //调用方编码                  
    public static $_to_code = '';                         //目的方编码
    public static $_owner_user_id = '';                   //货主编码

    /*
     * wms->菜鸟  主动模式系统级参数
     */
    public static $_method = '';
    public static $_customerid = '';
    public static $_appkey = '';
    public static $_sign = '';
    public static $_warehouseid = '';
    public static $_data = '';

    /*
     *
     *公共参数
     */
    public static $_api_id = '';
    public static $_req_time = '';                //接口请求时间
    public static $_resp_time = '';               //接口响应时间
    public static $_mid_req_time = '';          //oms请求wms|erp时间
    public static $_mid_resp_time = '';         //wms|erp响应oms时间
    public static $_funcInstance = '';            //公用函数
    private static $_apiClass = '';             //接口调用类名
    private static $_apiMethod = '';            //接口调用方法名
    private static $_from = '';                   //调用方
    private static $_to = '';                     //目的方
    private static $_format = 'xml';              //报文格式
    private static $_v = '1.0';                   //版本号

    //日志参数
    public static $_outputContent = '';
    public static $_msgId = '';                    //日志生成id

    public static $_requestUrl = '';               //请求url
    public static $_toApiUrl = '';               //目的 url
    public static $_toApiParams = '';             //发送报文
    public static $_toApiReturn = '';              //接口返回内容
    public static $_rs_flag = '';                //接口返回状态

    public function process()
    {
        self::$_req_time = util::microtime_float();

        //记录接收到的数据日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_cn_storage_receive.log';
            error_log(print_r($_REQUEST, 1), 3, LOG_PATH . $logName);
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
            self::__filterParams();

            //获取接口对应的类、方法
            $apiClass = self::$_apiClass;
            $apiMethod = self::$_apiMethod;

            //生成日志ID
            self::$_msgId = self::$_funcInstance->makeMsgId();

            //签名校验
            $signClassFile = sprintf(API_ROOT . '/router/sign/storage/%s/v%s.php', self::$_from, self::$_v);
            if (!file_exists($signClassFile)) {
                //取第一个版本
                $signClassFile = sprintf(API_ROOT . '/router/sign/storage/%s/v1.0.php', self::$_from);
                if (!file_exists($signClassFile)) {
                    //获取标准签名文件
                    $signClassFile = API_ROOT . '/router/sign/storage/common/v1.0.php';
                }
            }
            if (file_exists($signClassFile)) {
                include addslashes($signClassFile);
                if (class_exists('sign')) {
                    $signObj = new sign();
                    if (self::$_from == 'erp') {
                        if (!$signObj->check(STORAGE_APP_SECRET, self::$_logistics_interface, self::$_data_digest)) {
                            $this->send_user_error('S001', '签名错误');
                        }
                    } else {
                        if (!$signObj->check(SRD_APP_SECRET, self::$_data, self::$_sign)) {

                            $this->send_user_error('S001', '签名错误');
                        }
                    }
                } else {
                    $this->send_user_error('S002', '没有找到签名方法');
                }
            } else {
                $this->send_user_error('S002', '没有找到签名文件');
            }

            //数据转换
            $requestData = array();
            $formatClassFile = sprintf(API_ROOT . '/router/format/storage/v%s.php', self::$_v);
            if (!file_exists($formatClassFile)) {
                //取第一个版本
                $formatClassFile = API_ROOT . '/router/format/storage/v1.0.php';
                if (!file_exists($formatClassFile)) {
                    //获取标准转换文件
                    $formatClassFile = sprintf(API_ROOT . '/router/format/storage/common/v%s.php', self::$_v);
                    if (!file_exists($formatClassFile)) {
                        //取第一个版本
                        $formatClassFile = API_ROOT . '/router/format/storage/common/v1.0.php';
                    }
                }
            }
            if (file_exists($formatClassFile)) {
                include_once addslashes($formatClassFile);
                if (class_exists('format')) {
                    $formatObj = new format();
                    if (self::$_from == 'erp') {
                        $requestData = $formatObj->request(self::$_logistics_interface);
                    } else {
                        $requestData = $formatObj->request(self::$_data);
                    }
                    self::$_owner_user_id = !empty($requestData['ownerUserId']) ? $requestData['ownerUserId'] : (!empty(self::$_customerid) ? self::$_customerid : '');
                } else {
                    $this->send_user_error('S002', '找不到数据格式化的方法');
                }
            } else {
                $this->send_user_error('S002', '找不到数据格式化的文件');
            }

            //数据过滤
            $filterClassFile = sprintf(API_ROOT . '/router/filter/storage/v%s/%s.php', self::$_v, 'filter' . ucfirst(self::$_apiClass));
            if (!file_exists($filterClassFile)) {
                //取第一个版本
                $filterClassFile = sprintf(API_ROOT . '/router/filter/storage/v1.0/%s.php', 'filter' . ucfirst(self::$_apiClass));
                if (!file_exists($filterClassFile)) {
                    //获取标准文件
                    $filterClassFile = sprintf(API_ROOT . '/router/filter/storage/common/v%s/%s.php', self::$_v, 'filter' . ucfirst(self::$_apiClass));
                    if (!file_exists($filterClassFile)) {
                        $filterClassFile = sprintf(API_ROOT . '/router/filter/storage/common/v1.0/%s.php', 'filter' . ucfirst(self::$_apiClass));
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

                        if (!$filterRs['success']) {
                            $this->send_user_error($filterRs['errorCode'], $filterRs['errorMsg'], $requestData);
                        }

                    } else {
                        $this->send_user_error('S002', '找不到数据过滤的方法', $requestData);
                    }
                }
            }

            //接口请求处理  //wms->erp用原始报文直接传给erp 不用转化过的数据
            $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/storage/v%s/%s.php', self::$_to, self::$_v, self::$_to . ucfirst(self::$_apiClass));
            if (!file_exists($interfaceClassFile)) {
                //调用自定义实例
                $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/storage/v1.0/%s.php', self::$_to, self::$_to . ucfirst(self::$_apiClass));
                if (!file_exists($interfaceClassFile)) {
                    //调用标准实例
                    $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/storage/common/v%s/%s.php', self::$_to, self::$_v, self::$_to . ucfirst(self::$_apiClass));
                    if (!file_exists($interfaceClassFile)) {
                        $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/storage/common/v1.0/%s.php', self::$_to, self::$_to . ucfirst(self::$_apiClass));
                        if (!file_exists($interfaceClassFile)) {
                            $this->send_user_error('S002', '找不到接口调用文件', $requestData);
                        }
                    }
                }
            }
            $interfaceClassName = self::$_to . ucfirst(self::$_apiClass);
            include_once addslashes($interfaceClassFile);
            if (class_exists($interfaceClassName)) {
                $instance = new $interfaceClassName();
                if (method_exists($instance, $apiMethod)) {
                    if (self::$_from == 'erp') {
                        self::$_outputContent = $instance->$apiMethod($requestData);
                    } else {
                        self::$_outputContent = $instance->$apiMethod(self::$_data, $requestData);
                    }
                    //addon由转发类返回，目的是为了记录日志
                    if (isset(self::$_outputContent['addon'])) {
                        self::$_toApiParams = self::$_outputContent['addon']['api_params'];
                        self::$_rs_flag = self::$_outputContent['addon']['rs_flag'];
                        self::$_toApiReturn = self::$_outputContent['addon']['return_msg'];
                    }
                } else {
                    $this->send_user_error('S002', '没有找到接口请求的方法', $requestData);
                }
            } else {
                $this->send_user_error('S002', '没有找到接口请求的文件', $requestData);
            }

            self::$_funcInstance->writeLog(self::$_msgId, self::$_owner_user_id, self::$_requestUrl, self::$_api_id, $_REQUEST, self::output(self::$_outputContent, false));
            self::$_funcInstance->writeLog('', self::$_owner_user_id, self::$_toApiUrl, self::$_api_id, self::$_toApiParams, self::$_toApiReturn, self::$_msgId);
            self::$_funcInstance->writeStorageLog(self::$_api_id, self::$_owner_user_id, $requestData, self::$_rs_flag, self::$_toApiReturn, '');

            unset(self::$_outputContent['addon']);

            self::$_resp_time = util::microtime_float();
            $timeRs = self::getTimeLength(self::$_req_time, self::$_mid_req_time, self::$_mid_resp_time, self::$_resp_time);
            self::addTimeLog($timeRs['wms_time'], $timeRs['all_time'], self::$_req_time, self::$_resp_time, self::$_mid_req_time, self::$_mid_resp_time, $apiClass . '.' . $apiMethod, self::$_api_id);

            //返回结果前，记录天蚕日志
            $wmsRequestArr = array(
                'WMS_INVENTORY_COUNT',
                'WMS_INVENTORY_ADJUST_UPLOAD',
                'WMS_STOCK_IN_ORDER_CONFIRM',
                'WMS_STOCK_OUT_ORDER_CONFIRM',
                'WMS_CONSIGN_ORDER_CONFIRM'
            );
            if (isset($_REQUEST['method']) && in_array($_REQUEST['method'],$wmsRequestArr) && CHECK_TIANCAN == 1) {
                global $check_tc;
                $check_tc->write_system_log(array(API_RETURN_DATA_CODE_FIELD=>'success', API_RETURN_DATA_MESSAGE_FIELD=>'成功！'));
            }

            //返回结果
            if (self::$_from == 'wms' && self::$_toApiReturn) {
                self::output(self::$_toApiReturn);
            } else {
                self::$_outputContent['success'] = self::$_outputContent['success'] ? 'true' : 'false';
                self::output(self::$_outputContent);
            }

        } catch (Exception $e) {
            $this->send_user_error('S007', $e->getMessage());
        }
    }


    /**
     * 分析请求参数:支持POST与GET
     * @param Array $request 请求参数
     * @return array
     */
    public static function __parseParams(&$req)
    {
        if (empty($req)) {
            self::send_user_error('S003', '接收到的数据为空');
            return false;
        } else {
            $request = $_REQUEST;
        }

        if (isset($request['msg_type'])) {
            //菜鸟->wms
            self::$_msg_type = $request['msg_type'];
            self::$_msg_id = $request['msg_id'];
            self::$_from_code = $request['from_code'];
            self::$_to_code = isset($request['to_code']) ?: '';
            self::$_partner_code = $request['partner_code'];
            self::$_data_digest = $request['data_digest'];
            self::$_format = isset($request['format']) ? $request['format'] : 'xml';
            self::$_logistics_interface = $request['logistics_interface'];
            self::$_from = 'erp';
            self::$_api_id = $request['msg_type'];
            self::$_requestUrl = STORAGE_CN_URL;
            self::$_toApiUrl = STORAGE_WMS_API_URL;
        } elseif ($request['method']) {
            //wms->菜鸟
            self::$_method = $request['method'];     //api名称
            self::$_customerid = $request['customerid']; //不用
            self::$_appkey = $request['appkey'];
            self::$_sign = $request['sign'];
            self::$_warehouseid = $request['warehouseid'];
            self::$_data = $request['data'];
            self::$_from = 'wms';
            self::$_api_id = $request['method'];
            self::$_requestUrl = STORAGE_WMS_API_URL;
            self::$_toApiUrl = STORAGE_CN_URL;
        } else {
            self::send_user_error('S001', '接口名称不能为空！');
        }
    }

    /**
     * 系统级参数校验及初始化
     * @return bool
     */
    public static function __filterParams()
    {
        //校验系统级参数完整性
        if (self::$_from == 'erp') {
            if (!self::$_msg_type || !self::$_msg_id || !self::$_data_digest || !self::$_logistics_interface) {
                self::send_user_error('S003', '系统级参数不完整');
                return false;
            }
        } else {
            if (!self::$_method || !self::$_appkey || !self::$_sign || !self::$_warehouseid || !self::$_data) {
                self::send_user_error('S003', '系统级参数不完整');
                return false;
            }
        }

        //校验接口api
        global $db;

        $sql = 'SELECT * FROM t_api_list WHERE api_id=:api_id AND is_valid=1 ';
        $model = $db->prepare($sql);
        $model->bindParam(':api_id', self::$_api_id);
        $model->execute();
        $apiInfo = $model->fetch(PDO::FETCH_ASSOC);

        if (empty($apiInfo)) {
            self::send_user_error('S003', 'api接口' . self::$_api_id . '不存在');
            return false;
        } elseif (empty($apiInfo['api_class']) || empty($apiInfo['api_method']) || empty($apiInfo['api_from']) || empty($apiInfo['api_to'])) {
            self::send_user_error('S005', 'api接口:' . self::$_api_id . '信息配置不完整');
            return false;
        }
        //获取不同接口调用的方法
        self::$_to = $apiInfo['api_to'];
        self::$_apiClass = $apiInfo['api_class'];
        self::$_apiMethod = $apiInfo['api_method'];

        if (self::$_from == 'wms') {
            //根据仓库编码warehouse_code找寻菜鸟的cpcode ->qimen_customer_id
            $sql = 'SELECT qimen_customer_id FROM t_qimen_customer_bind WHERE warehouse_code=:warehouseid AND is_valid=1';
            $model = $db->prepare($sql);
            $model->bindParam(':warehouseid', self::$_warehouseid);
            $model->execute();
            $rsBind = $model->fetch(PDO::FETCH_ASSOC);
            if (empty($rsBind['qimen_customer_id'])) {
                self::send_user_error('S003', '仓库编码:' . self::$_warehouseid . '不存在或无效');
                return false;
            }

            self::$_logistic_provider_id = $rsBind['qimen_customer_id'];
        }
        return true;
    }

    public static function send_user_error($errorCode = '', $errorMsg = '', $requestData = array())
    {
        $content = array(
            'success' => 'false',
            'errorCode' => $errorCode,
            'errorMsg' => $errorMsg,
        );

        self::$_toApiReturn = self::output($content, false);
        //记录错误日志
        self::$_funcInstance->writeLog(self::$_msgId, self::$_api_id, self::$_requestUrl, self::$_method, $_REQUEST, self::$_toApiReturn);
        self::$_funcInstance->writeStorageLog(self::$_api_id, self::$_owner_user_id, $requestData, 'false', '', self::$_toApiReturn);

        //返回结果前，记录天蚕日志
        if (CHECK_TIANCAN == 1) {
            global $check_tc;
            $check_tc->write_system_log(array(API_RETURN_DATA_CODE_FIELD => 'false', API_RETURN_DATA_MESSAGE_FIELD => $errorMsg));
        }

        //返回结果
        self::output($content);
    }

    //接口标准输出函数
    public static function output($content = '', $forceOutput = true)
    {
        if ($forceOutput === true) {//默认强制输出
            if (self::$_format == 'xml') {
                if (!is_array($content)) {
                    $xmlReturn = $content;
                } else {
                    $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                    $xmlReturn .= '<response>';
                    $xmlReturn .= util::array2xml($content);
                    $xmlReturn .= '</response>';
                }
                echo $xmlReturn;
            } else {
                $rs['response'] = $content;
                echo json_encode($rs);
            }
            exit;
        } else {//返回输出格式，不强制输出
            if (self::$_format == 'xml') {
                if (!is_array($content)) {
                    $xmlReturn = $content;
                } else {
                    $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                    $xmlReturn .= '<response>';
                    $xmlReturn .= util::array2xml($content);
                    $xmlReturn .= '</response>';
                }
            } else {
                $rs['response'] = $content;
                $xmlReturn = json_encode($rs);
            }
            return $xmlReturn;
        }
    }

    //获取耗时时长
    private function getTimeLength($reqTime, $midReqTime, $midRespTime, $respTime)
    {
        $wmsTimeLength = $midRespTime - $midReqTime;
        $allTimeLength = $respTime - $reqTime;
        return array('wms_time' => $wmsTimeLength, 'all_time' => $allTimeLength);
    }

    //添加耗时日志
    private function addTimeLog($wmsTime, $allTime, $reqTime, $respTime, $midReqTime, $midRespTime, $method, $customerId)
    {
        global $db;

        $addSql = "INSERT INTO t_api_time_log(customer_id,method,req_time,mid_req_time,mid_resp_time,resp_time,wms_time_length,all_time_length,create_time) VALUES(:customer_id,:method,:req_time,:mid_req_time,:mid_resp_time,:resp_time,:wms_time_length,:all_time_length,now())";
        $model = $db->prepare($addSql);
        $model->bindParam(':customer_id', $customerId);
        $model->bindParam(':method', $method);
        $model->bindParam(':req_time', $reqTime);
        $model->bindParam(':mid_req_time', $midReqTime);
        $model->bindParam(':mid_resp_time', $midRespTime);
        $model->bindParam(':resp_time', $respTime);
        $model->bindParam(':wms_time_length', $wmsTime);
        $model->bindParam(':all_time_length', $allTime);
        $model->execute();
    }
}
