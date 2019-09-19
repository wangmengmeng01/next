<?php
/**
 * Notes:贝贝查询第三方仓库库存操作类
 * Date: 2019/6/17
 * Time: 22:38
 */
require API_ROOT . 'router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsInventoryQuery extends wmsRequest
{
    /**
     * 库存查询
     */
    public function search($params)
    {
        try {

            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }

            # 转发数据给wms
            $response = $this->send();

            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '', 'wms接口调用失败');
            }
            # 返回数据
            return $this->msgObj->outputBeibei($response['success'], $response['data'],  $response['message'], $response['addon']);

        } catch (Exception $e) {
            return $this->msgObj->outputBeibei(false, '', $e->getMessage());
        }
    }
}