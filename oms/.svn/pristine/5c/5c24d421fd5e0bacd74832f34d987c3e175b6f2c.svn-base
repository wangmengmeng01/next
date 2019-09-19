<?php
/**
 * Notes: 贝贝是否允许发货接口 （仓库库内作业完成，发货前发起）
 * Date: 2019/6/18
 * Time: 16:30
 */

require API_ROOT . 'router/interface/erp/beibei/common/erpRequest.php';
class erpSoDeliver extends erpRequest
{
    /***
     * Notes:判断是否允许发货
     * Date: 2019/6/18
     * Time: 16:27
     * @param $params
     * @return array
     */
    public function judge($params)
    {
        try {
            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }

            # 转发数据给贝贝
            $response = $this->send();
            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '', '贝贝接口调用失败');
            }
            # 返回数据
            return $this->msgObj->outputBeibei($response['success'], $response['data'],  $response['message'], $response['addon']);

        } catch (Exception $e) {
            return $this->msgObj->outputBeibei(false, '', $e->getMessage());
        }
    }
}

