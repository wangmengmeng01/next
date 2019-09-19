<?php
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
/**
 * 奇门数据字典获取接口处理类
 * @author Renee
 *
 */
class wmsMetaDataQuery extends wmsRequest {
    public function query($params) {
        if (empty($params)) {
            $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            $response = $this->send();
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