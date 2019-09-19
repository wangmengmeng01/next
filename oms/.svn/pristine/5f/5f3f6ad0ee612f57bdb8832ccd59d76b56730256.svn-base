<?php
/**
 * Notes: 贝贝天舟-入库单更新接口
 * Date: 2019/7/25
 * Time: 11:23
 */
require API_ROOT . 'router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsEntryorderUpdate extends wmsRequest
{
    /**
     * 入库单更新
     * @param $params
     * @return array
     */
    public function update($params)
    {
        try {
            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }
            # 转发数据给wms
            $response = $this->send();

            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '', 'wms接口调用失败');
            }

            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }
           # 判断入库单是否存在
            $checkRs = $this->check_inbound($params);
            if ($checkRs) {
                # 更新入库单
                $this->update_entry_order($checkRs['order_id'],$params);
            }
            return $this->msgObj->outputBeibei(true, $response['data'], $response['message'], $response['addon']);
        } catch (PDOException $p) {
            return $this->msgObj->outputBeibei(false,'', $p->getMessage());
        } catch (Exception $e) {
            return $this->msgObj->outputBeibei(false, '', $e->getMessage());
        }
    }
    /***
     * Notes: 更新入库单
     * Date: 2019/7/25
     * Time: 17:40
     * @param $order_id 入库单id
     * @param $params 请求参数
     */
    public function update_entry_order($order_id,$params)
    {
        $update_arr = array(
            'expect_end_time' => $params['updateFiled']['subscribeArrivalTime'],
            'remark' => $params['comment']
        );
        OmsDatabase::$oms_db->update('t_inbound_info',$update_arr, 'order_id=:order_id', array(':order_id' => $order_id));
    }
    /***
     * Notes: 判断是否存在入库单号
     * Date: 2019/7/25
     * Time: 16:41
     * @param $params
     * @return mixed
     */
    public function check_inbound($params)
    {
        return OmsDatabase::$oms_db->fetchOne('order_id', 't_inbound_info', 'order_no = :order_no AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1', array(':order_no' => $params['billId'], ':customer_id' => beibei_service::$_customerId, ':warehouse_code' => $params['warehouse']));
    }
}

