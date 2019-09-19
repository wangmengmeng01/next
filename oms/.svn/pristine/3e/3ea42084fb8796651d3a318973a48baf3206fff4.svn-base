<?php
/**
 * 订单状态批量查询接口过滤类
 * @author Renee
 *
 */
class filterOrderStatusBatchQuery extends msg 
{

	/**
	 * 订单状态批量查询接口请求信息校验
	 * @param $requestData
	 * @return xml
	 */
	public function search(&$requestData)
	{
	    //校验数据是否为空
	    if (empty($requestData)) {
	        return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
	    }
		$request = $requestData;

		//校验货主编码
		if (empty($request['ownerCode'])) {
		    return $this->outputQimen('failure', '货主编码不能为空', 'S003');
		}
		
		//校验订单最后操作时间
		if (empty($request['startTime'])) {
		    return $this->outputQimen('failure', '订单最后操作时间不能为空', 'S003');
		}
		return $this->outputQimen('success');
	}
}