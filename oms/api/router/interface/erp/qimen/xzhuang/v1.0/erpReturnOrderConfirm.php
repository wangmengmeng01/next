<?php
/**
 * 退货入库单确认操作类
 * wms => oms => erp
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/xzhuang/erpRequest.php';
class erpReturnOrderConfirm extends erpRequest
{
	/**
	 * 退货入库单状态明细回传信息推送
	 * @param $params
	 * @return array
	 */
	public function confirm($params)
    {   	  
    	if (empty($params)) {
    	    return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
    	} else {
    	    //转发数据给erp
    		$response = $this->send();
    		//解析返回的数据
    		if (!empty($response)) {
    		    if ($response['flag'] == 'success') {
		            $this->insert_return_order_record($params);
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
     * 把退货入库单回传信息写入数据库
     * @param array
     * @return 
     */
    public function insert_return_order_record($params)
    {
        if (!empty($params)) {
            global $db;
            $returnOrder = $params['returnOrder'];
            $sender = $returnOrder['senderInfo'];
            $orderLine = $params['orderLines']['orderLine'];
            if (!empty($returnOrder)) {
                //获取退货入库单状态回传表数据库字段对应关系
                $column_arr = $this->get_dataBase_relation('return_order_inbound_info_record');
                $column_key_str = implode(',', array_values($column_arr)) . ',';
                $column_sender_arr = $this->get_dataBase_relation('return_order_sender_record');
                $column_sender_key_str = implode(',', array_values($column_sender_arr)) . ',order_id,create_time';
                //获取退货入库单明细回传表数据库字段对应关系
                $detail_record_column_arr = $this->get_dataBase_relation('return_order_inbound_detail_record');
                $detail_record_column_key_str = implode(',', array_values($detail_record_column_arr)) . ',order_id,create_time';
                //获取退货入库单回传明细记录批次表数据库字段对应关系
                $batch_detail_record_column_arr = $this->get_dataBase_relation('t_inbound_detail_batch_record');
                $batch_detail_record_column_key_str = implode(',', array_values($batch_detail_record_column_arr)) . ',detail_id,create_time';
               
                $customerId = qimen_service::$_customerId;
                //获取退货入库单表order_id
                $sql = "SELECT order_id from t_inbound_info where order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
                $model = $db->prepare($sql);
                $model->bindParam(':order_no', $returnOrder['returnOrderCode']);
                //$model->bindParam(':order_type', $returnOrder['orderType']);
                $model->bindParam(':customer_id', $customerId);
                $model->bindParam(':warehouse_code', $returnOrder['warehouseCode']);
                $model->execute();
                $rs = $model->fetch(PDO::FETCH_ASSOC);
                $orderId = $rs['order_id'];
                
                if ($orderId != '') {
                    //插入退货入库单状态回传记录
                    $column_value_str = ':' . implode(',:', array_values($column_arr)) . ',';
                    $column_sender_value_str = ':' . implode(',:', array_values($column_sender_arr)) . ",'{$orderId}',now()";
                    $sql = "INSERT INTO t_inbound_info_record(" . $column_key_str . $column_sender_key_str . ") VALUES(" . $column_value_str . $column_sender_value_str . ")";
                    $model = $db->prepare($sql);
                    $values = array();
                    
                    foreach ($column_sender_arr as $k1 => $v1) {
                        $values[':'.$v1] = empty($sender[$k1]) ? '' : $sender[$k1];
                    }
                    foreach ($column_arr as $k => $v) {
                        $values[':'.$v] = empty($returnOrder[$k]) ? '' : $returnOrder[$k];
                        
                    }
                    $model->execute($values);
                    
                    //插入库单明细回传记录
                    if(!empty($orderLine)){
                        $detail_record_column_value_str = ':' . implode(',:', array_values($detail_record_column_arr)) . ",'{$orderId}',now()";
                        if (empty($orderLine[0])) {
                            $orderLine = array($orderLine);
                        }
                        foreach ($orderLine as $i_val){
                            $i_values = array();
                            foreach ($detail_record_column_arr as $i_k => $i_v)
                            {
                                if ($i_k == 'inventoryType') {
                                    $i_values[':'.$i_v] = empty($i_val[$i_k]) ? 'ZP' : $i_val[$i_k];
                                } else {
                                    $i_values[':'.$i_v] = empty($i_val[$i_k]) ? '' : $i_val[$i_k];
                                }
                            }
                            $i_values['customer_id'] = $customerId;
                            $sql = "INSERT INTO t_inbound_detail_record({$detail_record_column_key_str}) VALUES({$detail_record_column_value_str})";
                            $model = $db->prepare($sql);
                            $model->execute($i_values);
                            $detailId = $db->lastInsertID();
                            
                            $batch = $i_val['batchs']['batch'];
                            if (!empty($batch)) {
                                $batch_detail_record_column_value_str = ':' . implode(',:', array_values($batch_detail_record_column_arr)) . ",'{$detailId}',now()";
                                $i_b_values = array();
                                if (empty($batch[0])) {
                                    $batch = array($batch);
                                }
                                foreach ($batch as $i_b_v) {
                                    foreach ($batch_detail_record_column_arr as $k_b => $v_b) {
                                        $i_b_values[':'.$v_b] = empty($i_b_v[$k_b]) ? '' : $i_b_v[$k_b];
                                    }
                                    $sql = "INSERT INTO t_inbound_detail_batch_record({$batch_detail_record_column_key_str}) VALUES({$batch_detail_record_column_value_str})";
                                    $model = $db->prepare($sql);
                                    $model->execute($i_b_values);
                                }
                            }
                        }
                    }
                }
            }            
        }
    }
}

