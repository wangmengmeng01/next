<?php
/**
 * Description: 网易考拉 仓库回调推送理货报告当前处理状态
 * Date: 2018-05-10 16:54
 * Created by XL.
 */
require_once API_ROOT . '/router/interface/erp/kaola/common/erpRequest.php';


class KlTallyStatusInfo extends erpRequest
{

    public function status($params)
    {

        if (empty($params)) {

            return $this->msgObj->outputKaola(false, '仓库回调推送理货报告当前处理状态接口错误：请求数据不能为空');
        }


        $response = $this->send();

        if (!$response['success']) {

            return $this->msgObj->outputKaola(false, '仓库回调推送理货报告当前处理状态接口错误:' . $response['error_msg'], '', $response['inventory']);
        }

        return $this->msgObj->outputKaola(true, '仓库回调推送理货报告当前处理状态转发成功:' . $response['error_msg']);
    }
}
