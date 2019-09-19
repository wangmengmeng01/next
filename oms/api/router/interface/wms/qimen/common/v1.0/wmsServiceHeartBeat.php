<?php
/**
 * 奇门心跳接口业务处理类
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsServiceHeartBeat extends wmsRequest
{
	/**
	 * 心跳处理
	 */
	public function heartBeat($params)
	{
		//转发数据给wms
		$response = $this->send();
		//解析返回的数据
		if (!empty($response)) {
			return $this->msgObj->outputQimen($response['flag'], date("Y-m-d H:i:s"), $params['serialNumber'], $response['addon']);
		} else {
			return $this->msgObj->outputQimen('failure', date("Y-m-d H:i:s"), $params['serialNumber']);
		}
	}
}