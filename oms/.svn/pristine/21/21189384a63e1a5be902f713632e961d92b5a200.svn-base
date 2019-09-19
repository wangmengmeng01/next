<?php
/**
 * 缺货订单接收接口
 * @author Renee
 *
 */
class omsDeliveryOrderShortage{
    public $msgObj = null;
    
    public function __construct(){
        $this->msgObj = new msg();
    }
    public function get($params){
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            if (empty($params['deliveryOrder']) || empty($params['orderLines']['orderLine'])) {
                return $this->msgObj->outputQimen('failure', '失败：请求的数据不完整', 'S003');
            } else {
                try {
                    global $db;
                    //查询有无该发货单
                    $queryInfoSql = "SELECT delivery_id,order_type,deliv_create_time,create_time FROM t_delivery_order_info WHERE delivery_order_code=:delivery_order_code AND customer_id=:customer_id";
                    $queryInfoModel = $db->prepare($queryInfoSql);
                    $queryInfoModel->bindParam(':delivery_order_code',$params['deliveryOrder']['deliveryOrderCode']);
                    $queryInfoModel->bindParam(':customer_id',$params['deliveryOrder']['ownerCode']);
                    $queryInfoModel->execute();
                    $deliveryOrderInfo = $queryInfoModel->fetch(PDO::FETCH_ASSOC);
                    
                    if (!empty($deliveryOrderInfo)) {
                        //查询缺货表中有无erp取消的单子,有的话直接跳出，不做处理
                        $query4Sql = "SELECT order_id FROM t_delivery_order_shortage WHERE delivery_order_code=:delivery_order_code AND customer_id=:customer_id AND done_flag IN(2,4)";
                        $query4Model = $db->prepare($query4Sql);
                        $query4Model->bindParam(':delivery_order_code',$params['deliveryOrder']['deliveryOrderCode']);
                        $query4Model->bindParam(':customer_id',$params['deliveryOrder']['ownerCode']);
                        $query4Model->execute();
                        $erpCancelOrderInfo = $query4Model->fetch(PDO::FETCH_ASSOC);

                        if (!empty($erpCancelOrderInfo)) {
                            return $this->msgObj->outputQimen('success', '缺货单接收成功，但由于此订单被客户取消，所以不录入！', '0000');
                        }

                        //查询缺货表中有无未处理的单子
                        $querySql = "SELECT * FROM t_delivery_order_shortage WHERE delivery_order_code=:delivery_order_code AND customer_id=:customer_id AND done_flag=0";
                        $queryModel = $db->prepare($querySql);
                        $queryModel->bindParam(':delivery_order_code',$params['deliveryOrder']['deliveryOrderCode']);
                        $queryModel->bindParam(':customer_id',$params['deliveryOrder']['ownerCode']);
                        $queryModel->execute();
                        $shortageOrderInfo = $queryModel->fetch(PDO::FETCH_ASSOC);
                        
                        //所有商品编码字符串
                        $detailInfo = $this->getOrderDetailInfo($params['deliveryOrder']['deliveryOrderCode'],$params['deliveryOrder']['ownerCode']);
                        $allItemStr = '';
                        foreach ($detailInfo as $d_v) {
                            $allItemStr .= $d_v['item_code'].',';
                        }
                        $allItemStr = substr($allItemStr,0,-1);
                        
                        $shortSkuStr = '';
                        if (empty($params['orderLines']['orderLine'][0])) {
                            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
                        }
                        foreach ($params['orderLines']['orderLine'] as $d_v) {
                            $shortSkuStr .= $d_v['itemCode'].',';
                        }
                        $shortSkuStr = substr($shortSkuStr,0,-1);
                        
                        $now = date('Y-m-d H:i:s');
                        $erpCreateTime = !empty($deliveryOrderInfo['deliv_create_time']) && $deliveryOrderInfo['deliv_create_time'] ? $deliveryOrderInfo['deliv_create_time'] : '';
                        $deliveryCreateTime = $deliveryOrderInfo['create_time'];
                        $orderType = $deliveryOrderInfo['order_type'];
                        if (empty($shortageOrderInfo)) {
                            //新增
                            $insertSql = "INSERT INTO t_delivery_order_shortage(
                                            delivery_order_code,
                                            customer_id,
                                            warehouse_code,
                                            order_type,
                                            short_sku,
                                            all_sku,
                                            done_flag,
                                            erp_create_time,
                                            delivery_create_time,
                                            create_time)
                                        VALUES(
                                            :delivery_order_code,
                                            :customer_id,
                                            :warehouse_code,
                                            :order_type,
                                            :short_sku,
                                            :all_sku,
                                            :done_flag,
                                            :erp_create_time,
                                            :delivery_create_time,
                                            :create_time)";
                            $insertModel = $db->prepare($insertSql);
                            
                            $values = array(
                                ':delivery_order_code'=>$params['deliveryOrder']['deliveryOrderCode'],
                                ':customer_id'=>$params['deliveryOrder']['ownerCode'],
                                ':warehouse_code'=>$params['deliveryOrder']['warehouseCode'],
                                ':order_type'=>$orderType,
                                ':short_sku'=>$shortSkuStr,
                                ':all_sku'=>$allItemStr,
                                ':done_flag'=>0,
                                ':erp_create_time'=>$erpCreateTime,
                                ':delivery_create_time'=>$deliveryCreateTime,
                                ':create_time'=>$now,
                            );
                            $insertModel->execute($values);
                            $orderId = $db->lastInsertID();
                        
                            $this->insertShortageDetail($params, $orderId, $now);
                        } else {
                            //更新
                            $updateOrderSql = "UPDATE t_delivery_order_shortage SET short_sku=:short_sku
                                                WHERE order_id=:order_id;";
                            $updateOrderModel = $db->prepare($updateOrderSql);
                            $updateOrderModel->bindParam(':short_sku',$shortSkuStr);
                            $updateOrderModel->bindParam(':order_id',$shortageOrderInfo['order_id']);
                            $updateOrderModel->execute();
                            
                            $updateInvaliditySql = "UPDATE t_delivery_order_shortage_detail SET is_valid=0
                                                    WHERE order_id = :order_id;";
                            $queryOrderIdModel = $db->prepare($updateInvaliditySql);
                            $queryOrderIdModel->bindParam(':order_id',$shortageOrderInfo['order_id']);
                            $queryOrderIdModel->execute();
                        
                            //插入明细
                            $this->insertShortageDetail($params, $shortageOrderInfo['order_id'], $now);
                        }
                        return $this->msgObj->outputQimen('success', '缺货单接收成功！', '0000');
                    } else {
                        return $this->msgObj->outputQimen('failure', '无此发货单！', 'S003');
                    }
                } catch (Exception $e) {
                    return $this->msgObj->outputQimen('failure', $e->getMessage(), $e->getCode());
                }
            }
        }
    }
    
    /**
     * 插入缺货订单明细
     * @param 请求参数  $params
     * @param 订单id  $orderId
     * @param 当前时间  $now
     */
    public function insertShortageDetail($params,$orderId,$now){
        global $db;
        
        //查询发货单商品明细
        $itemsInfo = $this->getOrderDetailInfo($params['deliveryOrder']['deliveryOrderCode'],$params['deliveryOrder']['ownerCode']);
        
        if (empty($params['orderLines']['orderLine'][0])) {
            $params['orderLines']['orderLine'] = array($params['orderLines']['orderLine']);
        }
        $itemssInfo = array();
        foreach ($itemsInfo as $item_k=>$item_v) {
            $item_v['is_short'] = 0;
            foreach ($params['orderLines']['orderLine'] as $olVal) {
                if ($olVal['itemCode'] == $item_v['item_code']) {
                    $item_v['is_short'] = 1 ;
                }
            }
            $itemssInfo[$item_k] = $item_v;
        }
        
        $insertDetailSql = "INSERT INTO t_delivery_order_shortage_detail(
                                order_id,
                                item_code,
                                item_name,
                                plan_qty,
                                inventory_type,
                                is_short,
                                is_valid,
                                create_time
                                ) VALUES(
                                :order_id,
                                :item_code,
                                :item_name,
                                :plan_qty,
                                :inventory_type,
                                :is_short,
                                :is_valid,
                                :create_time
                                )";
        foreach ($itemssInfo as $item) {
            $insertDetailModel = $db->prepare($insertDetailSql);
            $detailValues = array(
                ':order_id'=>$orderId,
                ':item_code'=>$item['item_code'],
                ':item_name'=>$item['item_name'],
                ':plan_qty'=>$item['plan_qty'],
                ':inventory_type'=>$item['inventory_type'],
                ':is_short'=>$item['is_short'],
                ':is_valid'=>1,
                ':create_time'=>$now,
            );
            $insertDetailModel->execute($detailValues);
        }
    }
    
    /**
     * 查询原订单所有明细
     * @param 订单号 $deliveryOrderCode
     */
    public function getOrderDetailInfo($deliveryOrderCode,$customerId){
        global $db;
        
        //查询发货单商品明细
        $queryItemSql = "SELECT b.item_code item_code,b.item_name item_name,b.plan_qty plan_qty,b.inventory_type inventory_type
                        FROM t_delivery_order_info a
                        LEFT JOIN t_delivery_order_detail b
                        ON a.delivery_id=b.delivery_id
                        WHERE a.delivery_order_code=:delivery_order_code AND a.customer_id=:customer_id";
        $queryItemModel = $db->prepare($queryItemSql);
        $queryItemModel->bindParam(':delivery_order_code',$deliveryOrderCode);
        $queryItemModel->bindParam(':customer_id',$customerId);
        $queryItemModel->execute();
        return $queryItemModel->fetchAll(PDO::FETCH_ASSOC);
    }
}