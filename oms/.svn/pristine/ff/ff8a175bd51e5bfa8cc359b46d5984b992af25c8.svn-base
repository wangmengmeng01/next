<?php

/**
 * 入库单创建过滤类
 * @author Renee
 */
class filterEntryOrderCreate extends msg
{
    public function create(&$requestData)
    {
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
        $orderLine = $requestData['orderLines']['orderLine'];
        
        //校验单据总行数,当单据总行数为空时赋值为1
        if (empty($requestData['entryOrder']['totalOrderLines'])) {
            $requestData['deliveryOrder']['totalOrderLines'] = 1;
        } elseif (!preg_match("/^\d+$/", $requestData['entryOrder']['totalOrderLines'])) {
            return $this->outputQimen('failure', '单据总行数必须为整数', 'S003');
        }
        //校验入库单号
        if (empty($requestData['entryOrder']['entryOrderCode'])) {
            return $this->outputQimen('failure', '入库单号不能为空', 'S003');
        }
        //校验货主
        if (empty($requestData['entryOrder']['ownerCode'])) {
            return $this->outputQimen('failure', '货主编码不能为空', 'S003');
        } 
        
        //校验仓库
        if (empty($requestData['entryOrder']['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        
        //校验入库单明细
        if (empty($orderLine)) {
            return $this->outputQimen('failure', '入库单明细不能为空', 'S003');
        } else {
            if (empty($orderLine[0])) {
                $orderLine = array($orderLine);
            }
            foreach ($orderLine as $val) {
                if (empty($val['ownerCode'])) {
                    return $this->outputQimen('failure', '明细中货主不能为空', 'S003');
                } else {
                    if (empty($requestData['entryOrder']['ownerCode'])) {
                        $requestData['entryOrder']['ownerCode'] = $val['ownerCode'];
                    } else {
                        if ($val['ownerCode'] != $requestData['entryOrder']['ownerCode']) {
                            return $this->outputQimen('failure', '明细中货主与订单货主不一致', 'S003');
                        }
                    }
                }
                if (empty($val['itemCode'])) {
                    return $this->outputQimen('failure', '明细中商品编码不能为空', 'S003');
                }
                if (empty($val['planQty']) && $val['planQty'] != 0) {
                    return $this->outputQimen('failure', '明细中应收商品数量不能为空', 'S003');
                }
            }
        }
        return $this->outputQimen('success');
    }
}