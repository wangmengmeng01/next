<?php
/**
 * 奇门发货单波次通知接口处理类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpWaveNumReport extends erpRequest{
    public function report($params) {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure','失败：请求的数据为空','S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    global $db;
                    $columnDeliveryWaveArr = $this->get_dataBase_relation('delivery_wave_info');
                    $column_key_wave = implode(',', array_values($columnDeliveryWaveArr)) . ',create_time';
                    $column_value_wave = ':' . implode(',:', array_values($columnDeliveryWaveArr)) . ',now()';
                    $sql = "INSERT INTO t_delivery_order_wave_info({$column_key_wave}) VALUES({$column_value_wave})";
                    $model = $db->prepare($sql);
                    $values = array();
                    if (empty($params['orders']['order'][0])) {
                        $params['orders']['order'] = array($params['orders']['order']);
                    }
                    foreach ($params['orders']['order'] as $o_v) {
                        foreach ($columnDeliveryWaveArr as $w_k=>$w_v) {
                            $values[':'.$w_v] = empty($o_v[$w_k]) ? '' : $o_v[$w_k];
                        }
                        $values[':wave_num'] = $params['waveNum'];
                        $values[':customer_id'] = qimen_service::$_customerId;
                        $model->execute($values);
                    }
                    return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                } else {
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', '失败：wms接口调用失败', 'S007');
            }
        }
    }
}
?>