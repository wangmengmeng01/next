<?php
/**
 * 跨境出库单下发接口处理类
 * User: Renee
 * Date: 2018/1/17
 * Time: 10:54
 */
require API_ROOT . 'router/interface/wms/custom/common/wmsRequest.php';
class wmsKjSoOrder extends wmsRequest {
    public function create($params){
        if (empty($params)) {
            return $this->msgObj->outputCustom('false', '失败：请求的数据为空!');
        } else {
            try {
                $response = $this->send();
                if (!empty($response)) {
                    if ($response['success'] == 'true') {
                        if ($this->checkOrder($params)) {
                            $this->updateOrder($params);
                            $this->insertOrder($params);
                        } else {
                            $this->insertOrder($params);
                        }
                        return $this->msgObj->outputCustom('true', $response['reasons'],$response['addon']);
                    } else {
                        return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                    }
                } else {
                    return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputCustom('false', $e->getMessage());
            }
        }
    }

    /**
     * 判断订单是否存在
     * @param  请求数据
     * @return 单据id
     */
    public function checkOrder($params){
        global $db;

        $sql = 'SELECT delivery_id
                FROM t_delivery_order_info 
                WHERE delivery_order_code=:delivery_order_code AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1';
        $model = $db->prepare($sql);
        $model->bindParam(':delivery_order_code', $params['externalNo']);
        $model->bindParam(':customer_id', $params['storer']);
        $model->bindParam(':warehouse_code', $params['wmwhseid']);
        $model->execute();
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        return $rs;
    }

    /**
     * 插入单据信息
     * @param $params
     * @return bool
     */
    public function insertOrder($params){
        global $db;

        $orderInfoRelt = $this->getDbRelation('cnec_wh_6');
        $column_key_orderInfo = implode(',', array_values($orderInfoRelt)) . ',create_time';

        $orderInfoDtRelt = $this->getDbRelation('cnec_wh_6_item');
        $column_key_orderInfoDt = implode(',', array_values($orderInfoDtRelt)) . ',create_time';

        if (!empty($params)) {
            $column_value_orderInfo = ":" . implode(",:", array_values($orderInfoRelt)). ',now()';
            $orderSql = "INSERT INTO t_delivery_order_info(".$column_key_orderInfo.") VALUES(" . $column_value_orderInfo . ")";

            $orderModel = $db->prepare($orderSql);
            $values = array();
            foreach ($orderInfoRelt as $order_k=>$order_v) {
                $values[':'.$order_v] = empty($params[$order_k]) ? '' : $params[$order_k];
            }
            $values[':order_flag'] = 'kj';
            $orderModel->execute($values);
            $orderId = $db->lastInsertId();

            if (empty($params['item'][0])) {
                $params['item'] = array($params['item']);
            }
            foreach ($params['item'] as $item) {
                $column_value_orderInfoDt = ":" . implode(",:", array_values($orderInfoDtRelt)). ',now()';
                $detailSql = "INSERT INTO t_delivery_order_detail(".$column_key_orderInfoDt.") VALUES(".$column_value_orderInfoDt.")";
                $dModel = $db->prepare($detailSql);
                $dValues = array();
                foreach ($orderInfoDtRelt as $d_k=>$d_v) {
                    $dValues[':'.$d_v] = empty($item[$d_k]) ? '' : $item[$d_k];
                }
                $dValues[':delivery_id'] = $orderId;
                $dModel->execute($dValues);
            }
        }
        return true;
    }

    /**
     * 更新单据信息
     * @param $params
     * @return bool
     */
    public function updateOrder($params){
        global $db;

        $sql = "UPDATE t_delivery_order_info SET is_valid=0 WHERE delivery_order_code=:delivery_order_code AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':delivery_order_code',$params['externalNo']);
        $model->bindParam(':customer_id',$params['storer']);
        $model->bindParam(':warehouse_code',$params['wmwhseid']);
        $model->execute();
        return true;
    }
}