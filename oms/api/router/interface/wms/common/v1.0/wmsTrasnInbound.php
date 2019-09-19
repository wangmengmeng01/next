<?php
/**
 * 入库单下发操作类
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';

class wmsTrasnInbound extends wmsRequest
{

    /**
     * 创建入库单
     * @param $params         
     * @return array
     */
    public function create($params)
    {
        if (! empty($params)) {
            // 转发数据给wms
            $response = $this->send(service::$_methodTo, $params);
            // 解析返回的数据
            if (! empty($response)) {
                // 获取错误数据
                if ($response['returnFlag'] != 1) {
                	$error_info_arr = $this->merge_error_data($response['resultInfo'], msg::$err_arr);
                } else {
                	$error_info_arr = $this->merge_error_data('', msg::$err_arr);
                }
                // 组合失败数据成xml格式
                $xmlData = $this->msgObj->get_error_str($error_info_arr);
                if ($response['returnFlag'] == 0) {              	
                    return $this->msgObj->output(0, $response['returnDesc'], '0001', $xmlData, $response['addon']);
                } else {
                    if ($response['returnFlag'] == 1 || $response['returnFlag'] == 2) {
                        // 获取操作成功的订单数据
                        if (! empty($response['resultInfo'])) {
                            foreach ($response['resultInfo'] as $k => $v) {
                                foreach ($params['header'] as $key => $val) {
                                    if ($v['CustomerID'] == $val['CustomerID'] && $v['OrderType'] == $val['OrderType'] && $v['OrderNo'] == $val['OrderNo'] && $v['WarehouseID'] == $val['WarehouseID']) {
                                        unset($params['header'][$key]);
                                    }
                                }
                            }
                        }
                        // 写入数据库
                        if (! empty($params['header'])) {
                        	global $db;
                            $multiFlag = $this->utilObj->isArrayMulti($params);
                            if ($multiFlag) {
                            	$headers = $params['header'];
                            } else {
                            	$headers = array($params['header']);
                            }
                            
                            if (! empty($headers)) {
                            	// 获取入库单单头参数与数据库字段对应关系
                            	$column_arr = $this->get_dataBase_relation('inbound_info');
                            	$title_key_str = implode(',', array_values($column_arr)) . ',create_time';
                            	$title_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
                            	// 获取入库单明细参数与数据库字段对应关系
                            	$column_detail_arr = $this->get_dataBase_relation('inbound_detail');
                            	$detail_key_str = implode(',', array_values($column_detail_arr)) . ',order_id,create_time';
                                foreach ($headers as $key => $val) {
                                    // 判断是否存在：1、存在，逻辑删除 2、不存在 写入
                                    $this->has_upd_inbound($val['OrderNo'], $val['OrderType'], $val['CustomerID'], $val['WarehouseID']);
                                    //写入入库单单头信息                                 
                                    $sql = "INSERT INTO t_inbound_info({$title_key_str}) VALUE({$title_value_str})";
                                    $model = $db->prepare($sql);                                  
                                    $values = array();
                                    foreach ($column_arr as $k => $v) {
                                    	$values[':' . $v] = empty($val[$k]) ? '' : $val[$k];
                                    }
                                    $model->execute($values);
                                    // 写入入库单明细
                                    if (! empty($val['detailsItem'])) {                                    	
                                    	$detail_value_str = ":" . implode(',:', array_values($column_detail_arr)) . ",'{$db->lastInsertId()}',now()";
                                    	$sql = "INSERT INTO t_inbound_detail({$detail_key_str}) VALUES({$detail_value_str})";
                                    	$model = $db->prepare($sql);                                    	
                                    	foreach ($val['detailsItem'] as $val)
                                    	{
                                    		$values = array();
                                    		foreach ($column_detail_arr as $k => $v) {
                                    			$values[':' . $v] = empty($val[$k]) ? '' : $val[$k];
                                    		}
                                    		$model->execute($values);
                                    	}
                                    }
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
                // 获取错误数据
                $error_info_arr = $this->merge_error_data($params['header'], msg::$err_arr);
                // 组合失败数据成xml格式
                $xmlData = $this->msgObj->get_error_str($error_info_arr);
                return $this->msgObj->output(0, 'fail', 'S007', $xmlData, $response['addon']);
            }
        } else {
            if (! empty(msg::$err_arr)) {
                $xml = new xml();
                $xmlData = '';
                foreach (msg::$err_arr as $key => $val) {
                    $xmlData .= $xml->array2xml(array(
                        'result' => $val
                    ));
                }
            }
            return $this->msgObj->output(0, 'fail', '0001', $xmlData);
        }
    }

    /**
     * 判断是否存在入库单号，存在则逻辑删除处理
     * @param $orderNo 订单号         
     * @param $orderType 订单类型 
     * @param $customerId 货主id
     * @param $warehouseCode  仓库id   
     * @return bool
     */
    public function has_upd_inbound($orderNo, $orderType, $customerId, $warehouseCode)
    {
    	global $db;
        $values = array();
        $values[':order_no'] = $orderNo;
        $values[':order_type'] = $orderType;
        $values[':customer_id'] = $customerId;
        $values[':warehouse_code'] = $warehouseCode;
        $Ssql = 'SELECT COUNT(*) t, order_id FROM `t_inbound_info` WHERE order_no = :order_no AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1;';
        $datas = $db->prepare($Ssql);
        $datas->execute($values);
        $rs = $datas->fetch(PDO::FETCH_ASSOC);
        if ($rs['t'] > 0) {
            $Usql = ' UPDATE `t_inbound_info` SET is_valid = 0 WHERE order_no = :order_no AND order_type=:order_type AND customer_id = :customer_id AND warehouse_code=:warehouse_code; ';
            $Usql .= ' UPDATE `t_inbound_detail` SET is_valid = 0 WHERE order_id= :order_id; ';
            $values[':order_id'] = $rs['order_id'];
            $model = $db->prepare($Usql);
            if ($model->execute($values)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 解析返回的错误数据，并和之前的错误数据进行组合
     *
     * @param $error_info erp接口返回的resultInfo错误信息            
     * @param $error_arr msg类中存储的错误信息            
     * @return array
     */
    public function merge_error_data($error_info, $error_arr)
    {
        $return_arr = array();
        $i = 0;
        if (! empty($error_info)) {
            foreach ($error_info as $v) {
                $return_arr[$i] = $v;
                $i ++;
            }
        }
        if (! empty($error_arr)) {
            foreach ($error_arr as $val) {
                $return_arr[$i] = $val;
                $i ++;
            }
        }
        return $return_arr;
    }
}

