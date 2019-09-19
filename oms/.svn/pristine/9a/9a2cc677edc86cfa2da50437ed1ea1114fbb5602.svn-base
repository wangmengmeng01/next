<?php
/**
 * Notes:唯品会缺货回传接口taobao.qimen.deliveryorder.shortage
 * Date: 2019/3/4
 * Time: 9:29
 */
class omsDeliveryOrderShortage
{
    public $msgObj = null;
    public function __construct(){
        $this->msgObj = new msg();
    }
    public function get($params)
    {
        try {
            // 数据检查
            if (empty($params)) {
                return $this->msgObj->outputQimen('failure', '失败：请求的数据为空！', 'S003');
            }
            $delivery_order_code = $params['deliveryOrder']['deliveryOrderCode'];
            $warehouse_code = $params['deliveryOrder']['warehouseCode'];
            $customer_id = $params['deliveryOrder']['ownerCode'];
            $short_sku_num = $params['deliveryOrder']['pickNumLack'];
            if (empty($params['deliveryOrder']) || empty($params['orderLines']['orderLine'])) {
                return $this->msgObj->outputQimen('failure', '失败：请求的数据不完整！', 'S003');
            }

            // 查询有无该拣货单
            $pick_info = OmsDatabase::$oms_db->fetchOne('id,pick_num,co_mode,warehouse,create_time,record_time', 't_vip_pick_list', 'vendor_id=:vendor_id and pick_no=:pick_no and warehouse=:warehouse', array(':vendor_id' => $customer_id, 'pick_no' => $delivery_order_code,'warehouse'=> $warehouse_code));
            if (!empty($pick_info)) {
                //更新拣货单实际商品数量
                $real_pick_num = $pick_info['pick_num'] - $short_sku_num;
                OmsDatabase::$oms_db->update('t_vip_pick_list', array('real_pick_num'=>$real_pick_num), 'id = :id', array(':id' => $pick_info['id']));
                //查询缺货表有未处理的缺货订单
                $shortageOrderInfo = OmsDatabase::$oms_db->fetchOne('order_id', 't_delivery_order_shortage', 'delivery_order_code=:delivery_order_code and customer_id=:customer_id and warehouse_code=:warehouse_code and done_flag = 0', array(':delivery_order_code' => $delivery_order_code, ':customer_id' => $customer_id,'warehouse_code' => $warehouse_code));
                //所有商品编码字符串
                $detailInfo = OmsDatabase::$oms_db->fetchAll('barcode', 't_vip_pick_product', 'pick_id=:pick_id', array(':pick_id' => $pick_info['id']));
                $allItemStr = '';
                foreach ($detailInfo as $d_k=>$d_v) {
                    $allItemStr .= $d_v['barcode'].',';
                }
                $allItemStr = substr($allItemStr,0,-1);
                $shortSkuStr = '';
                if (empty($params['orderLines']['orderLine'][0])) {
                    $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
                }
                foreach ($params['orderLines']['orderLine'] as $d_k=>$d_v) {
                    $shortSkuStr .= $d_v['itemCode'].',';
                }
                $shortSkuStr = substr($shortSkuStr,0,-1);
                $now = date('Y-m-d H:i:s');
                $erpCreateTime = $pick_info['create_time'] ? $pick_info['create_time'] : '';
                $deliveryCreateTime = $pick_info['record_time'];
                if (empty($shortageOrderInfo)) {
                    //新增
                    $insert_arr = [
                        'delivery_order_code' => $delivery_order_code,
                        'customer_id' => $customer_id,
                        'warehouse_code' => $warehouse_code,
                        'order_type' => $pick_info['co_mode'],
                        'short_sku' => $shortSkuStr,
                        'short_sku_num' => $short_sku_num ,
                        'all_sku' => $allItemStr,
                        'done_flag' => 0,
                        'erp_create_time' => $erpCreateTime,
                        'delivery_create_time' => $deliveryCreateTime,
                        'create_time' => $now,
                    ];
                    $orderId = OmsDatabase::$oms_db->insert('t_delivery_order_shortage', $insert_arr);
                    $this->insertShortageDetail($params, $orderId, $now,$pick_info['id']);
                } else {
                    //更新
                    $update_arr = array(
                        'short_sku' => $shortSkuStr,
                        'short_sku_num' => $short_sku_num
                    );
                    //缺货单
                    OmsDatabase::$oms_db->update('t_delivery_order_shortage', $update_arr, 'order_id = :order_id', array(':order_id' => $shortageOrderInfo['order_id']));
                    //商品详情
                    OmsDatabase::$oms_db->update('t_delivery_order_shortage_detail', array('is_valid'=> 0), 'order_id = :order_id', array(':order_id' => $shortageOrderInfo['order_id']));
                    //插入明细
                    $this->insertShortageDetail($params, $shortageOrderInfo['order_id'], $now,$pick_info['id']);
                }
                return $this->msgObj->outputQimen('success', '缺货单接收成功！', '0000');
            } else {
                return $this->msgObj->outputQimen('failure', '失败：无此发货单！', 'S003');
            }
        } catch (Exception $e) {
            return $this->msgObj->outputQimen('failure', $e->getMessage(), 'S003');
        }

    }
    /***
     * Notes:插入缺货订单明细
     * Date: 2019/3/5
     * Time: 15:38
     * @param $params 请求参数
     * @param $orderId  订单id
     * @param $now  当前时间
     * @param $pick_id  拣货单id
     */
    public function insertShortageDetail($params,$orderId,$now,$pick_id)
    {
        //查询发货单商品明细
        $itemsInfo = OmsDatabase::$oms_db->fetchAll('barcode,product_name,stock', 't_vip_pick_product', 'pick_id=:pick_id', array(':pick_id' => $pick_id));
        if (empty($params['orderLines']['orderLine'][0])) {
            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
        }
        $itemssInfo = array();
        foreach ($itemsInfo as $item_k=>$item_v) {
            $item_v['is_short'] = 0;
            foreach ($params['orderLines']['orderLine'] as $olVal) {
                if ($olVal['itemCode'] == $item_v['barcode']) {
                    $item_v['is_short'] = 1 ;
                    $item_v['lack_qty'] = $olVal['lackQty'] ;
                } else {
                    $item_v['lack_qty'] = 0 ;
                }
            }
            $itemssInfo[$item_k] = $item_v;
        }
        $insert_arr = array();
        foreach ($itemssInfo as $item) {
            $insert_arr[] = array(
                'order_id' => $orderId,
                'item_code' => $item['barcode'],
                'item_name' => $item['product_name'],
                'plan_qty' => $item['stock'],
                'lack_qty' => $item['lack_qty'],
                'is_short' => $item['is_short'],
                'is_valid' => 1,
                'create_time' => $now
            );
        }
        OmsDatabase::$oms_db->insertAll('t_delivery_order_shortage_detail', $insert_arr);
    }
}