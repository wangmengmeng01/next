<?php
/**
 * 如意达菜鸟电子面单商家授权接口类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/erp/cainiao/cainiaoRequest.php';
class sellerAuthorization extends cainiaoRequest{
    
    /**
     * 商家授权信息处理方法
     * @param $params 商家授权信息
     */
    public function authorize ($params) {
        if (!empty($params)) {
            if ($params['platform_elec'] == 'CAINIAO') {
                return $this->saveCnAuthInfo($params);
            } elseif ($params['platform_elec'] == 'JD') {
                $saveRs = $this->saveOmsJdAuthInfo($params);
                if ($saveRs['code'] != '0000') {
                    return $saveRs;
                }
                return $this->saveJdAuthInfo($params);   
            }   
        } else {
            return $this->msgObj->outputCainiao(3, 'S003', '商家授权信息为空');
        }
    }

    /**
     * @param $params  请求数据
     * @return array   返回报文
     */
    public function saveOmsJdAuthInfo($params){
        global $db;

        if (!empty($params['seller_id'])) {
            $sellerId = $params['seller_id'];
        } else {
            return $this->msgObj->outputCainiao(3,'S003','商家id不能为空');
        }

        try {
            $sql = "SELECT * FROM csk_seller_access_token WHERE seller_id=:seller_id AND platform_elec='JD'";
            $model = $db->prepare($sql);
            $model->bindParam(':seller_id',$sellerId);
            $model->execute();
            $sellerInfo = $model->fetch(PDO::FETCH_ASSOC);

            if (empty($sellerInfo)) {
                $sql = "INSERT INTO csk_seller_access_token(seller_id,access_token,shop_name,platform_elec,time) VALUES(:seller_id,:access_token,:shop_name,:platform_elec,:time)";
                $values = array();
                $values[':seller_id']    = $params['seller_id'];
                $values[':access_token'] = $params['access_token'];
                $values[':platform_elec']= 'JD';
                $values[':shop_name']    = $params['shop_name'];
                $values[':time']         = date("Y-m-d H:i:s");
                $model = $db->prepare($sql);
                $model->execute($values);
            } else {
                if (!isset($sellerInfo['seller_id'])) {
                    return $this->msgObj->outputCainiao(3,'S003','查询出来的商家id不能为空');
                }
                $sql = "UPDATE csk_seller_access_token SET access_token=:access_token,shop_name=:shop_name,time=:time WHERE seller_id=:seller_id AND platform_elec=:platform_elec";

                $values = array();
                $values[':access_token'] = $params['access_token'];
                $values[':shop_name']    = $params['shop_name'];
                $values[':time']         = date("Y-m-d H:i:s");
                $values[':platform_elec']= 'JD';
                $values[':seller_id']    = $sellerInfo['seller_id'];
                $model = $db->prepare($sql);
                $model->execute($values);
            }
            return $this->msgObj->outputCainiao(2, '0000', '成功');
        } catch (Exception $e) {
            return $this->msgObj->outputCainiao(3,$e->getCode(),$e->getMessage());
        }
    }

    /**
     * 保存京东商家授权信息
     * @param 业务请求参数 $params
     */
    public function saveJdAuthInfo($params){
        global $jdDb;
        if (!empty($params['seller_id'])) {
            $sellerId = $params['seller_id'];
        } else {
            return $this->msgObj->outputCainiao(3,'S003','商家id不能为空');
        }

        try {
            $sql = "SELECT * FROM oms_jd_seller WHERE plat_sales_code=:plat_sales_code;";
            $model = $jdDb->prepare($sql);
            $model->bindParam(':plat_sales_code',$sellerId);
            $model->execute();
            $sellerInfo = $model->fetch(PDO::FETCH_ASSOC);

            $columnsArr = $this->get_database_relation('jd_seller_authorization') ;//获取请求数据与数据库字段对应关系
            if (empty($sellerInfo)) {
                return $this->msgObj->outputCainiao(3,'S003','相应商家信息未维护！');
            } else {
                $setSql = '';
                foreach ($columnsArr as $k=>$v) {
                    if (!empty($params[$k])) {
                        if ($v != 'plat_sales_code') {
                            $setSql .= "{$v}='{$params[$k]}',";
                        }
                    } else {
                        //报错
                        return $this->msgObj->outputCainiao(3,'S003','商家授权信息不完整');
                    }
                }
                $setSql = substr($setSql, 0, -1);
                $plat_sales_code = $sellerInfo['plat_sales_code'];
                if ($setSql != '') {
                    $updateSql = "UPDATE oms_jd_seller SET ". $setSql . " WHERE plat_sales_code='{$plat_sales_code}'";
                    $model = $jdDb->prepare($updateSql);
                    $model->execute();
                }
            }
            return $this->msgObj->outputCainiao(2, '0000', '成功');
        } catch (Exception $e) {
            return $this->msgObj->outputCainiao(3,$e->getCode(),$e->getMessage());
        }
    }
    
    /**
     * 保存菜鸟商家授权信息
     * @param 业务请求参数 $params
     */
    public function saveCnAuthInfo($params){
        global $db;
        if (!empty($params['seller_id'])) {
            $sellerId = $params['seller_id'];
        } else {
            return $this->msgObj->outputCainiao(3,'S003','商家id不能为空');
        }
        $sql = "SELECT * FROM csk_seller_access_token WHERE seller_id = :seller_id";
        $model = $db->prepare($sql);
        $model->bindParam(':seller_id', $sellerId);
        $model->execute();
        $sellerInfo = $model->fetch(PDO::FETCH_ASSOC);
        
        $columnsArr = $this->get_database_relation('seller_authorization') ;//获取请求数据与数据库字段对应关系
        //判断表中商家id相关信息是否存在
        if (empty($sellerInfo)) {
            $columns = implode(',', array_values($columnsArr)) ;
            $columns_value = ':' . implode(",:", $columnsArr) ;
            $sql = " INSERT INTO csk_seller_access_token({$columns}) VALUES({$columns_value}) " ;
            $values = array();
            foreach ($columnsArr as $k=>$v) {
                if (!empty($params[$k])) {
                    $values[':'.$v] = $params[$k] ;
                } else {
                    //报错
                    return $this->msgObj->outputCainiao(3,'S003','商家授权信息不完整');
                }
            }
            $model = $db->prepare($sql);
            
            $model->execute($values);
        } else {
            $setSql = "";
            $values = array();
            foreach ($columnsArr as $k=>$v) {
                if (!empty($params[$k])) {
                    if ($v != 'seller_id') {
                        $setSql .= "{$v}='{$params[$k]}',";
                    }
                } else {
                    //报错
                    return $this->msgObj->outputCainiao(3,'S003','商家授权信息不完整');
                }
            }
            $setSql = substr($setSql, 0, -1);
            $updateSql = "UPDATE csk_seller_access_token SET ". $setSql . " WHERE seller_id='{$sellerInfo['seller_id']}'";
            $model = $db->prepare($updateSql);
            $model->execute();
        }
        return $this->msgObj->outputCainiao(2, '0000', '成功');
    }
}
?>