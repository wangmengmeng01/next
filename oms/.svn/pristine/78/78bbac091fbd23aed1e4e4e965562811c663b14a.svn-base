<?php
/**
 * 奇门发货单创建过滤类
 */
class filterDeliveryOrderCreate extends msg
{
	public function create(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		//校验出库单号
		if (empty($requestData['deliveryOrder']['deliveryOrderCode'])) {
			return $this->outputQimen('failure', '出库单号不能为空', 'S003');
		}

		//校验出库单类型
		if (empty($requestData['deliveryOrder']['orderType'])) {
			return $this->outputQimen('failure', '出库单类型不能为空', 'S003');
		} 
		//校验原出库单号（ERP分配），出库单类型为换货出库时必填
		if ($requestData['deliveryOrder']['orderType'] == 'HHCK' && empty($requestData['deliveryOrder']['preDeliveryOrderCode'])) {
			return $this->outputQimen('failure', '出库单类型为换货出库时原出库单号（ERP分配）不能为空', 'S003');
		}
		//校验原出库单号（WMS分配），出库单类型为换货出库时必填
		if ($requestData['deliveryOrder']['orderType'] == 'HHCK' && empty($requestData['deliveryOrder']['preDeliveryOrderId'])) {
			return $this->outputQimen('failure', '出库单类型为换货出库时原出库单号（WMS分配）不能为空', 'S003');
		}
		//校验物流公司编码
		if (empty($requestData['deliveryOrder']['logisticsCode'])) {
			return $this->outputQimen('failure', '物流公司编码不能为空', 'S003');
		}
		//校验收件人信息
	    if (empty($requestData['deliveryOrder']['receiverInfo']['name'])) {
			return $this->outputQimen('failure', '收件人姓名不能为空', 'S003');
		}
		if (empty($requestData['deliveryOrder']['receiverInfo']['mobile']) && empty($requestData['deliveryOrder']['receiverInfo']['tel'])) {
			return $this->outputQimen('failure', '收件人固定电话和移动电话不能都为空', 'S003');
		} else {
		    if (empty($requestData['deliveryOrder']['receiverInfo']['mobile'])) {
		        $requestData['deliveryOrder']['receiverInfo']['mobile'] = $requestData['deliveryOrder']['receiverInfo']['tel'];
		    }
		}
		//校验详细地址
		if (empty($requestData['deliveryOrder']['receiverInfo']['detailAddress'])) {
			return $this->outputQimen('failure', '收件人详细地址不能为空', 'S003');
		}
		//校验发票
		if ($requestData['deliveryOrder']['invoiceFlag'] == 'Y') {	
			//发票信息处理成2维数组		
			if (empty($requestData['deliveryOrder']['invoices']['invoice'][0])) {
				$requestData['deliveryOrder']['invoices']['invoice'] = array($requestData['deliveryOrder']['invoices']['invoice']);
			}
			foreach ($requestData['deliveryOrder']['invoices']['invoice'] as $val)
			{
				if (empty($val['type'])) {
					return $this->outputQimen('failure', '发票类型不能为空', 'S003');
				} elseif (!in_array($val['type'], array('INVOICE', 'VINVOICE', 'EVINVOICE'))) {
					return $this->outputQimen('failure', '发票类型' . $val['type'] . '不存在', 'S003');
				}
			}
		}
		//校验订单明细中的信息
		if (empty($requestData['orderLines']['orderLine'])) {
			return $this->outputQimen('failure', '发货单明细信息不能为空', 'S003');
		} else {
			if (empty($requestData['orderLines']['orderLine'][0])) {
				$requestData['orderLines']['orderLine'] =  array($requestData['orderLines']['orderLine']);
			}
			foreach ($requestData['orderLines']['orderLine'] as $val)
			{
				//校验货主编码
				if (empty($val['ownerCode'])) {
					return $this->outputQimen('failure', '货主编码不能为空', 'S003');
				} 
				//校验商品编码
				if (empty($val['itemCode'])) {
					return $this->outputQimen('failure', '商品编码不能为空', 'S003');
				} 
				//校验应发商品数量
				if (empty($val['planQty']) && $val['planQty'] != 0) {
					return $this->outputQimen('failure', '应发商品数量不能为空', 'S003');
				} elseif (!preg_match("/^\d+$/", $val['planQty'])) {
					return $this->outputQimen('failure', '应发商品数量必须为整数', 'S003');
				}
			}
		}
		return $this->outputQimen('success');
	}
}