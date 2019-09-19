<?php
/**
 * 出库订单确认接口过滤类
 * @author Renee
 *
 */
class filterStockOutOrderConfirm extends msg {
    public function confirm(&$requestData){
        if (empty($requestData)) {
            return $this->outputCnStorage(false, "body中数据不能为空!", 'S003');
        }
        if (empty($requestData['orderCode'])) {
            return $this->outputCnStorage(false, "菜鸟订单id不能为空!", 'S003');
        }
        if (empty($requestData['orderType'])) {
            return $this->outputCnStorage(false, "订单类型不能为空!", 'S003');
        }
        if (empty($requestData['orderConfirmTime'])) {
            return $this->outputCnStorage(false, "仓库订单完成时间不能为空!", 'S003');
        }
        
        return $this->outputCnStorage(true);
    }
    
    
}