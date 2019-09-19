<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2019/1/21
 * Time: 13:48
 */
require_once API_ROOT . '/router/interface/wms/vip/wmsRequest.php';

class pickOrder extends wmsRequest
{
    /**
     * 下发拣货单
     * @param $params
     * @return array
     */
    public function create($jsonData)
    {
        if (!empty($jsonData)) {
            // 获取拣货单数据
            $aData = json_decode($jsonData, true);
            $params = $this->getPickData($aData);
            // 转发数据给WMS
            $response = $this->send(vip_service::$_method, $params);
            if ($response == '') {
                $rs = array(
                    "flag" => "failure",
                    "code" => "S003",
                    "message" => "请求超时"
                );
            } else {
                $rs = urldecode($response);
                $rs = $this->xmlObj->xmlStr2array($rs);
                $rs = $this->utilObj->filter_null($rs);
                if (!isset($aData['is_lack']) || $aData['is_lack'] != 1) {
                    if ($rs['flag'] == 'success') {
                        //更新拣货单状态为 已下发
                        $db = OmsDatabase::$oms_db;
                        $updateData = array(
                            'status' => '已下发',
                            'is_send' => 1,
                            'send_time' => date("Y-m-d H:i:s"),
                            'warehouse' => $params['request']['warehouse']
                        );
                        $where = 'pick_no=:pick_no and vendor_id = :vendor_id and is_lack = 0';
                        $whereParams = array(':pick_no' => $params['request']['pick_no'], ':vendor_id' => $aData['vendor_id']);
                        $db->update('t_vip_pick_list', $updateData, $where, $whereParams);
                    }
                }
            }
        } else {
            $rs = array(
                "flag" => "failure",
                "code" => "S003",
                "message" => "请求参数不能为空"
            );
        }
        return $this->arr_to_json($rs);
    }

    /***
     * Notes:数组转化为json格式的数据（中文不转义）
     * Date: 2019/3/8
     * Time: 16:26
     * @param $arr 数组
     * @return string json字串
     */
    public function arr_to_json($arr)
    {
        $str = '';
        foreach ($arr as $k => $a) {
            $str .= '"' . $k . '":' . '"' . $a . '",';
        }
        $str = rtrim($str, ',');
        $json_str = "{" . $str . "}";
        return $json_str;
    }

    public function getPickData($aData)
    {
        //获取 拣货单信息
        $db = OmsDatabase::$oms_db;
        $pick_where = "p.vendor_id = :vendor_id and p.pick_no = :pick_no ";
        $pick_params = array(':vendor_id' => $aData['vendor_id'], ':pick_no' => $aData['pick_no']);
        $select = "p.id,p.warehouse,p.vendor_id,p.po_no,p.pick_no,p.sell_site,p.create_time,p.order_cate,p.total,p.store_sn,p.jit_type,p.co_mode,po.brand_name,po.sell_st_time,po.sell_et_time";
        $p_sql = "select {$select} from t_vip_pick_list as p LEFT JOIN t_vip_po_list as po on po.po_no = p.po_no and po.vendor_id = p.vendor_id where {$pick_where}  order by p.record_time desc limit 1";
        $p_model = $db->getPdo()->prepare($p_sql);
        $p_model->execute($pick_params);
        $pick_info = $p_model->fetch(PDO::FETCH_ASSOC);
        //获取拣货单商品明细
        if (isSet($aData['is_lack']) && $aData['is_lack'] == 1) {
            //缺货单下发
            $product_where = 'd.is_valid = 1 and d.is_short =1 and p.id = :id and o.done_flag =0 ';
            $product_params = array(':id' => $pick_info['id']);
            $lack_sql = "select pp.*,d.lack_qty from t_delivery_order_shortage_detail as d 
                         join t_delivery_order_shortage as o on d.order_id = o.order_id 
                         join t_vip_pick_list as p  on o.delivery_order_code = p.pick_no and o.customer_id = p.vendor_id and o.warehouse_code = p.warehouse                              join t_vip_pick_product as pp on p.id = pp.pick_id and pp.barcode = d.item_code
                         where {$product_where}";
            $lack_model = $db->getPdo()->prepare($lack_sql);
            $lack_model->execute($product_params);
            $pick_product = $lack_model->fetchAll(PDO::FETCH_ASSOC);
        } else {
            $product_where = 'pick_id=:pick_id';
            $product_params = array(':pick_id' => $pick_info['id']);
            $pick_product = $db->fetchAll('*', 't_vip_pick_product', $product_where, $product_params);
        }
        $product = array();
        foreach ($pick_product as $item) {
            array_push($product, array(
                'item' => array(
                    'stock' => isset($item['lack_qty']) ? $item['lack_qty'] : $item['stock'],
                    'barcode' => $item['barcode'],
                    'art_no' => $item['art_no'],
                    'product_name' => $item['product_name'],
                    'size' => $item['size'],
                    'actual_unit_price' => $item['actual_unit_price'],
                    'actual_market_price' => $item['actual_market_price'],
                    'not_delivery_num' => $item['not_delivery_num'],
                )
            ));
        }
        // 未指定拣货仓时，获取默认拣货仓
        if (empty($aData['warehouse'])) {
            $where = 'customer_id=:customer_id AND platform_code=:platform_code AND rc_addr=:rc_addr AND shop_name=:shop_name AND is_valid=1';
            $where_params = array(
                ':customer_id' => $pick_info['vendor_id'],
                ':platform_code' => 'VIP',
                ':rc_addr' => $pick_info['sell_site'],
                ':shop_name' => $pick_info['store_sn']
            );
            $wh = $db->fetchOne('wh1', 't_vip_wh_info', $where, $where_params);
            if ($wh) {
                $aData['warehouse'] = $wh['wh1'];
            } else {
                $aData['warehouse'] = '';
            }
        }
        $arrData = array(
            'request' => array(
                'warehouse' => $aData['warehouse'],
                'vendor_id' => $pick_info['vendor_id'],
                'po_no' => $pick_info['po_no'],
                'pick_no' => $pick_info['pick_no'],
                'sell_site' => $pick_info['sell_site'],
                'create_time' => $pick_info['create_time'],
                'brand_name' => $pick_info['brand_name'],
                'sell_st_time' => $pick_info['sell_st_time'],
                'sell_et_time' => $pick_info['sell_et_time'],
                'order_cate' => $pick_info['order_cate'],
                'total' => $pick_info['total'],
                'store_sn' => $pick_info['store_sn'],
                'jit_type' => $pick_info['jit_type'],
                'co_mode' => $pick_info['co_mode'],
                'items' => $product
            )
        );
        /*
        $product[] = array('item'=>array(
            'stock' => 'ddfs111',
            'barcode' => '524212',
            'art_no' => '45645',
            'product_name' => 'dasdf',
            'size' => 48,
            'actual_unit_price' => 18,
            'actual_market_price' => 22,
            'not_delivery_num' => 5,
        ));
        $product[] = array('item'=>array(
            'stock' => 'ddfs222',
            'barcode' => '524212',
            'art_no' => '45645',
            'product_name' => 'dasdf',
            'size' => 48,
            'actual_unit_price' => 18,
            'actual_market_price' => 22,
            'not_delivery_num' => 5,
        ));
        $arrData = array(
            'request'=>array(
                'warehouse' => 'QMWS',
                'vendor_id' => 550,
                'po_no' => '7754655',
                'pick_no' => '445445',
                'sell_site' => 'sdfasfsa',
                'create_time' => '2018-12-25 11:10:11',
                'brand_name' => "dfsdd",
                'sell_st_time' => '2018-12-24 11:10:11',
                'sell_et_time' => '2018-12-26 11:10:11',
                'order_cate' => "42545",
                'total' => 6,
                'store_sn' => '5454ddd',
                'jit_type' => '1',
                'co_mode' => '545',
                'items' => $product
            )
        );
        */
        return $arrData;
    }
}