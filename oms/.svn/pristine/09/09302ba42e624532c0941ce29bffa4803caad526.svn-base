<?php
/**
 * 发货单SN通知接口业务处理类
 */
require API_ROOT . '/router/interface/erp/qimen/common/erpRequest.php';
class erpSnReport extends erpRequest
{
	/**
	 * 发货单SN通知数据处理
	 */
	public function report($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
			//转发数据给wms
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
				if ($response['flag'] == 'success') {
					//写入数据库
					$this->insertSnReport($params);
					//返回
					return $this->msgObj->outputQimen('success', $response['message'], $response['code'], $response['addon']);
				} else {
					return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
				}
			} else {
				return $this->msgObj->outputQimen('failure', 'erp接口调用失败', 'S007');
			}
		}
	}
	
	/**
	 * 写入发货单SN数据到数据库
	 */
	public function insertSnReport($params)
	{
		//连接数据库
		global $db;
		//获取发货单SN单头基础信息接口参数与数据库字段对应关系
		$orderInfoColumnArr = $this->get_dataBase_relation('SnReportInfo');		
		$orderInfoKey = implode(',', array_values($orderInfoColumnArr)) . ',create_time';
			
		//获取发货SN中明细基础信息接口参数与数据库字段对应关系
		$orderDetailColumnArr = $this->get_dataBase_relation('SnReportDetail');
		$orderDetailKey = implode(',', array_values($orderDetailColumnArr)) . ',sn_id,create_time';
		
        //写入发货单SN单头信息
		$orderInfoValue = ":" . implode(",:", array_keys($orderInfoColumnArr)) . ",now()";
		$orderInfoSql = "INSERT INTO t_sn_record({$orderInfoKey}) VALUES({$orderInfoValue})";
		$model = $db->prepare($orderInfoSql);
		$values = array();		
		foreach ($orderInfoColumnArr as $k => $v)
		{
			if ($k == 'ownerCode') {
				if (empty($params['deliveryOrder']['ownerCode'])) {
					$values[':'.$k] = qimen_service::$_customerId;
				} else {
					$values[':'.$k] = $params['deliveryOrder']['ownerCode'];
				}				
			}  else {
			    $values[':'.$k] = empty($params['deliveryOrder'][$k]) ? '' : $params['deliveryOrder'][$k];
			}
		}
		$model->execute($values);
		$snId = $db->lastInsertID();
		
		//写入明细信息
		$orderDetailValue = ":" . implode(",:", array_keys($orderDetailColumnArr)) . ",'{$snId}',now()";
		$orderDetailSql = "INSERT IGNORE INTO t_sn_product_record({$orderDetailKey}) VALUES({$orderDetailValue})";
		if (!empty($params['items']['item'])) {
			if (empty($params['items']['item'][0])) {
				$params['items']['item'] = array($params['items']['item']);
			}
			$model = $db->prepare($orderDetailSql);
			foreach ($params['items']['item'] as $b)
			{
				$values = array();
				foreach ($orderDetailColumnArr as $k => $v)
				{
					$values[':'.$k] = empty($b[$k]) ? '' : $b[$k];
				}
				$model->execute($values);
			}
		}
		return true;
	}
}