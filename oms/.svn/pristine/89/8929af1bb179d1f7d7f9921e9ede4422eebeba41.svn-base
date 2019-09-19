<?php
/**
 * 订单流水查询接口过滤类
 * @author Renee
 *
 */
class filterOrderProcessQuery extends msg 
{

	/**
	 * 订单流水查询接口请求信息校验
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

		//校验单据号
		if (empty($request['orderCode'])) {
		    return $this->outputQimen('failure', '单据号不能为空', 'S003');
		}
		return $this->outputQimen('success');
	}
}