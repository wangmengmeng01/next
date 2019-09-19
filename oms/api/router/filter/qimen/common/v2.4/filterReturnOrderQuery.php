<?php
/**
 * 退货入库单查询接口过滤类
 * @author Renee
 *
 */
class filterReturnOrderQuery extends msg 
{
	/**
	 * 退货入库单查询接口请求信息校验
	 * @param $requestData
	 * @return xml
	 */
	public function search(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		$request = $requestData;
        
        if (empty($request['returnOrderCode'])) {
            return $this->outputQimen('failure', '退货单编码不能为空', 'S003');
        }
        return $this->outputQimen('success');
	}
}