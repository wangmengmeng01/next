<?php
require './cainiao_wms_base.php';
include_once '../../../../config.php';

class cainiaoWms extends cainiao_wms_base
{
    /*
     * 判断用户是否存在 (菜鸟请求oms)
     */
    public function isExist ($customer_id)
    {
        //判断合作方编码是否存在
        $field   = '*';
        $table   = 't_qimen_customer';
        $where   = 'customer_id=:customer_id';
        $params  = array(':customer_id' => $customer_id);
        return OmsDatabase::$oms_db->fetchOne($field , $table , $where , $params);
    }

    /*
     * 查询qimen_customer_id ，请求菜鸟时作为logistic_provider_id参数的值
     */
    public function findQimenCustomerId($warehouse_code)
    {
        $field   = 'qimen_customer_id';
        $table   = 't_qimen_customer_bind';
        $where   = 'warehouse_code=:warehouse_code';
        $params  = array(':warehouse_code' => $warehouse_code);
        return OmsDatabase::$oms_db->fetchOne($field , $table , $where , $params);
    }

    /*
     * 菜鸟WMS出库单创建接口 CN_WMS_STOCKOUT_CREATE (自测完)
     */
    public function stockOutCreate($data) 
    {
        $info = $data['deliveryOrder'];
        //如果韵达wms返回成功将数据存至t_outbound_info和t_outbound_detail
        $outbound_info = array(
            'order_no'               => $info['deliveryOrderCode'],
            'cn_order_code'          => $info['cnOrderCode'],
            'order_type'             => $info['orderType'],
            'customer_id'            => $info['ownerCode'],
            'warehouse_code'         => $info['warehouseCode'],
            'order_time'             => isset($info['createTime']) ? $info['createTime'] : '',
            'required_delivery_time' => isset($info['scheduleDate']) ? $info['scheduleDate'] : '',
            'carrier_id'             => isset($info['logisticsCode']) ? $info['logisticsCode'] : '',
            'carrier_name'           => isset($info['logisticsName']) ? $info['logisticsName'] : '',
            'supplier_code'          => isset($info['supplierCode']) ? $info['supplierCode'] : '',
            'supplier_name'          => isset($info['supplierName']) ? $info['supplierName'] : '',
            'picker_company'         => isset($info['pickerInfo']['company']) ? $info['pickerInfo']['company'] : '',
            'picker_name'            => isset($info['pickerInfo']['name']) ? $info['pickerInfo']['name'] : '',
            'picker_tel'             => isset($info['pickerInfo']['tel']) ? $info['pickerInfo']['tel'] : '',
            'picker_mobile'          => isset($info['pickerInfo']['mobile']) ? $info['pickerInfo']['mobile'] : '',
            'picker_id'              => isset($info['pickerInfo']['id']) ? $info['pickerInfo']['id'] : '',
            'picker_car_no'          => isset($info['pickerInfo']['carNo']) ? $info['pickerInfo']['carNo'] : '',
            'sender_company'         => isset($info['senderInfo']['company']) ? $info['senderInfo']['company'] : '',
            'sender_name'            => isset($info['senderInfo']['name']) ? $info['senderInfo']['name'] : '',
            'sender_zip_code'        => isset($info['senderInfo']['zipCode']) ? $info['senderInfo']['zipCode'] : '',
            'sender_tel'             => isset($info['senderInfo']['tel']) ? $info['senderInfo']['tel'] : '',
            'sender_mobile'          => isset($info['senderInfo']['mobile']) ? $info['senderInfo']['mobile'] : '',
            'sender_country_code'    => isset($info['senderInfo']['countryCode']) ? $info['senderInfo']['countryCode'] : '',
            'sender_email'           => isset($info['senderInfo']['email']) ? $info['senderInfo']['email'] : '',
            'sender_province'        => isset($info['senderInfo']['province']) ? $info['senderInfo']['province'] : '',
            'sender_city'            => isset($info['senderInfo']['city']) ? $info['senderInfo']['city'] : '',
            'sender_area'            => isset($info['senderInfo']['area']) ? $info['senderInfo']['area'] : '',
            'sender_town'            => isset($info['senderInfo']['town']) ? $info['senderInfo']['town'] : '',
            'sender_detail_address'  => isset($info['senderInfo']['detailAddress']) ? $info['senderInfo']['detailAddress'] : '',
            'sender_id'              => isset($info['senderInfo']['idNumber']) ? $info['senderInfo']['idNumber'] : '',
            'consignee_name'         => isset($info['receiverInfo']['name']) ? $info['receiverInfo']['name'] : '',
            'c_province'             => isset($info['receiverInfo']['province']) ? $info['receiverInfo']['province'] : '',
            'c_city'                 => isset($info['receiverInfo']['city']) ? $info['receiverInfo']['city'] : '',
            'c_address1'             => isset($info['receiverInfo']['town']) ? $info['receiverInfo']['town'] : '',
            'c_address2'             => isset($info['receiverInfo']['area']) ? $info['receiverInfo']['area'] : '',
            'c_address3'             => isset($info['receiverInfo']['detailAddress']) ? $info['receiverInfo']['detailAddress'] : '',
            'c_address3'             => isset($info['receiverInfo']['detailAddress']) ? $info['receiverInfo']['detailAddress'] : '',
            'remark'                 => isset($info['remark']) ?  $info['remark'] : '',
            'create_time'            => date('Y-m-d H:i:s')
        );

        $order_id         = OmsDatabase::$oms_db->insert('t_outbound_info' , $outbound_info);
        $outbound_details = array();
        foreach ($data['orderLines']['orderLine'] as $val) {
            $outbound_detail  = array(
                'order_id'       => $order_id,
                'line_no'        => $val['orderLineNo'],
                'customer_id'    => $info['ownerCode'],
                'sku'            => $val['itemCode'],
                'qty_ordered'    => $val['planQty'],
                'cn_item_code'   => $val['itemCode'],
                'item_name'      => isset($val['itemName']) ? $val['itemName'] : '',
                'inventory_type' => isset($val['inventoryType']) ? $val['inventoryType'] : '',
                'batch_code'     => isset($val['batchCode']) ? $val['batchCode'] : '',
                'product_date'   => isset($val['productDate']) ? $val['productDate'] : '',
                'expire_date'    => isset($val['expireDate']) ? $val['expireDate'] : '',
                'produce_code'   => isset($val['produceCode']) ? $val['produceCode'] : '',
                'create_time'    => date('Y-m-d H:i:s')
            );
            array_push($outbound_details , $outbound_detail);
        }
        
        OmsDatabase::$oms_db->insertAll('t_outbound_detail' , $outbound_details);

        return $this->sendSucc();
    }
    
    /*
     * 退货入库单创建接口CN_WMS_RETURNORDER_CREATE
     */
    public function returnOrderCreate ($data)
    {
        $info = $data['returnOrder'];
        $inbound_info = array(
            'order_no'              => isset($info['returnOrderCode']) ? $info['returnOrderCode'] : '',
            'order_type'            => isset($info['orderType']) ? $info['orderType'] : '',
            'customer_id'           => isset($info['ownerCode']) ? $info['ownerCode'] : '',
            'warehouse_code'        => isset($info['warehouseCode']) ? $info['warehouseCode'] : '',
            'order_code'            => isset($info['returnOrderCode']) ? $info['returnOrderCode'] : '',
            'asn_reference2'        => isset($info['expressCode']) ? $info['expressCode'] : '',
            'asn_reference3'        => isset($info['cnOrderCode']) ? $info['cnOrderCode'] : '',
            'pono'                  => isset($info['preDeliveryOrderCode']) ? $info['preDeliveryOrderCode'] : '',
            'pre_delivery_order_id' => isset($info['preCnOrderCode']) ? $info['preCnOrderCode'] : '',
            'user_define1'          => isset($info['logisticsName']) ? $info['logisticsName'] : '',
            'user_define3'          => isset($info['returnReason']) ? $info['returnReason'] : '',
            'remark'                => isset($info['remark']) ? $info['remark'] : '',
            'create_time'           => date('Y-m-d H:i:s'),
            'buyer_nick'            => isset($info['buyerNick']) ? $info['buyerNick'] : '',
            'sender_company'        => isset($info['senderInfo']['company']) ? $info['senderInfo']['company'] : '',
            'sender_name'           => isset($info['senderInfo']['name']) ? $info['senderInfo']['name'] : '',
            'sender_zip_code'       => isset($info['senderInfo']['zipCode']) ? $info['senderInfo']['zipCode'] : '',
            'sender_tel'            => isset($info['senderInfo']['tel']) ? $info['senderInfo']['tel'] : '',
            'sender_mobile'         => isset($info['senderInfo']['mobile']) ? $info['senderInfo']['mobile'] : '',
            'sender_email'          => isset($info['senderInfo']['email']) ? $info['senderInfo']['email'] : '',
            'sender_countrycode'    => isset($info['senderInfo']['countryCode']) ? $info['senderInfo']['countryCode'] : '',
            'sender_province'       => isset($info['senderInfo']['province']) ? $info['senderInfo']['province'] : '',
            'sender_city'           => isset($info['senderInfo']['city']) ? $info['senderInfo']['city'] : '',
            'sender_area'           => isset($info['senderInfo']['area']) ? $info['senderInfo']['area'] : '',
            'sender_town'           => isset($info['senderInfo']['town']) ? $info['senderInfo']['town'] : '',
            'sender_detail_address' => isset($info['senderInfo']['detailAddress']) ? $info['senderInfo']['detailAddress'] : '',
        );

        $order_id        = OmsDatabase::$oms_db->insert('t_inbound_info' , $inbound_info);
        $inbound_details = array();
        foreach ($data['orderLines']['orderLine'] as $val) {
            $inbound_detail  = array(
                'order_id'              => $order_id,
                'line_no'               => $val['orderLineNo'],
                'customer_id'           => $info['ownerCode'],
                'sku'                   => isset($val['itemCode']) ? $val['itemCode'] : '',
                'source_order_code'     => isset($val['sourceOrderCode']) ? $val['sourceOrderCode'] : '',
                'sub_source_order_code' => isset($val['subSourceOrderCode']) ? $val['subSourceOrderCode'] : '',
                'product_date'          => isset($val['productDate']) ? $val['productDate'] : '',
                'expire_date'           => isset($val['expireDate']) ? $val['expireDate'] : '',
                'produce_code'          => isset($val['produceCode']) ? $val['produceCode'] : '',
                'batch_code'            => isset($val['batchCode']) ? $val['batchCode'] : '',
                'inventory_type'        => isset($val['inventoryType']) ? $val['inventoryType'] : '',
                'expected_qty'          => isset($val['planQty']) ? $val['planQty'] : ''
            );
            array_push($inbound_details , $inbound_detail);

        }
        OmsDatabase::$oms_db->insertAll('t_inbound_detail' , $inbound_details);

        return $this->sendSucc();
    }
    
    /*
     * 单据取消接口 CN_WMS_ORDER_CANCEL
     */
    public function orderCancel ($data)
    {
        //入库单分类
        $inbound = array('SCRK','LYRK','CCRK','CGRK','DBRK','QTRK','B2BRK','XNRK','THRK','HHRK');
        //出库单分类
        $outbound = array('PTCK','DBCK','B2BCK','QTCK','CGTH','XNCK','JITCK');

        //根据不同类型，查询不同的表
        if (in_array($data['orderType'] , $inbound)) {
            $find_table = 't_inbound_info';
            $table_name = 't_inbound_cancel';
            $find_field = 'order_id';
            $field_name = 'order_no';
        } elseif (in_array($data['orderType'] , $outbound)) {
            $find_table = 't_outbound_info';
            $table_name = 't_outbound_cancel';
            $find_field = 'order_id';
            $field_name = 'order_no';
        } else {
            $find_table = 't_delivery_order_info';
            $table_name = 't_delivery_order_cancel';
            $find_field = 'delivery_id';
            $field_name = 'delivery_order_code';
        }

        $inbound_where   = 'customer_id=:customer_id and warehouse_code=:warehouse_code and ' . $field_name .'=:order_no';
        $inbound_params  = array(
            ':customer_id'    => $data['ownerCode'],
            ':warehouse_code' => $data['warehouseCode'],
            ':order_no'       => $data['orderCode'],
        );
        $order_id = OmsDatabase::$oms_db->fetchOne($find_field , $find_table , $inbound_where , $inbound_params);

        $inbound_cancel_infos = array(
            'order_id'       => $order_id[$find_field],
            'order_no'       => $data['orderCode'],
            'order_type'     => isset($data['orderType']) ? $data['orderType'] : '',
            'customer_id'    => $data['ownerCode'],
            'warehouse_code' => $data['warehouseCode'],
            'reason'         => isset($data['cancelReason']) ? $data['cancelReason'] : '',
            'create_time'    => date('Y-m-d H:i:s'),
        );

        OmsDatabase::$oms_db->insert($table_name , $inbound_cancel_infos);

        return $this->sendSucc();
    }
    
    
    /*
     * 出库单确认接口 CN_WMS_STOCKOUT_CONFIRM 1
     */
    public function stockOutConfirm($data)
    {
        $t_outbound_info_record_infos = array(
            'customer_id'            => $data['deliveryOrder']['ownerCode'],
            'oms_order_no'           => $data['deliveryOrder']['deliveryOrderCode'],
            'warehouse_code'         => $data['deliveryOrder']['warehouseCode'],
            'order_type'             => isset($data['deliveryOrder']['orderType']) ? $data['deliveryOrder']['orderType'] : '' ,
            'warehouse_code'         => $data['deliveryOrder']['warehouseCode'],
            'out_biz_code'           => $data['deliveryOrder']['outBizCode'],
            'confirm_type'           => isset($data['deliveryOrder']['confirmType']) ? $data['deliveryOrder']['confirmType'] : '' ,
            'carrier_id'             => isset($data['packages']['packageNode']['logisticsCode']) ? $data['packages']['packageNode']['logisticsCode'] : '' ,
            'carrier_name'           => isset($data['packages']['packageNode']['logisticsName']) ? $data['packages']['packageNode']['logisticsName'] : '' ,
            'delivery_no'            => isset($data['packages']['packageNode']['expressCode']) ? $data['packages']['packageNode']['expressCode'] : '' ,
            'required_delivery_time' => isset($data['deliveryOrder']['orderConfirmTime']) ? $data['deliveryOrder']['orderConfirmTime'] : ''
        );
        $t_outbound_info_record_id = OmsDatabase::$oms_db->insert('t_outbound_info_record' , $t_outbound_info_record_infos);

        $t_outbound_detail_record_infos = array();
        foreach ($data['orderLines']['orderLine'] as $val) {
            $t_outbound_detail_record_info = array(
                'order_id'       => $t_outbound_info_record_id,
                'out_biz_code'   => $data['deliveryOrder']['outBizCode'],
                'order_no'       => isset($data['deliveryOrder']['cnOrderCode']) ? $data['deliveryOrder']['cnOrderCode']: '',
                'line_no'        => $val['orderLineNo'],
                'customer_id'    => $data['deliveryOrder']['ownerCode'],
                'sku'            => $val['itemCode'],
                'item_name'      => isset($val['itemName']) ? $val['itemName'] : '',
                'inventory_type' => isset($val['inventoryType']) ? $val['inventoryType'] : '',
                'qty_shipped'    => $val['actualQty'],
                'delivery_no'    => $data['packages']['packageNode']['logisticsCode'],
                'weight'         => isset($data['packages']['packageNode']['weight']) ? $data['packages']['packageNode']['weight'] : ''
            );
            array_push($t_outbound_detail_record_infos , $t_outbound_detail_record_info);
        }

        OmsDatabase::$oms_db->insertAll('t_outbound_detail_record' , $t_outbound_detail_record_infos);

        return $this->sendSucc();
    }


    /*
     * 退货入库单确认接口 CN_WMS_RETURNORDER_CONFIRM
     */
    public function returnOrderConfirm($data)
    {
        $info = $data['returnOrder'];
        $t_inbound_info_record_infos = array(
            'oms_order_no'       => $info['returnOrderCode'],
            'order_type'         => isset($info['orderType']) ? $info['orderType'] : '' ,
            'customer_id'        => $info['ownerCode'],
            'warehouse_code'     => $info['warehouseCode'],
            'cn_order_id'        => isset($info['cnOrderCode']) ? $info['cnOrderCode'] : '' ,
            'order_confirm_time' => isset($info['orderConfirmTime']) ? $info['orderConfirmTime'] : '' ,
            'carrier_id'         => isset($info['logisticsCode']) ? $info['logisticsCode'] : '' ,
            'carrier_name'       => isset($info['logisticsName']) ? $info['logisticsName'] : '' ,
            'create_time'        => date('Y-m-d H:i:s'),
            'remark'             => isset($info['remark']) ? $info['remark'] : '' ,
        );
        $t_inbound_info_record_id = OmsDatabase::$oms_db->insert('t_inbound_info_record' , $t_inbound_info_record_infos);

        $t_inbound_detail_record_infos = array();

        foreach ($data['orderLines']['orderLine'] as $val) {
            $inbound_detail_record_info = array(
                'order_id'          => $t_inbound_info_record_id,
                'line_no'           => $val['orderLineNo'],
                'sku'               => $val['itemCode'],
                'customer_id'       => $info['ownerCode'],
                'received_qty'      => isset($val['actualQty']) ? $val['actualQty'] : '',
                'create_time'       => date('Y-m-d H:i:s'),
                'source_order_code' => $info['cnOrderCode'],
                'inventory_type'    => '',
                'product_date'      => '',
                'expire_date'       => '',
                'produce_code'      => '',
                'qr_code'           => isset($val['qrCode']) ? $val['qrCode'] : ''
            );
            array_push($t_inbound_detail_record_infos , $inbound_detail_record_info);
        }

        OmsDatabase::$oms_db->insertAll('t_inbound_detail_record' , $t_inbound_detail_record_infos);

        return $this->sendSucc();
    }

    /*
     * 库存盘点通知接口 CN_WMS_INVENTORY_REPORT
     */
    public function inventoryReport($data)
    {
        $t_inventory_check_record_infos = array(
            'warehouse_code'   => $data['warehouseCode'],
            'check_order_code' => isset($data['check_order_code']) ? $data['check_order_code'] : '',
            'check_order_id'   => isset($data['check_order_id']) ? $data['check_order_id'] : '',
            'customer_id'      => $data['ownerCode'],
            'check_time'       => isset($data['checkTime']) ? $data['checkTime'] : '',
            'out_biz_code'     => $data['outBizCode'],
            'remark'           => isset($data['remark']) ? $data['remark'] : '',
            'create_time'      => date('Y-m-d H:i:s')
        );
        $order_id = OmsDatabase::$oms_db->insert('t_inventory_check_record' , $t_inventory_check_record_infos);

        $t_inventory_check_product_record_infos = array();
        foreach ($data['items']['item'] as $val) {
            $t_inventory_check_product_record_info = array(
                'inventory_id'   => $order_id,
                'inventory_type' => isset($val['inventoryType']) ? $val['inventoryType'] : '',
                'item_code'      => $val['itemCode'],
                'quantity'       => $val['quantity'],
                'batch_code'     => isset($val['batchCode']) ? $val['batchCode'] : '',
                'product_date'   => isset($val['productDate']) ? $val['productDate'] : '',
                'expire_date'    => isset($val['expireDate']) ? $val['expireDate'] : '',
                'produce_code'   => isset($val['produceCode']) ? $val['produceCode'] : '',
                'sn_code'        => isset($val['snCode']) ? $val['snCode'] : '',
                'remark'         => isset($val['remark']) ? $val['remark'] : '',
                'create_time'    => date('Y-m-d H:i:s')
            );
            array_push($t_inventory_check_product_record_infos , $t_inventory_check_product_record_info);
        }

        OmsDatabase::$oms_db->insertAll('t_inventory_check_product_record' , $t_inventory_check_product_record_infos);

        return $this->sendSucc();
    }

    /*
     * 订单流水通知接口 CN_WMS_ORDERPROCESS_REPORT
     */
    public function orderProcessReport($data)
    {
        $t_order_process_record = array(
            'order_code'     => $data['order']['orderCode'],
            'cn_order_code'  => isset($data['order']['cnOrderCode']) ? $data['order']['cnOrderCode'] : '',
            'order_type'     => isset($data['order']['orderType']) ? $data['order']['orderType'] : '',
            'warehouse_code' => isset($data['order']['warehouseCode']) ? $data['order']['warehouseCode'] : '',
            'process_status' => $data['process']['processStatus'],
            'operator_code'  => isset($data['process']['operatorCode']) ? $data['process']['operatorCode'] : '',
            'operator_name'  => isset($data['process']['operatorName']) ? $data['process']['operatorName'] : '',
            'operate_time'   => isset($data['process']['operateTime']) ? $data['process']['operateTime'] : '',
            'operate_info'   => isset($data['process']['operateInfo']) ? $data['process']['operateInfo'] : '',
            'remark'         => isset($data['process']['remark']) ? $data['process']['remark'] : '',
            'create_time'    => date('Y-m-d H:i:s')
        );
        OmsDatabase::$oms_db->insert('t_order_process_record' , $t_order_process_record);

        return $this->sendSucc();
    }

    /*
     * 发货单确认接口 CN_WMS_DELIVERYORDER_CONFIRM
     */
    public function deliveryOrderConfirm($data)
    {
        $info = $data['deliveryOrder'];

        $where  = 'delivery_order_code=:delivery_order_code ';
        $where .= ' and cn_order_code=:cn_order_code';
        $where .= ' and customer_id=:customer_id';
        $where .= ' and warehouse_code=:warehouse_code';

        $params  = array(
            ':delivery_order_code' => $info['deliveryOrderCode'],
            ':cn_order_code'       => $info['cnOrderCode'],
            ':customer_id'         => $info['ownerCode'],
            ':warehouse_code'      => $info['warehouseCode']
        );

        $delivery_id = OmsDatabase::$oms_db->fetchOne('delivery_id' , 't_delivery_order_info' , $where , $params);

        $t_delivery_order_info_record_infos = array(
            'delivery_id'         => $delivery_id['delivery_id'],
            'delivery_order_code' => $info['deliveryOrderCode'],
            'cn_order_code'       => isset($info['cnOrderCode']) ? $info['cnOrderCode'] : '',
            'warehouse_code'      => $info['warehouseCode'],
            'order_type'          => $info['orderType'],
            'customer_id'         => $info['ownerCode'],
            'out_biz_code'        => $info['outBizCode'],
            'confirm_type'        => isset($info['confirmType']) ? $info['confirmType'] : '',
            'order_confirm_time'  => $info['orderConfirmTime'],
            'operator_code'       => isset($info['operatorCode']) ? $info['operatorCode'] : '',
            'operator_name'       => isset($info['operatorName']) ? $info['operatorName'] : '',
            'operate_time'        => isset($info['operateTime']) ? $info['operateTime'] : '',
            'create_time'         => date('Y-m-d H:i:s')
        );

        $order_id = OmsDatabase::$oms_db->insert('t_delivery_order_info_record' , $t_delivery_order_info_record_infos);

        $t_delivery_order_detail_record_infos = array();
        foreach ($data['orderLines']['orderLine'] as $val) {
            $t_delivery_order_detail_record_info = array(
                'delivery_id'       => $order_id,
                'order_line_no'     => $val['orderLineNo'],
                'customer_id'       => $val['ownerCode'],
                'item_code'         => $val['itemCode'],
                'inventory_type'    => isset($val['inventoryType']) ? $val['inventoryType'] : '',
                'item_name'         => isset($val['itemName']) ? $val['itemName'] : '',
                'actual_qty'        => isset($val['actualQty']) ? $val['actualQty'] : '',
                'qr_code'           => isset($val['qrCode']) ? $val['qrCode'] : '',
                'create_time'       => date('Y-m-d H:i:s')
            );
            array_push($t_delivery_order_detail_record_infos , $t_delivery_order_detail_record_info);
        }

        OmsDatabase::$oms_db->insertAll('t_delivery_order_detail_record' , $t_delivery_order_detail_record_infos);

        return $this->sendSucc();
    }

    /*
     * 入库单创建接口 CN_WMS_ENTRYORDER_CREATE
     */
    public function entryOrderCreate($data)
    {
        $t_inbound_infos = array(
            'order_no'                => $data['entryOrder']['entryOrderCode'],
            'order_type'              => isset($data['entryOrder']['orderType']) ? $data['entryOrder']['orderType'] : '',
            'customer_id'             => $data['entryOrder']['ownerCode'],
            'warehouse_code'          => $data['entryOrder']['warehouseCode'],
            'expected_arrive_time1'   => isset($data['entryOrder']['expectStartTime']) ? $data['entryOrder']['expectStartTime'] : '',
            'remark'                  => isset($data['entryOrder']['remark']) ? $data['entryOrder']['remark'] : '',
            'carrier_id'              => isset($data['entryOrder']['logisticsCode']) ? $data['entryOrder']['logisticsCode'] : '',
            'carrier_name'            => isset($data['entryOrder']['logisticsName']) ? $data['entryOrder']['logisticsName'] : '',
            'supplier_code'           => isset($data['entryOrder']['supplierCode']) ? $data['entryOrder']['supplierCode'] : '',
            'supplier_name'           => isset($data['entryOrder']['supplierName']) ? $data['entryOrder']['supplierName'] : '',
            'create_time'             => date('Y-m-d H:i:s'),
            'sender_company'          => isset($data['entryOrder']['senderInfo']['company']) ? $data['entryOrder']['senderInfo']['company'] : '',
            'sender_name'             => $data['entryOrder']['senderInfo']['name'],
            'sender_zip_code'         => isset($data['entryOrder']['senderInfo']['zipCode']) ? $data['entryOrder']['senderInfo']['zipCode'] : '',
            'sender_tel'              => isset($data['entryOrder']['senderInfo']['tel']) ? $data['entryOrder']['senderInfo']['tel'] : '',
            'sender_mobile'           => $data['entryOrder']['senderInfo']['mobile'],
            'sender_email'            => isset($data['entryOrder']['senderInfo']['email']) ? $data['entryOrder']['senderInfo']['email'] : '',
            'sender_countrycode'      => isset($data['entryOrder']['senderInfo']['countryCode']) ? $data['entryOrder']['senderInfo']['countryCode'] : '',
            'sender_province'         => $data['entryOrder']['senderInfo']['province'],
            'sender_city'             => $data['entryOrder']['senderInfo']['city'],
            'sender_area'             => isset($data['entryOrder']['senderInfo']['area']) ? $data['entryOrder']['senderInfo']['area'] : '',
            'sender_town'             => isset($data['entryOrder']['senderInfo']['town']) ? $data['entryOrder']['senderInfo']['town'] : '',
            'sender_detail_address'   => $data['entryOrder']['senderInfo']['detailAddress'],
            'receiver_company'        => isset($data['entryOrder']['receiverInfo']['company']) ? $data['entryOrder']['receiverInfo']['company'] : '',
            'receiver_name'           => $data['entryOrder']['receiverInfo']['name'],
            'receiver_zip_code'       => isset($data['entryOrder']['receiverInfo']['zipCode']) ? $data['entryOrder']['receiverInfo']['zipCode'] : '',
            'receiver_tel'            => isset($data['entryOrder']['receiverInfo']['tel']) ? $data['entryOrder']['receiverInfo']['tel'] : '',
            'receiver_mobile'         => isset($data['entryOrder']['receiverInfo']['mobile']) ? $data['entryOrder']['receiverInfo']['mobile'] : '',
            'receiver_email'          => isset($data['entryOrder']['receiverInfo']['email']) ? $data['entryOrder']['receiverInfo']['email'] : '',
            'receiver_countrycode'    => isset($data['entryOrder']['receiverInfo']['countryCode']) ? $data['entryOrder']['receiverInfo']['countryCode'] : '',
            'receiver_province'       => $data['entryOrder']['receiverInfo']['province'],
            'receiver_city'           => $data['entryOrder']['receiverInfo']['city'],
            'receiver_area'           => isset($data['entryOrder']['receiverInfo']['area']) ? $data['entryOrder']['receiverInfo']['area'] : '',
            'receiver_town'           => isset($data['entryOrder']['receiverInfo']['town']) ? $data['entryOrder']['receiverInfo']['town'] : '',
            'receiver_detail_address' => $data['entryOrder']['receiverInfo']['detailAddress'],
            'operator_code'           => isset($data['entryOrder']['operatorCode']) ? $data['entryOrder']['operatorCode'] : '',
            'operator_name'           => isset($data['entryOrder']['operatorName']) ? $data['entryOrder']['operatorName'] : '',
            'operate_time'            => isset($data['entryOrder']['operateTime']) ? $data['entryOrder']['operateTime'] : '',
        );
        $order_id = OmsDatabase::$oms_db->insert('t_inbound_info' , $t_inbound_infos);

        $t_inbound_detail_infos = array();

        foreach ($data['orderLines']['orderLine'] as $val) {
            $t_inbound_detail_info = array(
                'order_id'       => $order_id,
                'line_no'        => $val['orderLineNo'],
                'customer_id'    => $data['entryOrder']['ownerCode'],
                'sku'            => $val['itemCode'],
                'remark'         => isset($data['entryOrder']['remark']) ? $data['entryOrder']['remark'] : '',
                'create_time'    => date('Y-m-d H:i:s'),
                'item_name'      => isset($val['itemName']) ? $val['itemName'] : '' ,
                'inventory_type' => isset($val['inventoryType']) ? $val['inventoryType'] : '' ,
                'product_date'   => isset($val['productDate']) ? $val['productDate'] : '' ,
                'expire_date'    => isset($val['expireDate']) ? $val['expireDate'] : '' ,
                'produce_code'   => isset($val['produceCode']) ? $val['produceCode'] : '' ,
                'batch_code'     => isset($val['batchCode']) ? $val['batchCode'] : ''
            );
            array_push($t_inbound_detail_infos , $t_inbound_detail_info);

        }
        OmsDatabase::$oms_db->insertAll('t_inbound_detail' , $t_inbound_detail_infos);

        return $this->sendSucc();
    }

    /*
     * 发货单创建接口 CN_WMS_DELIVERYORDER_CREATE
     */
    public function deliveryOrderCreate($data)
    {
        $info = $data['deliveryOrder'];

        $t_delivery_order_infos = array(
            'delivery_order_code'     => $info['deliveryOrderCode'],
            'cn_order_code'           => $info['cnOrderCode'],
            'pre_delivery_order_code' => isset($info['preDeliveryOrderCode']) ? $info['preDeliveryOrderCode'] : '',
            'order_type'              => $info['orderType'],
            'customer_id'             => $info['ownerCode'],
            'warehouse_code'          => $info['warehouseCode'],
            'deliv_create_time'       => $info['createTime'],
            'place_order_time'        => $info['placeOrderTime'],
            'pay_time'                => isset($info['payTime']) ? $info['payTime'] : '',
            'pay_no'                  => isset($info['payNo']) ? $info['payNo'] : '',
            'operator_code'           => isset($info['operatorCode']) ? $info['operatorCode'] : '',
            'operator_name'           => isset($info['operatorName']) ? $info['operatorName'] : '',
            'operate_time'            => $info['operateTime'],
            'shop_nick'               => $info['shopNick'],
            'seller_nick'             => isset($info['sellerNick']) ? $info['sellerNick'] : '',
            'buyer_nick'              => isset($info['buyerNick']) ? $info['buyerNick'] : '',
            'total_amount'            => isset($info['totalAmount']) ? $info['totalAmount'] : '',
            'item_amount'             => isset($info['itemAmount']) ? $info['itemAmount'] : '',
            'discount_amount'         => isset($info['discountAmount']) ? $info['discountAmount'] : '',
            'freight'                 => isset($info['freight']) ? $info['freight'] : '',
            'ar_amount'               => isset($info['arAmount']) ? $info['arAmount'] : '',
            'got_amount'              => isset($info['gotAmount']) ? $info['gotAmount'] : '',
            'service_fee'             => isset($info['serviceFee']) ? $info['serviceFee'] : '',
            'logistics_code'          => isset($info['logisticsCode']) ? $info['logisticsCode'] : '',
            'logistics_name'          => isset($info['logisticsName']) ? $info['logisticsName'] : '',
            'express_code'            => isset($info['expressCode']) ? $info['expressCode'] : '',
            'logistics_area_code'     => isset($info['logisticsAreaCode']) ? $info['logisticsAreaCode'] : '',
            'schedule_type'           => isset($info['deliveryRequirements']['scheduleType']) ? $info['deliveryRequirements']['scheduleType'] : '',
            'schedule_day'            => isset($info['deliveryRequirements']['scheduleDay']) ? $info['deliveryRequirements']['scheduleDay'] : '',
            'delivery_type'           => isset($info['deliveryRequirements']['deliveryType']) ? $info['deliveryRequirements']['deliveryType'] : '',
            'sender_company'          => isset($info['senderInfo']['company']) ? $info['senderInfo']['company'] : '',
            'sender_name'             => isset($info['senderInfo']['name']) ? $info['senderInfo']['name'] : '',
            'sender_tel'              => isset($info['senderInfo']['tel']) ? $info['senderInfo']['tel'] : '',
            'sender_mobile'           => isset($info['senderInfo']['mobile']) ? $info['senderInfo']['mobile'] : '',
            'sender_email'            => isset($info['senderInfo']['email']) ? $info['senderInfo']['email'] : '',
            'sender_countrycode'      => isset($info['senderInfo']['countryCode']) ? $info['senderInfo']['countryCode'] : '',
            'sender_province'         => isset($info['senderInfo']['province']) ? $info['senderInfo']['province'] : '',
            'sender_city'             => isset($info['senderInfo']['city']) ? $info['senderInfo']['city'] : '',
            'sender_area'             => isset($info['senderInfo']['area']) ? $info['senderInfo']['area'] : '',
            'sender_town'             => isset($info['senderInfo']['town']) ? $info['senderInfo']['town'] : '',
            'sender_detail_address'   => isset($info['senderInfo']['detailAddress']) ? $info['senderInfo']['detailAddress'] : '',
            'receiver_company'        => isset($info['receiverInfo']['company']) ? $info['receiverInfo']['company'] : '',
            'receiver_name'           => isset($info['receiverInfo']['name']) ? $info['receiverInfo']['name'] : '',
            'receiver_zipcode'        => isset($info['receiverInfo']['zipCode']) ? $info['receiverInfo']['zipCode'] : '',
            'receiver_tel'            => isset($info['receiverInfo']['tel']) ? $info['receiverInfo']['tel'] : '',
            'receiver_mobile'         => isset($info['receiverInfo']['mobile']) ? $info['receiverInfo']['mobile'] : '',
            'receiver_email'          => isset($info['receiverInfo']['email']) ? $info['receiverInfo']['email'] : '',
            'receiver_countrycode'    => isset($info['receiverInfo']['countryCode']) ? $info['receiverInfo']['countryCode'] : '',
            'receiver_province'       => isset($info['receiverInfo']['province']) ? $info['receiverInfo']['province'] : '',
            'receiver_city'           => isset($info['receiverInfo']['city']) ? $info['receiverInfo']['city'] : '',
            //'receiver_area'           => isset($info['receiverInfo']['area']) ? $info['receiverInfo']['area'] : '',
            'receiver_town'           => isset($info['receiverInfo']['town']) ? $info['receiverInfo']['town'] : '',
            'receiver_detail_address' => isset($info['receiverInfo']['detailAddress']) ? $info['receiverInfo']['detailAddress'] : '',
            'is_urgency'              => isset($info['receiverInfo']['isUrgency']) ? $info['receiverInfo']['isUrgency'] : '',
            'invoice_flag'            => isset($info['receiverInfo']['invoiceFlag']) ? $info['receiverInfo']['invoiceFlag'] : '',
            'buyer_message'           => isset($info['buyerMessage']) ? $info['buyerMessage'] : '',
            'seller_message'          => isset($info['sellerMessage']) ? $info['sellerMessage'] : '',
            'buyer_message'           => isset($info['buyerMessage']) ? $info['buyerMessage'] : '',
            'create_time'             => date('Y-m-d H:i:s'),
            'remark'                  => isset($info['remark']) ? $info['remark'] : '',
        );
		//error_log(print_r($t_delivery_order_infos,1),3,'/tmp/ll.txt');
        $order_id = OmsDatabase::$oms_db->insert('t_delivery_order_info' , $t_delivery_order_infos);

        $t_delivery_order_detail_infos = array();
		
        foreach ($data['orderLines']['orderLine'] as $val) {
            $t_delivery_order_detail_info = array(
                'delivery_id'           => $order_id,
                'order_line_no'         => isset($val['orderLineNo']) ? $val['orderLineNo'] : '',
                'source_order_code'     => isset($val['sourceOrderCode']) ? $val['sourceOrderCode'] : '',
                'sub_source_order_code' => isset($val['subSourceOrderCode']) ? $val['subSourceOrderCode'] : '',
                'pay_no'                => isset($val['payNo']) ? $val['payNo'] : '',
                'customer_id'           => isset($info['ownerCode']) ? $info['ownerCode'] : '',
                'item_code'             => isset($val['itemCode']) ? $val['itemCode'] : '',
                'inventory_type'        => isset($val['inventoryType']) ? $val['inventoryType'] : '',
                'item_name'             => isset($val['itemName']) ? $val['itemName'] : '',
                'ext_code'              => isset($val['extCode']) ? $val['extCode'] : '',
                'plan_qty'              => isset($val['planQty']) ? $val['planQty'] : '',
                'retail_price'          => isset($val['retailPrice']) ? $val['retailPrice'] : '',
                'actual_price'          => isset($val['actualPrice']) ? $val['actualPrice'] : '',
                'discount_amount'       => isset($val['discountAmount']) ? $val['discountAmount'] : '',
                'batch_code'            => isset($val['batchCode']) ? $val['batchCode'] : '',
                'product_date'          => isset($val['product_date']) ? $val['product_date'] : '',
                'expire_date'           => isset($val['expireDate']) ? $val['expireDate'] : '',
                'create_time'           => date('Y-m-d H:i:s')
            );
            array_push($t_delivery_order_detail_infos , $t_delivery_order_detail_info);
        }
        OmsDatabase::$oms_db->insertAll('t_delivery_order_detail' , $t_delivery_order_detail_infos);

        return $this->sendSucc();
    }

    /*
     * 入库单确认接口 CN_WMS_ENTRYORDER_CONFIRM
     */
    public function entryOrderConfirm($data)
    {
        $info = $data['entryOrder'];
        $where  = 'customer_id=:customer_id and warehouse_code=:warehouse_code and order_no=:order_no';
        $params = array(
            ':customer_id'    => $info['ownerCode'],
            ':warehouse_code' => $info['warehouseCode'],
            ':order_no'       => $info['entryOrderCode']
        );
        $order_id = OmsDatabase::$oms_db->fetchOne('*' , 't_inbound_info' , $where , $params);
        $t_inbound_info_record_infos = array(
            'order_id'           => $order_id['order_id'],
            'oms_order_no'       => $info['entryOrderCode'],
            'order_type'         => isset($info['orderType']) ? $info['orderType'] : '',
            'customer_id'        => $info['ownerCode'],
            'warehouse_code'     => $info['warehouseCode'],
            'cn_order_id'        => isset($info['cnOrderCode']) ? $info['cnOrderCode'] : '',
            'create_time'        => date('Y-m-d H:i:s'),
            'out_biz_code'       => $info['outBizCode'],
            'confirm_type'       => isset($info['confirmType']) ? $info['confirmType'] : '',
            'remark'             => isset($info['remark']) ? $info['remark'] : '',
        );

        $t_inbound_info_record_id = OmsDatabase::$oms_db->insert('t_inbound_info_record' , $t_inbound_info_record_infos);

        $t_inbound_detail_record_infos = array();

        foreach ($data['orderLines']['orderLine'] as $val) {
            $inbound_detail_record_info = array(
                'record_id'         => $t_inbound_info_record_id,
                'order_id'          => $order_id['order_id'],
                'line_no'           => $val['orderLineNo'],
                'sku'               => $val['itemCode'],
                'item_name'         => isset($val['itemName']) ? $val['itemName'] : '',
                'out_biz_code'      => $info['outBizCode'],
                'customer_id'       => $info['ownerCode'],
                'received_qty'      => $val['actualQty'],
                'create_time'       => date('Y-m-d H:i:s'),
                'source_order_code' => isset($info['cnOrderCode']) ? $info['cnOrderCode'] : '',
                'inventory_type'    => '',
                'product_date'      => '',
                'expire_date'       => '',
                'produce_code'      => ''
            );
            array_push($t_inbound_detail_record_infos , $inbound_detail_record_info);
        }
        OmsDatabase::$oms_db->insertAll('t_inbound_detail_record' , $t_inbound_detail_record_infos);

        return $this->sendSucc();
    }
    
    /*
     * 商品信息通知 CN_WMS_SINGLEITEM_SYNCHRONIZE
     */
    public function singleItemSyncronize($data)
    {
        $t_base_product = array(
            'sku'            => $data['item']['itemCode'],
            'customer_id'    => $data['ownerCode'],
            'warehouse_code' => $data['warehouseCode'],
            'supplier_code'  => isset($data['supplierCode']) ? : '',
            'supplier_name'  => isset($data['supplierName']) ? : '',
            'sku_short'      => isset($data['item']['shortName']) ? : '',
            'descr_c'        => $data['item']['itemName'],
            'descr_e'        => isset($data['item']['englishName']) ? : '',
            'gross_weight'   => isset($data['item']['grossWeight']) ? : '',
            'net_weight'     => isset($data['item']['netWeight']) ? : '',
            'cube'           => isset($data['item']['volume']) ? : '',
            'sku_Length'     => isset($data['item']['length']) ? : '',
            'sku_width'      => isset($data['item']['width']) ? : '',
            'sku_height'     => isset($data['item']['grossWeight']) ? : '',
            'freight_class'  => $data['item']['itemType'],
            'create_time'    => date('Y-m-d H:i:s')
        );

        OmsDatabase::$oms_db->insert('t_base_product' , $t_base_product);

        return $this->sendSucc();
    }
    
    /*
     * 菜鸟传输数据记录
     */
    public function writeLog($data,$response,$order_id,$ownerCode)
    {
        $data['logistics_interface'] = json_encode(simplexml_load_string($data['logistics_interface']));//将对象转换个JSON
        $data['logistics_interface'] = json_decode($data['logistics_interface'] , true);
        $data['response']            = is_array($response) ? json_encode($response) : $response;
        $data['logistics_interface'] = json_encode($data['logistics_interface']);
        $data['create_time']         = date('Y-m-d H:i:s');
        $data['order_id']            = empty($order_id)  ? '0' : $order_id;
        $data['owner_code']          = empty($ownerCode) ? '0' : $ownerCode;

        OmsDatabase::$oms_db->insert('t_get_cainiao_wms_info_log' , $data);

        return true;
    }

}
