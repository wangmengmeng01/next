<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/12 14:02
 * @description:
 */

require API_ROOT . '/router/interface/wms/storage/common/wmsRequest.php';

class wmsSkuInfoNotify extends wmsRequest{
    /*
     * 数据格式装换推送给wms
     */
    public function create($requestData){
        switch($requestData['type']){
            case 'PACKING_MATERIALS':
                $requestData['type']='BC';  //包材
                break;
            case 'NORMAL':
                $requestData['type']='ZC';  //正常商品
                break;
            case 'CONSUMPTIVE_MATERIALS':
                $requestData['type']='HC';  //耗材
                break;
            default:
                $requestData['type']='OTHER';
        }
        $warehouseCode = empty($requestData['storeCode']) ? cn_storage_service::$_partner_code : $requestData['storeCode'];
        
        $skuInfo = $this->findProduct($requestData,$warehouseCode);
        if (!empty($skuInfo)) {
            $itemId      = $skuInfo['item_id'];
            $itemVersion = $skuInfo['alternate_sku3'];
            $actionType  = 'update';
            
            $updateFlag = 0;
            if ($itemVersion < $requestData['itemVersion']) {
                $updateFlag = 1;
            } 
            /*if ($itemVersion >= $requestData['itemVersion']) {
                $logExt = array(
                    'api_params' => array(),
                    'rs_flag'    =>'true',
                    'return_msg' => '<?xml version="1.0" encoding="utf-8"?><response><success>true</success><errorCode></errorCode><errorMsg></errorMsg></response>',
                );
                return $this->msgObj->outputCnStorage(true, '', '', $logExt);  
            }*/
        } else {
            $itemId = '';
            $actionType = 'add';
        }
        
        $data=array(
            'request'=>array(
                'actionType'    =>$actionType,  //操作类型
                'warehouseCode' =>$warehouseCode,    //仓库编码
                'ownerCode'     =>$requestData['ownerUserId'],  //货主编码
                'supplierCode'  =>'',                           //供应商编码  N
                'supplierName'  =>'',                           //供应商名称  N
                'item'=>array(
                        'itemCode'      =>$requestData['itemCode'], //商品编码
                        'itemId'        =>$itemId,     //
                        'cnItemId'      =>$requestData['itemId'],     //菜鸟商品编码
                        'itemName'      =>isset($requestData['name'])?$requestData['name']:'',     //商品名称
                        'itemType'      =>isset($requestData['type'])?$requestData['type']:'',    //商品类型  ZC=正常商品, FX=分销商品, ZH=组合商品, ZP=赠品, BC=包材, HC=耗材, FL=辅料, XN=虚拟品, FS=附属品, CC=残次品, OTHER=其它
                        'shortName'     =>'',                         //商品简称  N
                        'englishName'   =>'',                        //N
                        'barCode'       =>isset($requestData['barCode'])?$requestData['barCode']:'',      //条形码
                        'skuProperty'   =>isset($requestData['specification'])?$requestData['specification']:'',                           //商品属性
                        'stockUnit'     =>isset($requestData['unit'])?$requestData['unit']:'',                         //商品计量单位
                        'length'        =>isset($requestData['length'])?$requestData['length']/10:'',              // 长 (厘米)
                        'width'         =>isset($requestData['width'])?$requestData['width']/10:'',                //宽 (厘米)
                        'height'        =>isset($requestData['height'])?$requestData['height']/10:'',              //高 (厘米)
                        'volume'        =>isset($requestData['volume'])?$requestData['volume']/1000:'',               //体积 (升)  
                        'grossWeight'   =>isset($requestData['grossWeight'])?$requestData['grossWeight']/1000:'',     //毛重 (千克)
                        'netWeight'     =>isset($requestData['netWeight'])?$requestData['netWeight']/1000:'',          //净重  (千克)
                        'color'         =>isset($requestData['color'])?$requestData['color']:'',
                        'size'          =>isset($requestData['size'])?$requestData['size']:'',
                        'title'         =>'',
                        'categoryId'    =>isset($requestData['category'])?$requestData['category']:'',
                        'categoryName'  =>isset($requestData['categoryName'])?$requestData['categoryName']:'',
                        'pricingCategory'=>'',
                        'safetyStock'   =>'',
                        'tagPrice'      =>isset($requestData['tagPrice'])?$requestData['tagPrice']:'',
                        'retailPrice'   =>isset($requestData['retailPrice'])?$requestData['retailPrice']:'',
                        'costPrice'     =>isset($requestData['costPrice'])?$requestData['costPrice']:'',
                        'purchasePrice' =>isset($requestData['purchasePrice'])?$requestData['purchasePrice']:'',
                        'seasonCode'    =>'',
                        'seasonName'    =>'',
                        'brandCode'     =>isset($requestData['brand'])?$requestData['brand']:'',
                        'brandName'     =>isset($requestData['brandName'])?$requestData['brandName']:'',
                        'isSNMgmt'      =>'N',
                        'productDate'   =>'',           //生产日期
                        'expireDate'    =>'',           //过期日期, string (10) , YYYY-MM-DD
                        'isShelfLifeMgmt'=>isset($requestData['isShelflife']) ? $requestData['isShelflife'] : '',                    //是否需要保质期管理, Y/N (默认为N)
                        'shelfLife'     =>isset($requestData['lifecycle'])? $requestData['lifecycle']*24 : '',                        //保质期 (小时) , int/
                        'rejectLifecycle'=>isset($requestData['rejectLifecycle']) ? $requestData['rejectLifecycle'] : '',                 //保质期禁收天数, int/
                        'lockupLifecycle'=>isset($requestData['lockupLifecycle']) ? $requestData['lockupLifecycle'] : '',                   //保质期禁售天数, int/
                        'adventLifecycle'=>isset($requestData['adventLifecycle']) ? $requestData['adventLifecycle'] : '',                 //保质期临期预警天数, int/
                        'isBatchMgmt'   =>'',                   //是否需要批次管理, Y/N (默认为N)/
                        'batchCode'     =>'',                   //批次代码, string (50)
                        'batchRemark'   =>'',                  //批次备注, string (200)
                        'packCode'      =>'',                      //包装代码, string (50)
                        'pcs'           =>isset($requestData['pcs']) ? $requestData['pcs'] : '',                        //箱规, string(50)
                        'originAddress' =>isset($requestData['producingArea']) ? $requestData['producingArea'] : '',                   //商品的原产地, string (50)
                        'approvalNumber'=>isset($requestData['approvalNumber']) ? $requestData['approvalNumber'] : '',              //批准文号, string (50)
                        'isFragile'     =>isset($requestData['isHygroscopic']) ? $requestData['isHygroscopic'] : '',                     //是否易碎品, Y/N,  (默认为N)
                        'isHazardous'   =>isset($requestData['isDanger']) ? $requestData['isDanger'] : '',                 //是否危险品, Y/N,  (默认为N)
                        'remark'        =>'',                   //备注,  string (500)
                        'createTime'    =>time(),                 //创建时间, string (19) , YYYY-MM-DD HH:MM:SS
                        'updateTime'    =>time(),                 //更新时间, string (19) , YYYY-MM-DD HH:MM:SS
                        'isValid'       =>'Y',                   //是否有效, Y/N (默认为Y)
                        'isSku'         =>'Y',                     //是否sku, Y/N,  (默认为Y)
                        'packageMaterial'=>isset($requestData['materialType']) ? $requestData['materialType'] : '',                //商品包装材料类型, string (200)
                )
            )
        );
        $content=$this->xmlObj->array2xml($data);
        $response = $this->send($content,'singleitem.synchronize');
        
        //保存到数据库
        if($response['success']){
            try{
                global $db;
                if ($actionType == 'update') {
                    if ($updateFlag == 1) {
                        $this->updateProduct($requestData,$warehouseCode);
                    } 
                } else {
                    $request=$data;
                    $column_detail_arr  = $this->get_dataBase_relation('sku_info_notify');
                    $detail_key_str     = implode(',', array_values($column_detail_arr)) . ',alternate_sku3,create_time';
                    $values = array();
                    foreach ($request['request'] as $d_v)
                    {
                        foreach ($column_detail_arr as $k => $v) {
                            $values[':' . $v] = empty($d_v[$k]) ? '' : $d_v[$k];
                        }
                    }
                    $values[':warehouse_code']  = $request['request']['warehouseCode'];
                    $values[':customer_id']     = $request['request']['ownerCode'];
                    $return_msg = isset($response['addon']['return_msg']) ? $response['addon']['return_msg'] : '';
                    
                    if($return_msg){
                        $return_msg = simplexml_load_string($return_msg);
                        $item_id    = json_decode(json_encode($return_msg),1);
                        $values[':item_id'] = isset($item_id['itemId'])?$item_id['itemId']:'';
                    }else{
                        $values[':item_id']='';
                    }
                    $detail_value_str = ":" . implode(',:', array_values($column_detail_arr)) . ",{$requestData['itemVersion']},now()";
                    
                    $sql = "INSERT INTO t_base_product({$detail_key_str}) VALUES({$detail_value_str})";
                    $model = $db->prepare($sql);
                    $model->execute($values);
                }
            }catch(Exception $e){
                return $this->msgObj->outputCnStorage(false,'系统异常，商品信息通知接口保存数据失败','S003');
            }
        }
        return $response;
    }
    
    public function updateProduct($params,$warehousCode){
        global $db;
        $sql = "UPDATE t_base_product 
                SET descr_c=:descr_c,alternate_sku1=:alternate_sku1,alternate_sku3=:alternate_sku3
                WHERE sku=:sku 
                AND customer_id=:customer_id 
                AND warehouse_code=:warehouse_code";
        $model = $db->prepare($sql);
        $values = array(
            ':descr_c'          =>  $params['name'],
            ':alternate_sku1'   =>  $params['barCode'],
            ':alternate_sku3'   =>  $params['itemVersion'],
            ':sku'              =>  $params['itemCode'],
            ':customer_id'      =>  $params['ownerUserId'],
            ':warehouse_code'   =>  $warehousCode
        );
        $model->execute($values);
    }

    public function findProduct($params,$warehousCode){
        global $db;
        $sql = "SELECT item_id,alternate_sku3
                FROM t_base_product 
                WHERE sku=:sku 
                AND customer_id=:customer_id 
                AND warehouse_code=:warehouse_code";
        $model = $db->prepare($sql);
        $values = array(
            ':sku'              =>  $params['itemCode'],
            ':customer_id'      =>  $params['ownerUserId'],
            ':warehouse_code'   =>  $warehousCode
        );
        $model->execute($values);
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        return $rs;
    }
}