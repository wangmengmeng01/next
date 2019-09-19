<?php
/**
 * 跨境出库单下发过滤类
 * @author wp
 *
 */
class filterCrossBorderDelivery extends msg
{

    /**
     * 跨境出库单信息校验
     * @param $requestData
     * @return xml
     */
    public function create(&$requestData)
    {
        if(empty($requestData['header'])) {
            //接口层记录日志
            $logExt = array(
                'api_url' => '',
                'api_method' => service::$_method,
                'api_params' => $requestData,
                'return_msg' => 'filter: header不能为空'
            );
            return $this->output(0, 'data不能为空', 'S003', '', $logExt);
        }
        //判断数据里面订单是否有多个
        $utilObj = new util();
        $multiFlag = $utilObj->isArrayMulti($requestData);
        if ($multiFlag) {
            $headers = $requestData['header'];
        } else {
            $headers = array($requestData['header']);
        }

        $error_arr = array();
        $success_arr = array();
        $exists_arr = array();
        $customerArr = array();
        $warehouseArr = array();
        $skuArr = array();
        global $db;
        foreach ($headers as $k => $v)
        {
            //单头信息校验
            //校验验订单类型
            if(empty($v['OrderType'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型不能为空', $v, $error_arr);
                continue;
            } elseif (!in_array($v['OrderType'], array('SO', 'TO', 'RP', 'IL'))) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单类型错误', $v, $error_arr);
                continue;
            }
            //校验出库单号
            if (empty($v['OrderNo'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单号不能为空', $v, $error_arr);
                continue;
            } else {
                //校验订单号是否存在
                if (STRICT_VERIFY_FLAG) {
                    $sql = 'SELECT order_id,order_status FROM t_outbound_info WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
                    $model = $db->prepare($sql);
                    $model->bindParam(':order_no', $v['OrderNo']);
                    $model->bindParam(':order_type', $v['OrderType']);
                    $model->bindParam(':customer_id', $v['CustomerID']);
                    $model->bindParam(':warehouse_code', $v['WarehouseID']);
                    $model->execute();
                    $rs = $model->fetch(PDO::FETCH_ASSOC);
                    if (!empty($rs) && $rs['order_status'] != '') {
                        $error_arr[$k] = $this->get_error_data($k, 'S003', '该订单仓库已经有操作，不允许修改', $v, $error_arr);
                        continue;
                    }
                }
            }
            //校验数据是否重复
            if ($exists_arr[$v['WarehouseID']][$v['OrderType']][$v['OrderNo']] != '') {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单号'.$v['OrderNo'].'重复', $v, $error_arr);
                continue;
            }
            //校验仓库ID
            if (empty($v['WarehouseID'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '仓库ID不能为空', $v, $error_arr);
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
            //校验货主
            if (empty($v['CustomerID'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '货主ID不能为空', $v, $error_arr);
                continue;
            } else {
                if (!in_array($v['CustomerID'], $customerArr) && STRICT_VERIFY_FLAG) {
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
            }
            //校验订单创建时间
            if (empty($v['OrderTime'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单创建时间不能为空', $v, $error_arr);
                continue;
            } elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $v['OrderTime'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单创建时间格式错误，时间格式为YYYY-MM-DD HH:MM:SS', $v, $error_arr);
                continue;
            }
            //校验订单下发方
            if (empty($v['UserDefine4'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单下发方不能为空', $v, $error_arr);
                continue;
            } elseif (!in_array($v['UserDefine4'], array('ERP', 'OMS', 'WMS'))) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单下发方只能为ERP、OMS和WMS', $v, $error_arr);
                continue;
            }
            //校验承运商编码和名称，如果没有默认为YUNDA
            if (empty($v['CarrierId'])) {
                $v['CarrierId'] = 'YUNDA';
            }
            if (empty($v['CarrierName'])) {
                $v['CarrierName'] = '韵达快递';
            }

            //包裹信息校验
            $p1 = 0;
            if (empty($v['package'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '订单包裹信息不能为空', $v, $error_arr);
                continue;
            } else {
                if (empty($v['package'][0])) {
                    $v['package'] = array($v['package']);
                }
                foreach ($v['package'] as $p_val) {
                    if (empty($p_val['PackageNo'])) {
                        $error_arr[$k] = $this->get_error_data($k, 'S003', '入库凭证号不能为空', $v, $error_arr);
                        $p1++;
                        continue;
                    } else {
                        //明细信息校验
                        $m = 0;
                        if (empty($p_val['detailsItem'])) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细不能为空', $v, $error_arr);
                            continue;
                        } else {
                            if (empty($p_val['detailsItem'][0])) {
                                $p_val['detailsItem'] = array($p_val['detailsItem']);
                            }
                            foreach ($p_val['detailsItem'] as $val)
                            {
                                if (empty($val['CustomerID'])) {
                                    $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中货主ID不能为空', $v, $error_arr);
                                    $m++;
                                    break;
                                } else {   //验证单头中的货主ID和明细信息中的货主ID是否一致
                                    if ($v['CustomerID'] != $val['CustomerID']) {
                                        $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中货主ID与单头中的货主ID不一致', $v, $error_arr);
                                        $m++;
                                        break;
                                    }
                                }
                                if (empty($val['SKU'])) {
                                    $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中SKU:'.$val['SKU'].'不能为空', $v, $error_arr);
                                    $m++;
                                    break;
                                } else {
                                    if (!in_array($val['SKU'], $skuArr) && STRICT_VERIFY_FLAG) {
                                        //校验SKU是否存在
                                        $sql = "SELECT sku FROM t_base_product WHERE sku=:sku AND customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
                                        $model = $db->prepare($sql);
                                        $model->bindParam(':sku', $val['SKU']);
                                        $model->bindParam(':customer_id', $v['CustomerID']);
                                        $model->execute();
                                        $rs = $model->fetch(PDO::FETCH_ASSOC);
                                        if (empty($rs)) {
                                            $error_arr[$k] = $this->get_error_data($k, 'S003', 'SKU:'.$val['SKU'].'不存在或不属于货主', $v, $error_arr);
                                            $m++;
                                            break;
                                        } elseif ($rs['sku'] != $val['SKU']) {
                                            $error_arr[$k] = $this->get_error_data($k, 'S003', 'SKU大小写错误', $v, $error_arr);
                                            $m++;
                                            break;
                                        }
                                        $skuArr[$k] = $val['SKU'];
                                    }
                                }
                                if (empty($val['QtyOrdered'])) {
                                    $error_arr[$k] = $this->get_error_data($k, 'S003', '订单明细中订货数不能为空', $v, $error_arr);
                                    $m++;
                                    break;
                                }
                            }
                        }
                        if ($m > 0) {
                            continue;
                        }
                    }
                }
            }
            if ($p1 > 0) {
                continue;
            }

            //发票信息校验
            $n = 0;
            if ($v['InvoicePrintFlag'] == 1) {
                if (empty($v['invoiceItem'])) {
                    $error_arr[$k] = $this->get_error_data($k, 'S003', '发票信息不能为空', $v, $error_arr);
                    continue;
                } else {
                    if (empty($v['invoiceItem'][0])) {
                        $v['invoiceItem'] = array($v['invoiceItem']);
                    }
                    foreach ($v['invoiceItem'] as $val)
                    {
                        if (empty($val['OrderNo'])) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '发票信息中订单号不能为空', $v, $error_arr);
                            $n++;
                            break;
                        }
                        if (empty($val['LineNumber'])) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '发票信息中行编号不能为空', $v, $error_arr);
                            $n++;
                            break;
                        }
                        if (empty($val['SKU'])) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '发票信息中SKU不能为空', $v, $error_arr);
                            $n++;
                            break;
                        }
                        if (empty($val['UOM'])) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '发票信息中单位不能为空', $v, $error_arr);
                            $n++;
							break;
						}
                        if (empty($val['QTY'])) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '发票信息中数量不能为空', $v, $error_arr);
                            $n++;
                            break;
                        }
                        if (empty($val['UnitPrice'])) {
                            $error_arr[$k] = $this->get_error_data($k, 'S003', '发票信息中单价不能为空', $v, $error_arr);
                            $n++;
                            break;
                        }
                    }
                }
            }
            if ($n > 0) {
                continue;
            }

            $success_arr[$k] = $v;
            $exists_arr[$v['WarehouseID']][$v['OrderType']][$v['OrderNo']] = $v['OrderNo'];
        }

        if(!empty($error_arr)) {
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
     * @param $key  键名
     * @param $errorCode  错误编码
     * @param $errorDescr 错误描述
     * @param $data       错误详细数据
     * @param $error_arr  错误数组
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