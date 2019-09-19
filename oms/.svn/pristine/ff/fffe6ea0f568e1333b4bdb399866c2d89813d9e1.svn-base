<?php
/**
 * Description: 拼多多商家取消电子面单
 * User: XL
 * Date: 2019/4/3 0003 14:57
 */

require API_ROOT . 'router/interface/erp/pdd/erpRequest.php';

class pddWaybillCancel extends erpRequest
{

    public function cancel($params)
    {

        if (empty($params)) {

            return $this->outputPdd(0, 'S003', '请求数据不能为空');
        }

        $tokenInfo = $this->getTokenByCustomerCode($params);

        # 发起请求
        $res = $this->send($tokenInfo['access_token'], $params);

        pdd_service::$_pddReturnStr = $res['addon']['return_msg'];


        # 错误消息
        if (isset($res['error_response'])) {

            $errorResponse = $res['error_response'];

            return $this->msgObj->outputPdd(4, $errorResponse['error_code'], $errorResponse['error_msg'], $res['addon']);
        }


        return $this->msgObj->outputPdd(1, '0000', '', $res['addon']);
    }

}