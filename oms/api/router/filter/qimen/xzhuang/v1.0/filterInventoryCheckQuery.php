<?php
/**
 * 奇门库存盘点查询接口参数过滤类
 */
class filterInventoryCheckQuery extends msg
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
		//校验盘点单编码
		if (empty($requestData['checkOrderCode'])) {
			return $this->outputQimen('failure', '盘点单编码不能为空', 'S003');
		}

        qimen_service::$_warehouseCode = $requestData['warehouseCode'];

		//校验当前页
		if (empty($requestData['page'])) {
			return $this->outputQimen('failure', '当前页不能为空', 'S003');
		} elseif (!preg_match("/^\d+$/", $requestData['page']) || $requestData['page'] < 1) {
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