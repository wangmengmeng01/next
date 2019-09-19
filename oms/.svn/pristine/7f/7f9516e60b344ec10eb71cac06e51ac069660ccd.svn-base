<?php
/**
 * 奇门店铺同步接口
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsShopSynchronize extends wmsRequest 
{
    public function create($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            //转发数据给WMS
            $response = $this->send();
            if (!empty($response)) {
                if ($response['flag'] == 'success') {
                    global $db;
                    $shopInfo = $params['shop'];
                    $selectSql = "SELECT shop_id FROM t_base_shop WHERE customer_id=:customer_id AND shop_code=:shop_code AND udf2=:udf2 AND is_valid=1";
                    $selectModel = $db->prepare($selectSql);
                    $selectModel->bindParam(':customer_id', qimen_service::$_customerId);
                    $selectModel->bindParam(':shop_code', $shopInfo['shopCode']);
                    $selectModel->bindParam(':udf2', $shopInfo['platformShopCode']);
                    $selectModel->execute();
                    $shopIdArr = $selectModel->fetchAll(PDO::FETCH_ASSOC);
                    
                    //判断店铺表中有无该店铺信息
                    if (empty($shopIdArr)) {
                        //添加
                        $this->insertShopInfo($shopInfo);
                    } else {
                        //更新 
                        $this->updateShopInfo($shopIdArr);
                        //添加
                        $this->insertShopInfo($shopInfo);
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
     * 添加店铺方法
     * @param 店铺信息 $shopInfo
     */
    public function insertShopInfo($shopInfo)
    {
        global $db;
        $column_shop_info_arr = $this->get_dataBase_relation('shop_info');
        $column_key_shop_info = implode(',', array_values($column_shop_info_arr)) . ",modify_time,create_time";
        $column_value_shop_info = ":" . implode(',:', array_keys($column_shop_info_arr)) . ",now(),now()";
        $sql = "INSERT IGNORE INTO t_base_shop({$column_key_shop_info}) VALUES({$column_value_shop_info})";
        $model = $db->prepare($sql);
        
        $values = array();
        foreach ($column_shop_info_arr as $shop_k=>$shop_v) {
            if ($shop_k == 'zipCode' || $shop_k == 'province' || $shop_k == 'city' || $shop_k == 'area' || $shop_k == 'town' || $shop_k == 'detailAddress') {
                $values[':'. $shop_k] = empty($shopInfo['shopAddress'][$shop_k]) ? '' : $shopInfo['shopAddress'][$shop_k] ;
            } else {
                $values[':'. $shop_k] = empty($shopInfo[$shop_k]) ? '' : $shopInfo[$shop_k] ;
            }
        }
        $values[':customer_id'] = qimen_service::$_customerId;
        $model->execute($values);
    }
    
    /**
     * 更新店铺有效性
     * @param 店铺Id $shopId
     */
    public function updateShopInfo($shopIdArr)
    {
        global $db;
        $updateSql = "UPDATE t_base_shop SET is_valid=0 WHERE shop_id=:shop_id";
        foreach ($shopIdArr as $shopIdVal) {
            $model = $db->prepare($updateSql);
            $model->bindParam(':shop_id', $shopIdVal['shop_id']);
            $model->execute();
        }
    }
}