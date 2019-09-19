<?php
/**
 * 确认发货接口过滤类
 * @author Renee
 *
 */
class filterConsignOrderConfirm extends msg {
    public function confirm(&$requestData){
        if (empty($requestData)) {
            return $this->outputCnStorage(false, "body中数据不能为空!", 'S003');
        }
        if (empty($requestData['orderConfirmTime'])) {
            return $this->outputCnStorage(false, "仓库订单完成时间不能为空!", 'S003');
        }
        if (empty($requestData['orderCode'])) {
            return $this->outputCnStorage(false, "菜鸟订单编码不能为空!", 'S003');
        }

        $requestData['confirmType']=$requestData['confirmType']==='0'?0:$requestData['confirmType'];
        if (getType($requestData['confirmType'])!='integer' && empty($requestData['confirmType'])) {
            return $this->outputCnStorage(false, "支持出入库单多次确认不能为空!", 'S003');
        }
        if (empty($requestData['orderType'])) {
            return $this->outputCnStorage(false, "订单类型不能为空!", 'S003');
        }
        if (empty($requestData['orderItems']['orderItem'])) {
            return $this->outputCnStorage(false, "订单明细不能为空!", 'S003');
        } else {
            if (empty($requestData['orderItems']['orderItem'][0])) {
                $requestData['orderItems']['orderItem'] = array($requestData['orderItems']['orderItem']);
            }
            foreach ($requestData['orderItems']['orderItem'] as $orderItem) {
                if (empty($orderItem['orderItemId'])) {
                    return $this->outputCnStorage(false, "订单明细行号不能为空!", 'S003');
                }
                if (empty($orderItem['items']['item'])) {
                    return $this->outputCnStorage(false, "订单明细商品信息不能为空!", 'S003');
                } else {
                    if (empty($orderItem['items']['item'][0])) {
                        $orderItem['items']['item'] = array($orderItem['items']['item']);
                    }
                    foreach ($orderItem['items']['item'] as $i_k=>$i_v) {
                        if (empty($i_v['inventoryType'])) {
                            return $this->outputCnStorage(false, "订单明细商品库存类型不能为空!", 'S003');
                        }
                        if (empty($i_v['quantity'])) {
                            return $this->outputCnStorage(false, "订单明细商品数量不能为空!", 'S003');
                        }
                    }
                }
            }
        }

        return $this->outputCnStorage(true);
    }
    
    
}