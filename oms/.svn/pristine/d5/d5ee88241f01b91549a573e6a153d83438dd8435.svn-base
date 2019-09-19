<?php
/**
 * 商品资料操作类
 * @author Jeremy
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';

class wmsProduct extends wmsRequest
{

    /**
     * 创建商品资料
     * @param  $params            
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
                        // 获取成功的数据
                        if (! empty($response['resultInfo'])) {
                            foreach ($response['resultInfo'] as $k => $v) {
                                foreach ($params['header'] as $key => $val) {
                                    if ($v['CustomerID'] == $val['CustomerID'] && $v['SKU'] == $val['SKU']) {
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
                                // 获取商品资料接口参数与数据库字段对应关系
                                $column_arr = $this->get_dataBase_relation('product');
                                $column_key_str = implode(',', array_values($column_arr)) . ',create_time';
                                $column_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
                                $sql = "INSERT INTO t_base_product({$column_key_str}) VALUES({$column_value_str})";
                                $model = $db->prepare($sql);
                                foreach ($headers as $key => $val) {
                                    // 判断是否存在：1、存在，逻辑删除 2、不存在 写入
                                    $this->has_insert_product($val['SKU'], $val['CustomerID']);
                                    $values = array();
                                    foreach ($column_arr as $k => $v) {
                                        $values[':' . $v] = empty($val[$k]) ? '' : $val[$k];
                                    }
                                    $model->execute($values);
                                }
                            }
                        }
                    }
                }
                if ($response['returnFlag'] == 1) {
                    if (empty($error_info_arr)) {
                        return $this->msgObj->output(1, 'ok', '0000', $xmlData, $response['addon'],$response['addon']['return_msg']);
                    } else {
                        return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon'],$response['addon']['return_msg']);
                    }
                } elseif ($response['returnFlag'] == 2) {
                    return $this->msgObj->output(2, '部分成功部分失败', '0001', $xmlData, $response['addon'],$response['addon']['return_msg']);
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
                foreach (msg::$err_arr as $key => $val) 
                {
                    $xmlData .= $xml->array2xml(array('result' => $val));
                }
            }
            return $this->msgObj->output(0, 'fail', '0001', $xmlData);
        }
    }

    /**
     * 存在的SKU，逻辑删除处理
     * @param $sku 产品编码            
     * @param $customer_id 客户ID            
     * @return bool
     */
    public function has_insert_product($sku, $customerId)
    {
    	global $db;
        $values = array();
        $values[':sku'] = $sku;
        $values[':customer_id'] = $customerId;
        $Ssql = 'INSERT IGNORE INTO t_base_product_log SELECT * FROM `t_base_product` WHERE sku = :sku AND customer_id=:customer_id AND is_valid = 1';
        $datas = $db->prepare($Ssql);
        $datas->execute($values);
		//删除无效数据
		$Dsql = "DELETE FROM t_base_product WHERE customer_id = :customer_id AND sku = :sku AND is_valid=1";
		$model = $db->prepare($Dsql);
		if ($model->execute($values)) {
			return true;
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

