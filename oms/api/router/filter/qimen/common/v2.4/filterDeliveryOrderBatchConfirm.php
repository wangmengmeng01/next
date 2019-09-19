<?php
/**
 * 奇门发货单确认接口(批量)过滤类
 */
class filterDeliveryOrderBatchConfirm extends msg
{
	public function confirm(&$requestData)
	{
	    //校验数据是否为空
	    if (empty($requestData)) {
	        return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
	    }
	    return $this->outputQimen('success');
	}
}
