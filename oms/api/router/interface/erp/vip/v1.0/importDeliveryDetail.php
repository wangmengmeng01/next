<?php
/**
 * Notes:导入出仓明细
 * Date: 2019/1/9
 * Time: 16:51
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';
class importDeliveryDetail extends erpRequest
{
    /**
     * Notes:导入出仓明细
     * Date: 2019/1/9
     * Time: 16:51
     */
    public function import($jsonData)
    {
        $db = OmsDatabase::$oms_db;
        try {
            # 数据检查
            if (empty($jsonData)) {
                return '错误：请求数据不能为空';
            }
            # 数据发送
            $params = array(
                'method' => 'importDeliveryDetail',
                'data'   => $jsonData
            );
            $response = $this->send($params);
            $responseData = json_decode($response);
            if($responseData->returnCode === '0') {
                $aData = json_decode($jsonData,true);
                $arrData = array();
                # 出仓单录入数据库x
                $where = 'vendor_id=:vendor_id AND po_no=:po_no AND storage_no=:storage_no';
                $whereParams = array(':vendor_id'=>$aData['vendor_id'],':po_no'=>$aData['po_no'],':storage_no'=>$aData['storage_no']);
                $delivery_info = $db->fetchOne('id','t_vip_delivery_info',$where,$whereParams);
                foreach ($aData['delivery_list'] as $delivery) {
                    $arrData[] = array(
                        'dlv_id' => $delivery_info['id'],
                        'po_no' => $aData['po_no'],
                        'pick_no' => $delivery['pick_no'],
                        'amount' => $delivery['amount'],
                        'vendor_type' => $delivery['vendor_type'],
                        'barcode' => $delivery['barcode'],
                        'box_no' => $delivery['box_no'],
                        'create_time' => date('Y-m-d H:i:s'),
                    );
                }
                $db->getPdo()->beginTransaction();
                $inse_res = $db->insertAll('t_vip_delivery_detail',$arrData);
                // 更新拣货单状态为 已拣货，并绑定入库单号
                $updateData = array(
                    'status' => '已拣货',
                    'storage_no' => $aData['storage_no']
                );
                //使用in参数绑定有问题，故pick_no不使用参数绑定
                $pick_no_str = '';
                foreach ($aData['delivery_list'] as $p) {
                    $pick_no_str .= "'{$p['pick_no']}'".',';
                }
                $pick_no_str = '('.substr($pick_no_str,0,-1).')';
                $where = "vendor_id=:vendor_id AND po_no=:po_no AND warehouse=:warehouse AND pick_no in {$pick_no_str}";
                $whereParams = array(':vendor_id'=>$aData['vendor_id'],':po_no'=>$aData['po_no'],':warehouse'=>$_REQUEST['warehouseid']);
                $res = $db->update('t_vip_pick_list',$updateData,$where,$whereParams);
                if ($inse_res && $res){
                    $db->getPdo()->commit();
                    echo '导入出仓明细成功并存入数据库成功';exit;
                } else {
                    $db->getPdo()->rollBack();
                    echo '导入出仓明细成功但存入数据库失败';exit;
                }
            } else {
                throw new Exception('访问接口失败:'.$responseData->returnMessage);
            }
        } catch (\Exception $e) {
            return $e->getMessage();
        }
    }
}
