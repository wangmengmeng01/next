<?php
/**
 * 库存汇总可用量查询model
 * table: t_qimen_customer_bind
 * @author Renee
 *
 */

class InventorySumAvailable extends CActiveRecord
{
    public static function model($className=__CLASS__)
    {
        return parent::model($className);
    }
    
    public function tableName()
    {
        return '{{inventory_sum_available]}}';
    }
    
    public static function search($param,$search = 0)
    {   
        if($search == 1){
            $param = $_POST['inventorySumAvailable'];
        }
        //校验权限
        util::operatePriContr(23.1, 'json');
        $rsCustomer = findData(new QimenCustomer,'customer_id,customer_name',array('customer_id'=>$param['customer_id'],'customer_name'=>$param['customer_name']));
        if(empty($rsCustomer)){
            die(json_encode(array(
                    'status' => 'error',
                    'msg' => '查无此货主或者填写的货主ID与货主名称不对应！'
                )));
        }
        $rsWarehouse = findData(new Warehouse,'warehouse_code,descr_c',array('warehouse_code'=>$param['warehouse_code'],'descr_c'=>$param['warehouse_name']));
        if(empty($rsWarehouse)){
            die(json_encode(array(
                    'status' => 'error',
                    'msg' => '查无此仓库或者填写的仓库编码与仓库名称不对应！'
                )));
        }
        isset($param['act']) && $param['act'] == 'export'?$ex = ",":$ex = "\n";

        $product_nameArr = array_filter(array_unique(explode($ex, trim($param['product_name']))));
        $product_nameStr = '';
        $skuArr = array_filter(array_unique(explode($ex, trim($param['sku']))));

        foreach ($product_nameArr as $k => $v) {
            $product_nameStr .= 'descr_c like "%'.$v.'%" or ';
        }
        $product_nameStr = $product_nameStr == ''?'':substr($product_nameStr, 0,-3).' and customer_id ="'.$param['customer_id'].'"';
        $info = array();
        if($product_nameStr != ''){
            $info=Product::model()->findAll(array(
                          "select"=>array('sku'), //要查询的字段
                          "condition"=>$product_nameStr, //查询条件
                        ));
        }
        if(empty($info) && empty($skuArr)){
            die(json_encode(array(
                    'status' => 'error',
                    'msg' => '查无此商品！'
                )));
        }else{
            $itemCode = '';
            $infoArr = array();
            foreach ($info as $k => $v) {
                $infoArr[$k] = $v['sku'];
            }
            $sku = array_unique(array_merge($skuArr,$infoArr));
            error_log(print_r($sku,1),3,"d:/hs.log");
            foreach ($sku as $k => $v) {
                $itemCode .= '<itemCode>'.$v.'</itemCode>';
            }
        }
        $requestXml =  '<?xml version="1.0" encoding="utf-8"?>
                        <request>
                            <criteria>
                                <warehouseCode>'.$param['warehouse_code'].'</warehouseCode>
                                <ownerCode>'.$param['customer_id'].'</ownerCode>
                                <deliveryOrderType>JYCK</deliveryOrderType>
                                <items>'
                                    .$itemCode.
                                '</items>
                            </criteria>
                        </request>';
        $customerInfo = QimenCustomer::model()->findByPk($param['customer_id']);
        if (empty($customerInfo)) {
            die(json_encode(array(
                'status' => 'error',
                'msg' => '查无此用户！'
            )));
        }
        //直接访问wms那边接口       系参自己拼接    请求参数自己拼接
        $apiParams = array(
            'method' => 'inventory.sum.available',
            'customerid' => $param['customer_id'],
            'appkey' => $customerInfo['wms_app_key'],
            'sign' => strtoupper(base64_encode(md5($customerInfo['wms_secret'] . $requestXml . $customerInfo['wms_secret']))),
            'timestamp' => date('Y-m-d H:i:s'),
            'data' => $requestXml
        );

        
        include_once Yii::app()->basePath . '/ext/httpclient.php';
        include_once Yii::app()->basePath . '/ext/xml.php';
        $httpObj = new httpclient();
        $response = $httpObj->post($customerInfo['wms_api_url'], $apiParams);
        if ($response == '') {
            die(json_encode(array(
                'status' => 'error',
                'msg' => '数据推送失败，请检查YWMS接口是否能正常访问'
            )));
        } else {
            $xmlObj = new xml();
            $responseArr = $xmlObj->xmlStr2array($response);
            if ($responseArr['flag'] == 'success') {
                //$this->writeSendLog($rsCustomer['customer_id'], 1, $responseArr['code'], $responseArr['message']);
                if(empty($responseArr['items']['item'][0])){
                    $arr = $responseArr['items']['item'];
                    unset($responseArr['items']['item']);
                    $responseArr['items']['item'][0] = $arr;
                    unset($arr);
                }
                $data = array();
                $total = count($responseArr['items']['item']);
                if($search == 1){
                    $start = ($_POST['page']-1)*$_POST['rows'];
                    $responseArr['items']['item'] = array_splice($responseArr['items']['item'], $start,$_POST['rows']);
                }
                foreach ($responseArr['items']['item'] as $k => $v) {
                    $data[$k]['customer_id'] = $v['ownerCode'];
                    $data[$k]['customer_name'] = $rsCustomer['customer_name'];
                    $data[$k]['warehouse_code'] = $v['warehouseCode'];
                    $data[$k]['warehouse_name'] = $rsWarehouse['descr_c'];
                    $data[$k]['sku'] = $v['itemCode'];
                    $item=Product::model()->find(array(
                          "select"=>array('descr_c'), //要查询的字段
                          "condition"=>"sku = '".$v['itemCode']."'", //查询条件
                        ));
                    $data[$k]['product_name'] = $item['descr_c'];
                    $data[$k]['availableQuantity'] = $v['onhandQuantity'] - $v['occupiedQuantity'];
                    $data[$k]['onhandQuantity'] = $v['onhandQuantity'];
                    $data[$k]['occupiedQuantity'] = $v['occupiedQuantity'];
                }
                return '{"total":' . $total . ',"rows":' . CJSON::encode($data) . '}';  
            } else {
                //$this->writeSendLog($rsCustomer['customer_id'], 0, $responseArr['code'], $responseArr['message']);
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => $responseArr['message']
                )));
            }
            Yii::app()->end();
        }
    }

    public static function getColumns()
    {
        $columns = array(
            '货主ID' => 'customer_id',
            '货主名称' => 'customer_name',
            '仓库编码' => 'warehouse_code',
            '仓库名称' => 'warehouse_name',
            'SKU' => 'sku',
            '商品名称' => 'product_name',
            '可用量' => 'availableQuantity',
            '占用量' => 'occupiedQuantity',
            '在库量' => 'onhandQuantity'
        );
        return $columns;
    }
}


function findData($table,$select,$arr){
    $sql = '';
    foreach ($arr as $k => $v) {
        if(trim($v) != ''){
            $sql .= $k.'="'.$v.'" and ';
        }
    }
    $sql = substr($sql, 0,-4);
    return $table::model()->find(array('select' =>array($select),'condition' => $sql));
}