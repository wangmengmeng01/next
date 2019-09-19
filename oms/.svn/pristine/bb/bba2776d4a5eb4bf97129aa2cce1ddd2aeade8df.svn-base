<?php
/**
 * 奇门心跳接口过滤类
 */
class filterServiceHeartBeat extends msg
{
    /**
     * 数据校验
     */	
	public function heartBeat(&$requestData)
	{
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		//校验序列号
		if (empty($requestData['serialNumber'])) {
			return $this->outputQimen('failure', '序列号不能为空', 'S003');
		}
		
		return $this->outputQimen('success');
	}
}