<?php
/**
 * 仓内加工单确认接口过滤类
 * @author Renee
 *
 */
class filterStoreProcessConfirm extends msg 
{

	/**
	 * 仓内加工单确认接口请求信息校验
	 * @param $requestData
	 * @return xml
	 */
	public function confirm(&$requestData)
	{
	    //校验数据是否为空
	    if (empty($requestData)) {
	        return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
	    }
	    $request = $requestData;
        $materialItem = $request['materialitems']['item'];
        $productItem = $request['productitems']['item'];

        //校验加工单编码
        if (empty($request['processOrderCode'])) {
            return $this->outputQimen('failure', '加工单编码不能为空', 'S003');
        }
        //校验外部业务编码  
        if (empty($request['outBizCode'])) {
            return $this->outputQimen('failure', '外部业务编码不能为空', 'S003');
        } 
        //校验单据类型
        if (empty($request['orderType'])) {
            return $this->outputQimen('failure', '单据类型不能为空', 'S003');
        }
        //校验加工单完成时间
        if (empty($request['orderCompleteTime'])) {
            return $this->outputQimen('failure', '加工单完成时间不能为空', 'S003');
        }
        //校验材料明细
        if (empty($materialItem)) {
            return $this->outputQimen('failure', '仓内加工单材料明细不能为空', 'S003');
        } else {
            if (empty($materialItem[0])) {
                $materialItem = array($materialItem);
            }
            foreach ($materialItem as $m_v) {
                if (empty($m_v['itemCode'])) {
                    return $this->outputQimen('failure', '仓内加工单材料明细中系统商品编码不能为空', 'S003');
                }
                if (empty($m_v['quantity'])) {
                    return $this->outputQimen('failure', '仓内加工单材料明细中数量不能为空', 'S003');
                }
            }
        }
        //校验商品明细
        if (empty($productItem)) {
            return $this->outputQimen('failure', '仓内加工单商品明细不能为空', 'S003');
        } else {
            if (empty($productItem[0])) {
                $productItem = array($productItem);
            }
            foreach ($productItem as $i_v) {
                if (empty($i_v['itemCode'])) {
                    return $this->outputQimen('failure', '仓内加工单商品明细中系统商品编码不能为空', 'S003');
                }
                if (empty($i_v['quantity'])) {
                    return $this->outputQimen('failure', '仓内加工单商品明细中数量不能为空', 'S003');
                }
            }
        }
		return $this->outputQimen('success');
	}
}