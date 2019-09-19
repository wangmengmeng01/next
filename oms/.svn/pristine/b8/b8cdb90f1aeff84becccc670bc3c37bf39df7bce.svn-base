<?php
/**
 * Description: 推送理货单审核状态给仓库
 * Date: 2018-05-07 16:28
 * Created by XL.
 */

require_once API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';

class KlPushAuditStatus extends wmsRequest
{

    public function status($params)
    {


        if (empty($params)) {

            return $this->msgObj->outputKaola(false, '推送理货单审核状态给仓库接口错误：请求数据不能为空');
        }

        $response = $this->send();

        if (!$response['success']) {

            return $this->msgObj->outputKaola(false, '推送理货单审核状态给仓库接口错误:' . $response['error_msg'], '', $response['inventory']);
        }

        return $this->msgObj->outputKaola(true, '推送理货单审核状态给仓库接口转发成功');

    }

}