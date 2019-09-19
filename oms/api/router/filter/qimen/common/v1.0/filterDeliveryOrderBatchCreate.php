<?php
/**
 * 发货单创建接口(批量)过滤类
 * @author Renee
 */
class filterDeliveryOrderBatchCreate extends msg
{
	public function create(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
		    return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		return $this->outputQimen('success');
	}
}