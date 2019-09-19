<?php
/**
 * 考拉入库单推送接口(新)接口处理类
 * User: Renee
 * Date: 2018/5/10
 * Time: 13:25
 */

require API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';
class KlAsnCreate extends wmsRequest
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
                        //添加或更新入库单
                        $this->insertAsnInfo($params);
                    }
                    return $this->msgObj->outputKaola($resp['success'],$resp['error_msg'],kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false,$e->getMessage());
            }
        }
    }

    /**
     * 插入入库单单头及明细
     * @param $params
     */
    public function insertAsnInfo($params)
    {
        $where = "order_no=:order_no AND customer_id=:customer_id AND is_valid=:is_valid";
        $whParams = array(
            ':order_no'   =>$params['inbound_id'],
            ':customer_id'=>kaola_service::$_ownerId,
            ':is_valid'   =>1,
        );
        $asnInfo = OmsDatabase::$oms_db->fetchOne('order_id','t_inbound_info',$where,$whParams);

        if (empty($asnInfo)) {
            //写入订单头信息
            $asnParams = array(
                'order_no'      => $params['inbound_id'],
                'pono'          => isset($params['outbound_id']) ? $params['outbound_id'] : '',
                'order_type'    => $params['type'],
                'customer_id'   => kaola_service::$_ownerId,
                'warehouse_code'=> kaola_service::$_stockId,
                'create_time'   => date("Y-m-d H:i:s")
            );
            $orderId = OmsDatabase::$oms_db->insert('t_inbound_info',$asnParams);

            //写入订单明细（商品信息）
            if (!empty($params['items'])) {
                $itemsParams = array();
                foreach ($params['items'] as $iKey=>$item) {
                    $itemsParams[$iKey]['order_id'] = $orderId;
                    $itemsParams[$iKey]['customer_id'] = kaola_service::$_ownerId;
                    $itemsParams[$iKey]['sku'] = $item['sku_id'];
                    $itemsParams[$iKey]['expected_qty'] = isset($item['good_qty']) ? $item['good_qty'] : '';
                    $itemsParams[$iKey]['item_name'] = isset($item['sku_desc']) ? $item['sku_desc'] : '';
                    $itemsParams[$iKey]['create_time'] = date("Y-m-d H:i:s");
                }
                OmsDatabase::$oms_db->insertAll('t_inbound_detail',$itemsParams);
            }
        }
    }
}