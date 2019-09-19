<?php

/**
 * 跨境贸易仓储接口入口类
 * User: Renee
 * Date: 2017/12/29
 * Time: 15:14
 */
class custom_service
{
    const TIMEOUT = '30'; //默认预警超时时间
    public static $_msgtype = '';
    public static $_msg = '';
    public static $_userid = '';
    public static $_sign = '';

    public static $_respHead = '';//响应格式
    public static $_extraValueArr = array();//接口特殊返回内容
    public static $_toApiReturn = '';

    public static $_outputContent = '';
    public static $_toApiParams = '';

    public static $_requestUrl = 'custom_api.php';
    private static $_fromApiCode = '';//请求方编码
    private static $_fromApiVer = ''; //请求方使用版本号
    private static $_toApiCode = '';//目的方编码
    private static $_toApiVer = '';//目的方使用版本号
    private static $_toApiUrl = '';
    private static $_secret = '';//请求方秘钥

    private static $_from = '';
    private static $_to = '';
    private static $_apiClass = '';
    private static $_apiMethod = '';

    private static $_funcInstance = '';//公共方法类实例
    private static $_logId = '';       //日志id

    /**
     * 接口业务总流程处理函数
     */
    public function process()
    {
        //记录文件日志
        if (RECEIVE_LOG_FLAG) {
            $logName = date("Ymd") . '_oms_kj_receive.log';
            error_log(print_r($_REQUEST, 1), 3, LOG_PATH . $logName);
        }

        //设置与客户机断开是否会终止脚本的执行。
        ignore_user_abort();
        //设置程序执行时间
        set_time_limit(0);
        //关闭持久连接
        header('Connection: close');

        //判断magic_quotes_gpc是否开启。开启则对单引号等字符进行转义
        if (get_magic_quotes_gpc()) {
            self::strip_magic_quotes($_REQUEST);
        }

        self::$_funcInstance = new func();

        try {
            //系统级参数解析
            self::__parseParams($_REQUEST);

            self::$_logId = self::$_funcInstance->makeMsgId();

            //数据转换
            $formatClassFile = API_ROOT . 'router/format/custom/common/v1.0.php';
            include_once $formatClassFile;
            $formatObj = new format();
            if (isset(self::$_msg) && !empty(self::$_msg)) {
                $requestData = $formatObj->request(self::$_msg);
            } else {
                $requestData = array();
                self::__parseLogisticParam($requestData);
                $this->send_user_error('报文参数msg为空！',$requestData);
            }

            //获取错误根节点
            self::__parseLogisticParam($requestData);

            //系统级参数校验
            self::__filterParams($requestData);

            //签名校验
            $signClassFile = sprintf(API_ROOT . 'router/sign/custom/%s/v%s.php', self::$_fromApiCode, self::$_fromApiVer);
            if (!file_exists($signClassFile)) {
                //取第一个版本
                $signClassFile = sprintf(API_ROOT . 'router/sign/custom/%s/v1.0.php', self::$_fromApiCode);
                if (!file_exists($signClassFile)) {
                    //获取标准签名文件
                    $signClassFile = API_ROOT . 'router/sign/custom/common/v1.0.php';
                }
            }
            if (file_exists($signClassFile)) {
                include addslashes($signClassFile);
                if (class_exists('sign')) {
                    $signObj = new sign();
                    if (!$signObj->check(self::$_userid, self::$_secret, self::$_sign)) {
                        $this->send_user_error('签名错误！',$requestData);
                    }
                } else {
                    $this->send_user_error('没有找到签名方法！',$requestData);
                }
            } else {
                $this->send_user_error('没有找到签名文件！',$requestData);
            }

            //数据过滤
            $filterClassFile = sprintf(API_ROOT . 'router/filter/custom/%s/v%s/%s.php', self::$_fromApiCode, self::$_fromApiVer, 'filter' . ucfirst(self::$_apiClass));
            if (!file_exists($filterClassFile)) {
                //取第一个版本
                $filterClassFile = sprintf(API_ROOT . 'router/filter/custom/%s/v1.0/%s.php', self::$_fromApiCode, 'filter' . ucfirst(self::$_apiClass));
                if (!file_exists($filterClassFile)) {
                    //获取标准文件
                    $filterClassFile = sprintf(API_ROOT . 'router/filter/custom/common/v%s/%s.php', self::$_fromApiVer, 'filter' . ucfirst(self::$_apiClass));
                    if (!file_exists($filterClassFile)) {
                        $filterClassFile = sprintf(API_ROOT . 'router/filter/custom/common/v1.0/%s.php', 'filter' . ucfirst(self::$_apiClass));
                    }
                }
            }

            $apiMethod = self::$_apiMethod;
            if (file_exists($filterClassFile)) {
                include_once(addslashes($filterClassFile));
                $filterClassName = 'filter' . ucfirst(self::$_apiClass);
                if (class_exists($filterClassName)) {
                    $filterObj = new $filterClassName();
                    if (method_exists($filterObj, $apiMethod)) {
                        $filterRs = $filterObj->$apiMethod($requestData);
                        if ($filterRs['success'] == 'false') {
                            $this->send_user_error($filterRs['reasons'],$requestData);
                        }
                    } else {
                        $this->send_user_error('找不到数据过滤的方法！',$requestData);
                    }
                }
            }

            //接口请求处理
            $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/custom/%s/v%s/%s.php', self::$_to, self::$_toApiCode, self::$_toApiVer, self::$_to . ucfirst(self::$_apiClass));
            if (!file_exists($interfaceClassFile)) {
                //调用自定义实例
                $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/custom/%s/v1.0/%s.php', self::$_to, self::$_toApiCode, self::$_to . ucfirst(self::$_apiClass));
                if (!file_exists($interfaceClassFile)) {
                    //调用标准实例
                    $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/custom/common/v%s/%s.php', self::$_to, self::$_toApiVer, self::$_to . ucfirst(self::$_apiClass));
                    if (!file_exists($interfaceClassFile)) {
                        $interfaceClassFile = sprintf(API_ROOT . 'router/interface/%s/custom/common/v1.0/%s.php', self::$_to, self::$_to . ucfirst(self::$_apiClass));
                        if (!file_exists($interfaceClassFile)) {
                            $this->send_user_error('找不到接口调用文件！',$requestData);
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
                        $instance::$userId = self::$_userid;
                        $instance::$erpApi = self::$_toApiUrl;
                        $instance::$erpApiSecret = self::$_secret;
                    } elseif (self::$_to == 'wms') {
                        $instance::$userId = self::$_userid;
                        $instance::$wmsApiUrl = self::$_toApiUrl;
                        $instance::$wmsApiSecret = self::$_secret;
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
                    $this->send_user_error('没有找到接口请求的方法！',$requestData);
                }
            } else {
                $this->send_user_error('没有找到接口请求的文件！',$requestData);
            }

            //记录请求日志:
            self::$_funcInstance->writeLog(self::$_logId, self::$_userid, self::$_requestUrl, self::$_msgtype, $_REQUEST, $this->output(self::$_outputContent, false));
            //记录转发目的地返回日志,self::$_logId作为此日志的父ID
            self::$_funcInstance->writeLog('', self::$_userid, self::$_toApiUrl, self::$_msgtype, self::$_toApiParams, self::$_toApiReturn, self::$_logId);

            self::$_funcInstance->addCustomLog(self::$_msgtype,$requestData,'',$this->output(self::$_outputContent, false));

            self::output(self::$_outputContent);
        } catch (Exception $e) {
            $error = $e->getMessage();
            $this->send_user_error($error);
        }
    }

    /**
     * 解析系统级参数
     * @param $request HTTP $_REQUEST
     */
    private static function __parseParams(&$request)
    {
        self::$_msgtype = empty($request['msgtype']) ? $request['msgType'] : $request['msgtype'];
        self::$_msg = empty($request['msg']) ? '' : $request['msg'];
        self::$_userid = empty($request['userid']) ? $request['userId'] : $request['userid'];
        self::$_sign = empty($request['sign']) ? '' : $request['sign'];

    }

    /**
     * 校验系统级参数
     * @return bool
     */
    private function __filterParams($requestData)
    {
        if (!self::$_msgtype || !self::$_msg || !self::$_userid || !self::$_sign) {
            $this->send_user_error('系统级参数不完整！',$requestData);
        }

        global $db;

        //校验msgtype配置信息
        $sql = "SELECT * FROM t_api_list WHERE api_id=:api_id AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':api_id', self::$_msgtype);
        $model->execute();
        $apiInfo = $model->fetch(PDO::FETCH_ASSOC);

        if (empty($apiInfo)) {
            $this->send_user_error('系统级参数msgtype:'.self::$_msgtype.'不存在',$requestData);
            return false;
        } elseif (empty($apiInfo['api_class']) || empty($apiInfo['api_method']) || empty($apiInfo['api_from']) || empty($apiInfo['api_to'])) {
            $this->send_user_error('系统中系统级参数msgtype:'.self::$_msgtype.'信息配置不完整',$requestData);
            return false;
        }

        //校验平台是否存在
        $userSql = "SELECT * FROM t_kj_user WHERE user_id=:user_id AND is_valid=1";
        $userModel = $db->prepare($userSql);
        $userModel->bindParam(':user_id', self::$_userid);
        $userModel->execute();
        $userInfo = $userModel->fetch(PDO::FETCH_ASSOC);
        if (empty($userInfo)) {
            $this->send_user_error('该平台信息不存在！',$requestData);
        }

        //获取api配置路径
        if ($apiInfo['api_from'] == 'erp') {
            self::$_fromApiCode = $userInfo['erp_code'];
            self::$_fromApiVer = $userInfo['erp_version'];
            self::$_toApiCode = $userInfo['wms_code'];
            self::$_toApiVer = $userInfo['wms_version'];
            self::$_toApiUrl = $userInfo['wms_url'];
        } else {
            self::$_fromApiCode = $userInfo['wms_code'];
            self::$_fromApiVer = $userInfo['wms_version'];
            self::$_toApiCode = $userInfo['erp_code'];
            self::$_toApiVer = $userInfo['erp_version'];
            self::$_toApiUrl = $userInfo['erp_url'];
        }
        self::$_secret = $userInfo['secret'];
        self::$_from = $apiInfo['api_from'];
        self::$_to = $apiInfo['api_to'];
        self::$_apiClass = $apiInfo['api_class'];
        self::$_apiMethod = $apiInfo['api_method'];

        return true;
    }

    /**
     * 根据正向接口名称来匹配对应响应报文格式
     * @param $requestData
     */
    private static function __parseLogisticParam($requestData)
    {
        switch (self::$_msgtype) {
            case 'cnec_wh_1':
                self::$_respHead = 'kjStorerResponse';
                self::$_extraValueArr = array(
                    'storer' => $requestData['storer']
                );
                break;
            case 'cnec_wh_2':
                self::$_respHead = 'kjCarrierResponse';
                self::$_extraValueArr = array(
                    'carrier' => $requestData['carrier'],
                    'wmwhseid' => $requestData['wmwhseid'],
                );
                break;
            case 'cnec_wh_3':
                self::$_respHead = 'kjSkuResponse';
                self::$_extraValueArr = array(
                    'storer' => $requestData['storer'],
                    'skuKey' => $requestData['skuKey'],
                );
                break;
            case 'cnec_wh_4':
                self::$_respHead = 'kjAsnResponse';
                self::$_extraValueArr = array(
                    'storer' => isset($requestData['storer']) && $requestData['storer'] ? $requestData['storer'] : '',
                    'wmwhseid' => isset($requestData['wmwhseid']) && $requestData['wmwhseid'] ? $requestData['wmwhseid'] : '',
                    'externalNo' => isset($requestData['externalNo']) && $requestData['externalNo'] ? $requestData['externalNo'] : '',
                );
                break;
            case 'cnec_wh_5':
                self::$_respHead = 'kjAsnCancelResponse';
                self::$_extraValueArr = array(
                    'storer' => $requestData['storer'],
                    'wmwhseid' => $requestData['wmwhseid'],
                    'externalNo' => $requestData['externalNo']
                );
                break;
            case 'cnec_wh_6':
                self::$_respHead = 'kjsoOrderResponse';
                self::$_extraValueArr = array(
                    'storer' => isset($requestData['storer']) && $requestData['storer'] ? $requestData['storer'] : '',
                    'wmwhseid' => isset($requestData['wmwhseid']) && $requestData['wmwhseid'] ? $requestData['wmwhseid'] : '',
                    'externalNo' => isset($requestData['externalNo']) && $requestData['externalNo'] ? $requestData['externalNo'] : '',
                );
                break;
            case 'cnec_wh_7':
                self::$_respHead = 'wmsStockResponse';
                break;
            case 'cnec_wh_9':
                self::$_respHead = 'mftVerifyResponse';
                self::$_extraValueArr = array(
                    'externalNo' => $requestData['externalNo']
                );
                break;
            case 'cnec_wh_10':
                self::$_respHead = 'wmsGjRqLockRequest';
                break;
            case 'cnec_wh_11':
                self::$_respHead = 'wmsOrderCancelResponse';
                break;
            case 'cnec_im_1':
                self::$_respHead = 'wmsAsnReceiveResponse';
                self::$_extraValueArr = array(
                    'storer' => $requestData['storer'],
                    'wmwhseid' => $requestData['wmwhseid'],
                    'externalNo' => $requestData['externalNo']
                );
                break;
            case 'cnec_im_3':
                self::$_respHead = 'kjsoDeclResponse';
                self::$_extraValueArr = array(
                    'lockFlag' => $requestData['lockFlag'],//仅当success=false有效,其他错误返回2
                );
                break;
            case 'cnec_im_4':
                self::$_respHead = 'wmsLackGoodsResponse';
                break;
            case 'cnec_im_5':
                self::$_respHead = 'wmsStockOutFeedBackResponse';
                break;
            case 'cnec_im_6':
                self::$_respHead = 'wmsFxResponse';
                self::$_extraValueArr = array(
                    'storer' => $requestData['storer'],
                    'wmwhseid' => $requestData['wmwhseid'],
                );
                break;
            case 'updataDeliveryInfo':
                self::$_respHead = 'updataDeliveryInfoResponse';
                break;
            case 'getKaolaStoreInfo':
                self::$_respHead = 'response';
                break;
            default:
                self::$_respHead = 'kjResponse';
                break;
        }
    }

    /**
     * 接口前置错误输出
     * @param $errorMsg
     */
    public function send_user_error($errorMsg,$requestData=array())
    {
        $errorArr = array(
            'success' => 'false',
            'reasons' => $errorMsg
        );
        if (!empty(self::$_extraValueArr)) {
            $content = array_merge($errorArr, self::$_extraValueArr);
        } else {
            $content = $errorArr;
        }

        self::$_funcInstance->writeLog(self::$_logId, self::$_userid, self::$_requestUrl, self::$_msgtype, $_REQUEST, $errorMsg);
        self::$_funcInstance->addCustomLog(self::$_msgtype,$requestData,$errorMsg,self::output($content,false));

        self::output($content);
    }

    /**
     * 响应报文输出函数
     * @param string $content
     * @param bool $forceOutput
     * @return string
     */
    public static function output($content = '', $forceOutput = true)
    {
        if (self::$_toApiReturn != '' && xml::isXml(self::$_toApiReturn)) {
            $xmlReturn = self::$_toApiReturn;
        } else {
            $xmlReturn = '<?xml version="1.0" encoding="utf-8"?>';
            $xmlReturn .= '<' . self::$_respHead . '>';
            $xmlReturn .= util::array2xml($content);
            $xmlReturn .= '</' . self::$_respHead . '>';
        }
        //判断是否强制输出
        if ($forceOutput === true) {
            echo $xmlReturn;
            exit();
        } else {
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


}