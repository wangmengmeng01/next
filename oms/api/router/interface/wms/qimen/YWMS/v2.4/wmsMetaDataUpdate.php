<?php
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
/**
 * 奇门数据字典更新接口处理类
 * @author Renee
 *
 */
class wmsMetaDataUpdate extends wmsRequest{
    public function update($params) {
        if (empty($params)) {
            $this->msgObj->outputQimen('failure', '失败：请求数据不能为空', 'S003');
        } else {
            $response = $this->send();
            if (!empty($response)) {
			    return $this->msgObj->outputQimen($response['flag'], $response['message'], $response['code'], $response['addon']);
            } else {
                $this->msgObj->outputQimen('failure', 'wms接口访问异常', 'S003');
            }
        }
    }
}
?>