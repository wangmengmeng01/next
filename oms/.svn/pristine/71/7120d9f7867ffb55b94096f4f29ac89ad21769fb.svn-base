<?php

/**
 * 库存盘点通知接口过滤类
 *
 */
class filterInventoryReport extends msg
{

    /**
     * 库存盘点通知请求数据
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
        $request = $requestData;
        $item = $request['items']['item'];
        
        //校验仓库
        if (empty($request['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        //校验盘点单编码
        if (empty($request['checkOrderCode'])) {
            return $this->outputQimen('failure', '盘点单编码不能为空', 'S003');
        }
        //校验货主编码
        if (empty($request['ownerCode'])) {
            return $this->outputQimen('failure', '货主编码不能为空', 'S003');
        }
        //校验外部业务编码
        if (empty($request['outBizCode'])) {
            return $this->outputQimen('failure', '外部业务编码不能为空', 'S003');
        }
        //校验商品明细
        if (empty($item)) {
            return $this->outputQimen('failure', '商品明细不能为空', 'S003');
        } else {
            if (empty($item[0])) {
                $item = array($item);
            }
            foreach ($item as $k => $v) {
                if (empty($v['itemCode'])) {
                    return $this->outputQimen('failure', '商品编码不能为空', 'S003');
                }
                if (empty($v['quantity'])) {
                    return $this->outputQimen('failure', $k . '盘盈盘亏商品变化量不能为空', 'S003');
                }   
            }
        }
		return $this->outputQimen('success');
    }
}