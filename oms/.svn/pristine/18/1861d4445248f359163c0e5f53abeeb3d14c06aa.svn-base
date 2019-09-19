<?php
/**
 * Notes:贝贝发货单创建接口业务处理类
 * Date: 2019/6/17
 * Time: 22:38
 */

require API_ROOT . 'router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsDeliveryOrderCreate extends wmsRequest
{
    /**
     * 创建发货单
     * @param $params
     * @return array
     */
    public function create($params)
    {
        try {
            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }
            //转发数据给wms
            $response = $this->send();

            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '', 'wms接口调用失败');
            }

            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }
            //开启事物
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            //校验发货单是否存在，如果存在则状态置为无效
            $this->checkDeliveryOrder($params);
            //写入数据库
            $this->insertDeliveryOrder($params);

            //事务提交
            OmsDatabase::$oms_db->getPdo()->commit();
            return $this->msgObj->outputBeibei(true, $response['data'], $response['message'], $response['addon']);
        } catch (PDOException $p) {
            //事务回滚
            OmsDatabase::$oms_db->getPdo()->rollBack();
            return $this->msgObj->outputBeibei(false,'', $p->getMessage());
        } catch (Exception $e) {
            return $this->msgObj->outputBeibei(false, '', $e->getMessage());
        }
    }
    /***
     * Notes:更新订单号和订单类型关系数据
     * Date: 2019/6/18
     * Time: 10:30
     * @param $params
     */
    public function updateOrderNoTypeRelation($params)
    {
        OmsDatabase::$oms_db->update('t_orderno_type_relation', array('order_type' => $params['orderType']), 'order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code', array(':order_no' => $params['orderNo'],':customer_id' => $params['companyId'],':warehouse_code' => $params['warehouseNo']));
    }
    /***
     * Notes:写入订单号和订单类型关系数据
     * Date: 2019/6/18
     * Time: 10:30
     * @param $params
     */
    public function insertOrderNoTypeRelation($params)
    {
        $insert_arr = array(
            'order_no' => $params['orderNo'],
            'order_type' => $params['orderType'],
            'customer_id' => $params['companyId'],
            'warehouse_code' => $params['warehouseNo'],
            'create_time' => date('Y-m-d H:i:s'),
        );
        OmsDatabase::$oms_db->insert('t_orderno_type_relation', $insert_arr);
    }

    /**
     * 校验发货单号是否存在
     * @parameter $params
     */
    public function checkDeliveryOrder($params)
    {
        $rs = OmsDatabase::$oms_db->fetchOne('delivery_id', 't_delivery_order_info', 'delivery_order_code = :order_no AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1', array(':order_no' => $params['orderNo'], ':customer_id' => $params['companyId'], ':warehouse_code' => $params['warehouseNo']));
        if (!empty($rs)) {
            $this->updateOrderNoTypeRelation($params);
            //更新原发货单有效性
            $this->updateDeliveryOrder($rs['delivery_id']);
        } else {
            $this->insertOrderNoTypeRelation($params);
        }
    }

    /**
     * 更新发货单有效性
     * @param $deliveryId
     */
    public function updateDeliveryOrder($deliveryId)
    {
        OmsDatabase::$oms_db->update('t_delivery_order_info', array('is_valid' => 0), 'delivery_id=:delivery_id', array(':delivery_id' => $deliveryId));
    }

    /**
     * 写入发货单到数据库
     * @param $params
     */
    public function insertDeliveryOrder($params)
    {
        //发货单信息
        $delivery_arr = array(
            'delivery_order_code' => $params['orderNo'],
            'order_type' => $params['orderType'],
            'customer_id' => $params['companyId'],
            'warehouse_code' => $params['warehouseNo'],
            'deliv_create_time' => $params['orderTime'],
            'express_code' => $params['expressNo'],
            'pay_time' => $params['paidTime'],
            'shop_id' => $params['shopUid'],
            'buyer_nick' => $params['buyerInfo'],
            'logistics_code' => $params['expressCompanyCode'],
            'logistics_name' => $params['expressCompany'],
            'receiver_name' => $params['receiverName'],
            'receiver_mobile' => $params['receiverPhone'],
            'receiver_province' => $params['receiverProvince'],
            'receiver_city' => $params['receivingCity'],
            'receiver_detail_address' => $params['receivingAddress'],
            'receiver_area' => $params['receivingCounty'],
            'create_time' => date('Y-m-d H:i:s')
        );
        $delivery_id = OmsDatabase::$oms_db->insert('t_delivery_order_info', $delivery_arr);
        //商品详情
        $detailsItem = $params['detailsItem'];
        if (!empty($detailsItem)) {
            if (empty($detailsItem[0])) {
                $detailsItem = array($detailsItem);
            }
            $detail_arr = array();
            foreach ($detailsItem as $a => $b) {
                $one_arr = array(
                    'delivery_id' => $delivery_id,
                    'order_line_no' => $b['lineNo'],
                    'customer_id' => $b['companyId'],
                    'item_code' => $b['sku'],
                    'plan_qty' => $b['quantity'],
                    'retail_price' => $b['price'],
                    'inventory_type' => $b['inventoryStatus'],
                    'create_time' => date('Y-m-d H:i:s')
                );
                $detail_arr[] = $one_arr;
            }
            OmsDatabase::$oms_db->insertAll('t_delivery_order_detail', $detail_arr);
        }
    }
}