<?php
/**
 * 入库单确认接口过滤类
 * @author Renee
 *
 */
class filterEntryOrderConfirm extends msg 
{

	/**
	 * 入库单确认接口请求信息校验
	 * @param $requestData
	 * @return xml
	 */
	public function confirm(&$requestData)
	{
	    //连接数据库
	    global $db;
	    //校验数据是否为空
	    if (empty($requestData)) {
	        return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
	    }
		
		//校验入库单编码
		if (empty($requestData['entryOrder']['entryOrderCode'])) {
		    return $this->outputQimen('failure', '入库单编码不能为空', 'S003');
		} 
		
		//校验仓库编码
		if (empty($requestData['entryOrder']['warehouseCode'])) {
		    return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		//校验外部业务编码
		if (empty($requestData['entryOrder']['outBizCode'])) {
		    return $this->outputQimen('failure', '外部业务编码不能为空', 'S003');
		}
		//校验入库单状态
		if (empty($requestData['entryOrder']['status'])) {
		    return $this->outputQimen('failure', '入库单状态不能为空', 'S003');
		}
		//校验入库单回传明细
		if (empty($requestData['orderLines']['orderLine'])) {
		    return $this->outputQimen('failure', '入库单明细不能为空', 'S003');
		} else {
		    if (empty($requestData['orderLines']['orderLine'][0])) {
		        $requestData['orderLines']['orderLine'] = array($requestData['orderLines']['orderLine']);
		    }
		    foreach ($requestData['orderLines']['orderLine'] as $v) {
		        if (empty($v['itemCode'])) {
		            return $this->outputQimen('failure', '入库单明细中商品编码不能为空', 'S003');
		        }
		        if (empty($v['actualQty'])) {
		            return $this->outputQimen('failure', '入库单明细中实收数量不能为空', 'S003');
		        }
		    }
		}
		return $this->outputQimen('success');
	}
}