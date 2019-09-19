<?php
/**
 * Notes:贝贝入库单创建操作类
 * Date: 2019/6/13
 * Time: 10:41
 */
require API_ROOT . 'router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsEntryOrderCreate extends wmsRequest
{
    /**
     * 创建入库单
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

        //判断入库单是否存在
        $checkRs = $this->check_inbound($params);
        //开启事物
        OmsDatabase::$oms_db->getPdo()->beginTransaction();
        if (empty($checkRs)) {
            //记录单号和单据类型的关系
            $this->insertOrderNoTypeRelation($params);
            //写入单据数据
            $this->insert_entry_order($params);
        } else {
            //更新单号和单据类型的关系
            $this->updateOrderNoTypeRelation($params);
            //将入库单设为无效
            $this->update_inbound($checkRs['order_id']);
            //插入新的入库单
            $this->insert_entry_order($params);
        }
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
     * Notes:更新订单号和订单类型关系数据
     * Date: 2019/6/16
     * Time: 22:07
     * @param $params 请求报文
     */
    public function updateOrderNoTypeRelation($params)
    {
        $update_arr = array(
            'order_type' => $params['billType']
        );
        OmsDatabase::$oms_db->update('t_orderno_type_relation', $update_arr, 'order_no=:order_no AND customer_id=:customer_id AND warehouse_code=:warehouse_code', array(':order_no' => $params['billId'], ':customer_id' => $params['company'], ':warehouse_code' => $params['warehouse']));
    }
    /***
     * Notes:写入订单号和订单类型关系数据
     * Date: 2019/6/17
     * Time: 0:05
     * @param $params 请求报文
     */
    public function insertOrderNoTypeRelation($params)
    {
        $insert_arr = array(
            'order_no' => $params['billId'],
            'order_type' => $params['billType'],
            'customer_id' => $params['company'],
            'warehouse_code' => $params['warehouse'],
            'create_time' => date('Y-m-d H:i:s'),
        );
        OmsDatabase::$oms_db->insert('t_orderno_type_relation', $insert_arr);
    }
    /***
     * Notes:写入订单数据到数据库
     * Date: 2019/6/17
     * Time: 0:06
     * @param $params 请求报文
     */
    public function insert_entry_order($params)
    {
        //商品信息
        $details = $params['details'];
        //写入入库单单头信息
        $inbound_arr = array(
            'order_no' => $params['billId'],
            'order_type' => $params['billType'],
            'customer_id' => $params['company'],
            'warehouse_code' => $params['warehouse'],
            'order_create_time' => $params['opTime'],
            'create_time' => date('Y-m-d H:i:s')
        );
        $order_id = OmsDatabase::$oms_db->insert('t_inbound_info', $inbound_arr);
        //商品详情
        if (!empty($details)) {
            if (empty($details[0])) {
                $details = array($details);
            }
            $detail_arr = array();
            foreach ($details as $k => $v) {
                $detail_arr[] = array(
                    'line_no' => $v['lineNo'],
                    'sku' => $v['sku'],
                    'expected_qty' => $v['quantity'],
                    'inventory_type' => $v['inventoryStatus'],
                    'order_id' => $order_id,
                    'customer_id' => $params['company'],
                    'create_time' => date('Y-m-d H:i:s')
                );
            }
            OmsDatabase::$oms_db->insertAll('t_inbound_detail', $detail_arr);
        }
    }

    /***
     * Notes: 判断是否存在入库单号
     * Date: 2019/7/25
     * Time: 16:40
     * @param $params
     * @return mixed
     */
    public function check_inbound($params)
    {
        return OmsDatabase::$oms_db->fetchOne('order_id', 't_inbound_info', 'order_no = :order_no AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1', array(':order_no' => $params['billId'], ':customer_id' => $params['company'], ':warehouse_code' => $params['warehouse']));
    }
    /***
     * Notes: 更新入库单及明细
     * Date: 2019/6/17
     * Time: 0:06
     * @param $order_id 主键
     */
    public function update_inbound($order_id)
    {
        //更新入库单有效性
        OmsDatabase::$oms_db->update('t_inbound_info', array('is_valid' => 0), 'order_id=:order_id', array(':order_id' => $order_id));
        //更新出库单明细有效性
        OmsDatabase::$oms_db->update('t_inbound_detail', array('is_valid' => 0), 'order_id=:order_id', array(':order_id' => $order_id));
    }
}

