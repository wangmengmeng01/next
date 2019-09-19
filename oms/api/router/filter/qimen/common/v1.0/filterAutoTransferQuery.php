<?php
/**
 * 奇门菜鸟自动流转查询接口（扩展）参数过滤类
 */
class filterAutoTransferQuery extends msg
{
	/**
	 * 参数校验
	 */
	public function search(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		//校验交易平台订单编码
		if (empty($requestData['orderSourceCode'])) {
			return $this->outputQimen('failure', '交易平台订单编码不能为空', 'S003');
		}
		
		return $this->outputQimen('success');
	}
}