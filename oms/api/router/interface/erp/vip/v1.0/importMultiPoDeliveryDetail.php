<?php
/**
 * Notes:导入出仓明细2.0
 * Date: 2019/1/9
 * Time: 16:51
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';
class importMultiPoDeliveryDetail extends erpRequest
{
    /**
     * Notes:导入出仓明细2.0
     * Date: 2019/1/9
     * Time: 16:51
     */
    public function import($jsonData)
    {
        $db = OmsDatabase::$oms_db;
        $db->getPdo()->beginTransaction();
        try {

            # 数据检查
            if (empty($jsonData)) {
                return '错误：请求数据不能为空';
            }
            # 出仓单录入数据库
            $insertData = $this->getInsertData($jsonData);
            $db->insertAll('t_vip_delivery_detail',$insertData);
            # 数据发送
            $params = array(
                'method' => 'importMultiPoDeliveryDetail',
                'data'   => $jsonData
            );
            $response = $this->send($params);
            $responseData = json_decode($response);
            if($responseData->returnCode === 0) {
                $aData = json_decode($jsonData,true);
                // 更新拣货单状态为 已拣货，并绑定入库单号
                $updateData = array(
                    'status' => '已拣货',
                    'storage_no' => $aData['storage_no']
                );
                $where = 'vendor_id=:vendor_id AND po_no=:po_no';
                $whereParams = array(':vendor_id'=>$aData['vendor_id'],':po_no'=>$aData['po_no']);
                $db->update('t_vip_pick_list',$updateData,$where,$whereParams);
                $db->getPdo()->commit();
            } else {
                $db->getPdo()->rollBack();
            }
            return $response;


        } catch (\Exception $e) {
            $db->getPdo()->rollBack();
            return $this->msgObj->outputVip(false, $e->getMessage());
        }
    }

    /**
     * @param $jsonData
     * @return array
     * delivery_list:出仓产品列表
     * "vendor_type","barcode","box_no","pick_no","amount","po_no"
     * vendor_type:供应商类型，COMMON或3PL
     * barcode:条形码
     * box_no:供应商箱号
     * pick_no:拣货单号
     * amount:商品数量
     * po_no:po号，创建拣货单时的po单编号，多po用英文逗号隔开
     */
    private function __parseParams($jsonData)
    {
        $aData = json_decode($jsonData);
        return array(
            $aData['vendor_id'],       //供应商ID
            $aData['po_no'],           //PO单号，创建出仓单时的po单编号，多po用英文逗号隔开
            $aData['storage_no'],      //入库单号
            $aData['store_sn'],        //门店编码，OXO业务要求填写，非OXO业务不用填写
            $aData['delivery_list'],   //出仓产品列表
        );
    }

    public function getInsertData($jsonData)
    {
        $aData = json_decode($jsonData,true);
        $arrData = array();
        $db = OmsDatabase::$oms_db;
        $where = 'vendor_id=:vendor_id AND po_no=:po_no AND storage_no=:storage_no';
        $whereParams = array(':vendor_id'=>$aData['vendor_id'],':po_no'=>$aData['po_no'],':storage_no'=>$aData['storage_no']);
        $delivery_info = $db->fetchOne('id','t_vip_delivery_info',$where,$whereParams);
        foreach ($aData['delivery_list'] as $delivery) {
            $arrData[] = array(
                'dlv_id' => $delivery_info['id'],
                'po_no' => $delivery['po_no'],
                'pick_no' => $delivery['pick_no'],
                'amount' => $delivery['amount'],
                'vendor_type' => $delivery['vendor_type'],
                'barcode' => $delivery['barcode'],
                'box_no' => $delivery['box_no'],
                'create_time' => date('Y-m-d H:i:s'),
            );
        }
        return $arrData;
    }

}
