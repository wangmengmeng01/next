<?php
/**
 * 奇门发货单缺货查询参数过滤类
 */
class filterItemLackQuery extends msg
{
	/**
	 * 查询参数校验
	 */
	public function search(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		//校验出库单号
		if (empty($requestData['deliveryOrderCode'])) {
			return $this->outputQimen('failure', '出库单号不能为空', 'S003');
		}
		//校验当前页
		if (empty($requestData['page'])) {
			return $this->outputQimen('failure', '当前页不能为空', 'S003');
		} elseif (!preg_match("/^\d+$/", $requestData['page'])) {
			return $this->outputQimen('failure', '当前页必须为大于0的整数', 'S003');
		}
		//校验每页orderLine条数（最多100条）
		if (empty($requestData['pageSize'])) {
			return $this->outputQimen('failure', '每页orderLine条数不能为空', 'S003');
		} elseif (!preg_match("/^\d+$/", $requestData['pageSize'])) {
			return $this->outputQimen('failure', '每页orderLine条数必须为整数', 'S003');
		} elseif ($requestData['pageSize'] > 100) {
			return $this->outputQimen('failure', '每页orderLine条数最大为100', 'S003');
		}
		
		return $this->outputQimen('success');
	}
}