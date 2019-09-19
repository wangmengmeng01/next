<?php
/**
 * Notes:创建出仓单2.0
 * Date: 2019/1/9
 * Time: 16:39
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';
class createMultiPoDelivery extends erpRequest
{
    /**
     * Notes:创建出仓单2.0
     * Date: 2019/1/9
     * Time: 16:39
     */
    public function create($jsonData)
    {
        $db = OmsDatabase::$oms_db;
        $db->getPdo()->beginTransaction();
        try {

            # 数据检查
            if (empty($jsonData)) {
                return '错误：请求数据不能为空';
            }
            # 判断是否已经创建
            $isCreated = $this->isCreated($jsonData);
            if ($isCreated) {
                $db->getPdo()->rollBack();
                return json_encode(array(
                    'returnCode' => 0,
                    'msg' => '已经创建过',
                    'result' => array(
                        'delivery_id' => $isCreated['delivery_id'],
                        'storage_no' => $isCreated['storage_no']
                    )
                ));
            }
            # 出仓单录入数据库
            $insertData = $this->getInsertData($jsonData);
            $re = $db->insertAll('t_vip_delivery_info',$insertData);
            if($re) {
                # 数据发送
                $params = array(
                    'method' => 'createMultiPoDelivery',
                    'data'   => $jsonData
                );
                $response = $this->send($params);
                $responseData = json_decode($response);
                if($responseData->returnCode === 0) {
                    $db->getPdo()->commit();
                } else {
                    $db->getPdo()->rollBack();
                }
                return $response;
            } else {
                $db->getPdo()->rollBack();
                return '错误：数据无法录入';
            }


        } catch (\Exception $e) {
            $db->getPdo()->rollBack();
            return $e->getMessage();
        }
    }

    private function __parseParams($jsonData)
    {
        $aData = json_decode($jsonData,true);
        $createMultiPoDeliveryRequest1 = new \vipapis\delivery\CreateMultiPoDeliveryRequest();
        //供应商ID
        $createMultiPoDeliveryRequest1->vendor_id = $aData['vendor_id'];
        //po单号，多po以英文逗号隔开
        $createMultiPoDeliveryRequest1->po_no = $aData['po_no'];
        //运单号
        $createMultiPoDeliveryRequest1->delivery_no = $aData['delivery_no'];
        //送货仓库
        $createMultiPoDeliveryRequest1->warehouse = $aData['warehouse'];
        //送货时间
        $createMultiPoDeliveryRequest1->delivery_time = $aData['delivery_time'];
        //要求到货时间
        $createMultiPoDeliveryRequest1->arrival_time = $aData['arrival_time'];
        //承运商编码
        $createMultiPoDeliveryRequest1->carrier_code = $aData['carrier_code'];
        //配送方式 1：汽运，2：空运
        $createMultiPoDeliveryRequest1->delivery_method = $aData['delivery_method'];
        //门店编码
        $createMultiPoDeliveryRequest1->store_sn = $aData['store_sn'];
        //1：OXO，2：仓中仓，3：预调拨，不填则回普通jit类型
        $createMultiPoDeliveryRequest1->jit_type = $aData['jit_type'];
        return $createMultiPoDeliveryRequest1;
    }

    public function isCreated($jsonData)
    {
        $db = OmsDatabase::$oms_db;
        $aData = json_decode($jsonData,true);
        $where = 'vendor_id=:vendor_id AND po_no=:po_no AND delivery_no=:delivery_no';
        $whereParams = array(':vendor_id'=>$aData['vendor_id'],':po_no'=>$aData['po_no'],':delivery_no'=>$aData['delivery_no']);
        $delivery_info = $db->fetchOne('id,delivery_id,storage_no','t_vip_delivery_info',$where,$whereParams);
        return $delivery_info;
    }

    public function getInsertData($jsonData)
    {
        $aData = json_decode($jsonData,true);
        return array(
            'vendor_id' => $aData['vendor_id'],
            'po_no' => $aData['po_no'],
            'delivery_no' => $aData['delivery_no'],
            'sell_site' => $aData['warehouse'],
            'delivery_time' => $aData['delivery_time'],
            'arrival_time' => $aData['arrival_time'],
            'delivery_method' => $aData['delivery_method'],
            'store_sn' => $aData['store_sn'],
            'carrier_code' => $aData['carrier_code'],
            'jit_type' => $aData['jit_type'],
            'create_time' => date('Y-m-d H:i:s'),
        );
    }

}
