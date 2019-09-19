<?php
/**
 * 考拉出库单推送接口(新)接口处理类
 * User: Renee
 * Date: 2018/5/15
 * Time: 20:04
 */
require API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';
class KlSoCreate extends wmsRequest
{
    public function create ($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputKaola(false,'执行错误：请求数据为空！');
        } else {
            try {
                $resp = $this->send();
                if (empty($resp)) {
                    return $this->msgObj->outputKaola(false,'返回数据为空！');
                } else {
                    if ($resp['success']) {
                        //添加或更新出库单
                        $this->insertSoInfo($params);
                    }
                    return $this->msgObj->outputKaola($resp['success'],$resp['error_msg'],kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false,$e->getMessage());
            }
        }
    }

    /**
     * 插入出库单单头及明细
     * @param $params
     */
    public function insertSoInfo($params)
    {
        $where = "order_no=:order_no AND customer_id=:customer_id AND is_valid=:is_valid";
        $whParams = array(
            ':order_no'   =>$params['outbound_id'],
            ':customer_id'=>kaola_service::$_ownerId,
            ':is_valid'   =>1
        );
        $soInfo = OmsDatabase::$oms_db->fetchOne('order_id','t_outbound_info',$where,$whParams);

        if (empty($soInfo)) {
            //写入订单头信息
            $soParams = array(
                'order_no'      => $params['outbound_id'],
                'order_type'    => $params['type'],
                'customer_id'   => kaola_service::$_ownerId,
                'warehouse_code'=> kaola_service::$_stockId,
                'consignee_name'=> $params['contacts'],
                'c_tel1'=> $params['phone'],
                'c_address1'=> $params['address'],
                'create_time'   => date("Y-m-d H:i:s")
            );
            $orderId = OmsDatabase::$oms_db->insert('t_outbound_info',$soParams);

            //写入订单明细（商品信息）
            if (!empty($params['items'])) {
                $itemsParams = array();
                foreach ($params['items'] as $iKey=>$item) {
                    $itemsParams[$iKey]['order_id'] = $orderId;
                    $itemsParams[$iKey]['customer_id'] = kaola_service::$_ownerId;
                    $itemsParams[$iKey]['sku'] = $item['sku_id'];
                    $itemsParams[$iKey]['qty_ordered'] = isset($item['good_qty']) ? $item['good_qty'] : '';
                    $itemsParams[$iKey]['item_name'] = isset($item['sku_desc']) ? $item['sku_desc'] : '';
                    $itemsParams[$iKey]['create_time'] = date("Y-m-d H:i:s");
                }
                OmsDatabase::$oms_db->insertAll('t_outbound_detail',$itemsParams);
            }
        }
    }
}