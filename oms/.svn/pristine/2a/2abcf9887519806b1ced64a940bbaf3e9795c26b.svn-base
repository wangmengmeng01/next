<?php

/**
 * 奇门接口入口类
 */
class qimen_service
{
    const TIMEOUT = '30'; //默认预警超时时间
    public static $_systemParams = array('app_key', 'customerId', 'format', 'method', 'sign_method', 'timestamp', 'v');
    public static $_customerId = '';            //客户id
    public static $_customerCode = '';          //货主编码
    public static $_method = '';                //请求的接口方法
    public static $_appKey = '';                //WMS系统APPKEY
    public static $_appSecret = '';             //客户在WMS系统中的appSecret
    private static $_sign = '';                //请求的签名
    public static $_signMethod = '';            //签名方法
    public static $_timeStamp = '';            //时间
    public static $_data = '';                    //数据
    public static $_format = 'xml';            //返回格式
    public static $_v = '1.0';                    //接口版本
    private static $_from = '';                 //调用方（erp或wms）
    private static $_fromApiCode = '';          //调用方API编码（ERP或WMS编码）
    private static $_fromApiVer = '';           //调用方API版本号（ERP或WMS版本号）
    private static $_to = '';                   //目的方（erp或wms）
    public static $_toApiCode = '';            //目的方API编码（ERP或WMS编码）
    private static $_toApiVer = '';             //目的方API版本号（ERP或WMS版本号）
    private static $_apiClass = '';             //接口调用类名
    private static $_apiMethod = '';            //接口调用方法名
    public static $sysMsgCode = array(
        'success' => '成功',
        'failure' => '失败',
        'S001' => '签名错误',
        'S002' => '系统异常',
        'S003' => '数据错误',
        'S004' => '货主错误',
        'S005' => '系统配置错误',
        'S006' => '记录已存在且不允许修改',
        'S007' => '其他错误',
        'E001' => '接口超时'
    );                                          //系统编码               
    private static $_start_time;                //API执行开始时间
    public static $_finish = false;            //完成标识
    private static $_funcInstance = '';        //公共方法类实例
    private static $_requestUrl = OMS_API_URL;    //当前请求URL
    private static $_msgId = '';                //日志ID
    public static $_outputContent = '';        //标准输出内容
    public static $_errorMsg = '';              //错误信息
    public static $_toApiUrl = '';                //目的地接口地址
    public static $_toApiParams = '';            //目的地接口发送参数
    public static $_toApiReturn = '';            //目的地返回结果
    public static $_queryFlag = false;          //是否是查询接口标志
    public static $_requestDataArr = '';        //请求数据的数组格式.
    public static $_filterErrStr = '';          //用于批量接口的错误拼接
    public static $_req_time = '';              //erp|wms请求oms时间
    public static $_mid_req_time = '';          //oms请求wms|erp时间
    public static $_mid_resp_time = '';         //wms|erp响应oms时间
    public static $_resp_time = '';             //oms响应wms|erp时间
    public static $_warehouseCode = '';            //仓库编码

    public function process()
    {
        self::$_req_time = util::microtime_float();

        //记录接收到的数据日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_oms_qimen_receive.log';
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

            //生成日志ID
            self::$_msgId = self::$_funcInstance->makeMsgId();

            //签名校验
            $signClassFile = sprintf(API_ROOT . '/router/sign/qimen/%s/v%s.php', self::$_fromApiCode, self::$_fromApiVer);
            if (!file_exists($signClassFile)) {
                //取第一个版本
                $signClassFile = sprintf(API_ROOT . '/router/sign/qimen/%s/v1.0.php', self::$_fromApiCode);
                if (!file_exists($signClassFile)) {
                    //获取标准签名文件
                    $signClassFile = API_ROOT . '/router/sign/qimen/common/v1.0.php';
                }
            }
            if (file_exists($signClassFile)) {
                include addslashes($signClassFile);
                if (class_exists('sign')) {
                    $signObj = new sign();
                    if (!$signObj->check(self::$_appSecret, self::$_data, self::$_sign, $_REQUEST)) {
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
            $formatClassFile = sprintf(API_ROOT . '/router/format/qimen/%s/v%s.php', self::$_fromApiCode, self::$_fromApiVer);
            if (!file_exists($formatClassFile)) {
                //取第一个版本
                $formatClassFile = sprintf(API_ROOT . '/router/format/qimen/%s/v1.0.php', self::$_fromApiCode);
                if (!file_exists($formatClassFile)) {
                    //获取标准转换文件
                    $formatClassFile = sprintf(API_ROOT . '/router/format/qimen/common/v%s.php', self::$_fromApiVer);
                    if (!file_exists($formatClassFile)) {
                        //取第一个版本
                        $formatClassFile = API_ROOT . '/router/format/qimen/common/v1.0.php';
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
                    $this->send_user_error('S002', '找不到数据格式化的方法');
                }
            } else {
                $this->send_user_error('S002', '找不到数据格式化的文件');
            }

            //数据过滤
            $filterClassFile = sprintf(API_ROOT . '/router/filter/qimen/%s/v%s/%s.php', self::$_fromApiCode, self::$_fromApiVer, 'filter' . ucfirst(self::$_apiClass));
            if (!file_exists($filterClassFile)) {
                //取第一个版本
                $filterClassFile = sprintf(API_ROOT . '/router/filter/qimen/%s/v1.0/%s.php', self::$_fromApiCode, 'filter' . ucfirst(self::$_apiClass));
                if (!file_exists($filterClassFile)) {
                    //获取标准文件
                    $filterClassFile = sprintf(API_ROOT . '/router/filter/qimen/common/v%s/%s.php', self::$_fromApiVer, 'filter' . ucfirst(self::$_apiClass));
                    if (!file_exists($filterClassFile)) {
                        $filterClassFile = sprintf(API_ROOT . '/router/filter/qimen/common/v1.0/%s.php', 'filter' . ucfirst(self::$_apiClass));
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
                        if ($filterRs['flag'] == 'failure') {
                            $this->send_user_error($filterRs['code'], $filterRs['message'], $requestData);
                        }
                    } else {
                        $this->send_user_error('S002', '找不到数据过滤的方法', $requestData);
                    }
                }
            }

            //分仓(正向接口才需要调用)
            if (self::$_from == 'erp') {
                self::$_funcInstance->getUrlByWarehouse($requestData, self::$_method);
            }

            //接口请求处理
            $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/qimen/%s/v%s/%s.php', self::$_to, self::$_toApiCode, self::$_toApiVer, self::$_to . ucfirst(self::$_apiClass));
            if (!file_exists($interfaceClassFile)) {
                //调用自定义实例
                $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/qimen/%s/v1.0/%s.php', self::$_to, self::$_toApiCode, self::$_to . ucfirst(self::$_apiClass));
                if (!file_exists($interfaceClassFile)) {
                    //调用标准实例
                    $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/qimen/common/v%s/%s.php', self::$_to, self::$_toApiVer, self::$_to . ucfirst(self::$_apiClass));
                    if (!file_exists($interfaceClassFile)) {
                        $interfaceClassFile = sprintf(API_ROOT . '/router/interface/%s/qimen/common/v1.0/%s.php', self::$_to, self::$_to . ucfirst(self::$_apiClass));
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
                    if (self::$_to == 'erp') {
                        $instance::$customerId = self::$_customerId;
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
                    $this->send_user_error('S002', '没有找到接口请求的方法', $requestData);
                }
            } else {
                $this->send_user_error('S002', '没有找到接口请求的文件', $requestData);
            }

            //记录请求日志:
            self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_requestUrl, self::$_method, $_REQUEST, $this->output(self::$_outputContent, false));
            //记录转发目的地返回日志,self::$_msgId作为此日志的父ID
            self::$_funcInstance->writeLog('', self::$_customerId, self::$_toApiUrl, self::$_method, self::$_toApiParams, self::$_toApiReturn, self::$_msgId);

            //记录订单接口日志
            if (YII_INTERFACE_LOG_FLAG) {
                self::$_funcInstance->writeQimenInterfaceLog(self::$_method, self::$_data, '', $requestData, self::$_toApiReturn, self::$_msgId, self::$_toApiUrl, self::$_outputContent['flag']);
            }

            //接口执行完成标识置为true
            self::$_finish = true;

            self::$_resp_time = util::microtime_float();
            $timeRs = self::getTimeLength(self::$_req_time, self::$_mid_req_time, self::$_mid_resp_time, self::$_resp_time);
            self::addTimeLog($timeRs['wms_time'], $timeRs['all_time'], self::$_req_time,self::$_resp_time,self::$_mid_req_time,self::$_mid_resp_time,self::$_method, self::$_customerId);

            /* //返回结果前，记录天蚕日志
            if (CHECK_TIANCAN == 1){
                global $check_tc;
                $check_tc->write_system_log(array(API_RETURN_DATA_CODE_FIELD=>'success', API_RETURN_DATA_MESSAGE_FIELD=>'成功！'));
            } */

            //返回结果
            self::output(self::$_outputContent);
        } catch (Exception $e) {
            self::$_errorMsg = $e->getMessage();
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

        self::$_customerId = isset($request['customerId']) ? $request['customerId'] : $request['customerid'];
        self::$_method = $request['method'];
        self::$_appKey = isset($request['app_key']) ? $request['app_key'] : $request['appkey'];
        self::$_sign = $request['sign'];
        self::$_signMethod = isset($request['sign_method']) ? $request['sign_method'] : 'md5';
        self::$_timeStamp = $request['timestamp'];
        self::$_format = isset($request['format']) ? $request['format'] : 'xml';
        self::$_v = isset($request['v']) ? $request['v'] : '2.0';
        self::$_data = isset($request['data']) ? $request['data'] : '';
        //重新赋值request
        $request['method'] = self::$_method;
        $request['timestamp'] = self::$_timeStamp;
        $request['format'] = self::$_format;
        $request['app_key'] = self::$_appKey;
        $request['v'] = self::$_v;
        $request['sign_method'] = self::$_signMethod;
        $request['customerId'] = self::$_customerId;
    }

    /**
     * 系统级参数校验及初始化
     * @return bool
     */
    private function __filterParams()
    {
        //校验系统级参数完整性
        if (!self::$_customerId || !self::$_method || !self::$_appKey || !self::$_sign || !self::$_timeStamp || !self::$_signMethod || !self::$_format || !self::$_v) {
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
    		$this->send_user_error('S003', '系统级参数method:'.self::$_method.'不存在');
    		return false;
    	} elseif (empty($apiInfo['api_class']) || empty($apiInfo['api_method']) || empty($apiInfo['api_from']) || empty($apiInfo['api_to'])) {
    		$this->send_user_error('S005', '系统中系统级参数method:'.self::$_method.'信息配置不完整');
    		return false;
    	}
    	
    	//校验customerId，并且获取customerId对应的ERP和WMS信息
    	$sql = ' SELECT * FROM t_qimen_customer WHERE customer_id=:customer_id AND is_valid=1';
    	$model = $db->prepare($sql);
    	$model->bindParam(':customer_id', self::$_customerId);
    	$model->execute();
    	$rsCustomer = $model->fetch(PDO::FETCH_ASSOC);
    	if ($apiInfo['api_from'] == 'erp') {   		
    		if (empty($rsCustomer)) {
    			$this->send_user_error('S003', '系统级参数customerId:'.self::$_customerId.'不存在或无效');
    			return false;
    		} elseif (empty($rsCustomer['wms_app_key']) || empty($rsCustomer['wms_secret']) || empty($rsCustomer['erp_code']) || empty($rsCustomer['erp_api_ver']) || empty($rsCustomer['erp_api_url']) || empty($rsCustomer['wms_code']) || empty($rsCustomer['wms_api_ver']) || empty($rsCustomer['wms_api_url'])) {
    			$this->send_user_error('S005', '系统中系统级参数customerId:'.self::$_customerId.'信息配置不完整');
    			return false;
    		}   		   		
    	} elseif ($apiInfo['api_from'] == 'wms') {
    		if (empty($rsCustomer)) {
    			$sql = 'SELECT qimen_customer_id,customer_id FROM t_qimen_customer_bind WHERE customer_id=:customer_id AND is_valid=1';
    			$model = $db->prepare($sql);
    			$model->bindParam(':customer_id', self::$_customerId);
    			$model->execute();
    			$rsBind = $model->fetch(PDO::FETCH_ASSOC);
    			if (empty($rsBind)) {
    				$this->send_user_error('S003', '系统级参数customerId:'.self::$_customerId.'不存在或无效');
    				return false;
    			} else {
    				$sql = ' SELECT * FROM t_qimen_customer WHERE customer_id=:customer_id AND is_valid=1';
    				$model = $db->prepare($sql);
    				$model->bindParam(':customer_id', $rsBind['qimen_customer_id']);
    				$model->execute();
    				$rsCustomer = $model->fetch(PDO::FETCH_ASSOC);
    				if (empty($rsCustomer)) {
    					$this->send_user_error('S003', '系统级参数customerId:'.self::$_customerId.'不存在或无效');
    					return false;
    				} elseif (empty($rsCustomer['wms_app_key']) || empty($rsCustomer['wms_secret']) || empty($rsCustomer['erp_code']) || empty($rsCustomer['erp_api_ver']) || empty($rsCustomer['erp_api_url']) || empty($rsCustomer['wms_code']) || empty($rsCustomer['wms_api_ver']) || empty($rsCustomer['wms_api_url'])) {
    					$this->send_user_error('S005', '系统中系统级参数customerId:'.self::$_customerId.'信息配置不完整');
    					return false;
    				}
    			}
    		} elseif (empty($rsCustomer['wms_app_key']) || empty($rsCustomer['wms_secret']) || empty($rsCustomer['erp_code']) || empty($rsCustomer['erp_api_ver']) || empty($rsCustomer['erp_api_url']) || empty($rsCustomer['wms_code']) || empty($rsCustomer['wms_api_ver']) || empty($rsCustomer['wms_api_url'])) {
    			$this->send_user_error('S005', '系统中系统级参数customerId:'.self::$_customerId.'信息配置不完整');
    			return false;
    		}
    	} else {
    		$this->send_user_error('S005', '系统中系统级参数method:'.self::$_method.'信息来源配置错误');
    		return false;
        }

        //返回参数赋值        
        if ($apiInfo['api_from'] == 'erp') {
            self::$_fromApiCode = $rsCustomer['erp_code'];
            self::$_fromApiVer = $rsCustomer['erp_api_ver'];
            self::$_toApiCode = $rsCustomer['wms_code'];
            self::$_toApiVer = $rsCustomer['wms_api_ver'];
            self::$_toApiUrl = $rsCustomer['wms_api_url'];
        } else {
            self::$_fromApiCode = $rsCustomer['wms_code'];
            self::$_fromApiVer = $rsCustomer['wms_api_ver'];
            self::$_toApiCode = $rsCustomer['erp_code'];
            self::$_toApiVer = $rsCustomer['erp_api_ver'];
            self::$_toApiUrl = $rsCustomer['erp_api_url'];
            self::$_customerCode = self::$_customerId;
            self::$_customerId = $rsCustomer['customer_id'];//如果wms回传数据的时候，将其系统级参数里面的customer_id为表中的客户id
        }
        self::$_from = $apiInfo['api_from'];
        self::$_to = $apiInfo['api_to'];
        self::$_apiClass = $apiInfo['api_class'];
        self::$_apiMethod = $apiInfo['api_method'];
        self::$_appKey = $rsCustomer['wms_app_key'];
        self::$_appSecret = $rsCustomer['wms_secret'];
        return true;
    }

    public function send_user_error($error_code, $error_msg = '', $requestData = array())
    {
        $content = array(
            'flag' => 'failure',
            'code' => $error_code,
            'message' => $error_msg != '' ? $error_msg : '',
        );
        //记录错误日志
        self::$_funcInstance->writeLog(self::$_msgId, self::$_customerId, self::$_requestUrl, self::$_method, $_REQUEST, $error_msg);
        //记录详细接口日志
        if (YII_INTERFACE_LOG_FLAG) {
            self::$_funcInstance->writeQimenInterfaceLog(self::$_method, self::$_data, $error_msg, $requestData, '', self::$_msgId, self::$_toApiUrl, 'failure');
        }

        //返回结果前，记录天蚕日志
        if (CHECK_TIANCAN == 1) {
            global $check_tc;
            $check_tc->write_system_log(array(API_RETURN_DATA_CODE_FIELD => 'failure', API_RETURN_DATA_MESSAGE_FIELD => $error_msg));
        }

        //返回结果
        self::output($content);
    }

    //接口标准输出函数
    public static function output($content = '', $forceOutput = true)
    {
        if ($forceOutput === true) {//默认强制输出
            if (self::$_format == 'xml') {
                if (self::$_toApiReturn != '' && xml::isXml(self::$_toApiReturn)) {
                    $xmlReturn = self::$_toApiReturn;
                } else {
                    $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                    $xmlReturn .= '<response>';
                    $xmlReturn .= util::array2xml($content);
                    $xmlReturn .= '</response>';
                }
                echo $xmlReturn;
            } else {
                if (self::$_toApiReturn != '') {
                    echo self::$_toApiReturn;
                } else {
                    $rs['response'] = $content;
                    echo json_encode($rs);
                }
            }
            exit;
        } else {//返回输出格式，不强制输出
            if (self::$_format == 'xml') {
                //xml格式输出
                if (self::$_toApiReturn != '' && xml::isXml(self::$_toApiReturn)) {
                    $xmlReturn = self::$_toApiReturn;
                } else {
                    $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
                    $xmlReturn .= '<response>';
                    $xmlReturn .= util::array2xml($content);
                    $xmlReturn .= '</response>';
                }
            } else {
                if (self::$_toApiReturn != '') {
                    $xmlReturn = self::$_toApiReturn;
                } else {
                    $rs['response'] = $content;
                    $xmlReturn = json_encode($rs);
                }
            }
            return $xmlReturn;
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