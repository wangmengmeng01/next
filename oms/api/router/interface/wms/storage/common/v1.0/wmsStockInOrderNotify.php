<?php
/**
 * 仓储普通入库单下发接口
 */
//require API_ROOT.'/router/interface/wms/storage/common/wmsRequest.php';
require API_ROOT.'/router/interface/wms/storage/common/v1.0/wmsOrderCancelNotify.php';

class wmsStockInOrderNotify extends wmsRequest
{
    /**
     * 入库单创建
     * @param $param
     * @return array
     */
    public function create($param) {
        if(empty($param)) {
            return $this->msgObj->outputCnStorage(false,'失败：请求的数据为空','S003');
        } else {
            //检查商品是否存在
            global $db;
            $prs = $this->getProduct($param);
            if ($prs['success'] != 1) {
                return $this->msgObj->outputCnStorage(false,$prs['errorMsg'],$prs['errorCode']);
            }

            //业务类型代码
            $order_type_arr = array(
                '302' => 'DBRK',	//调拨入库单
                '304' => 'QTRK',	//归还入库
                '501' => 'XTRK',	//销退入库单 
                '504' => 'HHRK',	//换货入库单
                '601' => 'CGRK',	//采购入库单
                '904' => 'QTRK',	//其他入库单
                '306' => 'B2BRK'	//B2B入库单
            );

            //修改order_type为奇门需要的参数
            $order_type = $order_type_arr[$param['orderType']];

            //拼装入库单创建接口报文
            $xml_in = '<?xml version="1.0" encoding="utf-8"?>
                    <request>
                    <entryOrder>
                    <entryOrderCode>'.  $param['erpOrderCode'].'</entryOrderCode>
                    <cnOrderId>'.       $param['orderCode'].'</cnOrderId>
                    <ownerCode>'.       $param['ownerUserId'].'</ownerCode>
                    <warehouseCode>'.   $param['storeCode'].'</warehouseCode>
                    <orderCreateTime>'. $param['orderCreateTime'].'</orderCreateTime>
                    <orderType>'.       $order_type.'</orderType>
                    <senderInfo>
                    <company></company>
                    <name>'.            $param['senderInfo']['senderName'].'</name>
                    <zipCode>'.         $param['senderInfo']['senderZipCode'].'</zipCode>
                    <tel>'.             $param['senderInfo']['senderPhone'].'</tel>
                    <mobile>'.          $param['senderInfo']['senderMobile'].'</mobile>
                    <province>'.        $param['senderInfo']['senderProvince'].'</province>
                    <city>'.            $param['senderInfo']['senderCity'].'</city>
                    </senderInfo>
                    </entryOrder>
                    <orderLines>';

            //报文拼接
            foreach ($param['orderItemList']['orderItem'] as $item) {
                if(is_array($item)) {
                    $xml_in .= $this->createXmlString($item);
                } else {
                    $xml_in .= $this->createXmlString($param['orderItemList']['orderItem']);
                    break;
                }
            }
            $xml_in .= '</orderLines></request>';

            //退货入库报文
            $xml_return = '<?xml version="1.0" encoding="utf-8"?>
                            <request>
                            <returnOrder>
                            <returnOrderCode>'.     $param['erpOrderCode'].'</returnOrderCode>
                            <warehouseCode>'.       $param['storeCode'].'</warehouseCode>
							<cnOrderId>'.       	$param['orderCode'].'</cnOrderId>
                            <orderType>'.           $order_type.'</orderType>
                            <preDeliveryOrderCode>'.$param['prevErpOrderCode'].'</preDeliveryOrderCode>
                            <senderInfo>
                            <name>'.                $param['senderInfo']['senderName'].'</name>
                            <mobile>'.              $param['senderInfo']['senderMobile'].'</mobile>
                            <province>'.            $param['senderInfo']['senderProvince'].'</province>
                            <city>'.                $param['senderInfo']['senderCity'].'</city>
                            <detailAddress>'.       $param['senderInfo']['senderAddress'].'</detailAddress>
                            </senderInfo>
                            </returnOrder>
                            <orderLines>';
            //报文拼接
            foreach ($param['orderItemList']['orderItem'] as $value) {
                if(is_array($value)) {
                    $xml_return .= $this->backXmlString($value);
                } else {
                    $xml_return .= $this->backXmlString($param['orderItemList']['orderItem']);
                    break;
                }
            }
            $xml_return .= '</orderLines></request>';
            
            //501(XTRK)、504(HHRK)退货入库单创建
            if($param['orderType'] == '501' || $param['orderType']=='504') {
                $xml = $xml_return;
                $method = 'returnorder.create'; 
            } else {
                $xml = $xml_in;
                $method = 'entryorder.create';
            }
            cn_storage_service::$_method = $method;

            //WMS响应值
            $response = $this->send($xml, $method);
            
            //解析返回数据
            if(!empty($response)) {

                if($response['success']) {

                    //检测入库单是否存在,Y忽略，N新增
                    $res = $this->checkInboundInfo($param);
                    //新增
                    if(empty($res)) {

                        $result = $this->insertInfo($param);

                        if($result) {

                            //数据库存储成功,调用订单流水接口
                            //下发入库单已存至数据库，开始调用订单流水接口
                            $res = $this->callUpload($param);
                            if($res) {
                                return $this->msgObj->outputCnStorage(false,$res['errorMsg'],'S007');
                            }else {
                                return $this->msgObj->outputCnStorage(true,'入库单下发成功','');
                            }

                        } else {

                            return $this->msgObj->outputCnStorage(false,'入库单下发创建数据存储失败','S007');
                        }

                    } else {
                        //入库单已存在，直接主动调用订单流水接口
                        //主动调用订单流水接口====================================
                        $res = $this->callUpload($param);
                        if($res) {
                            return $this->msgObj->outputCnStorage(false,$res['errorMsg'],'S007');
                        }
                        //====================================================


                    }
                    return $this->msgObj->outputCnStorage(true,'入库单下发创建成功','',$response['addon']);
                } else {
                    //转发失败
                    return $this->msgObj->outputCnStorage(false,$response['errorMsg'],$response['errorCode'],$response['addon']);
                }
            } else {
                return $this->msgObj->outputCnStorage(false,'wms接口调用失败','S007');
            }
        }
    }

    /**
     * 检查入库单是否已存在
     * @param $param
     * @return mixed
     */
    protected function checkInboundInfo($param) {

        global $db;

        $sql = "SELECT * FROM t_inbound_info WHERE order_no = :order_no AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1;";
        $model = $db->prepare($sql);
        $model->bindParam(':order_no',$param['erpOrderCode']);
        $model->bindParam(':order_type',$param['orderType']);
        $model->bindParam(':customer_id',$param['ownerUserId']);
        $model->bindParam(':warehouse_code',$param['storeCode']);
        $model->execute();

        $res = $model->fetch(PDO::FETCH_ASSOC);

        return $res;

    }

    /**
     * 发货单入库创建接口报文
     * @param $item
     * @return string
     */
    protected function createXmlString($item) {
        $xml = '<orderLine>
                    <ownerCode>'.       $item['ownerUserId'].'</ownerCode>
                    <itemCode>'.        $item['itemCode'].'</itemCode>
                    <cnOrderItemId>'.   $item['orderItemId'].'</cnOrderItemId>
                    <cnItemId>'.        $item['itemId'].'</cnItemId>
                    <itemName>'.        $item['itemName'].'</itemName>
                    <planQty>'.         $item['itemQuantity'].'</planQty>
                    <inventoryType>'.   $item['inventoryType'].'</inventoryType>
                    <productDate>'.     $item['produceDate'].'</productDate>
                    </orderLine>';
        return $xml;
    }

    /**
     * 退货入库单创建接口报文
     * @param $value
     * @return string
     */
    protected function backXmlString($value) {
        $xml = '<orderLine>
                <cnOrderItemId>'.       $value['orderItemId'].'</cnOrderItemId>
                <sourceOrderCode>'.     $value['orderSourceCode'].'</sourceOrderCode>
                <subSourceOrderCode>'.  $value['subSourceCode'].'</subSourceOrderCode>
                <ownerCode>'.           $value['ownerUserId'].'</ownerCode>
                <itemCode>'.            $value['itemCode'].'</itemCode>
                <inventoryType>'.       $value['inventoryType'].'</inventoryType>
                <planQty>'.             $value['itemQuantity'].'</planQty>
                <productDate>'.         $value['produceDate'].'</productDate>
                </orderLine>';

        return $xml;
    }

    /**
     * 插入数据到对应数据库
     * @param $param
     * @return bool
     */
    protected function insertInfo($param) {

        $senderInfo = $param['senderInfo'];                         //发件人信息
        $itemList   = $param['orderItemList']['orderItem'];         //订单商品明细


        //整理入库单接口参数与数据库字段对应字段信息============
        //入库单信息
        $inbound_1  = $this->get_dataBase_relation('stock_in_order_notify');
        $field_b    = implode(',',array_values($inbound_1)).',';        //插入字段
        //入库单发件人信息
        $inbound_2  = $this->get_dataBase_relation('stock_in_order_notify_sender');
        $field_b   .= implode(',',array_values($inbound_2)).',create_time';
        //入库单表数据准备
        $tmp = array();
        foreach ($inbound_1 as $k => $b) {  //基本信息
            $tmp[] = isset($param[$k])?$param[$k]:'';
        }unset($b);

        foreach ($inbound_2 as $k => $b) {  //发件人信息
            $tmp[] = isset($senderInfo[$k])?$senderInfo[$k]:'';
        }
        $value_b = "('".implode("','",$tmp)."',now())";

        //插入商品入库单表
        $order_id = $this->inboundInsertData('t_inbound_info',$field_b, $value_b);
        //==================================================




        //商品明细=======================================
        $detail     = $this->get_dataBase_relation('stock_in_order_notify_detail');
        $field_d    = implode(',',array_values($detail)).',order_id,create_time';

        //入库单明细表数据准备
        $value_d = array();
        foreach ($itemList as $k => $value) {
            if(is_array($value)) {
                $value_d[] = $this->inboundDataManage($value, $detail, $order_id);
            } else {
                $value_d[] = $this->inboundDataManage($itemList, $detail, $order_id);
                break;
            }
        }
        unset($value);

        //组合成字符串
        $value_d = implode(',',$value_d);

        //插入入库单明细表
        $this->inboundInsertData('t_inbound_detail',$field_d, $value_d);
        //===================================================



        //装箱明细================================
        if(!empty($param['caseInfoList'])) {

            $packing    = $param['caseInfoList']['wmsStockInCaseInfo']; //装箱列表
            $sub        = $this->get_dataBase_relation('stock_in_order_notify_detail_sub');
            $field_s    = implode(',',array_values($sub)).',order_id,create_time';


            //入库装箱明细表数据准备
            $value_s = array();
            foreach ($packing as $p => $pac) {

                //$pac为数组，且存在caseItemList键，说明$packing有多个装箱对象
                if(is_array($pac) && array_key_exists('caseItemList',$pac)) {

                    foreach ($pac['caseItemList']['wmsStockInCaseItem'] as $k => $v) {
                        if(is_array($v)) {
                            //装箱明细列表中有多个明细
                            $value_s[] = $this->inboundDataManage($v, $sub, $order_id);
                        } else {
                            //装箱明细列表中有一个明细
                            $value_s[] = $this->inboundDataManage($pac['caseItemList']['wmsStockInCaseItem'], $sub, $order_id);
                            break;
                        }
                    }
                } else {

                    //$packing有一个装箱对象
                    if($p == 'caseItemList') {

                        foreach ($pac['wmsStockInCaseItem'] as $k => $v) {
                            if(is_array($v)) {
                                $value_s[] = $this->inboundDataManage($v, $sub, $order_id);
                            } else {
                                $value_s[] = $this->inboundDataManage($pac['wmsStockInCaseItem'], $sub, $order_id);
                                break;
                            }
                        }
                    }

                }
            }

            //组合成字符串
            $value_s = implode(',',$value_s);

            //插入入库装箱明细表
            $this->inboundInsertData('t_inbound_detail_sub',$field_s, $value_s);
        }

        return true;
        //====================================
    }

    /**
     * 插入数据到对应数据库
     * @param $table  需要插入的数据表
     * @param $field  需要插入的字段
     * @param $values  插入的数据
     * @return mixed
     */
    protected function inboundInsertData($table,$field, $values) {

        global $db;

        $sql = "INSERT INTO {$table} ({$field}) VALUES {$values}";
        $model = $db->prepare($sql);
        $model->execute();

        if($table == 't_inbound_info') {
            //插入入库单语句，返回插入后自动生成的order_id
            return $db->lastInsertId();
        }
    }

    /**
     * 多条详情信息整理组合，返回所需要的多条数据数组
     * @param $dataList  信息总条数
     * @param $fieldArr  数据库对应字段数据
     * @param $order_id  对应插入入库单数据的id
     * @return array
     */
    protected function inboundDataManage($dataList, $fieldArr, $order_id) {

        $res = array();
        foreach ($fieldArr as $k => $v) {
            $res[]  = isset($dataList[$k])?$dataList[$k]:'';
        }
        $res[] = $order_id;
        $res[] = date("Y-m-d H:i:s");

        return "('".implode("','",$res)."')";
    }


    /**
     * 入库单下发成功后，调用订单流水接口，订单流水接口调用失败，则调用订单取消接口
     * @param $param
     * @return array|bool
     */
    protected function callUpload($param) {
        $data=array(
            'method'=>'WMS_ORDER_STATUS_UPLOAD',
            'customerid'=>$param['ownerUserId'],
            'appkey'=>SRD_APP_KEY,
            'sign'=>'',
            'warehouseid'=>$param['storeCode'],
            'data'=>'<request>
                        <orderType>'.$param['orderType'].'</orderType>
                        <orderCode>'.$param['orderCode'].'</orderCode>
                        <status>WMS_ACCEPT</status>
                        <operator>system</operator>
                        <operatorContact>15000000000</operatorContact>
                        <operateDate>'.date("Y-m-d H:i:s").'</operateDate>
                        <timeZone></timeZone>
                        <content></content>
                        <remark></remark>
                        <features></features>
                    </request>',
        );
        //签名
        $data['sign'] = strtoupper(base64_encode(md5(SRD_APP_SECRET.$data['data'].SRD_APP_SECRET)));

        $httpObj = new httpclient();
        $response = $httpObj->post(STORAGE_API_URL,$data);
        $response = $this->xmlObj->xmlStr2array($response);
        //返回失败调用订单取消接口
        if(!$response['success']) {

            $cancel = new wmsOrderCancelNotify();

            $cancel_param = array(
                'orderType'   => $param['orderType'],
                'orderCode'   => $param['orderCode'],
                'storeCode'   => $param['storeCode'],
                'ownerUserId' => $param['ownerUserId']
            );

            $resp = $cancel->cancel($cancel_param);
            if(!$resp['success']) {
                return $this->msgObj->outputCnStorage(false,'入库单下发调用订单取消接口失败：'.$resp['errorMsg'],'S007');
            }

            return $this->msgObj->outputCnStorage(false,'入库单下发调用订单流水接口:'.$response['errorMsg'],'S007');
        }
    }
}