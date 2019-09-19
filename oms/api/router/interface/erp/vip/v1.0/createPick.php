<?php
/**
 * Notes:创建拣货单、获取拣货单列表、拣货单详细
 * Date: 2019/1/2
 * Time: 10:55
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';

class createPick extends erpRequest
{
    /**
     * Notes:创建拣货单
     * Date: 2019/1/2
     * Time: 11:13
     */
    public function create($jsonData)
    {
        //记录脚本运行
        OmsDatabase::$oms_db->insert('t_vip_crontab_time', array('pick_time' => date('Y-m-d H:i:s')));
        try {
            //获取未抓取拣货单的po单
            $po_list = OmsDatabase::$oms_db->fetchAll('id,vendor_id,po_no', 't_vip_po_list', 'is_get = 0');
//            $logName = date("Ymd") . '_oms_vip_createPick.log';
//            error_log(print_r(date('Y-m-d H:i:s') . '    ' . $po_list, 1) . PHP_EOL, 3, 'G:/log/' . $logName);
            if (!empty($po_list)) {
                foreach ($po_list as $p_v) {
                    $po_no = $p_v['po_no'];
                    $vendor_id = $p_v['vendor_id'];
                    $po_id = $p_v['id'];
                    //访问唯品会createPick接口
                    $arr = array(
                        'vendor_id' => $vendor_id,
                        'po_no' => $po_no
                    );
                    $params = array(
                        'method' => 'createPick',
                        'data' => json_encode($arr)
                    );
                    $res = $this->send($params);
                    $data = json_decode($res, true);
                    $return_code = $data['returnCode'];
                    $list = @$data['result'];
                    if ($return_code === '0') {
                        if (!empty($list)) {
                            foreach ($list as $v) {
                                //判断是否存在记录
                                OmsDatabase::$oms_db->getPdo()->beginTransaction();
                                $pick_res = true;
                                $pick_info = OmsDatabase::$oms_db->fetchOne('id', 't_vip_pick_list', 'vendor_id=:vendor_id and pick_no=:pick_no and po_no=:po_no and is_lack = 0', array(':vendor_id' => $vendor_id, 'pick_no' => $v['pick_no'], 'po_no' => $po_no));
                                if (empty($pick_info)) {
                                    $insert_arr = [
                                        'vendor_id' => $vendor_id,
                                        'po_no' => $po_no,
                                        'pick_no' => $v['pick_no'],
                                        'po_id' => $po_id,
                                        'pick_type' => $v['pick_type'],
                                        'sell_site' => $v['warehouse'],
                                        'store_sn' => $v['store_sn'],
                                        'jit_type' => $v['jit_type'],
                                        'record_time' => date('Y-m-d H:i:s'),
                                    ];
                                    $pick_res = OmsDatabase::$oms_db->insert('t_vip_pick_list', $insert_arr);
                                }
                                //修改po_list中的is_get状态
                                $get_res = OmsDatabase::$oms_db->update('t_vip_po_list', array('is_get' => 1), 'id=:id', array(':id' => $po_id));
                                if ($pick_res && $get_res) {
                                    OmsDatabase::$oms_db->getPdo()->commit();
                                } else {
                                    OmsDatabase::$oms_db->getPdo()->rollBack();
                                    echo '拉取成功但存入数据库失败';
                                }
                            }
                        }
                    } else {
                        throw new Exception("访问创建拣货单接口失败");
                    }
                }
            }
            //获取拣货单列表
            $this->getPickList();
//            //获取指定拣货单明细信息
            $this->getDetail();
            echo '完成！！';
            exit;
        } catch (\Exception $e) {
            //4、捕获异常
//            error_log(print_r(date('Y-m-d H:i:s') . '    ' . $e->getMessage(), 1) . PHP_EOL, 3, 'G:/log/' . $logName);
//            return $this->msgObj->outputVip(false, '失败：唯品会创建拣货单调用失败', '', $this->logTxt);
            return  $json_return = '{"success":false,"reasons":"' . $e->getMessage() . '"}';

        }
        echo '创建拣货单信息成功！';
    }

    /***
     * Notes:获取指定供应商下拣货单列表信息
     * Date: 2019/1/10
     * Time: 9:37
     * @return bool   true 成功  false 表示失败
     */
    public function getPickList()
    {
        try {
            //获取po单信息
            $po_list = OmsDatabase::$oms_db->fetchAll('id,vendor_id,po_no', 't_vip_po_list', 'is_get in (0,1)');
//            $po_list = OmsDatabase::$oms_db->fetchAll('id,vendor_id,po_no', 't_vip_po_list', 'is_get = 1');
            if (!empty($po_list)) {
                foreach ($po_list as $p) {
                    $vendor_id = $p['vendor_id'];
//                    $po_no = $p['po_no'];
                    $page = 1;
                    while (true) {
                        $con = array(
                            'vendor_id' => $vendor_id,
//                            'po_no' => $po_no,
                            'limit' => 100,
                            'page' => $page
                        );
                        $params = array(
                            'method' => 'getPickList',
                            'data' => json_encode($con)
                        );
                        $res = $this->send($params);
                        $data = json_decode($res, true);
                        $list = @$data['result']['picks'];
                        if ($data['returnCode'] === '0') {
                            if (!empty($list)) {
                                foreach ($list as $v) {
                                    $pick_info = OmsDatabase::$oms_db->fetchOne('id,delivery_status', 't_vip_pick_list', 'vendor_id=:vendor_id and pick_no=:pick_no and po_no=:po_no and is_lack = 0', array(':vendor_id' => $vendor_id, 'pick_no' => $v['pick_no'], 'po_no' => $v['po_no']));
                                    if ($pick_info) {
                                        //更新拣货单信息
                                        $update_arr = array(
                                            'co_mode' => $v['co_mode'],
                                            'order_cate' => $v['order_cate'],
                                            'pick_num' => $v['pick_num'],
                                            'first_export_time' => $v['first_export_time'],
                                            'export_num' => $v['export_num'],
                                            'delivery_status' => $v['delivery_status'],
                                            'delivery_num' => $v['delivery_num'],
                                            'create_time' => $v['create_time']
                                        );
                                        if ($pick_info['delivery_status'] == 1) {
                                            $update_arr['status'] = '已送货';
                                        }
                                        $pick_result = OmsDatabase::$oms_db->update('t_vip_pick_list', $update_arr, 'id=:id', array(':id' => $pick_info['id']));
                                    } else {
                                        //新增拣货单信息
                                        $insert_arr = array(
                                            'pick_no' => $v['pick_no'],
                                            'vendor_id' => $vendor_id,
                                            'sell_site' => $v['sell_site'],
                                            'po_id' => $p['id'],
                                            'po_no' => $v['po_no'],
                                            'co_mode' => $v['co_mode'],
                                            'order_cate' => $v['order_cate'],
                                            'pick_num' => $v['pick_num'],
                                            'create_time' => $v['create_time'],
                                            'first_export_time' => $v['first_export_time'],
                                            'export_num' => $v['export_num'],
                                            'delivery_status' => $v['delivery_status'],
                                            'store_sn' => $v['store_sn'],
                                            'delivery_num' => $v['delivery_num'],
                                            'record_time' => date('Y-m-d H:i:s')
                                        );
                                        $pick_result = OmsDatabase::$oms_db->insert('t_vip_pick_list', $insert_arr);
                                    }
//                                    if (!empty($pick_result)) {
//                                        echo '拉取拣货单成功但存入数据库失败！！';exit;
//                                    }
                                }
                            } else {
                                break;
                            }
                        } else {
                            if ($page == 1) {
                                throw new Exception("拉取拣货单失败");
                            } else {
                                break;
                            }
                        }
                        $page++;
                    }
                }
            }
        } catch (\Exception $e) {
            return  $json_return = '{"success":false,"reasons":"' . $e->getMessage() . '"}';
        }
    }

    /***
     * Notes:获取指定拣货单明细信息
     * Date: 2019/1/9
     * Time: 15:42
     * @return bool
     */
    public function getDetail()
    {
        $pick_list = OmsDatabase::$oms_db->fetchAll('id,pick_no,vendor_id,po_no', 't_vip_pick_list', 'is_get_detail = 0 and is_lack = 0');
        if (!empty($pick_list)) {
            foreach ($pick_list as $p) {
                $po_no = $p['po_no'];
                $vendor_id = $p['vendor_id'];
                $pick_no = $p['pick_no'];
                $pick_id = $p['id'];
                if (count(explode(',', $po_no)) > 1) {
                    $this->getMultiPoPickDetail($vendor_id, $pick_no, $pick_id);
                } else {
                    $this->getPickDetail($po_no, $vendor_id, $pick_no, $pick_id);
                }
            }
        }
    }


    /***
     * Notes:获取指定拣货单明细信息(只支持单PO创建的拣货单)
     * Date: 2019/1/9
     * Time: 16:19
     * @param $po_no      po采购单号
     * @param $vendor_id  货主编码
     * @param $pick_no    拣货单编号
     * @param $pick_id    拣货单表id
     * @return bool
     */
    public function getPickDetail($po_no, $vendor_id, $pick_no, $pick_id)
    {
        try {
            $page = 1;
            //循环有问题
            while (true) {
                $con = array(
                    'vendor_id' => $vendor_id,
                    'po_no' => $po_no,
                    'pick_no' => $pick_no,
                    'limit' => 100,
                    'page' => $page
                );
                $params = array(
                    'method' => 'getPickDetail',
                    'data' => json_encode($con)
                );
                $res = $this->send($params);
                $data = json_decode($res, true);
                $result = @$data['result'];
                $list = @$result['pick_product_list'];
                if ($data['returnCode'] === '0') {
                    if (!empty($list)) {
                        $total = $result['total'];
                        OmsDatabase::$oms_db->getPdo()->beginTransaction();
                        //插入拣货单商品信息
                        $insert_arr = array();
                        foreach ($list as $p) {
                            //查询JIT供货价信息
//                            list($actual_unit_price, $actual_market_price) = $this->getSkuPriceInfo($vendor_id, $po_no, $p['barcode']);
                            $insert_arr[] = [
                                'pick_id' => $pick_id,
                                'pick_no' => $pick_no,
                                'stock' => $p['stock'],
                                'barcode' => $p['barcode'],
                                'art_no' => $p['art_no'],
                                'product_name' => $p['product_name'],
                                'size' => $p['size'],
//                                'actual_unit_price' => @$actual_unit_price,
//                                'actual_market_price' => @$actual_market_price,
                                'actual_unit_price' => 0.00,
                                'actual_market_price' => 0.00,
                                'not_delivery_num' => $p['not_delivery_num'],
                                'create_time' => date('Y-m-d H:i:s')
                            ];
                        }
                        $product_res = OmsDatabase::$oms_db->insertAll('t_vip_pick_product', $insert_arr);
                        //更新拣货单列表信息
                        $pick_res = OmsDatabase::$oms_db->update('t_vip_pick_list', array('total' => $total, 'is_get_detail' => 1), 'id = :pick_id', array(':pick_id' => $pick_id));
                        if ($pick_res && $product_res) {
                            OmsDatabase::$oms_db->getPdo()->commit();
                        } else {
                            OmsDatabase::$oms_db->getPdo()->rollBack();
//                            echo '拉取成功但存入数据库失败';
//                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    if ($page == 1) {
                        throw new Exception("拉取指定拣货单详细失败");
                    } else {
                        break;
                    }
                }
                $page++;
            }
        } catch (\Exception $e) {
            return $json_return = '{"success":false,"reasons":"' . $e->getMessage() . '"}';
        }
    }
    /***
     * Notes:获取指定拣货单明细信息2.0版本(只支持多PO创建的拣货单)
     * Date: 2019/1/9
     * Time: 16:21
     * @param $vendor_id   货主编码
     * @param $pick_no     拣货单编号
     * @param $pick_id     拣货单表id
     * @return bool
     */
    public function getMultiPoPickDetail($vendor_id, $pick_no, $pick_id)
    {
        try {
            $page = 1;
            while (true) {
                $con = array(
                    'getPickDetailRequest' => array(
                        'vendor_id' => $vendor_id,
                        'pick_no' => $pick_no,
                        'limit' => 100,
                        'page' => $page
                    )
                );
                $params = array(
                    'method' => 'getMultiPoPickDetail',
                    'data' => json_encode($con)
                );
                $res = $this->send($params);
                $data = json_decode($res, true);
                $result = @$data['result'];
                $list = @$result['pick_detail_list'];
                if ($data['returnCode'] === '0') {
                    if (!empty($list)) {
                        $total = $result['total'];
                        OmsDatabase::$oms_db->getPdo()->beginTransaction();
                        $insert_arr = array();
                        foreach ($list as $v) {
                            $insert_arr[] = [
                                'pick_id' => $pick_id,
                                'pick_no' => $pick_no,
                                'sell_site' => $v['warehouse'],
                                'barcode' => $v['barcode'],
                                'art_no' => $v['sn'],
                                'product_name' => $v['product_name'],
                                'size' => $v['size'],
                                'stock' => $v['pick_num'],
                                'not_delivery_num' => $v['not_delivery_num'],
                                'store_sn' => $v['store_sn'],
                                'jit_type' => $v['jit_type']
                            ];
                        }
                        $multi_res = OmsDatabase::$oms_db->insertAll('t_vip_pick_product', $insert_arr);
                        //更新拣货单列表信息
                        $pick_res = OmsDatabase::$oms_db->update('t_vip_pick_list', array('total' => $total, 'is_get_detail' => 1), 'id = :pick_id', array(':pick_id' => $pick_id));
                        if ($pick_res && $multi_res) {
                            OmsDatabase::$oms_db->getPdo()->commit();
                        } else {
                            OmsDatabase::$oms_db->getPdo()->rollBack();
//                            echo '拉取成功但存入数据库失败';exit;
                        }
                    } else {
                        break;
                    }
                } else {
                    if ($page == 1) {
                        throw new Exception("拉取指定拣货单详细失败");
                    } else {
                        break;
                    }
                }
                $page++;
            }
        } catch (\Exception $e) {
            return $json_return = '{"success":false,"reasons":"' . $e->getMessage() . '"}';
        }
    }
    /***
     * Notes:
     * Date: 2019/1/9
     * Time: 15:34
     * @param $vendor_id   货主编码
     * @param $po_no       po采购单号
     * @return array       $actual_unit_price  结算价（不含税）  $actual_market_price  结算价（含税）
     */
    public function getSkuPriceInfo($vendor_id, $po_no, $barcode)
    {
        try {
            $arr = array(
                'request' => array(
                    'po_no' => $po_no,
                    'vendor_id' => $vendor_id,
                    'barcodes' => array($barcode)
                )
            );
            $params = array(
                'method' => 'getPickDetail',
                'data' => json_encode($arr)
            );
            $res = $this->send($params);
            $data = json_decode($res, true);
            $return_code = $data['returnCode'];
            $price_list = @$data['result']['price_list'];
            if ($return_code === '0') {
                $actual_unit_price = $price_list[0]['actual_unit_price'];
                $actual_market_price = $price_list[0]['actual_market_price'];
                return array($actual_unit_price, $actual_market_price);
            } else {
                throw new Exception("访问借口失败");
            }
        } catch (\Exception $e) {
            return $json_return = '{"success":false,"reasons":"' . $e->getMessage() . '"}';
        }
    }
}