<?php
/**
 * 入库单取消操作类
 * ERP => OMS => WMS
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';

class wmsCancelAsn extends wmsRequest
{

    /**
     * 创建出库单取消订单，并推送WMS取消订单
     * @param $params
     * @return array
     */
    public function create($params)
    {
        if (! empty($params)) {
            // 转发数据给WMS
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
                        // 获取操作成功的仓库ID数据
                        if (! empty($response['resultInfo'])) {
                            foreach ($response['resultInfo'] as $k => $v) {
                                foreach ($params['header'] as $key => $val) {
                                    if ($v['CustomerID'] == $val['CustomerID'] && $v['OrderNo'] == $val['OrderNo'] && $v['OrderType'] == $val['OrderType'] && $v['WarehouseID'] == $val['WarehouseID']) {
                                        unset($params['header'][$key]);
                                    }
                                }
                            }
                        }
                        // 写入数据库
                        if (! empty($params['header'])) {
                        	global $db;
                            $utilObj = new util();
                            $multiFlag = $utilObj->isArrayMulti($params);
                            if ($multiFlag) {
                            	$headers = $params['header'];
                            } else {
                            	$headers = array($params['header']);
                            }
 
                            if (! empty($headers)) {                           	
                                // 获取入库单取消参数与数据库字段对应关系
                                $column_arr = $this->get_dataBase_relation('inbound_cancel');
                                $column_key_str = implode(',', array_values($column_arr)) . ',order_id,create_time';                                
                                foreach ($headers as $key => $val) {
                                	//获取取消的入库单order_id
                                	$sql = "SELECT order_id from t_inbound_info where order_no=:order_no AND order_type = :order_type AND customer_id =:customer_id AND warehouse_code=:warehouse_code  AND is_valid = 1";
                                	$model = $db->prepare($sql);
                                	$model->bindParam(':order_no', $val['OrderNo']);
                                	$model->bindParam(':order_type', $val['OrderType']);
                                	$model->bindParam(':customer_id', $val['CustomerID']);
                                	$model->bindParam(':warehouse_code', $val['WarehouseID']);
                                	$model->execute();
                                	$rs = $model->fetch(PDO::FETCH_ASSOC);
                                	$orderId = $rs['order_id'];
                                	
                                	//写入入库单取消数据
                                	$column_value_str = ":" . implode(",:", array_values($column_arr)) . ",{$orderId},now()";
                                	$sql = "INSERT INTO t_inbound_cancel({$column_key_str}) VALUES({$column_value_str})";
                                    $model = $db->prepare($sql);
                                    $values = array();
                                    foreach ($column_arr as $k => $v) {
                                        $values[':' . $v] = empty($val[$k]) ? '' : $val[$k];
                                    }
                                    $model->execute($values);
                                    
                                    //更新入库单的状态
                                    $sql = "UPDATE t_inbound_info SET order_status='90' where order_id='$orderId'";
                                    $model = $db->exec($sql);                                    
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

