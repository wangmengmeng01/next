<?php
/**
 * 奇门单据挂起（恢复）接口操作类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsOrderPending extends wmsRequest
{
    public function operate($params)
    {
        if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
			//转发数据给wms
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
			    if ($response['flag'] == 'success') {
			        global $db;
			        //单据类型
			        $entryType = array('SCRK','LYRK','CCRK','CGRK','DBRK','QTRK','B2BRK','XNRK','THRK','HHRK');
			        $stockOutType = array('PTCK','DBCK','B2BCK');//,'QTCK'
			        $deliveryType = array('JYCK','HHCK','BFCK');//,'QTCK'
			        $cnType = array('CNJG');
			        $specialType = array('QTCK');//特殊处理(因为出库单和发货单都有这个单据类型)
			        
			        $customerId = empty($params['ownerCode']) ? qimen_service::$_customerId : $params['ownerCode'];
			        if (in_array($params['orderType'], $entryType)) {
			            $tableName = 't_inbound_info';
			            $codeName = 'order_no';
			        } elseif (in_array($params['orderType'], $stockOutType)) {
			            $tableName = 't_outbound_info';
			            $codeName = 'order_no';
			        } elseif (in_array($params['orderType'], $deliveryType)) {
			            $tableName = 't_delivery_order_info';
			            $codeName = 'delivery_order_code';
			        } elseif (in_array($params['orderType'], $cnType)) {
			            $tableName = 't_store_process_order_info';
			            $codeName = 'process_order_code';
			        } elseif (in_array($params['orderType'], $specialType)) {
			            $this->updateOrderStatus('t_delivery_order_info', 'delivery_order_code', $customerId, $params);
			            $tableName = 't_outbound_info';
			            $codeName = 'order_no';
			        }
			        $this->updateOrderStatus($tableName, $codeName, $customerId, $params);
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
     * 修改单据状态
     * @param $tableName   表名
     * @param $codeName    单据号
     * @param $customerId  货主编码
     * @param $params      请求数据
     */
    public function updateOrderStatus($tableName,$codeName,$customerId,$params) {
        global $db;
        if ($tableName == 't_delivery_order_info') {
            $sql = "SELECT delivery_id FROM " . $tableName . " WHERE " . $codeName . "=:order_code AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND order_type=:order_type AND is_valid=1";
        } else {
            $sql = "SELECT order_id FROM " . $tableName . " WHERE " . $codeName . "=:order_code AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND order_type=:order_type AND is_valid=1";
        }
        $model = $db->prepare($sql);
        $model->bindParam(':order_code', $params['orderCode']);
        $model->bindParam(':customer_id', $customerId);
        $model->bindParam(':warehouse_code', $params['warehouseCode']);
        $model->bindParam(':order_type', $params['orderType']);
        $model->execute();
        $orderIdArr = $model->fetchAll(PDO::FETCH_ASSOC);
        if (!empty($orderIdArr)) {
            foreach ($orderIdArr as $orderIdVal) {
                $updateSql = "UPDATE " . $tableName . " SET order_status=:order_status WHERE order_id=:order_id";
                $updateModel = $db->prepare($updateSql);
                $updateModel->bindParam(':order_status', $params['actionType']);
                $updateModel->bindParam(':order_id', $orderIdVal['order_id']);
                $updateModel->execute();
            }
        }
    }
}

