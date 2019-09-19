<?php
/**
 * 订单取消接口处理类
 * User: Renee
 * Date: 2018/1/18
 * Time: 19:39
 */
require API_ROOT . 'router/interface/wms/custom/common/wmsRequest.php';
class wmsKjSoOrderCancel extends wmsRequest {
    public function cancel($params){
        if (empty($params)) {
            return $this->msgObj->outputCustom('false', '失败：请求的数据为空！');
        } else {
            try {
                $response = $this->send();
                if (!empty($response)) {
                    if ($response['success'] == 'true') {
                        $this->updateOrderInfo($params);
                        return $this->msgObj->outputCustom('true', $response['reasons'],$response['addon']);
                    } else {
                        return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                    }
                } else {
                    return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                }
            } catch (Exception $e){
                return $this->msgObj->outputCustom('false', $e->getMessage());
            }
        }
    }

    /**
     * 更新发货单状态为已取消
     * @param $params
     * @return bool
     */
    public function updateOrderInfo($params){
        global $db;

        $sql = "UPDATE t_delivery_order_info 
                SET order_status = 'CANCELED',is_valid=0 
                WHERE delivery_order_code=:delivery_order_code AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND order_flag='kj'";
        $model = $db->prepare($sql);
        $model->bindParam(':delivery_order_code',$params['externalNo']);
        $model->bindParam(':customer_id',$params['storer']);
        $model->bindParam(':warehouse_code',$params['wmwhseid']);
        $model->execute();
        return true;
    }
}