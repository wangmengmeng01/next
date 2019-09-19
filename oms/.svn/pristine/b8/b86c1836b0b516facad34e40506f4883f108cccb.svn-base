<?php
/**
 * 入库单确认操作类
 * wms => oms => erp
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/meituan/erpRequest.php';
class erpEntryOrderConfirm extends erpRequest
{
	/**
	 * 入库单状态明细回传信息推送
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
		            $this->insert_inbound_record($params);
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
     * 把入库单回传信息写入数据库
     * @param array
     * @return 
     */
    public function insert_inbound_record($params)
    {
        if (!empty($params)) {
            global $db;
            $entryOrder = $params['entryOrder'];
            $orderLine = $params['orderLines']['orderLine'];
            if (!empty($entryOrder)) {
                //获取入库单状态回传表数据库字段对应关系
                $column_arr = $this->get_dataBase_relation('inbound_info_record');
                $column_key_str = implode(',', array_values($column_arr)) . ',order_id,create_time';
                //获取入库单明细回传表数据库字段对应关系
                $detail_record_column_arr = $this->get_dataBase_relation('inbound_detail_record');
                $detail_record_column_key_str = implode(',', array_values($detail_record_column_arr)) . ',order_id,record_id,create_time';
                //获取入库单回传明细记录批次表数据库字段对应关系
                $batch_detail_record_column_arr = $this->get_dataBase_relation('t_inbound_detail_batch_record');
                $batch_detail_record_column_key_str = implode(',', array_values($batch_detail_record_column_arr)) . ',detail_id,create_time';
               
                //获取入库单order_id
                //$sql = "SELECT order_id from t_inbound_info where order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
                $sql = "SELECT order_id from t_inbound_info where order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
                $model = $db->prepare($sql);
                $model->bindParam(':order_no', $entryOrder['entryOrderCode']);
                //$model->bindParam(':order_type', $entryOrder['entryOrderType']);
                $model->bindParam(':customer_id', $entryOrder['ownerCode']);
                $model->bindParam(':warehouse_code', $entryOrder['warehouseCode']);
                $model->execute();
                $rs = $model->fetch(PDO::FETCH_ASSOC);
                $orderId = $rs['order_id'];

                if ($orderId != '') {
                    //插入入库单状态回传记录
                    $column_value_str = ":" . implode(',:', array_values($column_arr)) . ",'{$orderId}',now()";
                    $sql = "INSERT INTO t_inbound_info_record({$column_key_str}) VALUES({$column_value_str})";
                    $model = $db->prepare($sql);
                    $values = array();
                    foreach ($column_arr as $k => $v)
                    {
                        $values[':'.$v] = empty($entryOrder[$k]) ? '' : $entryOrder[$k];
                    }
                    $model->execute($values);
                    $recordId = $db->lastInsertID();
                    //插入库单明细回传记录    
                    if(!empty($orderLine)){
                        $detail_record_column_value_str = ":" . implode(',:', array_values($detail_record_column_arr)) . ",'{$orderId}','{$recordId}',now()";
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
                            $values[':customer_id'] = $entryOrder['ownerCode'];
                            $sql = "INSERT INTO t_inbound_detail_record({$detail_record_column_key_str}) VALUES({$detail_record_column_value_str})";
                            $model = $db->prepare($sql);
                            $model->execute($i_values);
                            $detailId = $db->lastInsertID();
                            
                            $batch = $i_val['batchs']['batch'];
                            if (!empty($batch)) {
                                $batch_detail_record_column_value_str = ":" . implode(',:', array_values($batch_detail_record_column_arr)) . ",'{$detailId}',now()";
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
                    //更新入库单状态
                    $this->update_inbound_status($orderId, $entryOrder['status']);
                }
            }            
        }
    }
   
    
    /**
     * 更新入库单状态
     * @param $orderId
     * @param $status
     * @return
     */
    public function update_inbound_status($orderId, $status)
    {
    	global $db;
    	$sql = "UPDATE t_inbound_info SET order_status=:order_status WHERE order_id=:order_id";
    	$model = $db->prepare($sql);
    	$model->bindParam(':order_status', $status);
    	$model->bindParam(':order_id', $orderId);
    	$model->execute();
    }
}

