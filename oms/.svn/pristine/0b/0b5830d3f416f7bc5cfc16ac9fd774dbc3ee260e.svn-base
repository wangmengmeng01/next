<?php

	class tiancan{
		public static $_tcSystemData = array();//天蚕被调记录参数
		public static $_systemPath = ''; //系统路径
		public static $_contentType = array(
										'F' => 'Content-Type: application/x-www-form-urlencoded',
										'J' => 'Content-type: application/json',
										'X' => 'Content-type: application/xml'
									);
		private $zoo;
		public static $_authEnabled;//天蚕校验标识
		public static $_apiReturnDataType;//接口返回字段属性
		
		public function __construct($returnType){
			
			self::$_apiReturnDataType = $returnType;
			//采用qconf连接zookeeper集群,取消单一连接zookeeper
			/*if (!$this->zoo){
				$this->zoo = new \Zookeeper(ZOOKEEPER_PATH);    
			}*/ 
			
		}
		
		/*
		*天蚕被调
		*/
		public function tiancanUnactive(){
			
			self::$_tcSystemData['beginTime'] = self::__getNewTime();
			self::$_tcSystemData['calleeId'] = PROJECT_NAME;
			self::$_systemPath = ZOOKEEPER_ROOT_DIR . PROJECT_NAME;
			//判断被调系统是否存在
			$unactiveIfm = self::__getConf(self::$_systemPath);
			if (!$unactiveIfm){
				return self::__outputData('TCU01', '被调系统不存在！');
			}
			$control_param = addslashes($_SERVER['HTTP_CONTROL_PARAM']);
			//根据请求url生成hashCode,获取interfaceId
			$urlBase = strpos($_SERVER['REQUEST_URI'], '?');
			if ($urlBase){
				$baseUrl = substr($_SERVER['REQUEST_URI'], strpos($_SERVER['REQUEST_URI'], PROJECT_NAME) + strlen(PROJECT_NAME), $urlBase - strlen($_SERVER['REQUEST_URI']));
				$baseUrl .= empty($control_param) ? '' : '?'. $control_param;
			}else{
				$baseUrl = substr($_SERVER['REQUEST_URI'], strpos($_SERVER['REQUEST_URI'], PROJECT_NAME) + strlen(PROJECT_NAME));
			}
			$hash32 = self::__hashCode32($baseUrl);
			$hash64 = self::__hashCode64($baseUrl);
			$urlMapping = self::__getBatchKeys(self::$_systemPath . '/URL_MAPPING');
			
			if (in_array($hash32, $urlMapping)){
				$hash = $hash32;
			}elseif (in_array($hash64, $urlMapping)){
				$hash = $hash64;
			}else{
				return self::__outputData('TCU02', '被调系统接口不存在！');
			}
			//验证header是否完整，不完整校验是否在过渡期
			self::$_tcSystemData['authApp'] = addslashes($_SERVER['HTTP_IFM_CALLER']);//授权系统
			$time = addslashes($_SERVER['HTTP_IFM_TIME']);//时间戳
			$sign = addslashes($_SERVER['HTTP_IFM_SIGN']);//签名
			self::$_tcSystemData['openIfs'] = self::__getConf(self::$_systemPath . '/URL_MAPPING/'.$hash);
			if (empty(self::$_tcSystemData['authApp']) || empty($time) || empty($sign)){
				$transition = self::__getConf(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/TRANSITION_ENABLED'); //过渡是标识
				//判断是否是过渡期，E表示过渡期，无需验证
				if ($transition == 'E'){
					return self::__outputData('SUCC', '校验通过');
				}
				return self::__outputData('TCU03', '主调系统传递的系统参数不完整！');
			}else{
				$authApps = self::__getBatchKeys(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/AUTH_APPS');
				if (in_array(self::$_tcSystemData['authApp'], $authApps)){
					self::$_authEnabled = self::__getConf(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/AUTH_APPS/'.self::$_tcSystemData['authApp'].'/AUTH_ENABLED');//授权系统校验标识:E-校验,D-不校验
					if (self::$_authEnabled == 'E'){
						//校验授权状态:E-授权，D-未授权
						$activeFlag = self::__getConf(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/AUTH_APPS/'.self::$_tcSystemData['authApp'].'/ACTIVE_FLAG');
						if ($activeFlag == 'D'){
							return self::__outputData('TCU05', '主调系统未被授权！');
						}
						$allowIp = self::__getConf(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/AUTH_APPS/'.self::$_tcSystemData['authApp'].'/ALLOW_IPS');
						$requestIp = self::__getRequestIp();
						//授权系统IP校验，ALLOW_IPS节点为空时，默认不校验IP
						if ($allowIp){
							if (!strpos($allowIp, $requestIp)){
								return self::__outputData('TCU06', '非法IP请求！');
							}
						}
						$clock = self::__getConf(ZOOKEEPER_ROOT_DIR . 'CLOCK');
						$timeOut = self::__getConf(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/EXPIRATION');
						$allowTime = $clock - $time;
						//校验时间戳
						if ($allowTime > ($timeOut * 1000)){
							return self::__outputData('TCU07', '非法请求时间！');
						}
						//校验签名
						$request_content = file_get_contents("php://input");//只接受流信息
						self::$_tcSystemData['request_content'] = !empty($request_content) ? $request_content : 'null';
						$checkSign = strtoupper(md5(self::$_tcSystemData['authApp'] . self::$_tcSystemData['openIfs'] . self::$_tcSystemData['request_content'] . $time));
						if ($sign != $checkSign){
							return self::__outputData('TCU08', '签名不正确！');
						}
					}
					return self::__outputData('SUCC', '校验通过');
				}else{
					return self::__outputData('TCU04', '被调系统未授权该主调系统！');
				}
			}
		}
		
		/*
		*天蚕主调
		*@param $callee  String  被调系统
		*@param $interfaceId  String  被调系统接口ID
		*@param $requestData  Array/String  请求接口参数
		*/
		public function tiancanActive($callee, $interfaceId, $requestData=null){
			self::$_systemPath = ZOOKEEPER_ROOT_DIR . $callee;
			$authCallers = self::__getBatchKeys(ZOOKEEPER_ROOT_DIR);
			//验证被调系统是否存在
			if (!in_array($callee, $authCallers)){
				return self::__outputData('TCA01', '被调系统不存在！');
			}
			//判断被调系统接口ID是否存在
			$interfaceIds = self::__getBatchKeys(self::$_systemPath . '/OPEN_IFS');
			if (!in_array($interfaceId, $interfaceIds)){
				return self::__outputData('TCA02', '被调系统接口ID不存在！');
			}
			//获取接口URL
			$appUrl = self::__getConf(self::$_systemPath . '/APP_URL');
			$appUri = self::__getConf(self::$_systemPath . '/OPEN_IFS/' . $interfaceId . '/URI');
			$requestUrl = $appUrl . $appUri;
			//获取请求方式及content-type
			$reqMethod = self::__getConf(self::$_systemPath . '/OPEN_IFS/' . $interfaceId . '/REQ_METHOD');
			$method = $reqMethod == 'P' ? 'POST' : 'GET';
			$contentType = self::__getConf(self::$_systemPath . '/OPEN_IFS/' . $interfaceId . '/CONTENT_TYPE');
			if ($requestData){
				if(is_array($requestData)){
					if($contentType == 'J'){
						$reqData = json_encode($requestData);
					}else{
						$reqData = http_build_query($requestData);
					} 
				}else{
					$reqData = $requestData;
				}
			}
			//请求体如果为空,默认拼接字符串null
			$reqStr = empty($reqData) ? 'null' : $reqData;
			$header = array();
			$header[] = isset(self::$_contentType[$contentType]) ? self::$_contentType[$contentType]: self::$_contentType['F'];
			$header[] = 'ifm_caller:' . ZOOKEEPER_PROJECT_SYSTEM;
			$control_param = strpos($appUri, '?') ? substr($appUri, strpos($appUri, '?')+1) : '';
			$header[] = 'control_param:' . $control_param;
			//判断是否是在过渡期，过渡期不构建header
			$transitionEnabled = self::__getConf(self::$_systemPath . '/OPEN_IFS/' . $interfaceId . '/TRANSITION_ENABLED');
			if ($transitionEnabled == 'E'){
				$reponse = self::__curlPost($requestUrl, $method, $reqData, $header);
				return $reponse;
			}
			$authApps = self::__getBatchKeys(self::$_systemPath . '/OPEN_IFS/' . $interfaceId . '/AUTH_APPS');
			if (!in_array(ZOOKEEPER_PROJECT_SYSTEM, $authApps)){
				return self::__outputData('TCA03', '主调系统未被授权！');
			}
			//构建header
			$ifmTime = self::__getConf('/IFM/CLOCK');
			
			$ifmSign = strtoupper(md5(ZOOKEEPER_PROJECT_SYSTEM . $interfaceId . $reqStr . $ifmTime));
			$header[] = 'ifm_time:' . $ifmTime;
			$header[] = 'ifm_sign:' . $ifmSign;
			
			$reponse = self::__curlPost($requestUrl, $method, $reqData, $header);

			return $reponse;
		}
		
		//记录天蚕日志
		public function write_system_log($returnData, $endTime=''){
			//经过天蚕校验后才记录日志
			if (self::$_authEnabled != 'E'){
				return;
			}
			
			if (!class_exists('Logger', false)){
				if (file_exists(LOGGER_PHP_PATH)) {
					require LOGGER_PHP_PATH;
				}else{
					return self::__outputData('TLOG1', '引入log4php日志类文件不存在！');
				}
			}
			
			//日志记录
			$statisLog = self::__getConf(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/AUTH_APPS/'.self::$_tcSystemData['authApp'].'/STATIS_ENABLED');//统计日志标识
			$detailLog = self::__getConf(self::$_systemPath . '/OPEN_IFS/'.self::$_tcSystemData['openIfs'].'/AUTH_APPS/'.self::$_tcSystemData['authApp'].'/DETAIL_ENABLED');//详细日志标识
			
			$statisLogData = array();
			$detailLogData = array();
			$endTime = empty($endTime) ? self::__getNewTime() : $endTime;
			if ($statisLog == 'E'){
				
				//主调系统ID
				$statisLogData['caller'] = self::$_tcSystemData['authApp'];
				//被请求接口
				$statisLogData['interfaceid'] = self::$_tcSystemData['openIfs'];
				//被调方接受请求时间
				$statisLogData['receivetime'] = self::$_tcSystemData['beginTime'];
				//被调方处理请求返回时间
				$statisLogData['responsetime'] = $endTime;
				//处理耗时
				$statisLogData['duration'] = $endTime - self::$_tcSystemData['beginTime'];
				//请求返回码
				$statisLogData['code'] = $returnData[API_RETURN_DATA_CODE_FIELD];
			}
			
			if ($detailLog == 'E'){
				//主调系统ID
				$detailLogData['caller_id'] = self::$_tcSystemData['authApp'];
				//被调系统ID
				$detailLogData['callee_id'] = self::$_tcSystemData['calleeId'];
				//被请求接口
				$detailLogData['interface_id'] = self::$_tcSystemData['openIfs'];
				//被调方接受请求时间
				$detailLogData['receive_time'] = self::$_tcSystemData['beginTime'];
				//被调方处理请求返回时间
				$detailLogData['return_time'] = $endTime;
				//处理耗时
				$detailLogData['process_duration'] = $endTime - self::$_tcSystemData['beginTime'];
				//请求返回码
				$detailLogData['response_code'] = $returnData[API_RETURN_DATA_CODE_FIELD];
				//请求内容
				$detailLogData['request_content'] = self::$_tcSystemData['request_content'];
				//请求返回内容
				$detailLogData['response_message'] = $returnData[API_RETURN_DATA_MESSAGE_FIELD];
			}
			
			if (file_exists(TIANCAN_LOG_CONFIG_PATH)) {
				Logger::configure(TIANCAN_LOG_CONFIG_PATH);
			}else{
				return self::__outputData('TLOG2', '引入log4php日志配置文件不存在！');
			}
			$logger = Logger::getLogger(self::$_tcSystemData['authApp']);//储存主调系统ID
			
			if (!empty($statisLogData)){
				//记录概括日志
				$logger->info(json_encode($statisLogData));
			}
			if (!empty($detailLogData)){
				//记录详细日志
				$logger->info(json_encode($detailLogData));
			}
			
			return self::__outputData('SUCC', '日志记录成功！');
		}
		
		private static function __curlPost($url, $method, $postFields='', $header=''){
			$ch = curl_init();
			curl_setopt($ch, CURLOPT_URL, $url);
			curl_setopt($ch, CURLOPT_FAILONERROR, false);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
			curl_setopt($ch, CURLOPT_TIMEOUT, 20);
			if ($header){
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
			}
			if ($method == 'POST'){
				curl_setopt ( $ch, CURLOPT_POSTFIELDS, $postFields);
			}
			error_log(print_r($header,1),3,'/yd/oms/log/ch.log');
			$reponse = curl_exec($ch);
			if (curl_errno($ch)){
				return curl_error($ch);
			}else{
				$httpStatusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
				if (200 !== $httpStatusCode){
					return $reponse;
				}
			}
			curl_close($ch);
			return $reponse;
		}
		
		//qconf获取zookeeper节点值
		private static function __getConf($path, $idc=ZOOKEEPER_IDC, $getFlag=0){
			return Qconf::getConf($path, $idc, $getFlag);
		}
		
		//qconf获取zookeeper节点的子节点
		private static function __getBatchKeys($path, $idc=ZOOKEEPER_IDC, $getFlag=0){
			return Qconf::getBatchKeys($path, $idc, $getFlag);
		}
		
		//qconf获取zookeeper节点的子节点和子节点的值,key=>value形式
		private static function __getBatchConf($path, $idc=ZOOKEEPER_IDC, $getFlag=0){
			return Qconf::getBatchConf($path, $idc, $getFlag);
		}
		
		//qconf获取zookeeper给定路径下获取所有可用的服务
		private static function __getAllHost($path, $idc=ZOOKEEPER_IDC, $getFlag=0){
			return Qconf::getAllHost($path, $idc, $getFlag);
		}
		
		//qconf获取zookeeper给定路径下获取一个可用的服务
		private static function __getHost($path, $idc=ZOOKEEPER_IDC, $getFlag=0){
			return Qconf::getHost($path, $idc, $getFlag);
		}
		
		//获取时间戳,精确到微秒
		private static function __getNewTime(){
			
			list($usec, $sec) = explode(" ", microtime());
			$time = ((float)$usec + (float)$sec);
			return $time;
		}
		
		//获取32位hash值
		private static function __hashCode32($str) {
			$h = 0;  
			$len = strlen($str);  
			for($i = 0; $i < $len; $i++)  {  
				$v = 31 * $h + ord($str[$i]);
				$v = $v % 4294967296;  
				if ($v > 2147483647){
					$h = $v - 4294967296;  
				}elseif ($v < -2147483648){
					$h = $v + 4294967296;
				}else{
					$h = $v;
				}  
			}  
			return $h;  
		}
		
		//获取64位hash值
		private static function __hashCode64($str) {
			$str = (string)$str;  
			$hash = 0;  
			$len = strlen($str);  
			if ($len == 0 )  
				return $hash;  
		   
			for ($i = 0; $i < $len; $i++) {  
				$h = $hash << 5;  
				$h -= $hash;  
				$h += ord($str[$i]);  
				$hash = $h;  
				$hash &= 0xFFFFFFFF;  
			}  
			return $hash;  
		}
		
		//获取访问IP
		private static function __getRequestIp() {
			
			$realip = '';
			if ($realip !== ''){
				return $realip;
			}
		
			if (isset($_SERVER)){
				//1.有代理
				if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])){
					$arr = explode(',', $_SERVER['HTTP_X_FORWARDED_FOR']);    
					/* 取X-Forwarded-For中第一个非unknown的有效IP字符串 */
					foreach ($arr AS $ip){
						$ip = trim($ip);    
						if ($ip != 'unknown'){
							$realip = $ip;    
							break;
						}
					}
				}elseif (isset($_SERVER['HTTP_CLIENT_IP'])){//2.无代理
					$realip = $_SERVER['HTTP_CLIENT_IP'];
				}else{
					if (isset($_SERVER['REMOTE_ADDR'])){
						$realip = $_SERVER['REMOTE_ADDR'];
					}else{
						$realip = '0.0.0.0';
					}
				}
			}else{//getenv不支持IIS的isapi方式运行的php
				if (getenv('HTTP_X_FORWARDED_FOR')){
					$realip = getenv('HTTP_X_FORWARDED_FOR');
				}elseif (getenv('HTTP_CLIENT_IP')){
					$realip = getenv('HTTP_CLIENT_IP');
				}else{
					$realip = getenv('REMOTE_ADDR');
				}
			}
		
			preg_match('/^([0-9]{1,3}\.){3}[0-9]{1,3}$/', $realip, $onlineip);
			$realip = !empty($onlineip[0]) ? $onlineip[0] : '0.0.0.0';
		
			return $realip;
		}
		
		//返回结果
		private static function __outputData($status, $msg){
			
			$returnData = sprintf(self::$_apiReturnDataType, $status, $msg);
			
			return $returnData;
		}
	}