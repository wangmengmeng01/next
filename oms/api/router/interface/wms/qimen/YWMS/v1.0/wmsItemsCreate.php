<?php
/**
 * 商品同步接口(批量)操作类
 * 
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';

class wmsItemsCreate extends wmsRequest
{

    /**
     * 创建商品信息
     * @param  $params            
     * @return array
     */
    public function create($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            // 转发数据给WMS
            $response = $this->send();
            //解析返回的数据
            if (!empty($response)) {
                $xmlObj = new xml();
                $responseArr = $xmlObj->xmlStr2array($response['addon']['return_msg']);//将YWMS返回的xml转化为数组
                if (!empty($responseArr['items']['item']) && empty($responseArr['items']['item'][0])) {
                    $responseArr['items']['item'] = array($responseArr['items']['item']);
                }
                //判断YWMS返回状态
                if ($response['flag'] == 'success') {
                    //将商品信息写入数据库
                    foreach ($params['items']['item'] as $v) {
                        //判断该商品是否存在
                        if ($this->has_product($v['itemCode'], $params['ownerCode'], $params['warehouseCode'])) {
                            $this->addItem($params, $v);
                        }
                    }
                    //判断校验时有无错误
                    if (!empty(msg::$err_arr)) {//校验有错误
                        qimen_service::$_errorMsg = msg::$err_arr;
                        $responseArr['flag'] = 'failure';
                        $responseArr['code'] = '0001';
                        $responseArr['message'] = '失败';
                        
                        $itemStr = '<items>';
                        foreach (msg::$err_arr as $key=>$val) {
                            $itemStr .= '<item>';
                            $itemStr .= $xmlObj->array2xml($val);
                            $itemStr .= '</item>';
                        }
                        $itemStr .= '</items>';
                        $response['addon']['return_msg'] = '<?xml version="1.0" encoding="utf-8"?><response>' . $xmlObj->array2xml($responseArr) . $itemStr . '</response>';
                        return $this->msgObj->outputQimen('failure', '失败', '0001', $response['addon'], $response['addon']['return_msg']);
                    } else {//全部成功
                        return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                    }
                } else {
                    //失败的其他情况,直接将message返回
				    if (!empty($responseArr['items']['item'])) {
				        //找出成功商品
				        foreach ($params['items']['item'] as $p_k=>$p_v) {
				            foreach ($responseArr['items']['item'] as $key=>$val) {
				                if ($p_v['itemCode'] == $val['itemCode']) {
				                    unset($params['items']['item'][$p_k]);
				                }
				            }
				        }
				        //将成功的数据写入数据库
				        if (!empty($params['items']['item'])) {
				            foreach ($params['items']['item'] as $i_v) {
				                if ($this->has_product($i_v['itemCode'], $params['ownerCode'], $params['warehouseCode'])) {
				                    $this->addItem($params, $i_v);
				                }
				            }
				        }
				        //添加校验时的错误
				        if (!empty(msg::$err_arr)) {
				            qimen_service::$_errorMsg = msg::$err_arr;
				            if (!empty($responseArr['items']['item'])) {
				                //取出YWMS返回错误数组的最大键值
				                $keys =  array_keys($responseArr['items']['item'], max($responseArr['items']['item']));
				            }
				            //拼接到返回的数组中
				            $key = $keys[0]+1;
				            foreach (msg::$err_arr as $v) {
				                $responseArr['items']['item'][$key] = $v;
				                $key++;
				            }
				        }
				        //将返回的数组转化为xml
				        $items = $responseArr['items']['item'];
				        unset($responseArr['items']);
				        $itemsXml = '<items>';
				        foreach ($items as $b) {
				            $itemsXml .= '<item>' . $xmlObj->array2xml($b) . '</item>';
				        }
				        $itemsXml .= '</items>';
				        $response['addon']['return_msg'] = '<?xml version="1.0" encoding="utf-8"?><response>' . $xmlObj->array2xml($responseArr) . $itemsXml . '</response>';
				    }
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon'], $response['addon']['return_msg']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
            }
        }
    }
    
    /**
     * 添加商品到数据库
     * @param $params
     * @return boolean
     */
    public function addItem($params,$items){
        global $db;
        $request = $params;
        
        $column_arr = $this->get_dataBase_relation('product');
        $column_key_str = implode(',', array_values($column_arr)) . ',create_time';
        $column_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
        $sql = "INSERT INTO t_base_product({$column_key_str}) VALUES({$column_value_str})";
        $model = $db->prepare($sql);
        $values = array();
        
        unset($column_arr['warehouseCode']);
        unset($column_arr['ownerCode']);
        unset($column_arr['supplierCode']);
        unset($column_arr['supplierName']);
        
        foreach ($column_arr as $k => $v) {
            if($k == 'isSNMgmt'){
                $values[':' . $v] = empty($items[$k]) ? 'N' : $items[$k];
            } elseif($k == 'isShelfLifeMgmt'){
                $values[':' . $v] = empty($items[$k]) ? 'N' : $items[$k];
            } elseif($k == 'isBatchMgmt'){
                $values[':' . $v] = empty($items[$k]) ? 'N' : $items[$k];
            } elseif($k == 'isFragile'){
                $values[':' . $v] = empty($items[$k]) ? 'N' : $items[$k];
            } elseif($k == 'isHazardous'){
                $values[':' . $v] = empty($items[$k]) ? 'N' : $items[$k];
            } elseif($k == 'isValid'){
                $values[':' . $v] = empty($items[$k]) ? 'Y' : $items[$k];
            } elseif($k == 'isSku'){
                $values[':' . $v] = empty($items[$k]) ? 'Y' : $items[$k];
            } else {
                $values[':' . $v] = empty($items[$k]) ? '' : $items[$k];
            }
        }
        
        $values[':warehouse_code'] = $request['warehouseCode'];
        $values[':customer_id'] = $request['ownerCode'];
        $values[':supplier_code'] = $request['supplierCode'];
        $values[':supplier_name'] = $request['supplierName'];
        $model->execute($values);
    }
 
    /**
     * 判断商品是否存在
     * @param 商品编码 $sku
     * @param 货主编码 $customerId
     * @return 商品不存在，返回true
     */
    public function has_product($sku,$customerId,$warehouseCode){
        $values = array();
        global $db;
        $values[':sku'] = $sku;
        $values[':customer_id'] = $customerId;
        $values[':warehouse_code'] = $warehouseCode;
//         $Psql = 'INSERT IGNORE INTO t_base_product_log SELECT * FROM `t_base_product` WHERE sku = :sku AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid = 1';
        $Psql = 'INSERT IGNORE INTO t_base_product_log SELECT * FROM `t_base_product` WHERE sku = :sku AND customer_id=:customer_id AND is_valid = 1';
        $datas = $db->prepare($Psql);
        $datas->execute($values);
		//删除无效数据
// 		$Dsql = "DELETE FROM t_base_product WHERE customer_id = :customer_id AND sku = :sku AND warehouse_code=:warehouse_code AND is_valid=1";
        $Dsql = "DELETE FROM t_base_product WHERE customer_id = :customer_id AND sku = :sku AND is_valid=1";
		$model = $db->prepare($Dsql);
		if ($model->execute($values)) {
			return true;
		} else {
		    return false;
		}				        
    }  
}

