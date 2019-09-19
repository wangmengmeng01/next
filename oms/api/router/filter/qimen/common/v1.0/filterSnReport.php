<?php
/**
 * 奇门发货单SN通知接口参数过滤类
 */
class filterSnReport extends msg
{
	/**
	 * SN通知数据校验
	 */
	public function report(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		//校验总页数
		if (empty($requestData['totalPage']) && $requestData['totalPage'] != 0) {
			return $this->outputQimen('failure', '总页数不能为空', 'S003');
		} elseif (!preg_match("/^\d{1,}$/", $requestData['totalPage'])) {
			return $this->outputQimen('failure', '总页数必须为整数', 'S003');
		}
		//校验当前页
		if (empty($requestData['currentPage'])) {
			return $this->outputQimen('failure', '当前页不能为空', 'S003');
		} elseif (!preg_match("/^[1-9]{1,}$/", $requestData['currentPage'])) {
			return $this->outputQimen('failure', '当前页必须为大于0的整数', 'S003');
		}
		//校验每页记录的条数
		if (empty($requestData['pageSize']) && $requestData['pageSize'] != 0) {
			return $this->outputQimen('failure', '每页记录的条数不能为空', 'S003');
		} elseif (!preg_match("/^[1-9]{1,}$/", $requestData['pageSize'])) {
			return $this->outputQimen('failure', '每页记录的条数为大于0的整数', 'S003');
		}
		//校验出库单号
		if (empty($requestData['deliveryOrder']['deliveryOrderCode'])) {
			return $this->outputQimen('failure', '出库单号不能为空', 'S003');
		}
		//校验仓库编码
		if (empty($requestData['deliveryOrder']['warehouseCode'])) {
			return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		//校验出库单类型
		if (empty($requestData['deliveryOrder']['orderType'])) {
			return $this->outputQimen('failure', '出库单类型不能为空', 'S003');
		}
		//校验明细中商品编码
		if (empty($requestData['items']['item'])) {
			return $this->outputQimen('failure', 'item信息不能为空', 'S003');
		} else {
			if (empty($requestData['items']['item'][0])) {
				$requestData['items']['item'] = array($requestData['items']['item']);
			}
			if (count($requestData['items']['item']) > 1000) {
				return $this->outputQimen('failure', '一次最多只允许推送1000条item信息', 'S003');
			}
			foreach ($requestData['items']['item'] as $v)
			{
				//校验商品编码
				if (empty($v['itemCode'])) {
					return $this->outputQimen('failure', '商品编码不能为空', 'S003');
				}
				//校验商品序列号
				if (empty($v['sn'])) {
					return $this->outputQimen('failure', '商品序列号不能为空', 'S003');
				}
			}
		}
		
		return $this->outputQimen('success');
	}
}