<?php
/**
 * 考拉入库单回传接口处理类
 * User: Renee
 * Date: 2018/5/10
 * Time: 13:21
 */
require_once API_ROOT . 'router/interface/erp/kaola/common/erpRequest.php';
class KlAsnBack extends erpRequest
{
    /**
     * 请求并根据返回处理数据
     * @param $params 请求数据
     * @return array  返回网易报文格式
     */
    public function back ($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputKaola(false,'执行错误：请求数据为空！');
        } else {
            try {
                //转发数据
                $rspArr = $this->send();
                if (empty($rspArr)) {
                    return $this->msgObj->outputKaola(false,'返回数据为空！');
                } else {
                    if ($rspArr['success']) {
                        $this->insertBackInfo($params);
                    }
                    return $this->msgObj->outputKaola(true,$rspArr['error_msg'],kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false,$e->getMessage());
            }
        }
    }

    /**
     * 插入回传信息
     * @param $params 请求数据
     */
    public function insertBackInfo($params)
    {
        $fWhere = "order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code";
        $fParam = array(
            ':order_no'      =>$params['inbound_id'],
            ':customer_id'   =>kaola_service::$_ownerId,
            ':warehouse_code'=>kaola_service::$_stockId,
        );
        $orderInfo = OmsDatabase::$oms_db->fetchOne('order_id','t_inbound_info',$fWhere,$fParam);

        if (isset($orderInfo['order_id'])) {
            //插入单头信息
            $addData = array(
                'order_id'=>$orderInfo['order_id'],
                'oms_order_no'=>$params['inbound_id'],
                'order_type'=>$params['type'],
                'customer_id'=>kaola_service::$_ownerId,
                'warehouse_code'=>kaola_service::$_stockId,
                'create_time'=>date("Y-m-d H:i:s")
            );
            $recordId = OmsDatabase::$oms_db->insert('t_inbound_info_record',$addData);

            if (!empty($params['items'])) {
                $itemsData = array();
                foreach ($params['items'] as $iKey=>$item) {
                    $itemsData[$iKey]['order_id'] = $orderInfo['order_id'];
                    $itemsData[$iKey]['record_id'] = $recordId;
                    $itemsData[$iKey]['sku'] = $item['sku_id'];
                    $itemsData[$iKey]['received_qty'] = $item['good_qty'];
                    $itemsData[$iKey]['create_time'] = date("Y-m-d H:i:s");
                }
                if (!empty($itemsData)) {
                    OmsDatabase::$oms_db->insertAll('t_inbound_detail_record',$itemsData);
                }
            }
        }
    }
}