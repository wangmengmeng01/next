<?php
/**
 * Notes:创建出仓单
 * Date: 2019/1/9
 * Time: 16:39
 */
require_once API_ROOT . '/router/interface/erp/vip/erpRequest.php';
class createDelivery extends erpRequest
{
    /**
     * Notes:创建出仓单
     * Date: 2019/1/9
     * Time: 16:39
     */
    public function create($jsonData)
    {
        $db = OmsDatabase::$oms_db;
        try {
            # 数据检查
            if (empty($jsonData)) {
                return '错误：请求数据不能为空';
            }
            # 数据发送
            $params = array(
                'method' => 'createDelivery',
                'data'   => $jsonData
            );
            $response = $this->send($params);
            $responseData = json_decode($response);
            if($responseData->returnCode === '0') {
                //出仓单和拣货单对应关系有待商讨 （怎么对应）
                # 出仓单录入数据库
                $aData = json_decode($jsonData,true);
                $insertData =  array(
                    'vendor_id' => $aData['vendor_id'],
                    'po_no' => $aData['po_no'],
//                    'pick_no' => $aData['pick_no'],
                    'delivery_no' => $aData['delivery_no'],
                    'warehouse' => $_REQUEST['warehouseid'],
                    'sell_site' => $aData['warehouse'],
                    'delivery_time' => $aData['delivery_time'],
                    'arrival_time' => $aData['arrival_time'],
                    'race_time' => $aData['race_time'],
                    'carrier_name' => $aData['carrier_name'],
                    'tel' => $aData['tel'],
                    'driver' => $aData['driver'],
                    'driver_tel' => $aData['driver_tel'],
                    'plate_number' => $aData['plate_number'],
                    'delivery_method' => $aData['delivery_method'],
                    'store_sn' => $aData['store_sn'],
                    'carrier_code' => $aData['carrier_code'],
                    'jit_type' => $aData['jit_type'],
                    'delivery_id' => $responseData->result->delivery_id,
                    'storage_no' => $responseData->result->storage_no,
                    'create_time' => date('Y-m-d H:i:s')
                );
                $id = $db->insert('t_vip_delivery_info',$insertData,true);
                if ($id) {
                    echo '创建出仓单并存入数据库成功';exit;
                } else {
                    echo '创建出仓单成功但存入数据库失败';exit;
                }
            } else {
                throw new Exception('创建出仓单失败:'.$responseData->returnMessage);
            }
        } catch (\Exception $e) {
            return $e->getMessage();
        }
    }
}
