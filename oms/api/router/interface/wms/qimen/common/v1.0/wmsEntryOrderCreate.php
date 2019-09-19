<?php
/**
 * 入库单创建操作类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';

class wmsEntryOrderCreate extends wmsRequest
{

    /**
     * 创建入库单
     * @param $params         
     * @return array
     */
    public function create($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            //转发数据给wms
			$response = $this->send();
            //解析返回的数据
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    //判断入库单是否存在
                    $checkRs = $this->check_inbound($params);
                    if (empty($checkRs)) {
                        $this->insert_entry_order($params);
                    } else {
                        if ($checkRs['finish_flag'] == 1) {
                            
                            //将入库单设为无效
                            $this->update_inbound($checkRs['order_id']);
                            //插入新的入库单
                            $this->insert_entry_order($params);
                        } else {
                            //追加入库单明细信息
                            $this->insert_entry_order($params,$checkRs);
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
     * 写入订单数据到数据库
     * @param 请求数据 $orderData
     * @param  $params
     */
    public function insert_entry_order($orderData, $params = Array()){
        $entryOrder = $orderData['entryOrder'];
        $senderInfo = $entryOrder['senderInfo'];
        $receiverInfo = $entryOrder['receiverInfo'];
        $orderLine = $orderData['orderLines']['orderLine'];
        
        // 获取入库单单头参数与数据库字段对应关系
        $column_arr1 = $this->get_dataBase_relation('entry_order_create');
        $title_key_str1 = implode(',', array_values($column_arr1)) . ',';
        
        $column_arr2 = $this->get_dataBase_relation('entry_order_sender_create');//发送人
        $title_key_str2 = implode(',', array_values($column_arr2)) . ',';
        
        $column_arr3 = $this->get_dataBase_relation('entry_order_receiver_create');//收货人
        $title_key_str3 = implode(',', array_values($column_arr3)) . ',create_time';
        
        // 获取入库单明细参数与数据库字段对应关系
        $column_detail_arr = $this->get_dataBase_relation('entry_order_create_detail');
        $detail_key_str = implode(',', array_values($column_detail_arr)) . ',order_id,create_time';
        
        $db = Yii::app()->db;
        if (empty($params)) {
            //写入入库单单头信息
            $title_value_str1 = ':' . implode(',:', array_values($column_arr1)) . ',';
            $title_value_str2 = ':' . implode(',:', array_values($column_arr2)) . ',';
            $title_value_str3 = ':' . implode(',:', array_values($column_arr3)) . ',now()';
            $sql_inbound = 'INSERT INTO t_inbound_info (' .$title_key_str1.$title_key_str2 . $title_key_str3 . ') VALUES (' . $title_value_str1 . $title_value_str2 . $title_value_str3 . ')';
            $model = $db->createCommand($sql_inbound);
            $values = array();
            foreach ($column_arr1 as $k => $v) {
                $values[':' . $v] = empty($entryOrder[$k]) ? '' : $entryOrder[$k] ;
            }
            foreach ($column_arr2 as $k1 => $v1) {
                $values[':' . $v1] = empty($senderInfo[$k1]) ? '' : $senderInfo[$k1] ;
            }
            foreach ($column_arr3 as $k2 => $v2) {
                $values[':' . $v2] = empty($receiverInfo[$k2]) ? '' : $receiverInfo[$k2] ;
            }
            $model->bindValues($values);
            $model->execute();
            $orderId = $db->getLastInsertID();
            
            $detail_value_str = ":" . implode(',:', array_values($column_detail_arr)) . ",$orderId,now()";
            $sql_detail =  "INSERT IGNORE INTO t_inbound_detail({$detail_key_str}) VALUES({$detail_value_str})";
            if (!empty($orderLine)) {
                if (empty($orderLine[0])) {
                    $orderLine = array($orderLine);
                }
                $model = $db->createCommand($sql_detail);
                $outBizCodeArr = array();   //定义外部业务编码数组，用于分批次推送时去重
                foreach ($orderLine as $a => $b) {
                    if (in_array($b['outBizCode'], $outBizCodeArr) && !empty($b['outBizCode'])) {
                        continue;
                    }
                    $values = array();
                    foreach ($column_detail_arr as $k => $v)
                    {
                        $values[':'.$v] = empty($b[$k]) ? '' : $b[$k];
                    }
                    $model->bindValues($values);
                    $model->execute();
                    if ($entryOrder['totalOrderLines'] > count($orderLine)) {
                        $outBizCodeArr[$a] = $b['outBizCode'];
                    }
                }
                if (!empty($outBizCodeArr)) {
                    //更新入库单完成标志为否
                    $sql_update = 'UPDATE t_inbound_info SET finish_flag=0 WHERE order_id=:order_id';
                    $model = $db->createCommand($sql_update);
                    $model->bindValue(':order_id', $orderId);
                    $model->execute();
                }
            } 
        } else {
            //查找原入库单中的明细数量
            $sql_linetotal = 'SELECT COUNT(*) AS num FROM t_inbound_detail WHERE order_id=:order_id AND is_valid=1';
            $model = $db->createCommand($sql_linetotal);
            $model->bindValue(':order_id', $params['order_id']);
            $rs = $model->queryRow();
            $lineNum = $rs['num'];
            
            //写入入库单明细数据
            $detail_value_str = ":" . implode(",:", array_keys($column_detail_arr)) . ",'{$params['order_id']}',now()";
            $sql_orderDetail = "INSERT IGNORE INTO t_inbound_detail({$detail_key_str}) VALUES({$detail_value_str})";
            if (!empty($orderLine)) {
                if (empty($orderLine[0])) {
                    $orderLine = array($orderLine);
                }
                $outBizCodeArr = array();   //定义外部业务编码数组，用于分批次推送时去重
                $num = 0;
                foreach ($orderLine as $a => $b)
                {
                    if (in_array($b['outBizCode'], $outBizCodeArr) && !empty($b['outBizCode'])) {
                        continue;
                    }
                    //校验外部业务编码是否和原来的明细信息中数据重复
                    $sql_outBizCode = 'SELECT * FROM t_inbound_detail WHERE order_id=:order_id AND out_biz_code=:out_biz_code AND is_valid=1';
                    $model = $db->createCommand($sql_outBizCode);
                    $model->bindValue(':order_id', $params['order_id']);
                    $model->bindValue(':out_biz_code', $b['outBizCode']);
                    $rs = $model->queryRow();
                    if (!empty($rs) && !empty($b['outBizCode'])) {
                        continue;
                    }
                    $values = array();
                    foreach ($column_detail_arr as $k => $v)
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
                    //更新入库单接收完成状态
                    $sql_update = 'UPDATE t_inbound_info SET finish_flag=1 WHERE order_id=:order_id';
                    $model = $db->createCommand($sql_update);
                    $model->bindValue(':order_id', $params['order_id']);
                    $model->execute();
                }
            }
        }
        
        
    }

    /**
     * 判断是否存在入库单号
     * @param $orderNo 订单号         
     * @param $orderType 订单类型 
     * @param $customerId 货主id
     * @param $warehouseCode  仓库id   
     * @return bool
     */
    public function check_inbound($params)
    {
        $db = Yii::app()->db;
        $sql = 'SELECT order_id,total_order_lines,finish_flag FROM `t_inbound_info` WHERE order_no = :order_no AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1;';
        $model = $db->createCommand($sql);
        $values = array();
        $values[':order_no'] = $params['entryOrder']['entryOrderCode'];
        $values[':order_type'] = $params['entryOrder']['orderType'];
        $values[':customer_id'] = $params['entryOrder']['ownerCode'];
        $values[':warehouse_code'] = $params['entryOrder']['warehouseCode'];
        $model->bindValues($values);
        $rs = $model->queryRow();
        return $rs;
    }
    
    /**
     * 更新入库单有效性
     * @param 订单id $orderId
     * @return boolean
     */
    public function update_inbound($orderId) {
        $db = Yii::app()->db;
        //更新入库单有效性
        $sql = 'UPDATE t_inbound_info SET is_valid=0 WHERE order_id=:order_id';
        $model = $db->createCommand($sql);
        $model->bindValue(':order_id', $orderId);
        $model->execute();
        //更新出库单明细有效性
        $sql = 'UPDATE t_inbound_detail SET is_valid=0 WHERE order_id=:order_id';
        $model = $db->createCommand($sql);
        $model->bindValue(':order_id', $orderId);
        $model->execute();
        return true;
    }
}

