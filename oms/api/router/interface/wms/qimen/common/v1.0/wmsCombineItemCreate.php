<?php
/**
 * 组合商品接口操作类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';

class wmsCombineItemCreate extends wmsRequest
{

    /**
     * 创建组合商品信息
     * @param  $params            
     * @return array
     */
    public function create($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            //转发数据给wms
			$response = $this->send();
            //解析返回的数据
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    $request = $params;
                    $db = Yii::app()->db;
                    $column_arr = $this->get_dataBase_relation('combine_item');
                    $column_key_str = implode(',', array_values($column_arr)) . ',create_time';
                    $column_value_str = ':' . implode(',:', array_values($column_arr)) . ',now()';
                    $sql = "INSERT INTO t_base_combine_product({$column_key_str}) VALUES({$column_value_str})";
                    $model = $db->createCommand($sql);
                    //判断该商品是否存在
                    $this->has_upd_product($request['itemCode'], $request['ownerCode'],$request['warehouseCode']);
                    $values = array();
                    foreach ($column_arr as $k => $v) {
                        $values[':' . $v] = empty($request[$k]) ? '' : $request[$k];
                    }
                    $model->bindValues($values);
                    $model->execute();
                    $combineId = $db->getLastInsertID();
                    
                    //写入组合商品明细
                    $column_detail_arr = $this->get_dataBase_relation('combine_item_detail');
                    $detail_key_str = implode(',', array_values($column_detail_arr)) . ',combine_id,create_time';
                    if (! empty($request['items'])) {
                        $detail_value_str = ":" . implode(',:', array_values($column_detail_arr)) . ",$combineId,now()";
                        $sql = "INSERT INTO t_base_combine_product_detail({$detail_key_str}) VALUES({$detail_value_str})";
                        $model = $db->createCommand($sql);
                        if (empty($request['items']['item'][0])) {
                            $request['items']['item'] = array($request['items']['item']);
                        }
                        foreach ($request['items']['item'] as $d_v)
                        {
                            $values = array();
                            unset($column_detail_arr['combineItemCode']);
                            foreach ($column_detail_arr as $k => $v) {
                                $values[':' . $v] = empty($d_v[$k]) ? '' : $d_v[$k];
                            }
                            $values[':combine_item_code'] = $request['itemCode'];
                            $model->bindValues($values);
                            $model->execute();
                        }
                    }
                    return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
                } else {
                    return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
                }
            } else {
                return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
            }
        }
    }

    
    /**
     * 存在的SKU，逻辑删除处理
     * @param $sku 产品编码
     * @param $customer_id 客户ID
     * @return bool
     */
    public function has_upd_product($sku, $customerId,$warehouseCode)
    {
        $values = array();
        $Ssql = '';
        $Usql = '';
        $connection = Yii::app()->db;
        $values[':item_code'] = $sku;
        $values[':customer_id'] = $customerId;
        $values[':warehouse_code'] = $warehouseCode;
        $Ssql = 'SELECT COUNT(*) t ,combine_id FROM `t_base_combine_product` WHERE item_code = :item_code AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid = 1';
        $datas = $connection->createCommand($Ssql);
        $datas->bindValues($values);
        $rs = $datas->queryRow();
        
        if ($rs['t'] > 0) {
            $Usql = 'UPDATE `t_base_combine_product` SET is_valid = 0 WHERE item_code = :item_code AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1; ';
            $Usql .= ' UPDATE `t_base_combine_product_detail` SET is_valid = 0 WHERE combine_id = :combine_id; ';
            $model = $connection->createCommand($Usql);
            $values[':combine_id'] = $rs['combine_id'];
            $model->bindValues($values);
            if ($model->execute()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
}

