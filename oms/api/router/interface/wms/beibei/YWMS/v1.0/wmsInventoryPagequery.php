<?php
/**
 * Notes: 贝贝天舟-分页查询第三方库存接口
 * Date: 2019/7/25
 * Time: 11:23
 */
require API_ROOT . 'router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsInventoryPagequery extends wmsRequest
{
    public function query($params)
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