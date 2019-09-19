<?php
/**
 * 考拉出库单取消接口处理类
 * User: Renee
 * Date: 2018/5/16
 * Time: 11:10
 */
require API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';
class KlSoCancel extends wmsRequest
{
    /**
     * 请求并根据返回处理数据
     * @param $params 请求数据
     * @return array  返回网易报文格式
     */
    public function cancel($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputKaola(false, '执行错误：请求数据为空！');
        } else {
            try {
                $resp = $this->send();
                if (empty($resp)) {
                    return $this->msgObj->outputKaola(false, '返回数据为空！');
                } else {
                    //wms响应成功作出相关处理
                    if ($resp['success']) {
                        $this->insertCancelInfo($params);
                    }
                    return $this->msgObj->outputKaola($resp['success'], $resp['error_msg'], kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false, $e->getMessage());
            }
        }
    }

    /**
     * 更新出库单状态以及插入订单取消记录
     * @param 请求数据
     */
    public function insertCancelInfo($params)
    {
        //更新出库单状态为取消并且设为无效
        $where = "order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=:is_validw";
        $wParams = array(
            ':order_no'      =>$params['outbound_id'],
            ':customer_id'   =>kaola_service::$_ownerId,
            ':warehouse_code'=>kaola_service::$_stockId,
            ':is_validw'      =>1
        );
        $uData = array(
            'order_status'=>'90',
            'is_valid'=>0
        );
        OmsDatabase::$oms_db->update('t_outbound_info',$uData,$where,$wParams);

        //插入出库单取消记录
        $addData = array(
            'order_no'=>$params['outbound_id'],
            'order_type'=>$params['type']
        );
        OmsDatabase::$oms_db->insert('t_outbound_cancel',$addData);
    }
}