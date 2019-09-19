<?php
/**
 * 奇门出库单查询操作类
 * @author Renee
 *
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';

class wmsStockOutQuery extends wmsRequest
{

    /**
     * 创建入库单查询
     * @param $params         
     * @return array
     */
    public function query($params)
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

