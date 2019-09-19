<?php
/**
 * 奇门订单状态批量查询查询业务类
 */
require API_ROOT . '/router/interface/wms/qimen/xzhuang/wmsRequest.php';
class wmsOrderStatusBatchQuery extends wmsRequest
{
	/**
	 * 订单状态批量查询
	 * @param $params
	 */
	public function search($params)
	{
		if (empty($params)) {
			return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
		} else {
			//转发数据给wms
			$response = $this->send();
			//解析返回的数据
			if (!empty($response)) {
				if ($response['flag'] == 'failure' && $response['code'] == 'E001') {
					return $this->msgObj->outputQimen('failure', $response['message'], $response['code'], $response['addon']);
				} else {
					qimen_service::$_queryFlag = true;
				    return $this->msgObj->outputQimen($response['flag'], $response['message'], $response['code'], $response['addon']);
				}
			} else {
				return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
			}
		}
	}
}