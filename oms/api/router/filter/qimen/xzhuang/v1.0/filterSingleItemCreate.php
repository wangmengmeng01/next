<?php
/**
 * 商品同步接口过滤类
 * @author Renee
 */
class filterSingleItemCreate extends msg
{
	public function create(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}

		$item = $requestData['item'];
		//校验操作类型
		if (empty($requestData['actionType'])) {
		    return $this->outputQimen('failure', '操作类型不能为空', 'S003');
		} else {
		    //判断操作类型是否正确
		    if (!in_array($requestData['actionType'], array("add","update"))) {
		        return $this->outputQimen('failure', '操作类型错误或者无效', 'S003');
		    }
		}
		//校验货主
		if (empty($requestData['ownerCode'])) {
		    return $this->outputQimen('failure', '货主编码不能为空', 'S003');
		}

		if (empty($requestData['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }

        qimen_service::$_warehouseCode = $requestData['warehouseCode'];

		//校验商品编码
		if (empty($item['itemCode'])) {
		    return $this->outputQimen('failure', '商品编码不能为空', 'S003');
		}
		//校验商品名称
		if (empty($item['itemName'])) {
		    return $this->outputQimen('failure', '商品名称不能为空', 'S003');
		}
		//校验商品类型                         
		if (empty($item['itemType'])) {
            return $this->outputQimen('failure', '商品类型不能为空', 'S003');
		}
		return $this->outputQimen('success');
	}
}