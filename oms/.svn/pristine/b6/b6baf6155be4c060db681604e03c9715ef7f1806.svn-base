<?php
/**
 * 商品同步接口(批量)过滤类
 * @author Renee
 */
class filterItemsCreate extends msg
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