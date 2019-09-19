<?php
/**
 * WMS订单状态回传接口 Version:1.0.0
 * Created by PhpStorm.
 * User: xl
 * Date: 2017/7/19
 * Time: 15:28
 */

require API_ROOT . '/router/interface/erp/storage/common/cnRequest.php';

class erpOrderStatusUpload extends cnRequest
{

    /**
     * 订单状态回传
     * @param $param
     * @return array
     */
    public function upload($param, $request)
    {

        require_once API_ROOT . '/router/format/storage/common/v1.0.php';
        $format = new format();
        $param = $format->request($param);

        if (empty($param)) {
            return $this->msgObj->outputCnStorage(false, '失败：请求的数据为空', 'S003');
        }

        //业务类型代码
        /*$order_type_arr = array(
            '201' => 'JYCK',	//交易出库单（销售出库）
            '502' => 'HHCK',	//换货出库单
            '503' => 'BFCK',	//补发出库单
            '302' => 'DBRK',	//调拨入库单
            '501' => 'XTRK',	//退货入库单（销退入库单)
            '504' => 'HHRK',	//换货入库
            '601' => 'CGRK',	//采购入库单
            '901' => 'PTCK',	//退供出库单(普通出库单)
            '301' => 'DBCK',	//调拨出库单
            '305' => 'B2BCK',	//B2B出库单
            '306' => 'B2BRK',	//B2B入库单
        );


        $param['orderType'] = $order_type_arr[$param['orderType']];*/

//        $order_type_arr = array(201, 502, 503, 302, 501, 504, 601, 901, 301, 305, 306);
        $order_type_arr = array(201, 202, 301, 302, 303, 305, 306, 501, 502, 503, 504, 601, 901, 903);

        //接收到的orderType不是菜鸟所需数字类型的话，查询对应数据的orderType
        if (!in_array($param['orderType'], $order_type_arr)) {
            $order_type = $this->checkInboundInfo($param);

            /*if (empty($order_type)) {
                return $this->msgObj->outputCnStorage(false, "订单流水通知接口:该入库单不存在", 'S003');
            }*/
            $param['orderType'] = $order_type['order_type'];
            //替换orderType
            $replace = "<orderType>" . $param['orderType'] . "</orderType>";

            $data = preg_replace("/<orderType>(.*)<\/orderType>/s", $replace, cn_storage_service::$_data);

        } else {
            $data = cn_storage_service::$_data;
        }

        $response = $this->send($data);


        if ($response['success']) {

            $warehouse_code = cn_storage_service::$_warehouseid;

            //回传成功，插入数据到 订单流水通知表 t_order_process_record
            global $db;
            $sql = "INSERT INTO `t_order_process_record`
                      (
                          `cn_order_code`, `order_type`, `warehouse_code`,
                          `process_status`, `operator_name`, `operate_time`,
                          `operate_info`, `remark`, `extend_props`, `create_time`
                      )
                      VALUES
                      (
                           '{$param['orderCode']}', '{$param['orderType']}','{$warehouse_code}',
                           '{$param['status']}','{$param['operator']}','{$param['operateDate']}',
                           '{$param['content']}','{$param['remark']}','{$param['features']}',now()
                      )";

            $model = $db->prepare($sql);
            $model->execute();

            if ($db->lastInsertId()) {
                return $this->msgObj->outputCnStorage(true, "订单流水通知接口处理成功", '');
            } else {
                return $this->msgObj->outputCnStorage(false, $response['errorMsg'], $response['errorCode']);
            }

        } else {
            return $this->msgObj->outputCnStorage(false, "订单流水通知接口处理失败".$response['errorMsg'], $response['errorCode']);
        }

    }

    /**
     * 检查入库单是否已存在，并获取order_type
     * @param $param
     * @return mixed
     */
    protected function checkInboundInfo($param)
    {

        //选取查询的对应数据表
        $info = $this->judgementTable($param['orderType']);

        if (!is_array($info)) {
            return false;
        }

        global $db;

        $sql = "SELECT order_type FROM {$info['table']} WHERE {$info['order_no']} = :order_no AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1;";

        $model = $db->prepare($sql);

        $model->bindParam(':order_no', $param['orderCode']);
        $model->bindParam(':customer_id', cn_storage_service::$_customerid);
        $model->bindParam(':warehouse_code', cn_storage_service::$_warehouseid);
        $model->execute();

        $res = $model->fetch(PDO::FETCH_ASSOC);

        return $res;

    }

    /**
     * 判断选择对应查询数据表
     * @param $order_type
     * @return array|string
     */
    protected function judgementTable($order_type)
    {

        $table_name = '';
        $order_num = 'cn_order_code';
        //待选查询表
        $table = array(
            'JYCK' => 't_delivery_order_info',    // 一般交易出库单（销售出库)201
            'B2BCK' => 't_delivery_order_info',    // B2B交易出库单202
            'DBCK' => 't_outbound_info',    // 调拨出库单301
            'DBRK' => 't_inbound_info',    // 调拨入库单302
            'B2BCK' => 't_outbound_info',    // 领用出库单303
            'B2BCK' => 't_outbound_info',    // B2B出库单305
            'B2BRK' => 't_inbound_info',    // B2B入库单306
            'XTRK' => 't_inbound_info',    // 退货入库单（销退入库单 ）501
            'HHCK' => 't_delivery_order_info',    // 换货出库单502
            'BFCK' => 't_delivery_order_info',    // 补发出库单503
            'HHRK' => 't_inbound_info',    //  换货入库504
            'CGRK' => 't_inbound_info',    // 采购入库单601
            'PTCK' => 't_outbound_info',    // 普通出库单(如货主拉走一部分货 901
            'QTCK' => 't_outbound_info',    // 其他出库单903
        );

        if (isset($table[$order_type])) {
            $table_name = $table[$order_type];
        }

        //对应查询表条件字段
        if ($table_name == 't_inbound_info') {
            $order_num = 'order_code';
        }

        if ($table_name == '') {
            return 'string';
        }

        return array(
            'table' => $table_name,
            'order_no' => $order_num
        );
    }

}