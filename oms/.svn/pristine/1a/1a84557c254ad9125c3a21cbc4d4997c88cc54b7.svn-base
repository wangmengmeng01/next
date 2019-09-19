<?php
/**
 * Notes: 贝贝销售退货下传操作类
 * Date: 2019/6/17
 * Time: 22:38
 */
require API_ROOT . 'router/interface/wms/beibei/YWMS/wmsRequest.php';
class wmsRmaCreate extends wmsRequest
{
    /**
     * 创建退货入库单
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

            $this->insert_return_order($params);
            //事务提交
            OmsDatabase::$oms_db->getPdo()->commit();
            return $this->msgObj->outputBeibei(true, $response['data'], $response['message'], $response['addon']);
        } catch (PDOException $p) {
            # 事务回滚
            OmsDatabase::$oms_db->getPdo()->rollBack();

            return $this->msgObj->outputBeibei(false,'', $p->getMessage());
        } catch (Exception $e) {
            return $this->msgObj->outputBeibei(false, '', $e->getMessage());
        }
    }
    /***
     * Notes:添加退货入库单信息
     * Date: 2019/6/18
     * Time: 15:14
     * @param $params
     */
    public function insert_return_order($params){
        $inbound_info  = $params['header'];
        //判断是否存在退货入库单号，存在则逻辑删除处理
        $this->has_upd_return_order($inbound_info['returnId'], $inbound_info['orderType'],$inbound_info['company'], $inbound_info['refundWarehouse']);
        //写入入库单
        $inbound_arr = array(
            'order_no' => $inbound_info['returnId'],
            'order_type' => $inbound_info['orderType'],
            'customer_id' => $inbound_info['company'],
            'order_create_time' => $inbound_info['createTime'],
            'sender_mobile' => $inbound_info['senderPhone'],
            'pono' => $inbound_info['shippingNo'],//erp分配？？
            'user_define1' => $inbound_info['refundExpressCompany'],
            'warehouse_code' => $inbound_info['refundWarehouse'],
            'user_define5' => $inbound_info['refundFlag'],
            'asn_reference4' => $inbound_info['shopUID'], //店铺UID
            'user_define2' => $inbound_info['refundExpressNo'],
            'create_time' => date('Y-m-d H:i:s')
        );
        $order_id = OmsDatabase::$oms_db->insert('t_inbound_info', $inbound_arr);
        //写入入库单明细
        $detail_info = $params['items'];
        if (!empty($detail_info)) {
            if (empty($detail_info[0])) {
                $detail_info = array($detail_info);
            }
            $detail_arr = array();
            foreach ($detail_info as $k => $v) {
                $detail_arr[] = array(
                    'order_id' => $order_id,
                    'customer_id' => $v['company'],
                    'sku' => $v['sku'],
                    'expected_qty' => $v['quantity'],
                    'total_price' => $v['returnAmount'],//申请售后退款
                    'remark' => $v['refundReason'],//退货原因
                    'create_time' => date('Y-m-d H:i:s')
                );
            }
            OmsDatabase::$oms_db->insertAll('t_inbound_detail', $detail_arr);
        }
    }
    /***
     * Notes:判断是否存在退货入库单号，存在则逻辑删除处理
     * Date: 2019/6/18
     * Time: 15:23
     * @param $orderNo 订单号
     * @param $orderType 订单类型
     * @param $customerId 货主编码
     * @param $warehouseCode 仓库编码
     */
    public function has_upd_return_order($orderNo, $orderType,$customerId,$warehouseCode)
    {
        $rs =  OmsDatabase::$oms_db->fetchOne('order_id', 't_inbound_info', 'order_no = :order_no AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1', array(':order_no' => $orderNo, ':customer_id' => $customerId, ':warehouse_code' => $warehouseCode));
        $param = array(
            'order_no'      =>$orderNo,
            'order_type'    =>$orderType,
            'customer_id' =>$customerId,
            'warehouse_code'=>$warehouseCode,
        );
        if (!empty($rs)) {
            //更新单号和单据类型的关系
            $this->updateOrderNoTypeRelation($param);
            OmsDatabase::$oms_db->update('t_inbound_info', array('is_valid' => 0), 'order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code ', array(':order_no' => $orderNo,':customer_id' => $customerId,':warehouse_code' => $warehouseCode));
            OmsDatabase::$oms_db->update('t_inbound_detail', array('is_valid' => 0), 'order_id= :order_id', array(':order_id' => $rs['order_id']));
        } else {
            //记录单号和单据类型的关系
            $this->insertOrderNoTypeRelation($param);
        }
    }
    /***
     * Notes:更新订单号和订单类型关系数据
     * Date: 2019/6/18
     * Time: 14:31
     * @param $params
     */
    public function updateOrderNoTypeRelation($params)
    {
        OmsDatabase::$oms_db->update('t_orderno_type_relation', array('order_type' => $params['order_type']), 'order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code ', array(':order_no' => $params['order_no'],':customer_id' => $params['customer_id'],':warehouse_code' => $params['warehouse_code']));
    }
    /***
     * Notes:写入订单号和订单类型关系数据
     * Date: 2019/6/18
     * Time: 14:31
     * @param $params
     */
    public function insertOrderNoTypeRelation($params){
        $insert_arr = array(
            'order_no' => $params['order_no'],
            'order_type' => $params['order_type'],
            'customer_id' => $params['customer_id'],
            'warehouse_code' => $params['warehouse_code'],
            'create_time' => date('Y-m-d H:i:s')
        );
        OmsDatabase::$oms_db->insert('t_orderno_type_relation',$insert_arr);
    }
}

