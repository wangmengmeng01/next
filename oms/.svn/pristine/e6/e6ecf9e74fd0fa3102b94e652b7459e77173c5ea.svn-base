<?php
/**
 * 跨境出库单业务处理类
 * @author Renee
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';
class wmsCrossBorderDelivery extends wmsRequest
{
	/**
	 * 创建出库单(销售出库、调拨出库,采购退货出库,盘亏出库)
	 * 销售出库单如果开启缓存机制则先存储到队列，然后异步推送给wms,其他出库单同步推送给wms
	 * @param $params
	 * @return array
	 */
	public function create($params)
    {      	
    	if (!empty($params)) {
    		global $db;
    		//判断数据中订单数据是否多条
    		$utilObj = new util();
    		$multiFlag = $utilObj->isArrayMulti($params);
    		if ($multiFlag) {
    			$headers = $params['header'];
    		} else {
    			$headers = array($params['header']);
    		}
    		
    		$saleArr = array();
    		$otherArr = array();

    		foreach ($headers as $k => $v)
    		{  	
    			if (ORDER_DELIVERY_REDIS_FLAG) {		
	    			//销售出库单使用缓存机制
	    			if ($v['OrderType'] == 'SO') {
	    				$saleArr[$k] = $v;
	    				unset($headers[$k]);
	    			} else {   				
	    				$otherArr[$k] = $v;
	    			}
    			} else {
    				$otherArr[$k] = $v;
    			}
    		}

    		//订单类型为销售出库的出库单如果开启了缓存机制则写入redis,然后由后台脚本推送wms
    		if (!empty($saleArr) && ORDER_DELIVERY_REDIS_FLAG) {
    			$queueData = array(
    					'customer_id' => service::$_customerId,
    					'method' => service::$_method,
    					'method_to' => service::$_methodTo,
    					'data' => json_encode($saleArr),
    					'messageid' => service::$_messageId,
    					'timestamp' => service::$_timeStamp
    			);
    			
    			try{
    				$redisObj = new func();
    				global $redisApiParam;
    				$taskName = $redisApiParam['queue_name'].'_'.service::$_customerId;
    				//校验队列是否启动正常
    				$ping = $redisObj->pingQueue();
    				if ($ping == '+PONG') {
    					//入队列
    					$redisObj->pushQueue($taskName, $queueData);
    					//写入数据库
    					if (!empty($saleArr)) {
							foreach ($saleArr as $key => $val)
							{
								//校验订单号是否存在
								$sql = 'SELECT order_id FROM t_outbound_info WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
								$model = $db->prepare($sql);
								$model->bindParam(':order_no', $val['OrderNo']);
								$model->bindParam(':order_type', $val['OrderType']);
								$model->bindParam(':customer_id', $val['CustomerID']);
								$model->bindParam(':warehouse_code', $val['WarehouseID']);
								$model->execute();
								$rs = $model->fetch(PDO::FETCH_ASSOC);
								if (empty($rs)) {
									//写入新的订单数据
									$this->insert_outbound($val, 'SO');
								} else {
								    echo '999';die;
									//更新原订单状态为无效
									$this->update_outbound($rs['order_id']);
									//写入新的订单数据
									$this->insert_outbound($val, 'SO');
								}
							}
    					}
    				} else {
    					msg::$err_arr = $this->merge_error_data($saleArr, msg::$err_arr);
    				}	
    			} catch (Exception $e){
    				$serviceObj = new service();
    				$serviceObj::$_finish = true;

    				$mergeArr = $this->merge_error_data($saleArr, $otherArr);
                    foreach ($mergeArr as $k =>$v)
                    {
                    	$mergeArr[$k]['errorcode'] = 'S002';
                    	$mergeArr[$k]['errordescr'] = '队列服务异常';
                    }
    				$errorDataArr = $this->merge_error_data($mergeArr, msg::$err_arr);
    				$xmlData = $this->msgObj->get_error_str($errorDataArr);
    				return $this->msgObj->output(0, 'fail', '0001', $xmlData);
    			}   			
    		}   	
    			
    		if (!empty($saleArr) && empty($otherArr)) {
    			return $this->msgObj->output(1, 'ok', '0000', '');
    		}
    		//其他类型的出库单转发数据给wms
    		$params = array('header' => $headers);    		    		
    		$response = $this->send(service::$_methodTo, $params);

    		//解析返回的数据
    		if (!empty($response)) {
    			//获取错误数据
    			if ($response['returnFlag'] != 1) {
    				$error_info_arr = $this->merge_error_data($response['resultInfo'], msg::$err_arr);
    			} else {
    				$error_info_arr = $this->merge_error_data('', msg::$err_arr);
    			}
    			//组合失败数据成xml格式
    			$xmlData = $this->msgObj->get_error_str($error_info_arr);   			
    			if ($response['returnFlag'] == 0) {
    				return $this->msgObj->output(0, $response['returnDesc'], '0001', $xmlData, $response['addon']);    				    				
    			} else { 
    				if ($response['returnFlag'] == 1 || $response['returnFlag'] == 2) {
    					//获取操作成功的数据
    					if (!empty($response['resultInfo'])) {
    						foreach ($response['resultInfo'] as $k => $v)
    						{
    							foreach ($otherArr as $key => $val)
    							{
    								if ($v['OrderType'] == $val['OrderType'] && $v['OrderNo'] == $val['OrderNo'] && $v['CustomerID'] == $val['CustomerID']) {
    									unset($otherArr[$key]);
    								}
    							}
    						}
    					}
    					
    					//写入数据库
    					if (!empty($otherArr)) {
                            foreach ($otherArr as $key => $val)
                            {  							 	
                            	  //校验订单号是否存在
                            	  $sql = 'SELECT order_id FROM t_outbound_info WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
                            	  $model = $db->prepare($sql);
                            	  $model->bindParam(':order_no', $val['OrderNo']);
                            	  $model->bindParam(':order_type', $val['OrderType']);
                            	  $model->bindParam(':customer_id', $val['CustomerID']);
                            	  $model->bindParam(':warehouse_code', $val['WarehouseID']);
                            	  $model->execute();
                            	  $rs = $model->fetch(PDO::FETCH_ASSOC);
                            	  if (empty($rs)) {
                            	  	   //写入新的订单数据
                             	   $this->insert_outbound($val);
                            	  } else {
                            	  	   //更新原订单状态为无效
                            	  	   $this->update_outbound($rs['order_id'],$val);
                            	  	   //写入新的订单数据
                            	  	   $this->insert_outbound($val);
                            	  }   							 	      							 	  
                            }
    					}
    				}   				
    			}
    			if ($response['returnFlag'] == 1) {
    				if (empty($error_info_arr)) {
    					return $this->msgObj->output(1, 'ok', '0000', $xmlData, $response['addon']);
    				} else {
    					return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon']);
    				}
    			} elseif ($response['returnFlag'] == 2) {
    				return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon']);
    			}
    		} else {
    			//获取错误数据
    			$error_info_arr = $this->merge_error_data($params['header'], msg::$err_arr);
    			//组合失败数据成xml格式
    			$xmlData = $this->msgObj->get_error_str($error_info_arr);
    			return $this->msgObj->output(0, 'fail', 'S007', $xmlData, $response['addon']);
    		}  		    		
    	} else {
    		if (!empty(msg::$err_arr)){
    			$xml = new xml();
    			$xmlData = '';
    			msg::$err_arr = $this->merge_error_data(msg::$err_arr);
    			foreach (msg::$err_arr as $key => $val)
    			{
    				$xmlData .= $xml->array2xml(array('result' => $val));
    			}
    		}
    		return $this->msgObj->output(0, 'fail', '0001', $xmlData);
    	}
    } 
    
    /**
     * 解析返回的错误数据，并和之前的错误数据进行组合，并且返回标准的错误参数
     * @param $error_info  wms接口返回的resultInfo错误信息
     * @param $error_arr   msg类中存储的错误信息
     * @return array
     */
    public function merge_error_data($error_info, $error_arr=array())
    {
    	$return_arr = array();
    	$resultInfoStr = service::$_methodErrorInfo[service::$_method];
    	$resultInfoArr = explode(",", $resultInfoStr);
    	$i=0;
    	if (!empty($error_info)) {
    		foreach ($error_info as $v)
    		{
    		    foreach ($resultInfoArr as $a)
	    		{	    				
	    			$return_arr[$i][$a] = $v[$a];       			
	    		}    		
	    		$i++;
    		}
    	}
    	if (!empty($error_arr)) {
    		foreach ($error_arr as $val)
    		{
    			foreach ($resultInfoArr as $a)
    			{
    				$return_arr[$i][$a] = $val[$a];    				
    			}
    			$i++;
    		}
    	}
    	return $return_arr;
    }
    
    /**
     * 更新跨境出库单信息
     * @param $orderId
     */
    public function update_outbound($orderId, $orderInfo)
    {
    	global $db;//首先找出package_id,再通过package_id设置detail表的有效性
    	
	    $sql = "SELECT package_id FROM t_outbound_package WHERE order_id='$orderId'";
	    $model = $db->prepare($sql);
	    $model->execute();
	    $packageIdArr = $model->fetch(PDO::FETCH_ASSOC);
	    
	    if (empty($packageIdArr[0])) {
	        $packageIdArr = array($packageIdArr);
	    }
	    //更新原有订单包裹明细为无效
	    foreach ($packageIdArr as $packageId) {
	        $pSql = "UPDATE t_outbound_package_detail SET is_valid = 0 WHERE package_id = :package_id";
	        $pModel = $db->prepare($pSql);
	        $pModel->bindParam(':package_id', $packageId);
	        $pModel->execute();
	        if (!UPDATE_INVENTORY_FLAG) {
    	        $updateInvSql = "SELECT sku,qty_ordered FROM t_outbound_package_detail WHERE package_id = '$packageId'";
    	        $updateInvModel = $db->query($updateInvSql);
    	        $row = $updateInvModel->fetchAll(PDO::FETCH_ASSOC);
    	        if (empty($row)) {
    	            foreach ($row as $v) {
    	                //更新库存
    	                $this->update_outbound_inventory($orderInfo['CustomerID'], $orderInfo['WarehouseID'], $v['sku'], $v['qty_ordered']*-1);
    	            }
    	        }
	        }
	    }
    	
    	//把原有的订单更新为无效
    	$sql = 'UPDATE t_outbound_info SET is_valid=0 WHERE order_id=:order_id';
    	$model = $db->prepare($sql);
    	$model->bindParam(':order_id', $orderId);
    	$model->execute();
    	$sql = 'UPDATE t_outbound_package SET is_valid=0 WHERE order_id=:order_id';
    	$model = $db->prepare($sql);
    	$model->bindParam(':order_id', $orderId);
    	$model->execute();
    	$sql = 'UPDATE t_outbound_bill_info SET is_valid=0 WHERE order_id=:order_id';
    	$model = $db->prepare($sql);
    	$model->bindParam(':order_id', $orderId);
    	$model->execute();
    }
    
    /**
     * 写入订单数据到数据库
     * @param $val
     */
    public function insert_outbound($val, $order_type='')
    {
    	global $db;
    	//获取跨境出库单单头基础信息接口参数与数据库字段对应关系
    	$column_orderInfo_arr = $this->get_dataBase_relation('outbound_info');
    	if ($order_type == 'SO') {
    		$column_key_orderInfo = implode(',', array_values($column_orderInfo_arr)) . ',redis_flag,create_time';
    	} else {
    	    $column_key_orderInfo = implode(',', array_values($column_orderInfo_arr)) . ',create_time';   
    	}
    	//获取跨境出库单下发包裹信息
    	$column_packageInfo_arr = $this->get_dataBase_relation('crossborder_package_info');
        $column_key_packageInfo = implode(',', array_values($column_packageInfo_arr)) . ',order_id,create_time';
    	
    	//获取跨境出库单下发包裹商品明细信息
    	$column_package_itemInfo_arr = $this->get_dataBase_relation('crossborder_package_item_info');
    	$column_key_itemInfo = implode(',', array_values($column_package_itemInfo_arr)) . ',package_id,create_time';
    		    		
    	//获取跨境出单单发票信息接口参数与数据库字段对应关系
    	$column_orderBill_arr = $this->get_dataBase_relation('outbound_bill');
    	$column_key_orderBill = implode(',', array_values($column_orderBill_arr)) . ',order_id,create_time';
    	
    	//写入跨境出库单单头信息   	
    	if ($order_type == 'SO') {
    		$column_value_orderInfo = ":" . implode(",:", array_keys($column_orderInfo_arr)) . ",1,now()";
    	} else {
		    $column_value_orderInfo = ":" . implode(",:", array_keys($column_orderInfo_arr)) . ",now()";
    	}
		$sql_orderInfo = "INSERT INTO t_outbound_info({$column_key_orderInfo}) VALUES({$column_value_orderInfo})";
		$model = $db->prepare($sql_orderInfo);
		$values = array();
		
		foreach ($column_orderInfo_arr as $k => $v)
		{
			$values[':'.$k] = empty($val[$k]) ? '' : $val[$k];
		}
		$model->execute($values);
		$order_id = $db->lastInsertId();
		
		//写入跨境出库单包裹信息
		$column_value_packageInfo = ":" . implode(",:", array_keys($column_packageInfo_arr)) . ",'{$order_id}',now()";
		$packageInfoSql = "INSERT IGNORE INTO t_outbound_package({$column_key_packageInfo}) VALUES({$column_value_packageInfo})";
		if (!empty($val['package'])) {
		    if (empty($val['package'][0])) {
		        $val['package'] = array($val['package']);
		    }
		    $packageModel = $db->prepare($packageInfoSql);
		    foreach ($val['package'] as $p_v) {
		        $values = array();
		        foreach ($column_packageInfo_arr as $p_i_k => $p_i_v) {
		            $values[':' . $p_i_k] = empty($p_v[$p_i_k]) ? : $p_v[$p_i_k] ;
		        }
		        $packageModel->execute($values);
		        $packageId = $db->lastInsertId();
		        
		        if (!empty($p_v['detailsItem'])) {
		            if (empty($p_v['detailsItem'][0])) {
		                $p_v['detailsItem'] = array($p_v['detailsItem']);
		            }
		            $column_value_itemInfo = ":" . implode(",:", array_keys($column_package_itemInfo_arr)) . ",'{$packageId}',now()";
		            $packageItemInfoSql = "INSERT IGNORE INTO t_outbound_package_detail({$column_key_itemInfo}) VALUES({$column_value_itemInfo})";
		            $itemInfoModel = $db->prepare($packageItemInfoSql);
		            foreach ($p_v['detailsItem'] as $m_v) {
		                $values = array();
		                foreach ($column_package_itemInfo_arr as $m_i_k => $m_i_v) {
		                    $values[':' . $m_i_k] = empty($m_v[$m_i_k]) ? '' : $m_v[$m_i_k] ;
		                }
		                $itemInfoModel->execute($values);
    		            //更新库存
        				if (!UPDATE_INVENTORY_FLAG) {
        					$this->update_outbound_inventory($val['CustomerID'], $val['WarehouseID'], $m_v['SKU'], $m_v['QtyOrdered']);
        				}
		            }
		        }
		    }
		}
		
		//写入发票数据
		$column_value_orderBill = ":" . implode(",:", array_keys($column_orderBill_arr)) . ",'{$order_id}',now()";
		$sql_orderBill = "INSERT IGNORE INTO t_outbound_bill_info({$column_key_orderBill}) VALUES({$column_value_orderBill})";
		if (!empty($val['invoiceItem'])) {
			if (empty($val['invoiceItem'][0])) {
				$val['invoiceItem'] = array($val['invoiceItem']);
			}
			$model = $db->prepare($sql_orderBill);
			foreach ($val['invoiceItem'] as $b)
			{
				$values = array();
				foreach ($column_orderBill_arr as $k => $v)
				{
					$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
				}
				$model->execute($values);
			}
		} 
    }

    /**
     * 更新库存
     * @param string $customerId 
     * @param string $warehouseId 
     * @param string $sku  
     * @param int $num  
     * @return boolean
     */
    public function update_outbound_inventory($customerId, $warehouseId, $sku, $num)
    {
    	global $db;
    	$sql = "UPDATE t_product_inventory set qty=qty-:QtyOrdered,occupy_qty=occupy_qty+:QtyOrdered WHERE customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
		$model = $db->prepare($sql);
		$values = array(
				':QtyOrdered' => $num,
				':customer_id' => $customerId,
				':warehouse_code' => $warehouseId,
				':sku' => $sku
		);
		$model->execute($values);
    }
   
}

