<?php

/**
 * 退货入库单创建过滤类
 *
 */
class filterReturnOrderCreate extends msg
{

    /**
     * 过滤退货入库单创建订单数据
     * @param  $requestData         
     * @return array
     *
     */
    public function create(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        $request = $requestData['returnOrder'];
        $orderLine = $requestData['orderLines']['orderLine'];
        
        //校验退货入库单编码
        if (empty($request['returnOrderCode'])) {
            return $this->outputQimen('failure', '退货入库单编码不能为空', 'S003');
        }
        //校验仓库
        if (empty($request['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        //校验原出库单号(ERP分配)
        if (empty($request['preDeliveryOrderCode'])) {
            return $this->outputQimen('failure', '原出库单号(ERP分配)不能为空', 'S003');
        } 
        //校验物流公司编码
        if (empty($request['logisticsCode'])) {
            return $this->outputQimen('failure', '物流公司编码不能为空', 'S003');
        }
        //校验发件人信息
        /*
        if (empty($request['senderInfo']['name'])) {
            return $this->outputQimen('failure', '发件人信息中姓名不能为空', 'S003');
        }
        if (empty($request['senderInfo']['mobile'])) {
            return $this->outputQimen('failure', '发件人信息中移动电话不能为空', 'S003');
        }
        if (empty($request['senderInfo']['province'])) {
            return $this->outputQimen('failure', '发件人信息中省份不能为空', 'S003');
        }
        if (empty($request['senderInfo']['city'])) {
            return $this->outputQimen('failure', '发件人信息中城市不能为空', 'S003');
        }
        if (empty($request['senderInfo']['detailAddress'])) {
            return $this->outputQimen('failure', '发件人信息中详细地址不能为空', 'S003');
        }
        */
        //校验退货入库单明细
        if (empty($orderLine)) {
            return $this->outputQimen('failure', '退货入库单明细不能为空', 'S003');
        } else {
            if (empty($orderLine[0])) {
                $orderLine = array($orderLine);
            }
            foreach ($orderLine as $val) {
                if (empty($val['ownerCode'])) {
                    return $this->outputQimen('failure', '退货入库单明细中货主不能为空', 'S003');
                }
                if (empty($val['itemCode'])) {
                    return $this->outputQimen('failure', '退货入库单明细中商品编码不能为空', 'S003');
                }
                if (empty($val['planQty'])) {
                    return $this->outputQimen('failure', '退货入库单明细中应收商品数量不能为空', 'S003');
                }
            }
        }
        return $this->outputQimen('success');
    }
}