<?php
/**
 * 奇门库存查询接口(多条件)过滤类
 * @author Renee
 *
 */
class filterStockQuery extends msg
{

    /**
     * 过滤奇门库存查询(多条件)请求数据
     * @param  $requestData         
     * @return array
     *
     */
    public function search(&$requestData)
    {
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
        return $this->outputQimen('success');
    }
}
?>