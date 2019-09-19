<?php
/**
 * 出库单创建类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsStockOutCreate extends wmsRequest
{
	/**
	 * 创建出库单(普通出库单（退仓）,调拨出库,B2B出库,其他出库)
	 * @param $params
	 * @return array
	 */
	public function create($params)
    {   
    	if (empty($params)) {
    	    return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
    	} else {
    	    //推送给wms
    		$response = $this->send();
    		//解析返回的数据
    		if (!empty($response)) {
    		    if ($response['flag'] == 'success') {
    		    	//判断订单信息是否存在
    		        $checkRes = $this->check_outbound($params['deliveryOrder']);
    		        if (empty($checkRes)) {
    		        	//写入出库单数据
    		        	$this->insert_outbound($params);
    		        } else {
    		        	if ($checkRes['finish_flag'] == 1) {
    		        		//更新原出库单有效性
    		        		$this->update_outbound($checkRes['order_id']);
    		        		//写入新的出库单数据
    		        		$this->insert_outbound($params);
    		        	} else {
    		        		//追加出库单明细信息
    		        		$this->insert_outbound($params, $checkRes);
    		        	}
    		        }
    		        		           
		            return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
    		    } else {
    		        return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
    		    }
    		} else {
    			return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
    		}
    	}
    	
    } 

    /**
     * 校验订单号是否存在
     */
    public function check_outbound($orderData)
    {
    	$db = Yii::app()->db;
    	$sql = 'SELECT order_id,total_order_lines,finish_flag FROM t_outbound_info WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
    	$model = $db->createCommand($sql);
    	$model->bindValue(':order_no', $orderData['deliveryOrderCode']);
    	$model->bindValue(':order_type', $orderData['orderType']);
    	$model->bindValue(':customer_id', qimen_service::$_customerId);
    	$model->bindValue(':warehouse_code', $orderData['warehouseCode']);
    	$rs = $model->queryRow();
    	return $rs;
    }
    
    /**
     * 更新出库单信息
     * @param $orderId
     */
    public function update_outbound($orderId)
    {
    	$db = Yii::app()->db;  	
    	//更新出库单有效性
    	$sql = 'UPDATE t_outbound_info SET is_valid=0 WHERE order_id=:order_id';
    	$model = $db->createCommand($sql);
    	$model->bindValue(':order_id', $orderId);
    	$model->execute();
    	//更新出库单明细有效性
    	$sql = 'UPDATE t_outbound_detail SET is_valid=0 WHERE order_id=:order_id';
    	$model = $db->createCommand($sql);
    	$model->bindValue(':order_id', $orderId);
    	$model->execute();
    	return true;
    }
    
    /**
     * 写入订单数据到数据库
     * @param $orderData
     * @param $params
     */
    public function insert_outbound($orderData, $params = Array())
    {
        $deliveryOrder = $orderData['deliveryOrder'];
        $picker = $deliveryOrder['pickerInfo'];
        $sender = $deliveryOrder['senderInfo'];
        $receiver = $deliveryOrder['receiverInfo'];
        $orderLines = $orderData['orderLines']['orderLine'];
        
    	//获取出库单单头基础信息接口参数与数据库字段对应关系
    	$column_orderInfo_arr = $this->get_dataBase_relation('stock_out_create');
    	$column_key_orderInfo = implode(',', array_values($column_orderInfo_arr)) . ',';  
    	//picker 
    	$column_orderInfo_picker_arr = $this->get_dataBase_relation('stock_out_picker_create');
    	$column_key_picker_orderInfo = implode(',', array_values($column_orderInfo_picker_arr)) . ',';
    	//sender
    	$column_orderInfo_sender_arr = $this->get_dataBase_relation('stock_out_sender_create');
    	$column_key_sender_orderInfo = implode(',', array_values($column_orderInfo_sender_arr)) . ',';
    	//receiver
    	$column_orderInfo_receiver_arr = $this->get_dataBase_relation('stock_out_receiver_create');
    	$column_key_receiver_orderInfo = implode(',', array_values($column_orderInfo_receiver_arr)) . ',customer_id,create_time';
    	
    	//获取出库单明细基础信息接口参数与数据库字段对应关系
    	$column_orderDetail_arr = $this->get_dataBase_relation('stock_out_detail');
    	$column_key_orderDetail = implode(',', array_values($column_orderDetail_arr)) . ',order_id,create_time';  

    	$db = Yii::app()->db;
    	if (empty($params)) {    	
	    	//写入出库单单头信息   	
		    $column_value_orderInfo = ":" . implode(",:", array_values($column_orderInfo_arr)) . ",";
		    $column_value_picker_orderInfo = ":" . implode(",:", array_values($column_orderInfo_picker_arr)) . ",";
		    $column_value_sender_orderInfo = ":" . implode(",:", array_values($column_orderInfo_sender_arr)) . ",";
		    $column_value_receiver_orderInfo = ":" . implode(",:", array_values($column_orderInfo_receiver_arr)) . ",:customer_id,now()";
			$sql_orderInfo = 'INSERT INTO t_outbound_info(' . $column_key_orderInfo . $column_key_picker_orderInfo . $column_key_sender_orderInfo . $column_key_receiver_orderInfo . ') VALUES( ' . $column_value_orderInfo . $column_value_picker_orderInfo . $column_value_sender_orderInfo . $column_value_receiver_orderInfo . ')';
			
			$model = $db->createCommand($sql_orderInfo);
			$values = array();			
			foreach ($column_orderInfo_arr as $k => $v)
			{
				$values[':'.$v] = empty($deliveryOrder[$k]) ? '' : $deliveryOrder[$k];
			}
			foreach ($column_orderInfo_picker_arr as $k2 => $v2)
			{
			    $values[':'.$v2] = empty($picker[$k2]) ? '' : $picker[$k2];
			}
			foreach ($column_orderInfo_sender_arr as $k3 => $v3)
			{
			    $values[':'.$v3] = empty($sender[$k3]) ? '' : $sender[$k3];
			}
			foreach ($column_orderInfo_receiver_arr as $k4 => $v4)
			{
			    $values[':'.$v4] = empty($receiver[$k4]) ? '' : $receiver[$k4];
			}
			$values[':customer_id'] = qimen_service::$_customerId;
			$model->bindValues($values);
			$model->execute();
			$order_id = $db->getLastInsertID();
			
			//写入出单单明细信息
			$column_value_orderDetail = ":" . implode(",:", array_keys($column_orderDetail_arr)) . ",'{$order_id}',now()";
			$sql_orderDetail = "INSERT IGNORE INTO t_outbound_detail({$column_key_orderDetail}) VALUES({$column_value_orderDetail})";
			if (!empty($orderLines)) {
				if (empty($orderLines[0])) {
					$orderLines = array($orderLines);
				}
				$model = $db->createCommand($sql_orderDetail);
				$outBizCodeArr = array();   //定义外部业务编码数组，用于分批次推送时去重
				foreach ($orderLines as $a => $b)
				{
					if (in_array($b['outBizCode'], $outBizCodeArr) && !empty($b['outBizCode'])) {
						continue;
					}
					$values = array();
					foreach ($column_orderDetail_arr as $k => $v)
					{
						$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
					}
					$model->bindValues($values);
					$model->execute();
					if ($deliveryOrder['totalOrderLines'] > count($orderLines)) {
						$outBizCodeArr[$a] = $b['outBizCode'];
					}				
				}
				if (!empty($outBizCodeArr)) {
					//更新出库单完成标志为否
					$sql_update = 'UPDATE t_outbound_info SET finish_flag=0 WHERE order_id=:order_id';
					$model = $db->createCommand($sql_update);
					$model->bindValue(':order_id', $order_id);
					$model->execute();
				}
			}
    	} else {
    		//查找原出库单中的明细数量
    		$sql_linetotal = 'SELECT COUNT(*) AS num FROM t_outbound_detail WHERE order_id=:order_id AND is_valid=1';
    		$model = $db->createCommand($sql_linetotal);
    		$model->bindValue(':order_id', $params['order_id']);
    		$rs = $model->queryRow();
    		$lineNum = $rs['num'];
    		//写入出单单明细数据
    		$column_value_orderDetail = ":" . implode(",:", array_keys($column_orderDetail_arr)) . ",'{$params['order_id']}',now()";
    		$sql_orderDetail = "INSERT IGNORE INTO t_outbound_detail({$column_key_orderDetail}) VALUES({$column_value_orderDetail})";
    		if (!empty($orderLines)) {
    			if (empty($orderLines[0])) {
    				$orderLines = array($orderLines);
    			}   			
    			$outBizCodeArr = array();   //定义外部业务编码数组，用于分批次推送时去重
    			$num = 0;
    			foreach ($orderLines as $a => $b)
    			{
    				if (in_array($b['outBizCode'], $outBizCodeArr) && !empty($b['outBizCode'])) {
    					continue;
    				} 
    				//校验外部业务编码是否和原来的明细信息中数据重复
    				$sql_outBizCode = 'SELECT * FROM t_outbound_detail WHERE order_id=:order_id AND out_biz_code=:out_biz_code AND is_valid=1';
    				$model = $db->createCommand($sql_outBizCode);
    				$model->bindValue(':order_id', $params['order_id']);
    				$model->bindValue(':out_biz_code', $b['outBizCode']);
    				$rs = $model->queryRow();
    				if (!empty($rs) && !empty($b['outBizCode'])) {
    					continue;
    				}
    				$values = array();
    				foreach ($column_orderDetail_arr as $k => $v)
    				{
    					$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
    				}
    				$model = $db->createCommand($sql_orderDetail);
    				$model->bindValues($values);
    				$model->execute();
    				$outBizCodeArr[$a] = $b['outBizCode'];
    				$num++;
    			}
    			if ($params['total_order_lines'] <= ($lineNum + $num)) {
    				//更新出库单接收完成状态
    				$sql_update = 'UPDATE t_outbound_info SET finish_flag=1 WHERE order_id=:order_id';
    				$model = $db->createCommand($sql_update);
    				$model->bindValue(':order_id', $params['order_id']);
    				$model->execute();
    			}
    		}
    	}
				
		return true;
    }
   
}

