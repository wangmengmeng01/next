<?php
/**
 * 仓储订单取消报文下发 Version:1.0.0 (单据取消接口)
 * Created by PhpStorm.
 * User: xl
 * Date: 2017/7/19
 * Time: 16:15
 */
require API_ROOT.'/router/interface/wms/storage/common/wmsRequest.php';

class wmsOrderCancelNotify extends wmsRequest
{

    public function cancel($param)
    {
        if(empty($param)) {
            return $this->msgObj->outputCnStorage(false,'失败：请求的数据为空','S003');
        }

        //业务类型代码
        $order_type_arr = array(
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
            '304' => 'THRK',	//归还入库单
            '904' => 'QTRK',	//其他入库单
        );


        //根据菜鸟单号，查询对应wms货主单号转发给wms
        $res = $this->judgeTable($param['orderType']);

        global $db;

        //查询对应order_id,order_no
        if ($res['table'] != 't_delivery_order_info') {
            $sql_select = "SELECT {$res['key']} AS order_id,order_no
                            FROM {$res['table']}
                            WHERE {$res['code']}='{$param['orderCode']}'
                            AND customer_id= '{$param['ownerUserId']}'
                            AND warehouse_code='{$param['storeCode']}'";
        } else {
            $sql_select = "SELECT {$res['key']} AS order_id,delivery_order_code as order_no
                            FROM {$res['table']}
                            WHERE {$res['code']}='{$param['orderCode']}'
                            AND customer_id= '{$param['ownerUserId']}'
                            AND warehouse_code='{$param['storeCode']}'";
        }

        $model = $db->prepare($sql_select);
        $model->execute();
        $orders = $model->fetch(PDO::FETCH_ASSOC);

        /*if(empty($orders)) {
            return $this->msgObj->outputCnStorage(false,"订单取消接口：该订单不存在",'S003');
        }*/

        if(!$order_type_arr[$param['orderType']]) {
            return $this->msgObj->outputCnStorage(false,"订单取消接口：请求信息订单类型错误",'S003');
        }
        //替换orderType
        $xml = '<?xml version="1.0" encoding="utf-8"?>
                <request>
                <warehouseCode>'.$param['storeCode']    .'</warehouseCode>
                <ownerCode>'    .$param['ownerUserId']  .'</ownerCode>
                <orderCode>'    .$orders['order_no']    .'</orderCode>
                <orderType>'    .$order_type_arr[$param['orderType']].'</orderType>
                </request>
                ';

        /*cn_storage_service::$_request_wms_data = $xml;*/
        cn_storage_service::$_method = 'order.cancel';

        $response = $this->send($xml, cn_storage_service::$_method);

        if($response['success']) {

            // 订单取消报文下发成功后
            // 1.根据订单类型 插入取消订单到对应数据库
            //插入数据到对应数据库
            $sql_insert = "INSERT INTO {$res['storage']}
                           (order_id,order_no,order_type,customer_id,warehouse_code,create_time)
                           VALUES (:order_id,:order_no,:order_type,:customer_id,:warehouse_code,now())";

            $model = $db->prepare($sql_insert);
            $model->bindParam(':order_id',$orders['order_id']);
            $model->bindParam(':order_no',$orders['order_no']);
            $model->bindParam(':order_type',$param['orderType']);
            $model->bindParam(':customer_id',$param['ownerUserId']);
            $model->bindParam(':warehouse_code',$param['storeCode']);
            $model->execute();


            // 2.数据库插入成功后 根据订单类型更新对应数据表中order_status字段为 WMS_ORDER_CANCELED ，is_valid=0
            //更新对应数据表中order_status,is_valid
            $sql_update = "UPDATE {$res['table']}
                           SET order_status='WMS_ORDER_CANCELED',is_valid=0
                           WHERE {$res['key']}='{$orders['order_id']}'";

            $model = $db->prepare($sql_update);
            $model->execute();


            return $this->msgObj->outputCnStorage(true,"订单取消报文下发成功",'');
        } else {
            return $this->msgObj->outputCnStorage(false,"订单取消报文下发失败 {$response['errorMsg']}",'S007');
        }
    }

    /**
     * 查询表及插入表信息处理
     * @param $order_type
     * @return array
     */
    protected function judgeTable($order_type)
    {

        $res = array();

        //待选查询表
        $table = array(
            '201' => 't_delivery_order_info',	//交易出库单（销售出库）
            '502' => 't_delivery_order_info',	//换货出库单
            '503' => 't_delivery_order_info',	//补发出库单
            '302' => 't_inbound_info',	//调拨入库单
            '306' => 't_inbound_info',	//B2B入库单
            '501' => 't_inbound_info',	//退货入库单（销退入库单)
            '504' => 't_inbound_info',	//换货入库
            '601' => 't_inbound_info',	//采购入库单
            '304' => 't_inbound_info',	//归还入库单
            '306' => 't_inbound_info',	//B2B入库单
            '904' => 't_inbound_info',	//其他入库单
            '901' => 't_outbound_info',	//退供出库单(普通出库单)
            '301' => 't_outbound_info',	//调拨出库单
            '305' => 't_outbound_info',	//B2B出库单

        );
        //对应待选表的主键
        $key = array(
            't_delivery_order_info' =>'delivery_id',
            't_outbound_info'       =>'order_id',
            't_inbound_info'        =>'order_id'
        );
        //待选需要插入数据的数据表
        $storage = array(
            't_delivery_order_info' =>'t_delivery_order_cancel',
            't_outbound_info'       =>'t_outbound_cancel',
            't_inbound_info'        =>'t_inbound_cancel'
        );

        //对应查询表条件菜鸟单号字段
        $code = array(
            't_delivery_order_info' =>'cn_order_code',
            't_outbound_info'       =>'cn_order_code',
            't_inbound_info'        =>'order_code'
        );

        //查询数据表及对应存储信息

        $res['table']   = $table[$order_type];
        $res['key']     = $key[$res['table']];
        $res['storage'] = $storage[$res['table']];
        $res['code']    = $code[$res['table']];

        return $res;
    }
}