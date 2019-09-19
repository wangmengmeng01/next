<?php
/**
 * 奇门库存查询接口业务处理类
 */
require API_ROOT . '/router/interface/wms/qimen/xzhuang/wmsRequest.php';
class wmsInventoryQuery extends wmsRequest
{
	/**
	 * 库存查询
	 */
	public function search($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
			//转发数据给wms
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
				if ($response['flag'] == 'failure' && $response['code'] == 'E001') {
					return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
				} else {
					qimen_service::$_queryFlag = true;
					$xmlObj = new xml();
					$xmlArr = $xmlObj->xmlStr2array($response['addon']['return_msg']);
					$items = $xmlArr['items']['item'];
					$this->updateInventory($items);
					return $this->msgObj->outputQimen($response['flag'], $response['message'], $response['code'], $response['addon']);
				}
			} else {
				return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
			}
		}
	}
	
	//更新库存数据
	public function updateInventory($items) {
	    global $db;
	    
	    if (!empty($items)) {
	        if (empty($items[0])) {
	            $items = array($items);
	        }
	        $columns = $this->get_dataBase_relation('product_inventory');
	        $columnKey = implode(',', array_values($columns)) . ',create_time';
	        $columnValue = ':' . implode(',:', array_values($columns)) . ',now()';
	        foreach ($items as $v) {
	            $sql = "REPLACE INTO t_product_inventory({$columnKey}) VALUES($columnValue)";
	            $model = $db->prepare($sql);
	            $values = array();
	            foreach ($columns as $a => $b) {
	                $values[':' . $b] = empty($v[$a]) ? '' : $v[$a] ;
	            }
	            $values[':customer_id'] = qimen_service::$_customerId;
	            $model->execute($values);
	        }
	    }
	}
}