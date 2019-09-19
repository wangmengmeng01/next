<?php
class filterConsignOrderNotify extends msg{
    public function notify(&$requestData){
        if (empty($requestData)) {
            return $this->outputCnStorage(false, '请求的数据为空！', 'S003');
        }
        if (empty($requestData['ownerUserId'])) {
            return $this->outputCnStorage(false, '货主编码不能为空！' , 'S003');
        }
        if (empty($requestData['storeCode'])) {
            return $this->outputCnStorage(false, '仓库编码不能为空！' , 'S003');
        }
        if (empty($requestData['orderCode'])) {
            return $this->outputCnStorage(false, '仓库订单编码不能为空！' , 'S003');
        }
        if (empty($requestData['erpOrderCode'])) {
            return $this->outputCnStorage(false, 'erp订单编码不能为空！' , 'S003');
        }
        if (empty($requestData['orderType'])) {
            return $this->outputCnStorage(false, '订单类型不能为空！' , 'S003');
        }
        if (empty($requestData['orderSource'])) {
            return $this->outputCnStorage(false, '订单来源不能为空！' , 'S003');
        }
        if (empty($requestData['orderCreateTime'])) {
            return $this->outputCnStorage(false, '订单创建时间不能为空！' , 'S003');
        }
        /*if (empty($requestData['receiverInfo']['receiverZipCode'])) {
            return $this->outputCnStorage(false, '收件方邮编不能为空！' , 'S003');
        }*/
        if (empty($requestData['receiverInfo']['receiverProvince'])) {
            return $this->outputCnStorage(false, '收件方省份不能为空！' , 'S003');
        }
        if (empty($requestData['receiverInfo']['receiverCity'])) {
            return $this->outputCnStorage(false, '收件方城市不能为空！' , 'S003');
        }
        if (empty($requestData['receiverInfo']['receiverAddress'])) {
            return $this->outputCnStorage(false, '收件方详细地址不能为空！' , 'S003');
        }
        if (empty($requestData['receiverInfo']['receiverName'])) {
            return $this->outputCnStorage(false, '收件方姓名不能为空！' , 'S003');
        }
        if (empty($requestData['orderCreateTime'])) {
            return $this->outputCnStorage(false, '订单创建时间不能为空！' , 'S003');
        }
        if (empty($requestData['orderItemList']['orderItem'])) {
            return $this->outputCnStorage(false, '订单明细不能为空！' , 'S003');
        } else {
            if (empty($requestData['orderItemList']['orderItem'][0])) {
                $requestData['orderItemList']['orderItem'] = array($requestData['orderItemList']['orderItem']);
            }
            foreach ($requestData['orderItemList']['orderItem'] as $o_k=>$o_v) {
                if (empty($o_v['orderItemId'])) {
                    return $this->outputCnStorage(false, '行号不能为空！' , 'S003');
                }
                if (empty($o_v['userId'])) {
                    return $this->outputCnStorage(false, '卖家ID不能为空！' , 'S003');
                }
                if (empty($o_v['ownerUserId'])) {
                    return $this->outputCnStorage(false, '货主ID不能为空！' , 'S003');
                }
                if (empty($o_v['itemCode'])) {
                    return $this->outputCnStorage(false, '商品编码不能为空！' , 'S003');
                }
                if (empty($o_v['inventoryType'])) {
                    return $this->outputCnStorage(false, '库存类型不能为空' , 'S003');
                }
                if (empty($o_v['itemQuantity'])) {
                    return $this->outputCnStorage(false, '商品数量不能为空！' , 'S003');
                }
            }
        }
        return $this->outputCnStorage(true);
    }
}