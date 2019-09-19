<?php
/**
 * 商品资料下发推送接口处理类
 * User: Renee
 * Date: 2018/5/8
 * Time: 17:01
 */
require API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';
class KlSkuCreate extends wmsRequest
{
    /**
     * 处理方法
     * @param $params
     * @return 返回消息
     */
    public function create ($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputKaola(false,'执行错误：请求数据为空！');
        } else {
            try {
                $resp = $this->send();
                if (empty($resp)) {
                    return $this->msgObj->outputKaola(false,'返回数据为空！');
                } else {
                    if ($resp['success']) {
                        //添加商品
                        $this->insertInfo($params);
                    }
                    return $this->msgObj->outputKaola($resp['success'],$resp['error_msg'],kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false,$e->getMessage());
            }
        }
    }

    /**
     * 新增更新数据
     * @param $params 请求参数
     */
    public function insertInfo ($params) {
        $upData = array();//更新

        $inWhere = '';
        $pParam = array();
        foreach ($params as $k1=>$item) {
            $pParam[':'.$k1.'id'] = $item['sku_id'];
            $inWhere .= ':'.$k1.'id,';
        }
        $inWhere = substr($inWhere,0,-1);

        $pParam[':customer_id'] = kaola_service::$_ownerId;
        $pParam[':warehouse_code'] = kaola_service::$_stockId;

        $skuInfos = OmsDatabase::$oms_db->fetchAll('product_id,sku', 't_base_product', "customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku IN (".$inWhere.")", $pParam);

        if (!empty($skuInfos)) {
            foreach ($skuInfos as $skey=>$skuInfo) {
                foreach ($params as $pKey=>$pVal) {
                    if ($skuInfo['sku'] == $pVal['sku_id']) {
                        $upData[$pKey] = $pVal;
                        $upData[$pKey]['product_id'] = $skuInfo['product_id'];
                        unset($params[$pKey]);
                    }
                }
            }
        }
        $addData = $params;

        $i = 0;
        if (!empty($upData)) {
            $updateParams = array();
            foreach ($upData as $uVal) {
                $params1 = array(
                    'product_id'=>$uVal['product_id'],
                    'descr_c'=>$uVal['goods_name'],
                    'descr_e'=>$uVal['goods_en_name'],
                    'goods_code'=>$uVal['goods_no'],
                    'gross_weight'=>$uVal['weight'],
                );
                $updateParams[$i] = $params1;
                $i++;
            }
            OmsDatabase::$oms_db->batch_update('t_base_product',$updateParams,'product_id');
        }

        $j = 0;
        if (!empty($addData)) {
            $addParams = array();
            foreach ($addData as $aVal) {
                $params2 = array(
                    'sku'=>$aVal['sku_id'],
                    'descr_c'=>$aVal['goods_name'],
                    'descr_e'=>$aVal['goods_en_name'],
                    'goods_code'=>$aVal['goods_no'],
                    'gross_weight'=>$aVal['weight'],
                    'customer_id'=>kaola_service::$_ownerId,
                    'warehouse_code'=>kaola_service::$_stockId,
                    'create_time'=>date("Y-m-d H:i:s")
                );
                $addParams[$j] = $params2;
                $j++;
            }
            if (count($addParams) != 1) {
                OmsDatabase::$oms_db->insertAll('t_base_product',$addParams);
            } else {
                OmsDatabase::$oms_db->insert('t_base_product',$addParams[0]);
            }
        }
    }
}