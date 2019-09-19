<?php
/**
 * 仓储普通出库单下发接口过滤类
 * @author Renee
 *
 */
class filterStockOutOrderNotify extends msg{
    public function notify(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCnStorage(false, 'body中数据不能为空！', 'S003');
        }
        if (empty($requestData['ownerUserId'])) {
            return $this->outputCnStorage(false, '货主ID不能为空！', 'S003');
        }
        if (empty($requestData['storeCode'])) {
            return $this->outputCnStorage(false, '仓库编码不能为空！', 'S003');
        }
        if (empty($requestData['orderCode'])) {
            return $this->outputCnStorage(false, '订单号不能为空！', 'S003');
        }
        if (empty($requestData['orderType'])) {
            return $this->outputCnStorage(false, '订单类型不能为空！', 'S003');
        }
        if (empty($requestData['orderCreateTime'])) {
            return $this->outputCnStorage(false, '订单创建时间不能为空！', 'S003');
        }
        if (empty($requestData['receiverInfo']['receiverProvince'])) {
            return $this->outputCnStorage(false, '收件方省份信息不能为空！', 'S003');
        }
        if (empty($requestData['receiverInfo']['receiverCity'])) {
            return $this->outputCnStorage(false, '收件方城市信息不能为空！', 'S003');
        }
        /*
        if (empty($requestData['receiverInfo']['receiverArea'])) {
            return $this->outputCnStorage(false, '收件方区县信息不能为空！', 'S003');
        }
        if (empty($requestData['receiverInfo']['receiverTown'])) {
            return $this->outputCnStorage(false, '收件方镇信息不能为空！', 'S003');
        }*/
        if (empty($requestData['receiverInfo']['receiverAddress'])) {
            return $this->outputCnStorage(false, '收件方地址信息不能为空！', 'S003');
        }
        if (empty($requestData['receiverInfo']['receiverName'])) {
            return $this->outputCnStorage(false, '收件方名称信息不能为空！', 'S003');
        }
        /*
        if (empty($requestData['senderInfo']['senderAddress'])) {
            return $this->outputCnStorage(false, '发件方地址信息不能为空！', 'S003');
        }
        if (empty($requestData['senderInfo']['senderName'])) {
            return $this->outputCnStorage(false, '发件方姓名信息不能为空！', 'S003');
        }
        */
        if (empty($requestData['orderItemList']['orderItem'])) {
            return $this->outputCnStorage(false, '订单明细不能为空！', 'S003');
        } else {
            if (empty($requestData['orderItemList']['orderItem'][0])) {
                $requestData['orderItemList']['orderItem'] = array($requestData['orderItemList']['orderItem']);
            }
            foreach ($requestData['orderItemList']['orderItem'] as $orderItem) {
                if (empty($orderItem['orderItemId'])) {
                    return $this->outputCnStorage(false, 'ERP主键ID不能为空！', 'S003');
                }
                if (empty($orderItem['ownerUserId'])) {
                    return $this->outputCnStorage(false, '货主编码不能为空！', 'S003');
                }
                if (empty($orderItem['itemName'])) {
                    return $this->outputCnStorage(false, '商品名称不能为空！', 'S003');
                }
                if (empty($orderItem['itemCode'])) {
                    return $this->outputCnStorage(false, '商品编码不能为空！', 'S003');
                }
                if (empty($orderItem['inventoryType'])) {
                    return $this->outputCnStorage(false, '库存类型不能为空！', 'S003');
                }
                if (empty($orderItem['itemQuantity'])) {
                    return $this->outputCnStorage(false, '商品数量不能为空！', 'S003');
                }
                if (empty($orderItem['itemVersion'])) {
                    return $this->outputCnStorage(false, '商品版本号不能为空！', 'S003');
                }
                if (empty($orderItem['dueDate'])) {
                    return $this->outputCnStorage(false, '商品到货日期不能为空！', 'S003');
                }
                if (empty($orderItem['produceDate'])) {
                    return $this->outputCnStorage(false, '商品生产日期不能为空！', 'S003');
                }
            }
        }
        return $this->outputCnStorage(true);
    }
}