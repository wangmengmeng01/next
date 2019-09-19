<?php

/**
 * 入库单下发过滤类
 *
 */
class filterTrasnInbound extends msg
{

    /**
     * 过滤入库单下发订单数据
     * @param  $requestData         
     * @return array
     *
     */
    public function create(&$requestData)
    {
        if (empty($requestData['header'])) {
        	//接口层记录日志
        	$logExt = array(
        			'api_url' => '',
        			'api_method' => service::$_method,
        			'api_params' => $requestData,
        			'return_msg' => 'filter: header不能为空'
        	);
            return $this->output(0, 'header不能为空', 'S003', '', $logExt);
        }
        
        $utilObj = new util();
        $multiFlag = $utilObj->isArrayMulti($requestData);
        if ($multiFlag) {
            $headers = $requestData['header'];
        } else {
            $headers = array(
                $requestData['header']
            );
        }

        $error_arr = array();
        $success_arr = array();
        $exists_arr = array();
        //$customerArr = array();
        $warehouseArr = array();
        $skuArr = array();
        global $db;
        foreach ($headers as $k => $v) {
        	// 校验货主ID
        	if (empty($v['CustomerID'])) {
        		$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不能为空', $v, $error_arr);
        		continue;
        	} else {
        		if ($v['CustomerID'] != service::$_customerId) {
        			$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID错误', $v, $error_arr);
        		}
        		/*
				if (!in_array($v['CustomerID'], $customerArr)) {									
					$sql = "SELECT customer_id FROM t_base_customer WHERE customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
					$model = $db->prepare($sql);
					$model->bindParam(':customer_id', $v['CustomerID']);
					$model->execute();
					$rs = $model->fetch(PDO::FETCH_ASSOC);
					if (empty($rs)) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不存在或者无效', $v, $error_arr);
						continue;
					} elseif ($rs['customer_id'] != $v['CustomerID']) {
						$error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID大小写错误', $v, $error_arr);
						continue;
					} else {
						$customerArr[$k] = $v['CustomerID'];
					}
				}
				*/
			}     	
        	// 校验所属仓库
        	if (empty($v['WarehouseID'])) {
        		$error_arr[$k] = $this->get_error_data($k, 'S003', '所属仓库不能为空', $v, $error_arr);
        		continue;
        	} else {
        		if (!in_array($v['WarehouseID'], $warehouseArr) && STRICT_VERIFY_FLAG) {
        			$sql = "SELECT warehouse_code FROM t_base_warehouse WHERE warehouse_code=:warehouse_code AND active_flag='Y' AND is_valid=1";
        			$model = $db->prepare($sql);
        			$model->bindParam(':warehouse_code', $v['WarehouseID']);
        			$model->execute();
        			$rs = $model->fetch(PDO::FETCH_ASSOC);
        			if (empty($rs)) {
        				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库编码不存在或者无效', $v, $error_arr);
        				continue;
        			} elseif ($rs['warehouse_code'] != $v['WarehouseID']) {
        				$error_arr[$k] = $this->get_error_data($k, 'S003', '仓库编码大小写错误', $v, $error_arr);
        				continue;
        			} else {
        				$warehouseArr[$k] = $v['WarehouseID'];
        			}
        		}
        	}
        	// 校验订单类型
        	if (empty($v['OrderType'])) {
        		$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型不能为空', $v, $error_arr);
        		continue;
        	} elseif (!in_array($v['OrderType'], array('PO', 'TR', 'RS', 'IP'))) {
				$error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型错误', $v, $error_arr);
				continue;
			} 
            // 校验订单号
            if (empty($v['OrderNo'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单号不能为空', $v, $error_arr);
                continue;
            } else {
                // 判断是否有重复数据
                if ($exists_arr[$v['WarehouseID']][$v['OrderType']][$v['OrderNo']] != '') {
                    $error_arr[$k] = $this->get_error_data($k, 'S006', '入库单单号'.$v['OrderNo'].'数据重复', $v, $error_arr);
                    continue;
                }
            }
            //校验订单下发方
            if (empty($v['UserDefine4'])) {
            	$error_arr[$k] = $this->get_error_data($k, 'S003', '订单下发方不能为空', $v, $error_arr);
            	continue;
            } elseif (!in_array($v['UserDefine4'], array('ERP', 'OMS', 'WMS'))) {
            	$error_arr[$k] = $this->get_error_data($k, 'S003', '订单下发方只能为ERP、OMS和WMS', $v, $error_arr);
            	continue;
            }              
            // 明细信息校验
            $m = 0;
            if (empty($v['detailsItem'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细不能为空', $v, $error_arr);
                continue;
            } else {
                if (empty($v['detailsItem'][0])) {
                    $v['detailsItem'] = array($v['detailsItem']);
                }
                foreach ($v['detailsItem'] as $val) {
                    if (empty($val['CustomerID'])) {
                        $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中货主ID不能为空', $v, $error_arr);
                        $m++;
                        break;
                    } else { // 验证单头中的货主ID和明细信息中的货主ID是否一致
                        if ($v['CustomerID'] != $val['CustomerID']) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中货主ID与单头中的货主ID不一致', $v, $error_arr);
                            $m++;
                            break;
                        }
                    }
                    if (empty($val['SKU'])) {
                        $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中SKU不能为空', $v, $error_arr);
                        $m++;
                        break;
                    } else {
                    	if (!in_array($val['SKU'], $skuArr) && STRICT_VERIFY_FLAG) {                    		                   	
	                    	$sql = "SELECT sku FROM t_base_product WHERE sku=:sku AND customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
	                    	$model = $db->prepare($sql);
	                    	$model->bindParam(':sku', $val['SKU']);
	                    	$model->bindParam(':customer_id', $val['CustomerID']);
	                    	$model->execute();
	                    	$rs = $model->fetch(PDO::FETCH_ASSOC);
	                    	if (empty($rs)) {
	                    		$error_arr[$k] = $this->get_error_data($k, 'S003', 'SKU:' . $val['SKU'] . '不存在或者无效', $v, $error_arr);
	                    		$m++;
	                    		break;
	                    	} elseif ($val['SKU'] != $rs['sku']) {
	                    		$error_arr[$k] = $this->get_error_data($k, 'S003', 'SKU:' . $val['SKU'] . '大小写错误', $v, $error_arr);
	                    		$m++;
	                    		break;
	                    	} else {
	                    		$skuArr[$k] = $val['SKU'];
	                    	}
                    	}
                    }
                    if (empty($val['ExpectedQty'])) {
                        $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中订货数不能为空', $v, $error_arr);
                        $m++;
                        break;
                    }
                }
            }
            if ($m > 0) {
                continue;
            }
            
            $success_arr[$k] = $v;
            $exists_arr[$v['WarehouseID']][$v['OrderType']][$v['OrderNo']] = $v['OrderNo'];
        }
        
        if (! empty($error_arr)) {
            $xmlData = $this->get_error_str($error_arr);
        }
        msg::$err_arr = $error_arr;
        if (empty($success_arr)) {
        	//接口层记录日志
        	$logExt = array(
        			'api_url' => '',
        			'api_method' => service::$_method,
        			'api_params' => $requestData,
        			'return_msg' => 'filter: 数据校验不通过'
        	);
            return $this->output(0, 'filter: 数据校验不通过', '0001', $xmlData, $logExt);
        } else {
        	$requestData = array('header' => $success_arr);
        }               
        return $this->output('succ');
    }

    /**
     * 记录错误数据
     * 
     * @param $key 键名            
     * @param $errorCode 错误编码            
     * @param $errorDescr 错误描述            
     * @param $data 错误详细数据            
     * @param $error_arr 错误数组            
     * @return $error_arr
     */
    public function get_error_data($key, $errorCode, $errorDescr, $data, $error_arr)
    {
        $return_arr = array();
        if (empty($error_arr[$key])) {
            $return_arr = $data;
            $return_arr['errorcode'] = $errorCode;
            $return_arr['errordescr'] = $errorDescr;
        } else {
            $return_arr = $error_arr[$key];
        }
        return $return_arr;
    }
}