<?php
/**
 * 奇门库存 查询接口过滤类
 */
class filterInventoryQuery extends msg
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

		if (empty($requestData['criteriaList']['criteria'])) {
		    return $this->outputQimen('failure', '查询信息不能为空', 'S003');
		} else {
		    if (empty($requestData['criteriaList']['criteria'][0])) {
		        $requestData['criteriaList']['criteria'] = array($requestData['criteriaList']['criteria']);
		    }

		    qimen_service::$_warehouseCode = $requestData['criteriaList']['criteria'][0]['warehouseCode'];

		    foreach ($requestData['criteriaList']['criteria'] as $val) {
		        if (empty($val['itemCode'])) {
		            return $this->outputQimen('failure', '商品编码不能为空', 'S003');
		        }
		    }
		}
		return $this->outputQimen('success');
	}
}