<?php
/**
 * 奇门发货单查询参数过滤类
 */
class filterDeliveryOrderQuery extends msg
{
	public function search(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		//校验发货单号
		if (empty($requestData['orderCode'])) {
			return $this->outputQimen('failure', '发货单号不能为空', 'S003');
		}
		
		return $this->outputQimen('success');
	}
}