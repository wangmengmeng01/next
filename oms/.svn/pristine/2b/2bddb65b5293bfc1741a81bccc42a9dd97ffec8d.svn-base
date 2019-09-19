<?php
/**
 * Description: 转库存出库反馈接口 im_5
 * Date: 2018-04-17 11:12
 * Created by XL.
 */

require_once API_ROOT . 'router/interface/erp/custom/common/erpRequest.php';


class erpStockOutFeedBackRequest extends erpRequest
{

    public function feedback($param)
    {

        if (empty($param)) {

            return $this->msgObj->outputCustom(false, '失败：请求数据不能为空');
        }

        try {

            # 转发数据
//            $response = $this->send();
            $response = $this->customSend();

            if ($response['success'] != 'true') {

                return $this->msgObj->outputCustom(false, '转库存出库反馈接口请求失败：' . $response['reasons'], $response['addon']);
            }

            return $this->msgObj->outputCustom(true, '转库存出库反馈接口请求成功', $response['addon']);

        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, '错误：' . $e->getMessage());
        }
    }
}