<?php
class OrderCommand  extends CConsoleCommand
{

    /**
     * 队列数据转发给WMS
     */
    public function actionPush()
    {
        //redis配置参数
        $redisConfig = include_once ROOT_DIR . 'api/config/params.php';
        $queueServer = $redisConfig['queue_server'];
        $queueConfig = $redisConfig[$queueServer];        
        $innerApiUrl = $redisConfig['inner_api_url'];       

        //设置日志存储路径
        echo date('Y-m-d H:i:s').'  start...'.PHP_EOL;
        Yii::log('开始转发...', 'info', 'crontab');

        register_shutdown_function(array(&$this, 'shutdown'));

        try{
            //请求队列
            include_once ROOT_DIR . 'api/ext/queue.php';
            $queueObj = queue::instance($queueServer, $queueConfig);

            //循环获取每个货主redis中的销售订单数据
            $sql = "SELECT customer_id FROM t_base_customer WHERE active_flag='Y' AND is_valid=1";
            $db = Yii::app()->db;
            $model = $db->createCommand($sql);
            $rs = $model->queryAll();
            if (empty($rs)) {
            	Yii::log('没有货主信息!', 'error', 'crontab');
            	die();
            } else {
            	$totalCounter = 0;
            	foreach ($rs as $v)
            	{
            		//校验货主配置是否完整
            		$checkFlag = $this->check_customer($v['customer_id']);
            		if (!$checkFlag) {
            			Yii::log('货主基础信息配置不完整，终止推送：'.$v['customer_id'], 'error', 'crontab');
            			continue;
            		}
            		$counter = 0;            		
            		$taskName = $redisConfig['queue_name'] . '_' . $v['customer_id'];
            	    $failQueueName = $redisConfig['fail_queue_name'] . '_' . $v['customer_id'];

            		while(true) {
            			$queueData = $queueObj->pop($taskName);
            			if ($queueData) {
            				$queueDataArr = json_decode($queueData, true);
            		
            				//请求内部API接口
            				$data = json_decode($queueDataArr['data'], true);
            				$params = array(
            						'inner_service' => 'true',
            						'customer_id' => $queueDataArr['customer_id'],
            						'method' => 'putSOData_OmsToWms',
            						'data' => json_encode($data),
            						'messageid' => $queueDataArr['messageid'],
            						'timestamp'=>date("Y-m-d H:i:s"),
            				);

            				$response = util::curl($innerApiUrl, $params);
            				$responseArr = array();
            				$logArr = array();
            				if ($response){
            					$responseArr = json_decode($response, true);     

                                if ($responseArr['returnFlag'] == 2) {
            						//获取返回的错误数据的完整信息
            						$errorArr = $this->get_full_data($data, $responseArr['resultInfo']);         
            						$errorRedisArr = array(
					    					'customer_id' => $queueDataArr['customer_id'],
					    					'method' => $queueDataArr['method'],
					    					'method_to' => $queueDataArr['method_to'],
					    					'data' => json_encode($errorArr),
					    					'messageid' => $queueDataArr['messageid'],
					    					'timestamp' => date("Y-m-d H:i:s"),
            								'send_num' => 1   //存放推送的次数
					    			);           			
            						 			
            						//根据返回的错误信息获取失败订单数据的完整订单信息
            						Yii::log('请求失败，写入失败队列：'.$failQueueName.'失败原因：'.$responseArr['returnDesc'], 'error', 'crontab');           		
            						//写入失败队列
            						$queueObj->push($failQueueName, json_encode($errorRedisArr));
            					} elseif ($responseArr['returnFlag'] == 0) {
            						$errorRedisArr = array(
            								'customer_id' => $queueDataArr['customer_id'],
            								'method' => $queueDataArr['method'],
            								'method_to' => $queueDataArr['method_to'],
            								'data' => json_encode($data),
            								'messageid' => $queueDataArr['messageid'],
            								'timestamp' => date("Y-m-d H:i:s"),
            								'send_num' => 1   //存放推送的次数
            						);
            						Yii::log('请求失败，写入失败队列：'.$failQueueName.'失败原因：'.$responseArr['returnDesc'], 'error', 'crontab');
            						//写入失败队列
            						$queueObj->push($failQueueName, json_encode($errorRedisArr));
            					}
          					           					
            				} else {
            					$errorRedisArr = array(
            							'customer_id' => $queueDataArr['customer_id'],
            							'method' => $queueDataArr['method'],
            							'method_to' => $queueDataArr['method_to'],
            							'data' => json_encode($data),
            							'messageid' => $queueDataArr['messageid'],
            							'timestamp' => date("Y-m-d H:i:s"),
            							'send_num' => 1   //存放推送的次数
            					);
            					Yii::log('对方接口无响应：'.$failQueueName.'失败原因：接口未获取到响应数据', 'error', 'crontab');
            					//写入失败队列
            					$queueObj->push($failQueueName, json_encode($errorRedisArr));
            				}
            				//获取订单推送日志的数据
            				$logArr = $this->get_log_data($v['customer_id'], $data, $responseArr);
            				//记录订单推送日志
            				$this->insert_order_log($logArr);
            				
            				$queueDataArr = null;
            				$queueData = null;
            				$errorArr = null;
            				$errorRedisArr = null;
            				$logArr =null;
            			} else {
            				Yii::log('队列:'.$taskName.'执行完成，休息一会儿，继续执行!', 'info', 'crontab');
            				break;
            			}
            		
            			//休息一会儿
            			$counter++;
            			$totalCounter++;
            			if ($counter >= 1000) {
            				$counter = 0;
            				sleep(2);
            			}
            		}
            	}
            }           
        } catch(Exception $e) {
            Yii::log('转发错误:' . $e->getMessage() . ' in file:'. $e->getFile() . ' in line:'.$e->getLine(), 'error', 'crontab');
        }

        Yii::log('-----结束...共转发：'.$totalCounter.PHP_EOL.PHP_EOL, 'info', 'crontab');
    }

    /**
     * 重新转发失败队列中的数据给WMS，最多转发5次，超过5次还没有转发成功的数据将记录到数据库中
     */
    public function actionRetry()
    {
    	//redis配置参数
    	$redisConfig = include_once ROOT_DIR . 'api/config/params.php';
    	$queueServer = $redisConfig['queue_server'];
    	$queueConfig = $redisConfig[$queueServer];
    	$innerApiUrl = $redisConfig['inner_api_url'];
    	
    	//设置日志存储路径
    	echo date('Y-m-d H:i:s').'  start...'.PHP_EOL;
    	Yii::log('开始重发错误队列订单数据...', 'info', 'crontab');
    	
    	register_shutdown_function(array(&$this, 'shutdown'));
    	
    	try{  
    		//请求队列
    		include_once ROOT_DIR . 'api/ext/queue.php';  
    		include_once ROOT_DIR . 'api/ext/httpclient.php';
    		$queueObj = queue::instance($queueServer, $queueConfig);  		
    		    	
    		//循环获取每个货主redis中的销售订单数据
    		$sql = "SELECT customer_id FROM t_base_customer WHERE active_flag='Y' AND is_valid=1";
    		$db = Yii::app()->db;
    		$model = $db->createCommand($sql);
    		$rs = $model->queryAll();
    		if (empty($rs)) {
    			Yii::log('没有货主信息!', 'error', 'crontab');
    			die();
    		} else {
    			$totalCounter = 0;
    			foreach ($rs as $v)
    			{
    				//校验货主配置是否完整
    				$checkFlag = $this->check_customer($v['customer_id']);
    				if (!$checkFlag) {
    					Yii::log('货主基础信息配置不完整，终止推送：'.$v['customer_id'], 'error', 'crontab');
    					continue;
    				}
    				$counter = 0;
    				$failQueueName = $redisConfig['fail_queue_name'] . '_' . $v['customer_id'];

    				while(true) {
    					$queueData = $queueObj->pop($failQueueName);
    					$errorArr = array();
    					if ($queueData) {
    						$queueDataArr = json_decode($queueData, true);
    	                    $send_num =  $queueDataArr['send_num'];
    	                    
    						//请求内部API接口
    						$data = json_decode($queueDataArr['data'], true);
    						$params = array(
    								'inner_service' => 'true',
    								'customer_id' => $queueDataArr['customer_id'],
    								'method' => 'putSOData_OmsToWms',
    								'data' => json_encode($data),
    								'messageid' => $queueDataArr['messageid'],
    								'timestamp' => date("Y-m-d H:i:s"),
    						);
    						
    						$httpObj =  new httpclient();
    						//$response = util::curl($innerApiUrl, $params, $timeout=5);
    						$response = $httpObj->post($innerApiUrl, $params);
    						$logArr = array();   						
    						$responseArr = array();
    						if ($response){
    							$responseArr = json_decode($response, true);
    							$send_num++;
    							if ($responseArr['returnFlag'] == 2 || $responseArr['returnFlag'] == 0) {
    								if ($responseArr['returnFlag'] == 2) {
    									//获取错误数据的完整信息
    									$errorArr = $this->get_full_data($data, $responseArr['resultInfo']);   									
    								} elseif ($responseArr['returnFlag'] == 0) {
    									$errorArr = $data;
    								}
    								if ($send_num >=5) {
    									//写入失败数据到出单单异常表
    									$errorInsertArr = $this->get_full_data($data, $responseArr['resultInfo'], $send_num);
    									$this->inser_table($errorInsertArr, 't_outbound_info_exception');
    								} else {
    									$errorRedisArr = array(
    											'customer_id' => $queueDataArr['customer_id'],
    											'method' => $queueDataArr['method'],
    											'method_to' => $queueDataArr['method_to'],
    											'data' => json_encode($errorArr),
    											'messageid' => $queueDataArr['messageid'],
    											'timestamp' => date("Y-m-d H:i:s"),
    											'send_num' => $send_num  //存放推送的次数
    									);
    									//根据返回的错误信息获取失败订单数据的完整订单信息
    									Yii::log('请求失败，重新写入失败队列：'.$failQueueName.'失败原因：'.$responseArr['returnDesc'], 'error', 'crontab');
    									//写入失败队列
    									$queueObj->push($failQueueName, json_encode($errorRedisArr));
    								}
    							} else {
    							    //把成功的数据写入数据库    								
    								//$this->inser_table($data, 't_outbound_info');
    							}    							
    						} else {
    							$send_num++;
    							if ($send_num >= 5) {
    								//写入失败数据到出单单异常表
    								$this->inser_table($data, 't_outbound_info_exception');
    							} else {
    								$errorRedisArr = array(
    										'customer_id' => $queueDataArr['customer_id'],
    										'method' => $queueDataArr['method'],
    										'method_to' => $queueDataArr['method_to'],
    										'data' => json_encode($data),
    										'messageid' => $queueDataArr['messageid'],
    										'timestamp' => date("Y-m-d H:i:s"),
    										'send_num' => $send_num   //存放推送的次数
    								);
    								Yii::log('对方接口无响应：'.$failQueueName.'失败原因：接口未获取到响应数据', 'error', 'crontab');
    								//写入失败队列
    								$queueObj->push($failQueueName, json_encode($errorRedisArr));
    							}
    						} 
    						//获取订单推送日志的数据
    						$logArr = $this->get_log_data($v['customer_id'], $data, $responseArr);
    						//记录订单推送日志
    						$this->insert_order_log($logArr);
    					} else {
    						Yii::log('队列:'.$taskName.'执行完成，休息一会儿，继续执行!', 'error', 'crontab');
    						break;
    					}    					
    	
    					//休息一会儿
    					$counter++;
    					$totalCounter++;
    					if ($counter >= 1000) {
    						$counter = 0;
    						sleep(2);
    					}
    				}
    			}
    		}
    	} catch(Exception $e) {
    		Yii::log('转发错误:' . $e->getMessage() . ' in file:'. $e->getFile() . ' in line:'.$e->getLine(), 'error', 'crontab');
    	}
    	
    	Yii::log('-----结束...共转发：'.$totalCounter.PHP_EOL.PHP_EOL, 'info', 'crontab');
    }
    
    /**
     * 推送通过excel导入的出库单信息给WMS，每次获取1个订单数据
     */
    public function actionExcelPush()
    {
    	//redis配置参数
    	$redisConfig = include_once ROOT_DIR . 'api/config/params.php';
    	$queueServer = $redisConfig['queue_server'];
    	$queueConfig = $redisConfig[$queueServer];
    	
    	//设置日志存储路径
    	echo date('Y-m-d H:i:s').'  start...'.PHP_EOL;
    	Yii::log('开始读取出库单表中excel导入的订单...', 'info', 'crontab');
    	
    	register_shutdown_function(array(&$this, 'shutdown'));
    	
    	try{
	    	$db = Yii::app()->db;
	    	while (true) {
		    	//获取excel导入的订单数据
		    	$start_time = date("Y-m-d", strtotime("-5 day")) . ' 00:00:00';
		    	$sql = "SELECT order_id,order_no,order_type,customer_id,warehouse_code,delivery_no,consignee_id,consignee_name,c_province,c_city,c_tel1,c_tel2,c_zip,c_mail,c_address1,c_address2,user_define4,user_define5,invoice_print_flag,remark,h_edi_01,h_edi_02,h_edi_03,h_edi_04,h_edi_05,h_edi_06,h_edi_07,h_edi_08,h_edi_09,h_edi_10,route_code,carrier_fax,channel,carrier_id  
		    	        FROM t_outbound_info WHERE create_time>='$start_time' AND source='excel' AND redis_flag=0 AND is_valid=1 ORDER BY create_time LIMIT 1";    	
		    	$model = $db->createCommand($sql);
		    	$rs = $model->queryRow();
		    	if (empty($rs)) {
		    		Yii::log('没有需要推送给队列的excel数据', 'info', 'crontab');
		    		die();
		    	} else {
		    		//请求队列
		    		include_once ROOT_DIR . 'api/ext/queue.php';
		    		$queueObj = queue::instance($queueServer, $queueConfig);
		    		
		    		try{
			    		//校验队列是否启动正常
			    		$ping = $queueObj->ping();
			    		if ($ping != '+PONG') {
			    			Yii::log('队列未启动', 'error', 'crontab');
			    		    die();
			    		}
		    		} catch (Exception $e) {
		    			Yii::log('队列启动异常', 'error', 'crontab');
		    			die();
		    		}
		    		
		    		//获取表头数据
		    		$headerColumnArr = $this->get_dataBase_relation('outbound_info');    		
		    		$headerArr = array();
		    		foreach ($headerColumnArr as $key=>$val)
		    		{
		    			if (in_array($val, array_keys($rs))) {
		    				$headerArr[$key] = $rs[$val];
		    			}
		    		}
		    		//获取订单明细信息    		
		    		$sql = "SELECT sku,qty_ordered,lot_att01,lot_att02,lot_att03,lot_att04,lot_att05,lot_att06,lot_att07,lot_att08,lot_att09,lot_att10,lot_att11,lot_att12,uom,release_status,priority,single_match 
		    				FROM t_outbound_detail WHERE order_id=:order_id AND is_valid=1";
		    		$model = $db->createCommand($sql);
		    		$model->bindValue(':order_id', $rs['order_id']);
		    		$row = $model->queryAll();
		    		if (!empty($row)) {
		    			$detailColumnArr =  $this->get_dataBase_relation('outbound_detail');
		    			$detailArr = array();
		    			foreach ($row as $key=>$val)
		    			{
		    				foreach ($detailColumnArr as $k=>$v)
		    				{
		    					if (in_array($v, array_keys($val))) {
		    						$detailArr[$key][$k] = $val[$v];
		    					}
		    				}
		    			}
		    		}
		    		//组合需要写入redis中的数据
		    		$headerArr['detailsItem'] = $detailArr;
		    		$orderArr = array($headerArr);
		    		$queueData = array(
		    					'customer_id' => $rs['customer_id'],
		    					'method' => 'putSOData_OmsToWms',
		    					'method_to' => 'putSOData',
		    					'data' => json_encode($orderArr),
		    					'messageid' => 'SO',
		    					'timestamp' => date("Y-m-d H:i:s")
		    		);
		    		//写入数据到redis
		    		$taskName = $redisConfig['queue_name'] . '_' . $rs['customer_id'];
		    		$queueObj->push($taskName, json_encode($queueData));
		    		//更新订单状态
		    		$sql = 'UPDATE t_outbound_info SET redis_flag=1 WHERE order_id=:order_id';
		    		$model = $db->createCommand($sql);
		    		$model->bindValue(':order_id', $rs['order_id']);
		    		$model->execute();
		    	}
	    	}
    	} catch(Exception $e) {
    		Yii::log('写入excel导入订单数据失败:' . $e->getMessage() . ' in file:'. $e->getFile() . ' in line:'.$e->getLine(), 'error', 'crontab');
    	}   	
    }
    
    /**
     * 当前类函数执行结束所执行的方法,不管是正常还是异常情况
     */
    public function shutdown(){
        //程序意外退出
        $errorMsg = '';
        if(function_exists('error_get_last')){
            $error = error_get_last();
            $errorCode = $error['type'];
            switch($errorCode) {
                case E_ERROR:
                case E_PARSE:
                case E_CORE_ERROR:
                case E_COMPILE_ERROR:
                    $errorMsg = $error['message']." in file ".$error['file']." on line ".$error['line'];
                    break;
            }
        }else{
            $errorMsg = '程序意外中止';
        }

        //程序异常
        if ($errorMsg) {
            //记录日志
            file_put_contents(LOG_PATH . '/'.LOG_FILENAME, date('Y-m-d H:i:s').'错误:'.$errorMsg);

            //错误返回输出
            echo 'error：'.$errorMsg;
        }

        echo date('Y-m-d H:i:s').' finish!';
    }

    /**
     * 获取2个数组的交集
     * @param  array
     * @return array
     */
    public function get_full_data($sourceArr, $objectiveArr, $send_num='')
    {
    	$returnArr = array();
    	if (!empty($objectiveArr)) {
    		foreach ($objectiveArr as $k => $v)
    		{
    			foreach ($sourceArr as $b)
    			{
    				if ($v['OrderNo'] == $b['OrderNo'] && $v['OrderType'] == $b['OrderType'] && $v['WarehouseID'] == $b['WarehouseID']) {
    					$returnArr[$k] = $b;
    					if ($send_num != '') {
    						$returnArr[$k]['errorcode'] = $v['errorcode'];
    						$returnArr[$k]['errordescr'] = $v['errordescr'];
    						$returnArr[$k]['send_num'] = $send_num;
    					}
    					break;
    				}
    			}
    		}
    	} else {
    		$returnArr = $sourceArr;
    	}
    	return $returnArr;
    }
    
    /**
     * 获取2个数组间的差集
     * @param  array
     * @return array
     */
    public function get_diff_data($sourceArr, $objectiveArr)
    {
    	if (!empty($objectiveArr)) {
    		foreach ($objectiveArr as $v)
    		{
    			foreach ($sourceArr as $a => $b)
    			{
    				if ($v['OrderNo'] == $b['OrderNo'] && $v['OrderType'] == $b['OrderType'] && $v['WarehouseID'] == $b['WarehouseID']) {
    					unset($sourceArr[$a]);
    				}
    			}
    		}
    		$returnArr = $sourceArr;
    	} else {
    		$returnArr = $sourceArr;
    	}
    	return $returnArr;
    }
    
    /**
     * 写入订单数据到数据库
     */
    public function inser_table($data, $tableName)
    {
    	if (!empty($data)) {
    		$db = Yii::app()->db;
    		//获取出库单单头基础信息接口参数与数据库字段对应关系
    		$column_orderInfo_arr = $this->get_dataBase_relation('outbound_info');
    		if ($tableName == 't_outbound_info') {   				
    			$column_key_orderInfo = implode(',', array_values($column_orderInfo_arr)) . ',create_time';
    		} else {
    			$column_key_orderInfo = implode(',', array_values($column_orderInfo_arr)) . ',send_num,error_code,error_desc,create_time';
    		}
    			  				
    		//获取出库单明细基础信息接口参数与数据库字段对应关系
    		$column_orderDetail_arr = $this->get_dataBase_relation('outbound_detail');
    		$column_key_orderDetail = implode(',', array_values($column_orderDetail_arr)) . ',order_id,create_time';   				
    				
    		//获取出单单发票信息接口参数与数据库字段对应关系
    		$column_orderBill_arr = $this->get_dataBase_relation('outbound_bill');
    		$column_key_orderBill = implode(',', array_values($column_orderBill_arr)) . ',order_id,create_time';   				
    	
    		foreach ($data as $val)
    		{
    			//判断订单号是否已经存在，如果存在则把原订单号置为无效，并且插入新的订单号
    			if ($tableName == 't_outbound_info') {
    				$updateDetailTable = 't_outbound_detail';
    				$updateBillTable = 't_outbound_bill_info';
    			} else {
    				$updateDetailTable = 't_outbound_detail_exception';
    				$updateBillTable = 't_outbound_bill_info_exception';
    			}
    			$sql = "SELECT order_id FROM {$tableName} WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
    			$model = $db->createCommand($sql);
    			$model->bindValue(':order_no', $val['OrderNo']);
    			$model->bindValue(':order_type', $val['OrderType']);
    			$model->bindValue(':customer_id', $val['CustomerID']);
    			$model->bindValue(':warehouse_code', $val['WarehouseID']);
    			$rs = $model->queryRow();
    			if (!empty($rs)) {
    				$orderId = $rs['order_id'];
    				if ($tableName == 't_outbound_info') {   					
	    				//获取订单明细中的sku数量
	    				$sql = "SELECT sku,qty_ordered FROM t_outbound_detail WHERE order_id='$orderId'";
	    				$model = $db->createCommand($sql);
	    				$row = $model->queryAll();
	    				if (!empty($row)) {
	    					foreach ($row as $k => $v)
	    					{
	    						//更新库存
	    						$this->update_outbound_inventory($val['CustomerID'], $val['WarehouseID'], $v['sku'], $v['qty_ordered']*-1);
	    					}
	    				}
    				}
    				//把原有的订单更新为无效
    				$sql = "UPDATE {$tableName} SET is_valid=0 WHERE order_id=:order_id";
    				$model = $db->createCommand($sql);
    				$model->bindValue(':order_id', $orderId);
    				$model->execute();
    				$sql = "UPDATE {$updateDetailTable} SET is_valid=0 WHERE order_id=:order_id";
    				$model = $db->createCommand($sql);
    				$model->bindValue(':order_id', $orderId);
    				$model->execute();
    				$sql = "UPDATE {$updateBillTable} SET is_valid=0 WHERE order_id=:order_id";
    				$model = $db->createCommand($sql);
    				$model->bindValue(':order_id', $orderId);
    				$model->execute();
    			}	
    			
    			//写入出库单单头信息
    			if ($tableName == 't_outbound_info') {
    				$tableDetailName = 't_outbound_detail';
    				$tableBillName = 't_outbound_bill_info';
    				$column_value_orderInfo = ":" . implode(",:", array_keys($column_orderInfo_arr)) . ",now()";
    			} else {
    				$tableDetailName = 't_outbound_detail_exception';
    				$tableBillName = 't_outbound_bill_info_exception';
    				$column_value_orderInfo = ":" . implode(",:", array_keys($column_orderInfo_arr)) . ",:send_num,:error_code,:error_desc,now()";
    			}   				
    			$sql_orderInfo = "INSERT INTO {$tableName}({$column_key_orderInfo}) VALUES({$column_value_orderInfo})";
    			$model = $db->createCommand($sql_orderInfo);
    			$values = array();
    			foreach ($column_orderInfo_arr as $k => $v)
    			{
    				$values[':'.$k] = empty($val[$k]) ? '' : $val[$k];
    			}
    			if ($tableName == 't_outbound_info_exception') {
    				$values[':send_num'] = isset($val['send_num']) ? $val['send_num'] : 5;
    				$values[':error_code'] = isset($val['errorcode']) ? $val['errorcode'] : '';
    				$values[':error_desc'] = isset($val['errordesc']) ? $val['errordesc'] : '';
    			}
    			$model->bindValues($values);
    			$model->execute();
    			$order_id = $db->getLastInsertID();
    					
    			if ($order_id != '') {   				
	    			//写入出单单明细信息
	    			$column_value_orderDetail = ":" . implode(",:", array_keys($column_orderDetail_arr)) . ",'{$order_id}',now()";
	    			$sql_orderDetail = "INSERT INTO {$tableDetailName}({$column_key_orderDetail}) VALUES({$column_value_orderDetail})";
	    			if (!empty($val['detailsItem'])) {
	    				$model = $db->createCommand($sql_orderDetail);
	    				foreach ($val['detailsItem'] as $b)
	    				{
	    					$values = array();
	    					foreach ($column_orderDetail_arr as $k => $v)
	    					{
	    						$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
	    					}
	    					$model->bindValues($values);
	    					$model->execute();
	    					//更新库存
	    					if ($tableName == 't_outbound_info') {
	    						$this->update_outbound_inventory($val['CustomerID'], $val['WarehouseID'], $b['SKU'], $b['QtyOrdered']);
	    					}
	    				}
	    			}
	    					
	    			//写入发票数据
	    			$column_value_orderBill = ":" . implode(",:", array_keys($column_orderBill_arr)) . ",'{$order_id}',now()";
	    			$sql_orderBill = "INSERT INTO {$tableBillName}({$column_key_orderBill}) VALUES({$column_value_orderBill})";
	    			if (!empty($val['invoiceItem'])) {
	    				$model = $db->createCommand($sql_orderBill);
	    				foreach ($val['invoiceItem'] as $b)
	    				{
	    					$values = array();
	    					foreach ($column_orderBill_arr as $k => $v)
	    					{
	    						$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
	    					}
	    					$model->bindValues($values);
	    					$model->execute();
	    				}
	    			}
    			}
    		}   		
    	}
    }
    
    /**
     * 接口参数与数据库字段对应关系
     * @param  $type 类型（ outbound_info：出库单单头信息， outbound_detail：出库单明细信息  ， outbound_bill：出库单发票信息  ）
     * @return array
     */
    public function get_dataBase_relation($type)
    {
    	$return_arr = array();
    	if ($type == 'outbound_info') {
    	    $return_arr['OrderNo'] = 'order_no';
    		$return_arr['OrderType'] = 'order_type';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['WarehouseID'] = 'warehouse_code';
    		$return_arr['OrderTime'] = 'order_time';
    		$return_arr['ExpectedShipmentTime1'] = 'expected_shipment_time1';
    		$return_arr['RequiredDeliveryTime'] = 'required_delivery_time';
    		$return_arr['SOReference2'] = 'so_reference2';
    		$return_arr['SOReference3'] = 'so_reference3';
    		$return_arr['SOReference4'] = 'so_reference4';
    		$return_arr['SOReference5'] = 'so_reference5';
    		$return_arr['DeliveryNo'] = 'delivery_no';
    		$return_arr['ConsigneeID'] = 'consignee_id';
    		$return_arr['ConsigneeName'] = 'consignee_name';
    		$return_arr['C_Country'] = 'c_country';
    		$return_arr['C_Province'] = 'c_province';
    		$return_arr['C_City'] = 'c_city';
    		$return_arr['C_Tel1'] = 'c_tel1';
    		$return_arr['C_Tel2'] = 'c_tel2';
    		$return_arr['C_ZIP'] = 'c_zip';
    		$return_arr['C_Mail'] = 'c_mail';
    		$return_arr['C_Address1'] = 'c_address1';
    		$return_arr['C_Address2'] = 'c_address2';
    		$return_arr['C_Address3'] = 'c_address3';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    		$return_arr['InvoicePrintFlag'] = 'invoice_print_flag';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['H_EDI_01'] = 'h_edi_01';
    		$return_arr['H_EDI_02'] = 'h_edi_02';
    		$return_arr['H_EDI_03'] = 'h_edi_03';
    		$return_arr['H_EDI_04'] = 'h_edi_04';
    		$return_arr['H_EDI_05'] = 'h_edi_05';
    		$return_arr['H_EDI_06'] = 'h_edi_06';
    		$return_arr['H_EDI_07'] = 'h_edi_07';
    		$return_arr['H_EDI_08'] = 'h_edi_08';
    		$return_arr['H_EDI_09'] = 'h_edi_09';
    		$return_arr['H_EDI_10'] = 'h_edi_10';
    		$return_arr['UserDefine6'] = 'user_define6';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'route_stop';
    		$return_arr['CarrierMail'] = 'carrier_mail';
    		$return_arr['CarrierFax'] = 'carrier_fax';
    		$return_arr['Channel'] = 'channel';
    		$return_arr['CarrierId'] = 'carrier_id';
    		$return_arr['CarrierName'] = 'carrier_name';
    	} elseif ($type == 'outbound_detail') {
    		$return_arr['LineNo'] = 'line_no';
    		$return_arr['CustomerID'] = 'customer_id';
    		$return_arr['SKU'] = 'sku';
    		$return_arr['QtyOrdered'] = 'qty_ordered';
    		$return_arr['LotAtt01'] = 'lot_att01';
    		$return_arr['LotAtt02'] = 'lot_att02';
    		$return_arr['LotAtt03'] = 'lot_att03';
    		$return_arr['LotAtt04'] = 'lot_att04';
    		$return_arr['LotAtt05'] = 'lot_att05';
    		$return_arr['LotAtt06'] = 'lot_att06';
    		$return_arr['LotAtt07'] = 'lot_att07';
    		$return_arr['LotAtt08'] = 'lot_att08';
    		$return_arr['LotAtt09'] = 'lot_att09';
    		$return_arr['LotAtt10'] = 'lot_att10';
    		$return_arr['LotAtt11'] = 'lot_att11';
    		$return_arr['LotAtt12'] = 'lot_att12';
    		$return_arr['UserDefine1'] = 'uer_define1';
    		$return_arr['UserDefine2'] = 'uer_define2';
    		$return_arr['UserDefine3'] = 'uer_define3';
    		$return_arr['UserDefine4'] = 'uer_define4';
    		$return_arr['UserDefine5'] = 'uer_define5';
    		$return_arr['UserDefine6'] = 'uer_define6';
    		$return_arr['Notes'] = 'remark';
    		$return_arr['Price'] = 'price';
    		$return_arr['D_EDI_03'] = 'd_edi_03';
    		$return_arr['D_EDI_04'] = 'd_edi_04';
    		$return_arr['D_EDI_05'] = 'd_edi_05';
    		$return_arr['D_EDI_06'] = 'd_edi_06';
    		$return_arr['D_EDI_07'] = 'd_edi_07';
    		$return_arr['D_EDI_08'] = 'd_edi_08';
    		$return_arr['D_EDI_09'] = 'd_edi_09';
    		$return_arr['D_EDI_10'] = 'd_edi_10';
    		$return_arr['D_EDI_11'] = 'd_edi_11';
    		$return_arr['D_EDI_12'] = 'd_edi_12';
    		$return_arr['D_EDI_13'] = 'd_edi_13';
    		$return_arr['D_EDI_14'] = 'd_edi_14';
    		$return_arr['D_EDI_15'] = 'd_edi_15';
    		$return_arr['D_EDI_16'] = 'd_edi_16';
    	} elseif ($type == 'outbound_bill') {
    		$return_arr['OrderNo'] = 'order_no';
    		$return_arr['LineNumber'] = 'line_number';
    		$return_arr['Title'] = 'title';
    		$return_arr['Reference1'] = 'reference1';
    		$return_arr['SKU'] = 'sku';
    		$return_arr['UOM'] = 'uom';
    		$return_arr['QTY'] = 'qty';
    		$return_arr['UnitPrice'] = 'unit_price';
    		$return_arr['Amount'] = 'amount';
    		$return_arr['TAXRATE'] = 'taxrate';
    		$return_arr['TAXAMOUNT'] = 'taxamount';
    		$return_arr['SKUDESCR'] = 'skudescr';
    		$return_arr['DetailTitle'] = 'detail_title';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['UserDefine1'] = 'user_define1';
    		$return_arr['UserDefine2'] = 'user_define2';
    		$return_arr['UserDefine3'] = 'user_define3';
    		$return_arr['UserDefine4'] = 'user_define4';
    		$return_arr['UserDefine5'] = 'user_define5';
    	} 
    	return $return_arr;
    }
    
    /**
     * 更新库存
     * @param string customerId
     * @param string warehouseId
     * @param string $sku
     * @param int $num
     * @return boolean
     */
    public function update_outbound_inventory($customerId, $warehouseId, $sku, $num)
    {
    	$db = Yii::app()->db;
    	$sql = "UPDATE t_product_inventory set qty=qty-:QtyOrdered,occupy_qty=occupy_qty+:QtyOrdered WHERE customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
    	$model = $db->createCommand($sql);
    	$values = array(
    			':QtyOrdered' => $num,
    			':customer_id' => $customerId,
    			':warehouse_code' => $warehouseId,
    			':sku' => $sku
    	);
    	$model->bindValues($values);
    	$model->execute();
    }
    
    /**
     * 校验货主是否具有数据推送的条件
     * @param string customerId
     * @return boolean
     */
    public function check_customer($customerId)
    {
    	$db = Yii::app()->db;
    	$sql = "SELECT a.customer_id,a.app_secret,b.erp_api_url,b.wms_api_url,b.erp_code,b.wms_code 
    			FROM t_base_customer a
    			LEFT JOIN t_bind_relation b ON a.customer_id=b.customer_id
    			WHERE a.customer_id=:customer_id AND a.is_valid=1 AND a.active_flag='Y' AND b.is_valid=1";
    	$model = $db->createCommand ($sql);
    	$model->bindValue(':customer_id', $customerId);
    	$rs = $model->queryRow();
    	if (empty($rs)) {
    		return false;
    	} else {
    		if ($rs['app_secret'] == '' || $rs['erp_api_url'] == '' || $rs['wms_api_url'] == '' || $rs['erp_code'] == '' || $rs['wms_code'] == '') {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * 获取推送错误数据的信息
     */
	public function get_log_data($customerId, $data, $response=array())
    {
    	$returnArr = array();
    	if (!empty($response)) {
    		if ($response['returnFlag'] == 1) {
    			foreach ($data as $key=>$val)
    			{  				
    				$returnArr[$key]['order_no'] = $val['OrderNo'];
    				$returnArr[$key]['order_type'] = $val['OrderType'];
    				$returnArr[$key]['customer_id'] = $val['CustomerID'];
    				$returnArr[$key]['warehouse_code'] = $val['WarehouseID'];
    				$returnArr[$key]['return_status'] = 1;
    				$returnArr[$key]['return_code'] = $response['returnCode'];
    				$returnArr[$key]['return_desc'] = $response['returnDesc'];
    				$returnArr[$key]['msg_id'] = $response['msg_id'];
    			}
    		} elseif ($response['returnFlag'] ==2) {
    			foreach ($data as $key=>$val)
    			{
    				$returnArr[$key]['order_no'] = $val['OrderNo'];
    				$returnArr[$key]['order_type'] = $val['OrderType'];
    				$returnArr[$key]['customer_id'] = $val['CustomerID'];
    				$returnArr[$key]['warehouse_code'] = $val['WarehouseID'];
    				$returnArr[$key]['return_status'] = 1;
    				$returnArr[$key]['return_code'] = $response['returnCode'];
    				$returnArr[$key]['return_desc'] = $response['returnDesc'];
    				$returnArr[$key]['msg_id'] = $response['msg_id'];
    				if (!empty($response['resultInfo'])) {   					
	    				foreach ($response['resultInfo'] as $v)
	    				{
	    					if ($val['OrderNo'] == $v['OrderNo'] && $val['OrderType'] == $v['OrderType'] && $val['CustomerID'] == $v['CustomerID'] && $val['WarehouseID'] == $v['WarehouseID']) {   						
	    						$returnArr[$key]['return_status'] = 0;
	    						$returnArr[$key]['return_code'] = $v['errorcode'] != '' ? $v['errorcode'] : $response['returnCode'];
	    						$returnArr[$key]['return_desc'] = $v['errordescr'] != '' ? $v['errordescr'] : $response['returnDesc'];
	    						break;
	    					}
	    				}
    				}
    			}
    		} else {
    			if (!empty($response['resultInfo'])) {
    				foreach ($response['resultInfo'] as $key=>$val)
    				{
    					$returnArr[$key]['order_no'] = $val['OrderNo'];
    					$returnArr[$key]['order_type'] = $val['OrderType'];
    					$returnArr[$key]['customer_id'] = $val['CustomerID'];
    					$returnArr[$key]['warehouse_code'] = $val['WarehouseID'];
    					$returnArr[$key]['return_status'] = 0;
    					$returnArr[$key]['return_code'] = $val['errorcode'] != '' ? $val['errorcode'] : $response['returnCode'];
    					$returnArr[$key]['return_desc'] = $val['errordescr'] != '' ? $val['errordescr'] : $response['returnDesc'];
    					$returnArr[$key]['msg_id'] = $response['msg_id'];
    				}
    			} else {
    				foreach ($data as $key=>$val)
    				{
    					$returnArr[$key]['order_no'] = $val['OrderNo'];
    					$returnArr[$key]['order_type'] = $val['OrderType'];
    					$returnArr[$key]['customer_id'] = $val['CustomerID'];
    					$returnArr[$key]['warehouse_code'] = $val['WarehouseID'];
    					$returnArr[$key]['return_status'] = 0;
    					$returnArr[$key]['return_code'] = $response['returnCode'];
    					$returnArr[$key]['return_desc'] = $response['returnDesc'];
    					$returnArr[$key]['msg_id'] = $response['msg_id'];
    				}
    			}    			
    		}
    	} else {
    		foreach ($data as $key=>$val)
    		{
    			$returnArr[$key]['order_no'] = $val['OrderNo'];
    			$returnArr[$key]['order_type'] = $val['OrderType'];
    			$returnArr[$key]['customer_id'] = $val['CustomerID'];
    			$returnArr[$key]['warehouse_code'] = $val['WarehouseID'];
    			$returnArr[$key]['return_status'] = 0;
    			$returnArr[$key]['return_code'] = '';
    			$returnArr[$key]['return_desc'] = '';
    			$returnArr[$key]['msg_id'] = '';
    		}
    	}
    	return $returnArr;
    }
    
    /**
     * 记录订单推送日志
     */
    public function insert_order_log($logArr)
    {
    	if (!empty($logArr)) {
    		$db = Yii::app()->db;
    		$sql = "INSERT INTO t_order_send_log(order_no,order_type,customer_id,warehouse_code,return_status,return_code,return_desc,msg_id,create_time) VALUES(:order_no,:order_type,:customer_id,:warehouse_code,:return_status,:return_code,:return_desc,:msg_id,now())";
    		$model = $db->createCommand($sql);
    		foreach ($logArr as $val)
    		{
    			$values = array();
    			$values[':order_no'] = $val['order_no'];
    			$values[':order_type'] = $val['order_type'];
    			$values[':customer_id'] = $val['customer_id'];
    			$values[':warehouse_code'] = $val['warehouse_code'];
    			$values[':return_status'] = $val['return_status'];
    			$values[':return_code'] = $val['return_code'];
    			$values[':return_desc'] = $val['return_desc'];
    			$values[':msg_id'] = $val['msg_id'];
    			$model->bindValues($values);
    			$model->execute();
    		}
    	}
    }
}