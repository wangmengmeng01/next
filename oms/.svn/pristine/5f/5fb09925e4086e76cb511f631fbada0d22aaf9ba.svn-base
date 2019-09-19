<?php
/**
 * Description: SKU效期信息回调接口
 * Date: 2018-05-23 13:42
 * Created by XL.
 */
require_once API_ROOT . 'router/interface/erp/kaola/common/erpRequest.php';


class KlSkuPeriod extends erpRequest
{


    public function period($params)
    {

        try {

            if (empty($params)) {

                return $this->msgObj->outputKaola(false, 'SKU效期信息回调接口：请求数据不能为空');
            }

            $response = $this->send();

            if (!$response['success']) {

                return $this->msgObj->outputKaola(false, $response['error_msg']);
            }

            return $this->msgObj->outputKaola(true, $response['error_msg']);

        } catch (Exception $e) {

            return $this->msgObj(false, $e->getMessage());
        }

    }
}