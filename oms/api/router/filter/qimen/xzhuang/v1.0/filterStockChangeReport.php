<?php

/**
 * 库存异动通知接口过滤类
 *
 */
class filterStockChangeReport extends msg
{

    /**
     * 库存异动通知请求数据
     * @param  $requestData         
     * @return array
     *
     */
    public function report(&$requestData)
    {
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
        
        if (empty($requestData['items']['item'][0])) {
            $requestData['items']['item'] = array($requestData['items']['item']);
        }
        foreach ($requestData['items']['item'] as $v) {
            //校验货主编码
            if (empty($v['ownerCode'])) {
                return $this->outputQimen('failure', '货主编码不能为空', 'S003');
            }
            //校验仓库
            if (empty($v['warehouseCode'])) {
                return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
            }
            //引起异动的单据编码
            if (empty($v['orderCode'])) {
                return $this->outputQimen('failure', '引起异动的单据编码不能为空', 'S003');
            }
            //校验外部业务编码
            if (empty($v['outBizCode'])) {
                return $this->outputQimen('failure', '外部业务编码不能为空', 'S003');
            }
            //校验商品编码
            if (empty($v['itemCode'])) {
                return $this->outputQimen('failure', '商品编码不能为空', 'S003');
            }
            //校验商品变化量
            if (empty($v['quantity'])) {
                return $this->outputQimen('failure', '商品变化量不能为空', 'S003');
            }
        }
        return $this->outputQimen('success');
    }
}