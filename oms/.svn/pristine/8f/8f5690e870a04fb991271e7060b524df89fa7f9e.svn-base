<?php
/**
 * WMS入库单确认接口
 */

require API_ROOT.'/router/interface/erp/storage/common/cnRequest.php';

class erpStockInOrderConfirm extends cnRequest
{

    /**
     * 入库单信息推送
     * @param $param
     * @return array
     */
    public function confirm($param,$request)
    {
        
        require_once API_ROOT.'/router/format/storage/common/v1.0.php';
        $format = new format();
        $param = $format->request($param);

        if(empty($param)) {
            return $this->msgObj->outputCnStorage(false,'失败：请求的数据为空','S003');
        } else {

            //业务类型代码
            /*$order_type_arr = array(
                '302' => 'DBRK',	//调拨入库单
                '304' => 'QTRK',	//归还入库
                '501' => 'XTRK',	//销退入库单
                '504' => 'HHRK',	//换货入库单
                '601' => 'CGRK',	//采购入库单
                '904' => 'QTRK',	//其他入库单
                '306' => 'B2BRK'	//B2B入库单
            );*/
            //$order_type_arr = array(302,304,501,504,601,904,306);

            //获取数据库中对应order_type
            $order_type     = $this->getInboundInfo($param['orderCode']);
            //$param['orderType'] = $order_type_arr[$param['orderType']];

            //修改参数为
            $replace = "<orderType>".$order_type['order_type']."</orderType>";
            //修改order_type
            $data = preg_replace("/<orderType>(.*)<\/orderType>/s",$replace,cn_storage_service::$_data);

            //重装报文
            cn_storage_service::$_logistics_interface = $data;
            //cn_storage_service::$_method = 'taobao.qimen.entryorder.confirm';

            error_log("确认接口data：".print_r($data,true),3,'/yd/oms/api/res.txt');

            //转发数据给erp
            $response = $this->send($data);

            error_log("确认接口wms响应：".print_r($response,true),3,'/yd/oms/api/res.txt');

            //解析返回数据
            if(!empty($response)) {
                if($response['success']) {
                    //插入数据到对应数据库
                    $res = $this->insertInfo($param);
                    error_log("确认接口错误日志：".print_r($res,true),3,'/yd/oms/api/res.txt');

                    if(is_array($res)) {
                        return $this->msgObj->outputCnStorage(false, $res['errorMsg'], 'S003');
                    }
                    return $this->msgObj->outputCnStorage(true, "入库单确认接口 数据处理成功");
                } else {
                    return $this->msgObj->outputCnStorage(false, $response['errorMsg'], $response['errorCode']);
                }
            } else {
                return $this->msgObj->outputCnStorage(false, 'wms接口调用失败', 'S007');
            }
        }
    }

    /**
     * 插入数据到对应数据表
     * @param $param
     * @return array
     */
    public function insertInfo($param)
    {
        if(!empty($param)) {
            $orderItem = $param['orderItems']['orderItem']; //订单商品信息列表

            //获取入库单表中对应的order_id
            $order_id     = $this->getInboundInfo($param['orderCode']);
            if(!$order_id) {
                return $this->msgObj->outputCnStorage(false,'该入库单不存在','S003');
            }

            //基础对应字段=============入库单状态明细回传单头信息表
            $confirm      = $this->get_dataBase_relation('wms_stock_in_order_confirm');
            //插入字段
            $field_c      = implode(',',$confirm).',order_id,create_time';
            // 数据准备
            $tmp          = array();
            foreach ($confirm as $key => $value) {
                $tmp[]    = $param[$key];
            }
            unset($value);
            $value_c      = "('".implode("','",$tmp)."',{$order_id['order_id']},now())";

            unset($value,$tmp);

            //插入数据表
            $record_id    = $this->insertIntoTable('t_inbound_info_record',$field_c,$value_c);
            //========================


            //订单商品信息==============入库单回传明细记录表
            $order_item   = $this->get_dataBase_relation('wms_stock_in_order_confirm_order_item');
            $field_o      = implode(',',$order_item);

            //商品列表信息对应字段
            $items        = $this->get_dataBase_relation('wms_stock_in_order_confirm_items');
            $field_o     .= ','.implode(',',$items).',order_id,record_id,create_time';

            //入库单回传明细记录表 数据准备
            $value_arr = array();
            foreach ($orderItem as $key => $value) {

                //多个子信息
                if(is_array($value) && $key !== 'items') {

                    $value_arr[] = $this->subFun($value, $order_id['order_id'], $record_id);

                } else {

                    $value_arr[] = $this->subFun($orderItem, $order_id['order_id'], $record_id);
                    break;

                }
            }
            $value_o = implode(',',$value_arr);

            //插入数据到回传明细表
            $this->insertIntoTable('t_inbound_detail_record',$field_o,$value_o);
            //===============================
        }
    }

    /**
     * 获取入库单表对应信息
     * @param $order_code
     * @return mixed
     */
    protected function getInboundInfo($order_code)
    {
        global $db;

        //获取仓库编码
        $warehouse_code = cn_storage_service::$_warehouseid;
        //客户id
        $customer_id = cn_storage_service::$_customerid;

        //查询获取入库单表中订单对应的order_id
        $sql = "SELECT order_id,order_type FROM t_inbound_info WHERE customer_id=:customer_id AND order_code =:order_code AND warehouse_code =:warehouse_code AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':order_code',$order_code);
        $model->bindParam(':customer_id',$customer_id);
        $model->bindParam(':warehouse_code',$warehouse_code);
        $model->execute();
        $res = $model->fetch(PDO::FETCH_ASSOC);
//        $order_id = $res['order_id'];

        return $res;
    }

    /**
     * 插入数据到对应数据库
     * @param $table  需要插入的数据表
     * @param $field  需要插入的字段
     * @param $values  插入的数据
     * @return mixed
     */
    protected function insertIntoTable($table,$field, $values)
    {
        global $db;

        $sql = "INSERT IGNORE INTO {$table} ({$field}) VALUES {$values}";
        $model = $db->prepare($sql);
        $model->execute();

        if($table == 't_inbound_info_record') {
            //插入入库单语句，返回插入后自动生成的order_id
            return $db->lastInsertId();
        }
    }

    /**
     * 遍历子类目，组合插入数据值
     * @param $data
     * @param $order_id
     * @param $record_id
     * @return array
     */
    protected function subFun($data, $order_id, $record_id) {

        $tmp = array();

        //商品信息获取
        foreach ($data['items']['item'] as $item) {

            //商品id
            $arr = array($data['orderItemId']);

            //子类目存在多个子数值组
            if(is_array($item)) {
                $arr[] = $item['inventoryType'];
                $arr[] = $item['quantity'];
                $arr[] = $order_id;
                $arr[] = $record_id;
                $arr[] = date("Y-m-d H:i:s");
                $tmp[] = $arr;
            } else {
                //子类目只存在一个数值组
                $arr[] = $data['items']['item']['inventoryType'];
                $arr[] = $data['items']['item']['quantity'];
                $arr[] = $order_id;
                $arr[] = $record_id;
                $arr[] = date("Y-m-d H:i:s");
                $tmp[] = $arr;
                break;
            }
        }

        //处理数据为字符串格式
        foreach ($tmp as $k => $v) {
            $tmp[$k] = "('".implode("','",$v)."')";
        }
        $tmp = implode(',',$tmp);
        return $tmp;

    }

}