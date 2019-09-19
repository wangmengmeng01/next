<?php

/**
 * 发货单缺货通知接口过滤类
 *
 */
class filterItemLackReport extends msg
{

    /**
     * 发货单缺货通知请求数据
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
        //校验ERP的发货单编码
        if (empty($request['deliveryOrderCode'])) {
            return $this->outputQimen('failure', 'ERP的发货单编码不能为空', 'S003');
        }
        //校验缺货回告创建时间
        if (empty($request['createTime'])) {
            return $this->outputQimen('failure', '缺货回告创建时间不能为空', 'S003');
        }
        //校验外部业务编码
        if (empty($request['outBizCode'])) {
            return $this->outputQimen('failure', '外部业务编码不能为空', 'S003');
        }
        //校验外部业务编码
        if (empty($request['outBizCode'])) {
            return $this->outputQimen('failure', '外部业务编码不能为空', 'S003');
        }
        //校验明细
        if (empty($item)) {
            return $this->outputQimen('failure', '商品明细不能为空', 'S003');
        } else {
            if (empty($item[0])) {
                $item = array($item);
            }
            foreach ($item as $v) {
                if (empty($v['itemCode'])) {
                    return $this->outputQimen('failure', '商品明细中商品编码不能为空', 'S003');
                }
                if (empty($v['planQty'])) {
                    return $this->outputQimen('failure', '商品明细中应发商品数量不能为空', 'S003');
                }
                if (empty($v['lackQty'])) {
                    return $this->outputQimen('failure', '商品明细中缺货商品数量不能为空', 'S003');
                }
            }
        }
		return $this->outputQimen('success');
    }
}