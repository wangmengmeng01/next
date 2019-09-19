<?php
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// START SNIPPET: doxia
require dirname(__FILE__) . '/Logger.php';

//定义elk日志xml配置文件目录
define('ELK_PATH_CONFIG', dirname(__FILE__) . '/AppenderRollingFile.xml');

class elk {
    private $_logger;
    private static  $begin;
    private static  $end;
    private static  $bench_start;   //开始时间 微秒
    private static  $bench_end;     //结束时间 微秒


	function __construct()
	{
		$this::$begin = date("Y-m-d H:i:s",time());
		list($t1, $t2) = explode(' ', microtime());
		$this::$bench_start  = (float)sprintf('%.0f', (floatval($t1) + floatval($t2)) * 1000);
	}

	//写入记录日志
	public function write_log($arr, $file = ELK_PATH_CONFIG)
	{
		list($t1, $t2) = explode(' ', microtime());
		$this::$end = date("Y-m-d H:i:s",time());
		$this::$bench_end  = (float)sprintf('%.0f', (floatval($t1) + floatval($t2)) * 1000);
		$cost = $this::$bench_end - $this::$bench_start;
		$arr_sys = array (
			"l_beg"=>$this::$begin,
			"l_end"=>$this::$end,
			"l_est" => $cost,

		);
	    $ult_arr = array_merge($arr,$arr_sys);
		Logger::configure($file);
		$this->_logger = Logger::getRootLogger();
		$json_info = json_encode($ult_arr);
		$this->_logger->error($json_info);
	}
	
	/**
	 * 生成日志数据
	 * @param string $moduel 模块
	 * @param string $operate 操作
	 * @param string $level 日志级别
	 * @param string $primary 参数(单号，主键类) 
	 * @param string $user 人员
	 * @param string $company 公司
	 * @param string $device 设备
	 * @param string $ip 客户端请求IP
	 * @param mix $input 输入数据
	 * @param mix $output 输出数据
	 * @param string $status 状态（Y-成功,N-失败）
	 * @return array 
	 */
    public function make_log_data($moduel, $operate, $level, $primary, $user, $company, $ip, $input = array(), $output = array(), $status = 'Y', $device = '') 
    {
		$data= array(
				'l_sys' => 'oms', //系统
				'l_mod' => $moduel, //模块
				'l_opt' => $operate, //操作
				'l_lvl' => $level, //日志级别
				'l_par' => $primary, //参数(单号，主键类) 
				'l_emp' => $user, //人员
				'l_cmp' => $company, //公司
				'l_dev' => $device, //设备
				'l_ipx' => $ip, //客户端请求IP
				'l_input' => is_array($input) ? json_encode($input) : $input, //输入数据
				'l_output' => is_array($output) ? json_encode($output) : $output, //输出数据
				'l_inf' => $status, //状态（Y-成功,N-失败）
		);
		return $data;
	}

}



