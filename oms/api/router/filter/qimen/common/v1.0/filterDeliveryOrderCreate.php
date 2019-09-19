<?php
/**
 * 奇门发货单创建过滤类
 */
class filterDeliveryOrderCreate extends msg
{
	public function create(&$requestData)
	{
		//连接数据库
		global $db;
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
		} elseif (!in_array($requestData['deliveryOrder']['orderType'], array('JYCK', 'HHCK', 'BFCK', 'QTCK'))) {
			return $this->outputQimen('failure', '该出库单类型' . $requestData['deliveryOrder']['orderType'] . '不存在', 'S003');
		}
		//校验原出库单号（ERP分配），出库单类型为换货出库时必填
		if ($requestData['deliveryOrder']['orderType'] == 'HHCK' && empty($requestData['deliveryOrder']['preDeliveryOrderCode'])) {
			return $this->outputQimen('failure', '出库单类型为换货出库时原出库单号（ERP分配）不能为空', 'S003');
		}
		//校验仓库编码
		if (empty($requestData['deliveryOrder']['warehouseCode'])) {
			return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		//校验订单来源平台编码
		if (empty($requestData['deliveryOrder']['sourcePlatformCode'])) {
			return $this->outputQimen('failure', '订单来源平台编码不能为空', 'S003');
		} else {
		    $sourcePlatformCode = $requestData['deliveryOrder']['sourcePlatformCode'];
		    $platSql = "SELECT * FROM t_qimen_cn_platform_relation WHERE qimen_platform_code=:qimen_platform_code AND is_valid=1";
		    $model = $db->prepare($platSql);
		    $model->bindParam(':qimen_platform_code', $sourcePlatformCode);
		    $model->execute();
		    $cnPlatform = $model->fetch(PDO::FETCH_ASSOC);
		    
		    if (!empty($cnPlatform['cn_platform_code'])) {
		        $requestData['deliveryOrder']['sourcePlatformCode'] = $cnPlatform['cn_platform_code'];
		        $platformCodeStr = "<sourcePlatformCode>" . $cnPlatform['cn_platform_code'] . "</sourcePlatformCode>";
		        qimen_service::$_data = preg_replace("/<sourcePlatformCode>(.*)<\/sourcePlatformCode>/s", $platformCodeStr, qimen_service::$_data);
		    } else {
		        $requestData['deliveryOrder']['sourcePlatformCode'] = "OTHERS";
		        $platformCodeStr = "<sourcePlatformCode>OTHERS</sourcePlatformCode>";
		        qimen_service::$_data = preg_replace("/<sourcePlatformCode>(.*)<\/sourcePlatformCode>/s", $platformCodeStr, qimen_service::$_data);
		    }
		}
		//校验发货单创建时间
		if (empty($requestData['deliveryOrder']['createTime'])) {
			return $this->outputQimen('failure', '发货单创建时间不能为空', 'S003');
		} elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['deliveryOrder']['createTime'])) {
			return $this->outputQimen('failure', '发货单创建时间格式错误', 'S003');
		}
		//校验前台订单 (店铺订单) 创建时间 (下单时间)
		if (empty($requestData['deliveryOrder']['placeOrderTime'])) {
			return $this->outputQimen('failure', '前台订单 (店铺订单) 创建时间 (下单时间)不能为空', 'S003');
		} elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['deliveryOrder']['placeOrderTime'])) {
			return $this->outputQimen('failure', '前台订单 (店铺订单) 创建时间 (下单时间)格式错误', 'S003');
		}
		//校验操作 (审核) 时间
	    if (empty($requestData['deliveryOrder']['operateTime'])) {
			return $this->outputQimen('failure', '操作 (审核) 时间不能为空', 'S003');
		} elseif (!preg_match("/^[1-9]{1}\d{3}-[0-1]{1}\d{1}-[0-3]{1}\d{1}\s[0-2]{1}\d{1}:[0-5]{1}\d{1}:[0-5]{1}\d{1}$/", $requestData['deliveryOrder']['operateTime'])) {
			return $this->outputQimen('failure', '操作 (审核) 时间格式错误', 'S003');
		}
		//校验店铺名称
		if (empty($requestData['deliveryOrder']['shopNick'])) {
			return $this->outputQimen('failure', '店铺名称不能为空', 'S003');
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
		if (empty($requestData['deliveryOrder']['receiverInfo']['province'])) {
			return $this->outputQimen('failure', '收件人省份不能为空', 'S003');
		}
		if (empty($requestData['deliveryOrder']['receiverInfo']['city'])) {
			return $this->outputQimen('failure', '收件人城市不能为空', 'S003');
		}
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
				} else {
					$sql = "SELECT customer_id FROM t_qimen_customer_bind WHERE customer_id=:customer_id AND is_valid=1";
					$model = $db->prepare($sql);
					$model->bindParam(':customer_id', $val['ownerCode']);
					$model->execute();
					$rs = $model->fetch(PDO::FETCH_ASSOC);
					if (empty($rs)) {
						return $this->outputQimen('failure', '货主编码不存在或无效', 'S003');
					} elseif ($rs['customer_id'] != $val['ownerCode']) {
						return $this->outputQimen('failure', '货主ID大小写错误', 'S003');
					}
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